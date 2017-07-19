import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;


public class Grid extends Frame implements Runnable, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
	private static final long serialVersionUID = 1L;
	BufferedImage im;
	int[][] arr;
	int zoom;
	double centerx;
	double centery;
	int mouseX;
	int mouseY;
	double centerxstate;
	double centerystate;
	boolean pressed;
	static Scrollbar s;
	public static void main(String[] args)
	{
		Grid g = new Grid();
		g.run();
	}
	
	public Grid()
	{
		zoom = 6;
		centerx = 0.0;
		centery = -0.0;
		centerxstate = 0.0;
		centerystate = -0.0;
		mouseX = 0;
		mouseY = 0;
		Panel p = new Panel(new GridLayout(1,2));
		s = new Scrollbar(Scrollbar.HORIZONTAL, 4, 10, 1, 300);
		p.add(s);
		this.setLayout(new BorderLayout());
		this.add(p, BorderLayout.SOUTH);
		this.setSize(600, 600);
		this.setVisible(true);
		this.im = (BufferedImage) this.createImage(600, 600);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addKeyListener(this);
		this.addWindowListener(new WindowListener()
		{
			@Override
			public void windowActivated(WindowEvent arg0) {}
			@Override
			public void windowClosed(WindowEvent arg0) {}

			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {}
			@Override
			public void windowDeiconified(WindowEvent arg0) {}
			@Override
			public void windowIconified(WindowEvent arg0) {}
			@Override
			public void windowOpened(WindowEvent arg0) {}
		});
	}

	@Override
	public void update(Graphics g)
	{
		int val = s.getValue()*2+1;
		arr = new int[val][val];
		Graphics c = im.getGraphics();
		int ptrx = val/2;
		int ptry = val/2-1;
		Vel ptrv = new Vel(0, 1, 1, 0, false);
		for (int i = 1; i <= (val*val); i++)
		{
			ptrx += ptrv.x;
			ptry += ptrv.y;
			arr[ptrx][ptry] = i;
			ptrv = Vel.getNext(ptrv);
		}
		c.setColor(Color.WHITE);
		c.fillRect(0, 0, im.getWidth(), im.getHeight());
		for (int xa = 0; xa < val; xa+=1)
		{

			for (int ya = 0; ya < val; ya+=1)
			{
				if(zoom==6)
				{
					if ((xa*64-4-(int)centerx) < -100 || (xa*64-4-(int)centerx) > 600 || (ya*64-4-(int)centery) < -100 || (ya*64-4-(int)centery) > 600)
						continue;
				String num = "";
				try {
				num = String.valueOf(arr[xa][ya]);

				c.setColor(Color.RED);
				if (arr[xa][ya]==1)
					c.setColor(Color.CYAN);
				if (MathUtil.isPrime(arr[xa][ya]))
					c.fillRect(xa*64-4-(int)centerx, ya*64-18-(int)centery, 64, 64);
				c.setColor(Color.BLACK);
				c.drawString(num, xa*64-(int)centerx, ya*64-(int)centery);
				}catch(Exception e){}
				}
				if(zoom==5)
				{
					if ((xa*32-2-(int)centerx) < -100 || (xa*32-2-(int)centerx) > 600 || (ya*32-2-(int)centery) < -100 || (ya*32-2-(int)centery) > 600)
						continue;
				String num = "";
				try {
				num = String.valueOf(arr[xa][ya]);

				c.setColor(Color.RED);
				if (arr[xa][ya]==1)
					c.setColor(Color.CYAN);
				if (MathUtil.isPrime(arr[xa][ya]))
					c.fillRect(xa*32-2-(int)centerx, ya*32-14-(int)centery, 32, 32);
				c.setColor(Color.BLACK);
				c.drawString(num, xa*32-(int)centerx, ya*32-(int)centery);
				}catch(Exception e){}
				}
				if(zoom<5)
				{
					int pow = (int)Math.pow(2, zoom);
					if ((xa*pow-(int)centerx) < -100 || (xa*pow-(int)centerx) > 600 || (ya*pow-(int)centery) < -100 || (ya*pow-(int)centery) > 600)
						continue;
					try {
						c.setColor(Color.RED);
						if (arr[xa][ya]==1)
							c.setColor(Color.CYAN);
						if (MathUtil.isPrime(arr[xa][ya]))
							c.fillRect(xa*pow-(int)centerx, ya*pow-(int)centery, pow, pow);
						c.setColor(Color.BLACK);
						}catch(Exception e){}
				}
			}
		}
		
		g.drawImage(im, this.getInsets().left, this.getInsets().top, this);
	}
	
	@Override
	public void run() {
		while(true)
		{
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.repaint();
		}
	}
	
	public void mouseMoved(MouseEvent event) {
		if (!pressed)
		{
			mouseX = event.getX();
			mouseY = event.getY();
		}
	}
	
	public void mousePressed(MouseEvent event) {
		pressed = true;
		{
			this.centerxstate = this.centerx;
			this.centerystate = this.centery;
		}
	}

	public void mouseReleased(MouseEvent event) {
		pressed = false;
		{
			this.centerxstate = this.centerx;
			this.centerystate = this.centery;
		}
		
	}


	public void mouseDragged(MouseEvent event) {
		{
			this.centerx = this.centerxstate - (event.getX() - mouseX);
			this.centery = this.centerystate - (event.getY() - mouseY);
		}
		//maycalc = true;
	}

	public void mouseWheelMoved(MouseWheelEvent event) {
		int zoomlvl = event.getWheelRotation() * -1;
		if ((this.zoom >= 6 && zoomlvl > 0) || (this.zoom < 1 && zoomlvl < 0))
			return;
		this.zoom+=zoomlvl;
		
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent event) {
			int key = event.getKeyCode();

			int zoomlvl = 0;
			if (key == KeyEvent.VK_PAGE_UP || key == KeyEvent.VK_EQUALS)
				zoomlvl = 1;
			else if (key == KeyEvent.VK_PAGE_DOWN || key == KeyEvent.VK_MINUS)
				zoomlvl = -1;
			if ((this.zoom >= 6 && zoomlvl > 0) || (this.zoom < 1 && zoomlvl < 0))
				return;
			this.zoom+=zoomlvl;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
}

