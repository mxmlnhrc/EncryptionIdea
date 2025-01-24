import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class NumberEncryption {

    // Method to generate a secret key
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // for AES-128
        return keyGen.generateKey();
    }

    // Method to encrypt a number
    public static BigInteger encrypt(int number, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(String.valueOf(number).getBytes());
        String encryptedString = Base64.getEncoder().encodeToString(encryptedBytes);
        char[] encryptedChars = encryptedString.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < encryptedChars.length; i++) {
            int c = (int) encryptedChars[i];
//            System.out.print((char) c + " = " +c + ";");
            stringBuilder.append(300 - c);
        }
        String numberEncryptedString = stringBuilder.toString();
        return new BigInteger(numberEncryptedString);
    }

    // Method to decrypt a number
//    public static int decrypt(String encryptedNumber, SecretKey key) throws Exception {
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.DECRYPT_MODE, key);
//        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedNumber));
//        return Integer.parseInt(new String(decryptedBytes));
//    }

    public static int decrypt(BigInteger encryptedNumber, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        //Generate encrypted String from Integer
        StringBuilder stringBuilder = new StringBuilder();
        String encryptedString = encryptedNumber.toString();

        List<String> result = new ArrayList<>();
        int lenght = encryptedString.length();
        for(int i = 0; i<lenght; i+=3) {
            result.add(encryptedString.substring(i, i+3));
        }
//        System.out.println(result);
        for(int i = 0; i<result.size(); i++) {
            int number = 300 - Integer.parseInt(result.get(i));
//            System.out.print((char) number + " = " +number + ";");
            stringBuilder.append((char) number);
        }

        System.out.println();


//            return 1;
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(stringBuilder.toString()));
        return Integer.parseInt(new String(decryptedBytes));
    }


    public static void main(String[] args) {
        try {
            SecretKey key = generateKey();
            int numberToEncrypt = 1234;
            BigInteger encryptedNumber = encrypt(numberToEncrypt, key);
            int decryptedNumber = decrypt(encryptedNumber, key);

            System.out.println("Original Number: " + numberToEncrypt);
            System.out.println("Encrypted Number: " + encryptedNumber);
            System.out.println("Decrypted Number: " + decryptedNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}