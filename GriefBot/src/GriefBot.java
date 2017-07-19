
	import java.awt.AWTException;
import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.MouseInfo;
import java.awt.Panel;
import java.awt.Robot;
import java.awt.Scrollbar;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
public class GriefBot {

		
		public static Scrollbar s;
		public static int flag;
		public static boolean commanden;
		public static boolean boten;
		
		public static void KeyStroke(Robot bot, int key, boolean shift)
		{
			if (shift) bot.keyPress(KeyEvent.VK_SHIFT);
			bot.keyPress(key);
			bot.keyRelease(key);
			if (shift) bot.keyRelease(KeyEvent.VK_SHIFT);
		}
		
		public static void chat(String str, Robot bot) throws InterruptedException
		{
			bot.keyPress(KeyEvent.VK_BACK_QUOTE);
			bot.keyRelease(KeyEvent.VK_BACK_QUOTE);
			Thread.sleep(s.getValue());
			GriefBot.Input(str, bot);
			Thread.sleep(s.getValue());
			bot.keyPress(KeyEvent.VK_ENTER);
			bot.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(s.getValue());
		}
		
		public static void Input(String str, Robot bot)
		{
			for (int i = 0; i < str.length(); i++)
			{
				GriefBot.InputChar(str.charAt(i), bot);
			}
		}
		
