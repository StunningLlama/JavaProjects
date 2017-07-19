import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

// #DEFINE NONE 0
// #DEFINE METAL 1
// #DEFINE P_SILICON 2
// #DEFINE N_SILICON 3
// #DEFINE CROSSING 4
// #DEFINE VIA 5
// #DEFINE POWER_SRC 6
// #DEFINE NPN_TRANSISTOR 7
// #DEFINE PNP_TRANSISTOR 8
// #DEFINE JK_FF_OR_SWITCH 9

// #DEFINE POWERED 0x01
// #DEFINE ACTIVE 0x02
// #DEFINE RISING_EDGE_BIT 0x04


public class Sim extends JFrame implements Runnable, MouseListener, MouseMotionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6720270638055944587L;
	public byte[][] particles;
	public byte[][] data;
	public byte[][][] undobuffer;
	public byte[][] clipboard;

	public int undopos = 0;
	public int undohead = 0;
	public int undobuffersize = 16;
	public int xsize = 96;
	public int ysize = 64;
	public int pixelsize = 8;
	public int mousex = 0;
	public int mousey = 0;
	public int selected = 1;
	public int tmp = 0;
	public boolean doticks = true;
	public boolean mousedown = false;
	public int barheight = 16;
	public int testpos = 0;

	public int selectclipboard = 0; //0=NONE, 1=CUT, 2=COPY
	public int startx = 0;
	public int starty = 0;
	public int endx = 0;
	public int endy = 0;

	public Image display;
	public Color BGColor = new Color(0x00333333);
	public Color GridColor = new Color(0x00666666);
	public Color SelectionColor = new Color(0x00FFFFFF);
	public JMenuBar menu;
	public CustomCanvas canvas;
	public TPSWindow TPSopts;
	public FieldOptionsWindow FieldOpts;
	public NewLevelWindow NewLevel;
	
	public static void main(String[] args)
	{
		Sim instance = new Sim();
		instance.run();
	}
	
	public void CopyToUndoBuffer()
	{
		if (undopos == undobuffersize - 1)
			for (int i = 0; i < undobuffersize - 1; i++)
				for (int x = 0; x < xsize; x++)
					for (int y = 0; y < ysize; y++)
						undobuffer[i][x][y] = undobuffer[i+1][x][y];
		else
			undopos++;
		for (int x = 0; x < xsize; x++)
			for (int y = 0; y < ysize; y++)
				undobuffer[undopos][x][y] = particles[x][y];
		undohead = undopos;
	}
	
	public void Undo()
	{
		if (undopos == 0) return;
		undopos--;
		for (int x = 0; x < xsize; x++)
			for (int y = 0; y < ysize; y++)
				particles[x][y] = undobuffer[undopos][x][y];
	}

	public void Redo()
	{
		if (undopos < undohead)
		{
			undopos++;
			for (int x = 0; x < xsize; x++)
				for (int y = 0; y < ysize; y++)
					particles[x][y] = undobuffer[undopos][x][y];
		}
	}
	
	public void initArrays()
	{
		updateSize();
		display = this.createImage(xsize*pixelsize, ysize*pixelsize+barheight);
		particles = new byte[xsize][ysize];
		data = new byte[xsize][ysize];
		undobuffer = new byte[undobuffersize][xsize][ysize];
		for (int x = 0; x < xsize; x++)
		{
			for (int y = 0; y < ysize; y++)
			{
				particles[x][y] = 0;
				data[x][y] = 0;
			}
		}
		for (int x = 0; x < xsize; x++)
			for (int y = 0; y < ysize; y++)
				for (int z = 0; z < undobuffersize; z++)
					undobuffer[z][x][y]=0;
	}
	
	public Sim()
	{
		this.setSize(xsize*pixelsize, ysize*pixelsize+barheight);
		this.setResizable(false);
		this.setVisible(true);
		canvas = new CustomCanvas(this);
		initArrays();
		TPSopts = new TPSWindow(this);
		FieldOpts = new FieldOptionsWindow(this);
		NewLevel = new NewLevelWindow(this);
		menu = new JMenuBar();
		JMenu menu_a = new JMenu("File"); //New, Open, Save
		JMenuItem a1 = new JMenuItem("New"); a1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK)); a1.addActionListener(new CustActionListener(this, 105)); menu_a.add(a1);
		JMenuItem a2 = new JMenuItem("Open"); a2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK)); a2.addActionListener(new CustActionListener(this, 106)); menu_a.add(a2);
		JMenuItem a3 = new JMenuItem("Save"); a3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK)); a3.addActionListener(new CustActionListener(this, 107)); menu_a.add(a3);
		JMenu menu_b = new JMenu("Edit"); //Cut, Copy, Paste
		JMenuItem b1 = new JMenuItem("Undo"); b1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK)); b1.addActionListener(new CustActionListener(this, 109)); menu_b.add(b1);
		JMenuItem b2 = new JMenuItem("Redo"); b2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK)); b2.addActionListener(new CustActionListener(this, 110)); menu_b.add(b2);
		JMenuItem b3 = new JMenuItem("Cut"); b3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK)); b3.addActionListener(new CustActionListener(this, 111)); menu_b.add(b3);
		JMenuItem b4 = new JMenuItem("Copy"); b4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK)); b4.addActionListener(new CustActionListener(this, 112)); menu_b.add(b4);
		JMenuItem b5 = new JMenuItem("Paste"); b5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK)); b5.addActionListener(new CustActionListener(this, 113)); menu_b.add(b5);
		JMenu menu_c = new JMenu("Simulation"); //TPS, Elements
		JMenuItem c1 = new JMenuItem("Ticks Per Second"); c1.addActionListener(new CustActionListener(this, 102)); menu_c.add(c1);
		JMenuItem c2 = new JMenuItem("Field Options"); c2.addActionListener(new CustActionListener(this, 103)); menu_c.add(c2);
		JMenuItem c3 = new JMenuItem("Clear Field"); c3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK)); c3.addActionListener(new CustActionListener(this, 104)); menu_c.add(c3);
		JMenu menu_d = new JMenu("Elements"); //TPS, Elements
		JMenuItem d1;
		for (int i = 0; i < Elem.names.length; i++)
		{
			d1 = new JMenuItem(Elem.names[i]);
			d1.addActionListener(new CustActionListener(this, i));
			switch(i)
			{
			case Elem.METAL:
				d1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0));
				break;
			case Elem.P_SILICON:
				d1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0));
				break;
			case Elem.N_SILICON:
				d1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0));
				break;
			case Elem.POWER_SRC:
				d1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, 0));
				break;
			case Elem.VIA:
				d1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0));
				break;
			case Elem.NONE:
				d1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, 0));
				break;
			}
			menu_d.add(d1);
		}
		d1 = new JMenuItem("Spark");
		d1.addActionListener(new CustActionListener(this, -1));
		menu_d.add(d1);
		
		menu.add(menu_a);
		menu.add(menu_b);
		menu.add(menu_c);
		menu.add(menu_d);
		
		this.setJMenuBar(menu);
		this.setTitle("Electronics Simulator");
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		this.setLayout(new BorderLayout());
		this.add(canvas);
		bartext = "Selected Element: " + Elem.names[selected];
		updateSize();
	}
	
	private boolean isPowered(int x, int y, int type)
	{
		if ((particles[max(x-1, 0)][y]==type && (max(data[x-1][y],0)&0x01)!=0)||
				(particles[min(x+1, xsize-1)][y]==type && (data[min(x+1, xsize-1)][y]&0x01)!=0)||
				(particles[x][max(y-1, 0)]==type && (data[x][max(y-1, 0)]&0x01)!=0)||
				(particles[x][min(y+1, ysize-1)]==type && (data[x][min(y+1, ysize-1)]&0x01)!=0))
			return true;
		return false;
	}
	
	private int max(int x, int y) {if (x > y) return x; return y;}
	private int min(int x, int y) {if (x < y) return x; return y;}

	//synchronized
	public void Tick()
	{
		if (!doticks) return;
		//1. update transistors/switches
		//2. clear electricity
		//3. update electricity using flood fill
		for (int x = 0; x < xsize; x++)
		{
			for (int y = 0; y < ysize; y++)
			{
				//Bitwise ops
				if (particles[x][y]==Elem.NPN_TRANSISTOR)
					if (isPowered(x, y, Elem.P_SILICON))
						data[x][y] |= 0x2;
					else
						data[x][y] &= (~0^0x02);
				if (particles[x][y]==Elem.PNP_TRANSISTOR)
					if (isPowered(x, y, Elem.N_SILICON))
						data[x][y] &= (~0^0x02);
					else
						data[x][y] |= 0x2;
				if (particles[x][y]==Elem.JK_FLIPFLOP)
					if (isPowered(x, y, Elem.P_SILICON))
						if (isPowered(x, y, Elem.N_SILICON))
						{
							if ((data[x][y]&0x4)==0) {
								data[x][y] ^= 0x2;
								data[x][y] |= 0x4;
							}
						} else {
							data[x][y] |= 0x2;
							data[x][y] &= (~0^0x04);
						}
					else if (isPowered(x, y, Elem.N_SILICON)){
						data[x][y] &= (~0^0x02);
						data[x][y] &= (~0^0x04);
					}
					else
						data[x][y] &= (~0^0x04);
			}
		}
		for (int x = 0; x < xsize; x++)
		{
			for (int y = 0; y < ysize; y++)
			{
				data[x][y] &= (~0^0x01);
			}
		}
		if (this.mousedown && this.selected == -1)
			this.flood_electric(mousex, mousey, mousex, mousey, Elem.POWER_SRC);
		long startTime = System.nanoTime();
		for (int x = 0; x < xsize; x++)
		{
			for (int y = 0; y < ysize; y++)
			{
				if(particles[x][y]==Elem.POWER_SRC)
				{
					flood_electric(x, y, x+1, y, Elem.POWER_SRC);
					flood_electric(x, y, x-1, y, Elem.POWER_SRC);
					flood_electric(x, y, x, y+1, Elem.POWER_SRC);
					flood_electric(x, y, x, y-1, Elem.POWER_SRC);
				}
			}
		}
		this.bartext = "Time: " + ((System.nanoTime() - startTime)/1000); 
	}
	
	public void flood_electric(int px, int py, int x, int y, int parentelem)
	{
		if (x < 0 || x >= xsize || y < 0 || y >= ysize)
			return;
		if (!canflood(particles[x][y], particles[px][py], (data[x][y]&0x02)!=0, (data[x][y]&0x01)!=0))
			return;
		data[x][y] |= 0x01;
		if (particles[x][y] != Elem.CROSSING)
		{
			flood_electric(x, y, x+1, y, particles[x][y]);
			flood_electric(x, y, x-1, y, particles[x][y]);
			flood_electric(x, y, x, y+1, particles[x][y]);
			flood_electric(x, y, x, y-1, particles[x][y]);
		}
		else
		{
			flood_electric(x, y, x+x-px, y+y-py, particles[x][y]);
		}
	}
	
	public boolean canflood(int elem, int parentelem, boolean elemactivestate, boolean elempoweredstate)
	{
		if (parentelem == Elem.NONE || elem == Elem.NONE || (elempoweredstate && !(elem == Elem.CROSSING))) return false;
		if (parentelem == elem) return true;
		switch(parentelem)
		{
		case Elem.CROSSING:
		case Elem.JK_FLIPFLOP:
		case Elem.POWER_SRC:
			if (elem == Elem.METAL) return true; else return false;
		case Elem.METAL:
			if (elem == Elem.CROSSING || elem == Elem.VIA || elem == Elem.OUTPUT || (elem == Elem.JK_FLIPFLOP  && elemactivestate)) return true; else return false;
		case Elem.P_SILICON:
			if (elem == Elem.N_SILICON || elem == Elem.VIA || elem == Elem.PNP_TRANSISTOR && elemactivestate) return true; else return false;
		case Elem.N_SILICON:
			if (elem == Elem.VIA || elem == Elem.NPN_TRANSISTOR && elemactivestate) return true; else return false;
		case Elem.VIA:
			if (elem == Elem.METAL || elem == Elem.P_SILICON || elem == Elem.N_SILICON) return true; else return false;
		case Elem.NPN_TRANSISTOR:
			if (elem == Elem.N_SILICON) return true; else return false;
		case Elem.PNP_TRANSISTOR:
			if (elem == Elem.P_SILICON) return true; else return false;
		default:
			return false;
		}
	}
	
	/*
	public static int NONE=0; //cannot_flood_fill
	public static int METAL= 1; //can go to: crossing, via, JK_SWITCH
	public static int P_SILICON= 2; //can go to: Nsilicon, PNP transistor, via
	public static int N_SILICON= 3; //can go to: NPN transistor, via
	public static int CROSSING= 4; //can go to: metal
	public static int VIA= 5; //can go to: metal, Psilicon, Nsilicon
	public static int POWER_SRC= 6; //cannot_flood_fill
	public static int NPN_TRANSISTOR= 7; //can go to: Nsilicon
	public static int PNP_TRANSISTOR= 8; //can go to: Psilicon
	public static int JK_FF_OR_SWITCH= 9; //can go to: metal*/
	
	public void render(Graphics2D g)
	{
		g.setColor(BGColor);
		int height = this.display.getHeight(this);
		int width = this.display.getWidth(this);
		g.fillRect(0, 0, width, height);
		for (int x = 0; x < xsize; x++)
			for (int y = 0; y < ysize; y++)
				if (particles[x][y] != 0){
					if ((data[x][y]&0x3)==0)
						g.setColor(new Color(colors[particles[x][y]]));
					else
						g.setColor(new Color(brightcolors[particles[x][y]]));
					g.fillRect(x*pixelsize, y*pixelsize, pixelsize, pixelsize);
				}
		if (this.selectclipboard == 3)
			for (int x = 0; x < clipboardxlen; x++)
				for (int y = 0; y < clipboardylen; y++)
					if (clipboard[x][y] != 0) {
						g.setColor(new Color(colors[clipboard[x][y]]));;
						g.fillRect((mousex+x)*pixelsize, (mousey+y)*pixelsize, pixelsize, pixelsize);
					}
		g.setColor(GridColor);
		if (this.FieldOpts.GridEnabled.isSelected())
			for (int x = 0; x < xsize; x++)
				for (int y = 0; y < ysize; y++)
					g.drawRect(x*pixelsize, y*pixelsize, pixelsize, pixelsize);
		g.setColor(SelectionColor);
		if (this.selectclipboard == 3)
			g.drawRect(mousex*pixelsize, mousey*pixelsize, clipboardxlen*pixelsize, clipboardylen*pixelsize);
		else if (this.selectclipboard != 0 && mousedown)
			g.drawRect(Math.min(endx, startx)*pixelsize, Math.min(endy, starty)*pixelsize, Math.abs(endx-startx)*pixelsize+pixelsize, Math.abs(endy-starty)*pixelsize+pixelsize);
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, height-barheight, width, barheight);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(new Font(Font.SANS_SERIF, 0, 12));
		g.setColor(Color.BLACK);
		g.drawString(bartext, 2, height - 4);
	}

	public String bartext = "";
	
	public static final int[] colors = {
			0x00000000,
			0x00777777,
			0x006B0000,
			0x0000006B,
			0x004A4A4A,
			0x00997799,
			0x00CDCD00,
			0x00FF4444,
			0x004444FF,
			0x00009900,
			0x00AAAAAA,
			0x00006666
	};
	
	public static final int[] brightcolors = {
			0x00000000,
			0x00AAAA55,
			0x00AA0000,
			0x000000AA,
			0x005A5A5A,
			0x00BB66BB,
			0x00CDCD00,
			0x00FF6666,
			0x006666FF,
			0x0000DD00,
			0x00AAAAAA,
			0x0000FFFF
	};
	
	public void updateSize()
	{
		Dimension d = new Dimension(xsize*pixelsize, ysize*pixelsize+barheight);
		canvas.setPreferredSize(d);
		canvas.revalidate();
		pack();
	}
	
	@Override
	public void run() {
		long timer = 0;
		int tmppixelsize = 8;
		while (true)
		{
			if (tmppixelsize != pixelsize)
			{
				display = this.createImage(xsize*pixelsize, ysize*pixelsize+barheight);
				tmppixelsize = pixelsize;
				updateSize();
			}
			setLabels();
			try {
				Thread.sleep(1000/60);
			if (timer % (60/TPSopts.TPSScrollbar.getValue()) == 0)
				this.Tick();
			canvas.repaint();
			timer++;
			} catch (InterruptedException e) {
				
			} catch (StackOverflowError e) {
				bartext="Error; Could not compute";
			}
			
		}
	}
	
	public void setLabels()
	{
		this.pixelsize = this.FieldOpts.PixelSizeScrollbar.getValue();
		TPSopts.TPSLabel.setText("Ticks Per Second: " + TPSopts.TPSScrollbar.getValue());
		this.FieldOpts.PixelSizeLabel.setText("Pixel Size: " + this.FieldOpts.PixelSizeScrollbar.getValue());
		this.NewLevel.XLabel.setText("Width: " + this.NewLevel.XScroll.getValue());
		this.NewLevel.YLabel.setText("Height: " + this.NewLevel.YScroll.getValue());
	}
	
	
	//synchronized
	public void setelem(int x, int y, int elem)
	{
		if (x < 0 || x >= xsize || y < 0 || y >= ysize)
			return;
		particles[x][y] = (byte) elem;
		data[x][y] = 0x00;
	}

	@Override public void mouseClicked(MouseEvent arg0) {}
	@Override public void mouseEntered(MouseEvent arg0) {}
	@Override public void mouseExited(MouseEvent arg0) {}
	
	
	int clipboardxlen;

	int clipboardylen;
	@Override public void mouseReleased(MouseEvent e)
	{
		mousedown = false;
		if (this.selectclipboard != 0)
		{
			bartext = "Selected Element: " + Elem.names[selected];
			if (this.selectclipboard == 3)
			{
				this.selectclipboard = 0;
				return;
			}
			this.endx = Math.max(startx, Math.min(xsize - 1, e.getX()/pixelsize));
			this.endy = Math.max(starty, Math.min(ysize - 1, e.getY()/pixelsize));
			startx = Math.min(startx, Math.max(0, e.getX()/pixelsize));
			starty = Math.min(starty, Math.max(0, e.getY()/pixelsize));
			clipboardxlen = endx-startx+1;
			clipboardylen = endy-starty+1;
			this.clipboard = new byte[clipboardxlen][clipboardylen];
			for (int x = 0; x < clipboardxlen; x++)
			{
				for (int y = 0; y < clipboardylen; y++)
				{
					clipboard[x][y] = particles[x+startx][y+starty];
				}
			}
			if (this.selectclipboard == 1)
			{
				for (int x = 0; x < clipboardxlen; x++)
				{
					for (int y = 0; y < clipboardylen; y++)
					{
						particles[x+startx][y+starty] = 0;
						data[x+startx][y+starty] = 0;
					}
				}
				this.CopyToUndoBuffer();
			}
			this.selectclipboard = 0;
		}
		else
		{
			this.CopyToUndoBuffer();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mousex = e.getX()/pixelsize;
		mousey = e.getY()/pixelsize;
		if (e.getButton() == MouseEvent.BUTTON2)
		{
			for (int y = 0; y < ysize; y++)
			{
				for (int x = 0; x < xsize; x++)
				{
					System.out.print(data[x][y]+", ");
				}
				System.out.println("");
			}
			return;
		}
		mousedown = true;
		int xl = e.getX()/pixelsize;
		int yl = e.getY()/pixelsize;
		if (this.selectclipboard != 0)
		{
			if (selectclipboard == 3)
			{
				if (e.getButton() == MouseEvent.BUTTON3) return;
				for (int x = 0; x < clipboardxlen; x++)
					for (int y = 0; y < clipboardylen; y++)
						if (clipboard[x][y] != 0)
							if (xl+x >= 0 && xl+x < xsize && yl+y >= 0 && yl+y < ysize)
							{
								particles[xl+x][yl+y] = clipboard[x][y];
								data[xl+x][yl+y] = 0x00;
							}
				this.CopyToUndoBuffer();
				return;
			}
			else
			{
				this.startx = e.getX()/pixelsize;
				this.starty = e.getY()/pixelsize;
				endx = e.getX()/pixelsize;
				endy = e.getY()/pixelsize;
				return;
			}
		}
		if (e.getButton() == MouseEvent.BUTTON1)
			tmp = selected;
		else
			tmp = 0;
		if (tmp == -1)
		{
			return;
		}
		draw(e.getX(), e.getY(), tmp);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mousex = e.getX()/pixelsize;
		mousey = e.getY()/pixelsize;
		if (this.selectclipboard!=0)
		{
			endx = e.getX()/pixelsize;
			endy = e.getY()/pixelsize;
			return;
		}
		if (tmp == -1)
		{
			return;
		}
		draw(e.getX(), e.getY(), tmp);
	}
	
	public int prevx = 0;
	public int prevy = 0;
	public int originx = 0;
	public int originy = 0;
	
	public void draw(int xi, int yi, int elem)
	{

		int x = xi/pixelsize;
		int y = yi/pixelsize;
		setelem(x, y, elem);
		if (Math.abs(x-prevx) > 1 || Math.abs(y-prevy) > 1)
		{
			int xx = x;
			int x2 = prevx;
			int yy = y;
			int y2 = prevy;
			int w = x2 - xx ;
			int h = y2 - yy ;
			int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
			if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
			if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
			if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
			int longest = Math.abs(w) ;
			int shortest = Math.abs(h) ;
			if (!(longest>shortest)) {
				longest = Math.abs(h) ;
				shortest = Math.abs(w) ;
				if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
				dx2 = 0 ;            
			}
			int numerator = longest >> 1 ;
		for (int i=0;i<=longest;i++) {
			setelem(xx, yy, elem);
			numerator += shortest ;
			if (!(numerator<longest)) {
				numerator -= longest ;
				xx += dx1 ;
				yy += dy1 ;
			} else {
				xx += dx2 ;
				yy += dy2 ;
			}
		}
		}
		prevx = x;
		prevy = y;
	}
	
	@Override
	public void mouseMoved(MouseEvent arg0) {
		mousex = prevx = arg0.getX()/pixelsize;
		mousey = prevy = arg0.getY()/pixelsize;
	}
	
	/*private int restrict(int min, int max, int val)
	{
		if (val < min) return min;
		if (val > max) return max;
		return val;
	}*/

	public void notify(int id)
	{
		if (id == -1)
		{
			selected = id;
			bartext = "Selected Element: Spark";
			return;
		}
		if (id < 100)
		{
			selected = id;
			bartext = "Selected Element: " + Elem.names[selected];
		}
		else
			switch(id) {
			case 100:
				BGColor = JColorChooser.showDialog(
	                     this,
	                     "Choose Background Color",
	                     new Color(0x00333333));
				return;
			case 101:
				GridColor = JColorChooser.showDialog(
	                     this,
	                     "Choose Background Color",
	                     new Color(0x00666666));
				return;
			case 102:
				this.TPSopts.setVisible(!this.TPSopts.isVisible());
				return;
			case 103:
				this.FieldOpts.setVisible(!this.FieldOpts.isVisible());
				return;
			case 104://clear
				for (int x = 0; x < xsize; x++) {
					for (int y = 0; y < ysize; y++) {
						particles[x][y] = 0;
						data[x][y] = 0;
					}
				}
				return;
			case 105:
				this.NewLevel.setVisible(true);
				return;
			case 106:
				doticks = false;
				this.constructlevel();
				doticks = true;
				return;
			case 107:
				doticks = false;
				this.exportLevel();
				doticks = true;
				return;
			case 108:
				doticks = false;
				this.NewLevel.setVisible(false);
				this.xsize = NewLevel.XScroll.getValue();
				this.ysize = NewLevel.YScroll.getValue();
				initArrays();
				doticks = true;
				return;
			case 109:
				this.Undo();
				return;
			case 110:
				this.Redo();
				return;
			case 111:
				selectclipboard = 1;
				bartext = "Select Area to Cut";
				return;
			case 112:
				selectclipboard = 2;
				bartext = "Select Area to Copy";
				return;
			case 113:
				selectclipboard = 3;
				bartext = "Click to Paste";
				return;
			case 114:
				SelectionColor = JColorChooser.showDialog(
	                     this,
	                     "Choose Selection Color",
	                     new Color(0x00FFFFFF));
				return;
			}
	}

	public boolean constructlevel()
	{

		JFileChooser fd = new JFileChooser();
		fd.setFileFilter(new FileFilter(){
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				if (f.getName().endsWith(".circuit")) return true;
				return false;
			}
			@Override
			public String getDescription() {
				return ".circuit";
			}
		});
		fd.setVisible(true);
		fd.showOpenDialog(this);
		File infile = fd.getSelectedFile();
		if (infile == null) return false;
		int[] simdata = new int[(int) infile.length()];
		FileInputStream fstr;
		try {
			fstr = new FileInputStream(infile);
		} catch (FileNotFoundException e) {
			return false;
		}
		int rbyte = -1;
		while (true)
		{
			try {
				rbyte = fstr.read();
				if (rbyte == -1) break;
				simdata[((int) fstr.getChannel().position()) - 1] = rbyte;
			} catch (IOException e) {break;}
		}
		try {
			fstr.close();
		} catch (IOException e) {
			return false;
		}
		for (int i = 0; i < simdata.length; i++)
		{
			if (i == 0) this.xsize = simdata[i];
			else if (i == 1){
				this.ysize = simdata[i];
				initArrays();
			}
			else
			{
				if (i % 2 == 0)
				{
					int x = ((i - 2)/2) % xsize;
					int y  = ((i - 2)/2) / xsize;
					particles[x][y] = (byte) simdata[i];
				}
				else
				{
					int x = ((i - 3)/2) % xsize;
					int y  = ((i - 3)/2) / xsize;
					data[x][y] = (byte) simdata[i];
				}
			}
		}
		return true;
	}

	public boolean exportLevel()
	{
		JFileChooser fd = new JFileChooser();
		fd.setFileFilter(new FileFilter(){
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				if (f.getName().endsWith(".circuit")) return true;
				return false;
			}
			@Override
			public String getDescription() {
				return ".circuit";
			}
		});
		fd.showSaveDialog(this);
		File outfile = fd.getSelectedFile();
		if (outfile == null) return false;
		FileOutputStream lvlfile;
		try {
			lvlfile = new FileOutputStream(outfile);
		} catch (FileNotFoundException e) {
			return false;
		}
		try
		{
			lvlfile.write(this.xsize);
			lvlfile.write(this.ysize);
			for (int y = 0; y < ysize; y++)
			{
				for (int x = 0; x < xsize; x++)
				{
					lvlfile.write((byte) particles[x][y]);
					lvlfile.write((byte) data[x][y]);
				}
			}
			lvlfile.close();
		}
		catch (IOException e) {return false;}
		return true;
	}

}

