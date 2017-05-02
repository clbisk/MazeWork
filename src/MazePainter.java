import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

public class MazePainter extends JPanel {
	Maze myMaze;
	int now;

	public MazePainter(Maze m, int time) {
		myMaze = m;
		now = time;
	}
	
	public void incTime() {
		now++;
	}

	/**paints maze components**/
	@Override
	public void paintComponent(Graphics g) {
		int zoom = myMaze.zoom;
		int height = myMaze.height;
		int width = myMaze.width;
		int startx = myMaze.startx;
		int starty = myMaze.starty;
		int endx = myMaze.endx;
		int endy = myMaze.endy;
		int[][] visited = myMaze.visited;
		int[][] discover = myMaze.discover;
		boolean[][] south = myMaze.south;
		boolean[][] north = myMaze.north;
		boolean[][] east = myMaze.east;
		boolean[][] west = myMaze.west;
		
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
				if (discover[y][x] <= now)
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
