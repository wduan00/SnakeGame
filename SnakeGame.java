/*
 * Filename: SnakeGame.java
 * Author:   William Duan
 * UserId:   cs11favj
 * Date:     11/10/2018
 * Sources of help: Javadocs, class notes, tutors
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
 * Class SnakeGame is the main GUI controller of the snake game, it handles all
 * key events and game logic
 */
public class SnakeGame extends WindowController implements KeyListener {
  // References to other classes
  private Grid grid;
  private Snake snake;
  private FruitLoop fruitLoop;
  // Declare PopupMessage and Text objects
  private PopupMessage popupMsg;
  private Text pauseMsg;
  private Text winMsg;
  private Text loseMsg;
  private Text restartMsg;
  // Decalre variables to determine game states/keep track of changes
  private int gridRows;
  private int gridCols;
  private int loopsToWin;
  private int delay;
  private boolean gameOver = false;
  private boolean isPaused; // tells if game is paused
  private Direction currDir = Direction.NONE; // remembers current direction
  private int gameState;
  // Declare GUI components
  private JPanel scorePanel;
  private JLabel fruitLoopsEaten;
  private JLabel fruitLoopsToWin;
  private JLabel mostLoopsEaten;
  
  private int loopsEaten = 0;
  private int highScore = 0;
  private static final int HALF_DIVISOR = 2;
  
