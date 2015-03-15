


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by admin on 14.02.2015.
 */
public class main  extends JFrame {
    server server;
    final   JTextField textField = new JTextField(10);
    Thread thread;
    public static void main(String[] args)   {
        new main("test");
    }
    ActionListener actionListener;
    public main(String name){
        super(name);
        setLayout(new FlowLayout());
        setSize(300,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        final    JButton   start = new JButton("Start");
        final   JButton end = new JButton("end");

      final   JButton send = new JButton("Send");
        add(start);
        add(textField);
        add(send);
        add(end);
        setVisible(true);
        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            if(e.getSource()==start)
            {
                server.setRunning(true);
                 server = new  server();
                thread =  new Thread(server);
                thread.start();

            }
          if(e.getSource() == end){
              server.close();
              thread.stop();

            }
            }
        };
        start.addActionListener(actionListener);
        send.addActionListener(actionListener);
        end.addActionListener(actionListener);
    }
    public  JTextField getTextF(){
        return this.textField;
    }


}
