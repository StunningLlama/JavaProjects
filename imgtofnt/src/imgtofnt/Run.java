package imgtofnt;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.imageio.ImageIO;

public class Run extends Frame {
	public static void main(String[] args)
	{
		new Run(Integer.valueOf(args[0]), Integer.valueOf(args[1]), args[2]);
	}
	public Run(int xl, int yl, String dir)
	{
		this.setVisible(true);
		BufferedImage i = null;
		FileDialog input = new FileDialog(this, ".", FileDialog.LOAD);
		input.setVisible(true);
		File inf = new File(input.getDirectory() + input.getFile());
		try {
			i = ImageIO.read(inf);
		} catch (IOException e) {
			System.out.print("adalishasuhduasiygduyask");
		}
		for (int y = 0; y < 16; y++)
			for (int x = 0; x < 16; x++)
			{
				Writer writer = null;

				try {
				    writer = new BufferedWriter(new OutputStreamWriter(
				          new FileOutputStream(dir + "char-" + (y * 16 + x) + ".fnt"), "utf-8"));
				} catch (IOException ex) {}
				for (int ln = 0; ln < yl; ln++)
				{
					String line = "";
					for (int col = 0; col < xl; col++)
					{
						if (i.getRGB(x * 8 + col, y * 12 + ln) != -16777216)
						{
							line += "1";
						}
						else
						{
							line += "0";
						}
					}
					line += "\n";
					
					   try {
						writer.write(line);
					} catch (IOException e) {}
				}
				try {
					writer.close();
				} catch (IOException e) {}
			}
	}
}