  /**
   * Method main parses the command line input to check if the user input is 
   * valid
   * The user may choose to enter a grid size, loops to win, and delay, or
   * nothing at all -- incorrectly formatted inputs, invalid integers, or out 
   * of bounds integers cause the appropriate error message to be printed
   *
   * @param args String array of command line input
   */
  public static void main(String[] args) {
		int gridRows = 0;
    int gridCols = 0;
    int loopsToWin = 0;
    int delay = 0;
    int xCount;
    // If more than 3 args, print error
    if (args.length > PA8Constants.MAX_NUM_ARGS) {
			System.out.println(PA8Constants.NUM_ARGS_ERR);
			usage();
			System.exit(1);
		}
    // If args == 3
    if (args.length == PA8Constants.MAX_NUM_ARGS) {
		  args[0] = args[0].toLowerCase();
      // Make sure there is only one occ of x
      xCount = 0;
			for (int i = 0; i < args[0].length(); i++) {
				if (args[0].charAt(i) == PA8Constants.GRID_SIZE_DELIM.charAt(0)) {
					xCount++;
				}
			}
      // If it has only one x...
			if (args[0].contains(PA8Constants.GRID_SIZE_DELIM) && xCount == 1) {
				// Check if the rows or columns doesn't exist
        if (args[0].substring(0,
            args[0].indexOf(PA8Constants.GRID_SIZE_DELIM)).equals("")) {
          System.out.printf(PA8Constants.GRID_SIZE_FMT_ERR, args[0]);
					usage();
					System.exit(1);
        }
        if (args[0].substring(args[0].indexOf(PA8Constants.GRID_SIZE_DELIM) + 1,
					  args[0].length()).equals("")) {
          System.out.printf(PA8Constants.GRID_SIZE_FMT_ERR, args[0]);
					usage();
					System.exit(1);
        }   
        // Parse first int as rows
        gridRows = isInteger(args[0].substring(
          0, args[0].indexOf(PA8Constants.GRID_SIZE_DELIM)),
                             PA8Constants.ROWS_STR);       
        // Try to parse the second part of the input as an int for the gridCols
				gridCols = isInteger(args[0].substring(
          args[0].indexOf(PA8Constants.GRID_SIZE_DELIM) + 1, args[0].length()),
                          PA8Constants.COLS_STR);			  
        // If gridRows is out of bounds, print error message
				boundCheck(gridRows, PA8Constants.MIN_ROWS, PA8Constants.MAX_ROWS,
                   PA8Constants.ROWS_STR);				
        // If gridCols is out of bounds, print error message
				boundCheck(gridCols, PA8Constants.MIN_COLS, PA8Constants.MAX_COLS,
                   PA8Constants.COLS_STR);
      } else {
					// if improperly formatted, print out error
          System.out.printf(PA8Constants.GRID_SIZE_FMT_ERR, args[0]);
					usage();
					System.exit(1);
			}
      // Try to pass the second arg as an int for loopsToWin
			loopsToWin = isInteger(args[1], PA8Constants.LOOPS_STR);
      // If loopsToWin is less than the min, print the error message
			if (loopsToWin < PA8Constants.MIN_LOOPS_TO_WIN ||
          loopsToWin > gridRows * gridCols - 1) {
	      System.out.printf(PA8Constants.LOOPS_RANGE_ERR,
                          PA8Constants.MIN_LOOPS_TO_WIN);
	      usage();
				System.exit(1);
		  }
      // check if delay is valid int
      delay = isInteger(args[2], PA8Constants.DELAY_STR);
      boundCheck(delay, PA8Constants.MIN_DELAY, PA8Constants.MAX_DELAY,
                 PA8Constants.DELAY_STR);
      usage();
    } // end of args == 3

		// If there are 2 args provided
		if (args.length == PA8Constants.MAX_NUM_ARGS - 1) {
			args[0] = args[0].toLowerCase();
      // Count num of X
			xCount = 0;
			for (int i = 0; i < args[0].length(); i++) {
				if (args[0].charAt(i) == PA8Constants.GRID_SIZE_DELIM.charAt(0)) {
					xCount++;
				}
			} 
      // if only one x, parse for valid rows and columns
			if (args[0].contains(PA8Constants.GRID_SIZE_DELIM) && xCount == 1) {
				if (args[0].substring(
              0, args[0].indexOf(PA8Constants.GRID_SIZE_DELIM)).equals("")) {
          System.out.printf(PA8Constants.GRID_SIZE_FMT_ERR, args[0]);
					usage();
					System.exit(1);
        }
        if (args[0].substring(args[0].indexOf(
              PA8Constants.GRID_SIZE_DELIM) + 1,
              args[0].length()).equals("")) {
          System.out.printf(PA8Constants.GRID_SIZE_FMT_ERR, args[0]);
					usage();
					System.exit(1);
        }
        // If args[0] passed, parse args[1]
        gridRows = isInteger(args[0].substring(
        0, args[0].indexOf(PA8Constants.GRID_SIZE_DELIM)),
                           PA8Constants.ROWS_STR);       
        // Try to parse the second part of the input as an int for the gridCols
				gridCols = isInteger(args[0].substring(args[0].indexOf(
                               PA8Constants.GRID_SIZE_DELIM) + 1,
                               args[0].length()), PA8Constants.COLS_STR);  
        // If gridRows is out of bounds, print error message				
        boundCheck(gridRows, PA8Constants.MIN_ROWS, PA8Constants.MAX_ROWS,
                   PA8Constants.ROWS_STR);				
        // If gridCols is out of bounds, print error message
				boundCheck(gridCols, PA8Constants.MIN_COLS, PA8Constants.MAX_COLS,
                   PA8Constants.COLS_STR);
      } else {
					// print error if rowsxcolumns incorrectly formatted
          System.out.printf(PA8Constants.GRID_SIZE_FMT_ERR, args[0]);
					usage();
					System.exit(1);
			}
			// Try to pass the second arg as an int for loopsToWin
			loopsToWin = isInteger(args[1], PA8Constants.LOOPS_STR);
      // If loopsToWin is less than the min, print the error message
			if (loopsToWin < PA8Constants.MIN_LOOPS_TO_WIN ||
          loopsToWin > gridRows * gridCols - 1) {
				System.out.printf(PA8Constants.LOOPS_RANGE_ERR,
                          PA8Constants.MIN_LOOPS_TO_WIN);
				usage();
				System.exit(1);
			}
      // if all passed, set default delay and print usage msg
      usage();
      delay = PA8Constants.DEFAULT_ANIMATION_DELAY;
		} // end of args == 2

		// If args == 1
		if (args.length == 1) {
			args[0] = args[0].toLowerCase();
      // Try to parse the first part of the input as an int for gridRows
			xCount = 0;
			// Make sure there's only one x
      for (int i = 0; i < args[0].length(); i++) {
				if (args[0].charAt(i) == PA8Constants.GRID_SIZE_DELIM.charAt(0)) {
					xCount++;
				}
			}
      // see if rows or columns is empty
			if (args[0].contains(PA8Constants.GRID_SIZE_DELIM) && xCount == 1) {
				if (args[0].substring(0,
              args[0].indexOf(PA8Constants.GRID_SIZE_DELIM)).equals("")) {
          System.out.printf(PA8Constants.GRID_SIZE_FMT_ERR, args[0]);
					usage();
					System.exit(1);
        }
        if (args[0].substring(args[0].indexOf(PA8Constants.GRID_SIZE_DELIM) + 1,
								args[0].length()).equals("")) {
          System.out.printf(PA8Constants.GRID_SIZE_FMT_ERR, args[0]);
					usage();
					System.exit(1);
        }
        // parse rows as an int
        gridRows = isInteger(args[0].substring(0,
              args[0].indexOf(PA8Constants.GRID_SIZE_DELIM)),
            PA8Constants.ROWS_STR);
        // Try to parse the second part of the input as an int for the gridCols
				gridCols = isInteger(args[0].substring(args[0].indexOf
              (PA8Constants.GRID_SIZE_DELIM) + 1,
								args[0].length()), PA8Constants.COLS_STR);
       	// If gridRows is out of bounds, print error message
				boundCheck(gridRows, PA8Constants.MIN_ROWS, PA8Constants.MAX_ROWS,
            PA8Constants.ROWS_STR);				
        // If gridCols is out of bounds, print error message
				boundCheck(gridCols, PA8Constants.MIN_COLS, PA8Constants.MAX_COLS,
            PA8Constants.COLS_STR);
        usage();
      } else {
					// if improperly formatted, print the error
          System.out.printf(PA8Constants.GRID_SIZE_FMT_ERR, args[0]);
					usage();
					System.exit(1);
      }
      // if all passes, set default loopsToWin, delay, and print usage
      loopsToWin = gridRows * gridCols - 1;
      delay = PA8Constants.DEFAULT_ANIMATION_DELAY;
			} // end of args == 1

		// If args == 0, set default values
		if (args.length == 0) {
			usage();
      gridRows = PA8Constants.DEFAULT_ROWS;
      gridCols = PA8Constants.DEFAULT_COLS;
      loopsToWin = gridRows * gridCols -1;
      delay = PA8Constants.DEFAULT_ANIMATION_DELAY;
	  } // end of args == 0

    // If all arguments pass, create the window
    new Acme.MainFrame(new SnakeGame(gridRows, gridCols, loopsToWin, delay),
                       args, gridCols * PA8Constants.GRID_CELL_SIZE - 9,
                       gridRows * PA8Constants.GRID_CELL_SIZE + 1 +
                       PA8Constants.SCORE_PANEL_HEIGHT);    
  } // end of main

