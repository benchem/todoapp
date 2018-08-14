package team.benchem.todoapp.aspect;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import team.benchem.todoapp.annotation.RemoteMethodProxy;
import team.benchem.todoapp.lang.CustomerStateCode;
import team.benchem.todoapp.lang.MicroServiceException;
import team.benchem.todoapp.lang.RequestType;
import team.benchem.todoapp.lang.Result;
import team.benchem.todoapp.lang.StateCode;
import team.benchem.todoapp.lang.SystemStateCode;
import team.benchem.todoapp.util.Global;
import team.benchem.todoapp.util.HttpClientHelper;
import team.benchem.todoapp.util.RsaHelper;

@Aspect
public class RemoteMethodProxyAspect {

    private static final String TAG = "RemoteMethodProxyAspect";

    private static final String serviceCenterUrl = "https://ds-suf-uat.xunmu.wang";

    @Around(value = "@annotation(methodProxy)")
    public Object aroundMethod(ProceedingJoinPoint point, RemoteMethodProxy methodProxy){
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        String[] argsNames = methodSignature.getParameterNames();
        Object[] argsValues = point.getArgs();

        HashMap<String, String> requestHeaders = buildRequestHeaders(
                methodProxy.type(),
                methodProxy.serviceKey(),
                methodProxy.path()
        );

        String requestBodyStr = getRequestBodyStr(methodProxy, argsNames, argsValues);
        Result result = getResponseResult(methodProxy, requestHeaders, JSONObject.parseObject(requestBodyStr));
        Type targetType = methodSignature.getMethod().getReturnType();
        if(targetType == void.class){
            return null;
        }
        if(SystemStateCode.OK.getCode().equals(result.getStateCode())) {
            if (targetType == JSONObject.class){
                String jsonStr = JSON.toJSONString(result);
                JSONObject resultObj = JSONObject.parseObject(jsonStr);
                return resultObj.getJSONObject("result");
            } else if (targetType == JSONArray.class) {
                String jsonStr = JSON.toJSONString(result);
                JSONObject resultObj = JSONObject.parseObject(jsonStr);
                return resultObj.getJSONArray("result");
            } else {
                return result.getResult();
            }
        } else {
            StateCode stateCode = new CustomerStateCode(
                    result.getStateCode(),
                    result.getStateCodeName(),
                    result.getMessage()
            );
            throw new MicroServiceException(stateCode);
        }
    }

    private HashMap<String, String> buildRequestHeaders(RequestType requestType, String targetServiceName, String targetServicePath){
        HashMap<String, String> headers = new HashMap<>();

//        String token ;
//        try{
//            token = RsaHelper.publicKeyEncrypt(serviceName, serviceRsaKey);
//        } catch (Exception ex){
//            token = "";
//            Log.w(TAG, "buildRequestHeaders: "+ex.getMessage());
//        }

        String currUser = Global.getInstance().getToken();
        if(currUser != null) {
            headers.put("Suf-Token", currUser);
        }
//        headers.put("Suf-MS-SourceServiceName", serviceName);
//        headers.put("Suf-MS-Token", token);
//        headers.put("Suf-MS-TargetServiceName", targetServiceName);
//        headers.put("Suf-MS-TargetServicePath", targetServicePath);
//        headers.put("Suf-MS-InvokeType", requestType.name());

        return  headers;
    }

    private String getRequestBodyStr(RemoteMethodProxy methodProxy, String[] argsNames, Object[] argsValues) {
        String requestBodyStr = "";
        if(methodProxy.type() == RequestType.POST){
            if(argsValues.length == 0){
                requestBodyStr = "";
            } else if(argsValues.length == 1){
                if(argsValues[0] == null){
                    requestBodyStr = "";
                }else if(argsValues[0].getClass() == JSONObject.class){
                    requestBodyStr = ((JSONObject)argsValues[0]).toJSONString();
                }else if(argsValues[0].getClass() == JSONArray.class){
                    requestBodyStr = ((JSONArray)argsValues[0]).toJSONString();
                }else {
                    JSONObject postBody = new JSONObject();
                    postBody.put(argsNames[0], argsValues[0]);
                    requestBodyStr =postBody.toJSONString();
                }
            } else {
                JSONObject postBody = new JSONObject();
                for(int index = 0; index<argsNames.length; index++) {
                    postBody.put(argsNames[index], argsValues[index]);
                }
                requestBodyStr =postBody.toJSONString();
            }
        } else if (methodProxy.type() == RequestType.GET){
            JSONObject queryParam = new JSONObject();
            for (int index=0; index<argsNames.length; index++){
                queryParam.put(argsNames[index], argsValues[index]);
            }
            requestBodyStr = queryParam.toJSONString();
        }
        return requestBodyStr;
    }

    private Result getResponseResult(final RemoteMethodProxy methodProxy,
                                     final HashMap<String, String> requestHeaders,
                                     final JSONObject requestBody) {

        String redisKey="";
        if(methodProxy.enableCache()){

            redisKey = String.format("%s:%s:%s", methodProxy.type().toString(), methodProxy.serviceKey(), methodProxy.path());
            if(requestHeaders.containsKey("Suf-Token")) {
                redisKey = String.format("%s:%s", redisKey, requestHeaders.get("Suf-Token"));
            }
            redisKey = String.format("%s%s", redisKey, requestBody);

//            if(redisTemplate.hasKey(redisKey)){
//                return JSONObject.parseObject(redisTemplate.opsForValue().get(redisKey), Result.class);
//            }
        }

        final Result result = new Result(SystemStateCode.REMOTECALLING);
        Runnable networkTask = new Runnable() {
            @Override
            public void run() {
                Result remoteResult;
                try{
                    String resultStr = "";
                    if(methodProxy.type() ==  RequestType.GET) {
                        HashMap<String, Object> pararms = new HashMap<>();
                        for(Map.Entry<String, Object> item : requestBody.entrySet()){
                            pararms.put(item.getKey(), item.getValue());
                        }
                        resultStr = HttpClientHelper.get(serviceCenterUrl, methodProxy.path(), requestHeaders, pararms);
                    } else {
                        resultStr = HttpClientHelper.post(serviceCenterUrl, methodProxy.path(), requestHeaders, requestBody.toJSONString());
                    }
                    remoteResult = JSON.parseObject(resultStr).toJavaObject(Result.class);
                } catch (Exception ex){
                    Log.e(TAG, "getResponseResult:" + ex.getMessage());
                    remoteResult = new Result(-105, "微服务中心请求异常:" + ex.getMessage());
                }
                result.setStateCode(remoteResult.getStateCode());
                result.setStateCodeName(remoteResult.getStateCodeName());
                result.setMessage(remoteResult.getMessage());
                result.setResult(remoteResult.getResult());
            }
        };

        new Thread(networkTask).start();
        while(result.getStateCode() == SystemStateCode.REMOTECALLING.getCode()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }


//        if(methodProxy.enableCache()){
//            if( SystemStateCode.OK.getCode().equals(result.getStateCode())) {
//                redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(result), methodProxy.cacheExpireSeconds(), TimeUnit.SECONDS);
//            }
//        }

        return result;
    }
}
