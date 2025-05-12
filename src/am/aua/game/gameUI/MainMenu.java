package am.aua.game.gameUI;

import am.aua.game.fileIO.SaveLoadManager;
import am.aua.game.gameLogic.GameCore;
import am.aua.game.players.Player;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

public class MainMenu extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Strategy Game - Main Menu");

        // --- Title ---
//        Label title = new Label("⚔️ Strategy Game");
//        title.setStyle("-fx-font-size: 32px; -fx-text-fill: white; -fx-font-weight: bold;");

        // --- Buttons ---
        Button newGameBtn = styledButton("Start New Game");
        Button loadGameBtn = styledButton("Load Game");
        Button exitBtn = styledButton("Exit");

        // --- Button actions ---
        newGameBtn.setOnAction(e -> {
            Optional<String> player1Name = askName("Player 1");
            if (!player1Name.isPresent()) return;

            Optional<String> player2Name = askName("Player 2");
            if (!player2Name.isPresent()) return;

            ArrayList<Player> players = new ArrayList<>();
            players.add(new Player(player1Name.get()));
            players.add(new Player(player2Name.get()));

            GameCore gameCore = new GameCore(players);
            new GameWindow(primaryStage, gameCore);
        });

        loadGameBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Saved Game");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                GameCore loadedCore = SaveLoadManager.loadGame(file.getAbsolutePath());
                if (loadedCore != null) {
                    try {
                        new GameWindow(primaryStage, loadedCore);
                    } catch (Exception ex) {
                        showError("Error: Choose a proper save file.");
                    }
                } else {
                    showError("Failed to load game.");
                }
            }
        });

        exitBtn.setOnAction(e -> primaryStage.close());

        // --- Layout ---
        VBox menuBox = new VBox(20, newGameBtn, loadGameBtn, exitBtn);
        menuBox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(menuBox);
        root.setPadding(new javafx.geometry.Insets(30));

        // Background image (optional)
        BackgroundImage bgImage = new BackgroundImage(
                new Image(getClass().getResource("/am/aua/game/resources/background.jpg").toExternalForm(), 600, 800, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        );
        root.setBackground(new Background(bgImage));

        Scene scene = new Scene(root, 600, 800);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // Helper: create styled button
    private Button styledButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: #333;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-padding: 10px 20px;
            -fx-background-radius: 10;
            -fx-cursor: hand;
        """);
        btn.setOnMouseEntered(e -> btn.setStyle(btn.getStyle().replace("#333", "#555")));
        btn.setOnMouseExited(e -> btn.setStyle(btn.getStyle().replace("#555", "#333")));
        return btn;
    }

    // Helper: show name dialog
    private Optional<String> askName(String playerLabel) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(playerLabel + " name");
        dialog.setContentText("Name:");
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent() || result.get().trim().isEmpty()) {
            showError("Invalid name for " + playerLabel + "!");
            return Optional.empty();
        }
        return result;
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.WARNING, message, ButtonType.OK).showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
