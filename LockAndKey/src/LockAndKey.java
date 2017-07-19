import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class LockAndKey extends JFrame implements MouseListener, Runnable {
	private static final long serialVersionUID = 1L;
	public int[][] grid;
	Display canvas;
	public static final int XW = 8;
	public static final int YW = 8;
	public LockAndKey()
	{
		grid = new int[XW][YW];
		for (int x = 0; x < 8; x++)
			for (int y = 0; y < 8; y++) {
				if ((x*8+y)%2==0&&(x%2)==0 || (x*8+y)%2==1&&(x%2)==1)
				grid[x][y] = 0;
				else
					grid[x][y]=1;
			}
		setSize(500, 500);
		setVisible(true);
		canvas = new Display(this);
		add(canvas);
		Insets i = this.getInsets();
		setPreferredSize(new Dimension(XW*64+i.left+i.right+1, YW*64+i.top+i.bottom+1));
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
			for (int x = 0; x < XW; x++)
				grid[x][event.getY()/64] ^= 0x01;
			//for (int y = 0; y < XW; y++)
			//	grid[event.getX()/64][y] ^= 0x01;
			//grid[event.getX()/64][event.getY()/64] ^= 0x01;
		}
		if (event.getButton() == MouseEvent.BUTTON3)
			//grid[event.getX()/64][event.getY()/64] ^= 0x01;
			for (int y = 0; y < XW; y++)
				grid[event.getX()/64][y] ^= 0x01;
		if (event.getButton() == MouseEvent.BUTTON2) {
			grid[event.getX()/64][event.getY()/64] ^= 0x01;
		grid[event.getX()/64+1][event.getY()/64] ^= 0x01;
		grid[event.getX()/64][event.getY()/64+1] ^= 0x01;
		grid[event.getX()/64+1][event.getY()/64+1] ^= 0x01;
		}
	}
	
	public static void main(String[] args)
	{
		LockAndKey instance = new LockAndKey();
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
	LockAndKey parent;
	public Display(LockAndKey k)
	{
		parent = k;
		display = k.createImage(LockAndKey.XW*64+1, LockAndKey.YW*64+1);
	}
	@Override
	public void paintComponent(Graphics realgphx)
	{
		Graphics g = display.getGraphics();
		for (int x = 0; x < LockAndKey.XW; x++)
		{
			for (int y = 0; y < LockAndKey.YW; y++)
			{
				if (parent.grid[x][y] == 0)
					g.setColor(Color.BLUE);
				else
					g.setColor(Color.RED);
				g.fillRect(x*64, y*64, 64, 64);
				g.setColor(Color.BLACK);
				g.drawRect(x*64, y*64, 64, 64);
			}
		}
		realgphx.drawImage(display, 0, 0, parent);
	}
}