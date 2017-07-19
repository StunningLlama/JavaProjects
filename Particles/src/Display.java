import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;


public class Display extends JPanel {
	private static final long serialVersionUID = 5322839047131059135L;
	public static int SUPERSAMPLING = 1;
	Particles parent;
	Image display;
	public static final int width = 1600;
	public static final int height = 900;
	
	public Display(Particles g)
	{
		parent = g;
		display = g.window.createImage(width*SUPERSAMPLING, height*SUPERSAMPLING);
	}
	
	@Override
	public void paintComponent(Graphics realgphx)
	{
		parent.frames++;
		Graphics2D g = (Graphics2D)display.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, display.getWidth(parent.window), display.getHeight(parent.window));
		//parent.particles.
		try {
			while (parent.particles_LOCK) {
				Thread.sleep(0, 100000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		parent.particles_LOCK = true;
		for (Particle b: parent.particles)
		{
			b.render(g, SUPERSAMPLING *(((b.x-parent.centerx)*parent.zoom)+width/2.0), SUPERSAMPLING *((b.y-parent.centery)*parent.zoom)+height/2.0);
		}
		parent.particles_LOCK = false;
		for (ForceField b: parent.forcefields)
		{
			b.render(g, SUPERSAMPLING * ((b.x-parent.centerx)*parent.zoom)+width/2, SUPERSAMPLING * ((b.y-parent.centery)*parent.zoom)+height/2);
		}
		for (Emitter b: parent.emitters)
		{
			b.render(g, SUPERSAMPLING * ((b.x-parent.centerx)*parent.zoom)+width/2, SUPERSAMPLING * ((b.y-parent.centery)*parent.zoom)+height/2);
		}
		g.drawString("Compute: " + parent.actualcomputetime/1000000.0, 1, 12);
		g.drawString("Particles: " + parent.particles.size(), 1, 22);
		g.drawString("FPS: " + parent.fps, 1, 32);
		if (SUPERSAMPLING > 1)
			realgphx.drawImage(display.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, parent.window);
		else
			realgphx.drawImage(display, 0, 0, parent.window);
	}
}
