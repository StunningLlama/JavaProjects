
public class Coil extends ParametricEquation {
	public Coil(Camera cami, double points)
	{
		super(cami);
		double interval = getDomain()/points;
		centerpoints = new Vec3[turns];
		swapindexes = new int[turns*2-2];
		swappointers = new int[turns*2-2];
		totalswappoints = turns*2-2;
		for (int i = 0; i < turns; i++)
		{
			centerpoints[i] = new Vec3(0, spacing*(i+0.5), 0);
		}
		for (int i = 0; i < turns; i++)
		{
			swapindexes[i] = (int)((i+1.0)*2.0*Math.PI / interval);
			swappointers[i] = i;
		}
	}

	int turns = 8;
	double spacing = 0.1;
	
	double A = turns*2*Math.PI;
	double B = 0.25;
	double C = spacing*turns;
	double D = 0.25;
	
	public Vec3 func(double t)
	{
		if (t < A)
			return new Vec3(Math.sin(t),	t*spacing/(2*Math.PI),		Math.cos(t));
		else if (t < A+B)
			return new Vec3(0,				C,							1 + (t - A));
		else if (t < A+B+C)
			return new Vec3(0,				C - (t - (A+B)),			1.25);
		else
			return new Vec3(0,				0,							1.25 - (t - (A+B+C)));
	}

	@Override
	public double getDomain() {
		return A+B+C+D;
	}
}
