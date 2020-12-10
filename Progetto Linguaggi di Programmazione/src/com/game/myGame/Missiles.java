package com.game.myGame;

import java.awt.Dimension;
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
	 * It sets the type of present to special so it does more damage and change the image
	 */
	public void specialShot() {
		setImage("specialPresent");
		resizeSpecial();
		setDamage(getDamage() * 4);
		setSpecial(true);
	}
	
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
