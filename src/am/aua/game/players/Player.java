package am.aua.game.players;
import am.aua.game.navigation.Cell;
import am.aua.game.units.Unit;

import java.util.*;

public class Player {
    private final String name;

    private double resources;

    private final ArrayList<Unit> units;

    private final ArrayList<Cell> territory;

    public Player(String name) {
        this.name = name;
        this.resources = 1500;
        this.units = new ArrayList<>();
        this.territory = new ArrayList<>();
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

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void buyUnit(Unit unit) {
        this.units.add(unit);
        this.resources -= unit.getPrice();
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

    public void collectResources() {

    }

    public void moveUnit(Unit u, Cell newPosition) {

    }



}
