import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;


public class Game extends Frame implements Runnable, MouseListener, WindowListener, MouseMotionListener, ComponentListener
{
	private static final long serialVersionUID = 1L;
	//gxCanvas field;
	Pipe[][] pipes;
	Pipe[][] defaultpipes;
	List<Point> inputs;
	List<Point> outputs;
	Image i;
	int xsize;
	int ysize;
	int xoffset;
	int yoffset;
	String lvlname;
	String filename = "";
	String dir;
	MenuBar menu;
	Label displayname;
	TextField displayeditname;
	int mouseX = 0;
	int mouseY = 0;
	boolean constructing = false;
	boolean mousepressed = false;
	boolean autosave = true;
	boolean won;
	boolean lvleditor;
	boolean level = false;
	CheckboxMenuItem editoroption;
	CheckboxMenuItem autosaveoption;

	public static void main(String[] args)
	{
		Game instance = new Game();
		instance.run();
	}
	
	public Game()
	{
		this.addMouseListener(this);
		this.addWindowListener(this);
		this.addMouseMotionListener(this);
		this.addComponentListener(this);
		lvleditor = false;
		lvlname = "New level";
		won = false;
		displayeditname = new TextField(".");
		displayname = new Label();
		displayname.setAlignment(Label.CENTER);
		displayname.setText("Choose a file");
		//displayname.setFont(new Font("Arial", Font.BOLD, 18));
		inputs = new ArrayList<Point>();
		outputs = new ArrayList<Point>();
		
		menu = new MenuBar();
		
		Menu m1 = new Menu("Level");
		MenuItem open = new MenuItem("Load level");
		open.addActionListener(new MenuListener(0, this));
		MenuItem newl = new MenuItem("New level");
		newl.addActionListener(new MenuListener(2, this));
		MenuItem savel = new MenuItem("Save level");
		savel.addActionListener(new MenuListener(3, this));
		m1.add(open);
		m1.add(newl);
		m1.add(savel);
		
		Menu m2 = new Menu("Game");
		MenuItem restart = new MenuItem("Restart");
		restart.addActionListener(new MenuListener(10, this));
		MenuItem quit = new MenuItem("Exit game");
		quit.addActionListener(new MenuListener(11, this));
		autosaveoption = new CheckboxMenuItem("Toggle autosave");
		autosaveoption.addItemListener(new CustItemListener(12, this));
		autosaveoption.setState(true);
		m2.add(restart);
		m2.add(quit);
		m2.add(autosaveoption);
		
		Menu m3 = new Menu("Editor");
		editoroption = new CheckboxMenuItem("Toggle level editor");
		editoroption.addItemListener(new CustItemListener(20, this));
		//MenuItem save = new MenuItem("Export");
		//save.addActionListener(new MenuListener(1, this));
		m3.add(editoroption);
		//m3.add(save);
		editoroption.setEnabled(false);
		
		menu.add(m1);
		menu.add(m2);
		menu.add(m3);
		
		this.setMenuBar(menu);
		xsize = 15;
		ysize = 15;
		this.setSize(xsize * 16, ysize * 16);
		this.setLayout(new BorderLayout());
		this.add(displayname, BorderLayout.SOUTH);
		this.pipes = new Pipe[xsize][ysize];
		for (int x = 0; x < xsize; x++)
			for (int y = 0; y < ysize; y++)
				pipes[x][y] = new Pipe(Pipe.UP, (int) Math.floor(Math.random() * 8), 0, false);
		pipes[7][7] = new Pipe(Pipe.UP, Pipe.CORNER, 1, false);
		System.out.println(pipes[7][7].orient + " " + pipes[7][7].locked + " " + pipes[7][7].type);
		this.defaultpipes = clonePipeArr(this.pipes, xsize, ysize);
		updatePipeIO();
		this.setVisible(true);
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		xoffset = ((this.getWidth() - (this.getInsets().left + this.getInsets().right)) / 2 + this.getInsets().left) - xsize * 16 / 2;
		yoffset = ((this.getHeight() - (this.getInsets().top + this.getInsets().bottom)) / 2 + this.getInsets().top / 2) - ysize * 16 / 2;
		i = createImage(this.getWidth(), this.getHeight());
	}
	
