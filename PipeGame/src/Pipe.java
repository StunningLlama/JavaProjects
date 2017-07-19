import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;


public class Pipe implements Cloneable{

	/*public enum PipeOrientation {
		NONE, UP, DOWN, LEFT, RIGHT
	}
	
	public enum PipeShape {
		NONE, LINE, CORNER, T, CROSS, BRIDGE, DOUBLE
	}*/
	
	/**************************/
	static final int NO_TYPE = 0;
	static final int REGULAR = 0;
	static final int INPUT = 1;
	static final int OUTPUT = 2;
	static final int MAX_TYPE = 2;
	
	static final int NO_ORIENTATION = 0;
	static final int UP = 0;
	static final int DOWN = 1;
	static final int LEFT = 2;
	static final int RIGHT = 3;
	static final int MAX_ORIENTATION = 3;

	static final int NO_SHAPE = 0;
	static final int LINE = 1; // |
	static final int CORNER = 2; // L
	static final int T = 3; // T
	static final int CROSS = 4; // +
	static final int BRIDGE = 5; // -|-
	static final int DOUBLE = 6; // ><
	static final int CAP = 7; // o-
	static final int MAX_SHAPE = 7;
	
	static final int COLOR_WHITE = 0;
	static final int COLOR_BLACK = 1;
	static final int COLOR_BLUE = 2;
	static final int COLOR_RED = 3;
	static final int COLOR_GREEN = 4;
	static final int COLOR_DARKGRAY = 5;
	static final int COLOR_MOUSE_HOVER = 6;
	static final int COLOR_MOUSE_PRESSED = 7;
	/***************************/

	int orient;
	int shape;
	int water;
	int type;
	boolean locked;

	public Pipe Clone() {try {return (Pipe) super.clone();}
	catch (CloneNotSupportedException e) {return null;}}
	
	public Pipe(int pipeOrientation, int pipeShape, int pipeType, boolean pipeLocked) throws IllegalArgumentException
	{
		if (pipeOrientation < NO_ORIENTATION || pipeOrientation > MAX_ORIENTATION || pipeShape < NO_SHAPE || pipeShape > MAX_SHAPE || pipeType < NO_TYPE || pipeType > MAX_TYPE)
			throw new IllegalArgumentException();
		if (pipeType != REGULAR && (pipeShape == BRIDGE || pipeShape == DOUBLE))
			this.shape = CROSS;
		else
			this.shape = pipeShape;
		this.orient = pipeOrientation;
		this.type = pipeType;
		this.water = 0;
		this.locked = pipeLocked;
	}
	
	private void drawPixel(Graphics g, int c, int x, int y)
	{
		if (c == COLOR_WHITE) g.setColor(Color.WHITE);
		if (c == COLOR_BLACK) g.setColor(Color.BLACK);
		if (c == COLOR_BLUE) g.setColor(Color.BLUE);
		if (c == COLOR_RED) g.setColor(Color.RED);
		if (c == COLOR_GREEN) g.setColor(Color.GREEN);
		if (c == COLOR_DARKGRAY) g.setColor(Color.DARK_GRAY);
		if (c == COLOR_MOUSE_HOVER) g.setColor(new Color(223, 223, 223));
		if (c == COLOR_MOUSE_PRESSED) g.setColor(new Color(191, 191, 191));
		g.drawRect(x, y, 1, 1);
	}
	
