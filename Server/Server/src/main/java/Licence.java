import org.json.*;

public class Licence
{
    String key;
    volatile int no_licences;
    Integer expiry_sec;
    JSONArray ips;

    Licence(String key, Integer no_licences, Integer expiry_sec, JSONArray cidr)
    {
        this.key = key;
        this.no_licences = no_licences;
        this.expiry_sec = expiry_sec;
        ips = cidr;
    }

    public boolean setExpiration()
    {
        if (expiry_sec <= 0) return false;
        Expirator e = new Expirator(this, expiry_sec);
        Thread t = new Thread(e);
        t.start();
        return true;
    }

    public boolean match_key(String key)
    {
        return this.key.equals(key);
    }

    public Integer getExpiry_sec()
    {
        if (expiry_sec <= 0) return 0;
        return expiry_sec;
    }

    public JSONArray getIpRange()
    {
        return ips;
    }

    public synchronized boolean withdrawLicence()
    {
        if (no_licences <= 0)
        {
            return false;
        }
        else
        {
            no_licences--;
            return true;
        }
    }

    public synchronized void returnLicence()
    {
        no_licences++;
    }
}
