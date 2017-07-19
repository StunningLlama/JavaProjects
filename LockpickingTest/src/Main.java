import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main extends JFrame implements MouseListener, Runnable, KeyListener {
	private static final long serialVersionUID = 1L;
	Display canvas;
	public double pos = 0;
	public double vel = 0;
	public boolean render = true;
	public Main()
	{
		setSize(500, 500);
		setVisible(true);
		canvas = new Display(this);
		add(canvas);
		Insets i = this.getInsets();
		setPreferredSize(new Dimension(600, 600));
		pack();
		canvas.addMouseListener(this);
		this.addKeyListener(this);
		canvas.addKeyListener(this);
		
	}
	@Override public void mouseClicked(MouseEvent arg0) {}
	@Override public void mouseEntered(MouseEvent arg0) {}
	@Override public void mouseExited(MouseEvent arg0) {}
	@Override public void mouseReleased(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent event) {
		
	}
	
	public static void main(String[] args)
	{
		Main instance = new Main();
		instance.run();
	}
	public void run(){
		while (true)
		{
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {}
			canvas.repaint();
			if (keyheld)
				vel += 0.05;
			else 
				vel -= 0.02;
			if (vel > 0.5) vel = 0.5;
			pos += vel;
			if (pos < -8.0){
				pos = -8.0;
			vel = 0.0;
			}
			if (render == false)
				pos = 6.0;
			if (pos > 5)
				render = false;
		}
	}
	boolean keyheld = false;
	@Override
	public void keyPressed(KeyEvent arg0) {
		keyheld = true;
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		keyheld = false;
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}

class Display extends JPanel
{
	Image display;
	Main parent;
	public Display(Main k)
	{
		parent = k;
		display = k.createImage(600, 600);
	}
	@Override
	public void paintComponent(Graphics realgphx)
	{
		Graphics2D g = (Graphics2D)display.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 599, 599);
		g.setColor(Color.RED);
		g.drawLine(60-(int)(30*Math.sin(-5.0/10)), 60-(int)(30*Math.cos(-5.0/10)), 60+(int)(30*Math.sin(-5.0/10)), 60+(int)(30*Math.cos(-5.0/10)));
		g.setColor(Color.BLUE);
		g.drawLine(60-(int)(30*Math.sin(5.0/10)), 60-(int)(30*Math.cos(5.0/10)), 60+(int)(30*Math.sin(5.0/10)), 60+(int)(30*Math.cos(5.0/10)));
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(2));
		if (parent.render)
			g.drawLine(60-(int)(30*Math.sin(-parent.pos/10)), 60-(int)(30*Math.cos(-parent.pos/10)), 60+(int)(30*Math.sin(-parent.pos/10)), 60+(int)(30*Math.cos(-parent.pos/10)));
		else
			g.drawString("BROKEN", 30, 60);
		if (parent.pos < -5.0)
			g.drawString("TOO LOOSE", 30, 60);
		realgphx.drawImage(display, 0, 0, parent);
	}
}