  /**
   * The SnakeGame ctor takes in a number of rows, columns, loops to win, and
   * delay to create a snakeGame object
   *
   * @param gridRows Number of rows
   * @param griCols Number of columns
   * @param loopsToWin Number of loops that need to be eaten in order to win
   * @param delay Milliseconds between each snake movement
   */
  public SnakeGame(int gridRows, int gridCols, int loopsToWin, int delay) {
    this.gridRows = gridRows;
    this.gridCols = gridCols;
    this.loopsToWin = loopsToWin;
    this.delay = delay;
    this.grid = grid;
  } // end of ctor

  /**
   * Method isInteger parses a string and checks if it's an int -- if not, it
   * prints an error msg.
   *
   * @param input String to be parsed as an int
   * @param label The label of what the input was, to be used in the error msg.
   * @return the int value of the parsed input
   */
	public static int isInteger(String input, String label) {
    int number = 0;
    try {
      number = Integer.parseInt(input);
    } catch (NumberFormatException e) {
        System.out.printf(PA8Constants.INT_ERR, label, input);
        usage();
			  System.exit(1);
      }
    return number;
  }
  
  /**
   * Method boundCheck checks to see if a given number is between a given
   * min/max
   *
   * @param input The number to be checked
   * @param min The min. bound
   * @param max The max bound
   * @param label The label of what the input was, to be used in the error msg.
   */
  public static void boundCheck(int input, int min, int max, String label) {
    if (input < min || input > max) {
      System.out.printf(PA8Constants.RANGE_ERR, label,
        							  input, min, max);
			usage();
			System.exit(1);
    }
  }

  /**
   * Method usage is a helper method that prints out the usage msg.
   */
  public static void usage() {
    System.out.printf(PA8Constants.USAGE_STR, PA8Constants.MIN_ROWS,
								      PA8Constants.MAX_ROWS, PA8Constants.MIN_COLS, 
						          PA8Constants.MAX_COLS,
								      PA8Constants.DEFAULT_ROWS, PA8Constants.DEFAULT_COLS,
    								  PA8Constants.MIN_LOOPS_TO_WIN, PA8Constants.MIN_DELAY,
								      PA8Constants.MAX_DELAY, PA8Constants.DEFAULT_ANIMATION_DELAY);
  }

