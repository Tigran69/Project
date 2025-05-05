package am.aua.game.players;
import am.aua.game.exceptions.CoordinateBlockedException;
import am.aua.game.exceptions.NotEnoughMoneyException;
import am.aua.game.navigation.Cell;
import am.aua.game.navigation.Map;
import am.aua.game.units.Unit;

import java.util.*;

public class Player {
    private final String name;

    private final char abbreviation;

    private double resources;

    private final ArrayList<Unit> units;

    private final ArrayList<Cell> territory;

    private Unit currentUnit;

    public Player(String name) {
        this.name = name;
        this.resources = 1500;
        this.units = new ArrayList<>();
        this.territory = new ArrayList<>();
        this.abbreviation = name.charAt(0);
    }

    public String getName() {
        return name;
    }

    public double getResources() {
        return resources;
    }

    public void setResources(double resources) {
        this.resources = resources;
    }

    public Unit getCurrentUnit() {
        return currentUnit;
    }

    public void setCurrentUnit(Unit currentUnit) {
        this.currentUnit = currentUnit;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void buyUnit(Unit unit, int x, int y, Map map) throws CoordinateBlockedException, NotEnoughMoneyException {
            if(this.resources >= unit.getPrice()) {
                placeUnit(unit, x, y, map);
                this.units.add(unit);
                this.resources -= unit.getPrice();
            }
            else {
                throw new NotEnoughMoneyException();
            }
    }

    public void sellUnit(Unit unit) {
        this.units.remove(unit);
        this.resources += unit.getPrice() * 0.5;
    }

    public ArrayList<Cell> getTerritory() {
        return territory;
    }

    public void addToTerritory(Cell cell) {
        this.territory.add(cell);
    }

    public void removeFromTerritory(Cell cell) {
        this.territory.remove(cell);
    }

    public void moveUnit(Unit u, Cell oldPosition, Cell newPosition) throws CoordinateBlockedException {
        if (!newPosition.isOccupied() && newPosition.getTerrain() == Cell.TerrainType.NORMAL) {
            newPosition.setOwner(this);
            oldPosition.setUnit(null);
            newPosition.setUnit(u);
        }
        else {
            throw new CoordinateBlockedException("Coordinate Blocked!");
        }
    }

    public void placeUnit(Unit u, int x, int y, Map map) throws CoordinateBlockedException {
        Cell position = map.getCellAt(x, y);

        if (position.isOccupied()) {
            throw new CoordinateBlockedException("Cell is already occupied.");
        }

        if (!position.isPassable()) {
            throw new CoordinateBlockedException("Cannot place unit on " + position.getTerrain() + " terrain.");
        }

        boolean neighboursOccupied = false;
        for (Cell neighbor : map.getNeighbouringCells(position)) {
            if (neighbor.isOccupied() && !neighbor.getOwner().equals(this)) {
                neighboursOccupied = true;
                break;
            }
        }

        if (neighboursOccupied) {
            throw new CoordinateBlockedException("Enemy unit is too close.");
        }

        position.setOwner(this);
        position.setUnit(u);
    }


    public char getAbbreviation() {
        return abbreviation;
    }



}
