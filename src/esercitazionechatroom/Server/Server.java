package esercitazionechatroom.Server;

import java.net.*;
import java.io.*;
import java.util.*;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

/**
 * <h3> manages chat and connections </h3>
 * @see Accepter
 * @see ClientHandler
 * @author provenzano.riccardo
 */
public class Server
{
    protected Socket socket;
    public Vector <Connection> listOfConnection = new Vector();
    ClientHandler ch = null;
    public String path = "src\\esercitazionechatroom\\Server\\chat", admin="", currentMode="", username="", chatname;
    public TextArea t, t1;
    ListView <String> l;
    Accepter accepter;

    /**
     * 
     * @param t text area for logs
     * @param t1 text area for messages
     * @throws IOException if it can't find the path
     */
    public Server(TextArea t, TextArea t1) throws IOException
    {
        this.t = t;
        this.t1 = t1;
        ch = new ClientHandler("src\\esercitazionechatroom\\Server\\users");
    }
    
    /**
     * 
     * @param t text area for logs
     * @param t1 text area for messages
     * @param l listview for choosing the restored chat
     * @throws IOException 
     */
    public Server(TextArea t, TextArea t1, ListView l) throws IOException
    {
        this.t = t;
        this.t1 = t1;
        this.l = l;
        ch = new ClientHandler("src\\esercitazionechatroom\\Server\\users");
    }
    
    /**
     * <p> Create a new folder with file users.txt and chat.txt </p>
     * @param chatname name of the chat to create
     * @throws IOException 
     */
    public void createNewChat(String chatname) throws IOException
    {
        
        currentMode = "NewChat";
        File f;
        PrintWriter p;
        String dataChat;
        Socket socket = null;
        GregorianCalendar gc = new GregorianCalendar();
        dataChat = "" + gc.get(Calendar.YEAR)+"."+(gc.get(Calendar.MONTH)+1)+"."+gc.get(Calendar.DAY_OF_MONTH);
        this.chatname = dataChat +" "+ chatname;
        String folder = path+"\\"+dataChat+" "+chatname;
        f = new File(folder);
        f.mkdir();
        this.path = folder;
        f = new File(folder+"\\users.txt");
        f.createNewFile();
        
        f = new File(folder+"\\chat.txt");
        p = new PrintWriter(new FileWriter(f), true);
        p.close();
        
        t.setText("Ready for connections");

    }
    
    /**
     * <p> lists the chats that the admin can restore </p>
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void restoreChat() throws FileNotFoundException, IOException
    {
        currentMode = "Restore";
        BufferedReader br;
        while(true)
        {
            File f = new File(this.path);
            File f1;
            File[] chats = f.listFiles();
            Vector <File> adminchats = new Vector();
            if(chats.length > 0)
            {
                for(int i=0; i<chats.length; i++)
                {
                    f1 = new File(chats[i].getPath() + "\\users.txt");
                    br = new BufferedReader(new FileReader(f1));
                    String line;
                    if( (line = br.readLine()) != null)
                    {
                        if(line.equals("Admin: "+username))
                        {
                            f1 = new File(chats[i].getPath());
                            adminchats.add(f1);
                        }
                        
                    }        
                }
                for(int i=0; i<adminchats.size(); i++)
                {
                    l.getItems().add(adminchats.get(i).getPath().substring(38));
                }
                break;
            }
        }
        
    }
    
   /**
    * 
    * @param a Accepter
    */
    public void setAccepter(Accepter a)
    {
        this.accepter = a;
    }
    
    /**
     * close all clients sockets and stop the accepter in order to not accept more incoming connections
     * @throws IOException 
     */
    public void closeAllConnection() throws IOException
    {
        for(int i = 0; i<listOfConnection.size(); i++)
        {
            listOfConnection.get(i).socket.close();
        }
        if(accepter != null)
            accepter.ss.close();
    }
    
    /**
     * 
     * @param message message to be shown
     */
    public void printChat(String message)
    {
        t1.setText(t1.getText()+"\n"+message);
    }
    
    /**
     * check if the admin accont exists
     * @param username Admin's username
     * @param password Admin's password
     * @return outcome of the verification
     */
    public boolean verifyAdmin(String username, String password)
    {

        if(ch.isUser(username))
        {
            if(ch.verifyUser(username, password))
            {
                this.username = username;
                return true;
            }else
            {
                System.out.println("wrong password");
            }
        }else
        {
            System.out.println("user not registered");
        }
        return false;
    }
    
    /**
     * 
     * @return the admin of the chat
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public String getAdmin() throws FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(path+"\\users.txt"));
        String user;
        if( (user = br.readLine()) != null)
        {
            return user.split("Admin: ")[1];
        }
        return null;
    }
        
}
