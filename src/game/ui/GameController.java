package game.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    private Circle circle;

    @FXML
    private Pane pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pane.setOnKeyPressed(this::handleKeyPress);


    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.BACK_SPACE) {
            System.out.println("back");
        }
        if (event.getCode() == KeyCode.SPACE) {
            System.out.println("space");
        }
        if (event.getCode() == KeyCode.UP) {
            System.out.println("up");
        }
    }


    public void receiveMessage(String message) {
//        messages.appendText("\nfriend: " + message);
    }
}