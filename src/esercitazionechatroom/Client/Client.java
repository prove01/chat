package esercitazionechatroom.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

/**
 * <h3> Client: send and receive messages </h3>
 * 
 * @author provenzano.riccardo
 */
public class Client extends Thread
{
    static PrintStream out;
    String IP;
    int port;
    Socket socket = null;
    String username;
    boolean app = true, connected = false, serveractive = false;
    TextArea t;
    Text text;
    String chatname;
    
    /**
     * 
     * @param IP IP server
     * @param port server port
     * @param t textarea for visualize the chat
     * @param text camp for errors
     */
    public Client(String IP, int port, TextArea t, Text text)
    {
        this.IP = IP;
        this.text = text;
        this.port = port;
        this.t = t;
    }
    
    @Override
    public void run()
    {
        try 
        {
            
            socket = new Socket(IP, port);  
            serveractive = true;    //if socket started --> serveractive = true
            out = new PrintStream(socket.getOutputStream());
        } catch (IOException ex)
        {
            text.setText("Impossible to establish a connection");
            t.setText("Impossible to establish a connection");
            System.err.println("Impossible to establish a connection");
        }
        if(serveractive)
        {
            InputStreamReader isr = null;
            try 
            {
                isr = new InputStreamReader(socket.getInputStream());
                BufferedReader inFromServer = new BufferedReader(isr);
                String s;
                do
                {
                    s = inFromServer.readLine();    //string from server
                    if(s.equals("logged"))
                    {
                        connected = true;
                        chatname = inFromServer.readLine();     //waiting for the chat name
                    }
                    else if(s != null)
                    {
                        System.out.println(s);
                        text.setText(s);        //error message from server
                        t.setText(t.getText()+"\n"+s);      //message from server printed in the text area
                    }

                }while(s != null && !socket.isClosed());        //while socket isn't closed
                System.out.println("connection closed");
            } catch (IOException ex) 
            {
                try 
                {
                    closeConnection();
                } catch (IOException ex1) 
                {
                    System.err.println("Unable to close connection");
                }
                connected = false;
                t.setText(t.getText()+"\n"+"Server disconnected");

            }
        }
        else
        {
            System.out.println("Server not activated");
            text.setText("Server not activated");
        }
    }

    public boolean isServeractive() {
        return serveractive;
    }

    public void setServeractive(boolean serveractive) {
        this.serveractive = serveractive;
    }
    
    
    public String getChatname() {
        return chatname;
    }

    public void setChatname(String chatname) {
        this.chatname = chatname;
    }
    
    /**
     * close associated socket
     * @throws IOException 
     */
    public void closeConnection() throws IOException
    {
        this.socket.close();
    }
    
    /**
     * 
     * @param message send with the printstream the message
     */
    public void sendMessage(String message)
    {
        out.println(message);
    }
    
    public boolean isConnected()
    {
        return connected;
    }
    
    public static PrintStream getOut() {
        return out;
    }

    public static void setOut(PrintStream out) {
        Client.out = out;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public TextArea getT() {
        return t;
    }

    public void setT(TextArea t) {
        this.t = t;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getUsername()
    {
        return username;
    }
}
