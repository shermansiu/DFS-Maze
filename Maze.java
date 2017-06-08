import hsa.Console;
import java.awt.*;

public class Maze
{
    // Configurable variables
    static int consoleWidth = 150;
    static int consoleHeight = 33;
    static int cellSize = 5;
    
    static int[] playPos = {consoleWidth*4/cellSize, consoleHeight*10/cellSize};
    
    static Color charColor = new Color(255, 50, 0);
    static Color endpointColor = new Color(0, 162, 232);

    static boolean debug = true;
    static boolean backtrackDebug = false;
    static boolean intenseDebug = false;
    static boolean debugSln = true;
    static int debugDelay = 0;
    
    // Initialize other important variables
    static int mazeW, mazeWM, mazeH, mazeHM, numSteps, distance, longestDistance, cellCount;
    static int[] genPos, endPos;
    static int[][] mazeCells, availCells, distFromStart;
    static int[][][] prevPos;
    static long startingTime, finishTime;
    static Color bgColor, inverseEPCol;
    static boolean victory, posR, posU, posL, posD, traceTrail, epColMode;
    
    static Console c;
    
    // Initialize constants
    static final int RIGHT = 0;
    static final int UP = 1; 
    static final int LEFT = 2; 
    static final int DOWN = 3; 
    
    
    private static void initVars()
    {
	// Initialize variables
	mazeW = consoleWidth * 8 / cellSize;
	mazeWM = mazeW - 1;
	mazeH = consoleHeight * 20 / cellSize;
	mazeHM = mazeH - 1;
	mazeCells = new int[mazeW][mazeH];
	availCells = new int[mazeW][mazeH];
	distFromStart = new int[mazeW][mazeH];
	numSteps = 0;
	distance = 0;
	longestDistance = 0;
	cellCount = 1;
	genPos = (int[]) playPos.clone();
	endPos = new int[2];
	prevPos = new int[mazeW][mazeH][2];
	victory = false;
	posR = false;
	posU = false;
	posL = false;
	posD = false;
	traceTrail = false;
	bgColor = new Color(255 - (255-charColor.getRed())/2,
			    255 - (255-charColor.getGreen())/2,
			    255 - (255-charColor.getBlue())/2);
	inverseEPCol = new Color(255 - endpointColor.getRed(),
				 255 - endpointColor.getGreen(),
				 255 - endpointColor.getBlue());
	epColMode = false;
	c = new Console(consoleHeight, consoleWidth);
    } // initVars method
    
    
    public static void posTrial()
    {
	for(int x = 0; x < mazeW; x++)
	{
	    for(int y = 0; y < mazeH; y++)
	    {
		if(prevPos[x][y][0] != -1 && (prevPos[x][y][0] != x || prevPos[x][y][1] != y))
		{
		    // Draw the square
		    c.setColor(Color.pink);
		    c.fillRect(prevPos[x][y][0]*cellSize, prevPos[x][y][1]*cellSize, cellSize, cellSize);
		}
	    }
	    
	}
	for(int x = 0; x < mazeW; x++)
	{
	    for(int y = 0; y < mazeH; y++)
	    {
		if(prevPos[x][y][0] != -1 && (prevPos[x][y][0] != x || prevPos[x][y][1] != y))
		{
		    // Draw the square
		    c.setColor(Color.orange);
		    c.fillRect(x*cellSize, y*cellSize, cellSize/2, cellSize/2);
		}
	    }
	    
	}
	
    } // temporary method
    
    
    public static void debugDraw(Color col)
    {
	// Draw the square
	c.setColor(col);
	c.fillRect(genPos[0]*cellSize, genPos[1]*cellSize, cellSize, cellSize);
	
	// Wait for debugDelay ms
	try
	{
	    Thread.sleep(debugDelay);
	} // try statement
	catch (InterruptedException e)
	{     
	} // catch statement
    } // debugDrawBlue method
    
    
    public static void markOld(int dir)
    {
	switch(dir)
	{
	    case RIGHT:
		prevPos[genPos[0]+2][genPos[1]][0] = genPos[0];
		prevPos[genPos[0]+2][genPos[1]][1] = genPos[1];
		break;
	    case UP:
		prevPos[genPos[0]][genPos[1]-2][0] = genPos[0];
		prevPos[genPos[0]][genPos[1]-2][1] = genPos[1];
		break;
	    case LEFT:
		prevPos[genPos[0]-2][genPos[1]][0] = genPos[0];
		prevPos[genPos[0]-2][genPos[1]][1] = genPos[1];
		break;
	    case DOWN:
		prevPos[genPos[0]][genPos[1]+2][0] = genPos[0];
		prevPos[genPos[0]][genPos[1]+2][1] = genPos[1];
		break;
	} // switch statement
	mazeCells[genPos[0]][genPos[1]] = 0;
    } // markOld method
    
    
    public static void deadendCheck()
    {
	if(genPos[1] == mazeHM - 1)
	    posD = true;
	else if(availCells[genPos[0]][genPos[1]+2] == 1)
	    posD = true;
	else
	    posD = false;
	if(genPos[1] == 1)
	    posU = true;
	else if(availCells[genPos[0]][genPos[1]-2] == 1)
	    posU = true;
	else
	    posU = false;
	if(genPos[0] == mazeWM - 1)
	    posR = true;
	else if(availCells[genPos[0]+2][genPos[1]] == 1)
	    posR = true;
	else
	    posR = false;
	if(genPos[0] == 1)
	    posL = true;
	else if(availCells[genPos[0]-2][genPos[1]] == 1)
	    posL = true;
	else
	    posL = false;
    } // deadendCheck method
    
    
    public static void backtrack()
    {
	int orig_x = genPos[0];
	genPos[0] = prevPos[genPos[0]][genPos[1]][0];
	genPos[1] = prevPos[orig_x][genPos[1]][1];
	if(debug)
	    debugDraw(Color.gray);
	if(backtrackDebug)
	    c.getChar();
    } // backtrack method

    
    public static void moveGen(int dir)
    {
	// Move the generator
	switch(dir)
	{
	    case RIGHT:
		genPos[0]++;
		break;
	    case UP:
		genPos[1]--;
		break;
	    case LEFT:
		genPos[0]--;
		break;
	    case DOWN:
		genPos[1]++;
		break;
	    default:
		break;
	}
	
	// Mark the current cell as already generated
	availCells[genPos[0]][genPos[1]] = 1;
	
	// Mark the current cell as a member of the maze
	mazeCells[genPos[0]][genPos[1]] = 0;
	
	// Draw a debug square if in debug mode
	if(debug)
	    debugDraw(Color.blue);
    } // moveGen method
    
    
    public static void genMaze()
    {
	boolean incomplete = true;
	boolean success;
	int dir;
	
	backtrackDebug = debug && backtrackDebug;
	
	for(int x = 0; x < mazeW; x++)
	{
	    for(int y = 0; y < mazeH; y++)
	    {
		// Generate a border
		availCells[x][y] = (y == 0 || y == mazeHM || x == 0 || x == mazeWM)? 1: 0;
		
		mazeCells[x][y] = 1;
		
		// Set the prevPos values to -1
		prevPos[x][y][0] = -1;
		prevPos[x][y][1] = -1;
		
		// Set the distFromStart values to -1
		distFromStart[x][y] = -1;
		// Temp - ensure that the bottom right square is blank - the goal
		// if(x == endPos[0] && y == endPos[1])
		//     mazeCells[x][y] = 0;
	    } // for loop
	} // for loop
	
	// Ensure that you don't return to the starting square
	availCells[genPos[0]][genPos[1]] = 1;
	
	// Set the distance of the starting square to 0
	distFromStart[genPos[0]][genPos[1]] = 0;
	
	if(debug)
	    debugDraw(Color.blue);
	
	// Attempt to move the generator in a random direction
	do
	{
	    success = false;
	    do
	    {
		// Choose a random direction            
		dir = (int) (Math.random() * 4);
		
		// Check to see if the generator can move in the chosen direction
		switch(dir)
		{
		    case RIGHT:
			if(availCells[genPos[0]+1][genPos[1]] == 1 || genPos[0] == mazeWM - 1)
			    break;
			if(availCells[genPos[0]+2][genPos[1]] == 1)
			    break;
			
			success = true;
			break;
		    case UP:
			if(availCells[genPos[0]][genPos[1]-1] == 1 || genPos[1] == 1)
			    break;
			if(availCells[genPos[0]][genPos[1]-2] == 1)
			    break;
			
			success = true;
			break;
		    case LEFT:
			if(availCells[genPos[0]-1][genPos[1]] == 1 || genPos[0] == 1)
			    break;
			if(availCells[genPos[0]-2][genPos[1]] == 1)
			    break;
			
			success = true;
			break;
		    case DOWN:
			if(availCells[genPos[0]][genPos[1]+1] == 1 || genPos[1] == mazeHM - 1)
			    break;
			if(availCells[genPos[0]][genPos[1]+2] == 1)
			    break;
			
			success = true;
			break;
		} // switch statement
	    } // do statement
	    while(!success);
	    
	    // Mark the old position
	    markOld(dir);
	     
	    // Move the generator twice
	    moveGen(dir);
	    moveGen(dir);
	    
	    //Increment the cell count
	    cellCount++;
	    
	    // Increment the distance from the start and mark the distance of the cell from the start
	    distance += 2;
	    distFromStart[genPos[0]][genPos[1]] = distance;
	    
	    // Ensure that you can't return to certain cells
	    availCells[genPos[0]][genPos[1]] = 1;
	    
	    // Check if generator is at a dead end
	    deadendCheck();

	    while(posR && posU && posL && posD)
	    {    
		try
		{
		    backtrack();
		    distance -= 2;
		    deadendCheck();
		} // try statement
		catch(java.lang.ArrayIndexOutOfBoundsException e)
		{
		    incomplete = false;
		    break;
		}
	    } // while loop
	} // do statement
	while(incomplete);
    } // genMaze method

    
    public static void placeEndpoint()
    {        
	// Declare variables
	int[] furthestPoint = {mazeWM, mazeHM};
	
	// Determine the cell that is the farthest from the start
	for(int x = mazeWM; x >= 0; x--)
	{
	    for(int y = mazeHM; y >= 0; y--)
	    {
		if(distFromStart[x][y] > distFromStart[furthestPoint[0]][furthestPoint[1]])
		{
		    furthestPoint[0] = x;
		    furthestPoint[1] = y;
		}
	    } // for loop
	} // for loop
	
	// Mark down the longest distance from the start
	longestDistance = distFromStart[furthestPoint[0]][furthestPoint[1]];
	
	// Place the endpoint on the farthest cell from the start
	endPos = (int[]) furthestPoint.clone();
    } // placeEndpoint method
    
    
    public static void drawChar()
    {
	// Draw the player
	c.setColor(charColor);
	c.fillRect(playPos[0]*cellSize, playPos[1]*cellSize, cellSize, cellSize);
    } // drawChar method
    
    
    public static void eraseChar()
    {
	// Erase the previous position
	if(traceTrail)
	    c.setColor(bgColor);
	else
	    c.setColor(Color.white);
	c.fillRect(playPos[0]*cellSize, playPos[1]*cellSize, cellSize, cellSize);
    } // eraseChar method
    
    
    public static void drawMaze()
    {
	// Draw the walls of the maze
	for(int x = 0; x < mazeW; x++)
	{
	    for(int y = 0; y < mazeH; y++)
	    {
		if(mazeCells[x][y] == 1)
		{
		    c.setColor(Color.black);
		    c.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);
		}
		else
		{
		    c.setColor(Color.white);
		    c.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);
		}
	    } // for loop
	} // for loop
	
	// Draw the golden road, if debugSln is true
	if(debugSln)
	{
	    int[] drawPos = (int[]) endPos.clone();
	    c.setColor(Color.yellow);
	    try
	    {
		while(drawPos[0] != playPos[0] || drawPos[1] != playPos[1])
		{
		    c.fillRect(drawPos[0]*cellSize, drawPos[1]*cellSize, cellSize, cellSize);
		    c.fillRect((prevPos[drawPos[0]][drawPos[1]][0]+drawPos[0])*cellSize/2,
			    (prevPos[drawPos[0]][drawPos[1]][1]+drawPos[1])*cellSize/2, cellSize, cellSize);
		    drawPos = (int[]) prevPos[drawPos[0]][drawPos[1]].clone();
		} // while loop
	    } // try statement
	    catch(Exception e)
	    {
	    }
	} // if statement
	
	// Draw the destination
	c.setColor(endpointColor);
	c.fillRect(endPos[0]*cellSize, endPos[1]*cellSize, cellSize, cellSize);
	
	// Draw the character
	drawChar();
    } // drawMaze method
    
    
    public static void moveChar()
    {
	// Declare variables
	char ch = c.getChar();
	switch(ch)
	{
	    case 'w':
		if(playPos[1] == 0)
		    break;
		if(mazeCells[playPos[0]][playPos[1]-1] == 1)
		    break;
		
		// Clear the old square
		eraseChar();
		
		// Move the character
		playPos[1] -= 1;
		
		// Draw the character
		drawChar();
		
		// Increment the number of step taken
		numSteps++;
		break;
	    case 'a':
		if(playPos[0] == 0)
		    break;
		if(mazeCells[playPos[0]-1][playPos[1]] == 1)
		    break;
		
		// Clear the old square
		eraseChar();
		
		// Move the character
		playPos[0] -= 1;
		
		// Draw the character
		drawChar();

		// Increment the number of step taken
		numSteps++;
		break;
	    case 's':
		if(playPos[1] == mazeHM)
		    break;
		if(mazeCells[playPos[0]][playPos[1]+1] == 1)
		    break;
		
		// Clear the old square
		eraseChar();
		
		// Move the character
		playPos[1] += 1;
		
		// Draw the character
		drawChar();

		// Increment the number of step taken
		numSteps++;
		break;
	    case 'd':
		if(playPos[1] == mazeWM)
		    break;
		if(mazeCells[playPos[0]+1][playPos[1]] == 1)
		    break;
		
		// Clear the old square
		eraseChar();
		
		// Move the character
		playPos[0] += 1;
		
		// Draw the character
		drawChar();

		// Increment the number of step taken
		numSteps++;
		break;
	    case 't':
		traceTrail = !traceTrail;
		break;
	    case 'p':
		epColMode = !epColMode;
		if(epColMode)
		    c.setColor(inverseEPCol);
		else
		    c.setColor(endpointColor);
		c.fillRect(endPos[0]*cellSize, endPos[1]*cellSize, cellSize, cellSize);
		break;
	    default:
		break;
	} // switch statement
	
	// Detect whether the player is on the winning square
	if(playPos[0] == endPos[0] && playPos[1] == endPos[1])
	{
	    // Set the ending time
	    finishTime = System.currentTimeMillis();
	    
	    // Attempt to wait 1 second
	    try
	    {
		Thread.sleep(1000);
	    } // try statement
	    catch (InterruptedException e)
	    {
		
	    } // catch statement
	    victory = true; 
	} // if statement
    } // moveChar method

    
    public static void endGame()
    {
	// Calculate the time taken (in seconds)
	double timeTaken = (double) (finishTime - startingTime) / 1000;
	
	// Congratulate player, display scoreboard
	c.clear();
	c.println("You made it! Thanks for playing!");
	c.println();
	c.println("SCOREBOARD");
	c.println("-------------------------------------");
	c.print("Least steps needed:");
	c.println(longestDistance, 16);
	c.print("Steps taken:");
	c.println(numSteps, 23);
	c.println();
	c.print("Time taken (seconds):", 34 - (int) (timeTaken) / 10);
	c.println(timeTaken, 0, 3);
	c.println();
	c.println();
	c.readChar();
	c.close();
    } // endGame method
    
    
    public static void main(String[] args)
    {
	// Initialize important variables
	initVars();
	
	// Generate the maze and choose the endpoint
	genMaze();
	placeEndpoint();
	
	// Draw the maze
	drawMaze();
	
	// Debug the backtracking system if requested
	if(debug && intenseDebug)
	    posTrial();
	
	// Set the starting time
	startingTime = System.currentTimeMillis();
	
	// Move the character according to the WASD keyboard input
	while(!victory)
	{
	    moveChar();
	}
	
	// Display the ending message
	endGame();
    }

} // Maze class
