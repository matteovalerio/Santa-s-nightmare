package com.game.myGame;

import java.awt.Graphics;
/**
 * This class represents the bomb launched by the enemy during level 2
 * @author MatteoValerio
 *
 */
public class Bomb extends Fire {

	
	public Bomb(int posX, int posY, String name, ImageLoader imgL, int count) {
		super(posX, posY, name, imgL, count);
	}
	
	/**
	 * Move the sprite vertically down
	 */
	public void move() {
		setDx(0);
		setDy(YSTEP);
		setPosY(getPosY()+YSTEP);
	}
	
	/**
	 * It paints this sprite to the screen
	 */
	public void paintSprite(Graphics g) {
		super.paintSprite(g);
		move();
	}
}
