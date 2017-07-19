import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class IIR extends JFrame implements Runnable, MouseListener, MouseMotionListener {

	RenderCanvas r;
	Filter filter;
	EnvelopeDet env;
	
	double freq_min;
	double freq_max;
	int num_filters;
	double timestep = 1;

	double Q;
	
	double sampleinterval;
	double invprescision;
	
	BufferedImage outputimg;
	
	byte[] raw_samples;
	float[] samples;
	int numsamples;
	int outputsamples;
	float[] envelopesamples;
	float[] freqs;
	int totalenvelopesamples;
	int samplerate;
	
	boolean log = false;
	
	public File INPUT_FILE;
	public File OUTPUT_FILE;
	
	Interface settings;
	
	float[][] output;
	
	double gamma = 1.0;
	double brightness = 1.0;
	
	Thread computethread;
	
	public static void main(String[] args) {
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {}
        
		IIR w = new IIR();
	}
	
	public IIR() {
		this.setSize(800,800);
		this.setVisible(true);
		r= new RenderCanvas(this);
		this.add(r);
		r.addMouseListener(this);
		r.addMouseMotionListener(this);
		env = new EnvelopeDet();
		settings = new Interface(this);
		settings.setVisible(true);
//		File myFile = new File(INPUT_FILE);

	}
	
	public void convertinput() {
		freq_min = (Double)settings.i_freq_low.getValue();
		freq_max = (Double)settings.i_freq_high.getValue();
		num_filters = (Integer)settings.i_img_h.getValue();
		outputsamples = (Integer)settings.i_img_w.getValue();
		Q = (Double)settings.i_q.getValue();
		invprescision = (Double)settings.i_prescision.getValue();
		log = settings.chckbxLogarithmic.isSelected();
	}
	
	public void commence() {
		AudioInputStream is = null;
		AudioFormat format = null;
		int framelength = 0;
		try {
			is = AudioSystem.getAudioInputStream(INPUT_FILE);
			DataInputStream dis = new DataInputStream(is);
			framelength = (int)is.getFrameLength();
			format = is.getFormat();
			samplerate = (int)format.getSampleRate();
			System.out.println(format);
			raw_samples = new byte[(framelength * format.getFrameSize())];
			dis.readFully(raw_samples);
			dis.close();
		}
		catch (UnsupportedAudioFileException e1) {
			System.out.println("Error: Audio format is not supported.");
			System.exit(0);
		}
		catch (IOException e1) {
			System.out.println("Error: File exception");
			System.exit(0);
		}

		int bytespersample = format.getSampleSizeInBits()/8;
		int channels = format.getChannels();
		int framesize = format.getFrameSize();
		samples = new float[framelength];

		for (int i = 0; i < framelength; i ++) {
			if (channels == 1) {
				if (bytespersample == 1) {
					samples[i] = raw_samples[i*framesize]/128.0f;
				} else if (bytespersample == 2) {
					samples[i] = (raw_samples[i*framesize]+raw_samples[i*framesize+1]*256)/32768.0f;
				}
			} else if (channels == 2) {
				if (bytespersample == 1) {
					samples[i] = raw_samples[i*framesize]/256.0f + raw_samples[i*framesize+1]/256.0f;
				} else if (bytespersample == 2) {
					samples[i] = (raw_samples[i*framesize]+raw_samples[i*framesize+1]*256)/65536.0f + (raw_samples[i*framesize]+raw_samples[i*framesize+1]*256)/65536.0f;
				}
			}
		}
		
		numsamples = samples.length;
		int maxsamplenumber = 1 + (int)(numsamples*(freq_max+100)/invprescision);
		outputimg = (BufferedImage) this.createImage(outputsamples, num_filters);
		output = new float[outputsamples][num_filters];
		for (int x = 0; x < outputsamples; x++)
			for (int y = 0; y < num_filters; y++)
				output[x][y] = 0.0f;
		freqs = new float[num_filters];
		for (int y = 0; y < num_filters; y++)
			freqs[y] = 0.0f;
		//output = new BufferedImage(outputsamples, num_filters, BufferedImage.TYPE_INT_ARGB);
		Graphics g = outputimg.getGraphics();
		g.setColor(Color.RED);
		g.fillRect(0, 0, outputsamples, num_filters);
		this.repaint();
		envelopesamples = new float[maxsamplenumber];
		System.out.println("Will use " + maxsamplenumber*4 + " bytes of mem.");
		computethread = new Thread(this);
		computethread.start();
	}
	
	public double getsample(double k) {
		int lower = (int)Math.floor(k);
		int upper = (int)Math.ceil(k);
	//	if (upper == lower)
	//		upper++;
		if (upper >= samples.length)
			upper--;
		if (lower >= samples.length) 
			lower--;

		double s_lower = samples[lower];
		double s_upper = samples[upper];
		
		double frac = k - Math.floor(k);
		return s_lower * (1.0 - frac) + s_upper * frac;
	}
	public boolean test = false;
	
	public double logpercent(double x, double l, double h, double k) {
		return l*(Math.pow((h/l), x) + ((x+k) * Math.log(h/l) - 1.0) - k*Math.log(h/l) + 1.0) / Math.pow(Math.log(h/k), 2);
	}
	
	public double linpercent(double x, double l, double h, double k) {
		return (1.0/6.0) * x*x * (h-l) * (3.0 * k + 2.0 * x) + l * x;
	}
	
	public void writeToImg() {
		this.gamma = (double)settings.i_gamma.getValue();
		this.brightness = (double)settings.i_intensity.getValue();
		for (int x = 0; x < outputsamples; x++)
			for (int y = 0; y < num_filters; y++) {

				int hex = (int)(Math.pow((output[x][y] * brightness), gamma) * 256.0);/// ((double) freqs[y] / freq_max));
			
				if (hex > 0xFFFF)
					outputimg.setRGB(x, num_filters - y - 1, 0xFF00FF);
				else if (hex > 0xFF)
					outputimg.setRGB(x, num_filters - y - 1, 0xFFFFFF);
				else
					outputimg.setRGB(x, num_filters - y - 1, ((hex << 16) | (hex << 8) | hex));
			}

		for (int y = 0; y < num_filters; y++) {
			double total = 0.0;
			for (int x = 0; x < outputsamples; x++) {
				total += output[x][y] * brightness;
			}
			System.out.println(freqs[y] + " " + total / outputsamples);
		}
		this.repaint();
	}
	
	@Override
	public void run() {
		//while (true) {
		//for (double f = 3.17; f < 3.22; f += 0.001) {
		long time = System.nanoTime();
		
		double logt0 = logpercent(0, freq_min, freq_max, 100);
		double logt1 = logpercent(1, freq_min, freq_max, 100);
		double lint1 = linpercent(1, freq_min, freq_max, 100);
		
		for (int f = 0; f < num_filters; f++) {
			double freq;
			if (log)
				freq = Math.pow(2.0, ((double)f / num_filters) * (Math.log(freq_max/freq_min) / Math.log(2.0))) * freq_min;
			else
				freq = (freq_min + (freq_max-freq_min)*((double)f/num_filters));
			freqs[f] = (float) freq;
			sampleinterval = invprescision/(freq+100);
			double val = this.getLC(freq * sampleinterval / samplerate, timestep);
			totalenvelopesamples = (int)Math.ceil(numsamples/sampleinterval);
			if (log)
				filter = new Filter(val, val, Q*2000);
			else
				filter = new Filter(val, val, Q*freq);
			double c = 200000;
			env.rst(c/(freq+c));
			int i = 0;
			for (int k = 0; k < totalenvelopesamples; k ++) {
			//	if (test) {
			//	try {
			//		Thread.sleep(17);
			//	} catch (InterruptedException e) {}
			//	}
				double sample = getsample(k * sampleinterval);
				filter.iterate(sample, timestep);
				//mouseY = (int) transform(0.1*Math.sin(2.0 * Math.PI * i * getfreq(timestep, L, C)));
				mouseY = (int) transform(sample);
				//System.out.println(mouseY + " " + filter.v_out);
				env.iterate(filter.v_out);
				envelopesamples[k] = (float)env.lastvalue;
			//	if (test)
			//		this.repaint();
				if (i > 200)
					test = false;
				i++;
			}
			//int cyclelength = (int) (samplerate / (freq * sampleinterval));
			int cyclelength = (int) (samplerate / freq / sampleinterval / 2);
			for (int x = 0; x < outputsamples; x++) {
				int start = (int)((double)totalenvelopesamples * ((double)x / (double)outputsamples));
				double total = 0;
				for (int n = -cyclelength; n <= cyclelength; n++) {
					if (start + n < 0 || start + n >= totalenvelopesamples)
						total += 0;
					else
						total += envelopesamples[start+n];
				}
				total /= cyclelength;
				output[x][f] = (float)total;
				
				int hex = (int)(total * 10.0 * 256.0);
				if (hex > 0xFFFF)
					outputimg.setRGB(x, num_filters - f - 1, 0xFF00FF);
				else if (hex > 0xFF)
					outputimg.setRGB(x, num_filters - f - 1, 0xFFFFFF);
				else
					outputimg.setRGB(x, num_filters - f - 1, ((hex << 16) | (hex << 8) | hex));
			}
			
			long now = System.nanoTime();
			if (now - time > 500 * 1000000) {
				time = now;
				this.repaint();
				double percent = (double)f / (double)num_filters;
				if (log) {
					percent = (logpercent(percent, freq_min, freq_max, 100) - logt0) / (logt1 - logt0);
				} else {
					percent = linpercent(percent, freq_min, freq_max, 100) / lint1;
				}
				String percentstr = String.format("%.2f", percent*100.0);
				System.out.println(percentstr + "% completed");
				settings.progressBar.setValue((int)(percent*10000));
				settings.progressBar.setString(percentstr + "%");
			}
		//	try {
		//		Thread.sleep(10);
		//	} catch (InterruptedException e) {}
//			test = true;
	//		System.out.println(f + " " + env.runningavg);
		}
		settings.progressBar.setValue(10000);
		settings.progressBar.setString("Finished!");
		this.repaint();
	}
	
	public void writeImageToFile() {
		System.out.println("finished! writing to file.");
		try {
			ImageIO.write(outputimg, "PNG", OUTPUT_FILE);
		} catch (IOException e) {
			System.out.println("Could not write to file!");
			return;
		}
		System.out.println("Successfully written to: " + OUTPUT_FILE.getAbsolutePath());
	}

	//public double reversetransform(double input) {
	//	return (input-350)/1000.0;
	//}
	public double transform(double input) {
		return input*1000.0 + 350;
	}
	
	int mouseX = 0;
	int mouseY = 250;

	@Override
	public void mouseClicked(MouseEvent arg0) {
		//updateMousePosition = !updateMousePosition;
	}
	boolean mouseIsPressed = false;
	PointerInfo p = MouseInfo.getPointerInfo();
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {mouseIsPressed = true;}
	@Override
	public void mouseReleased(MouseEvent arg0) {mouseIsPressed = false;}

	@Override
	public void mouseDragged(MouseEvent arg0) {
			mouseX = arg0.getX();
	//		mouseY = arg0.getY();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
			mouseX = arg0.getX();
	//		mouseY = arg0.getY();
	}
	
	public double getfreq(double timestep, double L, double C) {
		return 0.159265 * timestep / Math.sqrt(L*C);
	}
	
	public double getLC(double freq, double timestep) {
		return (timestep * 0.159265) / freq;
	}
}


