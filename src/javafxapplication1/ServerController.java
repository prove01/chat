package javafxapplication1;

import esercitazionechatroom.Server.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author provenzano.riccardo
 */
public class ServerController implements Initializable {

    @FXML
    private Tab newchat;
    @FXML
    private TextField namechat;
    @FXML
    private Button newchatbutton;
    @FXML
    private TextArea newchatbox;
    @FXML
    private Tab restorechat;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button restorebutton;
    @FXML
    private TextArea restorebox;
    @FXML
    private TextArea newchatconnection;
    
    Accepter accepter = null;
    Server s;
    
    @FXML
    private TextArea restoreconnections;
    @FXML
    private Button next;
    @FXML
    private ListView<String> listview;
    @FXML
    private Button exit;
    @FXML
    private Button exit2;
    
    /**
     * Initializes the controller class
     * avoid the editability
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        newchatconnection.setEditable(false);
        restoreconnections.setEditable(false);
        restorebox.setEditable(false);
        newchatbox.setEditable(false);
    }

    /**
     * create a new Server and a new chat
     * @param event
     * @throws IOException 
     */
    @FXML
    private void startChat(ActionEvent event) throws IOException 
    {
        if(isFull())
        {
            s = new Server(newchatconnection, newchatbox);
            s.createNewChat(namechat.getText());
            accepter = new Accepter(s);     //start the accepter
            s.setAccepter(accepter);        
            namechat.setDisable(true);      //disable the editability
        }
        else
        {
            newchatconnection.setText(newchatconnection.getText()+"\n"+"The field can't be empty");
        }
        
    }

    /**
     * create new Server and restore the chat
     * @param event
     * @throws IOException 
     */
    @FXML
    private void restoreChat(ActionEvent event) throws IOException 
    {
        s = new Server(restoreconnections, restorebox, listview);
        boolean app = false;

        if(s.verifyAdmin(username.getText(), password.getText()))
        {
            app = true;
        }
        else
        {
            restoreconnections.setText(restoreconnections.getText()+"\n"+"incorrect credentials");
        }
        
        if(app)
        {
            //starting the restored chat
            s.restoreChat();
            s.setAccepter(accepter);
        }
            
        
    }

    /**
     * starting the restored chat
     * @param event
     * @throws IOException 
     */
    @FXML
    private void startRestoredChat(ActionEvent event) throws IOException {
        s.path = "src\\esercitazionechatroom\\Server\\chat\\"+listview.getSelectionModel().getSelectedItem();       //path of the selected model
        listview.setVisible(false);
        restoreconnections.setText(restoreconnections.getText()+"\n"+"Ready for connections");
        s.chatname = s.path.substring(38);      //chatname = name of the chat splitted from the path
        s.admin = s.getAdmin();     //retrive the admin of the ex chat
        accepter = new Accepter(s);
        s.setAccepter(accepter);
    }

    /**
     * close connection and close the window
     * @param event
     * @throws IOException 
     */
    @FXML
    private void closeChat(ActionEvent event) throws IOException {
        if (s != null)
        {
            s.closeAllConnection();
        }
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();
    }
    
    /**
     * 
     * @return if the namechat isn't empty (with only spaces)
     */
    public boolean isFull()
    {
        for(int i=0; i<namechat.getText().length(); i++)
        {
            if(namechat.getText().charAt(i) != ' ')
            {
                return true;
            }
        }
        
        return false;
    }

}
