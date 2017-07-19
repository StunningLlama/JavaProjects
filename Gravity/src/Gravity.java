import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;


public class Gravity implements Runnable {
	public static final int iteration_multiplier = 1;
	public double zoom = 1.0;
	public double centerx = 0.0;
	public double centery = 0.0;
	public JFrame window;
	public List<Body> bodies;
	public static final double CONSTANT = 2.798; //I have no idea why
	SimulationDisplay canvas;
	Listeners l;
	int SunID = 1;
	int PlanetID = 0;
	long computetime = 0;
	long actualcomputetime = 0;
	int frame = 0;
	public Gravity()
	{
		bodies = new ArrayList<Body>();//                
		double radius = 20.0;
		double sunmass = 100.0;
		double planetmass = 0.0;
		//bodies.add(new Star(0.0, 0.0, 0.0, -(Math.sqrt(planetmass/radius)*(CONSTANT*planetmass/(planetmass+sunmass))), sunmass));
		bodies.add(new Star(0.0, 0.0, 0.0, 0.0, sunmass));
		bodies.get(0).setName("SUN-0001");
		//bodies.add(new Planet(radius, 0.0, 0.0, (Math.sqrt(sunmass/radius)*(CONSTANT*sunmass/(planetmass+sunmass))), planetmass));
		//bodies.get(bodies.size()-1).setName("PLANET-"+String.format("%04d", PlanetID));
		PlanetID++;
		window = new JFrame();
		window.setSize(500, 500);
		window.setVisible(true);
		canvas = new SimulationDisplay(this);
		window.add(canvas);
		canvas.setPreferredSize(new Dimension(SimulationDisplay.width, SimulationDisplay.height));
		window.pack();
		l = new Listeners(this);
		canvas.addMouseListener(l);
		canvas.addMouseWheelListener(l);
		canvas.addKeyListener(l);
		canvas.addMouseMotionListener(l);
	}
	public static void main(String[] args)
	{
		Gravity instance = new Gravity();
		instance.run();
	}
	public void run(){
		while (true)
		{
			frame++;
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {}
			long tstart = System.nanoTime();
			for (int i = 1; i <= iteration_multiplier; i++)
			{
				for (int x = 0; x < bodies.size(); x++)
				{
					for (int y = 0; y < bodies.size(); y++)
					{
						bodies.get(x).Affect(bodies.get(y));
					}
				}
				for (Body x: bodies){
					x.Move();
				}
			}
			computetime = computetime + (System.nanoTime() - tstart);
			if (frame == 30) {
				frame = 0;
				actualcomputetime = computetime / 30;
				computetime = 0;
			}
			//System.out.println(Math.sqrt(bodies.get(0).getDistanceSquared(bodies.get(1))));
			canvas.repaint();
		}
	}

	public int mouseoriginx = 0;
	public int mouseoriginy = 0;
	public int dragstartx = 0;
	public int dragstarty = 0;
	public double tempcenterx;
	public double tempcentery;
	public boolean dragging = false;
}
