package team.benchem.todoapp.util;

import android.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaHelper {
    public static final String CHAR_SET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";
    public static final int KEY_SIZE = 1024;
    public static final String PUBLICKEY_NAME = "publicKey";
    public static final String PRIVATE_NAME = "privateKey";

    /**
     * 公钥加密
     *
     * @param data         加密内容
     * @param publicKeyStr 公钥字符串
     * @return 加密后BASE64编码的字符串
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String publicKeyEncrypt(String data, String publicKeyStr) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] dataBytes = data.getBytes();
        RSAPublicKey rsaPublicKey = transToPublicKey(publicKeyStr);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
        byte[] encryptDataBytes = cipher.doFinal(dataBytes);

        //return new String(Base64.encode(encryptDataBytes, Base64.URL_SAFE));
        //return Base64.encodeToString(encryptDataBytes, Base64.NO_WRAP | Base64.NO_PADDING);
        //return jodd.util.Base64.encodeToString(cipher.doFinal(dataBytes));
        return Base64.encodeToString(cipher.doFinal(dataBytes), Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
    }


    /**
     * 公钥解密
     *
     * @param data        加密后BASE64编码的字符串
     * @param publikeyStr 公钥字符串
     * @return 加密前字符串
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String publicKeyDecrypt(String data, String publikeyStr) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] dataBytes = Base64.decode(data.getBytes(), Base64.URL_SAFE);
        RSAPublicKey rsaPublicKey = transToPublicKey(publikeyStr);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, rsaPublicKey);
        return new String(cipher.doFinal(dataBytes));
    }


    /**
     * 私钥解密
     *
     * @param data       加密后BASE64编码的字符串
     * @param privateStr 私钥字符串
     * @return 加密前字符串
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String privateKeyDecrypt(String data, String privateStr) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] dataBytes = Base64.decode(data.getBytes(), Base64.URL_SAFE);
        RSAPrivateKey rsaPrivateKey = transToPrivateKey(privateStr);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
        return new String(cipher.doFinal(dataBytes));
    }

    /**
     * 私钥加密
     *
     * @param data       加密内容
     * @param privateStr 私钥字符串
     * @return 加密后BASE64编码的字符串
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String privateKeyEncrypt(String data, String privateStr) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] dataBytes = data.getBytes();
        RSAPrivateKey rsaPrivateKey = transToPrivateKey(privateStr);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, rsaPrivateKey);
        return Base64.encodeToString(cipher.doFinal(dataBytes), Base64.URL_SAFE);
    }

    /**
     * 转换私钥字符串
     *
     * @param privateKeyStr BASE64编码后的私钥字符串
     * @return 私钥对象
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static RSAPrivateKey transToPrivateKey(String privateKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        // 通过PKCS#8编码的Key指令获取私钥对象
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKeyStr, Base64.URL_SAFE));
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    /**
     * 转换公钥字符串
     *
     * @param publicKeyStr BASE64编码后的公钥字符串
     * @return 公钥对象
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static RSAPublicKey transToPublicKey(String publicKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        // 通过X509编码的Key指令获得公钥对象
        X509EncodedKeySpec x509EncodedKeySpec = null;
        try {

            x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decode(publicKeyStr.getBytes("UTF-8"), Base64.URL_SAFE));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return (RSAPublicKey) keyFactory.generatePublic(x509EncodedKeySpec);
    }
}
