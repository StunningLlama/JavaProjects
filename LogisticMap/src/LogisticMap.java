import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class LogisticMap extends JFrame implements Runnable {
	LogisticCanvas canvas;
	
	public static void main(String[] args) {
		(new LogisticMap()).run();
	}
	
	public LogisticMap() {
		this.setSize(1600, 900);
		canvas = new LogisticCanvas(this);
		canvas.setSize(1600, 900);
		canvas.setVisible(true);
		this.add(canvas);
		this.pack();
		this.setVisible(true);
	}
	
	static int res = 4;
	static boolean done = false;

	static int WIDTH = 1600*res;
	static int HEIGHT = 900*res;
	@Override
	public void run() {
		long starttime = 0;
		long timediff = 0;
		int i = 0;
		int k = 0;
		while (true) {
			k++;
			try {
				Thread.sleep(Math.max(0, 16 - (timediff/1000000)));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			starttime = System.nanoTime();
			if (k%30 == 0) {
				canvas.wb();
				canvas.repaint();
			}
			boolean ret = false;
			for (int j = 0; j < 16; j++) {
				i = i+1;
				if (i >= WIDTH) {
					ret = true;
					break;
				}
				canvas.iter(i, (3.4+(4.0-3.4)*(i/(1600.0*res))));
			}
			if (ret)
				break;
			timediff = System.nanoTime()-starttime;
		}
		done = true;
		canvas.wb();
		canvas.repaint();
		System.out.println("End");
	}

}

class LogisticCanvas extends Canvas {
	BufferedImage image;
	BufferedImage disp;
	LogisticMap par;
	
	static int iterations = 10000;
	static int startplot = 100;
	
	public LogisticCanvas(LogisticMap instance) {
		instance = par;
		image = new BufferedImage(LogisticMap.WIDTH, LogisticMap.HEIGHT, BufferedImage.TYPE_INT_RGB);
		image.getGraphics().setColor(Color.WHITE);
		image.getGraphics().fillRect(0, 0, LogisticMap.WIDTH, LogisticMap.HEIGHT);
		disp = new BufferedImage(1600, 900, BufferedImage.TYPE_INT_RGB);
	}
	
	public void iter(int pix, double x) {
		double val = 0.5;
		for (int i = 0; i < iterations; i++) {
			val = x*val*(1.0-val);
			if (i > startplot) {
				int py = (int)((1.0-val)*900.0*LogisticMap.res);
				int col = (int)(256.0*10.0*(1.0-(10.0/(((image.getRGB(pix, py)&0xFF)/256.0)+10.0))));
				image.setRGB(pix, py, col | (col<<8) | (col<<16));
			}
		}
	}
	@Override
	public void update(Graphics g) {
		g.drawImage(disp, 0, 0, this);
	}
	public void wb() {
		disp.getGraphics().drawImage(image.getScaledInstance(1600, 900, Image.SCALE_SMOOTH), 0, 0, this);
	}
}