class RenderCanvas extends JPanel {
	Image screen;
	IIR parent;
	public static final int INTERVAL = 4;
	@Override
	public void paintComponent(Graphics real) {
		if (parent.test) {
				Graphics g = screen.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 800, 800);
		g.setColor(Color.BLUE);
		g.fillRect(200, (int) parent.transform(parent.filter.v_out), 20, 3);
		g.setColor(Color.RED);
		g.fillRect(300, 200, 20, (int) (parent.filter.energy*100000000.0));
		g.setColor(Color.MAGENTA);
		g.fillRect(350, 200, 20, (int) (parent.env.lastvalue * 10000.0));
		g.setColor(Color.GREEN);
		g.fillRect(400, (int) parent.mouseY, 20, 3);
			real.drawImage(screen, 0, 0, parent);
		} else {
			if (parent.outputimg != null)
				real.drawImage(parent.outputimg, 0, 0, 1600, 900, parent);
		}
	}
	
	public RenderCanvas(IIR w) {
		parent = w;
		screen = parent.createImage(800,800);
	}
}

class Filter {
	double charge = 0;
	double flux = 0;
	double capacitance;
	double inductance;
	double resistance;
	double v_out = 0;
	double last_v_out = 0;
	double energy = 0;
	
	double tmpdc;
	double tmpdf;
	
