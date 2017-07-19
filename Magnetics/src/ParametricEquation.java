
public abstract class ParametricEquation
{
	Camera cam;
	Vec3[] centerpoints;
	int[] swapindexes;
	int[] swappointers;
	int totalswappoints;
	
	public ParametricEquation (Camera cami)
	{
		cam = cami;
	}

	public abstract Vec3 func(double t);
	
	public abstract double getDomain();
}
