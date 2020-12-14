package com.game.myGame;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * This class is meant to build an AudioEffects manager that will load the sounds effects for the game. It also gives some methods
 * for playing them and to modify the volume.
 * @author MatteoValerio
 *
 */
public class AudioEffects {
	
	private String [] url = {"sounds/floop2_x.wav", "sounds/shoot.wav", "sounds/evil spell.wav", "sounds/grunt.wav", "sounds/explosion.wav",
			"sounds/evilLaugh.wav","sounds/game over.wav","sounds/evilYou.wav","sounds/evilHell.wav" };
	private String [] names = {"menu","shoot","evilSpell","santaGrunt","evilGrunt","evilLaugh","gameOver","evilYou","evilHell"};
	private File file;
	private AudioInputStream input;
	private DataLine.Info info;
	private AudioFormat format;
	private HashMap<String, Clip> effects = new HashMap<>();
	private HashMap<String,AudioInputStream> inputStreams = new HashMap<>();
	
	/**
	 * The public constructor
	 */
	public AudioEffects() {
		init();
	}
	
	/**
	 * This private method initialize all the variables and loads the sounds
	 */
	private void init() {
		for(int i=0;i<url.length;i++) {
			/*file = new File(url[i]);
			if(!file.exists())
				return;*/
			try {
				InputStream in = getClass().getClassLoader().getResourceAsStream(url[i]);
				InputStream bufferedIn = new BufferedInputStream(in);
				input = AudioSystem.getAudioInputStream(bufferedIn);
				//input = AudioSystem.getAudioInputStream(file);
				format =input.getFormat();
				info = new DataLine.Info(Clip.class, format);
			
				Clip c = (Clip) AudioSystem.getLine(info);
				effects.put(names[i], c);
				inputStreams.put(names[i], input);
				c.open(input);
				effects.get(names[i]).open();
				
				setVolume(0.4f,names[i]);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				System.err.println("Unsopported action");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This public method plays a sound given the id
	 * @param id The id of the sound
	 */
	public void startSound(String id) {
		Clip c = effects.get(id);
		input = inputStreams.get(id);
		c.setFramePosition(0);
		if(!c.isOpen())
			try {
				c.open(input);
			} catch (LineUnavailableException | IOException e){
				System.err.println("Unsupported action");
				e.printStackTrace();
			}
		c.start();
	}
	
	
	/**
	 * This method modifies the volume of the sound, given the id
	 * @param volume The new volume level. It must be a value between 0 and 1.
	 * @param id The id of the sound to modify
	 */
	public void setVolume(float volume, String id) {
		Clip c = effects.get(id);
	    if (volume < 0f || volume > 1f)
	        throw new IllegalArgumentException("Volume not valid: " + volume);
	    FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);        
	    gainControl.setValue(20f * (float) Math.log10(volume));
	}
	

}
