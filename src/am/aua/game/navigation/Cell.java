package am.aua.game.navigation;

import am.aua.game.players.Player;
import am.aua.game.units.Unit;

/**
 * Represents a single cell (tile) on the game map grid.
 * Each cell has coordinates, may belong to a player, may contain a unit,
 * and has a specific terrain type which may affect movement and gameplay.
 */
public class Cell {
    private final int x;
    private final int y;
    private Player owner;
    private Unit unit;
    private TerrainType terrain;

    /**
     * Enum representing different types of terrain a cell can have.
     */
    public enum TerrainType {
        NORMAL, ROCK, TREE
    }

    /**
     * Constructs a new Cell with given coordinates, owner, unit, and terrain type.
     *
     * @param x       the x-coordinate of the cell
     * @param y       the y-coordinate of the cell
     * @param owner   the player who owns this cell (may be null)
     * @param unit    the unit currently on this cell (may be null)
     * @param terrain the terrain type of the cell
     */
    public Cell(int x, int y, Player owner, Unit unit, TerrainType terrain) {
        this.x = x;
        this.y = y;
        this.owner = owner;
        this.unit = unit;
        this.terrain = terrain;
    }

    /**
     * Returns the terrain type of this cell.
     *
     * @return the terrain type
     */
    public TerrainType getTerrain() {
        return terrain;
    }

    /**
     * Sets the terrain type of this cell.
     *
     * @param terrain the new terrain type
     */
    public void setTerrain(TerrainType terrain) {
        this.terrain = terrain;
    }

    /**
     * Returns the x-coordinate of the cell.
     *
     * @return x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the cell.
     *
     * @return y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the player who owns this cell.
     *
     * @return the owner player (may be null)
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Sets the owner of this cell.
     *
     * @param owner the new owner player
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Returns the unit currently occupying this cell.
     *
     * @return the unit (may be null)
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * Places a unit on this cell.
     *
     * @param unit the unit to place
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * Removes any unit currently on this cell.
     */
    public void removeUnit() {
        this.unit = null;
    }

    /**
     * Checks whether this cell is currently occupied by a unit.
     *
     * @return true if a unit is on this cell, false otherwise
     */
    public boolean isOccupied() {
        return unit != null;
    }

    /**
     * Checks whether this cell is passable (can be traversed by units).
     * Only cells with {@code NORMAL} terrain are considered passable.
     *
     * @return true if the terrain is NORMAL, false otherwise
     */
    public boolean isPassable() {
        return terrain == TerrainType.NORMAL;
    }

    /**
     * Changes the ownership of this cell to a new player.
     *
     * @param newOwner the new owner
     */
    public void changeOwner(Player newOwner) {
        owner = newOwner;
    }

    /**
     * Returns a string representation of this cell.
     * If occupied, it returns the unit's symbol; otherwise, it returns ".".
     *
     * @return a string representing the cell
     */
    @Override
    public String toString() {
        if (isOccupied())
            return unit.getSymbol();
        return ".";
    }
}
