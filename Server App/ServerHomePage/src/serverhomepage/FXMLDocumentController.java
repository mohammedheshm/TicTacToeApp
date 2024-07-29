package serverhomepage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TableView<?> tableView;

    @FXML
    private TableColumn<?, ?> playerColumn;

    @FXML
    private TableColumn<?, ?> scoreColumn;

    @FXML
    private TableColumn<?, ?> statusColumn;

  
    
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playerColumn.setText("Player");
        playerColumn.setStyle("-fx-background-color: #D0B8A8; -fx-text-fill: #8D493A; -fx-font-weight: bold;");

        scoreColumn.setText("Score");
        scoreColumn.setStyle("-fx-background-color: #D0B8A8; -fx-text-fill: #8D493A; -fx-font-weight: bold;");

        statusColumn.setText("Status");
        statusColumn.setStyle("-fx-background-color: #D0B8A8; -fx-text-fill: #8D493A; -fx-font-weight: bold;");

    }    
    
}
