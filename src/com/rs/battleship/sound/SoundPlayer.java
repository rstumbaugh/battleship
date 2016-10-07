/**
 * @author Ryan
 * @date Aug 22, 2013
*/
package com.rs.battleship.sound;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SoundPlayer 
{
	private final String SOUND_PATH = "/com/rs/battleship/res/";
	
	private SourceDataLine line = null;
	private byte[] soundBytes;
	private int bytesRead;
	
	/**
	 * Reads sound bytes from the sound file
	 * @param fileName
	 */
	public SoundPlayer(String fileName)
	{
		InputStream fileStream = null;
		AudioInputStream audioInStream = null;
	
		try {
			fileStream = getClass().getResourceAsStream(SOUND_PATH + fileName);
			
			audioInStream = AudioSystem.getAudioInputStream(fileStream);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		AudioFormat audioFormat = audioInStream.getFormat();
	    DataLine.Info info = new DataLine.Info(SourceDataLine.class,
	                         audioFormat);
		
	    try {
			line = (SourceDataLine)AudioSystem.getLine(info);
			line.open(audioFormat);
		} catch (LineUnavailableException e) 
		{
			e.printStackTrace();
		}
	    
	    line.start();
	    
	    URL url = getClass().getResource(SOUND_PATH + fileName);
	    File f = null;
	    try {
			f = new File(url.toURI());
		} catch (URISyntaxException e1)
		{
			e1.printStackTrace();
		}
		soundBytes = new byte[(int)f.length()];
		
		try
	    {
			bytesRead = audioInStream.read(soundBytes, 0, soundBytes.length);
	    }
	    catch (IOException e)
	    {
	    	e.printStackTrace();
	    }
		
		try {
			audioInStream.close();
			fileStream.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Plays sound clip
	 */
	public void play()
	{
	    line.write(soundBytes, 0, bytesRead);
	    line.close();
	}
}
