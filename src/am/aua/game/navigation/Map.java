package am.aua.game.navigation;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private final int width = 20;
    private final int height = 20;
    private final Cell[][] grid = new Cell[width][height];

    public Map(){
        generateMap();      // do we need a logic that does not allow us to create a map more than once?
    }

    public void generateMap(){
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                grid[x][y] = new Cell(x,y,null,null);
            }
        }
    }


    public List<Cell> getNeighbouringCells(Cell cell) {
        List<Cell> neighbouringCells = new ArrayList<Cell>();
        int x = cell.getX();
        int y = cell.getY();

        int[][] directions = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };
        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];

            if (isWithinBounds(newX, newY) && grid[newX][newY] != null) {
                neighbouringCells.add(grid[newX][newY]);
            }
        }
        return neighbouringCells;
    }

    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void printMap(){
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                Cell c = grid[x][y];
                System.out.println(c.isOccupied() ? c.getUnit().getSymbol() : ".");
            }
        }
    }

    public Cell getCellAt(int x, int y){
        return grid[x][y];
    }

}
