import java.awt.Color;
import java.awt.Graphics;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ConsoleFont {
	
	public static ConsoleFont getFont(String str, boolean external, String dir)
	{
		String fontdir = "resource/font/" + str + "/";
		if (external) fontdir = dir + "/" + str + "/";
		//spec = ConsoleFont.class.getResourceAsStream("resource/font/" + str + "/font.spec");
		System.out.println("Started loading font.");
		long time = System.currentTimeMillis();
		ConsoleFont font = new ConsoleFont();
		font.xsize = 8;
		font.ysize = 12;
		for (int i = 0; i < 256; i++)
		{
			font.maps.add(new Integer[font.xsize][font.ysize]);
		}
		String nonexistent = "Error: Non-existent or corrupt font file for character(s) ";
		boolean err = false;
		for (int i = 0; i < 256; i++)
		{
			for (int x = 0; x < font.xsize; x++)
			{
				for (int y = 0; y < font.ysize; y++)
				{
					font.maps.get(i)[x][y] = 0;
				}
			}
			InputStream fchar;
			if (external)
			{
				try {
					fchar = new FileInputStream(fontdir + "char-" + String.valueOf(i) + ".fnt");
				} catch (FileNotFoundException e) {
					throw new RuntimeException();
				}
			}
			else
			{
				fchar = ConsoleFont.class.getResourceAsStream(fontdir + "char-" + String.valueOf(i) + ".fnt");
			}
			if (fchar == null)
			{
				nonexistent += (i + ", ");
				err = true;
				continue;
			}
			List<Integer> file = new ArrayList<Integer>();
			while (true)
			{
				int b;
				try {
					b = fchar.read();
				} catch (IOException e) {break;}
				if (b == -1) break;
				else
				{
					if (b == 48) file.add(0);
					if (b == 49) file.add(1);
				}
			}
			if (file.size() != font.xsize * font.ysize)
			{
				nonexistent += (i + ", ");
				err = true;
				continue;
			}
			for (int y = 0; y < font.ysize; y++)
			{
				for (int x = 0; x < font.xsize; x++)
				{
					font.maps.get(i)[x][y] = file.get(x + y * font.xsize);
				}
			}
		}
		if (err) System.err.println(nonexistent.substring(0, nonexistent.length() - 2));
		System.out.println("Took " + (System.currentTimeMillis() - time) + " milliseconds to load font.");
		return font;
	}
	
	private List<Integer[][]> maps;
	int xsize;
	int ysize;
	
	private ConsoleFont()
	{
		maps = new ArrayList<Integer[][]>();
	}
	
	public boolean getPixel(int x, int y, int c)
	{
		if (this.maps.get(c)[x][y] == 1) return true;
		return false;
	}
	
	public void render(Graphics g, int x, int y, int c, Color bground, Color fground, boolean cursor)
	{
		g.setColor(bground);
		g.fillRect(x, y, this.xsize, this.ysize);
		g.setColor(fground);
		Integer[][] chr = this.maps.get(c);
		for (int ix = 0; ix < xsize; ix++)
		{
			for (int iy = 0; iy < ysize; iy++)
			{
				if (chr[ix][iy] == 1)
				{
					g.drawRect(x + ix, y + iy, 0, 0);
				}
			}
		}
		if (cursor)
		{
			g.setColor(Terminal.getInvColor(bground));
			g.fillRect(x, y + ysize - 3, xsize, 3);
			g.setColor(Terminal.getInvColor(fground));
			for (int ix = 0; ix < xsize; ix++)
			{
				for (int iy = ysize - 3; iy < ysize; iy++)
				{
					if (chr[ix][iy] == 1)
					{
						g.drawRect(ix + x, iy + y, 0, 0);
					}
				}
			}
		}
	}
	
	public int getWidth()
	{
		return this.xsize;
	}
	
	public int getHeight()
	{
		return this.ysize;
	}
}
