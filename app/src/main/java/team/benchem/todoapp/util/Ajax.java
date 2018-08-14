package team.benchem.todoapp.util;


import com.alibaba.fastjson.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import team.benchem.todoapp.lang.RequestType;

public class Ajax {

    private static String baseHost;

    public static String getBaseHost() {
        return baseHost;
    }

    public static void setBaseHost(String baseHost) {
        Ajax.baseHost = baseHost;
    }

    public static Ajax get(String url, Map<String, Object> params) throws MalformedURLException {
        return new Ajax(url, null, params);
    }

    public static Ajax get(String url, Map<String, String> requestHeader, Map<String, Object> params) throws MalformedURLException {
        return new Ajax(url, requestHeader, params);
    }

    public static Ajax post(String url, JSONObject requestBody) throws MalformedURLException {
        return new Ajax(url, null, requestBody);
    }

    public static Ajax post(String url, Map<String, String> requestHeader, JSONObject requestBody) throws MalformedURLException {
        return new Ajax(url, requestHeader, requestBody);
    }

    public void subscribe(final ResponseHandler handler){

        Runnable networkRequest = new Runnable() {
            @Override
            public void run() {
                String responseStr;
                try {
                    if (requestType == RequestType.GET) {
                        responseStr = HttpClientHelper.get(requestHost, requestPath, requestHeader, params);
                    } else {
                        responseStr = HttpClientHelper.post(requestHost, requestPath, requestHeader, requestBody == null ? "" : requestBody.toJSONString());
                    }
                }catch (Exception ex){
                    if(handler != null){
                        handler.onError(ex);
                    }
                    return;
                }

                if(handler != null){
                    handler.onResponse(responseStr);
                }
            }
        };

        Thread bgThread = new Thread(networkRequest);
        bgThread.start();
    }

    public interface ResponseHandler {

        void onResponse(String responseStr);

        void onError(Exception ex);

    }

    private final String requestHost;
    private final String requestPath;
    private final Map<String, String> requestHeader;
    private final RequestType requestType;
    private final Map<String, Object> params;
    private final JSONObject requestBody;

    private Ajax(String url, Map<String, String> requestHeader, Map<String, Object> params) throws MalformedURLException {
        requestType = RequestType.GET;
        this.requestHeader = requestHeader;
        if(url.toLowerCase().startsWith("http")){
            URL mUrl = new URL(url);
            if(mUrl.getPort() == -1) {
                requestHost = String.format("%s://%s", mUrl.getProtocol(), mUrl.getHost());
            } else {
                requestHost = String.format("%s://%s:%s", mUrl.getProtocol(), mUrl.getHost(), mUrl.getPort());
            }
            requestPath = mUrl.getPath();
        } else {
            requestHost = getBaseHost();
            requestPath = url;
        }
        this.params = params;
        requestBody = null;
    }

    private Ajax(String url, Map<String, String> requestHeader, JSONObject requestBody) throws MalformedURLException {
        requestType = RequestType.POST;
        this.requestHeader = requestHeader;
        if(url.toLowerCase().startsWith("http")){
            URL mUrl = new URL(url);
            if(mUrl.getPort() == -1) {
                requestHost = String.format("%s://%s", mUrl.getProtocol(), mUrl.getHost());
            } else {
                requestHost = String.format("%s://%s:%s", mUrl.getProtocol(), mUrl.getHost(), mUrl.getPort());
            }
            requestPath = mUrl.getPath();
        } else {
            requestHost = getBaseHost();
            requestPath = url;
        }
        params = null;
        this.requestBody = requestBody;
    }
}
