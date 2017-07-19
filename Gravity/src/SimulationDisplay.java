import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;


public class SimulationDisplay extends JPanel {
	private static final long serialVersionUID = 5322839047131059135L;
	Gravity parent;
	Image display;
	public static final int width = 1600;
	public static final int height = 900;
	
	public SimulationDisplay(Gravity g)
	{
		parent = g;
		display = g.window.createImage(width, height);
	}
	
	@Override
	public void paintComponent(Graphics realgphx)
	{
		Graphics g = display.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, display.getWidth(parent.window), display.getHeight(parent.window));
		for (Body b: parent.bodies)
		{
			b.Render(g, (int)((b.x-parent.centerx)*parent.zoom)+width/2, (int)((b.y-parent.centery)*parent.zoom)+height/2);
		}
		g.drawString("Compute: " + parent.actualcomputetime/1000000.0, 1, 12);
		realgphx.drawImage(display, 0, 0, parent.window);
	}
}
