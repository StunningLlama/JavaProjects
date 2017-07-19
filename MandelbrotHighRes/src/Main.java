import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main extends JFrame implements Runnable {
	public static int WIDTH = 2048;
	public static int HEIGHT = 2048;
	Display d;
	public Main() {
		super();
		setSize(WIDTH, HEIGHT);
		setVisible(true);
		d = new Display(this);
		this.add(d);
	}
	public static void main(String[] args) {
		(new Main()).run();
	}
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//d.potato-= 0.001;
			d.repaint();
		}
	}
}

class Display extends JPanel {
	BufferedImage img;
	Main p;
	public final static int MAX_ITER = 200;
	public final static int ADDITIONAL_ITER = 5;
	public Display(Main m) {
		img = (BufferedImage) m.createImage(Main.WIDTH, Main.HEIGHT);
		p = m;
	}
	@Override
	public void paintComponent(Graphics g) {
		Graphics gr = img.getGraphics();
		gr.setColor(Color.WHITE);
		gr.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		for (int x = 0; x < Main.WIDTH; x++)
			for (int y = 0; y < Main.HEIGHT; y++) {
				double cr = ((double)x-(double)Main.WIDTH/2.0)/(double)Main.WIDTH*4.0;
				double ci = ((double)y-(double)Main.HEIGHT/2.0)/(double)Main.HEIGHT*4.0;
				double tx = 0.0;
				double ty = 0.0;
				int it = 0;
				while(tx*tx+ty*ty < 4.0 && it < MAX_ITER) {
					double tmpx = tx*tx-ty*ty+cr;
					ty = tx*ty*2.0+ci;
					tx = tmpx;
					it++;
				}
				if (it < MAX_ITER - 1) {
					for (int i = 0; i < ADDITIONAL_ITER; i++) {
						double tmpx = tx*tx-ty*ty+cr;
						ty = tx*ty*2.0+ci;
						tx = tmpx;
						it++;
					}
					double smoothcolor = it + 1 - Math.log(Math.log(Math.sqrt(tx * tx + ty * ty))) / Math.log(2);
					gr.setColor(new Color(Color.HSBtoRGB((float) (0.25 * Math.log(smoothcolor)) ,0.6f,1.0f)));
				} else
					gr.setColor(Color.BLACK);
				gr.fillRect(x, y, 1, 1);
			}
		g.drawImage(img, 0, 0, p);
	}
}