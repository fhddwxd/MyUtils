package com.wxd.myutils.Utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Encoder;

/**加密工具类
 *
 */
public class EncryptUtil {

	public EncryptUtil() {
		
	}
	public static String keyString;
	private static EncryptUtil instance = null;
	
	public static EncryptUtil getInstance() {
		if (instance == null)
			instance = new EncryptUtil();
		return instance;
	}
	
	/*****************MD5******************/
	/**MD5加密
	 * @param plainStr  原始字符串
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5Encrypt(String plainStr) throws NoSuchAlgorithmException {
		String result=null;
		MessageDigest md= MessageDigest.getInstance("MD5");
		md.update(plainStr.getBytes());
		byte b[] = md.digest();
		String buf = getString(b);
		System.out.println("result: " + buf);//32位的加密
		return buf;
	}
	
	/** 
     * 加密解密算法 执行一次加密，两次解密 
     */   
    public static String convertMD5(String inStr){
  
        char[] a = inStr.toCharArray();  
        for (int i = 0; i < a.length; i++){  
            a[i] = (char) (a[i] ^ 't');  
        }  
        String s = new String(a);
        return s;  
  
    } 
    
	
	public static String getString(byte[] b){
		int i;
		StringBuffer buf =new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset]; if(i<0) i+= 256;
			if(i<16) buf.append("0"); 
			buf.append(Integer.toHexString(i));
			} 
		return buf.toString();
	}

	/********************AES 256****************************/
	/**AES 256加密
	 * @param strKey密钥
	 * @param plainStr 要加密字符串
	 * @param strIv 初始向量
	 * @return 返回加密结果字符串
	 * @throws Exception
	 */
	public static String aesEncrypt(String plainStr)
			throws Exception {
//		plainStr =md5Encrypt(plainStr);//MD5处理
		keyString = getRandomString();//取得随机数
		keyString="yTKqSTqPlkC90VXFZVuQMLjL63ReLK9q";
		byte[] raw = keyString.getBytes();
		SecretKey secretKey = new SecretKeySpec(raw,"AES");
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher= Cipher.getInstance("AES/ECB/PKCS7Padding","BC");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] data = cipher.doFinal(plainStr.getBytes());
        return new BASE64Encoder().encode(data);
	}
	
	/***********************base64*************************/
	/**base64解码
	 * @param string
	 * @return
	 */
	public static String base64Decrypt(String finalText){
		byte[] decodedBytes= Base64_.decode(finalText);
		return new String(decodedBytes);
//		finalText= Base64.encodeToString(finalText.getBytes(),Base64.DEFAULT);
//		return finalText;
	}
	
	/**base64加密
	 * @param string
	 * @return
	 */
	public static String base64Encrypt(String string){
		return Base64_.encode(string.getBytes());
//		return new String(Base64.encodeToString(string.getBytes(),Base64.NO_PADDING));
//		return new String(android.util.Base64.decode(string,Base64.DEFAULT));
	}
	
	/**   
     * 字符串转换成十六进制字符串  
     * @param String str 待转换的ASCII字符串  
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]  
     */      
    public static String str2HexStr(String str)
    {      
        char[] chars = "0123456789ABCDEF".toCharArray();      
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();      
        int bit;      
        for (int i = 0; i < bs.length; i++)    
        {      
            bit = (bs[i] & 0x0f0) >> 4;      
            sb.append(chars[bit]);      
            bit = bs[i] & 0x0f;      
            sb.append(chars[bit]);    
//            sb.append(' ');    
        }      
        return sb.toString().trim();      
    }
    
    /**
	 * 转化十六进制编码为字符串
	 * @param s 十六进制的字符串
	 * @return 字符串
	 */
	public static String toStringHex(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "UTF-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}
    
    /**
     * 加密输入的信息
     * @param input 
     * 此处input为"MERID=00000000000078&USER_ID=admin78"这样的请求内容
     * @return 加密之后的字符串
     */
	public static String requestSecret(String input){
        try {
//        	String input1 = input + mdString;//加写死的字符串
        	input=base64Encrypt(input);
            String sign = md5Encrypt(input);//生成md5加签值
            input += "&SIGN=" + sign;//加上加签值
            input = str2HexStr(input);//转化成16进制
            input = base64Encrypt(input);//转换成base64编码
			input = java.net.URLEncoder.encode(input,"UTF-8");//UrlEncode处理
		} catch (Exception e) {
			e.printStackTrace();
		}
        return input;
    }
    
    /**
	 * 解密返回的信息
	 * 
	 * @param input
	 * 此处input为服务器加密之后的内容
	 * @return
	 */
	public static String decryptSecret(String input) {
		try {
			input = java.net.URLDecoder.decode(input, "UTF-8");// UrlEncode处理
			input = base64Decrypt(input);// base64解码toStringHex
			input = toStringHex(input);// 16进制转成字符串
			int idx = input.indexOf("SIGN=");
			String input2 = input.substring(idx, input.length());//取得加签值
			idx = input2.indexOf("=");
			input2 = input2.substring(idx+1, input2.length());//取得加签值
			idx = input.indexOf("SIGN=");
			input = input.substring(0, idx);//去掉加签值
			String sign = md5Encrypt(input);// 生成md5加签值
			
			if(!sign.equals(input2)){
				input = "ABNORMAL" ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return base64Decrypt(input);
	}
    
    /**
     * 生成随机字符串的方法
     * @param length
     * @return 返回一个长32位的随机数组
     */
    public static String getRandomString() { //length表示生成字符串的长度
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";   //生成字符串从此序列中取
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 32; i++) {   
            int number = random.nextInt(base.length());   
            sb.append(base.charAt(number));   
        }   
        return sb.toString();   
     }
    
    public static String testMd5(String pwd){
		String str="";
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(pwd.getBytes());
			str = HexCodec.hexEncode(md5.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return str;
	}
    /** 
	 * bitmap转为base64 
	 * @param bitmap 
	 * @return 
	 */  
	public static String bitmapToBase64(Bitmap bitmap) {
	  
	    String result = null;
	    ByteArrayOutputStream baos = null;
	    try {  
	        if (bitmap != null) {  
	            baos = new ByteArrayOutputStream();
	            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
	  
	            baos.flush();  
	            baos.close();  
	  
	            byte[] bitmapBytes = baos.toByteArray();  
	            result = org.kobjects.base64.Base64.encode(bitmapBytes);
	            result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
	        }  
	    } catch (IOException e) {
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            if (baos != null) {  
	                baos.flush();  
	                baos.close();  
	            }  
	        } catch (IOException e) {
	            e.printStackTrace();  
	        }  
	    }  
	    return result;  
	}
	//MD5加密
	public static String getmd5(String string) {
		byte[] hash;		
		try {
		    hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
		    throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
		    throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}
	    StringBuilder hex = new StringBuilder(hash.length * 2);
	    for (byte b : hash) {	
	        if ((b & 0xFF) < 0x10) hex.append("0");	
	        hex.append(Integer.toHexString(b & 0xFF));
	    }

	    return hex.toString().toLowerCase();

	}
}
