package am.aua.game.gameUI;

import am.aua.game.exceptions.*;
import am.aua.game.fileIO.SaveLoadManager;
import am.aua.game.gameLogic.GameCore;
import am.aua.game.navigation.Cell;
import am.aua.game.players.Player;
import am.aua.game.units.Archer;
import am.aua.game.units.Soldier;
import am.aua.game.units.Tank;
import am.aua.game.units.Unit;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class GameWindow {
    private int CELL_SIZE = 32;

    private final Stage stage;
    private final GameCore gameCore;
    private final int GRID_WIDTH;
    private final int GRID_HEIGHT;

    private Cell selectedCell = null;
    private StackPane selectedCellPane = null;
    private GridPane gridPane;

    private Label currentPlayerLabel;
    private Label playerResourcesLabel;
    private Label currentPlayerUnitsLabel;
    private Label currentPlayerTerritoryLabel;

    private VBox rightPanel;
    private final Label unitInfoPopup = new Label();

    private boolean isBuying = false;
    private Unit unitToBuy = null;

    private int selectedRow = -1;
    private int selectedCol = -1;


    public GameWindow(Stage primaryStage, GameCore gameCore) {
        this.stage = primaryStage;
        this.gameCore = gameCore;
        this.GRID_WIDTH = gameCore.getMap().getWidth();
        this.GRID_HEIGHT = gameCore.getMap().getHeight();

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        int maxWidth = (int) (screenWidth * 0.8);
        int maxHeight = (int) (screenHeight * 0.8);

        int cellWidth = maxWidth / GRID_WIDTH;
        int cellHeight = maxHeight / GRID_HEIGHT;
        this.CELL_SIZE = Math.max(32, Math.min(64, Math.min(cellWidth, cellHeight)));
        BorderPane mainLayout = new BorderPane();

        gridPane = new GridPane();
        gridPane.setHgap(1);
        gridPane.setVgap(1);
        gridPane.setPadding(new Insets(10));
        gridPane.setBackground(Background.fill(Color.GREEN));
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setHgrow(gridPane, Priority.ALWAYS);
        GridPane.setVgrow(gridPane, Priority.ALWAYS);

        renderGrid();

        currentPlayerLabel = new Label("Player: " + gameCore.getCurrentPlayer().getName());
        playerResourcesLabel = new Label("Resources: " + gameCore.getCurrentPlayer().getResources());
        currentPlayerUnitsLabel = new Label("Units: " + gameCore.getCurrentPlayer().getUnits().size());
        currentPlayerTerritoryLabel = new Label("Territory: " + gameCore.getCurrentPlayer().getTerritory().size());

        rightPanel = new VBox(10, currentPlayerLabel, playerResourcesLabel, currentPlayerUnitsLabel, currentPlayerTerritoryLabel);
        rightPanel.setPadding(new Insets(10));
        rightPanel.setStyle("-fx-background-color: #33f9ff;");
        rightPanel.setPrefWidth(200);
        rightPanel.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(rightPanel, Priority.ALWAYS);

        mainLayout.setCenter(gridPane);
        mainLayout.setRight(rightPanel);

        setupControls();

        unitInfoPopup.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 5;");
        unitInfoPopup.setVisible(false);

        StackPane root = new StackPane(mainLayout, unitInfoPopup);
        StackPane.setAlignment(unitInfoPopup, Pos.TOP_LEFT);
        StackPane.setMargin(unitInfoPopup, new Insets(10));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Strategy Game");
        stage.setResizable(true);
        stage.setFullScreen(true);
        stage.setMaximized(true);
        stage.show();
    }

    private void renderGrid() {
        gridPane.getChildren().clear();

        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_HEIGHT; j++) {
                Cell cell = gameCore.getMap().getCellAt(i, j);

                StackPane stack = new StackPane();
                stack.setPrefSize(CELL_SIZE, CELL_SIZE);

                String terrainFile = switch (cell.getTerrain()) {
                    case TREE -> "tree.png";
                    case ROCK -> "rock.png";
                    default -> "grass.png";
                };

                Image terrainImage = new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("/am/aua/game/assets/" + terrainFile)));
                ImageView terrainView = new ImageView(terrainImage);
                terrainView.setFitWidth(CELL_SIZE);
                terrainView.setFitHeight(CELL_SIZE);
                stack.getChildren().add(terrainView);

                Unit unit = cell.getUnit();
                if (unit != null) {
                    String unitFile = getUnitImageFilename(unit);
                    Image unitImage = new Image(Objects.requireNonNull(
                            getClass().getResourceAsStream("/am/aua/game/assets/" + unitFile)));
                    ImageView unitView = new ImageView(unitImage);
                    unitView.setFitWidth(CELL_SIZE);
                    unitView.setFitHeight(CELL_SIZE);
                    stack.getChildren().add(unitView);
                }

                // Determine if this cell is selected
                boolean isSelected = (i == selectedRow && j == selectedCol);

                // Set border color depending on selection or ownership
                if (isSelected) {
                    stack.setStyle("-fx-border-color: yellow; -fx-border-width: 1;");
                } else if (cell.getOwner() != null) {
                    if (cell.getOwner().equals(gameCore.getPlayers().get(0))) {
                        stack.setStyle("-fx-border-color: red; -fx-border-width: 1;");
                    } else if (cell.getOwner().equals(gameCore.getPlayers().get(1))) {
                        stack.setStyle("-fx-border-color: blue; -fx-border-width: 1;");
                    }
                } else {
                    stack.setStyle("-fx-border-color: black; -fx-border-width: 1;");
                }

                final int row = i, col = j;
                stack.setOnMouseClicked(e -> handleCellClicked(row, col, e));
                gridPane.add(stack, j, i);
            }
        }
    }


    private void handleCellClicked(int row, int col, MouseEvent e) {
        Cell clickedCell = gameCore.getMap().getCellAt(row, col);
        Unit clickedUnit = clickedCell.getUnit();

        // Deselect if the same cell is clicked again
        if (selectedCell != null && selectedCell == clickedCell) {
            selectedCell = null;
            selectedRow = -1;
            selectedCol = -1;
            unitInfoPopup.setVisible(false);
            renderGrid();
            return;
        }

        // If a cell is already selected (move/attack)
        if (selectedCell != null) {
            Unit selectedUnit = selectedCell.getUnit();
            try {
                if (clickedUnit == null) {
                    try {
                        gameCore.moveUnit(gameCore.getCurrentPlayer(), selectedUnit, selectedCell, clickedCell);
                    }catch (NotYourUnitException | PathNotClearException | OutOfRangeException exception){
                        new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
                    }
                } else if (clickedUnit.getOwner() != gameCore.getCurrentPlayer()) {
                    try {
                        gameCore.attackUnit(gameCore.getCurrentPlayer(), selectedUnit, selectedCell, clickedCell);
                    }catch (NotYourUnitException | FriendlyFireException | OutOfRangeException | PathNotClearException | NoUnitSelectedException excep){
                        new Alert(Alert.AlertType.ERROR, excep.getMessage()).showAndWait();
                    }
                }
                selectedCell = null;
                selectedRow = -1;
                selectedCol = -1;
                renderGrid();
                currentPlayerLabel.setText("Current Player: " + gameCore.getCurrentPlayer().getName());
                nextTurn();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            }
            return;
        }

        // If buying a unit
        if (isBuying && unitToBuy != null) {
            try {
                gameCore.buyUnit(unitToBuy, row, col, gameCore.getMap());
                isBuying = false;
                unitToBuy = null;
                renderGrid();
                currentPlayerLabel.setText("Current Player: " + gameCore.getCurrentPlayer().getName());
                updatePlayerInfoLabels();
            } catch (CoordinateBlockedException | NotEnoughMoneyException | NotYourTerritoryException ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            }
            return;
        }

        // If clicked, select and show info
        if (clickedUnit != null) {
            selectedCell = clickedCell;
            selectedRow = row;
            selectedCol = col;
            unitInfoPopup.setText(
//                    "Type: " + clickedUnit.getClass().getSimpleName() + "\n" +
                            "HP: " + clickedUnit.getHealth());
//                            "Owner: " + clickedUnit.getOwner().getName());
            unitInfoPopup.setLayoutX(e.getSceneX() + 5);
            unitInfoPopup.setLayoutY(e.getSceneY() - 5);
            unitInfoPopup.setVisible(true);
            renderGrid(); // Show selection highlight
        } else {
            // Empty click or clicking an enemy unit not after a selection
            selectedCell = null;
            selectedRow = -1;
            selectedCol = -1;
            unitInfoPopup.setVisible(false);
            renderGrid();
        }
    }


    private String getUnitImageFilename(Unit unit) {
        String prefix = (unit.getOwner() == gameCore.getPlayers().get(0)) ? "red." : "blue.";
        return switch (unit.getClass().getSimpleName()) {
            case "Tank" -> prefix + "tank.png";
            case "Archer" -> prefix + "archer.png";
            default -> prefix + "soldier.png";
        };
    }

    private void setupControls() {
        Button buyButton = new Button("Buy");
        Button sellButton = new Button("Sell");
        Button saveGameButton = new Button("Save Game");

        buyButton.setOnAction(e -> handleBuy());
        sellButton.setOnAction(e -> {
            try {
                handleSell();
            } catch (NoUnitSelectedException | NotYourUnitException ex) {
                new Alert(Alert.AlertType.WARNING, ex.getMessage()).show();
            }
        });
        saveGameButton.setOnAction(e -> handleSaveGame());

        rightPanel.getChildren().addAll(buyButton, sellButton, saveGameButton);
    }

    private void handleBuy() {
        Stage popupStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        Label prompt = new Label("Choose a unit to buy:");

        Button soldierBtn = new Button("Soldier (Cost: 150)");
        Button archerBtn = new Button("Archer (Cost: 300)");
        Button tankBtn = new Button("Tank (Cost: 400)");

        soldierBtn.setOnAction(e -> {
            unitToBuy = new Soldier(gameCore.getCurrentPlayer());
            isBuying = true;
            popupStage.close();
        });

        archerBtn.setOnAction(e -> {
            unitToBuy = new Archer(gameCore.getCurrentPlayer());
            isBuying = true;
            popupStage.close();
        });

        tankBtn.setOnAction(e -> {
            unitToBuy = new Tank(gameCore.getCurrentPlayer());
            isBuying = true;
            popupStage.close();
        });

        layout.getChildren().addAll(prompt, soldierBtn, archerBtn, tankBtn);
        Scene scene = new Scene(layout);
        popupStage.setScene(scene);
        popupStage.setTitle("Buy Unit");
        popupStage.show();
    }


    private void handleSell() throws NoUnitSelectedException, NotYourUnitException {
        if (selectedCell != null) {
            gameCore.sellUnit(selectedCell);
            selectedCell.setUnit(null);
            selectedCell = null;

            renderGrid();
            updatePlayerInfoLabels();

            System.out.println("Unit sold.");
        }

    }


    private void handleSaveGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter( ".txt","*.txt"));

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            SaveLoadManager.saveGame(file.getAbsolutePath(), gameCore);
            System.out.println("Game saved successfully.");
        }
    }


    private void updatePlayerInfoLabels() {
        currentPlayerLabel.setText("Player: " + gameCore.getCurrentPlayer().getName());
        playerResourcesLabel.setText("Resources: " + gameCore.getCurrentPlayer().getResources());
        currentPlayerUnitsLabel.setText("Units: " + gameCore.getCurrentPlayer().getUnits().size());
        currentPlayerTerritoryLabel.setText("Territory: " + gameCore.getCurrentPlayer().getTerritory().size());
    }

    private void checkDefeat() {
        if (gameCore.checkLooseCondition()) {
            Player loser = gameCore.getCurrentPlayer();
            Player winner = gameCore.getPlayers().stream()
                    .filter(p -> !p.equals(loser))
                    .findFirst()
                    .orElse(null);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);

            if (winner != null) {
                alert.setContentText("Game Over! " + winner.getName() + " wins!");
            } else {
                alert.setContentText("Game Over! All players are defeated.");
            }

            alert.showAndWait();
            stage.close(); // or go to main menu
        }
    }

    public void nextTurn(){
        renderGrid();
        updatePlayerInfoLabels();
        checkDefeat();
    }

    private void updateCellBorder(StackPane cellPane, Cell cell, boolean isSelected) {
        if (isSelected) {
            cellPane.setStyle("-fx-border-color: yellow; -fx-border-width: 1;");
        } else {
            cellPane.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        }
    }
}





