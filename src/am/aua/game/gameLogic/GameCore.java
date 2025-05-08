package am.aua.game.gameLogic;

import am.aua.game.exceptions.*;
import am.aua.game.fileIO.SaveLoadManager;
import am.aua.game.navigation.Cell;
import am.aua.game.navigation.Map;
import am.aua.game.players.Player;
import am.aua.game.units.Unit;
import java.util.List;

public class GameCore {

    private final List<Player> players;
    private Player currentPlayer;
    private int turnCount;
    private Map map;

    public GameCore(List<Player> players){
        this.players = players;
        this.currentPlayer = players.get(0);
        this.turnCount = 0;
        this.map = new Map();
//        placeUnit(new Soldier(currentPlayer), 0,0,map);
//        nextTurn();
//        placeUnit(new Soldier(currentPlayer), map.getWidth()-1, map.getHeight()-1, map);
//        nextTurn();
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
        return (this.currentPlayer.getResources() <= 0 || this.currentPlayer.getTerritory().isEmpty() && this.turnCount > 5);
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

        int stepX = Integer.compare(dx, 0);
        int stepY = Integer.compare(dy, 0);

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

    public void moveUnit(Player currentPlayer, Unit unit, Cell oldPosition, Cell newPosition)
            throws NotYourUnitException, PathNotClearException, OutOfRangeException {

        if (!unit.getOwner().equals(currentPlayer)) {
            throw new NotYourUnitException();
        }

        if (!isInMovementRange(oldPosition, newPosition)) {
            throw new OutOfRangeException();
        }

        if (!isPathClear(oldPosition, newPosition)) {
            throw new PathNotClearException();
        }

        if (newPosition.isOccupied()) {
            throw new PathNotClearException(); // Or use a new exception
        }

        // Move unit
        oldPosition.setUnit(null);
        newPosition.setUnit(unit);

        if (newPosition.getOwner() != null && !newPosition.getOwner().equals(currentPlayer)) {
            newPosition.getOwner().removeFromTerritory(newPosition);
        }

        newPosition.setOwner(currentPlayer);

        if (!currentPlayer.getTerritory().contains(newPosition)) {
            currentPlayer.addToTerritory(newPosition);
        }
        nextTurn();
    }


    public void attackUnit(Player currentPlayer, Unit unit, Cell oldPosition, Cell newPosition) throws NotYourUnitException, FriendlyFireException, OutOfRangeException, PathNotClearException, NoUnitSelectedException {
        if (unit.getOwner() != currentPlayer)
            throw new NotYourUnitException();

        if (!newPosition.isOccupied())
            throw new NoUnitSelectedException();

        Unit targetUnit = newPosition.getUnit();

        if (targetUnit.getOwner() == this.currentPlayer)
            throw new FriendlyFireException();

        if (!isInAttackRange(oldPosition, newPosition))
            throw new OutOfRangeException();

        int newHealth = targetUnit.getHealth() - unit.getAttackPower();
        targetUnit.setHealth(newHealth);

        if (newHealth <= 0) {
            newPosition.removeUnit();
            targetUnit.getOwner().getUnits().remove(targetUnit);
            moveUnit(currentPlayer,unit,oldPosition,newPosition);
        }
        nextTurn();
    }

    public void buyUnit(Unit unit, int x, int y, Map map) throws CoordinateBlockedException, NotEnoughMoneyException, NotYourTerritoryException {
        if(this.currentPlayer.getResources() >= unit.getPrice()) {
            placeUnit(unit, x, y, map);
            getMap().getCellAt(x, y).setOwner(this.currentPlayer);
            this.currentPlayer.getUnits().add(unit);
            this.currentPlayer.setResources(this.currentPlayer.getResources() - unit.getPrice());
        }
        else {
            throw new NotEnoughMoneyException();
        }
    }

    public void sellUnit(Cell cell) throws NotYourUnitException, NoUnitSelectedException {
        if (cell == null || cell.getUnit() == null){
            return;
        }
        if (!cell.isOccupied()) {
            throw new NoUnitSelectedException();
        } else if (cell.getUnit().getOwner() != this.currentPlayer) {
            throw new NotYourUnitException();
        } else {
            this.currentPlayer.getUnits().remove(cell.getUnit());
            this.currentPlayer.setResources(this.currentPlayer.getResources() + cell.getUnit().getPrice() * 0.5);
        }
    }



    public void placeUnit(Unit u, int x, int y, Map map) throws CoordinateBlockedException, NotYourTerritoryException {
        Cell position = map.getCellAt(x, y);

        if (position.isOccupied()) {
            throw new CoordinateBlockedException("Cell is already occupied.");
        }

        if (!position.isPassable()) {
            throw new CoordinateBlockedException("Cannot place unit on " + position.getTerrain() + " terrain.");
        }

        if (getMap().getCellAt(x, y).getOwner() != this.currentPlayer && getMap().getCellAt(x, y).getOwner() != null) {
            throw new NotYourTerritoryException();
        }

        boolean neighboursOccupied = false;
        for (Cell neighbor : map.getNeighbouringCells(position)) {
            if (neighbor.isOccupied() && !neighbor.getOwner().equals(this.currentPlayer)) {
                neighboursOccupied = true;
                break;
            }
        }

        if (neighboursOccupied) {
            throw new CoordinateBlockedException("Enemy unit is too close.");
        }

        position.setOwner(this.currentPlayer);

        if (!currentPlayer.getTerritory().contains(position)){
            this.currentPlayer.addToTerritory(position);
        }
        position.setUnit(u);
    }


        private boolean isBlockedTerrain(Cell cell) {
        Cell.TerrainType t = cell.getTerrain();
        return t == Cell.TerrainType.TREE || t == Cell.TerrainType.ROCK;
    }

    private boolean isInMovementRange(Cell from, Cell to) {
        currentPlayer.setCurrentUnit(from.getUnit());
        int range = currentPlayer.getCurrentUnit().getMovementRange(); // e.g., 1
        int dx = Math.abs(from.getX() - to.getX());
        int dy = Math.abs(from.getY() - to.getY());

        return Math.max(dx, dy) <= range;
    }


    private boolean isInAttackRange(Cell from, Cell to) {
        currentPlayer.setCurrentUnit(from.getUnit());
        int range = currentPlayer.getCurrentUnit().getAttackRange(); // e.g., 1 or 2
        int dx = Math.abs(from.getX() - to.getX());
        int dy = Math.abs(from.getY() - to.getY());

        return Math.max(dx, dy) <= range;
    }



}
