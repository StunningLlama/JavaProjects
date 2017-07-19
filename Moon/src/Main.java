import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;


public class Main extends Frame implements Runnable, KeyListener {
	private static final long serialVersionUID = 1L;
	public static void main(String[] args)
	{
		Main instance = new Main();
		instance.run();
	}
	Image screen;
	BigDecimal phase;
	TextField input;
	Button AnimButton;
	MoonCanvas animation;
	static boolean pause = true;
	public Main() {
		phase = new BigDecimal(0);
		animation = new MoonCanvas(this);
		this.setLayout(new BorderLayout());
		input = new TextField();
		AnimButton = new Button("Animate");
		AnimButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.pause = !Main.pause;
		}});
		input.addKeyListener(this);
		Panel grid = new Panel(new GridLayout(1, 3));
		grid.add(new Label("Moon phase (0.0 - 1.0)", 2));
		grid.add(input);
		grid.add(AnimButton);
		this.add(grid, BorderLayout.SOUTH);
		this.add(animation, BorderLayout.CENTER);
    	this.setSize(700, 425);
    	this.setVisible(true);
    	screen = createImage(this.getWidth(), this.getHeight() - 25);
	}
	@Override
    public void run() {
	while (true) {
	    try {
	    	repaint();
	    	if (!Main.pause)
	    		phase = phase.add(new BigDecimal(0.01));
			Thread.sleep(50);
	    } catch (Exception e) {}
	}
    }
	public void update(Graphics realg) {
		this.animation.repaint();
    }
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			try {
				this.phase = new BigDecimal(Float.valueOf(this.input.getText()));
			}
			catch (NumberFormatException ex)
			{return;}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
	}
	@Override
	public void keyTyped(KeyEvent e) {
	}
}

class MoonCanvas extends Canvas
{
	private static final long serialVersionUID = 1L;
	public void drawMoon(int x, int y, int r, float phase, Graphics g)
	{
		g.setColor(Color.BLACK);
		for (int i = y - r; i <= y + r; i++)
		{
			float len = (float) ((float) r * Math.cos(Math.asin(((float) i - (float) y) / (float) r)));
			float a = x - len;
			float b = x + len;
			g.setColor(new Color(255, 255, 255));
			g.drawLine(Math.round(a), i, Math.round(b), i);
			g.setColor(new Color(31, 31, 31));
			if (phase % 1 < 0.5f)
				g.drawLine(Math.round(b - (len * (phase % 1f) * 4f)), i, Math.round(b), i);
			else
				g.drawLine(Math.round(a), i, Math.round(a + (len * (0.5f - (phase % 1f - 0.5f)) * 4f)), i);
		}
		g.setColor(Color.BLACK);
	}
	
	public void drawBody(int x, int y, int r, Color ca, Color cb, Graphics g)
	{
		g.setColor(Color.BLACK);
		for (int i = y - r; i <= y + r; i++)
		{
			float len = (float) ((float) r * Math.cos(Math.asin(((float) i - (float) y) / (float) r)));
			float a = x - len;
			float b = x + len;
			g.setColor(ca);
			g.drawLine(Math.round(a), i, x, i);
			g.setColor(cb);
			g.drawLine(x, i, Math.round(b), i);
		}
		g.setColor(Color.BLACK);
	}
	
	public void drawPhase(float phase, Graphics g)
	{
		this.drawMoon(550, 200, 50, phase, g);
		this.drawBody(250, 200, 20, new Color(119, 119, 255), new Color(0, 0, 255), g);
		this.drawBody(50, 200, 30, new Color(255, 255, 0), new Color(255, 255, 0), g);
		this.drawBody(Math.round((float) (Math.cos((1f - (phase % 1 - 0.5f) * 2f) * Math.PI)) * 100 + 250), Math.round((float) (Math.sin((phase % 1 - 0.5f) * 2f * Math.PI)) * 100 + 200), 12, new Color(255, 255, 255), new Color(31, 31, 31), g);
		g.setColor(Color.RED);
		g.drawLine(400, 0, 400, main.getHeight());
		g.drawString(String.valueOf(phase), 200, 350);
	}
	
	@Override
	public void update(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setFont(new Font("Arial", Font.BOLD, 18));
		this.drawPhase(main.phase.floatValue(), g);
		//realg.drawImage(screen, 0, 0, this);
	}
	
	Main main;
	public MoonCanvas(Main m)
	{
		main = m;
	}
}
