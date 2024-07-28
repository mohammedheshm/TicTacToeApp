package alert;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FXMLPlayAgainDialogController {


     @FXML
    private Text messageText;
     
     @FXML
    private Button btnText;

    private Stage dialogStage;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setMessage(String message) {
            messageText.setText(message);
        
    }
    
    public void setButtonText(String text) {
            btnText.setText(text);
        
    }

    public boolean isOkClicked() {
        return okClicked;
    }

   @FXML
private void handleOkButton() {
    if (dialogStage != null) {
        okClicked = true;
        dialogStage.close();
    } else {
        System.out.println("dialogStage is null");
    }
}



    @FXML
    private void handleCancelButton() {
        dialogStage.close();
    }
}
