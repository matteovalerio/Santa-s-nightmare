package com.game.myGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


/**
 * This class represents the first level of the game. It has a thread that runs and manages the animation.
 * Here the keyboards control are set and also some statistics are stored.
 * @author MatteoValerio
 *
 */
public class Level1 extends Level implements Runnable {

	private boolean running;
	private boolean gameOver;
	private volatile boolean isPaused = false;
	private Thread animator;
	
	private Graphics2D dbg;
	private Image dbimg = null;
	
	private long startTime;
	private long currentTime;
	private long secondsPassed;
	private long minutesPassed;
	private boolean skip = false;
	
	private BufferedImage background;
	
	
	
	private Santa santa;
	private Enemy wizard;
	

	
	/**
	 * Public constructor. It takes the frame that called him and a name.
	 * @param frame The GameFrame that invoked him
	 * @param name The name of the level
	 */
	public Level1(GameFrame frame, String name) {
		super(frame, name);
		running = false;
		gameOver = false;
		init();
	}
	
	
	/**
	 * Private method that initialize the game. It loads the audio, background and build the characters.
	 */
	private void init() {
		setBackground(Color.WHITE);
		
		loadBackground();
		
		setFocusable(true);
		requestFocus();
		
		//SOUNDS SETTINGS
		getFrame().getAudioManager().playLoop("level1");
		


	
		//creates all the key bindings for this level
		
		addKeyBinding(this, KeyEvent.VK_RIGHT, "right", false, (evt)->{
			santa.move(true);
			santa.setImage("run");
			santa.startLooping(ANIM_PERIOD, SEQ_DURATION);	//warning
		});
		
		addKeyBinding(this, KeyEvent.VK_RIGHT, "rightReleased", true, (evt)->{
			santa.stop(true);
			santa.setImage("stand");
			santa.startLooping(ANIM_PERIOD, SEQ_DURATION);
		});
		
		addKeyBinding(this, KeyEvent.VK_LEFT, "leftReleased", true, (evt)->{
			santa.setImage("stand");
			santa.stop(false);
			santa.startLooping(ANIM_PERIOD, SEQ_DURATION);
		});
		
		addKeyBinding(this, KeyEvent.VK_LEFT, "left", false, (evt)->{
			santa.setImage("run");
			santa.move(false);
			santa.startLooping(ANIM_PERIOD, SEQ_DURATION);	//warning
		});
		
		addKeyBinding(this, KeyEvent.VK_SPACE, "spaceReleased", true, (evt)->{
			boolean direction = santa.isRightDirection();
			santa.setImage("stand");
			santa.setDirection(direction);
			santa.updateSprite();
			santa.startLooping(ANIM_PERIOD, SEQ_DURATION);
		});
		
		addKeyBinding(this, KeyEvent.VK_SPACE, "space", false, (evt)->{
			boolean direction = santa.isRightDirection();
			santa.setImage("stand");
			santa.setDirection(direction);
			santa.jump();
			santa.startLooping(ANIM_PERIOD, SEQ_DURATION);	//warning
		});
		
		addKeyBinding(this, KeyEvent.VK_A, "stopRun", true, (evt)->{
			boolean direction = santa.isRightDirection();
			santa.setImage("stand");
			santa.stop(direction);
			santa.startLooping(ANIM_PERIOD, SEQ_DURATION);
		});
		
		addKeyBinding(this, KeyEvent.VK_A, "run", false, (evt)->{
			boolean direction = santa.isRightDirection();
			santa.setImage("run");
			santa.run(direction);
			santa.startLooping(ANIM_PERIOD, SEQ_DURATION);	//warning
		});
		
		addKeyBinding(this, KeyEvent.VK_D, "stopFire", true, (evt)->{
			santa.setImage("stand");
			santa.updateSprite();
			santa.startLooping(ANIM_PERIOD, SEQ_DURATION);
		});
		
		addKeyBinding(this, KeyEvent.VK_D, "fire", false, (evt)->{
			santa.setImage("shoot");
			getFrame().getAudioEffects().startSound("shoot");
			santa.fire();
			santa.startLooping(ANIM_PERIOD, SEQ_DURATION);
		});
		
		addKeyBinding(this, KeyEvent.VK_S, "stopSpecial", true, (evt)->{
			santa.setImage("stand");
			santa.updateSprite();
			santa.startLooping(ANIM_PERIOD, SEQ_DURATION);
		});
		
		addKeyBinding(this, KeyEvent.VK_S, "special", false, (evt)->{
			if(santa.specialShootAvailable()) {
				santa.setImage("shoot");
				getFrame().getAudioEffects().startSound("shoot");
				santa.specialFire();
			}				
			santa.startLooping(ANIM_PERIOD, SEQ_DURATION);
		});
		
		addKeyBinding(this, KeyEvent.VK_ENTER, "skip", false, (evt)->{
			skip=true;
		});
		
		
	}
	
