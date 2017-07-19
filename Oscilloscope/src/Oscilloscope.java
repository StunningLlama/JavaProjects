import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Oscilloscope implements Runnable {
	Options optionWindow;
	public int DWidth = 1470;
	public int DHeight = 900;
	static int maxdatasize;
	public int fsize;
	
	int mode = 0;
	
	List<InputDescription> inputs = new ArrayList<InputDescription>();
	
	OscilloscopeDisplay display;
	
	public Oscilloscope() {
		Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		for (Mixer.Info mixerInfo : mixers){
			Line.Info[] lineInfos = AudioSystem.getMixer(mixerInfo).getTargetLineInfo();
			Mixer m = AudioSystem.getMixer(mixerInfo);
			for (Line.Info lineInfo: lineInfos) {
	            try {
	                m.open();
	                inputs.add(new InputDescription(mixerInfo, lineInfo)); 
	            } catch (LineUnavailableException e){
	                System.out.println("Line unavailable.");
	            }
			}
		}
		for (int i = 0; i < inputs.size(); i++) {
			System.out.println(inputs.get(i).toString());
		}
		
		display = new OscilloscopeDisplay();
		optionWindow = new Options(this);

		for (InputDescription i: inputs)
			optionWindow.inputList.addItem(i);
		final Oscilloscope osc = this;
		optionWindow.inputList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("AAA");
				osc.reset();
				System.out.println("BBB");
				osc.run();
				System.out.println("CCC");
			}
			
		});
		optionWindow.repaint();
		//this.setLayout(new BorderLayout());
		//this.add(display);
	//	this.pack();
	//	this.setVisible(true);
	}
	
	public static void main(String[] args) {
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {}
		Oscilloscope osc = (new Oscilloscope());
	}
	TargetDataLine line = null;
	byte[] data;
	int size;
	
	public void updateOptions() {
		if (AIThread != null)
			AIThread.updateOptions();
	}
	
	public void reset() {
		if (AIThread != null)
		{
			running = false;
			while (AIThread.isAlive()) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	AudioInputThread AIThread;
	boolean running = false;
	
	@Override
	public void run() {
		
		InputDescription input = (InputDescription)optionWindow.inputList.getSelectedItem();
		
		try {
			line = (TargetDataLine)AudioSystem.getMixer(input.mixer).getLine(input.line);
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
		System.out.println(line.getFormat().toString());
		AudioFormat format = line.getFormat();
		size = 200;
		fsize = format.getFrameSize();
		data = new byte[fsize*size];

		// Begin audio capture.
		try {
			line.open(format, 6000);
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}

		line.start();
		System.out.println(line.isOpen());
		Thread DThread = new DrawThread(this);
		AIThread = new AudioInputThread(this);
		DThread.start();
		running = true;
		AIThread.start();
	}
}

class DrawThread extends Thread {
	
	Oscilloscope parent;
	public DrawThread(Oscilloscope inst) {
		parent = inst;
	}
	@Override
	public void run() {
		parent.display.run_ogl();
	}
}

class AudioInputThread extends Thread {
	Oscilloscope parent;
	public AudioInputThread(Oscilloscope inst) {
		parent = inst;
	}
	
	double screenptr = 0.0;
	double tscale = 1.0;
	double prevx;
	double prevy;
	double vscale = 400.0;
	double hscale = 400.0;
	double trigger = 0.01;
	boolean trigInverted = false;
	
	public void updateOptions() {
		Options op = parent.optionWindow;
		trigger = Double.valueOf(op.trigtext.getText());
		
		parent.display.decay = (float) (double) (Double) op.decayRate.getValue();
		parent.display.pstr = (float) (double) (Double) op.phosphorStr.getValue();
		parent.AIThread.trigInverted = op.chkInv.isSelected();
		
		double tscaletmp = (Double)(op.tdivtext.getValue());
		switch(op.tdivunits.getSelectedIndex()) {
		case 0: //ns
			tscale = 2/(tscaletmp*line.getFormat().getSampleRate()*0.000001);
			break;
		case 1: //us
			tscale = 2/(tscaletmp*line.getFormat().getSampleRate()*0.001);
			break;
		case 2: //ms
			tscale = 2/(tscaletmp*line.getFormat().getSampleRate()*1.0);
			break;
		}

		double vscaletmp = (Double)(op.vdivtext.getValue());
		switch(op.vdivunits.getSelectedIndex()) {
		case 0: //mv
			vscale = 1/(vscaletmp*0.001);
			break;
		case 1: //v
			vscale = 1/(vscaletmp*1.0);
			break;
		case 2: //kv
			vscale = 1/(vscaletmp*1000.0);
			break;
		}
		

		double hscaletmp = (Double)(op.hdivtext.getValue());
		switch(op.hdivunits.getSelectedIndex()) {
		case 0: //mv
			hscale = 1/(hscaletmp*0.001);
			break;
		case 1: //v
			hscale = 1/(hscaletmp*1.0);
			break;
		case 2: //kv
			hscale = 1/(hscaletmp*1000.0);
			break;
		}
		
		parent.mode = (op.oscmode.getSelectedIndex());
	}
	
	int func(int x) {
	//	int r = (x&0xFF0000) >> 16;
	//	int g = (x&0x00FF00) >> 8;
	//	int b = x&0x0000FF;
		//r = 255;
	//	return ((int)(r*0.9) << 16) | ((int)(g*0.9) << 8) | ((int)(b*0.9));
		return 0;
	}

	public void drawline(float x, float y, float x1, float y1, float alpha) {
		int ind = parent.display.index;
		if (ind != -1 && ind < parent.display.numlines) {
			parent.display.vertices[parent.display.vertices_offset + ind  ] = x;//2.0f * (float)x / parent.DWidth - 1.0f;
			parent.display.vertices[parent.display.vertices_offset + ind+1] = y;//2.0f * (float)y / parent.DHeight - 1.0f;
			parent.display.vertices[parent.display.vertices_offset + ind+2] = x1;//2.0f * (float)x1 / parent.DWidth - 1.0f;
			parent.display.vertices[parent.display.vertices_offset + ind+3] = y1;//2.0f * (float)y1 / parent.DHeight - 1.0f;
			parent.display.colors[parent.display.colors_offset + ind*2 + 3] = alpha;
			parent.display.colors[parent.display.colors_offset + ind*2 + 7] = alpha;
			parent.display.index += 4;
		}
	}

	public boolean next(double value, Graphics g) {
		double x = screenptr;//(parent.DWidth*i)/parent.size;
		double y = -value*vscale;//+(parent.DHeight/2.0);
//*		g.setColor(Color.BLUE);
//*		g.drawLine((int)prevx, (int)prevy, (int)x, (int)y);

		double dist = 0.001/Math.sqrt((x-prevx)*(x-prevx) + (y-prevy)*(y-prevy));
		drawline((float)prevx, (float)prevy, (float)x, (float)y, (float)dist);
		
		prevx = x;
		prevy = y;
		screenptr += tscale;
		if (screenptr > 1.0) {
//*			parent.display.buffer.getGraphics().drawImage(parent.display.image, 0, 0, parent);
//*			parent.display.repaint();
//			parent.vertices
			
//			long ns = System.nanoTime();
//*			g.setColor(Color.WHITE);
			
//*			g.fillRect(0, 0, parent.DWidth, parent.DHeight);
	//		System.out.println("A: " + String.valueOf(System.nanoTime()-ns));
//			ns = System.nanoTime();
//*			BufferedImage img = parent.display.image;
	//		img.getRaster().setPixels(x, y, w, h, iArray);
//*			for (int ix = 0; ix < parent.DWidth; ix++)
//*				for (int iy = 250; iy < 650; iy++) {
//*					img.setRGB(ix, iy, func(img.getRGB(ix, iy)));
//*			}
	//		System.out.println("B: " + String.valueOf(System.nanoTime()-ns));
			screenptr = -1.0;
			prevx = -1.0;
			prevy = 0.0;//(parent.DHeight/2.0);
			return false;
		}
		return true;
	}
	
	public void nextxy(double xin, double yin) {
		double x = xin*hscale;//+(parent.DWidth/2.0);
		double y = yin*vscale;//+(parent.DHeight/2.0);
		double dist = 0.001/Math.sqrt((x-prevx)*(x-prevx) + (y-prevy)*(y-prevy));
		drawline((float)prevx, (float)prevy, (float)x, (float)y, (float)dist);
		prevx = x;
		prevy = y;
	}
	
	TargetDataLine line;
	@Override
	public void run() {
		line = parent.line;
		byte[] data = parent.data;
		boolean cont = false;
		boolean hysteresisdone = false;
		while (parent.running) {
			line.read(data, 0, data.length);
//*			Graphics g = parent.display.image.getGraphics();
			for (int i = 0; i < parent.size*parent.fsize; i += parent.fsize) {
				float ptL = (int)((data[i+1]<<8) | (data[i]&0xFF));
				float ptR = (int)((data[i+3]<<8) | (data[i+2]&0xFF));
				if (parent.mode == 2) {
					nextxy(ptL/32768.0, ptR/32768.0);
				} else {
					double x = (ptL+ptR)/32768.0;
					if (cont || parent.mode == 0) {
						if (!next(x, null)) {
							cont = false;
							hysteresisdone = false;
						}
					} else {
						double val = (trigInverted?x:-x);
						if (val > trigger && hysteresisdone) {
							cont = true;
						} else if (val < trigger)
							hysteresisdone = true;
					}
				}
			}
		}
		line.close();
	}
}

class InputDescription {
	Mixer.Info mixer;
	Line.Info line;
	public InputDescription(Mixer.Info mixerIn, Line.Info lineIn) {
		mixer = mixerIn;
		line = lineIn;
	}
	
	public String toString() {
		return mixer.toString();
	}
}