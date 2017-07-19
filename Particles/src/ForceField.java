import java.awt.Color;
import java.awt.Graphics2D;


public class ForceField extends RenderableObject {
	public double strength;
	public static double velScaleFactor = 10.0;
	public static double EXP = 2.0;

	@Override
	public void render(Graphics2D g, double x, double y) {
		g.setColor(Color.RED);
		g.drawLine((int)x, (int)y, (int)x, (int)y);
	}
	public void Affect(Particle b) {

		double xdistance = Math.abs(this.x-b.x);
		double ydistance = Math.abs(this.y-b.y);
		double xfraction = (xdistance/(xdistance+ydistance));
		double yfraction = (ydistance/(xdistance+ydistance));
		double distancesquared = Math.pow(Math.sqrt(xdistance*xdistance+ydistance*ydistance), EXP);
		b.vx += (xfraction * (this.strength/distancesquared) * velScaleFactor * Math.signum(this.x-b.x));
		b.vy += (yfraction * (this.strength/distancesquared) * velScaleFactor * Math.signum(this.y-b.y));
	}
	public ForceField(double xi, double yi) {
		this.x = xi;
		this.y = yi;
		this.strength = -50.0;
	}
}
