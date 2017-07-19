

import java.nio.ByteBuffer;

import javax.sound.sampled.*;
import javax.swing.JFrame;


public class Beeper extends JFrame {

	static char[] data = "AudioFormat format = new AudioFormat(SAMPLING_RATE, 16, 1, true, true);".toCharArray();

	//static double BASE_FREQ = (520.0 + 5.0/6.0);
	static double BASE_FREQ = 500;
	
	public Beeper() {
		this.setSize(500, 500);
		//this.setLayout(manager);
	}
	
	public static void main(String[] args) throws InterruptedException, LineUnavailableException 
	{
		final int SAMPLING_RATE = 44100;            // Audio sampling rate
		final int SAMPLE_SIZE = 2;                  // Audio sample size in bytes

		SourceDataLine line;

		double fCyclePosition = 0;        

		AudioFormat format = new AudioFormat(SAMPLING_RATE, 16, 1, true, true);
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

		if (!AudioSystem.isLineSupported(info)){
			System.out.println("Line matching " + info + " is not supported.");
			throw new LineUnavailableException();
		}

		line = (SourceDataLine)AudioSystem.getLine(info);
		line.open(format);  
		line.start();

		ByteBuffer cBuf = ByteBuffer.allocate(line.getBufferSize());   

		// int ctSamplesTotal = SAMPLING_RATE*5;         // Output for roughly 5 seconds
		//while (ctSamplesTotal>0) {
		while (true) {
			double fCycleInc = 1.0/SAMPLING_RATE;
			cBuf.clear();
			int ctSamplesThisPass = line.available()/SAMPLE_SIZE;   
			for (int i=0; i < ctSamplesThisPass; i++) {
				cBuf.putShort((short)(func(fCyclePosition)*10000));

				fCyclePosition += fCycleInc;
			}
			line.write(cBuf.array(), 0, cBuf.position());            
			while (line.getBufferSize()/2 < line.available())   
				Thread.sleep(1);                                             
		}
		// line.drain();                                         
		// line.close();
	}

	static double func(double time) {
		int bit = (int) Math.floor(time * BASE_FREQ / 1.0);
		boolean one = false;
		if (bit/8 < data.length)
			one = (data[bit/8]&(2 << (bit%8))) > 0;
			else
				return 0;

		if (one) {
			return Math.sin(time*Math.PI*2.0*BASE_FREQ*4.0);
		} else {
			return Math.sin(time*Math.PI*2.0*BASE_FREQ*3.0);
		}
		//return Math.sin(time*400*Math.PI*2)*(Math.exp(-time));
	}

}