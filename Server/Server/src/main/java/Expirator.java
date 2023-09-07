import java.util.concurrent.TimeUnit;

public class Expirator implements Runnable
{
    private Licence licence;
    private Integer time_sec;

    public Expirator(Licence licence, Integer time_sec)
    {
        this.licence = licence;
        this.time_sec = time_sec;
    }

    @Override
    public void run()
    {
        try
        {
            TimeUnit.SECONDS.sleep(time_sec);
        }
        catch (Exception e)
        {

        }

        licence.returnLicence();
    }
}
