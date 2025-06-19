import Acme.*;
import objectdraw.*;
import java.awt.Color;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Class SnakeSegment creates individual snake segments and moves them
 */
public class SnakeSegment {
  private GridCell gridCell;
  private FilledOval segment;

  /** Ctor SnakeSegment takes in a ref to the grid cell and canvas the
   * segment is to be drawn on then draws it
   *
   * @param gridCell The gridCell that should hold a ref. to the segment
   * @param canvas The canvas the segment is to be drawn on
   */
  public SnakeSegment(GridCell gridCell, DrawingCanvas canvas) {
    this.gridCell = gridCell;
    gridCell.insertSnakeSegment(this);
    segment = new FilledOval(gridCell.getX(), gridCell.getY(),
                             Constants.GRID_CELL_SIZE,
                             Constants.GRID_CELL_SIZE, canvas);
    segment.setColor(Color.WHITE);
  }

  /**
   * Method getGridCell returns the gridCell a segment is in
   *
   * @return The gridCell a snake segment is in
   */
  public GridCell getGridCell() {
    return gridCell;
  }

  /**
   * Method move takes in a gridCell and moves the snake segment from the
   * current cell to the one passed in
   *
   * @param newGridCell Destination grid cell
   */
  public void move(GridCell newGridCell) {
    double dx = newGridCell.getX() - gridCell.getX();
    double dy = newGridCell.getY() - gridCell.getY();
    segment.move(dx,dy);
    gridCell.removeSnakeSegment();
    gridCell = newGridCell;
    gridCell.insertSnakeSegment(this);
  }

  /**
   * Method removeSnakeBody removes the filled oval from the canvas
   */
  public void removeSnakeBody() {
    if (gridCell.hasSnakeSegment()) {
      segment.removeFromCanvas();
    }
  }

}