	public void render(Graphics g, int x, int y, boolean dontRenderWater, boolean hover, boolean press)
	{
		int[][] pixels = new int[16][16];
		int wcolor = COLOR_WHITE;
		int bcolor = COLOR_WHITE;
		if ((water != 0 || type == INPUT) && !dontRenderWater)
			wcolor = COLOR_BLUE;
		if (hover)
		{
			bcolor = COLOR_MOUSE_HOVER;
			if (press)
			{
				bcolor = COLOR_MOUSE_PRESSED;
			}
		}
		if (this.locked)
			bcolor = COLOR_DARKGRAY;
		for (int xp = 0; xp < 16; xp++)
			for (int yp = 0; yp < 16; yp++)
				pixels[xp][yp] = bcolor;
		switch (this.shape)
		{
		case NO_SHAPE:
			break;
		case CAP:
			for (int xp = 7; xp <= 8; xp++) for (int yp = 0; yp <= 9; yp++) pixels[xp][yp] = wcolor;
			for (int yp = 0; yp <= 9; yp++) pixels[9][yp] = COLOR_BLACK; for (int yp = 0; yp <= 9; yp++) pixels[6][yp] = COLOR_BLACK; for (int xp = 7; xp <= 8; xp++) pixels[xp][9] = COLOR_BLACK;
			break;
		case LINE:
			for (int xp = 7; xp <= 8; xp++) for (int yp = 0; yp <= 15; yp++) pixels[xp][yp] = wcolor;
			for (int yp = 0; yp <= 15; yp++) pixels[6][yp] = COLOR_BLACK; for (int yp = 0; yp <= 15; yp++) pixels[9][yp] = COLOR_BLACK;
			break;
		case CORNER:
			for (int xp = 7; xp <= 8; xp++) for (int yp = 0; yp <= 9; yp++) pixels[xp][yp] = wcolor;
			for (int xp = 0; xp <= 9; xp++) for (int yp = 7; yp <= 8; yp++) pixels[xp][yp] = wcolor;
			for (int yp = 0; yp <= 6; yp++) pixels[6][yp] = COLOR_BLACK; for (int xp = 0; xp <= 6; xp++) pixels[xp][6] = COLOR_BLACK;
			for (int yp = 0; yp <= 9; yp++) pixels[9][yp] = COLOR_BLACK; for (int xp = 0; xp <= 9; xp++) pixels[xp][9] = COLOR_BLACK;
			break;
		case T:
			for (int xp = 0; xp <= 15; xp++) for (int yp = 7; yp <= 8; yp++) pixels[xp][yp] = wcolor;
			for (int xp = 7; xp <= 8; xp++) for (int yp = 0; yp <= 9; yp++) pixels[xp][yp] = wcolor;
			for (int yp = 0; yp <= 6; yp++) pixels[6][yp] = COLOR_BLACK; for (int xp = 0; xp <= 6; xp++) pixels[xp][6] = COLOR_BLACK;
			for (int yp = 0; yp <= 6; yp++) pixels[9][yp] = COLOR_BLACK; for (int xp = 10; xp <= 15; xp++) pixels[xp][6] = COLOR_BLACK;
			for (int xp = 0; xp <= 15; xp++) pixels[xp][9] = COLOR_BLACK;
			break;
		case CROSS:
			for (int xp = 7; xp <= 8; xp++) for (int yp = 0; yp <= 15; yp++) pixels[xp][yp] = wcolor;
			for (int xp = 0; xp <= 15; xp++) for (int yp = 7; yp <= 8; yp++) pixels[xp][yp] = wcolor;
			for (int yp = 0; yp <= 6; yp++) pixels[6][yp] = COLOR_BLACK; for (int xp = 0; xp <= 6; xp++) pixels[xp][6] = COLOR_BLACK;
			for (int yp = 0; yp <= 6; yp++) pixels[9][yp] = COLOR_BLACK; for (int xp = 9; xp <= 15; xp++) pixels[xp][6] = COLOR_BLACK;
			for (int yp = 9; yp <= 15; yp++) pixels[6][yp] = COLOR_BLACK; for (int xp = 0; xp <= 6; xp++) pixels[xp][9] = COLOR_BLACK;
			for (int yp = 9; yp <= 15; yp++) pixels[9][yp] = COLOR_BLACK; for (int xp = 9; xp <= 15; xp++) pixels[xp][9] = COLOR_BLACK;
			break;
		case BRIDGE:
			int colorV = COLOR_WHITE;
			int colorH = COLOR_WHITE;
			if (isHorizontalWater()) colorH = COLOR_BLUE;
			if (isVerticalWater()) colorV = COLOR_BLUE;
			for (int xp = 0; xp <= 15; xp++) pixels[xp][6] = COLOR_BLACK; for (int xp = 0; xp <= 15; xp++) pixels[xp][9] = COLOR_BLACK;
			for (int xp = 0; xp <= 15; xp++) for (int yp = 7; yp <= 8; yp++) pixels[xp][yp] = colorH;
			for (int xp = 7; xp <= 8; xp++) for (int yp = 0; yp <= 15; yp++) pixels[xp][yp] = colorV;
			for (int yp = 0; yp <= 15; yp++) pixels[6][yp] = COLOR_BLACK; for (int yp = 0; yp <= 15; yp++) pixels[9][yp] = COLOR_BLACK;
			break;
		case DOUBLE:
			int colorTR = COLOR_WHITE;
			int colorBL = COLOR_WHITE;
			if (isTopRightWater()) colorTR = COLOR_BLUE;
			if (isBottomLeftWater()) colorBL = COLOR_BLUE;
			for (int xp = 7; xp <= 8; xp++) for (int yp = 0; yp <= 3; yp++) pixels[xp][yp] = colorTR;
			for (int dp = 0; dp < 4; dp++) {pixels[dp + 8][dp + 4] = colorTR; pixels[dp + 9][dp + 3] = colorTR; pixels[dp + 9][dp + 4] = colorTR;}
			for (int xp = 12; xp <= 15; xp++) for (int yp = 7; yp <= 8; yp++) pixels[xp][yp] = colorTR;
			for (int xp = 7; xp <= 8; xp++) for (int yp = 12; yp <= 15; yp++) pixels[xp][yp] = colorBL;
			for (int dp = 0; dp < 4; dp++) {pixels[dp + 4][dp + 8] = colorBL; pixels[dp + 3][dp + 9] = colorBL; pixels[dp + 4][dp + 9] = colorBL;}
			for (int xp = 0; xp <= 3; xp++) for (int yp = 7; yp <= 8; yp++) pixels[xp][yp] = colorBL;
			/*left up*/ for (int xp = 0; xp <= 3; xp++) pixels[xp][6] = 1; for (int yp = 12; yp <= 15; yp++) pixels[9][yp] = 1; for (int dp = 0; dp < 5; dp++) pixels[dp + 4][dp + 7] = 1;
			/*left down*/ for (int xp = 0; xp <= 2; xp++) pixels[xp][9] = 1; for (int yp = 13; yp <= 15; yp++) pixels[6][yp] = 1; for (int dp = 0; dp < 3; dp++) pixels[dp + 3][dp + 10] = 1;
			/*right up*/ for (int xp = 13; xp <= 15; xp++) pixels[xp][6] = 1; for (int yp = 0; yp <= 2; yp++) pixels[9][yp] = 1; for (int dp = 0; dp < 5; dp++) pixels[dp + 7][dp + 4] = 1;
			/*right down*/ for (int xp = 12; xp <= 15; xp++) pixels[xp][9] = 1; for (int yp = 0; yp <= 3; yp++) pixels[6][yp] = 1; for (int dp = 0; dp < 3; dp++) pixels[dp + 10][dp + 3] = 1;
			break;
		default:
			return;
		}
		
		int boxcolor = bcolor;
		if (type == INPUT)
			boxcolor = COLOR_BLUE;
		else if (type == OUTPUT)
			if (water == 1)
				boxcolor = COLOR_GREEN;
			else
				boxcolor = COLOR_RED;
		
		if (type != REGULAR)
		{
			for (int xp = 5; xp <= 10; xp++) pixels[xp][5] = COLOR_BLACK;
			for (int xp = 5; xp <= 10; xp++) pixels[xp][10] = COLOR_BLACK;
			for (int yp = 5; yp <= 10; yp++) pixels[5][yp] = COLOR_BLACK;
			for (int yp = 5; yp <= 10; yp++) pixels[10][yp] = COLOR_BLACK;
			for (int xp = 6; xp <= 9; xp++) for (int yp = 6; yp <= 9; yp++) pixels[xp][yp] = boxcolor;
		}
		
		switch (this.orient)
		{
		case UP:
			for (int xp = 0; xp <= 15; xp++)
				for (int yp = 0; yp <= 15; yp++)
					this.drawPixel(g, pixels[xp][yp], x + xp, y + yp);
			break;
		case DOWN:
			for (int xp = 0; xp <= 15; xp++)
				for (int yp = 0; yp <= 15; yp++)
					this.drawPixel(g, pixels[15 - xp][15 - yp], x + xp, y + yp);
			break;
		case LEFT:
			for (int xp = 0; xp <= 15; xp++)
				for (int yp = 0; yp <= 15; yp++)
					this.drawPixel(g, pixels[15 - yp][xp], x + xp, y + yp);
			break;
		case RIGHT:
			for (int xp = 0; xp <= 15; xp++)
				for (int yp = 0; yp <= 15; yp++)
					this.drawPixel(g, pixels[yp][15 - xp], x + xp, y + yp);
			break;
		default:
			for (int xp = 0; xp <= 15; xp++)
				for (int yp = 0; yp <= 15; yp++)
					this.drawPixel(g, 1, xp, yp);
		}
	}
	
