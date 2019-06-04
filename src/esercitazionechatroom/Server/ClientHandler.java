package esercitazionechatroom.Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * <h3> takes care of user management (add and verify) </h3>
 * @author provenzano.riccardo
 */
public class ClientHandler 
{
    public HashMap <String, String> credentials = new HashMap <String, String>();
    String path;
    
    /**
     * initializes credentials with the users written in the folder passed by the path
     * @param path path to the users folder
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public ClientHandler(String path) throws FileNotFoundException, IOException
    {
        this.path = path;
        File f = new File(path);
        
        File[] list = f.listFiles();    //list of clients
        String s, nome = "", pass = "", np = "";
        
        
        ArrayList <BufferedReader> a = new ArrayList();
        for(int i=0; i<list.length; i++)
        {
            a.add(new BufferedReader(new FileReader(list[i])));     //add new BufferedReaders associated to each file
            int j = 1;
            while( (s = a.get(i).readLine()) != null)       //until the file isn't empty
            {
                if(j == 1)      //the first time (line) it takes the username
                {
                    nome = s;
                    j++;
                }  
                else        //sencond time the password
                    pass = s;
            }

            credentials.put(nome, pass);        //  add this 2 variables in the hashmap
            
            a.get(i).close();       // closing BufferedReader
        }
        
        
    }
    
    /**
     * registration of a new user, the name and password credentials are saved as a new file in the users folder
     * @param name username
     * @param password password
     * @return outcome of user addition
     * @throws IOException 
     */
    public boolean addUser(String name, String password) throws IOException
    {
        if(!credentials.containsKey(name))      //if username is not already in use
        {
            credentials.put(name, password);
            File f = new File(path+"\\"+name+".txt");       //new File named as the own username
            f.createNewFile();      //create the file
            PrintWriter pw = new PrintWriter(new FileWriter(f));
            pw.println(name);
            pw.println(password);
            pw.flush();
            pw.close();
            return true;
        }
        else
            return false;
    }
    
    /**
     * verification of correspondence between user name and password
     * @param name username
     * @param password
     * @return correspondence between user name and password
     */
    public boolean verifyUser(String name, String password)
    {
        if(credentials.get(name).equals(password))
            return true;
        else
            return false;
    }
    
    /**
     * check if the user name is present among the users
     * @param name username
     * @return if the username is present among the users
     */
    public boolean isUser(String name)
    {
        if(credentials.containsKey(name))
            return true;
        else
            return false;
    }
    
}
