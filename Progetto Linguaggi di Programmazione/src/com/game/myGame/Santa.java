package com.game.myGame;

import java.util.ArrayList;

/**
 * This class is build to represent the main character of the game: Santa Claus
 * He has some special actions more than a normal sprite, so he has his own class
 * @author MatteoValerio
 *
 */
public class Santa extends Sprite {

	private static final int GOING_UP = 1;
	private static final int STAND = 0;
	private static final int GOING_DOWN = -1;
	private int state = STAND; //at the beginning is not jumping
	private static final int MAX_UP_STEPS = 14; // for trying
	private int yGround;
	private int upCount = 0;
	private final int STARTING_LIFE =30;
	private int life=STARTING_LIFE; //life of santa
	private ArrayList<Present> present = new ArrayList<>();
	private int xMax;
	private int yMax;
	private int hitCounter = 0;//initial hit counter of santa. When he hits the enemy, the counter continue.
	private int points= 0;//points of santa . For statistics only.
	private String levelName;
	
	
	/**
	 * public constructor. It takes the starting position x and y, the name, and imageLoader and the max x position of the screen
	 * @param posX Starting x
	 * @param posY Starting y
	 * @param name Name of the sprite
	 * @param imgL Image loader for animation
	 * @param xMax Maximum x of the screen
	 */
	public Santa(int posX, int posY, String name, ImageLoader imgL, int xMax, String levelName) {
		super(posX, posY, name, imgL);
		yGround = getPosY();
		this.xMax = xMax;
		this.levelName = levelName;
	}
	
	/**
	 * Makes Santa jump
	 */
	public void jump() {
		if(state!= GOING_UP && getPosY()==yGround)
			state = GOING_UP;
	}

	/**
	 * Updates all the variables relative to the positions
	 */
	public void updateSprite() {
		if(isActive()) {
			int tmpX = getPosX();  			//get the current position
			setPosX(getPosX()+getDx());		//try to set a new position
			if(tmpX<0)
				setPosX(0+getDx());			//if Santa is going offscreen on  the left
			else if(tmpX+getWidth()>=xMax)
				setPosX(tmpX-getDx());		//if Santa is going offscreen on the right

			switch (levelName) {
			case "level 1":
				jumping();
				break;
			}

			setPosY(getPosY()+getDy());		// updates the yPosition of the image

			if(isLooping())
				getImagePlayer().updateTick();	//set the next image
		}
	}
	
	/**
	 * Method for jumping
	 */
	private void jumping() {
		if(state==STAND)
			checkFalling();				//if Santa is standing, check if he should be falling
		else if (state== GOING_UP)		
			updateRising();				//if Santa is jumping, update 
		else
			updateFalling();			// if Santa is falling, update
	}
	
	/**
	 * It set the max y position
	 * @param y max y position
	 */
	public void setYMax(int y) {
		yMax = y-getHeight();
	}
	
	/**
	 * Just for level 2. Moves up the sprite
	 */
	public void moveUp(boolean rightDirection) {
		super.moveUp(rightDirection);
		if(getPosY()<yMax/2)
			setDy(0);
	}
	
	/**
	 *  Just for level 2. Moves down the sprite
	 */
	public void moveDown(boolean rightDirection) {
		super.moveDown(rightDirection);
		if(getPosY()>yMax)
			setDy(0);
	}
	
	/**
	 * Check if the sprite should be falling
	 */
	private void checkFalling() {
		if(getPosY()!=yGround)
			state= GOING_DOWN;
	}
	
	/**
	 * Updates the rising part of the jump
	 */
	private void updateRising() {
		if(upCount ==MAX_UP_STEPS) {		//if he reaches the max height
			state = STAND;					//he should have an instant in this position
			upCount = 0;
		}
		else {
			setDy(-YSTEP);		//continues to go up
			upCount++;
		}

	}
	
	/**
	 * Updates the falling part of the jump
	 */
	private void updateFalling() {
		if(getPosY()>=yGround) {		//if he reaches the ground, set this state and conclude the jump
			state=STAND;
			setDy(0);
			upCount=0;
		}
		else {
			setDy(YSTEP);		//else continue to fall
		}
	}
	
	/**
	 * Santa gets hit
	 * @param damage. In a scale between 1 to 100. 
	 * @return The life of Santa after being hit
	 */
	public int getHit(int damage) {
		if(damage>=1 || damage <100)
			life -=damage;
		return life;
	}
	
	/**
	 * It shoots a present. A new object Present is added to the list of the presents shot by Santa
	 * He can shoot only if he didn't already shoot 10 presents 
	 */
	public void fire() {
		if(present.size()<10)
			present.add(new Present(getPosX(), getPosY(), "present", getImageLoader()));
	}
	
	/**
	 * It shoots a present. A new object Present is added to the list of the presents shot by Santa
	 * He can shoot only if he didn't already shoot 10 presents 
	 */
	public void missiles() {
		if(present.size()<10)
			present.add(new Missiles(getPosX()+getWidth()/2, getPosY(), "present", getImageLoader()));
	}
	
	/**
	 * Special attack by santa. It can happen only when he has hit at least 10 times the enemy.
	 */
	public void specialMissile() {
		if(specialShootAvailable()) {
			Missiles m = new Missiles(getPosX(), getPosY(), "specialPresent", getImageLoader());
			m.specialShot();
			if(present.size()<10)
				present.add(m);
			hitCounter = 0;
		}
	}
	/**
	 * Special attack by santa. It can happen only when he has hit at least 10 times the enemy.
	 */
	public void specialFire() {
		if(specialShootAvailable()) {
			Present p = new Present(getPosX(), getPosY(), "specialPresent", getImageLoader());
			p.specialShot();
			if(present.size()<10)
				present.add(p);
			hitCounter = 0;
		}
	}
	
	/**
	 * It increments the hit point of santa
	 */
	public void incrementHitCounter() {
		hitCounter ++;
	}
	
	/**
	 * It returns if the special shoot is available
	 * @return True if available
	 */
	public boolean specialShootAvailable() {
		return hitCounter>10;
	}
	
	/**
	 * returns the list of all the presents Santa has shot
	 * @return An ArrayList of all the presents shot
	 */
	public ArrayList<Present> getPresents(){
		return present;
	}
	
	
	/**
	 * It gives the remaining life of Santa
	 * @return int representing the percentage of life remained
	 */
	public int getLife() {
		return life;
	}
	
	/**
	 * 
	 * @return The percentage of the remaining life of santa
	 */
	public int getLifePercent() {
		return life *100/STARTING_LIFE;
	}

	
	
}
