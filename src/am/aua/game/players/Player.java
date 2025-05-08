package am.aua.game.players;
import am.aua.game.navigation.Cell;
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

    public ArrayList<Cell> getTerritory() {
        return territory;
    }

    public void addToTerritory(Cell cell) {
        this.territory.add(cell);
    }

    public void removeFromTerritory(Cell cell) {
        this.territory.remove(cell);
    }

    public char getAbbreviation() {
        return abbreviation;
    }



}
