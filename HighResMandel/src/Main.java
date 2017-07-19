import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Main {
	public static final int WIDTH = 15360;
	public static final int HEIGHT = 8640;
	public static final int SCALE = 1;
	public static final int MAX_ITER = 1024;
	public static final int ADDITIONAL_ITER = 5;
	public static final String PATH = "F:\\Documents\\mandelbrot.png";
	public static void main(String[] args) {
		BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics gr = img.getGraphics();
		gr.setColor(Color.BLACK);
		gr.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		long nextsec = System.currentTimeMillis()+1000;
		long finishedPixels = 0;
		long totalPixels = WIDTH*HEIGHT;
		for (int x = 0; x < Main.WIDTH; x++) {
			for (int y = 0; y < Main.HEIGHT; y++) {
				if (System.currentTimeMillis() > nextsec) {
					System.out.println(100*finishedPixels/totalPixels + "% done");
					nextsec = System.currentTimeMillis()+1000;
				}
				finishedPixels++;
				double cReal = ((double)x-(double)Main.WIDTH/2.0)/(double)Main.WIDTH*4.0*1.2-0.5;
				double cIm = ((double)y-(double)Main.HEIGHT/2.0)/(double)Main.WIDTH*4.0*1.2;
				if ((cReal+1)*(cReal+1)+cIm*cIm<0.0625)
					continue;
				double q = (cReal-0.25)*(cReal-0.25)+cIm*cIm;
				if (q*(q+(cReal-0.25)) < 0.25*(cIm*cIm))
					continue;
				double iterX = 0.0;
				double iterY = 0.0;
				int iterations = 0;
				while(iterX*iterX+iterY*iterY < 4.0 && iterations < MAX_ITER) {
					double tmpX = iterX*iterX-iterY*iterY+cReal;
					iterY = iterX*iterY*2.0+cIm;
					iterX = tmpX;
					iterations++;
				}
				if (iterations == MAX_ITER)
					continue;
				for (int i = 0; i < ADDITIONAL_ITER; i++) {
					double tmpx = iterX*iterX-iterY*iterY+cReal;
					iterY = iterX*iterY*2.0+cIm;
					iterX = tmpx;
					iterations++;
				}
				double smoothcolor = iterations + 1 - Math.log(Math.log(Math.sqrt(iterX * iterX + iterY * iterY))) / Math.log(2);
				gr.setColor(new Color(Color.HSBtoRGB((float) (0.25 * Math.log(smoothcolor)) ,0.6f,1.0f)));
				gr.fillRect(x, y, 1, 1);
			}
		}
		//System.out.println("100% done. scaleing image");
		//Image scaledImageRaw = img.getScaledInstance(WIDTH/SCALE, HEIGHT/SCALE, BufferedImage.SCALE_SMOOTH);
		//img = new BufferedImage(WIDTH/SCALE, HEIGHT/SCALE, BufferedImage.TYPE_INT_ARGB);
		//img.getGraphics().drawImage(scaledImageRaw, 0, 0, null);
		//System.out.println("Writing image to disk.");
		File file = new File(PATH);
		try {
			ImageIO.write(img, "PNG", file);
		} catch (IOException e) {}
		System.out.println("image has been written.");
	}
}