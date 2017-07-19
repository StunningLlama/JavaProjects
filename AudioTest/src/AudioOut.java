import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AudioOut  {
	public static double val = 0.0;
	static int m = 20;
	static int[] scale = {-12, -10, -8, -7, -5, -3, -1, 0, 2, 4, 5, 7, 9, 11, 12};
	public static void main(String[] args) throws InterruptedException, LineUnavailableException {
		final int SAMPLING_RATE = 22000;            // Audio sampling rate
		final int SAMPLE_SIZE = 4;                  // Audio sample size in bytes

		SourceDataLine lineout;

		double fCyclePosition = 0;        

		AudioFormat format = new AudioFormat(SAMPLING_RATE, 16, 2, true, true);
		DataLine.Info sourceinfo = new DataLine.Info(SourceDataLine.class, format);

		if (!AudioSystem.isLineSupported(sourceinfo)){
			System.out.println("Line matching " + sourceinfo + " is not supported.");
			throw new LineUnavailableException();
		}

		lineout = (SourceDataLine)AudioSystem.getLine(sourceinfo);
		lineout.open(format);  
		lineout.start();
		

		ByteBuffer cBuf = ByteBuffer.allocate(lineout.getBufferSize());   

		// int ctSamplesTotal = SAMPLING_RATE*5;         // Output for roughly 5 seconds
		//while (ctSamplesTotal>0) {
		while (true) {
			double fCycleInc = 1.0/SAMPLING_RATE;
			cBuf.clear();
			int ctSamplesThisPass = lineout.available()/SAMPLE_SIZE;   
			for (int i=0; i < ctSamplesThisPass; i++) {
				cBuf.putShort((short)(func(fCyclePosition)*32767));
				cBuf.putShort((short)(func(fCyclePosition)*32767));

				fCyclePosition += fCycleInc;
			}
			lineout.write(cBuf.array(), 0, cBuf.position());            
			while (lineout.getBufferSize()/2 < lineout.available())   
				Thread.sleep(1);                                             
		}
		// line.drain();                                         
		// line.close();
	}
	
	
	
	static double func(double time) {
		//val += 0.004 * (Math.random() - 0.5);
		double v = 0;
		for (int i = 0; i < m; i++) {
			v += (1.0/(i+1)) * Math.sin(2.0*Math.PI*(Math.pow(2.0, scale[(int)Math.floor(time) % scale.length]/12.0))*time*i*50);
		}
		v *= 1.0/Math.log(m);
		return v;
		//double freq = Math.floor(time)*300.0;
		//return time*freq-Math.floor(time*freq);
	}
}
