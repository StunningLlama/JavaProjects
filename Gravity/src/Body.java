import java.awt.Graphics;


public abstract class Body {
	
	public static double velScaleFactor = 10.0;
	public static double EXP = 2.0;
	public double mass = 1.0;
	public abstract void Render(Graphics g, int x, int y);
	public String name = "";
	
	public double x;
	public double y;
	
	public double vx;
	public double vy;
	
	public boolean fixed;
	
	public Body(double ix, double iy, double ivx, double ivy, double imass)
	{
		x = ix; y = iy; vx = ivx; vy = ivy; mass = imass; fixed = false;
	}
	
	public void setName(String n)
	{
		name = n;
	}
	
	public void Affect(Body b) {
		if (this != b && !b.fixed)
		{
			double xdistance = Math.abs(this.x-b.x);
			double ydistance = Math.abs(this.y-b.y);
			double xfraction = (xdistance/(xdistance+ydistance));
			double yfraction = (ydistance/(xdistance+ydistance));
			double distancesquared = Math.pow(Math.sqrt(xdistance*xdistance+ydistance*ydistance), EXP);
			b.vx += (xfraction * (this.mass/distancesquared) * velScaleFactor * Math.signum(this.x-b.x));
			b.vy += (yfraction * (this.mass/distancesquared) * velScaleFactor * Math.signum(this.y-b.y));
		}
	}
	
	public void Move() {
		if (!fixed)
			this.x += this.vx;
			this.y += this.vy;
	}
	
	public double getDistanceSquared(Body b)
	{
		return (x-b.x)*(x-b.x) + (y-b.y)*(y-b.y);
	}
}
