package javafxapplication1;

import esercitazionechatroom.Client.Client;
import esercitazionechatroom.Time;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Connection Controller class
 * @author provenzano.riccardo
 * 
 */
public class ConnectionController implements Initializable {

    @FXML
    private TextField ip;
    @FXML
    private TextField port;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button next;
    @FXML
    private Tab chat;
    
    boolean connected = false;
    
    private Client c = null;
    @FXML
    private Tab connect;
    @FXML
    private TabPane tabpane;
    @FXML
    private TextArea textarea;
    @FXML
    private TextField message;
    @FXML
    private Button send;
    @FXML
    private Button exit;
    @FXML
    private Label user;
    @FXML
    private Label room;
    @FXML
    private Label hour;
    @FXML
    private ChoiceBox<String> choice = new ChoiceBox();
    @FXML
    private Label errors;
    @FXML
    private Text text;
    

    /**
     * Initializes the controller class
     * <p> set login or registration box and disable the second panel </p>
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        choice.getItems().add("login");
        choice.getItems().add("registration");
        choice.setValue("login");
        textarea.setEditable(false);
        tabpane.getTabs().get(1).setDisable(true);
        
        
    }    

    
    /**
     * if client isn't created start a new Client and send the choice, username and password
     * switch to the chat panel
     * @param event
     * @throws InterruptedException 
     */
    @FXML
    private void toChat(ActionEvent event) throws InterruptedException 
    {
        c = new Client(ip.getText(), Integer.parseInt(port.getText()), textarea, text);

        if(!c.isAlive())
            c.start();
        sleep(200);
        if(c.isServeractive())
        {
            c.sendMessage(choice.getValue());
            c.sendMessage(username.getText());
            c.setUsername(username.getText());
            c.sendMessage(password.getText());
            
            textarea.setText("");
            sleep(200);     //time for acquiring the valour
            connected = c.isConnected();
            for(int i=0; i<5; i++)
            {
                connected = c.isConnected();
                sleep(50);      //attend for the next control
            }
        }
        

        if(connected)       //if the chat can go on
        {
            tabpane.getSelectionModel().select(chat);       //change the tab
            tabpane.getTabs().get(0).setDisable(true);      //disable the connect panel
            tabpane.getTabs().get(1).setDisable(false);     //enable the chat panel
            Time t = new Time();
            String date = t.getDate() + " - " + t.getHour();        //date of the maessage
            hour.setText(date);     
            user.setText(c.getUsername());      
            room.setText(c.getChatname());      
        }
    }

    /**
     * Send message cointained in the textfield "message" to the server
     * @param event
     * @throws IOException 
     */
    @FXML
    private void sendMessage(ActionEvent event) throws IOException {
        if(!c.getSocket().isClosed() && c.isConnected())        //if socket isn't closed and client is connected
        {
            boolean correct = false;
            for(int i=0; i<message.getText().length(); i++)
            {
                if(message.getText().charAt(i) != ' ')      //control that the field isn't empty
                {
                    correct = true;
                    break;
                }
            }
            if(correct)     //if isn't empty
            {
                textarea.setText(textarea.getText()+ "\n" + new Time().getHour()+ " - ME: "+message.getText());     //set text in the chat area
                c.sendMessage(message.getText());
            }
            message.setText("");        //initialize message for the next one
        
        }
        else
        {
            closeChat(event);
        }
        
    }

    /**
     * close the window and the socket
     * @param event
     * @throws IOException 
     */
    @FXML
    private void closeChat(ActionEvent event) throws IOException {
        c.closeConnection();
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();     //take stage from the event
        window.close();
    }

    /**
     * send the message to the server if ENTER key is pressed
     * @param event 
     */
    @FXML
    private void Send(KeyEvent event) 
    {
        if(event.getCode() == KeyCode.ENTER)        //if ENTER is pressed
        {
            if(!c.getSocket().isClosed() && c.isConnected())
            {
                boolean correct = false;
                for(int i=0; i<message.getText().length(); i++)
                {
                    if(message.getText().charAt(i) != ' ')      //control that the field isn't empty
                    {
                        correct = true;
                        break;
                    }
                }
                if(correct)
                {
                    textarea.setText(textarea.getText()+"\nME: "+message.getText());        //set text in the chat area
                    c.sendMessage(message.getText());
                }
                message.setText("");        //initialize message for the next one

            }
        }
        
    }

    
    
    
    
}
