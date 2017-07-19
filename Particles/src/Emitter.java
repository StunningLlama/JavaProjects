import java.awt.Color;
import java.awt.Graphics2D;


public class Emitter extends RenderableObject{
	double angleCenter;
	double arcLength; //Degrees
	int particleDensity;
	double speedMin;
	double speedRange;
	@Override
	public void render(Graphics2D g, double x, double y) {
		g.setColor(Color.GRAY);
		g.drawLine((int)x, (int)y, (int)x, (int)y);
	}
	
	public Emitter(double xi, double yi) {
		x = xi;
		y = yi;
		angleCenter = 90;
		arcLength = 60;
		particleDensity = 30;
		speedMin = 1;
		speedRange = 0.5;
	}
}
