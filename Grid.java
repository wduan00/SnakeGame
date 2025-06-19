import Acme.*;
import objectdraw.*;
import java.awt.Color;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Class Grid creates the grid we see on the canvas and maintains a 2-D array
 * to keep track of the cells
 */
public class Grid {
  private GridCell[][] grids; // Declare 2D array to hold gridcell locations
  private GridCell gridCell; // reference to a gridCell
  private FilledRect grid; // Declare FilledRect to be used as background
  private Random random; // Declare random object
  private int rows;
  private int columns;
  private int numEmptyCells;
  /**
   * Ctor. Grid takes in the number of rows and columns to be drawn, as well as
   * the canvas to draw it on
   *
   * @param rows Number of rows to be drawn
   * @param columns Number of columns to be drawn
   * @param canvas The canvas to draw the grid on
   */
  public Grid(int rows, int columns, DrawingCanvas canvas) {
    grids = new GridCell[rows][columns];
    for(int i = 0; i < rows; i++) {
      for(int j = 0; j < columns; j++) {
        grids[i][j] = new GridCell(i, j, this);
      }
    }
    random = new Random();
    draw(canvas);
    this.rows = rows;
    this.columns = columns;
  }
  
  /**
   * Method  resets the game so that all the cells are empty
   */
  public void reset() {
    for(int i = 0; i < rows; i++) {
      for(int j = 0; j < columns; j++) {
        if (grids[i][j].hasFruitLoop()) {
          grids[i][j].removeFruitLoop();
        }
        if (grids[i][j].hasSnakeSegment()) {
          grids[i][j].removeSnakeBody();
          grids[i][j].removeSnakeSegment();
        }
      }
    }
  }

  /**
   * Method gridHeight returns the height of the grid
   *
   * @return Grid height
   */
  public int gridHeight() {
    return (rows * Constants.GRID_CELL_SIZE);
  }

  /**
   * Method gridWidth returns the width of the grid
   *
   * @return Grid width
   */
  public int gridWidth() {
    return (columns * Constants.GRID_CELL_SIZE);
  }

  /**
   * Method getGridCell takes in a row and column then returns the grid cell at
   * that index
   *
   * @param row value of index of row
   * @param column value of index of column
   * @return The grid cell at a given index, return null if out of grid
   */
  public GridCell getGridCell(int row, int column) {
    if (row < 0 || row == rows) {
      return null;
    }
    if (column < 0 || column == columns) {
      return null;
    }
    return grids[row][column];
  }

  /**
   * Method getCellNeighbor takes in a cell and returns the cell right next to
   * it in the direction passed in
   *
   * @param cell reference to a GridCell
   * @param dir direction the cell points to
   * @return The grid cell next to a given grid cell, in the given direction
   */
  public GridCell getCellNeighbor(GridCell cell, Direction dir) {
    return getGridCell(cell.getRow() + dir.getY(),
                       cell.getColumn() + dir.getX());
  }
  
  /**
   * Method getRandomEmptyCell generates random row and column and returns the
   * gridCell at that index if it's empty
   *
   * @return Random empty grid cell
   */
  public GridCell getRandomEmptyCell() {
    int randRow = random.nextInt(rows);
    int randCol = random.nextInt(columns);
    while (!grids[randRow][randCol].isEmpty()) {
      randRow = random.nextInt(rows);
      randCol = random.nextInt(columns);
    }
    return grids[randRow][randCol];
  }

  /**
   * Method draw takes in the canvas to draw the grid on, then draws it
   *
   * @param canvas Canvas to draw the grid on
   */
  public void draw(DrawingCanvas canvas) {
    double x = 0;
    double y = 0;
    Location start;
    Location end;
    // Creates filled rect that is the background
    grid = new FilledRect(0, 0, columns * Constants.GRID_CELL_SIZE,
                          rows * Constants.GRID_CELL_SIZE, canvas);
    grid.setColor(Constants.BACKGROUND_COLOR);
    // add all row lines
    for(int i = 0; i <= rows; i++) {
      y = i * Constants.GRID_CELL_SIZE;
      start = new Location(0, y);
      end = new Location(columns * Constants.GRID_CELL_SIZE, y);
      (new Line(start, end, canvas)).setColor(Constants.GRID_LINE_COLOR);
    }
    // add all column lines
    for(int i = 0; i <= columns; i++) {
      x = i * Constants.GRID_CELL_SIZE;
      start = new Location(x, 0);
      end = new Location(x, rows * Constants.GRID_CELL_SIZE);
      (new Line(start, end, canvas)).setColor(Constants.GRID_LINE_COLOR);
    }
  }
  
  /**
   * Method changeEmptyCells takes in a delta to change the number of empty
   * cells by
   *
   * @param delta How much to change numEmptyCells by
   */
  public void changeEmptyCells(int delta) {
    numEmptyCells += delta;
  }
  
  /**
   * Method getEmptyCells returns the number of empty cells there are in the
   * grid
   *
   * @return Number of empty cells in the grid
   */
  public int getEmptyCells() {
    return numEmptyCells;
  }

}
