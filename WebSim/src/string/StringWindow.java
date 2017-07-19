package string;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class StringWindow extends JFrame implements Runnable, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	public static final int POSX = 0;
	public static final int POSY = 1;
	public static final int VELX = 2;
	public static final int VELY = 3;
	
	int cpX = 0;
	int cpY = 0;
	
	double[][][] points;
	int[][] connections;
	int[] xNeighbors = {-1, 0, 1, 0};
	int[] yNeighbors = {0, -1, 0, 1};
	
	RenderCanvas r;
	public static final int POINTSX = 15;
	public static final int POINTSY = 15;
	
	public static final int xBegin = 0;
	public static final int xEnd = 0;
	public static final int yBegin = 1;
	public static final int yEnd = 0;
	public static final double xSpacing = 1.0;
	public static final double ySpacing = 1.0;
	
	public static final int PHYSICS_ITER = 10;
	public static final double PHYSICS_MUL = 1.0/PHYSICS_ITER;
	public static final double DAMPENING = Math.pow(0.98, PHYSICS_MUL);
	public static final double GRAVITY = 0.02*PHYSICS_MUL;
	public static final double VEL_COEFF = 0.4;
	public static final double MAXDIST = 1.0;

	public double tension(int x1, int y1, int x2, int y2, double dist) {
		if (dist < MAXDIST) return 0.0;
		else return (dist-MAXDIST)*(dist-MAXDIST);
	}
	
	public static void main(String[] args) {
		StringWindow w = new StringWindow();
		w.run();
	}
	
	public StringWindow() {
		this.setSize(900,900);
		points = new double[POINTSX][POINTSY][4];
		for (int i = 0; i < POINTSX; i++)
		{
			for (int k = 0; k < POINTSY; k++)
			{
				points[i][k][POSX]=i*xSpacing;
				points[i][k][VELX]=0.0;
				points[i][k][POSY]=k*ySpacing;
				points[i][k][VELY]=0.0;
			}
		}
		connections = new int[POINTSX-1][POINTSY-1];
		for (int i = 0; i < POINTSX-1; i++)
			for (int k = 0; k < POINTSY-1; k++)
				connections[i][k] = 0x3;
		this.setVisible(true);
		r= new RenderCanvas(this);
		this.add(r);
		r.addMouseListener(this);
		r.addMouseMotionListener(this);
	}
	
	public void Physics() {
		for (int i = xBegin; i < POINTSX - xEnd; i++)
		{
			for (int k = yBegin; k < POINTSY - yEnd; k++)
			{
				for (int n = 0; n < 4; n++) {
					if (isConnected(i, k, i+xNeighbors[n], k+yNeighbors[n])) {
						double xc = (points[i+xNeighbors[n]][k+yNeighbors[n]][POSX] - points[i][k][POSX]);
						double yc = (points[i+xNeighbors[n]][k+yNeighbors[n]][POSY] - points[i][k][POSY]);
						double dist = Math.sqrt(xc*xc+yc*yc);
						if (dist != 0) {
							points[i][k][VELX] += (xc/dist)*tension(i, k, i+xNeighbors[n], k+yNeighbors[n], dist)*VEL_COEFF*PHYSICS_MUL;
							points[i][k][VELY] += (yc/dist)*tension(i, k, i+xNeighbors[n], k+yNeighbors[n], dist)*VEL_COEFF*PHYSICS_MUL;
						}
					}
				}
			}
		}
		for (int i = xBegin; i < POINTSX - xEnd; i++)
		{
			for (int k = yBegin; k < POINTSY - yEnd; k++)
			{
				points[i][k][VELY] += GRAVITY;
				points[i][k][VELX] *= DAMPENING;
				points[i][k][VELY] *= DAMPENING;
			}
		}
		for (int i = xBegin; i < POINTSX - xEnd; i++)
		{
			for (int k = yBegin; k < POINTSY - yEnd; k++)
			{
				points[i][k][POSX] += points[i][k][VELX]*PHYSICS_MUL;
				points[i][k][POSY] += points[i][k][VELY]*PHYSICS_MUL;
			}
		}
		if (dragging) {
			points[cpX][cpY][POSX] = reversetransform(mouseX);
			points[cpX][cpY][POSY] = reversetransform(mouseY);
			points[cpX][cpY][VELX] = 0;
			points[cpX][cpY][VELY] = 0;
		}
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {}
			for (int i = 0; i < PHYSICS_ITER; i++)
				this.Physics();
			r.repaint();
		}
	}

	boolean updateMousePosition = true;
	boolean dragging = false;
	int mouseX = 0;
	int mouseY = 250;
	PointerInfo p = MouseInfo.getPointerInfo();

	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {
		double min = 1E99;
		for (int i = xBegin; i < StringWindow.POINTSX-xEnd; i++) {
			for (int k = yBegin; k < StringWindow.POINTSY-yEnd; k++) {
				double xDist = (transform(points[i][k][POSX])-mouseX);
				double yDist = (transform(points[i][k][POSY])-mouseY);
				if (xDist*xDist+yDist*yDist < min) {
					min = xDist*xDist+yDist*yDist;
					cpX = i;
					cpY = k;
				}
			}
		}
		dragging = true;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		dragging = false;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (updateMousePosition)
		{
			mouseX = arg0.getX();
			mouseY = arg0.getY();
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		if (updateMousePosition)
		{
			mouseX = arg0.getX();
			mouseY = arg0.getY();
		}
	}
	
	public boolean isConnected(int x1, int y1, int x2, int y2) {
		if (x1 < 0 || x2 < 0 || x1 >= POINTSX || x2 >= POINTSX || y1 < 0 || y2 < 0 || y1 >= POINTSY || y2 >= POINTSY)
			return false;
		return connections[(x1<x2)?x1:x2][(y1<y2)?y1:y2] == 1;
	}
	
	public double transform(double x) {
		return x*10.0 + 200.0;
	}
	
	public double reversetransform(double y) {
		return (y-200.0)*0.1;
	}
}

class RenderCanvas extends JPanel {

	private static final long serialVersionUID = 1L;
	public static final int POSX = 0;
	public static final int POSY = 1;
	public static final int VELX = 2;
	public static final int VELY = 3;

	Image screen;
	StringWindow p;
	@Override
	public void paintComponent(Graphics real) {
		Graphics g = screen.getGraphics();
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 1500, 1500);
		for (int i = 0; i < StringWindow.POINTSX; i++) {
			for (int k = 0; k < StringWindow.POINTSY; k++) {
				g.setColor(Color.BLACK);
				if (Double.isNaN(p.points[i][k][POSX])||Double.isNaN(p.points[i][k][POSY])) g.setColor(Color.RED);
				if (i < StringWindow.POINTSX-1)
					g.drawLine((int)p.transform(p.points[i][k][POSX]), (int)p.transform(p.points[i][k][POSY]), (int)p.transform(p.points[i+1][k][POSX]), (int)p.transform(p.points[i+1][k][POSY]));
				if (k < StringWindow.POINTSY-1)
					g.drawLine((int)p.transform(p.points[i][k][POSX]), (int)p.transform(p.points[i][k][POSY]), (int)p.transform(p.points[i][k+1][POSX]), (int)p.transform(p.points[i][k+1][POSY]));
					
				g.setColor(Color.BLUE);
				g.fillRect((int)p.transform(p.points[i][k][POSX]) - 0, (int)p.transform(p.points[i][k][POSY]) - 0, 1, 1);
			}
		}
		real.drawImage(screen, 0, 0, p);
}
	
	public RenderCanvas(StringWindow w) {
		p = w;
		screen = p.createImage(1500,1500);
	}
}