package string;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class StringWindow extends JFrame implements Runnable, MouseListener, MouseMotionListener {

	double[][] pos;
	double[][] vel;
	RenderCanvas r;
	public static final int POINTS = 256;

	public static void main(String[] args) {
		StringWindow w = new StringWindow();
		w.run();
	}
	
	public StringWindow() {
		this.setSize(800,800);
		pos = new double[POINTS+2][POINTS+2];
		vel = new double[POINTS+2][POINTS+2];
		for (int i = 0; i < POINTS+2; i++)
		{
			for (int j = 0; j < POINTS+2; j++)
			{
				pos[i][j]=0.0;
				vel[i][j]=0.0;
			}
		}
		this.setVisible(true);
		r= new RenderCanvas(this);
		this.add(r);
		r.addMouseListener(this);
		r.addMouseMotionListener(this);
	}
	//TODO implement more correct formula (distance based)
	public void Physics() {
		
		for (int i = 1; i < POINTS-1; i++)
		{
			for (int j = 1; j < POINTS-1; j++)
			{
				vel[i][j] += 0.5*((pos[i-1][j]+pos[i+1][j]+pos[i][j-1]+pos[i][j+1])/4-pos[i][j]);
			}
		}
		for (int i = 1; i < POINTS-1; i++)
		{
			for (int j = 1; j < POINTS-1; j++)
			{
				vel[i][j] *= 0.995;
				//vel[i][j] += 0.002;
			}
		}
		for (int i = 1; i < POINTS-1; i++)
		{
			for (int j = 1; j < POINTS-1; j++)
			{
				pos[i][j] += vel[i][j]*1.0;
			}
		}
		if (mouseIsPressed) {
			pos[mouseX/RenderCanvas.INTERVAL][mouseY/RenderCanvas.INTERVAL] = 10.0;//10.0*Math.sin(time*20.0);
		}
		//pos[100][100] = 20*(Math.sin(System.currentTimeMillis()/50.0)-0.5);
	}
	
	static int DIV_BY_N = 1;
	static int PHYS_MULTIPLIER = 5;
	
	double time = 0.0;
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(17/DIV_BY_N);
			} catch (InterruptedException e) {}
			for (int i = 0; i < PHYS_MULTIPLIER ; i++)
				this.Physics();
			r.repaint();
			time += 1.0/60.0;
		}
	}

	int mouseX = 0;
	int mouseY = 250;

	@Override
	public void mouseClicked(MouseEvent arg0) {
		//updateMousePosition = !updateMousePosition;
	}
	boolean mouseIsPressed = false;
	PointerInfo p = MouseInfo.getPointerInfo();
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {mouseIsPressed = true;}
	@Override
	public void mouseReleased(MouseEvent arg0) {mouseIsPressed = false;}

	@Override
	public void mouseDragged(MouseEvent arg0) {
			mouseX = arg0.getX();
			mouseY = arg0.getY();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
			mouseX = arg0.getX();
			mouseY = arg0.getY();
	}
	
	
}

class RenderCanvas extends JPanel {
	Image screen;
	StringWindow parent;
	public static final int INTERVAL = 4;
	@Override
	public void paintComponent(Graphics real) {
		Graphics g = screen.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 800, 800);
		for (int i = 0; i < StringWindow.POINTS; i++) {
			for (int j = 0; j < StringWindow.POINTS; j++) {
				int asd = (int) Math.round(parent.pos[i][j]*40.0);
				Color c = new Color(clamp(asd, 0, 255), 0, clamp(-asd, 0, 255));
				g.setColor(c);
				g.fillRect(i*INTERVAL, j*INTERVAL, INTERVAL, INTERVAL);
				//g.drawLine(i*7, (int)(parent.pos[i]*10.0+250), (i+1)*7, (int)(parent.pos[i+1]*10.0+250));
				//g.setColor(Color.BLUE);
				//g.fillRect(i*7, (int)(parent.pos[i]*10.0+250), 2, 2);
			}
		}
		real.drawImage(screen, 0, 0, parent);
	}
	
	private int clamp(int val, int min, int max) {
		if (val < min) return min;
		if (val > max) return max;
		return val;
	}
	public RenderCanvas(StringWindow w) {
		parent = w;
		screen = parent.createImage(800,800);
	}
}