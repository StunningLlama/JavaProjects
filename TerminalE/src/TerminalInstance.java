import java.awt.Frame;
import java.awt.Graphics;


public class TerminalInstance extends Frame {
	private static final long serialVersionUID = 1L;
	Terminal main;
	public TerminalInstance(Terminal t)
	{
		this.main = t;
	}

	@Override
	public void update(Graphics rg)
	{
		Graphics g = main.screen.getGraphics();
		for (int x = 0; x < main.width; x++)
		{
			for (int y = 0; y < main.height; y++)
			{
				main.font.render(g, x * main.font.xsize, y * main.font.ysize,
						main.chars[x + main.getScrollX()][y + main.getScrollY()],
						Terminal.getColor(main.bcolors[x + main.getScrollX()][y + main.getScrollY()]),
						Terminal.getColor(main.fcolors[x + main.getScrollX()][y + main.getScrollY()]),
						(x + main.getScrollX() == main.cursorX && y + main.getScrollY() == main.cursorY && main.cursorOn)? true : false);
			}
		}
		rg.drawImage(main.screen, this.getInsets().left, this.getInsets().top, this);
	}
}
