

import java.nio.ByteBuffer;
import javax.sound.sampled.*;


public class Beeper {
	
	static double[] harmonics = {1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
	
	static int[] pitch = {0, 2, 4, 5, 7, 9, 11, 12};
	static int[] duration = {};
	
	static int index = 0;
	
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
		double total = 0.0;
		if (time > duration[index])
			index++;
		double freq = 440*Math.pow(2.0, pitch[index]/12.0);
		for (int i = 0; i < harmonics.length; i++) {
			total += harmonics[i]*Math.sin(time*Math.PI*2*(i+1)* freq);
		}
		return total;
		//return Math.sin(time*400*Math.PI*2)*(Math.exp(-time));
	}

}