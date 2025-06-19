
# SnakeGame
## About
Simple implementation of the classic arcade game _Snake_.

## Usage
Compile:  
`javac -cp .:objectdraw.jar:Acme.jar SnakeGame.java`  
Execute:  
`java -cp .:objectdraw.jar:Acme.jar SnakeGame`  

### Optional command line args  
`rowsXcolumns`: size of the grid; 'X' is not case sensitive  
 - rows must be a valid int in the range `[2, 25]`
 - columns must be a valid int in the range `[2, 45]`
 - defaults to `20X18`
 
`loopsToWin`: the number of fruit loops that must be eaten to win  
 - must be valid integer in the range `[1, (rows * columns) - 1]`
 - defaults to `(rows * columns) - 1` (i.e. the full grid)
 
`delay`: length of the animation pause measured in milliseconds
 - must be valid integer in the range `[50, 1000]`
 - defaults to `120`