class Vel
{
	public int x;
	public int y;
	public int it;
	public int itmax;
	public boolean next;
	public Vel(int ix, int iy, int iit, int iitmax, boolean inext)
	{
		x = ix;
		y = iy;
		it = iit;
		itmax = iitmax;
		next = inext;
	}
	
	public static Vel getNext(Vel v)
	{
		if (v.it > 0) return new Vel(v.x, v.y, v.it - 1, v.itmax, v.next);
		if (v.next)
		{
		if (v.x == 1 && v.y == 0) return new Vel(0, 1, v.itmax + 1, v.itmax + 1, !v.next);
		if (v.x == 0 && v.y == 1) return new Vel(-1, 0, v.itmax + 1, v.itmax + 1, !v.next);
		if (v.x == -1 && v.y == 0) return new Vel(0, -1, v.itmax + 1, v.itmax + 1, !v.next);
		if (v.x == 0 && v.y == -1) return new Vel(1, 0, v.itmax + 1, v.itmax + 1, !v.next);
		}
		else 
		{
		if (v.x == 1 && v.y == 0) return new Vel(0, 1, v.itmax, v.itmax, !v.next);
		if (v.x == 0 && v.y == 1) return new Vel(-1, 0, v.itmax, v.itmax, !v.next);
		if (v.x == -1 && v.y == 0) return new Vel(0, -1, v.itmax, v.itmax, !v.next);
		if (v.x == 0 && v.y == -1) return new Vel(1, 0, v.itmax, v.itmax, !v.next);
		}
		return null;
	}
}
class MathUtil
{
	public static boolean isPrime(long a)
	{
		if (a<3) return true;
		for (int i = 2; i <= (int)Math.ceil(Math.sqrt((double)a)); i++)
		{
			if (i > 3 && ((i % 3 == 0) || (i % 2 == 0)))
				continue;
			if (a%i==0) return false;
		}
		return true;
	}
}