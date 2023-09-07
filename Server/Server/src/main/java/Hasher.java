//https://www.baeldung.com/java-md5

import org.apache.commons.codec.digest.DigestUtils;

public class Hasher 
{
    public static String md5(String input)
    {           
        String md5Hex = DigestUtils.md5Hex(input).toUpperCase();
        return md5Hex;
    }

    public static void main(String[] args)
    {
        System.out.println(Hasher.md5("Guest"));
        
    }
}
