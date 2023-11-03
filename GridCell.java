/*
 * Filename: GridCell.java
 * Author:   William Duan
 * UserId:   cs11favj
 * Date:     11/16/2018
 * Sources of help: Class handouts, javadocs
 */
 
import Acme.*;
import objectdraw.*;
import java.awt.Color;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Class GridCell is responsible for generating the locations of each cell as
 * well as keeping track of the objects in it
 */
public class GridCell {
  private double x; // x location of center of grid
  private double y; // y location of center of grid
  private int row;
  private int column;
  private FruitLoop fruitLoop; // Reference to fruitloop
  private SnakeSegment snakeSegment; // Reference to SnakeSegment
  private Grid grid; // Reference to grid

  /**
   * Ctor GridCell takes in a row and column, and a ref to grid, then
   * calculates its index and the actual coordinates
   *
   * @param row The row index of the grid cell
   * @param column The column index of the grid cell
   * @param grid A reference to the grid
   */
  public GridCell(int row, int column, Grid grid) {
    x = (column) * PA8Constants.GRID_CELL_SIZE;
    y = (row) * PA8Constants.GRID_CELL_SIZE;
    this.row = row;
    this.column = column;
    this.grid = grid;
  } // end of ctor

  /**
   * Method insertFruitLoop takes in a fruitLoop and assigns the fruitLoop
   * reference in this class to it
   *
   * @param fruitLoop The fruitLoop that this.fruitLoop will be assigned to
   */
  public void insertFruitLoop(FruitLoop fruitLoop) {
    this.fruitLoop = fruitLoop;
  } // end of inserFruitLoop

  /**
   * Method insertSnakeSegment takes in a snakeSegment and assigns the
   * snakeSegment reference in this class to it
   *
   * @param snakeSegment The snakeSegment that this.snakeSegment will be
   *                     assigned to
   */ 
  public void insertSnakeSegment(SnakeSegment snakeSegment) {
    this.snakeSegment = snakeSegment;
  } // end of insertSnakeSegment

  /**
   * Method removeFruitLoop calls the eaten method of FruitLoop
   */
  public void removeFruitLoop() {
    if (fruitLoop != null) {
      fruitLoop.eaten();
    }  
  } // end of removeFruitLoop

  /**
   * Method removeSnakeSegment removes the segment from the canvas
   */
  public void removeSnakeSegment() {
    if (snakeSegment != null) {
      this.insertSnakeSegment(null);
    }
  } // end of removeSnakeSegment
  
  /**
   * Method removeSnakeBody removes the actual ovals from the canvas
   */
  public void removeSnakeBody() {
    snakeSegment.removeSnakeBody();
  }

  /**
   * Method getRow returns the row index of the grid cell
   *
   * @return row index of the grid cell
   */
  public int getRow() {
    return row;
  } // end of getRow

  /**
   * Method getColumn returns the column index of the grid cell
   *
   * @return column index of the grid cell
   */
  public int getColumn() {
    return column;
  } // end of getColumn

  /**
   * Method getX returns the x-coord. of the grid cell
   *
   * @return x-coord. of the grid cell
   */
  public double getX() {
    return x;
  } // end of getX

  /**
   * Method getY returns the y-coord. of the grid cell
   *
   * @return y-coord. of the grid cell
   */
  public double getY() {
    return y;
  } // end of getY

  /**
   * Method isEmpty returns a boolean -- true if there are no objects in the
   * grid cell, false if there is a segment or fruit loop
   *
   * @return Whether or not the grid cell is empty
   */
  public boolean isEmpty() {
    return !(hasFruitLoop() || hasSnakeSegment());
  } // end of isEmpty
  
  /**
   * Method hasFruitLoop returns whether or not a grid cell contains a fruit
   * loop
   *
   * @return Whether or not the grid cell has a fruit loop
   */
  public boolean hasFruitLoop() {
    return (fruitLoop != null);
  } // end of hasFruitLoop

  /**
   * Method hasSnakeSegment returns whether or not a grid cell contains a
   * snake segment
   *
   * @return Whether or not the grid cell has a snake segment
   */
  public boolean hasSnakeSegment() {
    return (snakeSegment != null);
  } // end of hasSnakeSegment
 
 } // end of class
