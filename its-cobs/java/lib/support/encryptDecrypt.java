package lib.support;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import org.apache.commons.codec.binary.Hex;

public class encryptDecrypt {
	private static MessageDigest md;
	static {
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			md = null;
		}
	}

	private static byte[] md5(String source) {
		byte[] bytes = source.getBytes();
		byte[] digest = md.digest(bytes);
		return digest;
	}

	public static String encrypt(String input, String key) {
		byte[] ivbytes = "sixteenbyteslong".getBytes();
		IvParameterSpec ips = new IvParameterSpec(ivbytes);
		System.out.println("plaintext: " + input);
		byte[] keybytes = md5(key);
		System.out.println("Key: " + Hex.encodeHexString(keybytes));
		System.out.println("Ivs: " + Hex.encodeHexString(ivbytes));
		byte[] crypted = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(keybytes, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey, ips);
			byte[] ptext = input.getBytes();
			crypted = cipher.doFinal(ptext);
		} catch (Exception e) {
		}
		return new String(Hex.encodeHexString(crypted));
	}

	public static String decrypt(String input, String key) {
		IvParameterSpec ips = new IvParameterSpec("sixteenbyteslong".getBytes());
		byte[] keybytes = md5(key);
		// System.out.println("key : " + Hex.encodeHexString(keybytes));
		byte[] output = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(keybytes, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skey, ips);
			output = cipher.doFinal(Hex.decodeHex(input.toCharArray()));
		} catch (Exception e) {
		}
		return new String(output);
	}
	
	public static boolean checkEncrypt(String input, String key) {
		try {
			encrypt(input, key);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean checkDecrypt(String input, String key) {
		try {
			decrypt(input, key);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		String input = JOptionPane.showInputDialog("Input");
		System.out.println(encryptDecrypt.encrypt(input, "thebestsecretkey"));
		//System.out.println(encryptDecrypt.decrypt(input, "thebestsecretkey"));
		//System.out.println(checkDecrypt(input, "thebestsecretkey"));
	}
}
