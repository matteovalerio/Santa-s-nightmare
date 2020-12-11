package com.game.myGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class Krampus extends Enemy {

	private Timer timer;
	private int startingLife = LEVEL3;
	private final int YSTEP =1;
	private final int FIRE3 = 3;
	private final int DELAY2 = 800;
	private int xTarget;
	private int yTarget;
	
	private ArrayList<SpecialFire> fires = new ArrayList<>();
	private int counter = 0;
	
	public Krampus(int posX, int posY, String name, ImageLoader imgL, int xMax, int level) {
		super(posX, posY, name, imgL, xMax, level);
		setImage("skull");
		startTimer();
	}

	public void startTimer() {
		timer = new Timer(DELAY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				switch(fireCounter) {
					case 1: {
						finalAttack2();//fire();
						break;
					}
					case 2:{
						finalAttack2();
						break;
					}
					case 3:{
					//	finalAttack3();
						break;
					}
				}
			}
		});
		timer.start();
	}
	
	
	private void finalAttack2() {
		SpecialFire f = new SpecialFire(getPosX()+getWidth(), getPosY(), "fire", getImageLoader(), fireCounter);
		fires.add(f);
		f.move(xTarget, yTarget);
	}
	
	
	public void fire() {
		counter++;
		if(counter==6) {
			counter=0;
			fires.add(new SpecialFire(getPosX(),getPosY()+getHeight()/4,"fire",getImageLoader(), fireCounter));
			fires.add(new SpecialFire(getPosX(),getPosY()+getHeight(),"fire",getImageLoader(), fireCounter));
		}
		else if(counter==4) {
			fires.add(new SpecialFire(getPosX(),getPosY()+getHeight()/2,"fire",getImageLoader(), fireCounter));
			fires.add(new SpecialFire(getPosX()+getWidth(),getPosY()+getHeight()/2,"fire",getImageLoader(), fireCounter));
		}
		else if(counter==2) {
			fires.add(new SpecialFire(getPosX(),getPosY()+getHeight(),"fire",getImageLoader(), fireCounter));
			fires.add(new SpecialFire(getPosX()+getWidth(),getPosY()+getHeight(),"fire",getImageLoader(), fireCounter));
		}
		else {
			fires.add(new SpecialFire(getPosX(),getPosY()+getHeight(),"fire",getImageLoader(), fireCounter));
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
		int life = getLife();
		if(life<=startingLife/3) {
			timer.setDelay(DELAY2);
			fireCounter =FIRE3;
		}
		else if(life<=startingLife*2/3) {
			timer.setDelay(DELAY1);
			fireCounter = FIRE2;
		}
		return getLife();
	}
	
	public void setTarget(int x, int y) {
		xTarget = x;
		yTarget = y;
	}
	
	public void move() {
		int y = getPosY();
		if(yTarget>y)
			setDy(YSTEP);
		else if(yTarget<y)
			setDy(-YSTEP);
		else
			setDy(0);
		for(SpecialFire s: fires)
			s.setTarget(xTarget, yTarget);
	}
	
	public ArrayList<SpecialFire> getSpecialFires (){
		return fires;
	}
}
