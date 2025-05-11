package am.aua.game.consoleBased;

import am.aua.game.exceptions.*;
import am.aua.game.fileIO.SaveLoadManager;
import am.aua.game.gameLogic.GameCore;
import am.aua.game.navigation.Cell;
import am.aua.game.navigation.Map;
import am.aua.game.players.Player;
import am.aua.game.gameLogic.*;
import am.aua.game.units.*;

import java.util.ArrayList;
import java.util.Scanner;

public class GameConsole {

    public static void startGame() throws NotYourUnitException, InvalidPathException, MalformedStringException, PathNotClearException, NotYourTerritoryException, CoordinateBlockedException {
        System.out.println("Game Started");
        System.out.println("Choose option");
        System.out.println("1. Play Game");
        System.out.println("2. Exit");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1: {
                System.out.println("1. New game");
                System.out.println("2. Load game");
                System.out.println("3. Exit");
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1: {
                        ArrayList<Player> players = new ArrayList<>();
                        System.out.println("First Player - ");
                        String firstName = scanner.nextLine();
                        System.out.println("Second Player - ");
                        String secondName = scanner.nextLine();
                        players.add(new Player(firstName));
                        players.add(new Player(secondName));

                        GameCore gameCore = new GameCore(players);
                        System.out.println("THE GAME STARTED");

                        while (!gameCore.checkLooseCondition()) {
                            System.out.println(gameCore.getCurrentPlayer().getName() + "'s turn");
                            printMap(gameCore,gameCore.getMap());

                            System.out.println("1. Move");
                            System.out.println("2. Attack");
                            System.out.println("3. Buy Unit");
                            System.out.println("4. Sell Unit");
                            System.out.println("5. Save Game");
                            System.out.println("6. Load Game");
                            System.out.println("7. Surrender");
                            System.out.println("8. Exit");

                            int playerChoice = scanner.nextInt();

                            switch (playerChoice) {
                                case 1:
                                    System.out.println("Write the coordinates of your unit");
                                    int x = scanner.nextInt();
                                    int y = scanner.nextInt();
                                    System.out.println("Write the coordinates of enemy's unit");
                                    int eX = scanner.nextInt();
                                    int eY = scanner.nextInt();

                                    try {
                                        gameCore.attackUnit(gameCore.getCurrentPlayer(),
                                                gameCore.getMap().getCellAt(x, y).getUnit(),
                                                gameCore.getMap().getCellAt(x, y),
                                                gameCore.getMap().getCellAt(eX, eY));
                                        System.out.println("Attack successful");
                                    } catch (Exception e) {
                                        System.out.println("Attack failed: " + e.getMessage());
                                    }

                                    gameCore.nextTurn();
                                    break;

                                case 2:
                                    System.out.println("Write the coordinates of your unit");
                                    int xMove = scanner.nextInt();
                                    int yMove = scanner.nextInt();
                                    System.out.println("Write the coordinates of destination cell");
                                    int xDest = scanner.nextInt();
                                    int yDest = scanner.nextInt();

                                    try {
                                        gameCore.moveUnit(gameCore.getCurrentPlayer(),
                                                gameCore.getMap().getCellAt(xMove, yMove).getUnit(),
                                                gameCore.getMap().getCellAt(xMove, yMove),
                                                gameCore.getMap().getCellAt(xDest, yDest));
                                        System.out.println("Move successful");
                                    } catch (Exception e) {
                                        System.out.println("Move failed: " + e.getMessage());
                                    }

                                    gameCore.nextTurn();
                                    break;

                                case 3:
                                scanner.nextLine();
                                System.out.println("Choose unit to buy: 1. Soldier, 2. Archer, 3. Tank");
                                int unitChoice = scanner.nextInt();
                                System.out.println("Enter coordinates to place the unit (x y):");
                                int buyX = scanner.nextInt();
                                int buyY = scanner.nextInt();

                                Unit unitToBuy = null;
                                switch (unitChoice) {
                                    case 1 -> unitToBuy = new Soldier(gameCore.getCurrentPlayer());
                                    case 2 -> unitToBuy = new Archer(gameCore.getCurrentPlayer());
                                    case 3 -> unitToBuy = new Tank(gameCore.getCurrentPlayer());
                                    default -> System.out.println("Invalid unit choice.");
                                }

                                if (unitToBuy != null) {
                                    try {
                                        gameCore.buyUnit(unitToBuy, buyX, buyY, gameCore.getMap());
                                        System.out.println("Unit purchased.");
                                    } catch (Exception e) {
                                        System.out.println("Could not buy unit: " + e.getMessage());
                                    }
                                }
                                gameCore.nextTurn();
                                break;

                            case 4:
                                System.out.println("Enter coordinates of unit to sell (x y):");
                                int sellX = scanner.nextInt();
                                int sellY = scanner.nextInt();
                                try {
                                    gameCore.sellUnit(gameCore.getMap().getCellAt(sellX, sellY));
                                    System.out.println("Unit sold.");
                                } catch (Exception e) {
                                    System.out.println("Could not sell unit: " + e.getMessage());
                                }
                                gameCore.nextTurn();
                                break;

                            case 5:
                                System.out.println("Enter file path to save:");
                                scanner.nextLine();
                                String savePath = scanner.nextLine();
                                gameCore.saveGame(savePath);
                                System.out.println("Game saved.");
                                break;

                            case 6:
                                System.out.println("Enter file path to load:");
                                scanner.nextLine();
                                String loadPath = scanner.nextLine();
                                gameCore = SaveLoadManager.loadGame(loadPath);
                                System.out.println("Game loaded.");
                                break;

                            case 7:
                                System.out.println("Player " + gameCore.getCurrentPlayer().getName() + " surrendered.");
                                return;

                            case 8:
                                System.out.println("Exiting...");
                                return;
                                default:
                                    System.out.println("Invalid option");
                            }
                        }
                        System.out.println("Game Over. " + gameCore.getCurrentPlayer().getName() + " lost.");
                        break;
                    }
                    case 2:
                        System.out.println("Loading game...");
                        GameCore gameCore = SaveLoadManager.loadGame(System.getProperty("user.dir") + "\\src\\am\\aua\\game\\gameSave.txt");
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
                break;
            }
            case 2:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    public static void printMap(GameCore gameCore, Map map) {
        Cell[][] grid = map.getGrid();
        int height = grid[0].length;
        int width = grid.length;

        System.out.print("   ");
        for (int y = 0; y < height; y++) {
            System.out.printf("%2d ", y);
        }
        System.out.println();

        for (int x = 0; x < width; x++) {
            System.out.printf("%2d ", x);
            for (int y = 0; y < height; y++) {
                Cell c = grid[x][y];
                String symbol = ".";
                String color = "";

                if (c.isOccupied()) {
                    symbol = c.getUnit().getSymbol();
                    if(c.getOwner() == gameCore.getPlayers().get(0)){
                        color = Map.ANSI_RED;
                    }
                    else {
                        color = Map.ANSI_CYAN;
                    }
                } else {
                    switch (c.getTerrain()) {
                        case TREE -> {
                            symbol = "T";
                            color = Map.GREEN;
                        }
                        case ROCK -> {
                            symbol = "R";
                            color = Map.GREY;
                        }
                        default -> {
                            symbol = "O";
                        }
                    }
                }

                System.out.print(" " + color + symbol + Map.RESET + " ");
            }
            System.out.println();
        }
    }
}