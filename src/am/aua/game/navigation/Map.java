package am.aua.game.navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the game map as a 2D grid of {@link Cell} objects.
 * The map is generated with random terrain types and has fixed dimensions (15x15).
 * Provides functionality to access and query cells and their neighbors.
 */
public class Map {
    private final int width = 15;
    private final int height = 15;
    private final Cell[][] grid = new Cell[width][height];

    /** ANSI color code to reset terminal text color. */
    public static final String RESET = "\u001B[0m";
    /** ANSI color code for green. */
    public static final String GREEN = "\u001B[32m";
    /** ANSI color code for grey. */
    public static final String GREY = "\u001B[90m";
    /** ANSI color code for red. */
    public static final String ANSI_RED = "\u001B[31m";
    /** ANSI color code for cyan. */
    public static final String ANSI_CYAN = "\u001B[36m";

    /**
     * Constructs a new map and automatically generates its cells with terrain.
     */
    public Map() {
        generateMap();
    }

    /**
     * Generates the terrain for each cell in the map grid randomly.
     * Ensures the top-left and bottom-right cells are set to NORMAL terrain.
     */
    public void generateMap() {
        Random random = new Random();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
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
        grid[0][0].setTerrain(Cell.TerrainType.NORMAL);
        grid[width - 1][height - 1].setTerrain(Cell.TerrainType.NORMAL);
    }

    /**
     * Returns the width of the map.
     *
     * @return the width (number of columns)
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the map.
     *
     * @return the height (number of rows)
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns a list of valid neighboring cells surrounding a given cell,
     * including diagonals.
     *
     * @param cell the central cell
     * @return list of valid neighboring {@link Cell} objects
     */
    public List<Cell> getNeighbouringCells(Cell cell) {
        List<Cell> neighbouringCells = new ArrayList<>();
        int x = cell.getX();
        int y = cell.getY();

        int[][] directions = {
                {-1, 0}, {1, 0},
                {0, -1}, {0, 1},
                {-1, -1}, {1, -1},
                {-1, 1}, {1, 1},
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

    /**
     * Checks if the given coordinates are within the bounds of the map.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if within bounds, false otherwise
     */
    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Returns the 2D grid of all cells in the map.
     *
     * @return 2D array of {@link Cell} objects
     */
    public Cell[][] getGrid() {
        return grid;
    }

    /**
     * Returns the cell at the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the {@link Cell} at the specified location
     */
    public Cell getCellAt(int x, int y) {
        return grid[x][y];
    }
}