	/**
	 * Loads the background image for this level
	 */
	private void loadBackground() {
		background = getFrame().getImageLoader().getImage("background level1");
	}

	@Override
	public void addNotify() {
		super.addNotify();
		startLevel();
	}
	
	
	
	/**
	 * Invoked to start the thread and the level
	 */
	private void startLevel() {
		if(animator==null || running ==false) {
			running = true;
			animator = new Thread(this);
			animator.start();
		}
	}

	@Override
	/**
	 * The loop of the level. Set the loop of the animation of the sprites before starting. Each loop it will call
	 * gameUpdate() for the logic, gameRender() for the graphics and then paintScreen() to visualize the effects. 
	 * It will also adjourn the statistics with storeStats()
	 */
	public void run() {
		getFrame().getAudioEffects().startSound("evilLaugh");

		while(!skip) {
			startRender();
			paintScreen();
		}
		running = true;
		startTime = System.currentTimeMillis();
		
		initCharacters();
		santa.startLooping(ANIM_PERIOD, SEQ_DURATION);
		
		while(running) {
			gameUpdate();
			gameRender();
			paintScreen();
			try {
				Thread.sleep(DELAY);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			storeStats();
		}
		
	}
	
	/**
	 * Initialize the characters, building the objects
	 */
	private void initCharacters() {
		wizard = new Enemy(getFrame().getWidth(), getFrame().getHeight(), "eStand", getFrame().getImageLoader(),getFrame().getWidth(),1);
		santa = new Santa(0, getFrame().getHeight(), "stand", getFrame().getImageLoader(), getFrame().getWidth()-(wizard.getWidth()*2), getName());
		wizard.startTimer();
	}
	
	/**
	 * It displays the starting image for level 1
	 */
	private void startRender() {
		if(dbimg == null) {
			dbimg = createImage(getFrame().getWidth(),getFrame().getHeight());
			if(dbimg == null) {
				System.err.println("dbimg is null");
				return;
			}//if
			else {
				dbg = (Graphics2D)dbimg.getGraphics();
			}//else
		}//if
		dbg.setColor(Color.BLACK);
		dbg.fillRect(0, 0, getFrame().getWidth(), getFrame().getHeight());
		dbg.setFont(new Font(font,Font.BOLD,70));
		dbg.setBackground(Color.BLACK);
		dbg.setColor(Color.WHITE);
		int x = 0;
		int y = getFrame().getHeight()/6;
		dbg.setColor(Color.RED);
		dbg.drawString("Dear Santa...", x, y);
		x = getWidth()/4;
		y = getHeight()*2/5;
		
		dbg.setFont(new Font(font,Font.BOLD,25));
		dbg.setColor(Color.WHITE);
		dbg.drawString("last year you stole my Christmas, giving me just coal!",x,y);
		y = getHeight()*3/5;
		dbg.drawString("Now I stole something from you: if you want them back, meet me in the forest..alone!!", x, y);
		
		y = getHeight()*4/5;
		dbg.setColor(Color.RED);
		dbg.setFont(new Font("French Script MT", Font.BOLD,50));
		dbg.drawString("A Friend?", x, y);
		dbg.setFont(new Font(font, Font.BOLD,20));
		dbg.setColor(Color.WHITE);
		y = getHeight()*6/7;
		dbg.drawString("ENTER TO SKIP", x*2, y);
		dbg.setFont(new Font(font,Font.BOLD,70));
		dbg.setColor(Color.RED);
		dbg.drawString("LEVEL 1: THE FOREST", getFrame().getWidth()/3, 70);
		
		
	}

	/**
	 * Called to adjourn the timing statistics. When the seconds
	 * will reach 60 it will add 1 to the minutes and restart counting
	 */
	private void storeStats() {
		currentTime = System.currentTimeMillis();
		secondsPassed = (currentTime-startTime)/1000L; // in seconds
		if(secondsPassed>=60) {
			startTime = currentTime;
			minutesPassed++;
		}
	}

	/**
	 * Invoked in the run() method. It creates a not visible screen for buffering the contents.
	 * After a check at the beginning, it will first paint the statistics, including the life of santa and of the enemy
	 * then it will paint santa, the enemy and all the bullets fired.
	 */
	private void gameRender() {
		if(dbimg == null) {
			dbimg = createImage(getFrame().getWidth(),getFrame().getHeight());
			if(dbimg == null) {
				System.err.println("dbimg is null");
				return;
			}//if
			else {
				dbg = (Graphics2D)dbimg.getGraphics();
			}//else
		}//if
		
		dbg.setColor(Color.WHITE);
		dbg.fillRect(0, 0, getFrame().getWidth(), getFrame().getHeight());
		
	
		dbg.drawImage(background, 0, 0, getFrame().getWidth(), getFrame().getHeight()*2/3, this);
		

		dbg.setFont(new Font(font,Font.BOLD,30));
		Color c = new Color(228,52,27);	//creates a custom red color
		dbg.setColor(c);
		if(minutesPassed==0)
			dbg.drawString("Time: " + secondsPassed + "s", 100, 100);
		else
			dbg.drawString("Time: " + minutesPassed + "m" +  secondsPassed + "s", 100, 100);
	
		dbg.drawString("Santa: "+santa.getLifePercent()+"%"+"     The Guy: "+wizard.getLifePercent()+"%", getWidth()*2/5, 100);
		
		if(santa.specialShootAvailable()) {
			BufferedImage temp = getFrame().getImageLoader().getImage("specialPresent");
			dbg.drawImage(temp,getWidth()*2/5, 120, 25 , 25, this);		
			}
		

		
		//draw only the present that are visibles, the others are removed from the list of presents available for Santa
		ArrayList<Present> p = santa.getPresents();
		if(p!=null) {
			for(int i = 0;i<p.size();i++) {
				if(p.get(i).getPosX()>getWidth() || !(p.get(i).isVisible())) {
					p.remove(i);
				}
				else
					p.get(i).paintSprite(dbg);
			}//for
		}//if
		
		ArrayList<Fire> f = wizard.getFires();
		if(f!=null) {
			for(int i = 0;i<f.size();i++) {
				if(f.get(i).getPosX()>getWidth() || !(f.get(i).isVisible())) {
					f.remove(i);
				}
				else
					f.get(i).paintSprite(dbg);
			}//for
		}
		santa.paintSprite(dbg);
		wizard.paintSprite(dbg);
		
		if(gameOver) {
			getFrame().getAudioManager().stopLoop("level1");
			if(santa.getLife()>0) {
				getFrame().getAudioManager().playSound("levelWin");
				gameOverMessage(dbg,"you win");
				getFrame().nextLevel("level 2");
				
			}
			else {
				santa.setImage("die");
				getFrame().getAudioEffects().startSound("gameOver");
				gameOverMessage(dbg,"you lose");

				getFrame().nextLevel("menu");

			}
		}
	}//game render
	

	/**
	 * Method called when the game is over:
	 * it can happen when you win or when you lose the level.
	 * @param dbg2 The graphics used to paint the message
	 */
	private void gameOverMessage(Graphics2D dbg2, String msg) {
		
		//calcola posizione x e y e messaggio msg
		int x = getFrame().getWidth()*2/5;
		int y = getFrame().getHeight()/2;
		
		
		dbg2.setFont(new Font(font,Font.BOLD,70));


		dbg2.setColor(Color.RED);
		dbg2.drawString(msg, x, y);
		if(msg.equals("you win")){
			BufferedImage img = getFrame().getImageLoader().getImage("victory1");
			dbg2.drawImage(img,x+100,y,400,400,this);
			paintScreen();
			try {
				Thread.sleep(LEVEL_DELAY);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			
	
			dbg2.setColor(Color.BLACK);
			dbg2.fillRect(0, 0, getFrame().getWidth(), getFrame().getHeight());
			dbg2.setFont(new Font(font,Font.BOLD,70));
			dbg2.setBackground(Color.BLACK);
			
			dbg2.setColor(Color.RED);
			getFrame().getAudioManager().playSound("levelWin2");
			y = getFrame().getHeight()/5;
			x=getFrame().getWidth()/5;
			msg = "You found the letters!!";
			dbg2.drawString(msg, x, y);
			img = getFrame().getImageLoader().getImage("letters");
			
			dbg2.drawImage(img,x+100,y,600,600,this);
		}
		paintScreen();
		
		try {
			Thread.sleep(START_DELAY);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	

	/**
	 * This method actively render the buffered image to the screen. 
	 */
	private void paintScreen() {
		Graphics g;
		try {
			g = this.getGraphics( ); // get the panel's graphic context
			if ((g != null) && (dbimg != null))
				g.drawImage(dbimg, 0, 0, null);
			Toolkit.getDefaultToolkit( ).sync( ); // sync the display on some systems
			g.dispose( );
		} catch (Exception e){ 
			System.out.println("Graphics context error: " + e); }
	} 
	// end of paintScreen( )

	
	/**
	 * This method updates all the sprites (movements variables..)
	 */
	private void gameUpdate() {
		if(!gameOver && !isPaused) {
			santa.updateSprite();
			checkCollision();
		}
	}
	
	/**
	 * This method checks if there are any collision between presents/wizard and fires/santa
	 */
	private void checkCollision() {
		Rectangle sR = santa.getMyRectangle();
		Rectangle eR = wizard.getMyRectangle();
		Rectangle pR,fR;
		ArrayList<Present> present = santa.getPresents();
		ArrayList<Fire> fires = wizard.getFires();
		
		Fire f;
		Present p;
		
		//fires santa
		for(int i =0;i<fires.size();i++) {
			f = fires.get(i);
			fR = f.getMyRectangle();
			if(fR.intersects(sR)) {
				int life = santa.getHit(f.getDamage());
				getFrame().getAudioEffects().startSound("santaGrunt");
				if(life<=0) {
					gameOver = true;
					running = false;
					int wSanta = santa.getWidth();
					int hSanta = santa.getHeight();
					santa.setImage("explosion");
					santa.setImageDimension(new Dimension(wSanta,hSanta));
				}
				f.setVisible(false);
			}
			// spell/present and present/wizard
			for(int j=0;j<present.size();j++) {
				p = present.get(j);
				pR = p.getMyRectangle();
				if(pR.intersects(fR)) {
					if(p.isSpecial()) {
						f.setVisible(false);
					}
					else
						p.setVisible(false);
				}
				else if(pR.intersects(eR) && p.isVisible()) {
					int life = wizard.hit(p.getDamage());
					getFrame().getAudioEffects().startSound("evilGrunt");
					santa.incrementHitCounter();
					p.setVisible(false);
					if(life<=0) {
						gameOver = true;
						running = false;
					}
				}
			}
		}	
	}
	
	

	
	
}
