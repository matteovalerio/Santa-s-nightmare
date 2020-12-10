package com.game.myGame;

import java.awt.Dimension;
import java.awt.Graphics;

/**
 * This class represents the presents launched in level 2.
 * @author MatteoValerio
 *
 */
public class Missiles extends Present {

	private int WATER_SPEED = SPEED/4;
	
	/**
	 * Public constructor. See present public constructor
	 * @param posX
	 * @param posY
	 * @param name
	 * @param imgL
	 */
	public Missiles(int posX, int posY, String name, ImageLoader imgL) {
		super(posX, posY, name, imgL);
	}

	/**
	 * Move the missile vertically
	 */
	public void move() {
		setDx(0);
		setDy(-WATER_SPEED);
		setPosY(getPosY()-WATER_SPEED);
	}
	
	/**
	 * It sets the type of present to special so it does more damage and change the image
	 */
	public void specialShot() {
		setImage("specialPresent");
		resizeSpecial();
		setDamage(getDamage() * 4);
		setSpecial(true);
	}
	
	/**
	 * It resizes the special bomb
	 */
	private void resizeSpecial() {
		setImageDimension(new Dimension(50,50));
	}
	/**
	 * It paints this sprite to the screen
	 */
	public void paintSprite(Graphics g) {
		super.paintSprite(g);
		move();
	}
}
