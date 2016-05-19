package targetDataLine;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import sun.audio.AudioStream;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("starting sound testing ...");
		
		try{
			
			AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
			
	        DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
	       
	        // check if line is supported 
	        if(!AudioSystem.isLineSupported(dataInfo)){
	        	System.err.println("The Line is not supported");
	        }
	        
	        final TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(dataInfo);
	        targetDataLine.open();
	        
	        // start recording from microphone
	        System.out.println("Starting recording ...");
			targetDataLine.start();
			
			// write recoreded audio into a file
			Thread thread = new Thread(){
				
				@Override
				public void run(){
					
					// get InputStream from TargetDataLine
					AudioInputStream ais = new AudioInputStream(targetDataLine);
					File file = new File("record.wav");
                    
                    	
                    // write to file
					try{ AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file); }
					catch(IOException ioe){ ioe.printStackTrace(); }
				}
			};
			
			thread.start();
			
			// record for 5 seconds
			Thread.sleep(5000);
			
			// stop recording
			targetDataLine.stop();
			targetDataLine.close();
		}
		catch(LineUnavailableException luae){ luae.printStackTrace(); }
		catch(InterruptedException ie){ ie.printStackTrace(); }
		
		System.out.println("Ending sound testing");
         
	}

}
