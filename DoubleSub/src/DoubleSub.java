import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class DoubleSub extends JFrame implements MouseListener, Runnable {
	private static final long serialVersionUID = 1L;
	public int[][] grid;
	Display canvas;
	public static final int XW = 8;
	public static final int YW = 8;
	public DoubleSub()
	{
		grid = new int[XW][YW];
		for (int x = 0; x < 8; x++)
			for (int y = 0; y < 8; y++)
				grid[x][y] = (int)(Math.random()*10+1);
		setSize(500, 500);
		setVisible(true);
		canvas = new Display(this);
		add(canvas);
		Insets i = this.getInsets();
		setPreferredSize(new Dimension(XW*64+i.left+i.right+1+64*2, YW*64+i.top+i.bottom+1+64*2));
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
			if (event.getX()/64 == 0) {
				for (int x = 0; x < 8; x++) {
					grid[x][event.getY()/64 - 1] *= 2;
				}
			}
			if (event.getY()/64 == 0) {
				for (int y = 0; y < 8; y++) {
					grid[event.getX()/64 - 1][y] --;
				}
			}
			if (event.getX()/64 == 9) {
				for (int x = 0; x < 8; x++) {
					grid[x][event.getY()/64 - 1] *= 4;
				}
			}
			if (event.getY()/64 == 9) {
				for (int y = 0; y < 8; y++) {
					grid[event.getX()/64 - 1][y] -= 10;
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		DoubleSub instance = new DoubleSub();
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
	DoubleSub parent;
	public Display(DoubleSub k)
	{
		parent = k;
		display = k.createImage(DoubleSub.XW*64+1+2*64, DoubleSub.YW*64+1+2*64);
	}
	@Override
	public void paintComponent(Graphics realgphx)
	{
		Graphics g = display.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, DoubleSub.XW*64+1+2*64, DoubleSub.YW*64+1+2*64);
		for (int x = 0; x < DoubleSub.XW+2; x++)
		{
			for (int y = 0; y < DoubleSub.YW+2; y++)
			{
				g.setColor(Color.BLACK);
				g.drawRect(x*64, y*64, 64, 64);
			}
		}
		for (int x = 0; x < DoubleSub.XW; x++)
		{
			for (int y = 0; y < DoubleSub.YW; y++)
			{
				g.setColor(Color.BLACK);
				g.drawString(String.valueOf(parent.grid[x][y]), x*64+96, y*64+96);
			}
		}
		realgphx.drawImage(display, 0, 0, parent);
	}
}