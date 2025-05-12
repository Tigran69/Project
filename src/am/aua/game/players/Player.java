package am.aua.game.players;

import am.aua.game.navigation.Cell;
import am.aua.game.units.Unit;

import java.util.ArrayList;

/**
 * Represents a player in the game, tracking their name, resources,
 * controlled units, owned territory, and current unit selection.
 * Each player is identified by an abbreviation derived from their name.
 */
public class Player {
    /** The name of the player. */
    private final String name;

    /** A single-character abbreviation used to represent the player. */
    private final char abbreviation;

    /** The current amount of resources the player has. */
    private double resources;

    /** The list of units under the control of the player. */
    private final ArrayList<Unit> units;

    /** The list of cells (territory) currently owned by the player. */
    private final ArrayList<Cell> territory;

    /** The unit currently selected by the player (e.g., for actions). */
    private Unit currentUnit;

    /**
     * Constructs a new player with the given name and default resources.
     * The abbreviation is set to the first character of the name.
     *
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
        this.resources = 1500;
        this.units = new ArrayList<>();
        this.territory = new ArrayList<>();
        this.abbreviation = name.charAt(0);
    }

    /**
     * Returns the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the current amount of resources the player has.
     *
     * @return the player's resources
     */
    public double getResources() {
        return resources;
    }

    /**
     * Sets the amount of resources the player has.
     *
     * @param resources the new resource value
     */
    public void setResources(double resources) {
        this.resources = resources;
    }

    /**
     * Returns the currently selected unit for the player.
     *
     * @return the selected {@link Unit}
     */
    public Unit getCurrentUnit() {
        return currentUnit;
    }

    /**
     * Sets the currently selected unit for the player.
     *
     * @param currentUnit the unit to set as selected
     */
    public void setCurrentUnit(Unit currentUnit) {
        this.currentUnit = currentUnit;
    }

    /**
     * Returns the list of units controlled by the player.
     *
     * @return the list of {@link Unit} objects
     */
    public ArrayList<Unit> getUnits() {
        return units;
    }

    /**
     * Returns the list of map cells that make up the player's territory.
     *
     * @return the list of {@link Cell} objects
     */
    public ArrayList<Cell> getTerritory() {
        return territory;
    }

    /**
     * Adds a cell to the player's territory.
     *
     * @param cell the {@link Cell} to add
     */
    public void addToTerritory(Cell cell) {
        this.territory.add(cell);
    }

    /**
     * Removes a cell from the player's territory.
     *
     * @param cell the {@link Cell} to remove
     */
    public void removeFromTerritory(Cell cell) {
        this.territory.remove(cell);
    }

    /**
     * Returns the character abbreviation used to identify the player.
     *
     * @return the abbreviation character
     */
    public char getAbbreviation() {
        return abbreviation;
    }
}
