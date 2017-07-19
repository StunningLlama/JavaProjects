package mandelbrot;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;


public class Mandelbrot extends Frame implements Runnable, MouseListener, MouseWheelListener, MouseMotionListener, WindowListener, KeyListener
{
	private static final long serialVersionUID = 1L;
	BufferedImage gphx;
	BufferedImage draw_buf;
	BufferedImage draw;
	Color[] colors;
	int[][] gearicon = {
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
			{0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0},
			{0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
			{0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0},
			{0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0},
			{0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0},
			{0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0},
			{0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
			{0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0},
			{0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
	};
	double zoom;
	double centerx;
	double centery;
	double ref_zoom;
	double ref_centerx;
	double ref_centery;
	double render_ref_zoom;
	double render_ref_centerx;
	double render_ref_centery;
	
	
	double julia_zoom;
	double julia_centerx;
	double julia_centery;
	double ref_julia_zoom;
	double ref_julia_centerx;
	double ref_julia_centery;
	double render_ref_julia_zoom;
	double render_ref_julia_centerx;
	double render_ref_julia_centery;
	
	boolean pressed;
	boolean hover;
	//boolean maycalc;
	Cell[][] cells;
	int xoffset;
	int yoffset;
	OptionWindow options;
	boolean showcursor;
	boolean JuliaSet;
	int mode;
	
	public Mandelbrot()
	{
		mode = 4;
		cells = new Cell[5][5];
		pressed = false;
		hover = false;
		showcursor = true;
		//maycalc = true;
		zoom = 500.0;
		centerx = 0.0;
		centery = -0.0;
		julia_zoom = 500.0;
		julia_centerx = 0.0;
		julia_centery = -0.0;
		centerxstate = 0.0;
		centerystate = -0.0;
		mouseX = 0;
		mouseY = 0;
		this.setResizable(false);
		//this.setResizable(false);
		this.setTitle("Mandelbrot Viewer");
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		xoffset = (this.getInsets().left + this.getInsets().right);
		yoffset = (this.getInsets().top + this.getInsets().bottom);
		this.setSize(xoffset + 500, yoffset + 500);
		for (int x = 0; x < 5; x++)
		{
			for (int y = 0; y < 5; y++)
			{
				cells[x][y] = new Cell(this, x, y, 100, 100, mode);
			}
		}
		draw = (BufferedImage) this.createImage(500, 500);
		draw_buf = (BufferedImage) this.createImage(500, 500);
		gphx = (BufferedImage) this.createImage(500, 500);
		colors = new Color[192];
		int ind = 0;
		for (int i = 0; i < 256; i += 8)
		{
			colors[ind] = new Color(255, i, 0);
			ind++;
		}
		for (int i = 255; i >= 0; i -= 8)
		{
			colors[ind] = new Color(i, 255, 0);
			ind++;
		}
		for (int i = 0; i < 256; i += 8)
		{
			colors[ind] = new Color(0, 255, i);
			ind++;
		}
		for (int i = 255; i >= 0; i -= 8)
		{
			colors[ind] = new Color(0, i, 255);
			ind++;
		}
		for (int i = 0; i < 256; i += 8)
		{
			colors[ind] = new Color(i, 0, 255);
			ind++;
		}
		for (int i = 255; i >= 0; i -= 8)
		{
			colors[ind] = new Color(255, 0, i);
			ind++;
		}
		this.addMouseListener(this);
		this.addMouseWheelListener(this);
		this.addMouseMotionListener(this);
		this.addWindowListener(this);
		this.addKeyListener(this);
		
		options = new OptionWindow(this);
		Graphics g = draw.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 500, 500);
		g.setColor(Color.CYAN);
		g.setFont(new Font("Arial", Font.BOLD, 48));
		centerString(g, g.getFontMetrics(), "LOADING", 250);
		
		MandelbrotCalc Calc = new MandelbrotCalc(this);
		Calc.start();
	}
	
	public void centerString(Graphics g, FontMetrics fm, String str, int ypos) {
		g.drawString(str, (this.getSize().width-fm.stringWidth(str))/2, ypos);
	}
	
	public static void main(String[] args)
	{
		Mandelbrot ins = new Mandelbrot();
		ins.run();
	}
	
	@Override
	public void update(Graphics rg)
	{
		//long time = System.currentTimeMillis();
		Graphics ga = gphx.getGraphics();
		ga.setColor(Color.CYAN);
		ga.fillRect(0, 0, 500, 500);
		
		for (int x = 0; x < 5; x++)
		{
			for (int y = 0; y < 5; y++)
			{
				cells[x][y].render();
			}
		}
		if (showcursor)
		{
			ga.setColor(Color.WHITE);
			ga.fillRect(249 - 1, 249 - 20, 2, 15);
			ga.fillRect(249 - 1, 249 + 5, 2, 15);
			ga.fillRect(249 - 20, 249 - 1, 15, 2);
			ga.fillRect(249 + 5, 249 - 1, 15, 2);
		}
		
		ga.setColor(Color.LIGHT_GRAY);
		if (hover)
			ga.setColor(Color.WHITE);
		ga.fillRect(500 - 12, 0, 12, 12);
		ga.setColor(Color.BLACK);
		for (int x = 0; x < 12; x++)
			for (int y = 0; y < 12; y++)
				if (this.gearicon[x][y] == 1)
					ga.drawRect(x + (500 - 12), y, 0, 0);
		//System.out.println(System.currentTimeMillis() - time);
		rg.drawImage(gphx, this.getInsets().left, this.getInsets().top, this);
	}
	
	//static boolean get(double x, double y)
	//{
	//	return (f(new Complex(x, y), new Complex(0, 0), 0) == 100);
	//}
	

	public void run()
	{
		while (true)
		{
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {}
			this.JuliaSet = this.options.setchoice.getSelectedCheckbox().getLabel().equals("Julia Set");
			this.repaint();
			OptionWindow opt = this.options;
			opt.col_b.setText("Color Scale: " + opt.colorscale.getValue());
			opt.it_b.setText("Iterations: " + opt.iterations.getValue());
			if (this.JuliaSet)
			{
				opt.x.setText("X (Real) = " + this.julia_centerx / 125.0);
				opt.y.setText("Y (Imag) = " + -(this.julia_centery / 125.0));
			}
			else
			{
				opt.x.setText("X (Real) = " + this.centerx / 125.0);
				opt.y.setText("Y (Imag) = " + -(this.centery / 125.0));
			}
		}
	}


	int mouseX;
	int mouseY;
	double centerxstate;
	double centerystate;
	

	public void mouseMoved(MouseEvent event) {
		if (!pressed)
		{
			mouseX = event.getX();
			mouseY = event.getY();
		}
		if (event.getX() - this.getInsets().left > 500 - 12 && event.getY() - this.getInsets().top < 12)
		{
			this.hover = true;
		}
		else
		{
			this.hover = false;
		}
	}
	
	public void mousePressed(MouseEvent event) {
		if (this.hover)
		{
			this.options.setVisible(true);
		}
		pressed = true;
		if (this.JuliaSet)
		{
			this.centerxstate = this.julia_centerx;
			this.centerystate = this.julia_centery;
		}
		else
		{
			this.centerxstate = this.centerx;
			this.centerystate = this.centery;
		}
	}

	public void mouseReleased(MouseEvent event) {
		pressed = false;
		if (this.JuliaSet)
		{
			this.centerxstate = this.julia_centerx;
			this.centerystate = this.julia_centery;
		}
		else
		{
			this.centerxstate = this.centerx;
			this.centerystate = this.centery;
		}
		
	}

	public void mouseWheelMoved(MouseWheelEvent event) {
		if(event.getWheelRotation() == -1)
		{
			if (this.JuliaSet)
				this.julia_zoom = this.julia_zoom / 1.1;
			else
				this.zoom = this.zoom / 1.1;
		}
		if(event.getWheelRotation() == 1)
		{
			if (this.JuliaSet)
				this.julia_zoom = this.julia_zoom * 1.1;
			else
				this.zoom = this.zoom * 1.1;
		}
		//maycalc = true;
	}

	public void mouseDragged(MouseEvent event) {
		if (this.JuliaSet)
		{
			this.julia_centerx = this.centerxstate - (event.getX() - mouseX) * (julia_zoom / 500.0);
			this.julia_centery = this.centerystate - (event.getY() - mouseY) * (julia_zoom / 500.0);
		}
		else
		{
			this.centerx = this.centerxstate - (event.getX() - mouseX) * (zoom / 500.0);
			this.centery = this.centerystate - (event.getY() - mouseY) * (zoom / 500.0);
		}
		//maycalc = true;
	}

	public void keyPressed(KeyEvent event)
	{
		int key = event.getKeyCode();
		if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W)
			this.centery -= (zoom / 50.0);
		else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S)
			this.centery += (zoom / 50.0);
		else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A)
			this.centerx -= (zoom / 50.0);
		else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D)
			this.centerx += (zoom / 50.0);
		
		if (key == KeyEvent.VK_PAGE_UP || key == KeyEvent.VK_EQUALS)
			this.zoom /= 1.1;
		else if (key == KeyEvent.VK_PAGE_DOWN || key == KeyEvent.VK_MINUS)
			this.zoom *= 1.1;
	}
	
	public void windowClosing(WindowEvent arg0) {
		System.exit(0);
	}

	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
}