  /**
   * Method changeScore changes the score of the game, and checks if the game
   * has been won
   */
  public void changeScore() {
    loopsEaten += 1;
    fruitLoopsEaten.setText(PA8Constants.LOOPS_EATEN_STR + loopsEaten);
    if (loopsEaten == loopsToWin) {
      gameState(PA8Constants.GAME_OVER);
      snake.setGameWon(true);
    }
  }

	/**
   * Method begin sets up all the GUI components in the window and calls the
   * setup method initGame()
   */
 	public void begin() {
    // instantiate GUI components
    scorePanel = new JPanel();
    scorePanel.setLayout(new GridLayout(1, PA8Constants.NUM_LOOP_LABELS));
    fruitLoopsEaten = new JLabel(PA8Constants.LOOPS_EATEN_STR + loopsEaten);
    fruitLoopsToWin = new JLabel(PA8Constants.LOOPS_TO_WIN_STR + loopsToWin);
    mostLoopsEaten = new JLabel(PA8Constants.MOST_LOOPS_EATEN_STR +
                                highScore);
    
    scorePanel.add(fruitLoopsEaten);
    scorePanel.add(fruitLoopsToWin);
    scorePanel.add(mostLoopsEaten);
    // Add components to window
    this.add(scorePanel, BorderLayout.NORTH);
    this.validate();
    // Add key listener, focus in window
    canvas.addKeyListener(this);
    canvas.requestFocusInWindow();
    // creates the actual grid on the canvas
    grid = new Grid(gridRows, gridCols, canvas);
    grid.draw(canvas);
    // call initGame for rest of setup
    initGame();
	} // end of begin

  /**
   * Method initGame sets up the snake game by creating the grid, snake, and
   * fruitLoop objects
   */
  public void initGame() {
    // Find center of the window
    double x = gridCols * PA8Constants.GRID_CELL_SIZE / HALF_DIVISOR;
    double y = gridRows * PA8Constants.GRID_CELL_SIZE / HALF_DIVISOR;
    Location center = new Location(x,y);

    // Create the text objects and hide them
    pauseMsg = new Text(PA8Constants.PAUSE_STR, center, canvas);
    pauseMsg.setFontSize(PA8Constants.BIG_FONT_SIZE);
    pauseMsg.setBold(true);
    pauseMsg.setColor(PA8Constants.TEXT_COLOR);
    pauseMsg.hide();

    winMsg = new Text(PA8Constants.WIN_STR, center, canvas);
    winMsg.setFontSize(PA8Constants.BIG_FONT_SIZE);
    winMsg.setBold(true);
    winMsg.setColor(PA8Constants.TEXT_COLOR);
    winMsg.hide();

    loseMsg = new Text(PA8Constants.LOSE_STR, center, canvas);
    loseMsg.setFontSize(PA8Constants.BIG_FONT_SIZE);
    loseMsg.setBold(true);
    loseMsg.setColor(PA8Constants.TEXT_COLOR);
    loseMsg.hide();

    restartMsg = new Text(PA8Constants.RESTART_STR, center, canvas);
    restartMsg.setFontSize(PA8Constants.SMALL_FONT_SIZE);
    restartMsg.setColor(PA8Constants.TEXT_COLOR);
    restartMsg.hide();

    // Create the popup message and hide it
    popupMsg = new PopupMessage(center, PA8Constants.POPUP_COLOR, canvas);
    popupMsg.hide();

    // Creates a new snake object
    snake = new Snake(this, grid, delay, canvas);
    snake.setDirection(Direction.NONE);

    // resets game state controllers
    gameState = 0;
    gameOver = false;
    // New fruit loop object
    fruitLoop = new FruitLoop(
      PA8Constants.FRUIT_LOOP_COLORS[0],
      PA8Constants.BACKGROUND_COLOR, grid.getRandomEmptyCell(), canvas);
  }
  
  /**
   * Paints the canvas
   *
   * @param g Object of graphics that draws
   */
  public void paint(Graphics g) {
    super.paint(g);
  }
  
  /**
   * Method getCurrDir returns the current direction of the snake
   *
   * @return Current direction the snake is moving
   */
  public Direction getCurrDir() {
    return currDir;
  }

