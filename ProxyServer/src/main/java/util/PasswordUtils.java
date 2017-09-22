package util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class PasswordUtils {
	
	private static Base64 base64 = new Base64();
	private static Hex hex = new Hex();
	
	
	//base64加密
	public static String base64Encrypt(String message) {
		try {
			return base64.encodeToString(message.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	//base64解密
	@SuppressWarnings("static-access")
	public static String base64Decrypt(String message) {
		byte[] data = base64.decodeBase64(message);
		return new String(data);
	}
	
	//Hex加密
	public static String hexEncrypt(String message) {
		try {
			return Hex.encodeHexString(message.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	//Hex解密
	public static String hexDecrypt(String message) {
		try {
			byte[] data = hex.decode(message.getBytes("UTF-8"));
			return new String(data);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//md5加密
	public static String md5Encrypt(String message) {
		try {
			return DigestUtils.md5Hex(message.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//sha加密
	@SuppressWarnings("deprecation")
	public static String shaEncript(String message) {
		try {
			return DigestUtils.shaHex(message.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	
	

}
