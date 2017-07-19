import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JPanel;


public class GraphCanvas extends JPanel {
	private static final long serialVersionUID = 8269844896112698725L;
	public double x;
	public double y;
	public double zoom;
	private Image canvas;
	public MathUtil math;
	public Display display;
	public static double SAMPLE_RATE = 1;
	public String error = "Insert equation then press [ENTER]";
	public int fps = 0;
	public double[] values;

	public int mouseoriginx = 0;
	public int mouseoriginy = 0;
	public int dragstartx = 0;
	public int dragstarty = 0;
	public double tempcenterx;
	public double tempcentery;
	public boolean dragging = false;
	
	public long totalCompute = 0;
	public long totalRender = 0;
	public double computeTime = 0;
	public double renderTime = 0;
	
	public GraphCanvas(MathUtil i, Display d) {
		values = new double[Graph.WIDTH];
		canvas = d.createImage(Graph.WIDTH, Graph.HEIGHT);
		x = 0;
		y = 0;
		zoom = 100;
		math = i;
		display = d;
	}
	
	@Override
	public void paintComponent(Graphics realgphx)
	{
		if (Graph.g == null) return;
		Graph.g.startCounter();
		Graphics g = canvas.getGraphics();
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, canvas.getWidth(display), canvas.getHeight(display));
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(0, (int)Math.round(-y*zoom)+getHeight()/2, Graph.WIDTH, (int)Math.round(-y*zoom)+getHeight()/2); //x axis
		g.drawLine((int)Math.round(-x*zoom)+getWidth()/2, 0, (int)Math.round(-x*zoom)+getWidth()/2, Graph.HEIGHT); //y axis
		g.setColor(Color.BLACK);
		int iy;
		int liy;
		double a;
		double b;
		for (double ix = 1; ix < Graph.WIDTH; ix += SAMPLE_RATE)
		{
			a = values[(int)ix];
			b = values[(int)ix-1];
			iy = (int)Math.round((a-y)*zoom)+getHeight()/2;
			liy = (int)Math.round((b-y)*zoom)+getHeight()/2;
			/*if (Double.isInfinite(a) ^ Double.isInfinite(b)) {
				if (a == Double.NEGATIVE_INFINITY)
					iy = Graph.HEIGHT;
				if (b == Double.NEGATIVE_INFINITY)
					liy = Graph.HEIGHT;
				if (a == Double.POSITIVE_INFINITY)
					iy = 0;
				if (b == Double.POSITIVE_INFINITY)
					liy = 0;
			}*/
			if (Double.isFinite(a) && Double.isFinite(b)) {
				g.drawLine((int)(ix-SAMPLE_RATE), liy, (int)ix, iy);
			} else {
			}
		}
		g.setColor(Color.RED);
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		g.drawString("FPS: " + fps, 4, 14);
		g.drawString(error, 4, 26);
		g.drawString("Compute: " + computeTime, 4, 38);
		g.drawString("Render: " + renderTime, 4, 50);
		realgphx.drawImage(canvas, 0, 0, display);
		Graph.g.framesdrawn++;
		totalRender += Graph.g.endCounter();
	}
}
