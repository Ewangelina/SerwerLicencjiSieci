import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import server.DiscoveryServer;

public class Connector implements Runnable
{
    private ServerSocket socServer;
    private Server upper;
    private Checker checker;
    private Integer port;
    private Gui display;

    Connector (Server s, Checker c, Gui display)
    {
        upper = s;
        checker = c;
        this.display = display;
    }

    public void quit()
    {
        try
        {
            socServer.close();
        }
        catch (Exception e)
        {

        }

        System.exit(0);
    }

    public Integer getPort()
    {
        return port;
    }


    @Override
    public void run()
    {
        port = 1111;
		while (true)
		{
			try
			{
				ServerSocket serverSocket = new ServerSocket(port);
				serverSocket.close();
				break;
			}
			catch (Exception e)
			{
                port++;
                if (port >= 9999)
                {
                    Scanner input = new Scanner(System.in);
                    while (true)
                    {
                        System.out.println("Could not establish a server port automatically please enter an open port");
                        String in = input.nextLine();

                        try
                        {
                            port = Integer.parseInt(in);
                            ServerSocket serverSocket = new ServerSocket(port);
                            serverSocket.close();
                            input.close();
                            break;
                        }
                        catch (Exception ex)
                        {

                        }
                    }
                }
			}
		}

        String message = "Server accepting connections on port " + port.toString();
        display.status_custom(message);

        try
        {
            socServer = new ServerSocket(port);
            DiscoveryServer ds = new DiscoveryServer(port.toString());
            Thread t = new Thread(ds);
		    t.start();
            System.out.println("Server now running!");
            waitForConnection();
        }
        catch (IOException e)
        {
            System.out.println("Oops! Error 2");
            System.out.println("Is a Server already running?");
        }

    }

    private void waitForConnection ()
    {
        try
        {
            while (!socServer.isClosed())
            {
                Socket temp = socServer.accept();
                System.out.println("New connection made!");

                Client newCli = new Client(temp, checker, upper);
                upper.addClient(newCli);

                Thread thr = new Thread(newCli);
                thr.start();


            }
        }
        catch (IOException e)
        {
            try
            {
                if (socServer != null)
                {
                    socServer.close();
                }
            }
            catch (IOException f)
            {
                System.out.println("Error 1");
            }
        }
    }
}
