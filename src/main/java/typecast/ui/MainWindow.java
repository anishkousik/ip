package typecast.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import typecast.TypeCast;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private TypeCast typeCast;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.png"));
    private Image typeCastImage = new Image(this.getClass().getResourceAsStream("/images/TypeCast.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        
        // Show welcome message
        String welcome = "Hello! I'm TypeCast\nWhat can I do for you?";
        dialogContainer.getChildren().add(
            DialogBox.getTypeCastDialog(welcome, typeCastImage)
        );
    }

    /** Injects the TypeCast instance */
    public void setTypeCast(TypeCast tc) {
        typeCast = tc;
        
        // Load existing tasks and show count
        if (typeCast.getTaskList().size() > 0) {
            String loadMessage = "Loaded " + typeCast.getTaskList().size() + " task(s) from previous session.";
            dialogContainer.getChildren().add(
                DialogBox.getTypeCastDialog(loadMessage, typeCastImage)
            );
        }
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing TypeCast's reply
     * and then appends them to the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = typeCast.getResponse(input);
        
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getTypeCastDialog(response, typeCastImage)
        );
        userInput.clear();
        
        // Exit application if bye command
        if (input.trim().equalsIgnoreCase("bye")) {
            Platform.exit();
        }
    }
}