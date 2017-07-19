import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Bacteria extends JFrame implements MouseListener, Runnable {
	private static final long serialVersionUID = 1L;
	public int[][] grid;
	Display canvas;
	public static final int XW = 16;
	public static final int YW = 16;
	public Bacteria()
	{
		grid = new int[XW][YW];
		for (int x = 0; x < 16; x++)
			for (int y = 0; y < 16; y++) {
				grid[x][y] = 0;
				
			}
		grid[0][0] = 1;
		setSize(500, 500);
		setVisible(true);
		canvas = new Display(this);
		add(canvas);
		Insets i = this.getInsets();
		setPreferredSize(new Dimension(XW*32+i.left+i.right+1, YW*32+i.top+i.bottom+1));
		pack();
		canvas.addMouseListener(this);
		
	}
	@Override public void mouseClicked(MouseEvent arg0) {}
	@Override public void mouseEntered(MouseEvent arg0) {}
	@Override public void mouseExited(MouseEvent arg0) {}
	@Override public void mouseReleased(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON1)
		{
			int x = event.getX()/32;
			int y = event.getY()/32;
			if (grid[x][y] == 1 && grid[x+1][y] == 0 && grid[x][y+1] == 0) {
				grid[x][y] = 0;
				grid[x+1][y] = 1;
				grid[x][y+1] = 1;
			}
		}
		if (event.getButton() == MouseEvent.BUTTON2) {
			
		}
	}
	
	public static void main(String[] args)
	{
		Bacteria instance = new Bacteria();
		instance.run();
	}
	public void run(){
		while (true)
		{
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {}
			canvas.repaint();
		}
	}
}

class Display extends JPanel
{
	Image display;
	Bacteria parent;
	public Display(Bacteria k)
	{
		parent = k;
		display = k.createImage(Bacteria.XW*32+1, Bacteria.YW*32+1);
	}
	@Override
	public void paintComponent(Graphics realgphx)
	{
		Graphics g = display.getGraphics();
		for (int x = 0; x < Bacteria.XW; x++)
		{
			for (int y = 0; y < Bacteria.YW; y++)
			{
				if (parent.grid[x][y] == 0)
					g.setColor(Color.BLUE);
				else
					g.setColor(Color.RED);
				g.fillRect(x*32, y*32, 32, 32);
				g.setColor(Color.BLACK);
				g.drawRect(x*32, y*32, 32, 32);
			}
		}
		realgphx.drawImage(display, 0, 0, parent);
	}
}