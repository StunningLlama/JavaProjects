package mandelbrot;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

class MandelbrotCalc extends Thread
{
	Mandelbrot instance;
	public MandelbrotCalc(Mandelbrot a)
	{
		instance = a;
	}
	
	@Override
	public void run()
	{
		while (true)
		{
			//if (instance.maycalc)
			{


				instance.ref_zoom = instance.zoom;
				instance.ref_centerx = instance.centerx;
				instance.ref_centery = instance.centery;
				instance.ref_julia_zoom = instance.julia_zoom;
				instance.ref_julia_centerx = instance.julia_centerx;
				instance.ref_julia_centery = instance.julia_centery;
				List<Point> l = new ArrayList<Point>();
				for (int x = 0; x < 5; x++)
				{
					for (int y = 0; y < 5; y++)
					{
						l.add(new Point(x, y));
					}
				}
				for (int i = 24; i >= 0; i--)
				{
					int rand = (int) Math.floor(Math.random() * (i + 1));
					Point p = (Point) l.get(rand).clone();
					l.remove(rand);
					//instance.updated[p.x][p.y] = false;
					instance.cells[p.x][p.y].calc();
					//try {
					//	Thread.sleep(100);
					//} catch (InterruptedException e) {}
				}
				instance.render_ref_zoom = instance.zoom;
				instance.render_ref_centerx = instance.centerx;
				instance.render_ref_centery = instance.centery;
				instance.render_ref_julia_zoom = instance.julia_zoom;
				instance.render_ref_julia_centerx = instance.julia_centerx;
				instance.render_ref_julia_centery = instance.julia_centery;
				instance.draw_buf = new BufferedImage(instance.draw.getColorModel(), instance.draw.copyData(null), instance.draw.isAlphaPremultiplied(), null);
				//instance.maycalc = false;
			}
			try {
				for (int i = 0; i < 5; i++)
				{
					Thread.sleep(100);
					this.instance.options.repaint();
				}
			} catch (InterruptedException e) {}
		}
		//ga.drawImage(instance.draw, 0, 0, instance);
	}
}