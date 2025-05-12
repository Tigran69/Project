package am.aua.game.fileIO;

import am.aua.game.gameLogic.GameCore;
import am.aua.game.navigation.Cell;
import am.aua.game.navigation.Map;
import am.aua.game.players.Player;
import am.aua.game.units.*;

import java.io.*;
import java.util.*;

public class SaveLoadManager {

    public static void saveGame(String filePath, GameCore gameCore) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("TURN:" + gameCore.getTurnCount());
            writer.println("CURRENT_PLAYER:" + gameCore.getCurrentPlayer().getName());
            for (Player player : gameCore.getPlayers()) {
                writer.println("PLAYER:" + player.getName() + ":" + player.getResources());
            }

            Map map = gameCore.getMap();
            for (int x = 0; x < map.getWidth(); x++) {
                for (int y = 0; y < map.getHeight(); y++) {
                    Cell cell = map.getCellAt(x, y);
                    writer.println("TERRAIN:" + x + "," + y + ":" + cell.getTerrain().name());
                }
            }

            for (int x = 0; x < map.getWidth(); x++) {
                for (int y = 0; y < map.getHeight(); y++) {
                    Cell cell = map.getCellAt(x, y);
                    if (cell.isOccupied()) {
                        Unit unit = cell.getUnit();
                        writer.println("UNIT:" + unit.getClass().getSimpleName() + ":" +
                                unit.getPrice() + ":" +
                                unit.getOwner().getName() + ":" +
                                x + "," + y + ":" + cell.getUnit().getHealth());
                    }
                }
            }
            for (Player player : gameCore.getPlayers()) {
                for (Cell cell : player.getTerritory()) {
                    int x = cell.getX();
                    int y = cell.getY();
                    writer.println("TERRITORY:" + player.getName() + ":" + x + "," + y);
                }
            }


        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }


    public static GameCore loadGame(String filePath) {
        List<Player> players = new ArrayList<>();
        Map map = new Map();
        int turn = 0;
        String currentPlayerName = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("TURN:")) {
                    turn = Integer.parseInt(line.split(":")[1]);

                } else if (line.startsWith("PLAYER:")) {
                    String[] parts = line.split(":");
                    Player player = new Player(parts[1]);
                    player.setResources(Double.parseDouble(parts[2]));
                    players.add(player);

                } else if (line.startsWith("TERRAIN:")) {
                    String[] parts = line.split(":");
                    String[] coords = parts[1].split(",");
                    int x = Integer.parseInt(coords[0]);
                    int y = Integer.parseInt(coords[1]);
                    Cell.TerrainType terrain = Cell.TerrainType.valueOf(parts[2]);
                    map.getCellAt(x, y).setTerrain(terrain);

                } else if (line.startsWith("UNIT:")) {
                    String[] parts = line.split(":");
                    String type = parts[1];
                    String ownerName = parts[3];
                    String[] coords = parts[4].split(",");
                    int x = Integer.parseInt(coords[0]);
                    int y = Integer.parseInt(coords[1]);
                    int hp = Integer.parseInt(parts[5]);

                    Player owner = null;
                    for (Player p : players) {
                        if (p.getName().equals(ownerName)) {
                            owner = p;
                            break;
                        }
                    }

                    Unit unit = null;
                    if (type.equals("Tank")) unit = new Tank(owner);
                    else if (type.equals("Archer")) unit = new Archer(owner);
                    else if (type.equals("Soldier")) unit = new Soldier(owner);

                    if (unit != null && owner != null) {
                        unit.setHealth(hp);
                        owner.getUnits().add(unit);
                        Cell cell = map.getCellAt(x, y);
                        cell.setUnit(unit);
                        cell.changeOwner(owner);
                        owner.addToTerritory(cell);
                    }
                } else if (line.startsWith("CURRENT_PLAYER:")) {
                    currentPlayerName = line.split(":")[1];
                }else if (line.startsWith("TERRITORY:")) {
                    String[] parts = line.split(":");
                    String playerName = parts[1];
                    String[] coords = parts[2].split(",");
                    int x = Integer.parseInt(coords[0]);
                    int y = Integer.parseInt(coords[1]);

                    Player owner = null;
                    for (Player p : players) {
                        if (p.getName().equals(playerName)) {
                            owner = p;
                            break;
                        }
                    }

                    if (owner != null) {
                        Cell cell = map.getCellAt(x, y);
                        cell.changeOwner(owner);
                        owner.addToTerritory(cell);
                    }
                }

            }

        } catch (IOException e) {
            System.err.println("Error loading game: " + e.getMessage());
        }

        GameCore core = new GameCore(players, map);
        for (int i = 0; i < turn; i++) {
            core.nextTurn();
        }
        if (currentPlayerName != null) {
            for (Player p : players) {
                if (p.getName().equals(currentPlayerName)) {
                    core.setCurrentPlayer(p);
                    break;
                }
            }
        }
        return core;
    }

}