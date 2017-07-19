import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 *              | 450.0
 *              |
 *              |
 *              |
 *              |
 * -------------|--------------
 *              |
 *              |
 *              |
 *              |
 *              |
 */
public class LiquidSim extends JFrame implements Runnable, MouseListener {
	public static int WIDTH = 1600;
	public static int HEIGHT = 900;
	public static int PHYSICS_ITER = 10;
	CopyOnWriteArrayList<Particle> particles;
	Display d;
	public LiquidSim() {
		super();
		setVisible(true);
		d = new Display(this);
		this.add(d);
		d.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.pack();
		d.addMouseListener(this);
		particles = new CopyOnWriteArrayList<Particle>();
	}
	public static void main(String[] args) {
		(new LiquidSim()).run();
	}
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < PHYSICS_ITER; i++) {
				for (int x = 0; x < particles.size(); x++)
				{
					for (int y = 0; y < particles.size(); y++)
					{
						particles.get(x).updatevel(particles.get(y));
					}
				}
				for (Particle x: particles){
					x.updatepos();
				}
			}
			//d.potato-= 0.001;
			d.repaint();
		}
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent event) {
		particles.add(new Particle(event.getX()*10.0-800.0, event.getY()*10.0-450.0));
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}

class Particle {
	double x;
	double y;
	double vx;
	double vy;
	double mass = 1.0;
	public static double velScaleFactor = 200.0;
	
	public Particle(double xx, double yy) {
		x = xx;
		y = yy;
		vx = 0;
		vy = 0;
	}
	
	public void updatevel(Particle p) {

		if (this != p)
		{
			double xdistance = Math.abs(this.x-p.x);
			double ydistance = Math.abs(this.y-p.y);
			if (xdistance == 0 && ydistance == 0) {
				vx = 0.2;
				vy = 0.2;
			}
			else{
			double xfraction = (xdistance/(xdistance+ydistance));
			double yfraction = (ydistance/(xdistance+ydistance));
			double distancesquared = func(Math.sqrt(xdistance*xdistance+ydistance*ydistance));
			vx += (xfraction * (this.mass*distancesquared) * velScaleFactor * Math.signum(this.x-p.x));
			vy += (yfraction * (this.mass*distancesquared) * velScaleFactor * Math.signum(this.y-p.y));
			}
		}
	}
	
	private double func(double dist) {
		//if (dist < 100)
		///	return 0.01;
		//return 0.;
		//return Math.exp(-(dist*dist)/4000.0)/2.0;
		return 3.0/(dist*dist);
	}
	
	public static double bounce = 0.0;
	
	public void updatepos() {
		//vx += Math.random()*0.05/LiquidSim.PHYSICS_ITER;
		//vy += Math.random()*0.2/LiquidSim.PHYSICS_ITER;
		vy += 1.0/LiquidSim.PHYSICS_ITER;
		vx *= Math.pow(0.92, 1/LiquidSim.PHYSICS_ITER);
		vy *= Math.pow(0.92, 1/LiquidSim.PHYSICS_ITER);
		x += vx/LiquidSim.PHYSICS_ITER;
		y += vy/LiquidSim.PHYSICS_ITER;
		if (x < -790) {
			x = -790;
			vx = -vx*bounce;
		}
		if (x > 790) {
			x = 790;
			vx = -vx*bounce;
		}
		if (y < -440) {
			y = -440;
			vy = -vy*bounce;
		}
		if (y > 440) {
			y = 440;
			vy = -vy*bounce;
			vy -= Math.random()*8.;
		}
	}
}

class Display extends JPanel {
	BufferedImage img;
	LiquidSim p;
	public Display(LiquidSim m) {
		img = (BufferedImage) m.createImage(LiquidSim.WIDTH, LiquidSim.HEIGHT);
		p = m;
	}
	@Override
	public void paintComponent(Graphics g) {
		Graphics gr = img.getGraphics();
		gr.setColor(Color.WHITE);
		gr.fillRect(0, 0, LiquidSim.WIDTH, LiquidSim.HEIGHT);
		gr.setColor(Color.CYAN);
		for (Particle part: p.particles) {
			gr.fillOval(((int)(part.x*0.1))+80-5, ((int)(part.y*0.1))+45-5, 11, 11);
		}
		g.drawImage(img, 0, 0, p);
	}
}