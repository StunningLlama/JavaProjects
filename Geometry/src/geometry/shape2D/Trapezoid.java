package geometry.shape2D;

public class Trapezoid extends Polygon {

	double topbase;
	double bottombase;
	double height;
	
	public double getArea()
	{
		return ((topbase + bottombase) * height / 2);
	}
}
