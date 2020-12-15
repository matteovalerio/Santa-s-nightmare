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

public class Level3 extends Level implements Runnable{

	private boolean running;
	private boolean gameOver;
	private BufferedImage background;
	private Graphics2D dbg;
	private Image dbimg = null;
	private final Color BACKGROUND_COLOR = new Color(180,236,252);
	private long currentTime;
	private long startTime;
	private long secondsPassed;
	private int minutesPassed;
	
	private boolean skip = false;

	
	private Thread animator;
	private boolean isPaused;
	private Santa santa;
	private Krampus wizard;
	
	/**
	 * Public constructor
	 * @param frame GameFrame 
	 * @param name Name of the level
	 */
	public Level3(GameFrame frame, String name) {
		super(frame, name);
		running = false;
		gameOver = false;
		init();
	}

	/**
	 * Initialize the level
	 */
	private void init() {
		setBackground(Color.BLACK);
		loadBackground();
		
		setFocusable(true);
		requestFocus();
		

		
		//creates all the key bindings for this level
		
		addKeyBinding(this, KeyEvent.VK_RIGHT, "right", false, (evt)->{
			santa.move(true);
			santa.startLooping(400, 4);	//warning
		});
		
		addKeyBinding(this, KeyEvent.VK_RIGHT, "rightReleased", true, (evt)->{
			santa.stop(true);
			santa.startLooping(400, 4);
		});
		
		addKeyBinding(this, KeyEvent.VK_LEFT, "leftReleased", true, (evt)->{
			santa.stop(false);
			santa.startLooping(400, 4);
		});
		
		addKeyBinding(this, KeyEvent.VK_LEFT, "left", false, (evt)->{
			santa.move(false);
			santa.startLooping(400, 4);	//warning
		});
		
		addKeyBinding(this, KeyEvent.VK_UP, "upReleased", true, (evt)->{
			boolean rightDirection = santa.isRightDirection();
			santa.stop(rightDirection);
			santa.startLooping(400, 4);
		});
		
		addKeyBinding(this, KeyEvent.VK_UP, "up", false, (evt)->{
			boolean rightDirection = santa.isRightDirection();
			santa.flyUp(rightDirection);
			santa.startLooping(400, 4);	//warning
		});

		addKeyBinding(this, KeyEvent.VK_DOWN, "downReleased", true, (evt)->{
			boolean rightDirection = santa.isRightDirection();
			santa.stop(rightDirection);
			santa.startLooping(400, 4);
		});
		
		addKeyBinding(this, KeyEvent.VK_DOWN, "down", false, (evt)->{
			boolean rightDirection = santa.isRightDirection();
			santa.moveDown(rightDirection);
			santa.startLooping(400, 4);	//warning
		});
		
		addKeyBinding(this, KeyEvent.VK_A, "stopRun", true, (evt)->{
			boolean direction = santa.isRightDirection();
			santa.stop(direction);
			santa.startLooping(400, 4);
		});
		
		addKeyBinding(this, KeyEvent.VK_A, "run", false, (evt)->{
			boolean direction = santa.isRightDirection();
			santa.run(direction);
			santa.startLooping(400, 4);	//warning
		});
		
		addKeyBinding(this, KeyEvent.VK_D, "stopFire", true, (evt)->{
			santa.updateSprite();
			santa.startLooping(400, 4);
		});
		
		addKeyBinding(this, KeyEvent.VK_D, "fire", false, (evt)->{
			getFrame().getAudioEffects().startSound("shoot");
			santa.fire();
			santa.startLooping(400, 4);
		});
		
		addKeyBinding(this, KeyEvent.VK_S, "stopSpecial", true, (evt)->{
			santa.updateSprite();
			santa.startLooping(400, 4);
		});
		
		addKeyBinding(this, KeyEvent.VK_S, "special", false, (evt)->{
			if(santa.specialShootAvailable()) {
				getFrame().getAudioEffects().startSound("shoot");
				santa.specialFire();
			}				
			santa.startLooping(400, 4);
		});
		
		addKeyBinding(this, KeyEvent.VK_ENTER, "skip", false, (evt)->{
			skip=true;
		});
		
	}
	
