import Acme.*;
import objectdraw.*;
import java.awt.Color;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Class Snake is responsible for creating the snake and handling its movement
 * based on the next cell
 */
public class Snake extends ActiveObject {
  // Declare objects to be used in this class
  private SnakeSegment head;
  private ArrayList<SnakeSegment> body;
  // References to other classes
  private SnakeGame snakeGame;
  private Grid grid;
  private DrawingCanvas canvas;
  private Direction direction = Direction.NONE;
  // variables used in this class
  private int delay;
  private boolean isPaused = false;
  private int colorIdx = 0;
  private boolean reset = false;
  private static final int COLOR_ARRAY_LENGTH = 5;
  private boolean gameWon = false;

  /**
   * Ctor Snake takes in a ref to snakeGame, the grid, the canvas, and the
   * delay
   * Creates a head segment and an array list to hold segments of the body
   *
   * @param snakeGame Reference to the snake game
   * @param grid Reference to the grid
   * @param delay Milliseconds between each snake movement
   * @param canvas Reference to the canvas
   */
  public Snake(SnakeGame snakeGame, Grid grid, int delay, DrawingCanvas canvas) {
    this.snakeGame = snakeGame;
    this.grid = grid;
    this.delay = delay;
    this.canvas = canvas;
    GridCell headCell = grid.getRandomEmptyCell();
    head = new SnakeSegment(headCell, canvas);
    body = new ArrayList<SnakeSegment>();
    start();
  }
  
  /**
   * Method set direction changes the direction of the snake based on the key
   * pressed
   *
   * @param dir Direction enum, tells the snake what direction to move
   */
  public void setDirection(Direction dir) {
    direction = dir;
  }
  
  /**
   * Method setGameWon sets the gameWon status to whatever is passed in
   *
   * @param gameWon Whether or not the game has been won
   */
  public void setGameWon (boolean gameWon) {
    this.gameWon = gameWon;
  }
  
  /**
   * Method GetDirection returns the current direction the snake is moving
   *
   * @return the direction the snake is currently moving
   */
  public Direction getDirection() {
    return direction;
  }

  /**
   * Method snakeLength returns the length of the snake
   *
   * @return Length of the snake
   */
  public int snakeLength() {
    return body.size() + 1;
  }

  /**
   * Method move is responsible for the behavior of the snake based on the next
   * cell in the direction its moving
   * If the next cell is empty or contains a snake segment, it dies
   * If it has a fruit loop, it adds a segment to the snake and keeps moving
   * If there's nothing in the way, it moves
   *
   * @return Whether or not the move was successful
   */
  public boolean move() {
    // create new cells based off the loc. of the head's cell, to be used as
    // references
    GridCell currCell = new GridCell(
      head.getGridCell().getRow(), head.getGridCell().getColumn(), grid);
    GridCell newCell = new GridCell(
      head.getGridCell().getRow() + direction.getY(),
      head.getGridCell().getColumn() + direction.getX(), grid);
    // If game was paused but now is running, move in the last direction used
    if (direction == Direction.NONE &&
        snakeGame.getGameState() == Constants.GAME_RUNNING) {
      direction = snakeGame.getCurrDir();
    }
    // If the head reaches the edge, die
    if (grid.getCellNeighbor(head.getGridCell(), direction) == null) {  
      return false;
    }
    // If the snake isn't paused and the next cell has a segment, die
    if (direction != Direction.NONE) {
      if (grid.getCellNeighbor(head.getGridCell(), 
          direction).hasSnakeSegment()) {
        return false;
      }
    }
    // Otherwise, move
    head.move(grid.getGridCell(newCell.getRow(), newCell.getColumn()));
    // If the next cell has a fruit loop, add a segment, remove the loop,
    // change the score, and create a new loop
    if(grid.getCellNeighbor(currCell, direction).hasFruitLoop()) {
      body.add(new SnakeSegment(
        grid.getGridCell(currCell.getRow(), currCell.getColumn()), canvas));
      grid.getGridCell(newCell.getRow(),
                       newCell.getColumn()).removeFruitLoop();
      snakeGame.changeScore();
      if (!gameWon) {
        // handles logic for the color and loc. of the new loop
        if (colorIdx < COLOR_ARRAY_LENGTH) {
          colorIdx += 1;
        } else {
          colorIdx = 0;
        }
      FruitLoop fruitLoop = new
        FruitLoop(Constants.FRUIT_LOOP_COLORS[colorIdx],
                  Constants.BACKGROUND_COLOR, grid.getRandomEmptyCell(),
                  canvas); 
      }      
    }
    if (!gameWon)  {
      // Updates the cell for the rest of the body to move into
      newCell = currCell;
      // Moves each segment
      for (SnakeSegment snakeSegment: body){
        currCell = snakeSegment.getGridCell();
        snakeSegment.move(
          grid.getGridCell(newCell.getRow(), newCell.getColumn()));
        newCell = currCell; 
      }
    }  
    return true;
  }
  
  /**
   * Method die calls to the snake game and tells it to terminate
   */
  public void die() {
    snakeGame.gameState(Constants.GAME_OVER);
  }
  
  /**
   * Method setReset sets the reset boolean, which will determine if the while
   * loop should run
   *
   * @param input Boolean of whether the run loop works
   */
  public void setReset(boolean input) {
    reset = input;
  }

  /** 
   * Method run moves the snake as long as it is not paused or dead
   */
  public void run() {
    while(!reset) {
      pause(delay);
      // If game is running, keep checking if move returns true
      if (snakeGame.getGameState() == Constants.GAME_RUNNING) {
        if(!move()) {
          reset = true;
          die();          
        }
      }
    }
  }
  
}
