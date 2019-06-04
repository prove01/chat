package esercitazionechatroom.Server;

import esercitazionechatroom.Time;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;

/**
 * <h3> handle the associated client </h3>
 * 
 * @author provenzano.riccardo
 */
public class Connection extends Thread
{
    Socket socket;
    String username, fromclient, toclient, date;
    static Vector <Socket> listOfClient = new Vector();     // list with all client sockets
    WriterChat wc;
    Server server;
    boolean active = true;      // connection status between Connection and Client
    
    /**
     * starts the thread and adds its connection to the client list
     * @param socket lato server
     * @param s server
     */
    public Connection(Socket socket, Server s)
    {
        this.server = s;
        this.socket = socket;
        listOfClient.add(socket);
        this.start();
    }
    
    @Override
    public void run()
    {
        InputStreamReader isr = null;
        BufferedReader inFromClient = null;
        
        try 
        {
            isr = new InputStreamReader(socket.getInputStream());
            inFromClient = new BufferedReader(isr);
            while( (fromclient = inFromClient.readLine()) != null)
            {
                
                date = new Time().getHour();        //local hour
                toclient = date + " - " + username + ": " + fromclient;
                sendMessageToAll(toclient);
                server.printChat(toclient);
                wc.write(toclient);     // print in file the message
            }
            active = false;
            if(server.admin.equals(username))       // if the disconnecting client is the administrator, the server closes all the connections
            {
                server.closeAllConnection();
            }
            server.t.setText(server.t.getText()+"\n"+username+" disconnected");     //log
            System.err.println(username+" disconnected");
            
        } catch (IOException ex) 
        {
            try {
                this.socket.close();        // close the socket
            } catch (IOException ex1) {
                System.err.println("Unable to close socket");
            }

        }

    }
    
    /**
     * 
     * @return socket
     */
    public Socket getSocket()
    {
        return this.socket;
    }
    
    /**
     * forward the message passed, to all clients in listOfClient
     * @param message to be sent
     * @throws IOException if sockets are closed
     */
    public void sendMessageToAll(String message) throws IOException
    {
        PrintStream out = null;
        for(Socket s : listOfClient)        // scrolls the list of clients and instantiates a PrintStream for each socket
        {
            if(s != this.socket)
            {
                out = new PrintStream(s.getOutputStream());
                out.println(message);       // send message
            }
            
        }
        
        
    }
    
}
