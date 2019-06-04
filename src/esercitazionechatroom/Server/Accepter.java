package esercitazionechatroom.Server;

import java.net.*;
import java.io.*;

/**
 * <h3> accepts client connections and starts the RequestLogin thread </h3>
 * @see RequestLogin
 * @author dellorto.andrea
 */
public class Accepter extends Thread
{
    static final int port = 1978;       // server listening on the port variable
    Server s = null;
    ServerSocket ss = null;
    
    /**
     * started the thread initializing Server variable
     * @param s server
     */
    public Accepter(Server s)
    {
        this.s = s;
        this.start();
    }
    
    @Override
    public void run() 
    {   
        Socket socket = null;
        RequestLogin rl;

        try
        {
            ss = new ServerSocket(port);
        } catch (IOException e)
        {
            e.printStackTrace();

        }
        
        /**
         * continues accepting connections until the server stops it
         */
        while (true)
        {
            try
            {
                socket = ss.accept();
                    System.out.println("Client connected: " + socket.getInetAddress());     //print the address of the connected client
                    rl = new RequestLogin(socket, s);       // starts the RequestLogin thread
            } catch (IOException e) 
            {
                try {
                    socket.close();     // socket close
                } catch (IOException ex) {
                    
                }
                s.t.setText(s.t.getText()+"\n"+"Server closed");
                System.err.println("Server closed");
                break;
            }
            
        }
        
    }
    
    /**
     * 
     * @return ServerSocket associated
     */
    public ServerSocket getSS()
    {
        return ss;
    }
    
}