	public static int getRightRotate(int o)
	{
		switch (o)
		{
		case UP: return RIGHT;
		case LEFT: return UP;
		case RIGHT: return DOWN;
		case DOWN: return LEFT;
		default: return NO_ORIENTATION;
		}
	}
	
	public static int getLeftRotate(int o)
	{
		switch (o)
		{
		case UP: return LEFT;
		case LEFT: return DOWN;
		case RIGHT: return UP;
		case DOWN: return RIGHT;
		default: return NO_ORIENTATION;
		}
	}
	
	public static int getOppositeRotate(int o)
	{
		switch (o)
		{
		case UP: return DOWN;
		case LEFT: return RIGHT;
		case RIGHT: return LEFT;
		case DOWN: return UP;
		default: return NO_ORIENTATION;
		}
	}
	
	public static int getShapeRotate(int o, boolean back)
	{
		if (!back)
			if (o == MAX_SHAPE)
				return NO_SHAPE;
			else
				return o + 1;
		else
			if (o == NO_SHAPE)
				return MAX_SHAPE;
			else
				return o - 1;
	}
	
	public static int getTypeRotate(int o, boolean back)
	{
		if (!back)
			if (o == MAX_TYPE)
				return NO_TYPE;
			else
				return o + 1;
		else
			if (o == NO_TYPE)
				return MAX_TYPE;
			else
				return o - 1;
	}
	
