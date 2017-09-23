package util;

public class HashUtil {
	
	public static String bytesToHex(byte[] hash) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<hash.length; i++) {
			int temp = hash[i];
			temp &= 0xff;
			if(temp < 10) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(temp));
		}
		return sb.toString();
	}
	
	

}
