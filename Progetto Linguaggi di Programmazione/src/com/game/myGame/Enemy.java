package com.game.myGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;


/**
 * Class that represent the enemy. It has some special features so it extends the Sprite class
 * @author MatteoValerio
 *
 */
public class Enemy extends Sprite {

	protected static final int LEVEL1 = 200;
	protected static final int LEVEL2 = 400;
	protected static final int LEVEL3 = 800;
	private int startingLife;
	private int life; //life of the Enemy
	private ArrayList<Fire> fires = new ArrayList<>();

	protected final int FIRE1 = 1;
	protected final int FIRE2 = 2;

	protected int fireCounter = FIRE1;
	
	private int counter = 0;
	private Timer timer;
	protected final int DELAY = 1300;
	protected final int DELAY1 = 1000;
	private Random r = new Random();
	private int targetX;
	private int xMax;
	private int level;
	protected final int SPEEDX1 = 8;
	protected final int SPEEDX2 = 16;
	private int speed;
	
	/**
	 * Public constructor. It builds the enemy sprite. It also adjust the position for this particular sprite
	 * @param posX The new x pos
	 * @param posY The new y pos
	 * @param name The name of the sprite
	 * @param imgL The image loader
	 * @param level The level that called him
	 */
	public Enemy(int posX, int posY, String name, ImageLoader imgL, int xMax, int level) {
		super(posX, posY, name, imgL);
		targetX = posX;
		this.xMax = xMax-getWidth();
		this.level = level;
		switch(level) {
		case 1:
			life = LEVEL1;
			break;
		case 2:
			life = LEVEL2;
			speed = SPEEDX1;
			break;
		case 3:
			life = LEVEL3;
			break;
		} // switch
		startingLife = life;
		setPosY(posY-getHeight());
		setPosX(getPosX()-getWidth());

		startTimer();
		//starting a timer: each DELAY a new fire is created
//		startTimer(level);
	}//constructor
	
	
	/**
	 * It starts the timer and activate some attacks, depending on the level
	 * @param level The level
	 */
	public void startTimer() {
		fire();
		timer = new Timer(DELAY, new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {

				switch(fireCounter) {
					case 1: {
						fire();
						break;
					}
					case 2:{
						fire2();
						break;
					}
				}
			}
			
		});
		timer.start();
	}
	
	/**
	 * Return the remaining life
	 * @return The remaining life
	 */
	public int getLife() {
		return life;
	}
	
	/**
	 * 
	 * @return The percentage of remaining life 
	 */
	public int getLifePercent() {
		int lifePercent = life*100/startingLife;
		if(lifePercent<0)
			lifePercent = 0;
		return lifePercent;
	}
	
	/**
	 * Gets hit. Return the remaining life. Damage can't be a number <0
	 * @param damage Damage to be inflicted
	 * @return The remaining life after being hit
	 */
	public int hit(int damage) {
		if(damage>0)
			life-=damage;
		if(life<=startingLife/2) {
			timer.setDelay(DELAY1);
			fireCounter =FIRE2;
		}
		return getLife();
	}
	
	/**
	 * This method creates a new fire and add it to the evil wizard's fires
	 */
	public void fire() {

		counter++;
		if(counter==6) {
			counter=0;
			fires.add(new Fire(getPosX(),getPosY()+getHeight()/4,"fire",getImageLoader(), fireCounter));
			fires.add(new Fire(getPosX(),getPosY()+getHeight(),"fire",getImageLoader(), fireCounter));
		}
		else if(counter==4) {
			fires.add(new Fire(getPosX(),getPosY()+getHeight()/2,"fire",getImageLoader(), fireCounter));
			fires.add(new Fire(getPosX()+getWidth(),getPosY()+getHeight()/2,"fire",getImageLoader(), fireCounter));
		}
		else if(counter==2) {
			fires.add(new Fire(getPosX(),getPosY()+getHeight(),"fire",getImageLoader(), fireCounter));
			fires.add(new Fire(getPosX()+getWidth(),getPosY()+getHeight(),"fire",getImageLoader(), fireCounter));
		}
		else {
			fires.add(new Fire(getPosX(),getPosY()+getHeight(),"fire",getImageLoader(), fireCounter));
		}
		
	}
	
	/**
	 * This method creates a new fire and add it to the evil wizard's fires
	 */
	public void fire2() {
		int rdm;
		for(int i=0;i<fireCounter;i++) {
			rdm = r.nextInt(getPosX());
			fires.add(new Fire(getPosX(),getPosY()+getHeight(),"fire",getImageLoader(), FIRE1));
			fires.add(new Fire(rdm,getHeight()*3/5,"fire",getImageLoader(), fireCounter));
		}
		
	}
	

	
	/**
	 * It moves the enemy, for level 2
	 */
	public void move() {
		int posX = getPosX();
		if(posX-targetX>0 && posX-speed-targetX>0) 
			setDx(-speed);
		else if(posX-targetX<0 && posX + speed-targetX<0)
			setDx(speed);
		else
			targetX = r.nextInt(xMax-getWidth());
	}
	
	
	/**
	 * It returns a list of all the fires shot by the wizard
	 * @return ArrayList of Fires shot by the evil wizard
	 */
	public ArrayList<Fire> getFires(){
		return fires;
	}
	
	public int getXMax() {
		return xMax;
	}
	
	public void setLife(int newLife) {
		life = newLife;
	}

}
