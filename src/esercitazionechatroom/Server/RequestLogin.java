package esercitazionechatroom.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.scene.control.TextArea;

/**
 * <h3> takes care of managing chat access </h3>
 * @author provenzano.riccardo
 */
public class RequestLogin extends Thread {
    
    Socket socket;
    Server s;
    String username;
    TextArea t;
    Connection c;
    PrintStream out = null;
    InputStreamReader in = null;
    BufferedReader inFromClient = null, br;
    OutputStreamWriter osw = null;
    PrintWriter pw = null;
    BufferedWriter bw = null;
    
    /**
     * start the thread
     * @param socket server side socket
     * @param s server
     */
    public RequestLogin(Socket socket, Server s)
    {
        this.socket = socket;
        this.s = s;
        this.t = s.t;
        this.start();
    }
    
    @Override
    public void run()
    {
        try {
            out = new PrintStream(socket.getOutputStream());
            in = new InputStreamReader(socket.getInputStream());
            inFromClient = new BufferedReader(in);
            osw = new OutputStreamWriter(socket.getOutputStream());
        
            File f = new File(s.path+"\\users.txt");
            pw = new PrintWriter(new FileWriter(f, true));
            br = new BufferedReader(new FileReader(f));
            
            boolean app = true;     // variable for cycle management
            while(app)      // cycle for entering and verifying credentials
            {
                String x = inFromClient.readLine();
                switch(x)
                {
                    case "login":
                        username = inFromClient.readLine();     //waiting for client username
                        if(!isLogged(username))
                        {
                            if(s.ch.isUser(username))       //if user exist
                            {                  
                                String password = inFromClient.readLine();      //waiting for client password
                                if(s.ch.verifyUser(username, password))     //verify the credentials
                                {
                                    t.setText(t.getText()+"\n"+"user "+username+" logged");     //log
                                    
                                    if(!s.currentMode.equals("NewChat"))
                                    {
                                        if(wasConnected(username))
                                        {
                                            PrintStream out = new PrintStream(socket.getOutputStream());
                                            out.println(loadChat());        //send the entire chat
                                            out.flush();
                                        }
                                    }
                                    
                                    if(!wasConnected(username))
                                        newUser(username);
                                    createConnection(username);
                                    app=false;
                                }else
                                {
                                    t.setText(t.getText()+"\n"+username+" wrong password");     //log
                                    out.println("wrong password");
                                }
                            }else
                            {
                                t.setText(t.getText()+"\n"+"user "+username+" not registered");     //log
                                out.println("You are not registered");
                            } 
                        }
                        else
                        {
                            t.setText(t.getText()+"\n"+"user "+username+" already logged");     //log
                            out.println("You are already logged");
                        }
                            
                    break;

                    case "registration":
                        username = inFromClient.readLine();     //waiting for client username
                        if(!isLogged(username))
                        {
                            String password = inFromClient.readLine();      //waiting for client password
                            if(s.ch.addUser(username, password))
                            {
                                t.setText(t.getText()+"\n"+"user "+username+" registrated");        //log
                                if(s.currentMode.equals("NewChat"))
                                {
                                    newUser(username);
                                    createConnection(username);
                                }
                                else
                                {
                                    createConnection(username);
                                    if(wasConnected(username))
                                    {
                                        PrintStream out = new PrintStream(socket.getOutputStream());
                                        out.println(loadChat());        //send the entire chat
                                        out.flush();
                                    }
                                }
                                app=false;
                            }else
                            {
                                t.setText(t.getText()+"\n"+"user "+username+" already registrated");        //log
                                out.println("Already registered");
                            }
                        }
                        else
                        {
                            t.setText(t.getText()+"\n"+"user "+username+" already logged");     //log
                            out.println("You are already logged");
                        }
                        
                    break;
                }
            }
            out.println("logged");
            out.println(s.chatname);
        } catch (IOException ex) 
        {
            t.setText(t.getText()+"\n"+"client "+socket.getInetAddress()+" disconnected");      //log
            System.err.println("client "+socket.getInetAddress()+" disconnected");
        }
    }
    
    /**
     * starting a new connection with the socket and initializing username and chat path
     * @param username client username
     */
    public void createConnection(String username)
    {
        c = new Connection(socket, s);
        c.username = username;
        c.wc = new WriterChat(s.path);
        s.listOfConnection.add(c);      //adding this connection
    }
    
    /**
     * write on users.txt file the variable username
     * @param username client username
     * @throws IOException 
     */
    public void newUser(String  username) throws IOException
    {
        if(br.readLine() != null)
        {
            br.close();
            pw.println("Client: "+username);
        }
        else
        {
            pw.println("Admin: "+username);
            s.admin = username;
        }
        pw.flush();
        pw.close();
        
    }
    
    /**
     * 
     * @return String with all the chat
     * @throws FileNotFoundException missing or incorrect path
     * @throws IOException 
     */
    public String loadChat() throws FileNotFoundException, IOException
    {
        String mess, chat = "";
        File f = new File(s.path+"\\chat.txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        while( (mess = br.readLine()) != null)      //read all the file chat.txt
        {
            chat += mess + "\n";
        }
        br.close();
        return chat;
    }
    
    /**
     * 
     * @param username client username
     * @return control if the client was connected
     * @throws FileNotFoundException missing or incorrect path
     * @throws IOException 
     */
    public boolean wasConnected(String username) throws FileNotFoundException, IOException
    {
        String user;
        boolean connected = false;
        BufferedReader br = new BufferedReader(new FileReader(s.path+"\\users.txt"));
        while( (user = br.readLine()) != null)
        {
            if(user.split(": ")[1].equals(username))        //take the username after the : 
            {
                connected = true;
                break;
            }
        }
        br.close();
        return connected;
    }
    
    /**
     * 
     * @param username
     * @return if is logged
     */
    public boolean isLogged(String username)
    {
        for(Connection c : s.listOfConnection)      //control all the connections
        {
            if(c.active && c.username.equals(username))     //if connection is active and has the same username the param
                return true;
        }
        return false;
    }
    
    
}
