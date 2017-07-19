import java.awt.Color;
import java.awt.Graphics2D;


public class Particle extends RenderableObject{
	double vx;
	double vy;
	static Color[] colors;
	public static void initColor() {
		colors = new Color[192];
		int ind = 0;
		for (int i = 0; i < 256; i += 8)
		{
			colors[ind] = new Color(255, i, 0);
			ind++;
		}
		for (int i = 255; i >= 0; i -= 8)
		{
			colors[ind] = new Color(i, 255, 0);
			ind++;
		}
		for (int i = 0; i < 256; i += 8)
		{
			colors[ind] = new Color(0, 255, i);
			ind++;
		}
		for (int i = 255; i >= 0; i -= 8)
		{
			colors[ind] = new Color(0, i, 255);
			ind++;
		}
		for (int i = 0; i < 256; i += 8)
		{
			colors[ind] = new Color(i, 0, 255);
			ind++;
		}
		for (int i = 255; i >= 0; i -= 8)
		{
			colors[ind] = new Color(255, 0, i);
			ind++;
		}
	}
	public void Move() {
		x += vx;
		y += vy;
	}
	public Particle(double xi, double yi, double vxi, double vyi) {
		x = xi;
		y = yi;
		vx = vxi;
		vy = vyi;
	}
	@Override
	public void render(Graphics2D g, double x, double y) {
		double vel = Math.sqrt(vx*vx+vy*vy);
		g.setColor(colors[Math.min((int)(vel*10.0), 191)]);
		g.fillRect((int)x, (int)y, Display.SUPERSAMPLING, Display.SUPERSAMPLING);
		/*double lower = (y-0.5)-(int)(y-0.5);
		double upper = 1-lower;
		double right = (x-0.5)-(int)(x-0.5);
		double left = 1-right;
		int xoff = -1;
		int yoff = -1;
		if (x-(int)x > 0.5) xoff = 0;
		if (y-(int)y > 0.5) yoff = 0;
		int xx = xoff+(int) x;
		int yy = yoff+(int) y;
		g.setColor(new Color(0, 255, 0, (int)(upper*left*255)));
		g.drawLine(xx, yy, xx, yy);
		g.setColor(new Color(0, 255, 0, (int)(upper*right*255)));
		g.drawLine(xx+1, yy, xx+1, yy);
		g.setColor(new Color(0, 255, 0, (int)(lower*left*255)));
		g.drawLine(xx, yy+1, xx, yy+1);
		g.setColor(new Color(0, 255, 0, (int)(lower*right*255)));
		g.drawLine(xx+1, yy+1, xx+1, yy+1);
		*/
	}
}
