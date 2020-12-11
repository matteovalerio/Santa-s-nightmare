package com.game.myGame;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * In this class we build the Frame that will contain the game and manage the switches between the levels.
 * It doesn't contain the main method.
 * @author MatteoValerio
 *
 */

public class GameFrame extends JFrame {

		
	private JPanel mainPanel; //the Panel that contains the game
	private CardLayout card;	//the layout for the game
	
	private Menu menu;
	private AudioManager audio;
	private AudioEffects effects;
	private ImageLoader imgL;
	
	
	/**
	 * public constructor for the Frame that will contain the game.
	 */
	public GameFrame() {
		init();
	}
	
	
	/**
	 * The initialization of all the variables.
	 */
	private void init() {
		initImages();
		effects = new AudioEffects();
		audio = new AudioManager();
		
		mainPanel = new JPanel();
		
		menu = new Menu(this,"menu");
		
		

	//	audio.playLoop(menu.getName());
		buildFrame();
	}
	
	/**
	 * This method loads all the images for the game. It is better to load everything before 
	 * the game starts.
	 */
	private void initImages() {
		imgL = new ImageLoader();
		ArrayList<BufferedImage> imgList = new ArrayList<>();
		ArrayList<String> nameList = new ArrayList<>();
		
		// loading santa img 
		String name = "die";
		imgList = imgL.loadSheetImages("res/img/santa/"+name, 6);
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		name="punch";
		imgList = imgL.loadSheetImages("res/img/santa/"+name, 5);
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		name="run";
		imgList = imgL.loadSheetImages("res/img/santa/"+name, 6);
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		name="shoot";
		imgList = imgL.loadSheetImages("res/img/santa/"+name, 5);
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		name="stand";
		imgList = imgL.loadSheetImages("res/img/santa/"+name, 1);
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		name="down";
		imgList = imgL.loadSheetImages("res/img/santa/"+name, 1);
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		name="sub";
		imgList = imgL.loadSheetImages("res/img/santa/"+name, 4);
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		name="explosion";
		imgList = imgL.loadSheetImages("res/img/"+name, 1);
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		name="fly";
		imgList = imgL.loadSheetImages("res/img/santa/"+name, 2);
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		imgL.linkName("santa", nameList);
		
		//loading evil wizard img
		name="eStand";
		imgList = imgL.loadSheetImages("res/img/evil wizard/"+name, 1);
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		name="evilShip";
		imgList = imgL.loadSheetImages("res/img/evil wizard/"+name, 1);
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		name="skull";
		imgList = imgL.loadSheetImages("res/img/evil wizard/"+name, 3);
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		
		imgL.linkName("evil wizard", nameList);
		
		//loading fireballs img
		name="fire";
		imgList = imgL.loadSheetImages("res/img/"+name, 1);
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		name="blast";
		imgList = imgL.loadSheetImages("res/img/"+name, 1);
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		name="bomb";
		imgList = imgL.loadSheetImages("res/img/"+name, 1);
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		
		imgL.linkName("fire", nameList);
		
		//loading present img
		name="present";
		imgList = imgL.loadSheetImages("res/img/"+name, 1);
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		name="specialPresent";
		imgList = imgL.loadSheetImages("res/img/"+name, 1);
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		imgL.linkName("present", nameList);
		
		//loading background img
		name="menuBackground";
		imgList = new ArrayList<BufferedImage>();
		imgList.add(imgL.loadImage("res/img/menuBackground.jpg"));
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		name="background level1";
		imgList = new ArrayList<BufferedImage>();
		imgList.add(imgL.loadImage("res/img/background level1.jpg"));
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		name="background level2";
		imgList = new ArrayList<BufferedImage>();
		imgList.add(imgL.loadImage("res/img/background level2.jpg"));
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		name="background level3";
		imgList = new ArrayList<BufferedImage>();
		imgList.add(imgL.loadImage("res/img/background level3.jpg"));
		imgL.insertImages(name, imgList);
		nameList.add(name);
		
		
	}


	/**
	 * This private method build graphically the Frame
	 */
	private void buildFrame() {
		setExtendedState(MAXIMIZED_BOTH);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUndecorated(true);
		setTitle("A Christmas in danger");
		setLocationRelativeTo(null);
		
		//creates the card layout 
		mainPanel.setPreferredSize(getSize());
		card =  new CardLayout();
		mainPanel.setLayout(card);

		mainPanel.add("menu",menu);
        setLayout(new BorderLayout());
        add(mainPanel,BorderLayout.CENTER);
		//and start to show the first panel
        

	}
	
	
	/**
	 *This method changes level displayed given his name  
	 * 	@param levelName The name of the level you want to see
 	*/
	protected void nextLevel(String levelName) {
		
			switch (levelName) {
			case "level 1": {
				mainPanel.add(levelName, new Level1(this, levelName));
				break;
			}
			
			case "level 2":{
				mainPanel.add(levelName, new Level2(this,levelName));
				break;
			}
			
			case "level 3":{
				mainPanel.add(levelName, new Level3(this,levelName));
				break;
			}
			
			case "menu": {
				menu = new Menu(this, levelName);
				audio.stopLoop("level 1");
				audio.playLoop(levelName);
				mainPanel.add(levelName, menu);
				break;
			}
			
			
		}
			card.show(mainPanel, levelName);
	}
	
	
	/**
	 * This method returns the AudioEffects object
	 * @return AudioEffects 
	 */
	public AudioEffects getAudioEffects() {
			return effects;
	}
	
	/**
	 * This method returns the AudioManager object
	 * @return AudioManager
	 */
	public AudioManager getAudioManager() {
		return audio;
	}
	
	/**
	 * This method returns the ImageLoader object
	 * @return ImageLoader
	 */
	public ImageLoader getImageLoader() {
		return imgL;
	}
	
}