		public static void InputChar(char key, Robot bot)
		{
			switch(key)
			{
				case ' ': KeyStroke(bot, KeyEvent.VK_SPACE, false); return;
				case '1': KeyStroke(bot, KeyEvent.VK_1, false); return;
				case '2': KeyStroke(bot, KeyEvent.VK_2, false); return;
				case '3': KeyStroke(bot, KeyEvent.VK_3, false); return;
				case '4': KeyStroke(bot, KeyEvent.VK_4, false); return;
				case '5': KeyStroke(bot, KeyEvent.VK_5, false); return;
				case '6': KeyStroke(bot, KeyEvent.VK_6, false); return;
				case '7': KeyStroke(bot, KeyEvent.VK_7, false); return;
				case '8': KeyStroke(bot, KeyEvent.VK_8, false); return;
				case '9': KeyStroke(bot, KeyEvent.VK_9, false); return;
				case '0': KeyStroke(bot, KeyEvent.VK_0, false); return;
				case 'q': KeyStroke(bot, KeyEvent.VK_Q, false); return;
				case 'w': KeyStroke(bot, KeyEvent.VK_W, false); return;
				case 'e': KeyStroke(bot, KeyEvent.VK_E, false); return;
				case 'r': KeyStroke(bot, KeyEvent.VK_R, false); return;
				case 't': KeyStroke(bot, KeyEvent.VK_T, false); return;
				case 'y': KeyStroke(bot, KeyEvent.VK_Y, false); return;
				case 'u': KeyStroke(bot, KeyEvent.VK_U, false); return;
				case 'i': KeyStroke(bot, KeyEvent.VK_I, false); return;
				case 'o': KeyStroke(bot, KeyEvent.VK_O, false); return;
				case 'p': KeyStroke(bot, KeyEvent.VK_P, false); return;
				case 'a': KeyStroke(bot, KeyEvent.VK_A, false); return;
				case 's': KeyStroke(bot, KeyEvent.VK_S, false); return;
				case 'd': KeyStroke(bot, KeyEvent.VK_D, false); return;
				case 'f': KeyStroke(bot, KeyEvent.VK_F, false); return;
				case 'g': KeyStroke(bot, KeyEvent.VK_G, false); return;
				case 'h': KeyStroke(bot, KeyEvent.VK_H, false); return;
				case 'j': KeyStroke(bot, KeyEvent.VK_J, false); return;
				case 'k': KeyStroke(bot, KeyEvent.VK_K, false); return;
				case 'l': KeyStroke(bot, KeyEvent.VK_L, false); return;
				case 'z': KeyStroke(bot, KeyEvent.VK_Z, false); return;
				case 'x': KeyStroke(bot, KeyEvent.VK_X, false); return;
				case 'c': KeyStroke(bot, KeyEvent.VK_C, false); return;
				case 'v': KeyStroke(bot, KeyEvent.VK_V, false); return;
				case 'b': KeyStroke(bot, KeyEvent.VK_B, false); return;
				case 'n': KeyStroke(bot, KeyEvent.VK_N, false); return;
				case 'm': KeyStroke(bot, KeyEvent.VK_M, false); return;
				case '`': KeyStroke(bot, KeyEvent.VK_BACK_QUOTE, false); return;
				case '-': KeyStroke(bot, KeyEvent.VK_SUBTRACT, false); return;
				case '=': KeyStroke(bot, KeyEvent.VK_EQUALS, false); return;
				case '[': KeyStroke(bot, KeyEvent.VK_OPEN_BRACKET, false); return;
				case ']': KeyStroke(bot, KeyEvent.VK_CLOSE_BRACKET, false); return;
				case '\\': KeyStroke(bot, KeyEvent.VK_BACK_SLASH, false); return;
				case ';': KeyStroke(bot, KeyEvent.VK_SEMICOLON, false); return;
				case '\'': KeyStroke(bot, KeyEvent.VK_QUOTE, false); return;
				case ',': KeyStroke(bot, KeyEvent.VK_COMMA, false); return;
				case '.': KeyStroke(bot, KeyEvent.VK_PERIOD, false); return;
				case '/': KeyStroke(bot, KeyEvent.VK_SLASH, false); return;
				case '!': KeyStroke(bot, KeyEvent.VK_1, true); return;
				case '@': KeyStroke(bot, KeyEvent.VK_2, true); return;
				case '#': KeyStroke(bot, KeyEvent.VK_3, true); return;
				case '$': KeyStroke(bot, KeyEvent.VK_4, true); return;
				case '%': KeyStroke(bot, KeyEvent.VK_5, true); return;
				case '^': KeyStroke(bot, KeyEvent.VK_6, true); return;
				case '&': KeyStroke(bot, KeyEvent.VK_7, true); return;
				case '*': KeyStroke(bot, KeyEvent.VK_8, true); return;
				case '(': KeyStroke(bot, KeyEvent.VK_9, true); return;
				case ')': KeyStroke(bot, KeyEvent.VK_0, true); return;
				case 'Q': KeyStroke(bot, KeyEvent.VK_Q, true); return;
				case 'W': KeyStroke(bot, KeyEvent.VK_W, true); return;
				case 'E': KeyStroke(bot, KeyEvent.VK_E, true); return;
				case 'R': KeyStroke(bot, KeyEvent.VK_R, true); return;
				case 'T': KeyStroke(bot, KeyEvent.VK_T, true); return;
				case 'Y': KeyStroke(bot, KeyEvent.VK_Y, true); return;
				case 'U': KeyStroke(bot, KeyEvent.VK_U, true); return;
				case 'I': KeyStroke(bot, KeyEvent.VK_I, true); return;
				case 'O': KeyStroke(bot, KeyEvent.VK_O, true); return;
				case 'P': KeyStroke(bot, KeyEvent.VK_P, true); return;
				case 'A': KeyStroke(bot, KeyEvent.VK_A, true); return;
				case 'S': KeyStroke(bot, KeyEvent.VK_S, true); return;
				case 'D': KeyStroke(bot, KeyEvent.VK_D, true); return;
				case 'F': KeyStroke(bot, KeyEvent.VK_F, true); return;
				case 'G': KeyStroke(bot, KeyEvent.VK_G, true); return;
				case 'H': KeyStroke(bot, KeyEvent.VK_H, true); return;
				case 'J': KeyStroke(bot, KeyEvent.VK_J, true); return;
				case 'K': KeyStroke(bot, KeyEvent.VK_K, true); return;
				case 'L': KeyStroke(bot, KeyEvent.VK_L, true); return;
				case 'Z': KeyStroke(bot, KeyEvent.VK_Z, true); return;
				case 'X': KeyStroke(bot, KeyEvent.VK_X, true); return;
				case 'C': KeyStroke(bot, KeyEvent.VK_C, true); return;
				case 'V': KeyStroke(bot, KeyEvent.VK_V, true); return;
				case 'B': KeyStroke(bot, KeyEvent.VK_B, true); return;
				case 'N': KeyStroke(bot, KeyEvent.VK_N, true); return;
				case 'M': KeyStroke(bot, KeyEvent.VK_M, true); return;
				case '~': KeyStroke(bot, KeyEvent.VK_BACK_QUOTE, true); return;
				case '_': KeyStroke(bot, KeyEvent.VK_SUBTRACT, true); return;
				case '+': KeyStroke(bot, KeyEvent.VK_EQUALS, true); return;
				case '{': KeyStroke(bot, KeyEvent.VK_OPEN_BRACKET, true); return;
				case '}': KeyStroke(bot, KeyEvent.VK_CLOSE_BRACKET, true); return;
				case '|': KeyStroke(bot, KeyEvent.VK_BACK_SLASH, true); return;
				case ':': KeyStroke(bot, KeyEvent.VK_SEMICOLON, true); return;
				case '\"': KeyStroke(bot, KeyEvent.VK_QUOTE, true); return;
				case '<': KeyStroke(bot, KeyEvent.VK_COMMA, true); return;
				case '>': KeyStroke(bot, KeyEvent.VK_PERIOD, true); return;
				case '?': KeyStroke(bot, KeyEvent.VK_SLASH, true); return;
				default: return;
			}
		}
		
