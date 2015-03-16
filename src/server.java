
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
    private boolean isRunning = false,isListening,isConnecting;

    main main;
    float curmX;
    float curmY;
    float curAndX = 0;
    float curAndY = 0;
    public float masInp[]={0,0,0};
    Robot robot;


    JTextField textField;
    public void  run(){
        while (isRunning) {
            try {

                robot = new Robot();
                serverSocket = new ServerSocket(1234, 10);
                System.out.println(serverSocket.getLocalSocketAddress());
                isConnecting = true;
                setUpConnection();
                System.out.println("acepted ");
                listen();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }

    }

    public void close(){
        try {
            outputStream.close();
            inputStream.close();
            connection.close();
        } catch (IOException e) {e.printStackTrace();}

    }
    public void setRunning(boolean inp){
        this.isRunning = inp;
    }
    private  void  setUpConnection(){
        while (isConnecting){
            try {
                connection = serverSocket.accept();
                connection.setTcpNoDelay(true);
                outputStream = new ObjectOutputStream(connection.getOutputStream());
                outputStream.flush();
                inputStream = new ObjectInputStream(connection.getInputStream());
                isConnecting = false;
                isListening = true;
            }
            catch (IOException e){
                System.out.println("trying");
            }
        }
    }
    private void listen() throws IOException {
        while (isRunning && isListening )
        { try {
            Point p = MouseInfo.getPointerInfo().getLocation();
            curmX = (float) p.getX();
            System.out.println("x :--------"+curmX+"--------");
            curmY = (float) p.getY();
            System.out.println("y :--------"+curmY+"--------");

                masInp = (float[]) inputStream.readObject();
                System.out.println( "You send:");
                System.out.println("Command"+masInp[0]);
                System.out.println("X:" + masInp[1]);
                System.out.println("Y:"+masInp[2]);
                if (masInp[0]==(float)2.0) {
                    // curAndX = masInp[1];
                    curmX = curmX + masInp[1] - curAndX;
                    curAndY = masInp[2];
                    curmY = curAndY + masInp[2] - curAndY;

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
            System.out.println("--");
            outputStream.flush();
            }
            catch (SocketException e){e.printStackTrace();
            } catch (IOException e){e.printStackTrace();} catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            ;

                /*x_str =(String) inputStream.readObject();
                x = Integer.parseInt(x_str);
                robot.mouseMove(x,100);*/
        }
    }
}
