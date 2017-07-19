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

	double[] ang;
	double[] angvel;

	RenderCanvas r;
	public static final int POINTS = 25;

	public static void main(String[] args) {
		StringWindow w = new StringWindow();
		w.run();
	}
	
	public StringWindow() {
		this.setSize(800,800);
		ang = new double[POINTS];
		angvel = new double[POINTS];
		for (int i = 0; i < POINTS; i++)
		{
			ang[i]=0.0;
			angvel[i]=0.0;
		}
		this.setVisible(true);
		r= new RenderCanvas(this);
		this.add(r);
		r.addMouseListener(this);
		r.addMouseMotionListener(this);
	}
	//TODO implement more correct formula (distance based)
	public void Physics() {

		for (int i = 1; i < POINTS; i++)
		{
			double dang = ang[i]-ang[i-1];
			angvel[i] -= dang*0.1;
			angvel[i-1] += dang*0.1;
		}
		for (int i = 1; i < POINTS; i++)
		{
			ang[i] += angvel[i];
		}
		for (int i = 0; i < POINTS; i++)
		{
			angvel[i] *= 0.99;
		}
		//pos[POINTS]=pos[POINTS-1];
		if (mouseIsPressed) {
			angvel[x] += (mouseY-250.0)/10000.0;
		}
		//pos[100][100] = 20*(Math.sin(System.currentTimeMillis()/50.0)-0.5);
	}
	
	static int DIV_BY_N = 1;
	static int PHYS_MULTIPLIER = 1;
	
	int i = 0;
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {}
			for (int i = 0; i < 16 ; i++)
				this.Physics();
				r.repaint();
		}
	}

	int mouseX = 0;
	int mouseY = 250;
	int x;
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
	public void mousePressed(MouseEvent arg0) {mouseIsPressed = true;
	x = (int)(mouseX/20.0);
	}
	
	public static double absmin(double x, double y) {
		if (Math.abs(x) < Math.abs(y)) return x;
		return y;
	}
	
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
		double x = 100;
		double y = 400;
		double px = x;
		double py = y;
		for (int i = 0; i < StringWindow.POINTS; i++) {
			x = px + 7.0*Math.cos(parent.ang[i]);
			y = py + 7.0*Math.sin(parent.ang[i]);
			g.setColor(Color.BLUE);
			g.drawLine((int)px, (int)py, (int)x, (int)y);
			px = x;
			py = y;
				//int asd = (int) Math.round(parent.pos[i][j]*40.0);
				//Color c = new Color(clamp(asd, 0, 255), 0, clamp(-asd, 0, 255));
				//g.setColor(c);
				//g.fillRect(i*INTERVAL, j*INTERVAL, INTERVAL, INTERVAL);
				//g.drawLine(i*20, (int)(parent.pos[i]*10.0+250), (i+1)*20, (int)(parent.pos[i+1]*10.0+250));
				//g.setColor(Color.BLUE);
				//g.fillRect(i*20, (int)(parent.pos[i]*10.0+250), 2, 2);
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