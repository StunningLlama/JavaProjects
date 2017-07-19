package mandelbrot;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

class OptionWindow extends Frame implements WindowListener
{
	private static final long serialVersionUID = 1L;
	Scrollbar iterations;
	Scrollbar colorscale;
	Button toggle;
	Label x;
	Label y;
	Label it_f;
	Label it_b;
	Label col_f;
	Label col_b;
	Mandelbrot set;
	Canvas canv;
	BufferedImage juliaprev;
	CheckboxGroup setchoice;
	CheckboxGroup colorchoice;
	//int mode;
	

	public OptionWindow(Mandelbrot instance)
	{
		this.setLayout(new BorderLayout());
		set = instance;
		Panel b = new Panel(new GridLayout(2, 2));
		Panel d = new Panel(new GridLayout(7, 2));
		x = new Label("X (Real) = 0.0");
		y = new Label("Y (Imag) = 0.0");
		it_f = new Label("Iterations: ");
		it_b = new Label("Iterations: 256");
		col_f = new Label("Color Scale: ");
		col_b = new Label("Color Scale: 100");
		iterations = new Scrollbar(Scrollbar.HORIZONTAL, 256, 100, 0, 4096 + 100);
		colorscale = new Scrollbar(Scrollbar.HORIZONTAL, 4, 5, 1, 20 + 5);
		canv = new Canvas();
		
		b.add(it_f); b.add(iterations); d.add(it_b); d.add(new Label(""));
		b.add(col_f); b.add(colorscale); d.add(col_b); d.add(new Label(""));
		d.add(x); d.add(new Label(""));
		d.add(y); d.add(new Label(""));

		setchoice = new CheckboxGroup();
		d.add(new Checkbox("Mandelbrot Set", setchoice, true));
		d.add(new Checkbox("Julia Set", setchoice, false));
		colorchoice = new CheckboxGroup();
		d.add(new Checkbox("Linear Color", colorchoice, true));
		d.add(new Checkbox("Log Color", colorchoice, false));
		
		toggle = new Button("Toggle Cursor");
		toggle.addActionListener(new ButtonListener(this, 1));
		d.add(toggle);
		toggle = new Button("Reset Position");
		toggle.addActionListener(new ButtonListener(this, 2));
		d.add(toggle);
		
		
		this.add(b, BorderLayout.NORTH);
		this.add(d, BorderLayout.SOUTH);
		this.add(canv, BorderLayout.CENTER);
		
		
		this.setTitle("Options");
		this.setSize(400, 400);
		this.setResizable(false);
		this.toFront();
		juliaprev = (BufferedImage)instance.createImage(100, 100);
		
		this.addWindowListener(this);
	}
	
	public void button(int id)
	{
		if (id == 0)
			this.set.JuliaSet = !this.set.JuliaSet;
		if (id == 1)
			this.set.showcursor = !this.set.showcursor;
		if (id == 2)
			if (this.set.JuliaSet)
			{
				this.set.julia_centerx = 0.0;
				this.set.julia_centery = -0.0;
				this.set.julia_zoom = 500.0;
			}
			else
			{
				this.set.centerx = 0.0;
				this.set.centery = -0.0;
				this.set.zoom = 500.0;
			}
	}
	
	public void update(Graphics rg)
	{
		Graphics jg = juliaprev.getGraphics();
		for (int x = 0; x < juliaprev.getWidth(); x++)
		{
			for (int y = 0; y < juliaprev.getHeight(); y++)
			{
				this.f((x - juliaprev.getWidth() / 2.0) / (juliaprev.getWidth() / 4.0), (y - juliaprev.getHeight() / 2.0) / (juliaprev.getHeight() / 4.0), jg, x, y);
			}
		}
		//this.canv.getGraphics().drawImage(this.juliaprev, 0, 0, this);
		Graphics cg = this.canv.getGraphics();
		cg.drawImage(this.juliaprev, (this.getWidth() - this.juliaprev.getWidth()) / 2 - this.getInsets().left, 40, this);
		cg.setColor(Color.BLACK);
		cg.drawString("Julia set preview", (this.getWidth() - this.juliaprev.getWidth()) / 2 - this.getInsets().left, 30);
		//rg.drawImage(this.juliaprev, canv.getX(), canv.getY(), this);
	}
	
	void f(double xc, double yc, Graphics g, int px, int py)
	{
		int iteration = 0;
		int max_iteration = 100;//this.options.iterations.getValue();
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
		c = new Complex(set.centerx / 125.0, set.centery / 125.0);
		z = new Complex(xc, yc);
		while ( (z.x*z.x + z.y*z.y) < 4.0 && iteration < max_iteration )
		{
			z = (Complex.Add(Complex.Square(z, set.mode), c));
			iteration ++;
		}
		if (iteration <= 1)
			g.setColor(new Color(191, 0, 0));
		else if (iteration < max_iteration)
			g.setColor(set.colors[(((iteration - 2) * set.options.colorscale.getValue()) % 192)]);
		else
			g.setColor(Color.BLACK);
		//g.drawRect(px, py, 0, 0);
		juliaprev.setRGB(px, py, g.getColor().getRGB());
	}

	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}

	public void windowClosing(WindowEvent arg0) {
		this.setVisible(false);
	}

}