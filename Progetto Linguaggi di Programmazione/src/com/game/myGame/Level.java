package com.game.myGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * This superclass extends the JPanel class and will be the model for each level.
 * It contains the audioManager so it will allow to play the music.
 * @author MatteoValerio
 *
 */
public class Level extends JPanel {

	private GameFrame frame;
	private String name;
	private AudioEffects audio;
	
	public static final int DELAY = 20;
	
	public static final int START_DELAY = 10000; // 10 seconds,in ms
	public static final int LEVEL_DELAY = 4000;
	
	public static final int ANIM_PERIOD = 400;
	public static final double SEQ_DURATION = 4;
	
	public static final String font = "Reprise Rehearsal Std";	// font to be used in this level

	
	/**
	 * Public constructor for the class. It takes the frame in which it will be displayed and a name for itself.
	 * @param frame The frame the manage this Level
	 * @param name The name of this Level
	 */
	public Level(GameFrame frame, String name) {
		this.frame = frame;
		this.name = name;
		audio = frame.getAudioEffects();
	}
	
	/**
	 * This method is to initialize the audio effects
	 */
	public void initAudioEffects() {
		audio = frame.getAudioEffects();
	}
	
	/**
	 * It returns the audioManager that is possible to use to reproduce music.
	 * @return The audioManager
	 */
	public AudioEffects getAudioEffects() {
		return audio;
	}
	
	/**
	 * Returns the name of the Level
	 * @return String name of the level
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the frame that manages the level
	 * @return GameFrame that manages the level
	 */
	public GameFrame getFrame() {
		return frame;
	}
	
	/**
	 * This private method is build to display easily the code in the init() method. Here we associate some actions that we want to happen
	 *  if a certain key is pressed.
	 * @param comp The JComponent from which we want to act the focus. In this case it will be the Menu.
	 * @param keyCode The key code correspondent to the the key we want to associate
	 * @param id An id we will use as bridge in the map. It is better if that it remembers the name of the key.
	 * @param listener An action listener that will manage the actions associated to the key.
	 */
	protected void addKeyBinding(JComponent comp, int keyCode, String id, boolean keyReleased, ActionListener listener) {
		InputMap im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = comp.getActionMap();
		
		im.put(KeyStroke.getKeyStroke(keyCode, 0, keyReleased), id);
		am.put(id, new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.actionPerformed(e);
			}
		});
		
	}
	
	
}
