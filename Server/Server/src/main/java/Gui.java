import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;

public class Gui
{
    JLabel upper_label;
    String server_response = "";
    JButton exit;
    JTextArea main_area;
    JTextField text_form;
    Integer recieving_port = 0;
    String text;
    public static BufferedWriter writer;
    Server server;

    public void add_server_response(String server_response)
    {
        this.server_response += server_response + "\n";
        main_area.setText(this.server_response);
    }

    public void status_custom(String status)
    {
        upper_label.setText(status);
    }


    Gui(Server s)
    {
        server = s;

        //Creating the Frame
        JFrame frame = new JFrame("Licencjomat - serwer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        //Creating the panel at bottom and adding components
        JPanel lower_panel = new JPanel();
        JPanel upper_panel = new JPanel();
        String message = "Waiting for Connector to initialise";
        upper_label = new JLabel(message);
        exit = new JButton("QUIT");
        upper_panel.add(upper_label);
        upper_panel.add(exit);


        // Text Area at the Center
        main_area = new JTextArea();

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, lower_panel);
        frame.getContentPane().add(BorderLayout.NORTH, upper_panel);
        frame.getContentPane().add(BorderLayout.CENTER, main_area);
        frame.setVisible(true);

        exit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                server.quit();
            }
        });
    }
}