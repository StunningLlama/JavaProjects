package mandelbrot;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

class Cell
{
	double s_zoom;
	double s_centerx;
	double s_centery;
	double s_julia_zoom;
	double s_julia_centerx;
	double s_julia_centery;
	
	Mandelbrot m;
	BufferedImage i;
	int x;
	int y;
	int xlen;
	int ylen;
	int mode;
	
	public Cell(Mandelbrot mc, int xc, int yc, int xlenc, int ylenc, int modec)
	{
		m = mc;
		i = (BufferedImage) mc.createImage(xlenc, ylenc);
		x = xc;
		y = yc;
		xlen = xlenc;
		ylen = ylenc;
		mode = modec;
		this.update();
	}
	
	public void render()
	{
		this.update();
		double scale;
		double xoffset;
		double yoffset;
		if (m.JuliaSet)
		{
			scale = s_julia_zoom/m.julia_zoom;
			xoffset = (s_julia_centerx - m.julia_centerx) / (m.julia_zoom / 500.0);
			yoffset = (s_julia_centery - m.julia_centery) / (m.julia_zoom / 500.0);
		}
		else
		{
			scale = m.render_ref_zoom/m.zoom;
			xoffset = (m.render_ref_centerx-m.centerx) / (m.zoom / 500.0);
			yoffset = (m.render_ref_centery-m.centery) / (m.zoom / 500.0);
		}
		Graphics ga = m.gphx.getGraphics();
		for (int ix = x * xlen; ix < x * xlen + xlen; ix ++)
		{
			for (int iy = y * ylen; iy < y * ylen + ylen; iy ++)
			{
				double d_ix = (double) ix;
				double d_iy = (double) iy;
				int px = (int) ((d_ix / scale - xoffset) + ((500.0 - (500.0 / scale)) / 2.0));
				int py = (int) ((d_iy / scale - yoffset) + ((500.0 - (500.0 / scale)) / 2.0));
				int rgb = Color.BLACK.getRGB();
				if (px >= 0 && py >= 0 && px < 500 && py < 500)
					rgb = m.draw_buf.getRGB(px, py);
				//ga.setColor(new Color((rgb >> 16) & 0x000000FF, (rgb >>8 ) & 0x000000FF, (rgb) & 0x000000FF));
				ga.setColor(new Color(rgb));
				ga.drawRect(ix, iy, 0, 0);
			}
		}
	}
	
	public void calc()
	{
		double tmp_s_centerx = m.ref_centerx;
		double tmp_s_centery = m.ref_centery;
		double tmp_s_zoom = m.ref_zoom;
		double tmp_s_julia_centerx = m.ref_julia_centerx;
		double tmp_s_julia_centery = m.ref_julia_centery;
		double tmp_s_julia_zoom = m.ref_julia_zoom;
		boolean color = m.options.colorchoice.getSelectedCheckbox().getLabel().equals("Linear Color");
		for (int ix = x * xlen; ix < x * xlen + xlen; ix ++)
		{
			for (int iy = y * ylen; iy < y * ylen + ylen; iy ++)
			{
				double px;
				double py;
				if (m.JuliaSet)
				{
					px = ((((ix - 250.0) / 250.0) * (tmp_s_julia_zoom / 2.0)) + tmp_s_julia_centerx) / 125.0;
					py = ((((iy - 250.0) / 250.0) * (tmp_s_julia_zoom / 2.0)) + tmp_s_julia_centery) / 125.0;
				}
				else
				{
					px = ((((ix - 250.0) / 250.0) * (tmp_s_zoom / 2.0)) + tmp_s_centerx) / 125.0;
					py = ((((iy - 250.0) / 250.0) * (tmp_s_zoom / 2.0)) + tmp_s_centery) / 125.0;
				}
				Complex.f(px, py, i, ix - x * xlen, iy - y * ylen, m.JuliaSet, color, mode, m);
			}
		}
		for (int ix = x * xlen; ix < x * xlen + xlen; ix ++)
		{
			for (int iy = y * ylen; iy < y * ylen + ylen; iy ++)
			{
				m.draw.setRGB(ix, iy, i.getRGB(ix - x * xlen, iy - y * ylen));
			}
		}
		this.update();
	}

	public void update2()
	{
		this.s_centerx = m.centerx;
		this.s_centery = m.centery;
		this.s_zoom = m.zoom;
		this.s_julia_centerx = m.julia_centerx;
		this.s_julia_centery = m.julia_centery;
		this.s_julia_zoom = m.julia_zoom;
	}
	public void update()
	{
		this.s_centerx = m.centerx;
		this.s_centery = m.centery;
		this.s_zoom = m.zoom;
		this.s_julia_centerx = m.julia_centerx;
		this.s_julia_centery = m.julia_centery;
		this.s_julia_zoom = m.julia_zoom;
	}
}