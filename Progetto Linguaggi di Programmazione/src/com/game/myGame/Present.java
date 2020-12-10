package com.game.myGame;

import java.awt.Dimension;
import java.awt.Graphics;
/**
 * This class represent the present bomb launched by Santa
 * @author MatteoValerio
 *
 */
public class Present extends Sprite {

	protected final int SPEED = 30;
	protected final static int ADJUSTX = 10;
	private final int START_DAMAGE = 3;
	private int damage = 3;
	private boolean isVisible;
	private boolean isSpecial;
	
	/**
	 * Public constructor. It also adjust the position of X and Y
	 * @param posX the starting X
	 * @param posY the starting Y
	 * @param name name of the sprite
	 * @param imgL The image loader
	 */
	public Present(int posX, int posY, String name, ImageLoader imgL) {
		super(posX+ADJUSTX, posY, name, imgL);
		isVisible=true;
		isSpecial = false;
		setPosY(posY);
		resize();
		move();
	}

	
	private void resize() {
		setImageDimension(new Dimension(25,25));
	}
	
	/**
	 * It moves the sprites through the screen at a constant speed
	 */
	public void move() {
		setPosX(getPosX()+SPEED);
	}
	
	/**
	 * It sets the current sprite visible or not
	 * @param visible True if it is visible
	 */
	public void setVisible(boolean visible) {
		isVisible = visible;
	}
	
	/**
	 * Return true if the sprite is visible
	 * @return true if visible
	 */
	public boolean isVisible() {
		return isVisible;
	}
	
	/**
	 * it returns the value of damage caused by this present bomb
	 * @return numeric value of the damage
	 */
	public int getDamage() {
		return damage;
	}
	
	/**
	 * It paints this sprite to the screen
	 */
	public void paintSprite(Graphics g) {
		move();
		super.paintSprite(g);
	}
	
	/**
	 * It sets the type of present to special so it does more damage and change the image
	 */
	public void specialShot() {
		setImage("specialPresent");
		resize();
		damage *=4;
		isSpecial = true;
	}
	
	/**
	 * 
	 * @return True if the actual present is special
	 */
	public boolean isSpecial() {
		return isSpecial;
	}
	
	/**
	 * Set the type of present back to normal.
	 */
	public void normalShot() {
		setImage("present");
		damage = START_DAMAGE;
	}
}
