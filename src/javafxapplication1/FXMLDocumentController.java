package javafxapplication1;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Main Stage Controller
 *
 * @author provenzano.riccardo
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Button server;
    @FXML
    private Button client;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    /**
     * change scene on Server
     * @param event
     * @throws IOException 
     */
    @FXML
    public void ServerScene(ActionEvent event) throws IOException
    {
        Parent clientViewParent = FXMLLoader.load(getClass().getResource("server.fxml"));       //load "server.fxml"
        Scene clientViewScene = new Scene(clientViewParent);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();     //take stage from the event
        
        window.setTitle("Server chat");
        window.setScene(clientViewScene);
        window.show();
    }
    
    /**
     * change scene in Client
     * @param event
     * @throws IOException 
     */
    @FXML
    public void ClientScene(ActionEvent event) throws IOException
    {
        Parent clientViewParent = FXMLLoader.load(getClass().getResource("connection.fxml"));       //load "connection.fxml"
        Scene clientViewScene = new Scene(clientViewParent);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();     //take stage from the event
        
        window.setTitle("Client chat");
        window.setScene(clientViewScene);
        window.show();
    }
    
}
