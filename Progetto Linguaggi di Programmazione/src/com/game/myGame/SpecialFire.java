package com.game.myGame;

import java.awt.Dimension;

public class SpecialFire extends Fire {

	private int targetX;
	private int targetY;
	private final int XSTEP =1;
	private final int FAST = 8;
	private final int SPEEDX = 40;
	private int speed;
	
	public SpecialFire(int posX, int posY, String name, ImageLoader imgL, int count) {
		super(posX, posY, name, imgL, count);
		speed = XSTEP;
	}

	/**
	 * Creates the target for this fire
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public void setTarget(int x, int y) {
		targetX=x;
		targetY=y;
	}
	
	public void move() {
		int posX = getPosX();
		int posY = getPosY();
		
		int dx,dy;
		
		if(targetX>posX)
			dx=speed;	//go right
		else if(targetX<posX)
			dx=-speed; //go left
		else
			dx=0; //don't change
		
		if(targetY>posY)
			dy=speed; //go down
		else if(targetY<posY)
			dy=-speed; //go up
		else
			dy=0;
		setPosX(posX+dx);
		setPosY(posY+dy);
	}
	
	/**
	 * get the current movement based on a counter
	 * @param counter The counter
	 */
	public void currentMovement(int counter) {
		switch (counter) {
			case 1: {
				move();
				break;
			}
			
			case 2:{
				fast();
				move2();
				break;
			}
		}
	}
	
	public void move2() {
		int posX = getPosX();
		setPosX(posX-speed);
	}
	
	public void laser() {
		//cambia immagine e mettila tutta verticale
		setImage("laser");
		setImageDimension(new Dimension(300,50));
		int x = getPosX();
		x -= getWidth();
		setPosX(x);
		fast();

	}
	
	public void fast() {
		speed = FAST;
	}
	
	public void slow() {
		speed = XSTEP;
	}
	
	
}
