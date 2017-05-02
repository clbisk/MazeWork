import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.JFrame;

public class Draw extends JPanel {
	
	public static void main(String[] args) {
		JFrame f = new JFrame("Maze");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Maze M = new Maze(30,25);
		M.setBackground(Color.WHITE);
		MazePainter MP = new MazePainter(M, 0);
		f.add(MP);
		f.setSize(785, 800);
		f.setVisible(true);
		for (int i = 1; i < M.time; i++) {
			MP.incTime();
			f.repaint();
			try {
				TimeUnit.MILLISECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}