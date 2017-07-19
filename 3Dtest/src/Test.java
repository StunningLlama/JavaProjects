import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Test extends JFrame implements MouseListener, MouseMotionListener, Runnable, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8431266624931397148L;
	Display canvas;
	Renderer render;
	public Test()
	{
		try {
			r = new Robot();
		} catch (AWTException e) {}
		render = new Renderer();
		render.createArray(24);
		setSize(1200, 700);
		setVisible(true);
		canvas = new Display(this);
		add(canvas);
		setPreferredSize(new Dimension(1200, 700));
		pack();
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addKeyListener(this);
		this.addKeyListener(this);
		
		render.addLine(new Vector3F(-1, 1, 2), new Vector3F(1, 1, 2));
		render.addLine(new Vector3F(-1, -1, 2), new Vector3F(1, -1, 2));
		render.addLine(new Vector3F(1, 1, 2), new Vector3F(1, -1, 2));
		render.addLine(new Vector3F(-1, -1, 2), new Vector3F(-1, 1, 2));
	
		render.addLine(new Vector3F(-1, 1, 2), new Vector3F(-1, 1, 3));
		render.addLine(new Vector3F(1, -1, 2), new Vector3F(1, -1, 3)); 
		render.addLine(new Vector3F(1, 1, 2), new Vector3F(1, 1, 3));
		render.addLine(new Vector3F(-1, -1, 2), new Vector3F(-1, -1, 3));
	
		render.addLine(new Vector3F(-1, 1, 3), new Vector3F(1, 1, 3));
		render.addLine(new Vector3F(-1, -1, 3), new Vector3F(1, -1, 3));
		render.addLine(new Vector3F(1, 1, 3), new Vector3F(1, -1, 3));
		render.addLine(new Vector3F(-1, -1, 3), new Vector3F(-1, 1, 3));
		
		
		
		
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
		Test instance = new Test();
		instance.run();
	}
	public void run(){
		while (true)
		{
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {}
			if (W_Key) {
				render.camerapos.z += 0.017;
			}
			if (S_Key)
				render.camerapos.z -= 0.017;
			if (A_Key)
				render.camerapos.x -= 0.017;
			if (D_Key)
				render.camerapos.x += 0.017;
			canvas.repaint();
			System.out.println(render.camerarot.y+" "+render.camerarot.z);
		}
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	int mx = 100;
	int my = 100;
	Robot r;
	@Override
	public void mouseMoved(MouseEvent event) {
		render.camerarot.y += (event.getX() - mx)/20.0;
		render.camerarot.z -= (event.getY() - my)/20.0;
		mx = event.getX();
		my = event.getY();
		//r.mouseMove(mx, my);
	}
	private boolean W_Key = false;
	private boolean S_Key = false;
	private boolean A_Key = false;
	private boolean D_Key = false;
	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_W) {
			W_Key = true;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_S)
			S_Key = true;
		if (arg0.getKeyCode() == KeyEvent.VK_A)
			A_Key = true;
		if (arg0.getKeyCode() == KeyEvent.VK_D)
			D_Key = true;
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_W)
			W_Key = false;
		if (arg0.getKeyCode() == KeyEvent.VK_S)
			S_Key = false;
		if (arg0.getKeyCode() == KeyEvent.VK_A)
			A_Key = false;
		if (arg0.getKeyCode() == KeyEvent.VK_D)
			D_Key = false;
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}

class Display extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4952519587073510725L;
	Image display;
	Test parent;
	public Display(Test k)
	{
		parent = k;
		display = k.createImage(1200, 700);
	}
	@Override
	public void paintComponent(Graphics realgphx)
	{
		Graphics g = display.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 1200, 700);
		parent.render.render(g);
		realgphx.drawImage(display, 0, 0, parent);
	}
}
