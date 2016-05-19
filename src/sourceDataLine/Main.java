package sourceDataLine;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import sun.audio.AudioStream;

public class Main {

	/*
	 * Record from the microphone and then playback what recoreded
	 */
	public static void main(String[] args) {

		AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
		
		try{
		
			DataLine.Info dataInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
			final SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(dataInfo);
			sourceLine.open();
			
			dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
			final TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(dataInfo);
			targetLine.open();
			
			// handle buffer bytes - have no fixed length
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			Thread sourceThread = new Thread(){
				
				@Override 
				public void run(){
					
					sourceLine.start();
					while(true){
						
						// write from targetLine to sourceLine
						sourceLine.write(out.toByteArray(), 0, out.size());
					}
				}
			};
			
			Thread targetThread = new Thread(){
				
				@Override
				public void run(){
					
					targetLine.start();
					byte[] data = new byte[targetLine.getBufferSize() / 5];
					int readByte;
					while(true){
						
						// read from targetLine (microphone) 
						// and return number of bytes putted in the buffer
						readByte = targetLine.read(data, 0, data.length);
						
						// write into ByteArray 
						out.write(data, 0, readByte);
						
					}
				}
			};
			
			targetThread.start();
			System.out.println("starting recording ...");
			Thread.sleep(5000);
			targetLine.stop();
			targetLine.close();
			System.out.println("Ending recording ...");
			
			sourceThread.start();
			System.out.println("starting playback ...");
			Thread.sleep(5000);
			sourceLine.stop();
			sourceLine.close();
			System.out.println("Ending playback");
			
		}
		catch(LineUnavailableException lue){ lue.printStackTrace(); }
		catch(InterruptedException ie){ ie.printStackTrace(); }
	}

}