	public Filter(double cap, double ind, double res) {
		capacitance = cap;
		inductance = ind;
		resistance = res;
	}
	
	public double iterate(double v_in, double timestep) {
		v_out = charge / capacitance;
		double c_ind = flux/inductance;
		energy = (charge*charge)/capacitance + (flux*flux)/inductance;
		double c_res = (v_in-v_out) / resistance;
		double dc = (c_res-c_ind) * timestep;
		double df = v_out * timestep;
		double nc = charge + dc;
		double nf = flux + df;
		c_res = (v_in-v_out) / resistance;
		tmpdc = (c_res - nf/inductance) * timestep;
		tmpdf = nc / capacitance * timestep;
		nc = charge + 0.5 * (dc + tmpdc);
		nf = flux + 0.5 * (df + tmpdf);
		charge = nc;
		flux = nf;
//		energy =Math.sqrt( v_out*v_out + (v_out - last_v_out)*(v_out - last_v_out)/timestep);
		last_v_out = v_out;
		return v_out;
	}
}

class EnvelopeDet {
	double lastvalue;
	double dconst;
//	double runningavg = 0;
//	double nums[];
//	int size = 500;
//	int id = 0;
	public EnvelopeDet() {
//		nums = new double[size];
		rst(0);
	}
	
	public void rst(double iconst) {
		lastvalue = 0;
		dconst = iconst;
//		id = 0;
//		for(int i = 0; i < size; i++)
//			nums[i] = 0;
	}
	
	public double iterate(double v_in) {
		double val = Math.abs(v_in);
		if (val > lastvalue)
			lastvalue = val;
		else
			lastvalue *= dconst;
/*		id++;
		if (id == size)
			id = 0;
		nums[id] = lastvalue;
		
		runningavg = 0;
		
		for(int i = 0; i < size; i++)
			runningavg += nums[i];
		
		runningavg /= size;
*/		
		return lastvalue;
	}
}
