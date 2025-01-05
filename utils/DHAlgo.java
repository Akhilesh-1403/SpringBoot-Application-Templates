import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
 
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
 
public class DHAlgo {
	byte[] ivbyte = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	IvParameterSpec iv = new IvParameterSpec(ivbyte);

	private static BigInteger power(BigInteger a, BigInteger b, BigInteger p)
	{
		return a.modPow(b, p);
	}
	public String encrypt(String key, String input) {
		String encryptedString = null;
		try {
 
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] dig = md.digest(key.getBytes(StandardCharsets.UTF_8));
 
			// 1. Digest secret key (symmetric key) with SHA256
			SecretKeySpec skeySpec = new SecretKeySpec(dig, "AES");
 
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
 
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
 
			// Encryption start
 
			// 2. Encrypt parameters with digest secret key
			byte[] encryptedText = cipher.doFinal(input.getBytes());
 
			// 3. Encode this encrypted parameters with Base64 encode.
			encryptedString = new String(Base64.getEncoder().encode(encryptedText));
		}
		catch(Exception e) {
			System.out.println(e);
		}

		return encryptedString;
	}

	public String decrypt(String key, String input) {
		String decryptedString = null;
		try {
 
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] dig = md.digest(key.getBytes(StandardCharsets.UTF_8));
 
			// 1. Digest secret key (symmetric key) with SHA256
			SecretKeySpec skeySpec = new SecretKeySpec(dig, "AES");
 
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
 
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
 
			// 2. Decode this encrypted parameters with Base64 decode
 
			byte[] basedecodedEncrypted = Base64.getDecoder().decode(input);
 
			// 3.	Decrypt parameters with digest secret key.
			byte[] original = cipher.doFinal(basedecodedEncrypted);
 
			decryptedString =  new String(original);
		}
		catch(Exception e) {
			System.out.println(e);
		}

		return decryptedString;
	}
 
	public static void main(String[] args) {
		BigInteger P, G, x, a, y;
		P = new BigInteger("34524689549219"); //hardcoded (from postman collection PRIME_NUMBER)
		G = new BigInteger("2");  //hardcoded (from postman collection PRIMITIVE_GENERATOR)
		a = new BigInteger("60"); // must be random number not fixed one (from postman collection dh_secret)
	    x = power(G, a, P); // this need to send in body of request_token api as public_key
	    y= new BigInteger("11219703153994"); // this is public_key received in request_token api response
	    String secretekey = power(y, a, P).toString(); //this need to calculate to encrypt/decrypt data
	    System.out.println("secretekey: " +secretekey);
	    DHAlgo dh = new DHAlgo();
	    String encrypteddata = dh.encrypt(secretekey, "{\"otp_key\":\"FZFVHEYRXTTB67XBXCNSOTRA272MSOHM\",\"admin_id\":42,\"email_id\":\"mpin@yopmail.com\",\"auth_key\":\"VWFLCGCOTNCGOJUJTBNACMVTYAPSQK\"}\"");
	    encrypteddata = encrypteddata.replaceAll("/[^a-z0-9+/=]/gi", "").replace("\n", "");  //remove \n from response
	    System.out.println("encrypteddata: " +encrypteddata);
	    String decryptededdata = dh.decrypt(secretekey, encrypteddata);
	    System.out.println("decryptededdata: " +decryptededdata);
 
	}
 
}
