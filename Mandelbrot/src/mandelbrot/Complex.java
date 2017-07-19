package mandelbrot;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

class Complex
{
	public Complex clone()
	{
		return new Complex(this.x, this.y);
	}
	double x;
	double y;
	public Complex(double xi, double yi)
	{
		x = xi;
		y = yi;
	}
	public static Complex Add(Complex a, Complex b)
	{
		return new Complex(a.x + b.x, a.y + b.y);
	}
	//public static Complex Mul(Complex a, Complex b)
	//{
	//	return new Complex(a.x * b.x - a.y * b.y, a.x * b.y + a.y * b.x);
	//}
	public static Complex Square(Complex a, int mode)
	{
		return new Complex(a.x*a.x + a.y*a.y, a.x*a.y* 2.0);
		/*switch(mode)
		{
		case 0: return new Complex(a.x*a.x - a.y*a.y, a.x*a.y* 2.0); //MANDELBROT
		case 1: return new Complex(a.x*a.x - a.y*a.y, Math.abs(a.x*a.y* 2.0)); //BURNING SHIP
		case 2: return new Complex(a.x*a.x - a.y*a.y, -(a.x*a.y* 2.0)); //MANDELBAR
		case 3: return new Complex(Math.abs(a.x*a.x - a.y*a.y), a.x*a.y* 2.0); //CELTIC
		case 4: return new Complex(Math.abs(a.x*a.x - a.y*a.y), Math.abs(a.x*a.y* 2.0)); //BUFFALO
		default: return new Complex(0.0, 0.0);
		}*/
		
	}
	
	public static void f(double xc, double yc, BufferedImage i, int px, int py, boolean julia, boolean color, int mode, Mandelbrot m)
	{
		int iteration = 0;
		int max_iteration = m.options.iterations.getValue();
		/*************************************
		double x0 = xc;
		double y0 = yc;
		double x = 0.0;
		double y = 0.0;
		double q = (xc-0.25)*(xc-0.25)+(yc*yc);
		if (q*(q+(xc-0.25)) < 0.25*yc*yc)
		{
			iteration = max_iteration;
		}
		else
		{
			while ( x*x + y*y < 2*2 && iteration < max_iteration )
			{
				double xtemp = x*x - y*y + x0;
				double ytemp = 2*x*y + y0;
				if (x == xtemp && y == ytemp)
				{
					iteration = max_iteration;
					break;
				}
				x = xtemp;
				y = ytemp;
				iteration = iteration + 1;
			}
		}
		************************************/
		Complex z;
		Complex c;
		if (julia)
		{
			c = new Complex(m.centerx / 125.0, m.centery / 125.0);
			z = new Complex(xc, yc);
		}
		else
		{
			z = new Complex(0.0, 0.0);
			c = new Complex(xc, yc);
		}
		if (!julia)
		{
			double q = (xc-0.25)*(xc-0.25)+(yc*yc);
			if (q*(q+(xc-0.25)) < 0.25*yc*yc)
			{
			//	iteration = max_iteration;
			}
		}
		while ( (z.x*z.x + z.y*z.y) < 4.0 && iteration < max_iteration )
		{
			z = (Complex.Add(Complex.Square(z, mode), c));
			iteration ++;
		}
		Graphics g = i.getGraphics();
		if (iteration <= 1)
			g.setColor(new Color(191, 0, 0));
		else if (iteration < max_iteration)
			if (color)
				g.setColor(m.colors[(((iteration - 2) * m.options.colorscale.getValue()) % 192)]);
			else
				g.setColor(m.colors[(((int) ((Math.sqrt((iteration - 2) * m.options.colorscale.getValue()+Math.E * 10.0)) * 100.0) - 128) % 192)]);
		else
			g.setColor(Color.BLACK);
		//g.drawRect(px, py, 0, 0);
		i.setRGB(px, py, g.getColor().getRGB());
	}
}