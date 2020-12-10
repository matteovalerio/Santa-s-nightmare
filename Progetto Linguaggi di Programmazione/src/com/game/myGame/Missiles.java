package com.game.myGame;

import java.awt.Graphics;

public class Missiles extends Present {

	private int WATER_SPEED = SPEED/4;
	
	public Missiles(int posX, int posY, String name, ImageLoader imgL) {
		super(posX, posY, name, imgL);
		// TODO Auto-generated constructor stub
	}

	public void move() {
		setDx(0);
		setDy(-WATER_SPEED);
		setPosY(getPosY()-WATER_SPEED);
	}
	
	/**
	 * It paints this sprite to the screen
	 */
	public void paintSprite(Graphics g) {
		super.paintSprite(g);
		move();
	}
}
