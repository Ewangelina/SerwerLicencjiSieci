import java.util.HashMap;
import java.util.Scanner;

import org.json.*;

import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressSeqRange;
import inet.ipaddr.IPAddressString;

import java.io.File;
import java.net.InetAddress;

public class Checker
{
    Gui display;
    HashMap<String, Licence> db;

    public Checker(Gui display)
    {
        this.display = display;
        db = new HashMap<String, Licence>();
    }
    
    public boolean loadJson(String filename)
    {
        try
        {
            File fileObject = new File(filename);
            Scanner scanner = new Scanner(fileObject);
            String input = "";
            while (scanner.hasNextLine())
            {
                input += scanner.nextLine() + "\n";
            }
            scanner.close();
            JSONObject inputJson = new JSONObject(input);

            JSONArray array = (JSONArray) inputJson.get("licences");
            db = new HashMap<String, Licence>();

            for (Object one : array)
            {
                JSONObject jsonLicence = (JSONObject) one;
                Licence licence = new Licence(Hasher.md5((String) jsonLicence.get("LicenceUserName")), (Integer) jsonLicence.get("Licence"), (Integer) jsonLicence.get("ValidationTime"), (JSONArray) jsonLicence.get("IPadresses"));
                db.put((String) jsonLicence.get("LicenceUserName"), licence);        
            }

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public Integer checkCredentials(String username, String key, String ipAddress)
    {
        Licence licence = db.get(username);

        if (licence == null)
        {
            String message = "Refused a licence to " + username;
            display.add_server_response(message);
            return -1;
        }

        if (!checkIP(licence.getIpRange(), ipAddress))
        {
            String message = "Refused a licence to " + username + " because of IP " + ipAddress;
            display.add_server_response(message);
            return -1;
        }

        if(licence.match_key(key))
        {
            if (licence.withdrawLicence())
            {
                String message;
                if (!licence.setExpiration())
                {
                    message = "Granted an INFINITE licence to " + username;
                }
                else
                {
                    message = "Granted a licence to " + username;
                }
                display.add_server_response(message);
                return licence.getExpiry_sec();
            }
            else
            {
                String message = "Refused a licence to " + username + " due to licence limit";
                display.add_server_response(message);
                return -1;
            }
        }
        else
        {
            String message = "Refused a licence to " + username + " because of key " + key;
            display.add_server_response(message);
            return -1;
        }
    }

    //https://chat-gpt.org/chat
    //Create a method in Java that uses InetAddress class to compare the IP address with the range in the CIDR format.
    public static boolean isIPAddressInRange(String ipAddress, String cidr) {
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            byte[] addressBytes = address.getAddress();
            int addressInt = ((((int) addressBytes[0]) & 0xFF) << 24) |
                    ((((int) addressBytes[1]) & 0xFF) << 16) |
                    ((((int) addressBytes[2]) & 0xFF) << 8) |
                    (((int) addressBytes[3]) & 0xFF);
 
            String[] cidrParts = cidr.split("/");
            InetAddress cidrAddress = InetAddress.getByName(cidrParts[0]);
            byte[] cidrAddressBytes = cidrAddress.getAddress();
            int cidrMask = Integer.parseInt(cidrParts[1]);
            int cidrInt = ((((int) cidrAddressBytes[0]) & 0xFF) << 24) |
                    ((((int) cidrAddressBytes[1]) & 0xFF) << 16) |
                    ((((int) cidrAddressBytes[2]) & 0xFF) << 8) |
                    (((int) cidrAddressBytes[3]) & 0xFF);
            int cidrMaskInt = 0xFFFFFFFF << (32 - cidrMask);
            return (addressInt & cidrMaskInt) == (cidrInt & cidrMaskInt);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkIP(JSONArray cidr, String ip)
    {
        try
        {
            for (Object one : cidr)
            {
                String cidrPart = (String) one;
                if (cidrPart.equals("any")) return true;
                if (isIPAddressInRange(ip, cidrPart)) return true;
            }

            return false;
        }
        catch (Exception e)
        {
            return false;
        }       
    }
}