  /**
   * Method gameState determines what to do based on the game state passed in
   *
   * @param state Int that corresponds to a game state
   */
  public void gameState(int state) {
   switch (state) {
     // If the game is running, change it to its original direction 
     case PA8Constants.GAME_RUNNING:
       snake.setDirection(currDir);    
       break;
     // If pause is called, remember its currDir and set direction to none
     case PA8Constants.GAME_PAUSED:    
       currDir = snake.getDirection();
       snake.setDirection(Direction.NONE);
       break;
     // If the game ends, set dir to none, check if win or lose, and print the
     // appropriate end msg. If the high score was beaten, update it
     case PA8Constants.GAME_OVER:
       snake.setReset(true);
       // allows R to be pressed
       gameOver = true;
       // checks win or lose
       if (loopsToWin == loopsEaten){
         popupMsg.display(winMsg, 0 , 0 - PA8Constants.TEXT_Y_OFFSET);
         popupMsg.display(restartMsg, 0, PA8Constants.TEXT_Y_OFFSET);       
       } else {
           popupMsg.display(loseMsg, 0, 0 - PA8Constants.TEXT_Y_OFFSET);
           popupMsg.display(restartMsg, 0, PA8Constants.TEXT_Y_OFFSET);
       }
       // checks if high score was beaten
       if (loopsEaten > highScore) {
         highScore = loopsEaten;
         mostLoopsEaten.setText(PA8Constants.MOST_LOOPS_EATEN_STR +
                                highScore);
       }
       break;
     default: break;
    }  
  }

  /**
   * Method getGameState returns the current state of the game
   *
   * @return the state of the game
   */
  public int getGameState() {
    return gameState;
  }
  
  /**
   * Method keyPressed handles events when certain keys are pressed
   *
   * @param evt Key Event generated when a key is pressed
   */
  public void keyPressed(KeyEvent evt) {
    if (gameState == PA8Constants.GAME_RUNNING) {
      switch (evt.getKeyCode()) {
        // If up is pressed, go up as long as the current dir isn't down
        case KeyEvent.VK_UP:
          if (snake.getDirection() != Direction.DOWN ||
              snake.snakeLength() == 1) {
            snake.setDirection(Direction.UP);    
          }
          break;
        // If down is pressed, go down as long as the current dir isn't up  
        case KeyEvent.VK_DOWN:
          if (snake.getDirection() != Direction.UP ||
              snake.snakeLength() == 1) {
            snake.setDirection(Direction.DOWN);
          }
          break;
        // If left is pressed, go left as long as the current dir isn't right  
        case KeyEvent.VK_LEFT:
          if (snake.getDirection() != Direction.RIGHT ||
              snake.snakeLength() == 1) {
            snake.setDirection(Direction.LEFT);
          }
          break;
        // If right is pressed, go right as long as the current dir isn't left  
        case KeyEvent.VK_RIGHT:
          if (snake.getDirection() != Direction.LEFT ||
              snake.snakeLength() == 1) {
            snake.setDirection(Direction.RIGHT);
          }
          break;
        default: break;
      } // end of switch      
    } // end of if game running
    // If space is pressed, pause if not paused and vice versa
    if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
      // only respond if game isn't over
      if (!gameOver) {
        // Pause if unpaused, unpause if paused
        if (isPaused) {
          gameState = PA8Constants.GAME_RUNNING;
          gameState(gameState);
          isPaused = false;
          popupMsg.hide();
          gameOver = false;
        } else {
            gameState = 1;
            gameState(gameState);
            isPaused = true;
            popupMsg.hide();
            popupMsg.display(pauseMsg);
            gameOver = false;
        }
      }
    }
    // Only reset game on r pressed if the game is over
    if (evt.getKeyCode() == KeyEvent.VK_R) {
      // only respond if the game is over
      if (gameOver) {
        popupMsg.hide();
        loopsEaten = 0;
        fruitLoopsEaten.setText(PA8Constants.LOOPS_EATEN_STR + loopsEaten);
        grid.reset();
        initGame();
      }
    }
  } // end of keyPressed

	/**
   * Method keyTyped is not implemented because no events need to be fired on
   * a key type
   *
   * @param evt Key event generated by typing a key
   */
	public void keyTyped(KeyEvent evt) {}
  
  /**
   * Method keyReleased is not implemented because no events need to be fired
   * on a key release
   *
   * @param evt Key event generated by a key release
   */
  public void keyReleased(KeyEvent evt) {}
} // end of class
