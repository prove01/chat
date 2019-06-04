package esercitazionechatroom.Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <h3> write the chat </h3>
 * @author provenzano.riccardo
 */
public class WriterChat 
{
    String path;
    PrintWriter pw;
    
    public WriterChat(String path)
    {
        this.path = path+"\\chat.txt";
    }
    
    /**
     * write the message passate as param in the file chat.txt
     * @param s message
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void write(String s) throws FileNotFoundException, IOException
    {
        File f = new File(path);
        pw = new PrintWriter(new FileWriter(f, true));
        if(f.isFile())
        {
            pw.println(s);
            pw.flush();
            pw.close();
        }
    }
}
