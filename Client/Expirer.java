import java.util.concurrent.TimeUnit;

public class Expirer implements Runnable
{
    Integer seconds;
    User upper;
    Boolean on = true;
    public Expirer(Integer seconds, User user)
    {
        this.seconds = seconds;
        upper = user;
    }

    public void turnOff()
    {
        on = false;
    }
    
    @Override
    public void run()
    {
        try
        {
            TimeUnit.SECONDS.sleep(seconds);
            if (on)
            {
               upper.sendRequest(); 
            }
            
        }
        catch (InterruptedException e)
        {
            return;
        }     
    }
}
