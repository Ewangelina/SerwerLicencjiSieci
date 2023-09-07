import java.util.Collections;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
public class Server
{
	private List<Client> list;
	private Gui display;
	private Thread connectorThread;
	private Connector connector;


	public void quit()
	{
		for(Client c : list)
		{
			c.quit();
		}
		connector.quit();
	}

	public void disconnect(Client c)
	{
		list.remove(c);
	}

	public void setDisplay(Gui display)
	{
		this.display = display;
	}
	public void setConnector(Connector connector)
	{
		this.connector = connector;
	}

	public void setConnectorThread(Thread connectorThread)
	{
		this.connectorThread = connectorThread;
	}

	public static void main (String [] args)
	{
		Server serwer = new Server();
		Gui display = new Gui(serwer);
		Checker checker = new Checker(display);
		if (!checker.loadJson("filename.json"))
		{
			System.out.println("JSON file ERROR");
			return;
		}
		Connector connector = new Connector(serwer, checker, display);
		Thread t = new Thread(connector);
		t.start();
		serwer.setConnectorThread(t);
		serwer.setDisplay(display);
		serwer.setConnector(connector);

	}

	public void addClient(Client c)
	{
		list.add(c);
	}
	
	public Server ()
	{
		list = Collections.synchronizedList(new ArrayList<Client>());
	}
	

}
