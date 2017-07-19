import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class AntiAlias
{
	public static int detectionRate = 0;
	public static int[][] cannotAA;
	public static int[][] image;
	public static void main(String[] args)
	{
		BufferedImage img = null;
		try {
			detectionRate = Integer.valueOf(args[0]);
		    img = ImageIO.read(new File(args[1]));
			cannotAA = new int[img.getWidth()][img.getHeight()];
			image = new int[img.getWidth()][img.getHeight()];
			for (int x = 0; x < img.getWidth(); x++)
			{
				for (int y = 0; y < img.getHeight(); y++)
				{
					image[x][y] = img.getRGB(x, y);
				}
			}
			long msstart = System.currentTimeMillis();
			for (int x = 1; x < img.getWidth() - 1; x++)
			{
				for (int y = 1; y < img.getHeight() - 1; y++)
				{
					int a = image[x-1][y];
					int b = image[x+1][y];
					int c = image[x][y-1];
					int d = image[x][y+1];
					if (
							getDifference(a, b) < detectionRate && getDifference(b, c) < detectionRate && getDifference(c, d) < detectionRate
							&& getDifference(d, a) < detectionRate && getDifference(a, c) < detectionRate && getDifference(b, d) < detectionRate)
						cannotAA[x][y] = 1;
				}
			}
			image = antiAlias(image, img.getWidth(), img.getHeight());
			image = antiAlias(image, img.getWidth(), img.getHeight());
			image = antiAlias(image, img.getWidth(), img.getHeight());
			image = antiAlias(image, img.getWidth(), img.getHeight());
			System.out.println("Took " + (System.currentTimeMillis() - msstart) + " ms to compute AA");
			for (int x = 0; x < img.getWidth(); x++)
			{
				for (int y = 0; y < img.getHeight(); y++)
				{
					img.setRGB(x, y, image[x][y]);
				}
			}
		    ImageIO.write(img, "png", new File(args[2]));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	public static int getAvg(int a, int b, int c, int d)
	{
		return ((((a>>16)+(b>>16)+(c>>16)+(d>>16))/4)<<16)
				+((((a>>8&0xFF)+(b>>8&0xFF)+(c>>8&0xFF)+(d>>8&0xFF))/4)<<8)
				+((((a&0xFF)+(b&0xFF)+(c&0xFF)+(d&0xFF))/4));
	}
	
	public static int getDifference(int a, int b)
	{
		return abs((a>>16)-(b>>16))+abs((a>>8&0xFF)-(b>>8&0xFF))+abs((a&0xFF)-(b&0xFF));
	}
	
	public static int abs(int a)
	{
		return (a>=0)? a : 0-a;
	}
	
	public static boolean doAvg(int a, int b, int c, int d)
	{
		if (getDifference(a, b) + getDifference(b, c) + getDifference(c, a) < detectionRate*3) return false;
		if (getDifference(b, c) + getDifference(c, d) + getDifference(d, b) < detectionRate*3) return false;
		if (getDifference(c, d) + getDifference(d, a) + getDifference(a, c) < detectionRate*3) return false;
		if (getDifference(d, a) + getDifference(a, b) + getDifference(b, d) < detectionRate*3) return false;
		return true;
	}
	
	public static int[][] antiAlias(int[][] input, int xlen, int ylen)
	{
		int[][] output = new int[xlen][ylen];
		for (int x = 1; x < xlen - 1; x++)
		{
			for (int y = 1; y < ylen - 1; y++)
			{
				int a = image[x-1][y];
				int b = image[x+1][y];
				int c = image[x][y-1];
				int d = image[x][y+1];
				if (doAvg(a, b, c, d) && cannotAA[x][y] == 0)
					output[x][y] = getAvg(a, b, c, d);
				else
					output[x][y] = input[x][y];
			}
		}
		return output;
	}
}
