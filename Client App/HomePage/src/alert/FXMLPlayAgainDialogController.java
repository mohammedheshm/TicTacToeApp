package alert;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FXMLPlayAgainDialogController {

    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;

    private Stage dialogStage;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
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