	public Collection<Integer> getToPipes(int o)
	{
		Collection<Integer> returnval = new ArrayList<Integer>();
		switch (this.shape)
		{
		case NO_SHAPE:
			break;
		case CAP:
			returnval.add(orient);
			break;
		case LINE:
			returnval.add(orient);
			returnval.add(getOppositeRotate(orient));
			break;
		case CORNER:
			returnval.add(orient);
			returnval.add(getLeftRotate(orient));
			break;
		case T:
			returnval.add(orient);
			returnval.add(getLeftRotate(orient));
			returnval.add(getRightRotate(orient));
			break;
		case CROSS:
			returnval.add(orient);
			returnval.add(getLeftRotate(orient));
			returnval.add(getRightRotate(orient));
			returnval.add(getOppositeRotate(orient));
			break;
		case BRIDGE:
			returnval.add(getOppositeRotate(o));
			break;
		case DOUBLE:
			if ((o == UP || o == DOWN) && (orient == LEFT || orient == RIGHT) || (o == LEFT || o == RIGHT) && (orient == UP || orient == DOWN))
				returnval.add(getLeftRotate(o));
			else
				returnval.add(getRightRotate(o));
			break;
		default: break;
		}
		if (type != INPUT)
			returnval.remove(o);
		return returnval;
	}
	
	public boolean getFromPipe(int o)
	{
		if (type == INPUT)
			return false;
		if (this.shape != BRIDGE && this.shape != DOUBLE && this.water == 1) return false;
		Collection<Integer> returnval = new ArrayList<Integer>();
		switch (this.shape)
		{
		case NO_SHAPE:
			return false;
		case CAP:
			returnval.add(orient);
			break;
		case LINE:
			returnval.add(orient);
			returnval.add(getOppositeRotate(orient));
			break;
		case CORNER:
			returnval.add(orient);
			returnval.add(getLeftRotate(orient));
			break;
		case T:
			returnval.add(orient);
			returnval.add(getLeftRotate(orient));
			returnval.add(getRightRotate(orient));
			break;
		case CROSS:
			return true;
		case BRIDGE:
			if (isVerticalWater())
				if ((o == UP || o == DOWN) && (orient == UP || orient == DOWN) || (o == LEFT || o == RIGHT) && (orient == LEFT || orient == RIGHT))
					return false;
			if (isHorizontalWater())
				if ((o == UP || o == DOWN) && (orient == LEFT || orient == RIGHT) || (o == LEFT || o == RIGHT) && (orient == UP || orient == DOWN))
					return false;
			return true;
		case DOUBLE:
			if (isTopRightWater()) //top right
				if ((o == UP || o == RIGHT) && orient == UP || (o == UP || o == LEFT) && orient == LEFT || (o == LEFT || o == DOWN) && orient == DOWN || (o == DOWN || o == RIGHT) && orient == RIGHT)
					return false;
			if (isBottomLeftWater()) //bottom left
				if ((o == DOWN || o == LEFT) && orient == UP || (o == DOWN || o == RIGHT) && orient == LEFT || (o == UP || o == RIGHT) && orient == DOWN || (o == UP || o == LEFT) && orient == RIGHT)
					return false;
			return true;
		default: break;
		}
		if (returnval.contains(o)) return true;
		else return false;
	}
	
	private boolean isVerticalWater()
	{
		return (water == 1 || water == 3);
	}
	
	private boolean isTopRightWater()
	{
		return (water == 1 || water == 3);
	}
	
	private boolean isHorizontalWater()
	{
		return (water == 2 || water == 3);
	}
	
	private boolean isBottomLeftWater()
	{
		return (water == 2 || water == 3);
	}
}
