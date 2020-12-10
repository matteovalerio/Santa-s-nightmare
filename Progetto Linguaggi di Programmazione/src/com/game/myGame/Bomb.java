package com.game.myGame;

import java.awt.Graphics;

public class Bomb extends Fire {

	public Bomb(int posX, int posY, String name, ImageLoader imgL, int count) {
		super(posX, posY, name, imgL, count);
		// TODO Auto-generated constructor stub
	}
	
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