	/**
	 * Loads the background
	 */
	private void loadBackground() {
		background = getFrame().getImageLoader().getImage("background level3");
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
	 * Initialize the characters
	 */
	private void initCharacters() {
		wizard = new Krampus(getFrame().getWidth(), getFrame().getHeight(), "skull", getFrame().getImageLoader(),getFrame().getWidth(),3);
		santa = new Santa(0, getFrame().getHeight(), "fly", getFrame().getImageLoader(), getFrame().getWidth(), getName());
		santa.setImageDimension(new Dimension(123,82));
		santa.setYMax(getFrame().getHeight());
		wizard.startTimer();
	}
	
	
	/**
	 * @Override The game loop
	 */
	@Override
	public void run() {
		//start scene

		getFrame().getAudioEffects().startSound("evilHell");
		while(!skip) {
			startRender();
			paintScreen();		
		}

		running = true;
		startTime = System.currentTimeMillis();
		getFrame().getAudioManager().playLoop("level3");
		initCharacters();
		santa.startLooping(ANIM_PERIOD, SEQ_DURATION);
		wizard.startLooping(ANIM_PERIOD, SEQ_DURATION);
		//real game loop
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
		dbg.drawString("it's almosto midnight..and i have your sleigh.",x,y);
		y = getHeight()*3/5;
		dbg.drawString("Come and find it in the sky!!", x, y);
		
		y = getHeight()*4/5;
		dbg.setColor(Color.RED);
		dbg.setFont(new Font("French Script MT", Font.BOLD,50));
		dbg.drawString("The Krampus", x, y);
		
		y = getHeight()*6/7;
		dbg.setFont(new Font(font, Font.BOLD,20));
		dbg.setColor(Color.WHITE);

		dbg.drawString("ENTER TO SKIP", x*2, y);
		dbg.setColor(Color.RED);
		dbg.setFont(new Font(font,Font.BOLD,70));
		dbg.drawString("LEVEL 3: THE SKY", getFrame().getWidth()/3, 70);
		
	}

	
	/**
	 * For rendering the image in the screen. It is called each loop
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
		
		dbg.setColor(BACKGROUND_COLOR);
		dbg.fillRect(0, 0, getFrame().getWidth(), getFrame().getHeight());
		
	
		dbg.drawImage(background, 0, 0, getFrame().getWidth(), getFrame().getHeight(), this);
		
		//display time passed and the remaining life of santa and the enemy
		dbg.setFont(new Font(font,Font.BOLD,30));
		Color c = new Color(228,52,27);	//creates a custom red color
		dbg.setColor(c);
		if(minutesPassed==0)
			dbg.drawString("Time: " + secondsPassed + "s", 100, 100);
		else
			dbg.drawString("Time: " + minutesPassed + "m" +  secondsPassed + "s", 100, 100);
	
		dbg.drawString("Santa: "+santa.getLifePercent()+"%"+"     The Guy: "+wizard.getLifePercent()+"%", getWidth()*3/4, 100);
		
		//check if santa has special shoots 
		//if so it display an icon on the screen
		if(santa.specialShootAvailable()) {
			BufferedImage temp = getFrame().getImageLoader().getImage("specialPresent");
			dbg.drawImage(temp,getWidth()*3/4, 120, 25 , 25, this);
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
		
		ArrayList<SpecialFire> f = wizard.getSpecialFires();
		if(f!=null) {
			for(int i = 0;i<f.size();i++) {
				if(f.get(i).getPosX()<=0 || !(f.get(i).isVisible())) {
					f.remove(i);
				}
				else {
					f.get(i).paintSprite(dbg);
				}
			}//for
		}
		
		
		santa.paintSprite(dbg);
		wizard.paintSprite(dbg);
		
		//if game over, it stops the loop
		if(gameOver) {
			getFrame().getAudioManager().stopLoop("level3");
			if(santa.getLife()>0) {
				getFrame().getAudioManager().playSound("levelWin");

				gameOverMessage(dbg,"you win");
				
				


				
			}
			else {
				santa.setImage("die");
				getFrame().getAudioEffects().startSound("gameOver");
				gameOverMessage(dbg,"you lose");



			}
			//and go back to the menu at the end of the game
			getFrame().nextLevel("menu");
		}
		
	}

	
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
			
			getFrame().getAudioManager().playSound("levelWin2");

			dbg2.setColor(Color.BLACK);
			dbg2.fillRect(0, 0, getFrame().getWidth(), getFrame().getHeight());
			dbg2.setFont(new Font(font,Font.BOLD,70));
			dbg2.setBackground(Color.BLACK);
			
			dbg2.setColor(Color.RED);
			y = getFrame().getHeight()/5;
			x=getFrame().getWidth()/5;
			msg = "You saved the Christmas!";
			dbg2.drawString(msg, x, y);
			img = getFrame().getImageLoader().getImage("victory2");
			
			dbg2.drawImage(img,x+100,y,600,600,this);
			
			x +=600;
			y += 600;
			dbg2.setColor(Color.WHITE);
			dbg2.setFont(new Font("French Script MT", Font.BOLD,50));
			dbg2.drawString("Santa Claus", x, y);
			paintScreen();
			
			try {
				Thread.sleep(START_DELAY);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			getFrame().getAudioManager().playLoop("victory");
			credits(dbg2);
			getFrame().getAudioManager().stopLoop("victory");
		}
		else {
			paintScreen();
			
			try {
				Thread.sleep(START_DELAY);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		
	}
	
	private void credits(Graphics2D dbg2) {
		String [] credits = {"all the resources used are free of use","ideated, designed, created, realised" , "by Matteo Valerio"};
		int y = getFrame().getHeight();
		int yMax = y;
		int x = getFrame().getWidth()/5;
		int counter = 0;
		int set =0;
		while(counter<yMax) {
			paintScreen();
			dbg2.setColor(Color.BLACK);
			dbg2.fillRect(0, 0, getFrame().getWidth(), getFrame().getHeight());
			dbg2.setFont(new Font(font,Font.BOLD,70));
			dbg2.setBackground(Color.BLACK);
			dbg2.setColor(Color.WHITE);
			for(int i =0;i<credits.length;i++)			
				dbg2.drawString(credits[i], x, (y+y*i/2)-set);
			set+=2;
			counter++;

			try {
				Thread.sleep(DELAY);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		dbg2.setColor(Color.BLACK);
		dbg2.fillRect(0, 0, getFrame().getWidth(), getFrame().getHeight());
		dbg2.setFont(new Font(font,Font.BOLD,70));
		dbg2.setBackground(Color.BLACK);
		dbg2.setColor(Color.RED);
		x = getFrame().getWidth()*2/3;
		y = getFrame().getHeight()*2/3;
		dbg2.drawString("MERRY CHRISTMAS",x,y);
		paintScreen();
		try {
			Thread.sleep(START_DELAY+LEVEL_DELAY);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Called to update the variable of the game each loop
	 */
	private void gameUpdate() {
		if(!gameOver && !isPaused) {
			santa.updateSprite();
			wizard.setTarget(santa.getPosX(), santa.getPosY());
			wizard.move();
			wizard.updateSprite();
			checkCollision();
		}
		else if(gameOver) {
			running = false;
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
	
	/**
	 * This method checks if there are any collision between presents/wizard and fires/santa
	 */
	private void checkCollision() {
		Rectangle sR = santa.getMyRectangle();
		Rectangle eR = wizard.getMyRectangle();
		Rectangle pR,fR;
		ArrayList<Present> present = santa.getPresents();
		ArrayList<SpecialFire> fires = wizard.getSpecialFires();
		
		SpecialFire f;
		Present p;
		//fires/santa
		for(int i =0;i<fires.size();i++) {
			f = fires.get(i);
			fR = f.getMyRectangle();
			if(fR.intersects(sR) && f.isVisible()) {
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

