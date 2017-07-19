import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Scrollbar;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;


public class Terminal implements Runnable, KeyListener, WindowListener, MouseListener, MouseWheelListener
{
	protected TerminalInstance instance;
	
	protected int width;
	protected int height;
	protected int wbuffer;
	protected int hbuffer;
	protected int cursorX;
	protected int cursorY;
	protected int cursorTime;
	protected boolean cursorOn;
	protected boolean fullscreen;
	
	protected int frameX;
	protected int frameY;
	protected Scrollbar scrollX;
	protected Scrollbar scrollY;
	protected int scrollX_fullScreen;
	protected int scrollY_fullScreen;
	protected ConsoleFont font;
	protected Image screen;
	protected int[][] chars;
	protected char[][] fcolors;
	protected char[][] bcolors;
	protected ConsoleListener listener;
	
	private StringBuilder text;
	private int cursorAt;
	protected int sx;
	protected int sy;
	//private String input;
	
	public Terminal()
	{
		this(80, 25, 80, 300, false);
	}
	
	public Terminal(int width_, int height_, boolean fullscreen_)
	{
		this(width_, height_, width_, height_, fullscreen_);
	}
	
	public Terminal(int width_, int height_, int widthbuffer_, int heightbuffer_, boolean fullscreen_)
	{
		fullscreen = fullscreen_;
		sx = 0;
		sy = 0;
		this.instance = new TerminalInstance(this);
		ImageIcon icon = new ImageIcon("resource/icon/default_icon.png");
		instance.setIconImage(icon.getImage());
		instance.setTitle("Console");
		text = new StringBuilder();
		width = width_;
		height = height_;
		wbuffer = widthbuffer_;
		hbuffer = heightbuffer_;
		cursorX = 0;
		cursorY = 0;
		cursorTime = 26;
		cursorAt = 0;
		chars = new int[wbuffer][hbuffer];
		fcolors = new char[wbuffer][hbuffer];
		bcolors = new char[wbuffer][hbuffer];
		for (int x = 0; x < wbuffer; x++)
		{
			for (int y = 0; y < hbuffer; y++)
			{
				chars[x][y] = 0;
				fcolors[x][y] = '7';
				bcolors[x][y] = '0';
			}
		}
		if (fullscreen)
		{
			instance.setUndecorated(true);
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(instance);
		}
		instance.addKeyListener(this);
		instance.addWindowListener(this);
		instance.addMouseWheelListener(this);
		instance.setResizable(false);
		instance.setLayout(new BorderLayout());
		scrollX_fullScreen = 0;
		scrollY_fullScreen= 0;
		this.scrollX = new Scrollbar(Scrollbar.HORIZONTAL, 0, width, 0, wbuffer);
		scrollX.addKeyListener(this);
		if (wbuffer - width == 0 || fullscreen) this.scrollX.setEnabled(false);
		if (this.scrollX.isEnabled())
			instance.add(this.scrollX, BorderLayout.SOUTH);
		this.scrollY = new Scrollbar(Scrollbar.VERTICAL, 0, height, 0, hbuffer);
		scrollY.addKeyListener(this);
		if (hbuffer - height == 0 || fullscreen) this.scrollY.setEnabled(false);
		if (this.scrollY.isEnabled())
			instance.add(this.scrollY, BorderLayout.EAST);

		this.font = ConsoleFont.getFont("DEFAULT", true, "F:\\Eclipse\\TerminalE\\resource\\font\\");

		instance.setVisible(true);
		if (this.scrollY.isEnabled())
			this.frameX = instance.getInsets().left + instance.getInsets().right + this.font.xsize * this.width + this.scrollY.getWidth();
		else
			this.frameX = instance.getInsets().left + instance.getInsets().right + this.font.xsize * this.width;
		if (this.scrollX.isEnabled())
			this.frameY = instance.getInsets().top + instance.getInsets().bottom + this.font.ysize * this.height + this.scrollX.getHeight();
		else
			this.frameY = instance.getInsets().top + instance.getInsets().bottom + this.font.ysize * this.height;
		instance.setSize(this.frameX, this.frameY);
		screen = instance.createImage(this.font.xsize * this.width, this.font.ysize * this.height);
		
		
		

		writeString( System.getProperty("user.dir") + ">");
	}
	
