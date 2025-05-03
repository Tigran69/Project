package am.aua.game.navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Map {
    private final int width = 20;
    private final int height = 20;
    private final Cell[][] grid = new Cell[width][height];


    public Map(){
        generateMap();      // do we need a logic that does not allow us to create a map more than once?
    }

    public void generateMap(){
        Random random = new Random();
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                Cell.TerrainType terrain;
                int r = random.nextInt(100);
                if (r < 10) {
                    terrain = Cell.TerrainType.ROCK;
                } else if (r < 20) {
                    terrain = Cell.TerrainType.TREE;
                } else {
                    terrain = Cell.TerrainType.NORMAL;
                }

                grid[x][y] = new Cell(x, y, null, null, terrain);
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
                {0, 1},
                {-1, -1},
                {1, -1},
                {-1, 1},
                {1, 1},
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

    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String GREY = "\u001B[90m";

    public void printMap() {
        System.out.print("   ");
        for (int y = 0; y < height; y++) {
            System.out.printf("%2d ", y);
        }
        System.out.println();

        for (int x = 0; x < width; x++) {
            System.out.printf("%2d ", x);
            for (int y = 0; y < height; y++) {
                Cell c = grid[x][y];
                String symbol = ".";
                String color = "";

                if (c.isOccupied()) {
                    String unitSymbol = c.getUnit().getSymbol();
                    if (c.getOwner() != null && c.getOwner().getName().equals("Player1")) {
                        symbol = unitSymbol.toUpperCase();
                    } else {
                        symbol = unitSymbol.toLowerCase();
                    }
                } else {
                    switch (c.getTerrain()) {
                        case TREE:
                            symbol = "T";
                            color = GREEN;
                            break;
                        case ROCK:
                            symbol = "R";
                            color = GREY;
                            break;
                        default:
                            symbol = "O";
                            break;
                    }
                }

                System.out.print(" " + color + symbol + RESET + " ");
            }
            System.out.println();
        }
    }




    public Cell[][] getGrid(){
        return grid;
    }

    public Cell getCellAt(int x, int y){
        return grid[x][y];
    }

}
