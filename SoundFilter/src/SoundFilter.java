

import java.nio.ByteBuffer;
import javax.sound.sampled.*;


public class SoundFilter {
	
	static byte[] storage = new byte[44100*20];
	
	public static void main(String[] args) throws InterruptedException, LineUnavailableException 
	{
		final int SAMPLING_RATE = 44100;            // Audio sampling rate
		final int SAMPLE_SIZE = 2;                  // Audio sample size in bytes

		SourceDataLine lineout;
		TargetDataLine linein;

		double fCyclePosition = 0;        

		AudioFormat format = new AudioFormat(SAMPLING_RATE, 16, 1, true, true);
		DataLine.Info sourceinfo = new DataLine.Info(SourceDataLine.class, format);
		DataLine.Info targetinfo = new DataLine.Info(TargetDataLine.class, format);

		if (!AudioSystem.isLineSupported(sourceinfo)){
			System.out.println("Line matching " + sourceinfo + " is not supported.");
			throw new LineUnavailableException();
		}
		if (!AudioSystem.isLineSupported(targetinfo)){
			System.out.println("Line matching " + targetinfo + " is not supported.");
			throw new LineUnavailableException();
		}

		lineout = (SourceDataLine)AudioSystem.getLine(sourceinfo);
		lineout.open(format);  
		lineout.start();
		
		linein = (TargetDataLine)AudioSystem.getLine(targetinfo);
		linein.open(format);
		linein.start();

		ByteBuffer cBuf = ByteBuffer.allocate(lineout.getBufferSize());   

		// int ctSamplesTotal = SAMPLING_RATE*5;         // Output for roughly 5 seconds
		//while (ctSamplesTotal>0) {
		
		
		byte[] bbuf = new byte[882];
		for (int i = 0; i < 44100*20; i += 882) {
			linein.read(bbuf, 0, 882);
			System.arraycopy(bbuf, 0, storage, i, 882);
		}
		
		while (fCyclePosition < 1.0) {
			double fCycleInc = 1.0/SAMPLING_RATE;
			cBuf.clear();
			int ctSamplesThisPass = lineout.available()/SAMPLE_SIZE;   
			for (int i=0; i < ctSamplesThisPass; i++) {
				cBuf.putShort((short)(Math.sin(2000.0*fCyclePosition)*5000));

				fCyclePosition += fCycleInc;
			}
			lineout.write(cBuf.array(), 0, cBuf.position());            
			while (lineout.getBufferSize()/2 < lineout.available())   
				Thread.sleep(1);                                             
		}
		fCyclePosition = 0;  
		
		while (true) {
			double fCycleInc = 1.0/SAMPLING_RATE;
			cBuf.clear();
			int ctSamplesThisPass = lineout.available()/SAMPLE_SIZE;   
			for (int i=0; i < ctSamplesThisPass; i++) {
				cBuf.putShort((short)(func(fCyclePosition)*10000));

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
		double x = (storage[(int)(time*44100.0)*2]*256 + storage[(int)(time*44100.0)*2 + 1])/32768.0;
		return Math.exp(x);
	}

}