import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Scrollbar;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Recorder
{

	public static void main(String[] args)
	{
		(new Screen(new Base())).run();
	}
}
class Base extends Frame
{
	private static final long serialVersionUID = 1L;
	int wait;
	Scrollbar b;
	public Base()
	{
		this.setLayout(new BorderLayout());
		b = new Scrollbar(Scrollbar.HORIZONTAL, 60, 10, 3, 120);
		this.setSize(200, 200);
		this.add(b, BorderLayout.SOUTH);
		this.setVisible(true);
	}
}
 class Screen extends JPanel implements Runnable, ComponentListener {
	private static final long serialVersionUID = 2L;
	BufferedImage i;
	Robot r;
	int fps = 0;
	int xsize;
	int ysize;
	int xoffset;
	int yoffset;
	int screenwidth;
	int screenheight;
	Base opt;
	public Screen(Base b)
	{
		opt = b;
		xsize = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth()/2;
		ysize = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight()/2;
		screenwidth = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
		screenheight = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
		this.setVisible(true);
		this.setSize(xsize+this.getInsets().left+this.getInsets().right, ysize+this.getInsets().top+this.getInsets().bottom);
		xoffset = this.getInsets().left;
		yoffset = this.getInsets().top;
		i = (BufferedImage) this.createImage(xsize, ysize);
		try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		this.addComponentListener(this);
	}
	public void run()
	{
		long next = System.currentTimeMillis() + 1000;
		while(true)
		{
			if (next < System.currentTimeMillis())
			{
				System.out.println("FPS: " + fps);
				fps = 0;
				next = System.currentTimeMillis() + 1000;
			}
			try {
				Thread.sleep((int)Math.ceil(1000.0/opt.b.getValue()));
			} catch (InterruptedException e) {}
			this.repaint();
		}
	}
	public void paintComponent(Graphics realg)
	{
		fps++;
		BufferedImage b = r.createScreenCapture(new Rectangle(0, 0, screenwidth, screenheight));
		realg.drawImage(b.getScaledInstance(xsize, ysize, Image.SCALE_DEFAULT), xoffset, yoffset, this);
	}
	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void componentResized(ComponentEvent event) {
		xsize = this.getWidth()-(this.getInsets().left+this.getInsets().right);
		ysize = this.getHeight()-(this.getInsets().top+this.getInsets().bottom);
		screenwidth = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
		screenheight = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
		xoffset = this.getInsets().left;
		yoffset = this.getInsets().top;
		i = (BufferedImage) this.createImage(xsize, ysize);
	}
	@Override
	public void componentShown(ComponentEvent arg0) {
	}
}
