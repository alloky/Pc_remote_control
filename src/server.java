
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by admin on 14.02.2015.
 */
public class server implements Runnable {
    private static ServerSocket serverSocket;
    private static Socket connection;
    private  static ObjectInputStream inputStream;
    private static ObjectOutputStream outputStream;
    main main;
    float curmX;
    float curmY;
    float curAndX = 0;
    float curAndY = 0;
    public float masInp[]={0,0,0};


    public boolean isRunning = true;
    JTextField textField;
    public void  run(){
        try {

            Robot robot = new Robot();
            Point p = MouseInfo.getPointerInfo().getLocation();
             curmX = (float) p.getX();
            System.out.println("x :--------"+curmX+"--------");
             curmY = (float) p.getY();
            System.out.println("y :--------"+curmY+"--------");
            serverSocket = new ServerSocket(1234,10);
            System.out.println(serverSocket.getLocalSocketAddress());
            while (isRunning )
            {
                try {
                    connection = serverSocket.accept();
                    connection.setTcpNoDelay(true);
                }
                catch (IOException e){
                    System.out.println("vvv");

                }
                System.out.println("acepted ");
                outputStream = new ObjectOutputStream(connection.getOutputStream());
                outputStream.flush();
                inputStream = new ObjectInputStream(connection.getInputStream());
                try {
                    masInp =(float[]) inputStream.readObject();
                    System.out.println( "You send:");
                    System.out.println("Command"+masInp[0]);
                    System.out.println("X:"+masInp[1]);
                    System.out.println("Y:"+masInp[2]);
                    if (masInp[0]==(float)2.0) {
                        //
                        curmX = curmX + masInp[1] - curAndX;
                        curAndX = masInp[1];
                        curmY = curAndY + masInp[2] - curAndY;
                        curAndY = masInp[2];
                        robot.mouseMove((int)curmX,(int)curmY);

                    }
                    if (masInp[0]==(float)1.0){
                        //connection.setTcpNoDelay(true);
                        curAndX = masInp[1];
                        curAndY = masInp[2];
                        p = MouseInfo.getPointerInfo().getLocation();
                       // curmX = (float) p.getX();
                       // System.out.println("x :--------"+curmX+"--------");
                       // curmY = (float) p.getY();
                       // System.out.println("y :--------"+curmY+"--------");
                    }
                    if((masInp[0]==(float)3.0)){
                      //  connection.setTcpNoDelay(false);
                        curAndX = masInp[1];
                        curAndY = masInp[2];
                        p = MouseInfo.getPointerInfo().getLocation();
                        curmX = (float) p.getX();
                        System.out.println("x :--------"+curmX+"--------");
                        curmY = (float) p.getY();
                        System.out.println("y :--------"+curmY+"--------");

                    }
                    if(masInp[0]== 10) {
                        robot.mousePress(InputEvent.BUTTON1_MASK);
                        robot.delay(300);
                        robot.mouseRelease(InputEvent.BUTTON1_MASK);
                    }
                    if(masInp[0]== 20){
                        robot.mousePress(InputEvent.BUTTON3_MASK);
                        robot.delay(300);
                        robot.mouseRelease(InputEvent.BUTTON3_MASK);
                    }
                }
                catch (SocketException e){
                } catch (IOException e){};
                System.out.println("--");
                outputStream.flush();
                /*x_str =(String) inputStream.readObject();
                x = Integer.parseInt(x_str);
                robot.mouseMove(x,100);*/
            }
            if(isRunning == false) {
                System.out.println("closed");

            }
        } catch (IOException e) {e.printStackTrace();} catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }

    public void close(){
        try {
            outputStream.close();
            inputStream.close();
            connection.close();
        } catch (IOException e) {e.printStackTrace();}

    }
}
