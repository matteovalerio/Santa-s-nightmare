package com.game.myGame;

import javax.sound.sampled.AudioFormat;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;


/**
 * This class is meant to manage the soundtracks of the game
 * @author MatteoValerio
 *
 */
public class AudioManager {

	private AudioInputStream input;
	private AudioFormat format;
	private String [] urlFile = {"sounds/MainTitle.wav","sounds/Level1.wav","sounds/ocean.wav","sounds/final.wav","sounds/endMusic.wav",
			"sounds/level win2.wav","sounds/level win.wav"} ;
	private String [] names = {"menu","level1","level2","level3","victory","levelWin2","levelWin"};
	private File [] file = new File[urlFile.length];
	private DataLine.Info info;
	private HashMap<String,Clip> audioClips = new HashMap<>();
	private HashMap<String,AudioInputStream> inputStreams = new HashMap<>();
	
	
	/**
	 * The public constructor
	 */
	public AudioManager() {
		init();
	}
	
	
	/**
	 * This private method initializes all the variables and loads all the tracks.
	 */
	private void init() {
		Clip c;

		for(int i=0;i<urlFile.length;i++) {

			input = null;
			info = null;
			format = null;
			try {				
				InputStream in = getClass().getClassLoader().getResourceAsStream(urlFile[i]);
				InputStream bufferedIn = new BufferedInputStream(in);
				input = AudioSystem.getAudioInputStream(bufferedIn);
				format = input.getFormat();
				info = new DataLine.Info(Clip.class, format);
				try {
					c = (Clip) AudioSystem.getLine(info);
					audioClips.put(names[i], c);
					inputStreams.put(names[i], input);
					System.out.println(names[i] +" "+urlFile[i]);
				}
				catch(LineUnavailableException e) {
					System.err.println("Clip non disponibile");
					e.printStackTrace();
				}
			}
			catch(UnsupportedAudioFileException e) {
				System.err.println("Formato audio non supportato");
				e.printStackTrace();
			}
			catch (IOException ex) {
				System.err.println("Problema di lettura del File");
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * This method allows to play a song in loop, given the id.
	 * @param id The id of the song
	 */
	public void playLoop(String id) {
		playSound(id);
		audioClips.get(id).loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	
	/**
	 * This method allows to play a song one time, given the id.
	 * @param id The id of the song
	 */
	public void playSound(String id) {
		Clip c = audioClips.get(id);
		input = inputStreams.get(id);
		c.setFramePosition(0);
		if(c!=null) {
			try {
				if(!c.isOpen())
					c.open(input);
				c.start();
			} catch (LineUnavailableException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method allows to stop the loop of the song, given the id.
	 * 
	 * @param id The id of the song
	 */
	public void stopLoop(String id) {
		Clip c = audioClips.get(id);
		if(c!=null) {
			c.stop();

		}
	}

}
