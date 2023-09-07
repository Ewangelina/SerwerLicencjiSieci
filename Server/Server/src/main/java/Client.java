import java.net.Socket;
import java.io.*;

public class Client implements Runnable
{
	private Socket socClient;
	
	private BufferedReader reader;
	public BufferedWriter writer;

	private Checker checker;
	private Server upper;

	private String ip;

	public void quit()
	{
		//sendMessage("bye");
		close();
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
	
	public Client (Socket soc, Checker c, Server s)
	{

		try
		{
			checker = c;
			socClient = soc;
			upper = s;
			ip = soc.getRemoteSocketAddress().toString().split("/")[1];
			ip = ip.split(":")[0];

			reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
		}
		catch (IOException e)
		{
			close();
		}
		
	}

	private void processMessage (String username, String key)
	{

		try
		{
			if (username.equals("bye") || key.equals("bye"))
			{
				sendMessage("bye");
				shutdown(reader, writer, socClient);
			}
			else if (!username.equals("") && ! key.equals(""))
			{
				Integer expiry_sec = checker.checkCredentials(username, key, ip);
				if (expiry_sec == -1)
				{
					sendMessage("Error");
				}
				else
				{
					sendMessage(expiry_sec.toString());
				}
			}
		}
		catch (Exception e)
		{

		}
	}
	
	@Override
	public void run()
	{
		while (socClient.isConnected())
		{
			try
			{
				String username = reader.readLine();
				String key = reader.readLine();
				processMessage(username, key);
			}
			catch (IOException e)
			{
				close();
				break;
			}
		}


		System.out.println("Connection was terminated!");
		upper.disconnect(this);

	}

	public void close()
	{
		shutdown(reader, writer, socClient);
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
