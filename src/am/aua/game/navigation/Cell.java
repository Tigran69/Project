package am.aua.game.navigation;

import am.aua.game.players.Player;
import am.aua.game.units.Unit;

public class Cell {
    private final int x;
    private final int y;
    private Player owner;
    private Unit unit;
    private TerrainType terrain;

    public enum TerrainType {
        NORMAL, ROCK, TREE
    }


    public Cell(int x, int y, Player owner, Unit unit, TerrainType terrain) {
        this.x = x;
        this.y = y;
        this.owner = owner;
        this.unit = unit;
        this.terrain = terrain;
    }

    public TerrainType getTerrain() {
        return terrain;
    }

    public void setTerrain(TerrainType terrain) {
        this.terrain = terrain;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void removeUnit() {
        this.unit = null;
    }

    public boolean isOccupied() {
        return unit != null;
    }

    public boolean isPassable() {
        return terrain == TerrainType.NORMAL;
    }

    public void changeOwner(Player newOwner) {
        owner = newOwner;
    }

    public String toString() {
        if (isOccupied())
            return unit.getSymbol();
        return ".";
    }
}