class Elem
{
	public static final int NONE = 0; //cannot_flood_fill
	public static final int METAL = 1; //can go to: crossing, via, JK_SWITCH
	public static final int P_SILICON = 2; //can go to: Nsilicon, PNP transistor, via
	public static final int N_SILICON = 3; //can go to: NPN transistor, via
	public static final int CROSSING = 4; //can go to: metal
	public static final int VIA = 5; //can go to: metal, Psilicon, Nsilicon
	public static final int POWER_SRC = 6; //cannot_flood_fill
	public static final int NPN_TRANSISTOR = 7; //can go to: Nsilicon
	public static final int PNP_TRANSISTOR = 8; //can go to: Psilicon
	public static final int JK_FLIPFLOP = 9; //can go to: metal
	public static final int WALL = 10;
	public static final int OUTPUT = 11;
	public static final int INPUT = 12; //
	
	public static final String[] names = {
		"Eraser",
		"Metal",
		"P-Type Silicon",
		"N-Type Silicon",
		"Wire Crossing",
		"Via",
		"Power Source",
		"NPN Transistor",
		"PNP Transistor",
		"Switch",
		"Wall",
		"Output",
		"Input"
	};
}

class CustActionListener implements ActionListener
{
	int id;
	Sim window;
	public CustActionListener(Sim w, int initid)
	{
		id = initid;
		window = w;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		window.notify(this.id);
	}
	
}