	public boolean importlevel()
	{

		FileDialog fd = new FileDialog(this, "Choose a level", FileDialog.LOAD);
		fd.setFile("*.lvl");
		fd.setVisible(true);
		if (fd.getFile() == null)
			return false;
		String filename = fd.getDirectory() + fd.getFile();
		File infile = null;
		infile = new File(filename);
		int[] data = new int[(int) infile.length()];
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
				data[((int) fstr.getChannel().position()) - 1] = rbyte;
			} catch (IOException e) {break;}
		}
		try {
			fstr.close();
		} catch (IOException e) {
			return false;
		}
		filename = fd.getDirectory() + fd.getFile().split("\\.")[0] + ".save";
		this.defaultpipes = clonePipeArr(this.pipes, xsize, ysize);
		infile = new File(filename);
		int[] rotdata = null;
		if (infile.exists())
		{
			rotdata = new int[(int) infile.length()];
			try {
				fstr = new FileInputStream(infile);
			} catch (FileNotFoundException e) {
				return false;
			}
			rbyte = -1;
			while (true)
			{
				try {
					rbyte = fstr.read();
					if (rbyte == -1) break;
					rotdata[((int) fstr.getChannel().position()) - 1] = rbyte;
				} catch (IOException e) {break;}
			}
			try {
				fstr.close();
			} catch (IOException e) {
				return false;
			}
		}
		won = false;
		this.lvlname = "";
		int strlen = 0;
		for (int i = 0; i < data.length; i++)
		{
			if (i == 0) this.xsize = data[i];
			else if (i == 1) this.ysize = data[i];
			else if (i == 2) strlen = data[i];
			else if (i > 2 && i <= 2 + strlen)
			{
				lvlname = lvlname + (char) data[i];
			}
			else
			{
				int currentbyte = data[i];
				int shape = (currentbyte & 7) / 1;
				int orientation = (currentbyte & 192) / 64;
				int type = (currentbyte & 24) / 8;
				int locked = (currentbyte & 32) / 32;
				int x = (i - 3 - strlen) % xsize;
				int y  = (i - 3 - strlen) / ysize;
				pipes[x][y] = new Pipe(orientation, shape, type, locked == 1);

			}
			if (i == 2)
			{
				if (data.length != (data[0] * data[1] + data[2] + 3))
				{
					return false;
				}
				this.pipes = new Pipe[data[0]][data[1]];
			}
		}

		int datalen = this.xsize * this.ysize;
		if (infile.exists())
		{
			for (int i = 0; i < rotdata.length; i++)
			{
				if (i == 0) {
					if (this.xsize != rotdata[i])
						break;
				} else if (i == 1) {
					if (this.ysize != rotdata[i])
						break;
				} else if (i == 2) {
					if (rotdata[i] != 0)
						this.won = true;
				} else {
					int[] splitbyte = new int[4];
					splitbyte[0] = (rotdata[i] & 192) / 64;
					splitbyte[1] = (rotdata[i] & 48) / 16;
					splitbyte[2] = (rotdata[i] & 12) / 4;
					splitbyte[3] = (rotdata[i] & 3) / 1;
					for (int oi = 0; oi < 4; oi++)
					{
						int x = (oi + (i - 3) * 4) % xsize;
						int y  = (oi + (i - 3) * 4) / ysize;
						if ((oi + (i - 3) * 4) < datalen)
							pipes[x][y].orient = splitbyte[oi];
					}
				}
			}
		}
		this.filename = fd.getFile().split("\\.")[0];
		this.dir = fd.getDirectory();
		this.setSize(xoffset * 2 + xsize * 16, yoffset * 2 + ysize * 16);
		this.displayname.setText(this.lvlname);
		this.updatePipeIO();
		this.level = true;
		editoroption.setEnabled(false);
		return true;
	}
	
	public boolean exportLevel()
	{
		FileDialog fd = new FileDialog(this, "Save as", FileDialog.SAVE);
		fd.setFile("untitled.lvl");
		fd.setVisible(true);
		if (fd.getFile() == null)
			return false;
		String filename = fd.getDirectory() + fd.getFile();
		FileOutputStream lvlfile;
		try {
			lvlfile = new FileOutputStream(filename);
		} catch (FileNotFoundException e) {
			return false;
		}
		this.filename = fd.getFile().split("\\.")[0];
		this.dir = fd.getDirectory();
		this.level = true;
		this.defaultpipes = clonePipeArr(this.pipes, xsize, ysize);
		try
		{
			lvlfile.write(this.xsize);
			lvlfile.write(this.ysize);
			lvlfile.write(this.lvlname.length());
			for (int i = 0; i < this.lvlname.length(); i++)
			{
				lvlfile.write((int) this.lvlname.charAt(i));
			}
			for (int y = 0; y < ysize; y++)
			{
				for (int x = 0; x < xsize; x++)
				{
					lvlfile.write(pipes[x][y].shape * 1 | pipes[x][y].orient * 64 | pipes[x][y].type * 8 | Game.boolToInt(pipes[x][y].locked) * 32);
				}
			}
			lvlfile.close();
		}
		catch (IOException e) {return false;}
		return true;
	}
	
	public boolean saveLevel()
	{
		FileOutputStream lvlfile;
		try {
			lvlfile = new FileOutputStream(this.dir + this.filename + ".save");
		} catch (FileNotFoundException e) {
			return false;
		}
		try
		{
			lvlfile.write(this.xsize);
			lvlfile.write(this.ysize);
			lvlfile.write(Game.boolToInt(this.won));
			int counter = 0;
			for (int y = 0; y < ysize; y++)
			{
				for (int x = 0; x < xsize; x++)
				{
					if(counter % 4 == 0)
					{
						int[] splitbyte = new int[4];
						for (int oi = 0; oi < 4; oi++)
						{
							if (counter + oi < xsize * ysize)
								splitbyte[oi] = pipes[(counter + oi) % xsize][(counter + oi) / ysize].orient;
							else
								splitbyte[oi] = 0;
						}
						lvlfile.write(splitbyte[0] * 64 | splitbyte[1] * 16 | splitbyte[2] * 4 | splitbyte[3] * 1);
					}
					counter++;
				}
			}
			lvlfile.close();
		}
		catch (IOException e) {return false;}
		return true;
	}
	
	@Override
    public void run() {
		while (true) {
			try {
				Thread.sleep(20);
			} catch(InterruptedException e) {}
			if (!constructing)
			{
				for (int x = 0; x < xsize; x++)
					for (int y = 0; y < ysize; y++)
						pipes[x][y].water = 0;
				if (!lvleditor)
				{

					this.updatePipeIO();
					for (Point p : this.inputs)
						for (int o : pipes[p.x][p.y].getToPipes(Pipe.NO_ORIENTATION))
							this.waterFloodFill(p.x, p.y, o);
					boolean lost = false;
					for (Point p : this.outputs)
						if (pipes[p.x][p.y].water == 0)
							lost = true;
					if (!lost && this.outputs.size() > 0) won = true;
					if (won)
					{
						this.editoroption.setEnabled(true);
						this.displayname.setText("YOU WON!");
					}
				}
				repaint();
			}
		}
	}
	
	public void waterFloodFill(int x, int y, int o)
	{
		if (pipes[x][y].shape == Pipe.BRIDGE)
			if ((o == Pipe.UP || o == Pipe.DOWN) && (pipes[x][y].orient == Pipe.UP || pipes[x][y].orient == Pipe.DOWN) || (o == Pipe.LEFT || o == Pipe.RIGHT) && (pipes[x][y].orient == Pipe.LEFT || pipes[x][y].orient == Pipe.RIGHT))
				pipes[x][y].water = pipes[x][y].water | 1;
			else
				pipes[x][y].water = pipes[x][y].water | 2;
		else if (pipes[x][y].shape == Pipe.DOUBLE)
			if ((o == Pipe.UP || o == Pipe.RIGHT) && pipes[x][y].orient == Pipe.UP || (o == Pipe.UP || o == Pipe.LEFT) && pipes[x][y].orient == Pipe.LEFT || (o == Pipe.LEFT || o == Pipe.DOWN) && pipes[x][y].orient == Pipe.DOWN || (o == Pipe.DOWN || o == Pipe.RIGHT) && pipes[x][y].orient == Pipe.RIGHT)
				pipes[x][y].water = pipes[x][y].water | 1;
			else
				pipes[x][y].water = pipes[x][y].water | 2;
		else
			pipes[x][y].water = 1;
		for (int i : pipes[x][y].getToPipes(o))
		{
			if (getAdjacent(x, y, i) != null && getAdjacent(x, y, i).getFromPipe(Pipe.getOppositeRotate(i)) == true)
			{
				int ax = 0;
				if (i == Pipe.LEFT) ax = -1;
				else if (i == Pipe.RIGHT) ax = 1;
				int ay = 0;
				if (i == Pipe.UP) ay = -1;
				else if (i == Pipe.DOWN) ay = 1;
				waterFloodFill(x + ax, y + ay, Pipe.getOppositeRotate(i));
			}
		}
	}
	
	public Pipe getAdjacent(int x, int y, int o)
	{
		switch (o)
		{
		case Pipe.UP:
			if (y - 1 >= 0)
				return pipes[x][y - 1];
			break;
		case Pipe.DOWN:
			if (y + 1 < ysize)
				return pipes[x][y + 1];
			break;
		case Pipe.LEFT:
			if (x - 1 >= 0)
				return pipes[x - 1][y];
			break;
		case Pipe.RIGHT:
			if (x + 1 < xsize)
				return pipes[x + 1][y];
			break;
		default:
			break;
		}
		return null;
	}
	
	@Override
	public void update(Graphics realg)
	{
		Graphics g = i.getGraphics();
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(new Font("Arial", Font.BOLD, 18));
		g.fillRect(0, 0, i.getWidth(this), i.getHeight(this));
		int mx = (this.mouseX - xoffset) / 16;
		int my = (this.mouseY - yoffset) / 16;
		for (int x = 0; x < xsize; x++)
			for (int y = 0; y < ysize; y++)
				pipes[x][y].render(g, 16 * x + xoffset, 16 * y + yoffset, this.lvleditor, mx == x && my == y, this.mousepressed);
		g.setColor(Color.BLACK);
		realg.drawImage(i, 0, 0, this);
	}

	public void updatePipeIO()
	{
		this.inputs = new ArrayList<Point>();
		this.outputs = new ArrayList<Point>();
		for (int y = 0; y < ysize; y++)
			for (int x = 0; x < xsize; x++)
				if (pipes[x][y].type == Pipe.INPUT)
				{
					this.inputs.add(new Point(x, y));
					//System.out.println("INPUT " + x + ", " + y);
					//System.out.println(pipes[x][y].orient + " " + pipes[x][y].locked + " " + pipes[x][y].type);
				}
				else if (pipes[x][y].type == Pipe.OUTPUT)
				{
					this.outputs.add(new Point(x, y));
					//System.out.println("OUTPUT " + x + ", " + y);
				}
	}
	
	private Pipe[][] clonePipeArr(Pipe[][] from, int xl, int yl)
	{
		Pipe[][] to = new Pipe[xl][yl];
		for (int x = 0; x < xl; x++)
			for (int y = 0; y < xl; y++)
				to[x][y] = from[x][y].Clone();
		return to;
	}
	
	public void listenerCalled(int id)
	{
		System.out.println("asdfdaf" + id);
		switch (id) {
		case 0:
			constructing = true;
			if (!this.importlevel())
				this.displayname.setText("Invalid or corrupt file");
			constructing = false;
			break;
		case 1:
			constructing = true;
			if (!this.exportLevel())
				this.displayname.setText("Invalid file");
			constructing = false;
			break;
		case 2:
			int xl = 16;
			int yl = 16;
			String s = (String) JOptionPane.showInputDialog(
					this, "Input the number of columns", "X length selector", JOptionPane.PLAIN_MESSAGE, null, null, "16");
			if (s == null) return;
			try {
				xl = Integer.valueOf(s);
			} catch(NumberFormatException e) {}
			s = (String) JOptionPane.showInputDialog(
					this, "Input the number of rows", "Y length selector", JOptionPane.PLAIN_MESSAGE, null, null, "16");
			if (s == null) return;
			try {
				yl = Integer.valueOf(s);
			} catch(NumberFormatException e) {}
			constructing = true;
			this.xsize = xl;
			this.ysize = yl;
			this.pipes = new Pipe[xl][yl];
			won = false;
			inputs = new ArrayList<Point>();
			outputs = new ArrayList<Point>();
			this.setSize(xoffset * 2 + xsize * 16, yoffset * 2 + ysize * 16);
			setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
			this.lvlname = "New Level";
			for (int y = 0; y < ysize; y++)
				for (int x = 0; x < xsize; x++)
					pipes[x][y] = new Pipe(Pipe.NO_ORIENTATION, Pipe.NO_SHAPE, Pipe.NO_TYPE, false);
			this.displayname.setText(this.lvlname);
			this.displayeditname.setText(this.lvlname);
			this.remove(this.displayname);
			this.add(displayeditname, BorderLayout.SOUTH);
			//this.revalidate();
			this.lvleditor = true;
			this.updatePipeIO();
			editoroption.setEnabled(true);
			this.editoroption.setState(true);
			this.defaultpipes = clonePipeArr(this.pipes, xsize, ysize);
			this.level = false;
			constructing = false;
			break;
		case 3:
			System.out.println("aaa");
			if (filename != "") {
				this.saveLevel();
			}
			else {
				constructing = true;
				if (!this.exportLevel())
					this.displayname.setText("Invalid file");
				constructing = false;
			}
			break;
		case 10:
			this.pipes = clonePipeArr(this.defaultpipes, xsize, ysize);
			break;
		case 11:
			System.exit(0);
			break;
		case 12:
			if (!this.autosaveoption.getState())
			{
				this.autosave = false;
			}
			else
			{
				this.autosave = true;
			}
			break;
		case 20:
			if (!this.editoroption.getState())
			{
				this.pipes = this.clonePipeArr(this.pipes, xsize, ysize);
				this.lvlname = this.displayeditname.getText();
				this.remove(this.displayeditname);
				this.add(displayname, BorderLayout.SOUTH);
				this.setVisible(true);
				this.lvleditor = false;
				this.displayname.setText(this.lvlname);
				this.updatePipeIO();
				System.out.println(inputs.size());
				System.out.println(lvleditor);
			}
			else
			{
				this.displayeditname.setText(this.lvlname);
				this.remove(this.displayname);
				this.add(displayeditname, BorderLayout.SOUTH);
				this.setVisible(true);
				this.lvleditor = true;
			}
			break;
		default:
			break;
		}
	}

	private static int boolToInt(boolean b)
	{
		if (b) return 1;
		return 0;
	}

	@SuppressWarnings("unused")
	private void p(String s){System.out.println(s);}
	
	/*************/

	@Override public void windowClosing(WindowEvent event)
	{
		if (this.autosave)
			this.saveLevel();
		System.exit(0);
	}
	
	@Override public void mousePressed(MouseEvent event)
	{
		mousepressed = true;
	}
	@Override public void mouseDragged(MouseEvent event)
	{
		this.mouseX = event.getX();
		this.mouseY = event.getY();
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		this.mouseX = event.getX();
		this.mouseY = event.getY();
	}
	
	@Override
	public void componentResized(ComponentEvent event) {
		
		xoffset = ((this.getWidth() - (this.getInsets().left + this.getInsets().right)) / 2 + this.getInsets().left) - xsize * 16 / 2;
		yoffset = ((this.getHeight() - (this.getInsets().top + this.getInsets().bottom)) / 2 + this.getInsets().top / 2) - ysize * 16 / 2;
	}
	
	@Override public void mouseReleased(MouseEvent event)
	{
		mousepressed = false;
		if (this.mouseX > xoffset & this.mouseX < xsize * 16 + xoffset & this.mouseY > yoffset & this.mouseY < ysize * 16 + yoffset)
		{
			int x = (this.mouseX - xoffset) / 16;
			int y = (this.mouseY - yoffset) / 16;
			if ((event.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0) {
				if (event.getButton() ==  MouseEvent.BUTTON1) {
					if (this.lvleditor)
						pipes[x][y].type = Pipe.getTypeRotate(pipes[x][y].type, (event.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0);
				} else if (this.lvleditor)
					if (this.lvleditor)
						pipes[x][y].locked = !pipes[x][y].locked;
			} else
				if (event.getButton() ==  MouseEvent.BUTTON1) {
					if (!pipes[x][y].locked) {
						if ((event.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0)
							pipes[x][y].orient = Pipe.getRightRotate(pipes[x][y].orient);
						else
							pipes[x][y].orient = Pipe.getLeftRotate(pipes[x][y].orient);
				}} else if (event.getButton() ==  MouseEvent.BUTTON3)
					if (this.lvleditor)
						pipes[x][y].shape = Pipe.getShapeRotate(pipes[x][y].shape, (event.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0);
		}
    }

	@Override public void windowActivated(WindowEvent event) {}
	@Override public void windowClosed(WindowEvent event) {}
	@Override public void windowDeactivated(WindowEvent event) {}
	@Override public void windowDeiconified(WindowEvent event) {}
	@Override public void windowIconified(WindowEvent event) {}
	@Override public void windowOpened(WindowEvent event) {}
	@Override public void mouseClicked(MouseEvent evt) {}
	@Override public void mouseEntered(MouseEvent event) {}
	@Override public void mouseExited(MouseEvent event) {}
	@Override public void componentHidden(ComponentEvent event) {}
	@Override public void componentMoved(ComponentEvent event) {}
	@Override public void componentShown(ComponentEvent event) {}
}

class Point
{
	int x;
	int y;
	public Point (int ix, int iy) {x = ix; y = iy;}
}

class MenuListener implements ActionListener
{
	Game holder;
	int evtID;

	public MenuListener(int id, Game instance)
	{
		evtID = id;
		holder = instance;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		holder.listenerCalled(evtID);
	}
}

class CustItemListener implements ItemListener
{
	Game held;
	int evtID;

	public CustItemListener(int id, Game m)
	{
		evtID = id;
		held = m;
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		held.listenerCalled(evtID);
	}
}