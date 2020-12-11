package com.game.myGame;

public class SpecialFire extends Fire {

	private int targetX;
	private int targetY;
	
	public SpecialFire(int posX, int posY, String name, ImageLoader imgL, int count) {
		super(posX, posY, name, imgL, count);
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
	
	public void move(int x, int y) {
		int posX = getPosX();
		int posY = getPosY();
		
		if(x>posX)
			setDx(-XSTEP);	//go right
		else if(x<posX)
			setDx(XSTEP); //go left
		else
			setDx(0); //don't change
		
		if(y>posY)
			setDy(-YSTEP); //go up
		else if(y<posY)
			setDy(YSTEP); //go down
		else
			setDy(0);
	}
	
	public void blast() {
		//cambia immagine e mettila tutta verticale
	}
	
	
}
