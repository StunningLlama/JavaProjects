package geometry.shape2D;

public class RegularPolygon extends Polygon {

	double sidelen;
	double sidecount;
	
	public double getArea()
	{
		return (0.25 * sidecount * sidelen * sidelen * (1 / Math.tan(Math.PI / sidecount)));
	}
	
}