class CustomCanvas extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3373524382427720158L;
	Sim parent;
	@Override
	public void paintComponent(Graphics gphx)
	{
		try
		{
		Graphics g = parent.display.getGraphics();
		parent.render((Graphics2D) g);
		gphx.drawImage(parent.display, 0, 0, parent);
		}catch(NullPointerException e){}
	}
	
	public CustomCanvas(Sim s)
	{
		parent = s;
	}
}

class FieldOptionsWindow extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2159879554704450610L;
	Sim parent;
	JLabel PixelSizeLabel;
	JScrollBar PixelSizeScrollbar;
	JCheckBox GridEnabled;
	public FieldOptionsWindow(Sim s)
	{
		this.setLayout(new GridLayout(6, 1));
		GridEnabled = new JCheckBox("Enable Grid");
		GridEnabled.setSelected(true);
		this.add(GridEnabled);
		PixelSizeLabel = new JLabel("Pixel Size: 8");
		this.add(PixelSizeLabel);
		PixelSizeScrollbar = new JScrollBar(JScrollBar.HORIZONTAL, 8, 10, 1, 74);
		this.add(PixelSizeScrollbar);
		JButton BGcolor = new JButton("Set Background Color");
		BGcolor.addActionListener(new CustActionListener(s, 100));
		this.add(BGcolor);
		JButton Gridcolor = new JButton("Set Grid Color");
		Gridcolor.addActionListener(new CustActionListener(s, 101));
		this.add(Gridcolor);
		JButton Selectioncolor = new JButton("Set Selection Color");
		Selectioncolor.addActionListener(new CustActionListener(s, 114));
		this.add(Selectioncolor);
		this.setSize(200, 175);
		this.setTitle("Playing Field Options");
		this.setVisible(false);
		parent = s;
	}
}

