package com.game.myGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Dimension;

import javax.swing.Timer;

public class Krampus extends Enemy {

	private Timer timer;
	private int startingLife = LEVEL3;
	private final int YSTEP =4;
	private final int FIRE3 = 3;
	private final int DELAY2 = 800;
	private int xTarget;
	private int yTarget;
	
	private ArrayList<SpecialFire> fires = new ArrayList<>();
	private int counter = 0;
	
	public Krampus(int posX, int posY, String name, ImageLoader imgL, int xMax, int level) {
		super(posX, posY, name, imgL, xMax, level);
		setImage("skull");
		fireCounter = FIRE1;
	}

	public void startTimer() {
		timer = new Timer(DELAY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				switch(fireCounter) {
					case 1: {
						fire();
						break;
					}
					case 2:{
						finalAttack2();
						break;
					}
					case 3:{
						finalAttack3();
						counter = 0;
						break;
					}
				}
			}
		});
		timer.start();
	}
	
	/**
	 * TODO
	 */
	private void finalAttack3() {
		counter = fires.size();
		if(counter<2) {
			SpecialFire f = new SpecialFire(getPosX(), getPosY(), "laser", getImageLoader(), fireCounter);
			f.laser();
			fires.add(f);
		}
	}
	
	
	private void finalAttack2() {
		counter++;
		if(counter<6) 
			fires.add(new SpecialFire(getPosX()-getWidth(), getPosY(), "fire", getImageLoader(), fireCounter));
		else {
			SpecialFire f = new SpecialFire(getPosX()-getWidth(), getPosY(), "fire", getImageLoader(), FIRE1);
			f.setTarget(xTarget, yTarget);
			fires.add(f);
			counter = 0;
		}
			
	}
	
	
	public void fire() {
		counter++;
		if(counter==6) {
			counter=0;
			fires.add(new SpecialFire(getPosX()-getWidth()*2,getPosY()-getHeight(),"fire",getImageLoader(), fireCounter));
			fires.add(new SpecialFire(getPosX()-getWidth()*2,getPosY()+getHeight(),"fire",getImageLoader(), fireCounter));
			fires.add(new SpecialFire(getPosX()-getWidth()*2,getPosY(),"fire",getImageLoader(), fireCounter));
		}
		else if(counter==4) {
			fires.add(new SpecialFire(getPosX()-getWidth()*2,getPosY()+getHeight()/2,"fire",getImageLoader(), fireCounter));
			fires.add(new SpecialFire(getPosX()-getWidth()*1/2,getPosY()+getHeight()/2,"fire",getImageLoader(), fireCounter));
			fires.add(new SpecialFire(getPosX()-getWidth(),getPosY()+getHeight()/2,"fire",getImageLoader(), fireCounter));
		}
		else if(counter==2) {
			fires.add(new SpecialFire(getPosX()-getWidth(),getPosY()+getHeight()*3/2,"fire",getImageLoader(), fireCounter));
			fires.add(new SpecialFire(getPosX()-getWidth(),getPosY()+getHeight()*1/2,"fire",getImageLoader(), fireCounter));
			fires.add(new SpecialFire(getPosX()-getWidth(),getPosY()+getHeight(),"fire",getImageLoader(), fireCounter));
			fires.add(new SpecialFire(getPosX()-getWidth(),getPosY()+getHeight()*2,"fire",getImageLoader(), fireCounter));
		}
		else {
			fires.add(new SpecialFire(getPosX()-getWidth(),getPosY()+getHeight(),"fire",getImageLoader(), fireCounter));
		}

	}
	
	/**
	 * Gets hit. Return the remaining life. Damage can't be a number <0
	 * @param damage Damage to be inflicted
	 * @return The remaining life after being hit
	 */
	public int hit(int damage) {
		if(damage>0)
			setLife(getLife()-damage);
		int life = getLifePercent();
		if(life<=66) {
			timer.setDelay(DELAY2);
			fireCounter =FIRE2;
		}
		else if(life<=33) {
			timer.setDelay(DELAY1);
			fireCounter = FIRE3;
		}
		return getLife();
	}
	
	public void setTarget(int x, int y) {
		xTarget = x;
		yTarget = y;
	}
	
	public void move() {
		int y = getPosY();
		if(yTarget>y && yTarget-y>YSTEP)
			setDy(YSTEP);
		else if(yTarget<y && y-yTarget>YSTEP)
			setDy(-YSTEP);
		else
			setDy(0);
		for(SpecialFire s: fires) {
			if(fireCounter==FIRE2) {
				s.slow();
				s.setTarget(xTarget, yTarget);
			}
			else if(fireCounter == FIRE1 ||fireCounter==FIRE3){
				s.setTarget(-s.getWidth(), s.getPosY());
				s.fast();
			}
			s.move();
		}
	}
	
	public ArrayList<SpecialFire> getSpecialFires (){
		return fires;
	}
}
