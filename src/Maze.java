import java.awt.Color;
import java.awt.Graphics;
import java.lang.Math;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

public class Maze extends JPanel {
	int height;
	int width;
	int zoom;
	int startx;
	int starty;
	int endx;
	int endy;
	int time;
	boolean east[][];
	boolean north[][];
	boolean south[][];
	boolean west[][];

	//0 = unvisited; 1 = visited; 2 = done
	int visited[][];
	int discover[][];

	/**constructors**/
	Maze() {
		this(10, 10);
	}
	Maze(int h, int w) {

		if (h <= 0 || w <= 0) {
			System.out.println("Error: height or width non positive integer.");
			return;
		}
		// zoom is multiple of 10
		if (h > 38 || w > 38)
			zoom = 10;
		else if ((h <= 38 && h > 25) || (w <= 38 && w > 25))
			zoom = 20;
		else if ((h <= 25 && h > 18) || (w <= 25 && w > 18))
			zoom = 30;
		else if ((h <= 18 && h > 15) || (w <= 18 && w > 15))
			zoom = 40;
		else if ((h <= 15) || (w <= 15))
			zoom = 50;

		// Sets class variables and changes h and w to encompass hidden circles
		height = h;
		width = w;
		h = h+2;
		w = w+2;

		//start off time as 0
		time = 0;

		// Creates arrays to tell if a wall/is visited exists in a certain direction for any circle.
		east = new boolean[h][w];
		west = new boolean[h][w];
		south = new boolean[h][w];
		north = new boolean[h][w];
		visited = new int[h][w];
		discover = new int[h][w];

		// Sets every element in the wall array to filled and every border element of visited to done
		for (int x = 0; x < h; x++) {
			for (int y = 0; y < w; y++) {
				east[x][y] = true;
				west[x][y] = true;
				south[x][y] = true;
				north[x][y] = true;
				if (x != 0 && x != h - 1 && y != 0 && y != w - 1)
					visited[x][y] = 0;
				else
					visited[x][y] = 2;
			}
		}

		// Starts on left side of maze at random point and ends on right side with random point
		SecureRandom randomNumbers = new SecureRandom();
		startx = 1;
		starty = randomNumbers.nextInt(height)+1;
		endx = width;
		endy = randomNumbers.nextInt(height)+1;

		createMaze(randomNumbers.nextInt(height) + 1,randomNumbers.nextInt(width) + 1);
		refresh();
		solve(starty,startx,endy,endx);
	}

	/**creates maze randomly**/
	// Does a depth first search algorithm to create maze.
	private void createMaze(int x, int y) {
		// Decides a random direction to take
		SecureRandom randomNumbers = new SecureRandom();
		int dir = randomNumbers.nextInt(3);
		visited[x][y] = 1;

		// Goes through each possible direction starting from random
		for(int i = 0; i<4;i++){
			// North
			if (dir == 0 && visited[x-1][y] == 0) {
				// If we can go north, call createMaze on the north circle
				createMaze(x-1,y);
				north[x][y] = false;
				south[x-1][y] = false;
			}

			// East
			if (dir == 1 && visited[x][y+1] == 0) {
				createMaze(x,y+1);
				east[x][y] = false;
				west[x][y+1] = false;
			}

			// South
			if (dir == 2 && visited[x+1][y] == 0) {
				createMaze(x+1,y);
				south[x][y] = false;
				north[x+1][y] = false;
			}

			// West
			if (dir == 3 && visited[x][y-1] == 0) {
				createMaze(x,y-1);
				west[x][y] = false;
				east[x][y-1] = false;
			}

			dir = (dir + 1)%4;
		}

		// Sets current element to done
		visited[x][y] = 2;
	}

	/**resets all the nodes to unvisited**/
	private void refresh() {
		for (int x = 0; x <= height + 1; x++) {
			for (int y = 0; y <= width + 1; y++) {
				if (x == 0 || x == height+1 || y == 0 || y == width+1)
					visited[x][y] = 3;
				else
					visited[x][y] = 0;
			}
		}
	}

	/**solves maze**/
	private boolean solve(int x, int y, int endx, int endy) {		
		visited[x][y] = 2;
		discover[x][y] = time;
		time++;

		// The correct path is the one that returns true and the incorrect paths are those that are false.

		// Are we there yet?
		if (x == endx && y == endy)
			return true;

		// North
		if (!north[x][y] && visited[x-1][y] == 0) {
			if (solve(x-1,y,endx,endy)){
				visited[x][y] = 1;
				return true;
			}
		}

		// East
		if (!east[x][y] && visited[x][y+1] == 0) {
			if (solve(x,y+1,endx,endy)){
				visited[x][y] = 1;
				return true;
			}
		}

		// South
		if (!south[x][y] && visited[x+1][y] == 0) {
			if (solve(x+1,y,endx,endy)){
				visited[x][y] = 1;
				return true;
			}
		}

		// West
		if (!west[x][y] && visited[x][y-1] == 0) {
			if (solve(x,y-1,endx,endy)){
				visited[x][y] = 1;
				return true;
			}
		}

		return false;
	}

	/**paints maze components**/
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.WHITE);
		g.setColor(Color.BLACK);
		for (int x = 1; x < width+1; x++) {
			for (int y = 1; y < height+1; y++) {
				//pick color for oval
				//startpoint or endpoint
				if ((x == startx && y == starty) || (x == endx && y == endy))
					g.setColor(Color.BLACK);
				//visited
				else if (visited[y][x] == 1)
					g.setColor(Color.MAGENTA);
				//done
				else if (visited[y][x] == 2)
					g.setColor(Color.BLUE);
				//unvisited
				else
					g.setColor(Color.GRAY);

				//draw the oval in this square if it has been discovered
				g.fillOval(x*zoom, y*zoom, zoom/5, zoom/5);

				//draw the four walls
				g.setColor(Color.BLACK);

				// 25 = zoom

				//east
				if (west[y][x] == true)
					g.drawLine(-zoom/2 +x*zoom, -zoom/2 +y*zoom, -zoom/2 +x*zoom, zoom/2 +y*zoom);
				//west
				if (east[y][x] == true)
					g.drawLine(zoom/2 +x*zoom, -zoom/2 +y*zoom, zoom/2 +x*zoom, zoom/2 +y*zoom);
				//north
				if (north[y][x] == true)
					g.drawLine(-zoom/2 +x*zoom, -zoom/2 +y*zoom, zoom/2 +x*zoom, -zoom/2 +y*zoom);
				//south
				if (south[y][x] == true)
					g.drawLine(-zoom/2 +x*zoom, zoom/2 +y*zoom, zoom/2 +x*zoom, zoom/2 +y*zoom);
			}
		}
	}
}