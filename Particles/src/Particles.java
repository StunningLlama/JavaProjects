import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;


public class Particles implements Runnable {
	public int selected = 1;
	public static final int iteration_multiplier = 1;
	public static final double speed_mul = 1.0;
	public double zoom = 1.0;
	public double centerx = 0.0;
	public double centery = 0.0;
	public JFrame window;
	public List<Emitter> emitters;
	public List<ForceField> forcefields;
	public Collection<Particle> particles;
	public static final double CONSTANT = 2.798; //wat
	public final long frametime = 16666666;
	Display canvas;
	Listeners l;
	int SunID = 1;
	int PlanetID = 0;
	long computetime = 0;
	long actualcomputetime = 0;
	int frame = 0;
	int frames = 0;
	int fps = 60;
	public boolean particles_LOCK = false;
	public Particles()
	{
		Particle.initColor();
		emitters = new CopyOnWriteArrayList<Emitter>();
		forcefields = new CopyOnWriteArrayList<ForceField>();
		particles = new ArrayList<Particle>();
		emitters.add(new Emitter(-300.0, 0.0));
		PlanetID++;
		window = new JFrame();
		window.setSize(500, 500);
		window.setVisible(true);
		canvas = new Display(this);
		window.add(canvas);
		canvas.setPreferredSize(new Dimension(Display.width, Display.height));
		window.pack();
		l = new Listeners(this);
		canvas.addMouseListener(l);
		canvas.addMouseWheelListener(l);
		canvas.addKeyListener(l);
		canvas.addMouseMotionListener(l);
		canvas.setDoubleBuffered(true);
	}
	public static void main(String[] args)
	{
		Particles instance = new Particles();
		instance.run();
	}
	public void run(){
		long c = 0;
		while (true)
		{
			frame++;
			try {
				if (c < 15999999)
					Thread.sleep((16000000-c)/1000000, (int)((16000000-c)%1000000));
			} catch (InterruptedException e) {}
			long tstart = System.nanoTime();
			try {
				while (particles_LOCK) {
					Thread.sleep(0, 100000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			c = System.nanoTime();
			particles_LOCK = true;
			for (Emitter e: emitters) {
				for (int i = 0; i < e.particleDensity; i++) {
					double angle = Math.random()*e.arcLength-e.arcLength/2.0+e.angleCenter;
					double speed = Math.random()*e.speedRange + e.speedMin;
					particles.add(new Particle(e.x, e.y, Math.sin(Math.toRadians(angle))*speed*speed_mul, Math.cos(Math.toRadians(angle))*speed*speed_mul));
				}
			}
			for (int i = 1; i <= iteration_multiplier; i++)
			{
				for (Particle p: particles)
				{
					for (int f = 0; f < forcefields.size(); f++)
					{
						forcefields.get(f).Affect(p);
					}
				}
				for (Particle b: particles){
					b.Move();
				}
			}
			Iterator<Particle> i = particles.iterator();
			while (i.hasNext()){
				Particle b = i.next();
				int x = (int)((b.x-centerx)*zoom)+Display.width/2;
				int y = (int)((b.y-centery)*zoom)+Display.height/2;
				if (x < 0 || x > Display.width || y < 0 || y > Display.height)
					i.remove();
			}
			particles_LOCK = false;
			c = System.nanoTime() - c;
			computetime = computetime + (System.nanoTime() - tstart);
			if (frame % 30 == 0) {
				actualcomputetime = computetime / 30;
				computetime = 0;
			}
			if (frame % 60 == 0) {
				fps = frames;
				frames = 0;
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
