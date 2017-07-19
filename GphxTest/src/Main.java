import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
abstract class GameObj
{
	public int x;
	public int y;
	public abstract boolean move(int ylen);
	public abstract void render(Graphics g);
	public GameObj(int xx, int yy)
	{
		x = xx;
		y = yy;
	}
}
class Proj extends GameObj
{
	public Proj(int xx, int yy) {
		super(xx, yy);
		// TODO Auto-generated constructor stub
	}
	public boolean move(int ylen)
	{
		if (this.y > 1)
			this.y = this.y - 4;
		else
			return true;
		return false;
	}
	public void render(Graphics g)
	{
		g.setColor(Color.RED);
		g.fillOval(x - 2, y - 2, 4, 4);
	}
}
class Enemy extends GameObj
{
	public Enemy(int xx, int yy) {
		super(xx, yy);
		// TODO Auto-generated constructor stub
	}
	public boolean move(int ylen) {
		if (this.y < ylen)
			this.y = this.y + 1;
		else
			return true;
		return false;
	}
	public void render(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(x - 10, y - 5, 20, 10);
	}
}
class Player extends GameObj
{
	public Player(int xx, int yy) {
		super(xx, yy);
		// TODO Auto-generated constructor stub
	}
	public boolean move(int ylen) {return false;};
	public void render(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect(x - 10, y - 5, 20, 10);
	}
}

public class Main extends Frame implements Runnable {
    
	private static final long serialVersionUID = 1L;
	//Thread engine = null;
	Player pl;
    Dimension winSize;
    Image dbimage;
    int score = 0;
    List<Proj> projs;
    List<Enemy> enemies;
    int outcome = 0;
    public Main() {
    	this.setSize(400, 400);
    	this.setLayout(new BorderLayout());
    	
    	//this.add(new Button("asd"), BorderLayout.SOUTH);
    	pl = new Player(200, 250);
    	this.setVisible(true);
    	dbimage = createImage(this.getWidth(), this.getHeight());
    	projs = new ArrayList<Proj>();
    	enemies = new ArrayList<Enemy>();
    }
    public static void main(String[] args)
	{
		Main instance = new Main();
		instance.run();
		System.out.print("Moon sim  ");
	}
    public void newgame()
    {
    	this.setSize(400, 300);
    	pl = new Player(200, 250);
    	this.setVisible(true);
    	dbimage = createImage(this.getWidth(), this.getHeight());
    	projs = new ArrayList<Proj>();
    	enemies = new ArrayList<Enemy>();
    }
	public void update(Graphics rg) {
		//Graphics g = this.getGraphics();
		Graphics g = dbimage.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setFont(new Font("Arial", Font.BOLD, 18));
		if (outcome != 0)
		{
			if (outcome == -1)
			{
			g.setColor(Color.RED);
			g.drawString("YOU LOSE", 140, 100);
			}
			else
			{
				g.setColor(Color.YELLOW);
				g.drawString("YOU WIN", 140, 100);
			}
		}
		else
		{
		try
		{
		for (Proj p : projs)
			p.render(g);
		for (Enemy p : enemies)
			p.render(g);
		}
		catch (Exception e){};
		pl.render(g);
		g.drawString("Number hit: " + score, 140, 280);
		}
		//g.setColor(Color.red);
		//g.drawString("PONG", 50, 50);
		//realg.drawImage(this, 0, 0, this);
		rg.drawImage(dbimage, 0, 0, this);
	    }
    @Override
    public void run() {
	while (true) {
	    try {
			Thread.sleep(15);
	    	//repaint();
			this.repaint();
	    	if (outcome != 0)
	    		{
	    		newgame();
	    		continue;
	    		}
	    	if (((int) Math.floor(Math.random()*41)) == 0)
	    		enemies.add(new Enemy((int) Math.floor(Math.random()*300) + 50, (int) Math.floor(Math.random()*101)));
	    	for (Proj p : projs)
	    		if (p.move(this.getHeight()))
	    			projs.remove(p);
	    	for (Enemy p : enemies)
	    		if (p.move(this.getHeight()))
	    	    	outcome = -1;
	    	for (Proj p : projs)
	    		for (Enemy e : enemies)
	    			if (p.x > e.x - 10 && p.x < e.x + 10 && p.y > e.y - 5 && p.y < e.y + 5)
	    			{
	    				projs.remove(p);
	    				enemies.remove(e);
	    				score++;
	    			}
	    	if (score > 29)
	    		outcome = 1;
	    } catch (Exception e) {}
	}
    }

    
    public boolean handleEvent(Event evt) {
    	if (evt.id == Event.MOUSE_MOVE) {
    	    pl.x = evt.x;
    	    return true;
    	} else if (evt.id == Event.MOUSE_DOWN) {
    		if (outcome == 0)
    			projs.add(new Proj(pl.x, pl.y));
    		else
    		{
    			outcome = 0;
    			score = 0;
    		}
    	    return true;
    	} else if (evt.id == Event.WINDOW_DESTROY) {
    		System.exit(0);
    		return true;
    	} else {	    
    	    return false;
    	}
        }
    /*public void start() {
	if (engine == null) {
	    engine = new Thread(this);
	    engine.start();
	}
    }

    public void stop() {
	if (engine != null && engine.isAlive()) {
	    engine.stop();
	}
	engine = null;
    }*/
    
}