package gamewindow;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import localpage.FXMLLocalController;
import pagemanager.Navigation;

public class FXMLGameWindowController implements Initializable {


    private String[][] board = new String[3][3];
    private boolean isUserTurn = true;
    private int userScore = 0;
    private int computerScore = 0;
    private boolean gameWon = false;
    private Random random = new Random();
    private String difficulty;


    @FXML
    private Text userScoreText;
    @FXML
    private Text computerNameText;
    @FXML
    private Text computerScoreText;

    @FXML
    private Button button00;
    @FXML
    private Button button01;
    @FXML
    private Button button02;
    @FXML
    private Button button10;
    @FXML
    private Button button11;
    @FXML
    private Button button12;
    @FXML
    private Button button20;
    @FXML
    private Button button21;
    @FXML
    private Button button22;
    @FXML
    private ImageView imageView;
    @FXML
    private MediaView mediaView;

    private final Image xImage = new Image(getClass().getResourceAsStream("/resources/x.png"));
    private final Image oImage = new Image(getClass().getResourceAsStream("/resources/o.png"));

    @FXML
    public void handleBackHButton(ActionEvent event) {
        Navigation.nextPage(event, "/localpage/FXMLLocal.fxml");
    }

    @FXML
    public void handleScreenShootButton(ActionEvent event) {

    }

    @FXML
    public void handleResetButton(ActionEvent event) {
        resetGame();
    }

    @FXML
    public void handleButtonClick(ActionEvent event) {
        if (gameWon) {
            return;
        }

        Button clickedButton = (Button) event.getSource();
        int row = GridPane.getRowIndex(clickedButton) == null ? 0 : GridPane.getRowIndex(clickedButton);
        int col = GridPane.getColumnIndex(clickedButton) == null ? 0 : GridPane.getColumnIndex(clickedButton);

        if (board[row][col] == null) {
            String currentPlayer = isUserTurn ? "X" : "O";
            Image currentImage = isUserTurn ? xImage : oImage;
            makeMove(clickedButton, row, col, currentPlayer, currentImage);

            if (!gameWon && !isBoardFull()) {
                if (FXMLLocalController.isTwoPlayers) {
                    isUserTurn = !isUserTurn;
                } else {
                    isUserTurn = false;
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(event1 -> computerMove());
                    pause.play();
                }
            }
        }
    }

    private void makeMove(Button button, int row, int col, String player, Image image) {
        board[row][col] = player;
        ImageView imageView = new ImageView(image);
        button.setGraphic(imageView);

        if (checkWin()) {
            highlightWinningButtons();
            gameWon = true;
            if (player.equals("X")) {
                userScore++;
                userScoreText.setText(String.valueOf(userScore));
                showGameResult("win");
            } else {
                computerScore++;
                computerScoreText.setText(String.valueOf(computerScore));
                showGameResult("lose");
            }
        } else if (isBoardFull()) {
            showGameResult("tie");
            resetGame();
        }
    }


    private void computerMove() {
        switch (difficulty) {
            case "easy":
                easyMove();
                break;
            case "medium":
                mediumMove();
                break;
            case "hard":
                hardMove();
                break;
        }


        if (checkWin()) {
            highlightWinningButtons();
            gameWon = true;
            computerScore++;
            computerScoreText.setText(String.valueOf(computerScore));
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Game Over");
                alert.setHeaderText(null);
                alert.setContentText("You lost to the computer!");
                //alert.showAndWait();
            });

        } else if (isBoardFull()) {
            showGameResult("tie");
            resetGame();
        } else {
            isUserTurn = true;
        }


    }

    //Handle Easy Mode With Computer
    private void easyMove() {
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (board[row][col] != null);

        Button button = getButtonByRowCol(row, col);
        if (button != null) {
            makeMove(button, row, col, "O", oImage);
        }
    }

    //Handle Medium Mode With Computer
    private void mediumMove() {
        if (!blockUserWin()) {
            easyMove();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }


}
