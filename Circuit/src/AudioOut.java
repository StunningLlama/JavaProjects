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

public class AudioOut extends Thread {
	public static double val = 0.0;
	double fCyclePosition = 0;
	double fCycleInc = 0.0;
	boolean put = true;
	int ctSamplesThisPass = 0;
	int i = 0;
	final int SAMPLING_RATE = 44100;            // Audio sampling rate
	final int SAMPLE_SIZE = 2;                  // Audio sample size in bytes

	ByteBuffer cBuf;
	SourceDataLine lineout = null;

	public static AudioOut INSTANCE = null;
	
	public AudioOut() {
		super();
		AudioOut.INSTANCE = this;
	}
	
	@Override
	public void run() {
		AudioFormat format = new AudioFormat(SAMPLING_RATE, 16, 1, true, true);
		DataLine.Info sourceinfo = new DataLine.Info(SourceDataLine.class, format);

		if (!AudioSystem.isLineSupported(sourceinfo)){
			System.out.println("Line matching " + sourceinfo + " is not supported.");
		}

		try {
			lineout = (SourceDataLine)AudioSystem.getLine(sourceinfo);
			lineout.open(format, 1024);
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		lineout.start();
		

		cBuf = ByteBuffer.allocate(lineout.getBufferSize());
		fCycleInc = 1.0/SAMPLING_RATE;
		ctSamplesThisPass = lineout.available()/SAMPLE_SIZE;   
	}
	
	public void putVal(double val) {
		if (!put)
			return;
		cBuf.putShort((short)(val*1000));
		fCyclePosition += fCycleInc;
		i++;
		if (i >= ctSamplesThisPass) {
			put = false;
			lineout.write(cBuf.array(), 0, cBuf.position());   
			cBuf.clear();
			ctSamplesThisPass = lineout.available()/SAMPLE_SIZE;   
			i = 0;
			put = true;
		}
	}
}
