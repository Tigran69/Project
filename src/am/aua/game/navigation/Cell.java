package am.aua.game.navigation;

import am.aua.game.players.Player;
import am.aua.game.units.Unit;

public class Cell {
    private final int x;
    private final int y;
    private Player owner;
    private Unit unit;

    public Cell(int x, int y, Player owner, Unit unit) {
        this.x = x;
        this.y = y;
        this.owner = owner;
        this.unit = unit;
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

    public void changeOwner(Player newOwner) {
        owner = newOwner;
    }

    public String toString() {
        if (isOccupied())
            return unit.getSymbol();
        return ".";
    }
}
