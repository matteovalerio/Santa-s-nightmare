package com.game.myGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;


/**
 * This class represents the Menu. A Menu is an extension of the Level and it displays a background image, three options (new game, options, exit) 
 * that allows the player to start the game, modify certains parametres or to exit the game.
 * @author MatteoValerio
 *
 */
public class Menu extends Level {

	private final String [] titles = {"NEW GAME", "GUIDE", "EXIT"};
	private final String [] tutorial = {"ARROWS -> MOVE", "SPACE -> JUMP", "D -> SHOOT", "A -> RUN", "S -> SPECIAL SHOOT"}; 
	private int selected = 0;
	private BufferedImage background;
	private AudioEffects effects;
	public static final String font = "Reprise Rehearsal Std";
	private boolean isMenu;
	
	/**
	 * The public constructor. It calls the superclass constructor before initialize the graphical aspect of the Menu.
	 * @param frame The GameFrame that manages this Menu
	 */
	public Menu(GameFrame frame, String name) {
		super(frame, name);
		init();
	}
	
	/**
	 * This private method is build to have a cleaner view of the code. In this method all the graphical aspects of the Menu are
	 * initialized and also it is set all the KeyBindings for setting up the controls that the user can do. It is also called a method to 
	 * load the background and to initialize the audio.
	 */
	private void init() {
		setBackground(Color.BLACK);
		loadBackground();
		setFocusable(true);
		getFrame().getAudioManager().playLoop("menu");
		effects = getAudioEffects();
		isMenu = true;
		
		//setting all the key bindings
		addKeyBinding(this, KeyEvent.VK_UP, "up", (evt)->{
			if(isMenu) {
				effects.startSound("menu");
				selected--;
				if(selected<0)
					selected =0;
			}
		});
		addKeyBinding(this, KeyEvent.VK_DOWN,"down", (evt)->{
			if(isMenu) {
				effects.startSound("menu");
				selected++;
				if(selected>=titles.length)
					selected--;
			}
		});
		addKeyBinding(this, KeyEvent.VK_ENTER, "enter", (evt)->{
			if(isMenu) {	
				effects.startSound("menu");
				if(selected==0) {
					getFrame().nextLevel("level 3");
					getFrame().getAudioManager().stopLoop(getName());
				}
				else if(selected==1) 
					viewTutorial();
				else if(selected==2) {
					getFrame().dispose();
					getFrame().getAudioManager().stopLoop(getName());
					System.exit(0);
				}

			}
			
			else {
				isMenu = true;
			}
		});
		
		}

	/**
	 * It sets the variable isMenu to false so the tutorial is displayed.
	 */
	private void viewTutorial() {
		isMenu = false;
	}

	/**
	 * In this method it's build the image that will be displayed as background in the Menu
	 */
	private void loadBackground() {
		
		background = getFrame().getImageLoader().getImage("menuBackground");
	}

	
	/**
	 * This private method is build to display easily the code in the init() method. Here we associate some actions that we want to happen
	 *  if a certain key is pressed.
	 * @param comp The JComponent from which we want to act the focus. In this case it will be the Menu.
	 * @param keyCode The key code correspondent to the the key we want to associate
	 * @param id An id we will use as bridge in the map. It is better if that it remembers the name of the key.
	 * @param listener An action listener that will manage the actions associated to the key.
	 */
	private void addKeyBinding(JComponent comp, int keyCode, String id, ActionListener listener) {
		InputMap im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = comp.getActionMap();
		
		im.put(KeyStroke.getKeyStroke(keyCode, 0, false), id);
		am.put(id, new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.actionPerformed(e);
			}
		});
		
	}
	
	/**
	 * This method overrides the same in JPanel and it allows us to paint in the Menu all the graphical contents.
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g2d);
		g2d.drawImage(background,0,0,getWidth(),getHeight(),this);

		//select the font and the color
		setFont(new Font(font, Font.BOLD, 30));
		g2d.setColor(Color.WHITE);
		
		if(isMenu) {
			int x = getWidth()-(getWidth()/5);
			int y = getHeight()/4;
			
			g2d.setFont(new Font(font, Font.BOLD, 70));
			g2d.drawString("SANTA'S NIGHTMARE", x/2, 70);
			
			g2d.setFont(new Font(font, Font.BOLD, 30));
			
			for(int i=0;i<titles.length;i++) {
				if(i== selected)
					g2d.setColor(Color.RED);
				else
					g2d.setColor(Color.WHITE);
				g2d.drawString(titles[i], x, y+y*i);
			}
		}
		
		else {
			int x = getWidth()-(getWidth()/5);
			int y = getHeight()/6;
			
			g2d.setFont(new Font(font, Font.BOLD, 70));
			g2d.drawString("COMMANDS", x/2, 70);
			g2d.setFont(new Font(font, Font.BOLD, 30));
			for(int i =0;i<tutorial.length;i++) {
				g2d.drawString(tutorial[i], x, y+y*i);
			}
			g2d.setColor(Color.RED);
			g2d.drawString("BACK TO MENU -> ENTER", x, y+y*tutorial.length);
		}
	}
	
	

}