class TPSWindow extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4034724070015602098L;
	Sim parent;
	JLabel TPSLabel;
	JScrollBar TPSScrollbar;
	public TPSWindow(Sim s)
	{
		this.setLayout(new GridLayout(2, 1));
		TPSLabel = new JLabel("Ticks Per Second: 60");
		this.add(TPSLabel);
		TPSScrollbar = new JScrollBar(JScrollBar.HORIZONTAL, 60, 10, 1, 70);
		this.add(TPSScrollbar);
		this.setSize(200, 75);
		this.setTitle("Edit Ticks Per Second");
		this.setVisible(false);
		parent = s;
	}
}

class NewLevelWindow extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3044148880077517186L;
	JScrollBar XScroll;
	JLabel XLabel;
	JScrollBar YScroll;
	JLabel YLabel;
	public NewLevelWindow(Sim s)
	{
		this.setLayout(new GridLayout(5, 1));
		XLabel = new JLabel("Width: 64");
		this.add(XLabel);
		XScroll = new JScrollBar(JScrollBar.HORIZONTAL, 64, 10, 1, 522);
		this.add(XScroll);
		YLabel = new JLabel("Height: 64");
		this.add(YLabel);
		YScroll = new JScrollBar(JScrollBar.HORIZONTAL, 64, 10, 1, 522);
		this.add(YScroll);
		JButton b = new JButton("Done");
		b.addActionListener(new CustActionListener(s, 108));
		this.add(b);
		this.setSize(200, 150);
		this.setVisible(false);
	}
}