	public void run()
	{
		while (true)
		{
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {}
			if (this.cursorTime == 0)
			{
				this.cursorTime = 26;
				this.cursorOn = !this.cursorOn;
			}
			this.cursorTime--;
			instance.repaint();
		}
	}
	
	public void writeString(String str)
	{
		for (int c = 0; c < str.length(); c++)
		{
			char chr = str.charAt(c);
			writeChar(chr);
		}
	}
	
	private void writeChar(char c)
	{
		this.chars[sx][sy] = c;
		if (sx == this.wbuffer || c == '\n')
		{
			sx = 0;
			if (sy >= this.hbuffer - 1)
			{
				sy = this.hbuffer - 1;
				for (int y = 0; y < this.hbuffer - 1; y++)
				{
					for (int x = 0; x < this.wbuffer; x++)
					{
						this.chars[x][y] = this.chars[x][y+1];
					}
				}
			}
			else
			{
				sy++;
			}
		}
		else if (c == '\t')
		{
			int tab = 8;
			sx = sx+(tab-(sx+1)%tab);
		}
		else
		{
			sx++;
		}
		this.cursorX = sx;
		this.cursorY = sy;
	}
	
	public void setConsoleListener(ConsoleListener instance)
	{
		this.listener = instance;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public int getWidthBuffer()
	{
		return this.wbuffer;
	}
	
	public int getHeightBuffer()
	{
		return this.hbuffer;
	}
	
	protected int getScrollX()
	{
		return (this.fullscreen)? this.scrollX_fullScreen : this.scrollX.getValue();
	}

	protected int getScrollY()
	{
		return (this.fullscreen)? this.scrollY_fullScreen : this.scrollY.getValue();
	}
	
	protected void setScrollX(int n)
	{
		if (this.fullscreen) this.scrollX_fullScreen = n;
		else this.scrollX.setValue(n);
	}
	
	protected void setScrollY(int n)
	{
		if (this.fullscreen) this.scrollY_fullScreen = n;
		else this.scrollY.setValue(n);
	}
	/*public boolean setForegroundColor(char c)
	{
		if (Terminal.getColor(c) == null) return false;
		this.fground = c;
		return true;
	}
	
	public boolean setBackgroundColor(char c)
	{
		if (Terminal.getColor(c) == null) return false;
		this.bground = c;
		return true;
	}*/
	
	public void setChar(int x, int y, int c)
	{
		this.chars[x][y] = c;
	}
	
	protected static Color getColor(char c)
	{
		switch(c)
		{
		case '0': return new Color(0, 0, 0);
		case '1': return new Color(0, 0, 127);
		case '2': return new Color(0, 127, 0);
		case '3': return new Color(0, 127, 127);
		case '4': return new Color(127, 0, 0);
		case '5': return new Color(127, 0, 127);
		case '6': return new Color(127, 127, 0);
		case '7': return new Color(191, 191, 191);
		case '8': return new Color(127, 127, 127);
		case '9': return new Color(0, 0, 255);
		case 'a': return new Color(0, 255, 0);
		case 'b': return new Color(0, 255, 255);
		case 'c': return new Color(255, 0, 0);
		case 'd': return new Color(255, 0, 255);
		case 'e': return new Color(255, 0, 255);
		case 'f': return new Color(255, 255, 255);
		default: return null;
		}
	}
	
	protected static Color getInvColor(Color c)
	{
		return new Color(191 - c.getRed(), 191 - c.getGreen(), 191 - c.getBlue()) ;
	}
	
	public void drawText(int x, int y, String str)
	{
		for (int i = 0; i < str.length(); i++)
		{
			this.chars[i + x][y] = str.charAt(i);
		}
	}

	boolean input = true;
	ProcThread p = null;
	@Override
	public void keyPressed(KeyEvent event)
	{
		if (listener != null)
			listener.onKeyPress(event);
		if (event.getKeyChar() == 27 || event.getKeyChar() == 3)
		{
			if (input)
				System.exit(0);
			else
				p.proc.destroy();
			return;
		}
		if (event.getKeyChar() > 255)
		{
			if (!this.cursorOn)
			{
				this.cursorTime = 26;
				this.cursorOn = true;
			}
			if (event.getKeyCode() == KeyEvent.VK_LEFT)
			{
				if (this.cursorAt > 0)
					this.cursorAt--;
			}
			else if (event.getKeyCode() == KeyEvent.VK_RIGHT)
			{
				if (this.cursorAt < text.length())
					this.cursorAt++;
			}
		}
		else
		{
			if (this.cursorOn)
			{
				this.cursorTime = 26;
				this.cursorOn = false;
			}
			if (event.getKeyCode() == KeyEvent.VK_BACK_SPACE)
			{
				if (this.cursorAt > 0)
				{
					text.deleteCharAt(this.cursorAt - 1);
					this.cursorAt--;
				}
			}
			else if (event.getKeyCode() == KeyEvent.VK_ENTER)
			{
				this.sy = (sx + sy * this.wbuffer + text.length()) / this.wbuffer + 1;
				this.sx = 0;
				this.cursorAt = 0;
				this.cursorX=0;
				this.cursorY++;
				if (text.toString().equals(""))
					return;
				if (input)
				{
					if (text.toString().equalsIgnoreCase("exit") || text.toString().equalsIgnoreCase("quit"))
						System.exit(0);
					p = new ProcThread(text.toString(), this);
					p.run();
					this.text = new StringBuilder("");
				}
				else
				{
					p.input(this.text.toString());
					this.text = new StringBuilder("");
				}
				
				return;
			}
			else
			{
				text.insert(this.cursorAt, event.getKeyChar());
				this.cursorAt++;
			}
		}
		
		for (int i = 0; i < text.length(); i++)
		{
			this.chars[(sx + sy * this.wbuffer + i) % this.wbuffer][(sx + sy * this.wbuffer + i) / this.wbuffer] = text.charAt(i);
		}
		this.chars[(sx + sy * this.wbuffer + text.length()) % this.wbuffer][(sx + sy * this.wbuffer + text.length()) / this.wbuffer] = 32;
		this.cursorX = (sx + sy * this.wbuffer + this.cursorAt) % this.wbuffer;
		this.cursorY = (sx + sy * this.wbuffer + this.cursorAt) / this.wbuffer;
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		if (listener != null)
			listener.onKeyRelease(event);
	}


	@Override
	public void windowClosing(WindowEvent arg0) {
		listener.onWindowClose();
		instance.dispose();
	}

	@Override public void keyTyped(KeyEvent arg0) {}
	@Override public void windowActivated(WindowEvent arg0) {}
	@Override public void windowClosed(WindowEvent arg0) {}
	@Override public void windowDeactivated(WindowEvent arg0) {}
	@Override public void windowDeiconified(WindowEvent arg0) {}
	@Override public void windowIconified(WindowEvent arg0) {}
	@Override public void windowOpened(WindowEvent arg0) {}
	@Override public void mouseClicked(MouseEvent arg0) {}
	@Override public void mouseEntered(MouseEvent arg0) {}
	@Override public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent event) {
		if (listener != null)
			listener.onMousePress(event);
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (listener != null)
			listener.onMouseRelease(event);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent event) {
			if (this.scrollY_fullScreen + event.getWheelRotation() * 3 >= 0 && this.scrollY_fullScreen + event.getWheelRotation() * 3 <= this.hbuffer - this.height)
				this.setScrollX(this.getScrollY() + event.getWheelRotation() * 3);
	}

}
