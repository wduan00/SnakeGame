import Acme.*;
import objectdraw.*;
import java.awt.Color;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Class FruitLoop creates fruit loop objects and places them in grids, also
 * handles what happens when they are "eaten"
 */
public class FruitLoop {
  private GridCell gridCell; // Ref to a gridCell
  private FilledOval outer; // Outer loop of fruit loop
  private FilledOval inner; // Inner loop
  private static final int HALF_DIVISOR = 2;

  /**
   * Ctor FruitLoop takes in the color of a loop, the background color, the
   * grid cell to place it in and the canvas to draw it on, then draws it
   *
   * @param color Color of the fruit loop
   * @param bgColor Background color, gives illusion of a "loop"
   * @param gridCell Grid cell to insert the fruit loop into
   * @param canvas The canvas to draw the fruit loop on
   */
  public FruitLoop(Color color, Color bgColor, GridCell gridCell,
                   DrawingCanvas canvas) {
    this.gridCell = gridCell; // set reference to grid cell
    // doubles to help calculate location of a fruit loop in a grid cell
    double cellXCenter = (gridCell.getX() +
                          Constants.GRID_CELL_SIZE / HALF_DIVISOR);
    double cellYCenter = (gridCell.getY() +
                          Constants.GRID_CELL_SIZE / HALF_DIVISOR);
    double outerLoopOffset = (Constants.GRID_CELL_SIZE /
                              Constants.OUTER_SIZE_DIVISOR / HALF_DIVISOR);
    double innerLoopOffset = (Constants.GRID_CELL_SIZE /
          Constants.INNER_SIZE_DIVISOR / HALF_DIVISOR);
    // create the two loops
    outer = new FilledOval(
      cellXCenter - outerLoopOffset,  cellYCenter - outerLoopOffset,
      Constants.GRID_CELL_SIZE / Constants.OUTER_SIZE_DIVISOR,
      Constants.GRID_CELL_SIZE / Constants.OUTER_SIZE_DIVISOR, canvas);
    
    inner = new FilledOval(
      cellXCenter - innerLoopOffset, cellYCenter - innerLoopOffset,
      Constants.GRID_CELL_SIZE / Constants.INNER_SIZE_DIVISOR,
      Constants.GRID_CELL_SIZE / Constants.INNER_SIZE_DIVISOR, canvas);
    // set loop colors and insert reference to the grid cell
    outer.setColor(color);
    inner.setColor(bgColor);
    gridCell.insertFruitLoop(this);
  }
  
  /** 
   * Method eaten removes the fruit loop from a cell and the canvas
   */
  public void eaten() {    
    gridCell.insertFruitLoop(null);
    outer.removeFromCanvas();
    inner.removeFromCanvas();
  }

}
