package befunge;

public class Pointer {
	Befunge interpreter;
	public Pointer(Befunge instance, int ix, int iy, int idx, int idy)
	{
		interpreter = instance;
		x = ix;
		y = iy;
		dx = idx;
		dy = idy;
	}
	public Pointer(Befunge instance, int ix, int iy)
	{
		interpreter = instance;
		x = ix;
		y = iy;
		dx = 1;
		dy = 0;
	}
	public Pointer(Befunge instance)
	{
		interpreter = instance;
		x = 0;
		y = 0;
		dx = 1;
		dy = 0;
	}
	boolean strmode;
	int dx;
	int dy;
	int x;
	int y;
	public void move()
	{
		
	}
	public int instruction(int x, int y)
	{
		return 0;
	}
}
