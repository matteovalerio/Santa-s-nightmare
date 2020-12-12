package com.game.myGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

public class GhostShip extends Enemy {

	private Timer timer;
	private int counter = 0;
	private ArrayList<Bomb> bombs = new ArrayList<>();
	private Random r = new Random();
	private int targetX;
	private int xMax;
	private int startingLife = LEVEL2;
	private int speed;

	
	public GhostShip(int posX, int posY, String name, ImageLoader imgL, int xMax, int level) {
		super(posX, posY, name, imgL, xMax, level);
		this.xMax = getXMax();
		speed = SPEEDX1;
		startTimer();
		}

	public void startTimer() {
		timer = new Timer(DELAY, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				switch(fireCounter) {
					case 1: {
						bomb();
						break;
					}
					case 2:{
						bomb2();
						break;
					}
				}
			}
		});
		timer.start();
	}
	
	/**
	 * This method creates a new bomb for level 2
	 */
	public void bomb() {
		counter++;
		if(counter==6) {
			counter = 0;
			bombs.add(new Bomb(getPosX()+getWidth()/2,getPosY()+getHeight(),"bomb",getImageLoader(),fireCounter));
			bombs.add(new Bomb(getPosX()+getWidth(),getPosY()+getHeight(),"bomb",getImageLoader(),fireCounter));
		}
		else if(counter == 4) {
			bombs.add(new Bomb(getPosX()+getWidth()/2,getPosY()+getHeight()*2,"bomb",getImageLoader(),fireCounter));
			bombs.add(new Bomb(getPosX()+getWidth(),getPosY()+getHeight(),"bomb",getImageLoader(),fireCounter));
		}
		else if(counter ==2) {
			bombs.add(new Bomb(getPosX()+getWidth()/2,getPosY()+getHeight(),"bomb",getImageLoader(),fireCounter));
			bombs.add(new Bomb(getPosX()+getWidth(),getPosY()+getHeight(),"bomb",getImageLoader(),fireCounter));
			bombs.add(new Bomb(getPosX(),getPosY()+getHeight(),"bomb",getImageLoader(),fireCounter));
		}
		else 
			bombs.add(new Bomb(getPosX()+getWidth()/2,getPosY()+getHeight(),"bomb",getImageLoader(),fireCounter));
		move();
	}
	
	/**
	 * This method creates a special bomb for level 2
	 */
	public void bomb2() {
		counter++;
		if(counter==6) {
			counter =0;
			bombs.add(new Bomb(getPosX()+getWidth()/2,getPosY()+getHeight(),"bomb",getImageLoader(),fireCounter));
			bombs.add(new Bomb(getPosX()+getWidth()*3/2,getPosY()+getHeight(),"bomb",getImageLoader(),fireCounter));
			bombs.add(new Bomb(getPosX()+getWidth(),getPosY()+getHeight(),"bomb",getImageLoader(),fireCounter));
			bombs.add(new Bomb(getPosX(),getPosY()+getHeight(),"bomb",getImageLoader(),fireCounter));
		}
		else if(counter ==4) {
			bombs.add(new Bomb(getPosX()+getWidth()/2,getPosY()+getHeight()*2,"bomb",getImageLoader(),fireCounter));
			bombs.add(new Bomb(getPosX()+getWidth(),getPosY()+getHeight(),"bomb",getImageLoader(),fireCounter));
			bombs.add(new Bomb(getPosX(),getPosY()+getHeight()/2,"bomb",getImageLoader(),fireCounter));
		}
		else if(counter ==2) {
			bombs.add(new Bomb(getPosX()+getWidth(),getPosY()+getHeight(),"bomb",getImageLoader(),fireCounter));
			bombs.add(new Bomb(getPosX()+getWidth()*2,getPosY()+getHeight()*2,"bomb",getImageLoader(),fireCounter));
			bombs.add(new Bomb(getPosX()+getWidth()/2,getPosY()+getHeight()/2,"bomb",getImageLoader(),fireCounter));
			bombs.add(new Bomb(getPosX()+getWidth()*3/2,getPosY()+getHeight()*3/2,"bomb",getImageLoader(),fireCounter));
		}
		else {
			bombs.add(new Bomb(getPosX()+getWidth()/2,getPosY()+getHeight(),"bomb",getImageLoader(),fireCounter));
			bombs.add(new Bomb(getPosX()+getWidth(),getPosY()+getHeight()*3/2,"bomb",getImageLoader(),fireCounter));
			bombs.add(new Bomb(getPosX(),getPosY()+getHeight()*3/2,"bomb",getImageLoader(),fireCounter));
			bombs.add(new Bomb(getPosX()+getWidth()/2,getPosY()+getHeight()*2,"bomb",getImageLoader(),fireCounter));
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
	 * 
	 * @return the list of bombs
	 */
	public ArrayList<Bomb> getBombs(){
		return bombs;
	}
	
	/**
	 * It changes the speed of the enemy
	 */
	public void increaseSpeed() {
		speed = SPEEDX2;
	}
	
	/**
	 * Gets hit. Return the remaining life. Damage can't be a number <0
	 * @param damage Damage to be inflicted
	 * @return The remaining life after being hit
	 */
	public int hit(int damage) {
		if(damage>0)
			setLife(getLife()-damage);
		int life = getLife();
		if(life<=startingLife/2) {
			timer.setDelay(DELAY1);
			fireCounter =FIRE2;
		}
		return getLife();
	}

}
	
