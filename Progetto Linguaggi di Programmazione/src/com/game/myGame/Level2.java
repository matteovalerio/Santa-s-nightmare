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

public class Level2 extends Level implements Runnable{

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
	


	
	private Thread animator;
	private boolean isPaused;
	private Santa santa;
	private Enemy wizard;
	
	
	public Level2(GameFrame frame, String name) {
		super(frame, name);
		running = false;
		gameOver = false;
		init();
	}

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
			santa.moveUp(rightDirection);
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
			santa.missiles();
			santa.startLooping(400, 4);
		});
		
		addKeyBinding(this, KeyEvent.VK_S, "stopSpecial", true, (evt)->{
			santa.updateSprite();
			santa.startLooping(400, 4);
		});
		
		addKeyBinding(this, KeyEvent.VK_S, "special", false, (evt)->{
			if(santa.specialShootAvailable()) {
				getFrame().getAudioEffects().startSound("shoot");
				santa.specialMissile();
			}				
			santa.startLooping(400, 4);
		});
		
		
		addKeyBinding(this, KeyEvent.VK_ESCAPE, "pause", false, (evt)->{
			pauseGame();
		});
		
	}
	
	private void loadBackground() {
		background = getFrame().getImageLoader().getImage("background level2");
	}

	@Override
	public void addNotify() {
		super.addNotify();
		startLevel();
	}
	
	/**
	 * Invoked to pause the Game
	 */
	public void pauseGame() {
		isPaused=true;
	}
	
	
	/**
	 * Invoked to resume the game
	 */
	public void resumeGame() {
		isPaused = false;
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
	 * Invoked to stop the level
	 */
	private void stopLevel() {
		running = false;
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
	
	private void initCharacters() {
		wizard = new Enemy(getFrame().getWidth()/2, background.getHeight()*4/7, "evilShip", getFrame().getImageLoader(),getFrame().getWidth(),2);
		santa = new Santa(0, getFrame().getHeight(), "sub", getFrame().getImageLoader(), getFrame().getWidth(), getName());
		
		santa.setYMax(getFrame().getHeight());
	}
	
	
	@Override
	public void run() {
		
		startRender();
		paintScreen();
		getFrame().getAudioEffects().startSound("evilYou");
		try {
			Thread.sleep(START_DELAY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		dbg.setFont(new Font(font,Font.BOLD,70));
		dbg.drawString("LEVEL 2: THE OCEAN", getFrame().getWidth()/3, 70);
		paintScreen();
		try {
			Thread.sleep(LEVEL_DELAY);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		running = true;
		startTime = System.currentTimeMillis();
		getFrame().getAudioManager().playLoop("level2");
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
		int y = getHeight()/6;
		dbg.setColor(Color.RED);
		dbg.drawString("Dear Santa...", x, y);
		x = getWidth()/4;
		y = getHeight()*2/5;
		
		dbg.setFont(new Font(font,Font.BOLD,30));
		dbg.setColor(Color.WHITE);
		dbg.drawString("you just found the letters of all children in the world..",x,y);
		y = getHeight()*3/5;
		dbg.drawString("I still have the presents, deep down in the ocean", x, y);
		
		y = getHeight()*4/5;
		dbg.setColor(Color.RED);
		dbg.drawString("A Kid?", x, y);
		
		
		
	}

	
	
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
		
		dbg.setFont(new Font(font,Font.BOLD,30));
		Color c = new Color(228,52,27);	//creates a custom red color
		dbg.setColor(c);
		if(minutesPassed==0)
			dbg.drawString("Time: " + secondsPassed + "s", 100, 100);
		else
			dbg.drawString("Time: " + minutesPassed + "m" +  secondsPassed + "s", 100, 100);
	
		dbg.drawString("Santa: "+santa.getLifePercent()+"%"+"     The Guy: "+wizard.getLifePercent()+"%", getWidth()*3/4, 100);
		
		if(santa.specialShootAvailable()) {
			BufferedImage temp = getFrame().getImageLoader().getImage("specialPresent");
			dbg.drawImage(temp,getWidth()*3/4, 120, 25 , 25, this);
		}
		
		//draw only the present that are visibles, the others are removed from the list of presents available for Santa
		ArrayList<Present> p = santa.getPresents();
		if(p!=null) {
			for(int i = 0;i<p.size();i++) {
				if(p.get(i).getPosY()<0 || !(p.get(i).isVisible())) {
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
				gameOverMessage(dbg,"you win");
				
				
				getFrame().getAudioEffects().startSound("levelWin");
				try {
					Thread.sleep(START_DELAY);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//TODO uncomment when creates the level3
				//getFrame().nextLevel("level 3");
				
			}
			else {
				santa.setImage("die");
				gameOverMessage(dbg,"you lose");

				getFrame().getAudioEffects().startSound("gameOver");
				try {
					Thread.sleep(START_DELAY);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				getFrame().nextLevel("menu");

			}
		}
		
	}

	
	/**
	 * Method called when the game is over:
	 * it can happen when you win or when you lose the level.
	 * @param dbg2 The graphics used to paint the message
	 */
	private void gameOverMessage(Graphics2D dbg2, String msg) {
		// TODO Auto-generated method stub
		
		//calcola posizione x e y e messaggio msg
		int x = getFrame().getWidth()*2/5;
		int y = getFrame().getHeight()/2;
		
		

		dbg2.setFont(new Font(font,Font.BOLD,70));



		dbg.setColor(Color.RED);
		dbg.drawString(msg, x, y);


		
		paintScreen();
	}
	
	private void gameUpdate() {
		if(!gameOver && !isPaused) {
			santa.updateSprite();
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
		ArrayList<Fire> fires = wizard.getFires();
		
		Fire f;
		Present p;
		//fires/santa
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
