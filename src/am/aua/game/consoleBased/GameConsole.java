package am.aua.game.consoleBased;

import am.aua.game.exceptions.*;
import am.aua.game.fileIO.SaveLoadManager;
import am.aua.game.gameLogic.GameCore;
import am.aua.game.navigation.Cell;
import am.aua.game.navigation.Map;
import am.aua.game.players.Player;

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

                            System.out.println("1. Attack");
                            System.out.println("2. Move");
                            System.out.println("3. Buy Unit");
                            System.out.println("4. Surrender");
                            System.out.println("5. SaveGame");
                            System.out.println("6. Exit");

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
                                    System.out.println("Buy unit feature not implemented yet.");
                                    break;
                                case 4:
                                    System.out.println("Player " + gameCore.getCurrentPlayer().getName() + " surrendered.");
                                    break;
                                case 5:
                                    System.out.println("Enter file path to save:");
                                    scanner.nextLine();
                                    String path = scanner.nextLine();
                                    gameCore.saveGame(path);
                                    System.out.println("Game saved.");
                                    break;
                                case 6:
                                    System.out.println("Exiting...");
                                    break;
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