package am.aua.game.gameLogic;

import am.aua.game.fileIO.SaveLoadManager;
import am.aua.game.navigation.Cell;
import am.aua.game.navigation.Map;
import am.aua.game.players.Player;
import am.aua.game.units.Unit;
import am.aua.game.exceptions.*;

import java.util.List;

public class GameCore {

    private final List<Player> players;
    private Player currentPlayer;
    private int turnCount;
    private Map map;

    public GameCore(List<Player> players) {
        this.players = players;
        this.currentPlayer = players.get(0);
        this.turnCount = 0;
        this.map = new Map();
    }

    public GameCore(List<Player> players, Map map) {
        this.players = players;
        this.map = map;
    }

    public void startGame(){
        map.generateMap();
    }

    public void nextTurn(){
        turnCount++;
        collectRecourses();
        int currentIndex = players.indexOf(currentPlayer);
        currentPlayer = players.get((currentIndex + 1) % players.size());
    }

    public void collectRecourses(){
        if (this.turnCount != 0 && this.turnCount % 5 == 0){
            players.get(0).setResources(players.get(0).getResources() + 200);
            players.get(1).setResources(players.get(1).getResources() + 200);
        }
    }

    public boolean checkLooseCondition(){
        return (this.currentPlayer.getUnits().isEmpty() || this.currentPlayer.getTerritory().isEmpty()) && turnCount != 0;
    }

    public int getTurnCount(){
        return this.turnCount;
    }

    public Map getMap() {
        return this.map;
    }

    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void saveGame(String filePath){
        SaveLoadManager.saveGame(filePath, this);
    }

    public void loadGame(String filePath){
        SaveLoadManager.loadGame(filePath);
    }


    public void setTurnCount(int turn) {
        this.turnCount = turn;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public boolean isPathClear(Cell from, Cell to) {
        int x0 = from.getX();
        int y0 = from.getY();
        int x1 = to.getX();
        int y1 = to.getY();

        int dx = x1 - x0;
        int dy = y1 - y0;

        if (dx != 0 && dy != 0 && Math.abs(dx) != Math.abs(dy)) {
            return false;
        }

        int stepX = Integer.compare(dx, 0); // -1, 0, or 1
        int stepY = Integer.compare(dy, 0); // -1, 0, or 1

        int x = x0 + stepX;
        int y = y0 + stepY;

        while (x != x1 || y != y1) {
            Cell cell = map.getCellAt(x, y);
            if (cell.isOccupied() || isBlockedTerrain(cell)) {
                return false;
            }
            x += stepX;
            y += stepY;
        }

        Cell dest = map.getCellAt(x1, y1);
        return !dest.isOccupied() && !isBlockedTerrain(dest);
    }

    public void moveUnit(Player currentPlayer, Unit unit, Cell oldPosition, Cell newPosition) throws NotYourUnitException, PathNotClearException{ 
        if(unit.getOwner().equals(currentPlayer)){
            if (isPathClear(oldPosition, newPosition)) {
                    oldPosition.setUnit(null);
                    newPosition.setUnit(unit);
                    newPosition.setOwner(currentPlayer);
                }
                else {
                        throw new PathNotClearException();
                }
        }
                else {
                    throw new NotYourUnitException();
                }
    }

    public void attackUnit(Player currentPlayer, Unit unit, Cell oldPosition, Cell newPosition)  throws NotYourUnitException, FriendlyFireException, OutOfRangeException {
        if (!unit.getOwner().equals(currentPlayer)) 
            throw new NotYourUnitException();
    
        if (!newPosition.isOccupied())
            return;

        Unit targetUnit = newPosition.getUnit();

        if (targetUnit.getOwner().equals(currentPlayer)) 
            throw new FriendlyFireException();
        
        if (!isInAttackRange(oldPosition, newPosition)) 
            throw new OutOfRangeException();

        int newHealth = targetUnit.getHealth() - unit.getAttackPower();
        targetUnit.setHealth(newHealth);

        if (newHealth <= 0) {
            newPosition.removeUnit();
            targetUnit.getOwner().getUnits().remove(targetUnit);
        }
    }

    private boolean isBlockedTerrain(Cell cell) {
        Cell.TerrainType t = cell.getTerrain();
        return t == Cell.TerrainType.TREE || t == Cell.TerrainType.ROCK;
    }

    private boolean isInMovementRange(Cell from, Cell to) {
        int range = currentPlayer.getCurrentUnit().getMovementRange(); // e.g., 1
        int dx = Math.abs(from.getX() - to.getX());
        int dy = Math.abs(from.getY() - to.getY());

        return Math.max(dx, dy) <= range;
    }


    private boolean isInAttackRange(Cell from, Cell to) {
        int range = currentPlayer.getCurrentUnit().getAttackRange(); // e.g., 1 or 2
        int dx = Math.abs(from.getX() - to.getX());
        int dy = Math.abs(from.getY() - to.getY());

        return Math.max(dx, dy) <= range;
    }

}