		public static void HandleEvt(int i)
		{
			if (i == 0)
			{
				boten = false;
			}
			if (i == 1)
			{
				boten = true;
			}
			if (i == 2)
			{
				spamright = !spamright;
			}
			if (i == 3)
			{
				spamleft = !spamleft;
			}
			if (i == 4)
			{

				inputdev.keyPress(KeyEvent.VK_W);
			}
			if (i == 5)
			{
				rotate = !rotate;
				time = 3600;
				inputdev.mouseMove(MouseInfo.getPointerInfo().getLocation().x, 900);
				inputdev.mouseMove(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y - 300);
			}
			if (i == 7)
			{
				inputdev.keyPress(KeyEvent.VK_W);

				rotate = !rotate;
				time = 3600;
				spamleft = !spamleft;
				inputdev.mouseMove(MouseInfo.getPointerInfo().getLocation().x, 900);
				inputdev.mouseMove(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y - 300);
			}
			if (i == 8)
			{

				rotate = false;
				time = 0;
				spamleft = false;
				spamright = false;
				inputdev.keyRelease(KeyEvent.VK_W);
			}
		}
		static boolean spamleft = false;
		static boolean spamright = false;
		static boolean focus = true;
		static Robot inputdev = null;
		static int time = 3600;
		static boolean rotate = false;
		public static void main(String args[]) throws InterruptedException, IOException
		{
			flag = 0;
			commanden = false;
			boten = true;
			try {
				 inputdev = new Robot();
			} catch (AWTException e) {
				System.exit(0);
			}
			Frame window = new Frame();
			window.addWindowListener(new WindowListener() {

				public void windowActivated(WindowEvent arg0) {
					focus = true;
				}
				public void windowClosed(WindowEvent arg0) {}
				public void windowClosing(WindowEvent arg0) {
					System.exit(0);
				}
				public void windowDeactivated(WindowEvent arg0) {
					focus = false;
				}
				@Override
				public void windowDeiconified(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void windowIconified(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void windowOpened(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			window.setSize(600, 300);
			window.setLayout(new BorderLayout());
			s = new Scrollbar(Scrollbar.HORIZONTAL, 50, 20, 10, 220);
			Panel t = new Panel(new GridLayout(4, 2));
			Button b = new Button("Start Bot");
			b.addActionListener(new ButtonHandler(0));
			t.add(b);
			b = new Button("Stop Bot");
			b.addActionListener(new ButtonHandler(1));
			t.add(b);
			b = new Button("Instaplace");
			b.addActionListener(new ButtonHandler(2));
			t.add(b);
			b = new Button("Instabreak");
			b.addActionListener(new ButtonHandler(3));
			t.add(b);
			b = new Button("Walk");
			b.addActionListener(new ButtonHandler(4));
			t.add(b);
			b = new Button("Rotate");
			b.addActionListener(new ButtonHandler(5));
			t.add(b);
			b = new Button("Grief");
			b.addActionListener(new ButtonHandler(6));
			t.add(b);
			b = new Button("Reset");
			b.addActionListener(new ButtonHandler(7));
			t.add(b);
			Panel p = new Panel(new GridLayout(2, 1));
			Label lat = new Label("Keystroke Latency: 50");
			p.add(lat);
			p.add(s);
			window.add(p, BorderLayout.SOUTH);
			
			window.add(t, BorderLayout.NORTH);
			window.setVisible(true);
			
			int mod = 0;
			String spamcharstr = "";
			long ang = 0;
			while (true)
			{
				while (System.in.available() == 0)
				{

					Thread.sleep(10);
					mod++;
					if (!boten) continue;
					if (spamleft && !focus && !spamright)
					{
						inputdev.mousePress(InputEvent.BUTTON3_MASK);
						inputdev.mouseRelease(InputEvent.BUTTON3_MASK);
					}
					if (spamright && !focus && !spamleft)
					{
						inputdev.mousePress(InputEvent.BUTTON1_MASK);
						inputdev.mouseRelease(InputEvent.BUTTON1_MASK);
					}
					if (rotate && !focus)
					{
						ang += 1;
						time -= 3;
						if (time < 0)
						{
							rotate = false;
							inputdev.keyRelease(KeyEvent.VK_W);
						}
						int dy;
						if ((ang%50-25)<0)
						{
							dy = -50;
						}
						else
						{
							dy = 50;
						}
						if (ang > 360) ang = ang - 360;
						
						inputdev.mouseMove(MouseInfo.getPointerInfo().getLocation().x + dy, MouseInfo.getPointerInfo().getLocation().y);
					}
				}
				String line = "";
				while (System.in.available() != 0)
				{
					int i = System.in.read();
					if (i == -1) System.exit(0);
					line += (char) i;
				}
				if (!boten) continue;
				if (line.indexOf(">") != -1)
				{
					String message = line.substring(line.indexOf(">") + 2);
					if (message.startsWith("!instaplace"))
					{
						spamright = !spamright;
					}
					if (message.startsWith("!instabreak"))
					{
						spamleft = !spamleft;
					}
					if (message.startsWith("!walk"))
					{
						inputdev.keyPress(KeyEvent.VK_W);
					}
					if (message.startsWith("!rotate"))
					{
						rotate = !rotate;
						time = 3600;
						inputdev.mouseMove(MouseInfo.getPointerInfo().getLocation().x, 900);
						inputdev.mouseMove(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y - 300);
					}
					if (message.startsWith("!grief"))
					{
						inputdev.keyPress(KeyEvent.VK_W);

						rotate = !rotate;
						time = 3600;
						spamleft = !spamleft;
						inputdev.mouseMove(MouseInfo.getPointerInfo().getLocation().x, 900);
						inputdev.mouseMove(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y - 300);
					}
					if (message.startsWith("!reset"))
					{
						rotate = false;
						time = 0;
						spamleft = false;
						spamright = false;
						inputdev.keyRelease(KeyEvent.VK_W);
					}
					if (message.startsWith("!help"))
					{
						chat("==== GRIEFBOT HELP ====", inputdev);
						chat("- !instaplace: start insta-placeing", inputdev);
						chat("- !instabreak: start insta-breaking", inputdev);
						chat("- !rotate: make powder dizzy!", inputdev);
						chat("- !walk: hold the walk key.", inputdev);
						chat("- !grief: combines walk instabreak and rotate.", inputdev);
						chat("- !reset: reset all.", inputdev);
					}
					/*if (BotOn)
					{
						if (message.startsWith("!ping"))
						{
							chat("pong", inputdev);
						}
						if (message.startsWith("!sudo"))
						{
							if (!commanden)
								chat("WHY DO YOU WANT TO USE A COMMAND? ARE YOU A NUB?", inputdev);
							else
							{
								chat("Command: /" + message.substring(6), inputdev);
								chat("/" + message.substring(6), inputdev);
							}
						}
						if (message.startsWith("!chat"))
						{
							if (message.substring(6).startsWith("/") && (!commanden))
								chat("Error: commands disabled in chat", inputdev);
							else
								chat(message.substring(6), inputdev);
						}
						if (message.startsWith("!spam"))
						{
							if (spamchat)
							{
								spamchat = false;
								chat("Spam chat disabled.", inputdev);
							}
							else
							{
							if (message.substring(6).startsWith("/") && (!commanden))
								chat("Error: commands disabled in chat", inputdev);
							else
							{
								spamchat = true;
								spamcharstr = message.substring(6);
								chat("Spam chat enabled.", inputdev);
							}
							}
						}
						if (message.startsWith("!hit"))
						{
							if (spamhit)
							{
								spamhit = false;
								chat("Spam hit disabled.", inputdev);
							}
							else
							{
								spamhit = true;
								chat("Spam hit enabled.", inputdev);	
							}
						}
					}*/
				}
			}
		}
	}

	class ButtonHandler implements ActionListener
	{
		int num;
		
		public ButtonHandler(int id)
		{
			num = id;
		}
		
		public void actionPerformed(ActionEvent event) {
			GriefBot.HandleEvt(num);
		}
		

}
