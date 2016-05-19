package MMC;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Main {

	public static Mixer mixer;
	public static Clip clip;
	
	public static void main(String[] args) {

		// get AudioSystem Mixers
		Mixer.Info[] mixInfos = AudioSystem.getMixerInfo();
		
		/**
		for(Mixer.Info info : mixInfos){
			System.out.println(info.getName() + " -- " + info.getDescription());
		}
		**/
		
		// get AudioSystem default Mixer 
		mixer = AudioSystem.getMixer(mixInfos[0]);
		
		DataLine.Info dataInfo = new DataLine.Info(Clip.class, null);
		try{ clip = (Clip) mixer.getLine(dataInfo); }
		catch(LineUnavailableException lue){ lue.printStackTrace(); }
		
		try{
			URL url = Main.class.getResource("/MMC/sound.wav");
			AudioInputStream ais = AudioSystem.getAudioInputStream(url);
			clip.open(ais);
		}
		catch(LineUnavailableException lua){ lua.printStackTrace(); }
		catch(UnsupportedAudioFileException uaf){ uaf.printStackTrace(); }
		catch(IOException io){ io.printStackTrace(); }
		
		clip.start();
		
		do{
			
			try{ Thread.sleep(50);} 
			catch(InterruptedException ie){ ie.printStackTrace(); }
			
		}while(clip.isActive());
			
	}

}
