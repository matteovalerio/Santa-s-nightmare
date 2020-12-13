package com.game.myGame;

import java.awt.Dimension;
import java.awt.Graphics;

/**
 * This class represents the fire shot by the evil wizard
 * @author MatteoValerio
 *
 */
public class Fire extends Sprite {

	private final int SPEEDX = -10;
	private final int SPEEDY = 10;
	private int maxY;
	private int maxX;
	private final static int ADJUSTX = -10;
	private int count;
	private int damage = 3;
	private boolean isVisible;
	
	/**
	 * The public constructor. It adjusts also the x position so it doesn't appear on the evil wizard
	 * @param posX The x pos
	 * @param posY The y  pos
	 * @param name The name
	 * @param imgL The image Loader
	 */
	public Fire(int posX, int posY, String name, ImageLoader imgL, int count) {
		super(posX+ADJUSTX, posY, name, imgL);
		
		maxX = getPosX();
		setPosX(posX);
		setPosY(posY);
		resize();
		maxY = posY-getHeight()*2;
		isVisible = true;
		this.count = count;
		currentMovement(count);
	}
	
	private void resize() {
		setImageDimension(new Dimension(50,50));
	}

	/**
	 * Move the fire with a certain speed
	 */
	public void move() {
		setPosX(getPosX()+SPEEDX*2);
		int y = getPosY();
		if(y>=maxY) {
			setPosY(y-SPEEDY);
		}
		else
			setPosY(getPosY() +SPEEDY);
	}
	
	/**
	 * Different type of movement
	 */
	public void move2() {
		setImage("blast");
		resize();
		int y = getPosY();
		if(y<0)
			setVisible(false);
		else
			setPosY(y+SPEEDY);
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
				move2();
				break;
			}
		}
	}
	
	/**
	 * 
	 * @return How much damage this fire causes
	 */
	public int getDamage() {
		return damage;
	}
	
	/**
	 * Paint this sprite to the screen
	 */
	public void paintSprite(Graphics g) {
		currentMovement(count);
		super.paintSprite(g);
	}
	
	/**
	 * 
	 * @param visible True if visible
	 */
	public void setVisible(boolean visible) {
		isVisible = visible;
	}
	
	/**
	 * 
	 * @return True if visible
	 */
	public boolean isVisible() {
		return isVisible;
	}
	
	public int getCount() {
		return count;
	}
}
