
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Gui 
{
    JLabel upper_label;
    String server_response = "";
    JButton exit;
    JButton stop;
    JButton getLicence;
    JButton setCredentials;
    JTextArea main_area;
    User user;

    public void add_server_response(String server_response)
    {
        this.server_response += server_response + "\n";
        main_area.setText(this.server_response);
    }

    public void status_custom(String status)
    {
        upper_label.setText(status);
    }


    Gui(User u)
    {
        user = u;

        //Creating the Frame
        JFrame frame = new JFrame("Licencjomat - user");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        //Creating the panel at bottom and adding components
        JPanel lower_panel = new JPanel();
        JPanel upper_panel = new JPanel();
        upper_label = new JLabel(user.presentUserInfo());
        exit = new JButton("QUIT");
        stop = new JButton("STOP");
        getLicence = new JButton("Get Licence");
        setCredentials = new JButton("Set Credentials");
        upper_panel.add(upper_label);
        lower_panel.add(setCredentials);
        lower_panel.add(getLicence);
        lower_panel.add(stop);
        lower_panel.add(exit);


        // Text Area at the Center
        main_area = new JTextArea();

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, lower_panel);
        frame.getContentPane().add(BorderLayout.NORTH, upper_panel);
        frame.getContentPane().add(BorderLayout.CENTER, main_area);
        frame.setVisible(true);

        exit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                user.quit();
            }
        });

        stop.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                user.stop();
            }
        });

        setCredentials.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                user.inputCredentials();
                status_custom(user.presentUserInfo());
            }
        });

        getLicence.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                user.sendRequest();
            }
        });
    }
}