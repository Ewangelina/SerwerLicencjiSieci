import java.net.Socket;
import java.io.*;
import java.util.*; 


public class User
{
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;

	private String username;
	private String key;

	private Thread expirerThread = null;
	private Expirer ex = null;
	private Gui display;

	private Scanner sc;

	public User (Socket soc) throws Exception
	{
		sc = new Scanner(System.in);
		try
		{
			socket = soc;
			
			reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
		}
		catch (IOException e)
		{
			close();
		}
	}

	public void setGui(Gui gui)
	{	
		display = gui;
	}

	public String presentUserInfo()
	{
		return "Username: " + username + " Key: " + key;
	}

	public void quit()
	{
		try
		{
			stop();
		}
		catch (Exception e)
		{

		}
		
		close();
		sc.close();
		System.exit(0);
	}

	public void setScanner(Scanner sc)
	{
		this.sc = sc;
	}

	public void stop()
	{
		try
		{
			ex.turnOff();
			expirerThread.interrupt();
			expirerThread = null;
		}
		catch (Exception e)
		{

		}

		display.add_server_response("Stopped renewing licence");
	}

	public void inputCredentials()
	{
		System.out.println("Insert your username");
		username = sc.nextLine();

		System.out.println("Insert your key");
		key = sc.nextLine();
	}

	public void sendRequest()
	{
		try
		{
			ex.turnOff();
			expirerThread.interrupt();
			expirerThread = null;
		}
		catch (Exception e)
		{

		}

		sendMessage(username);
		sendMessage(key);
		String answer;
		try
		{
			answer = reader.readLine();
		}
		catch (Exception e)
		{
			display.add_server_response("EXITED");
			close();
			return;
		}
		
		if (answer.equals("bye"))
		{
			display.add_server_response("EXITED");
			close();
			return;
		}

		if (answer.equals("Error"))
		{
			display.add_server_response("Couldn't get licence");
			return;
		}

		Integer secondsToExpiry;
		try
		{
			secondsToExpiry = Integer.parseInt(answer);
		}
		catch (Exception e)
		{
			display.add_server_response("Response error");
			return;
		}

		if (secondsToExpiry > 0)
		{
			ex = new Expirer(secondsToExpiry, this);
			expirerThread = new Thread(ex);
			expirerThread.start();
			String message = "Got licence for " + secondsToExpiry + " seconds";
			display.add_server_response(message);

		}
		else
		{
			String message = "Got an INFINITE licence";
			display.add_server_response(message);
		}
	}


	public void sendMessage (String message)
	{
		try
		{
			writer.write(message);
			writer.newLine();
			writer.flush();
		}
		catch (Exception e)
		{
			close();
		}
	}



	public static void main (String [] args)
	{
		User main;
		try
		{
			String[] server = DiscoveryClient.getServerAddress().split(":");
			main = new User(new Socket(server[0], Integer.parseInt(server[1])));
		}
		catch (Exception e)
		{
			System.out.println("Connection error");
			System.out.println(e.getMessage());
			return;
		}
		

		//main.inputCredentials();
		//main.sendRequest();
		Gui display = new Gui(main);
		main.setDisplay(display);

	}

	public void setDisplay(Gui gui)
	{
		display = gui;
	}

	public void close ()
	{
		shutdown(reader, writer, socket);
	}

	public static void shutdown (BufferedReader re, BufferedWriter wr, Socket soc)
	{
		try
		{
			if (re != null)
			{
				re.close();
			}

			if (wr != null)
			{
				wr.close();
			}

			if (soc != null)
			{
				soc.close();
			}
		}
		catch (Exception e)
		{
			System.out.println("Oops! Error 3");
		}

	}
}
