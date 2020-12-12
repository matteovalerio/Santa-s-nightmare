package com.game.myGame;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * This class is meant to be the super class of every character or object that appears in the game.
 * @author matte
 *
 */
public class Sprite {

	private int posX;
	private int posY;
	private int height, width;
	private int dx;
	private int dy;
	private boolean isActive;
	private boolean rightDirection;
	private boolean isLooping;
	private ImageLoader imgL;
	private BufferedImage img;
	private String name;
	protected final static int XSTEP = 7;
	protected final static int YSTEP = 7;
	private final int SIZE = 12;
	private ImagePlayer imgP;
	
	/**
	 * The public constructor. It takes the x and y starting position, a name and the image loader
	 * @param posX x starting position
	 * @param posY y starting position
	 * @param name name for the sprite
	 * @param imgL The image loader for animation
	 */
	public Sprite(int posX, int posY, String name, ImageLoader imgL) {
		this.posX = posX;
		this.posY = posY;
		
		dx = 0;
		dy = 0;
		
		isActive = true;
		this.imgL = imgL;
		setImage(name);
		
		//now i have the height of the image: i can adjust the posY
		this.posY = posY - height*2;
	}

	
	/**
	 * It sets the image associated with the sprite
	 * @param imgName The name of the sprite
	 */
	public void setImage(String imgName) {
		name = imgName;
		img = imgL.getImage(name);
		
		//if there isn't any image associated with this name
		if(img == null) {
			System.err.println("No image found with this name");
			height = SIZE;
			width = SIZE;
		}
		else {
			
			width = img.getWidth() +SIZE;
			height = img.getHeight()+ SIZE;
		}
		imgP = null;
		rightDirection = true;
		isLooping = false;
	}
	
	
	/**
	 * It sets the direction of the sprite(right or left).
	 * @param rightDirection True if facing right
	 */
	public void setDirection(boolean rightDirection) {
		this.rightDirection = rightDirection;
	}
	
	
	/**
	 * Total duration of the animation is seqDuration(in seconds), the ticking interval in milliseconds is animPeriod
	 * @param animPeriod Milliseconds ticking interval
	 * @param seqDuration Seconds Total duration of the animation
	 */
	public void startLooping(int animPeriod, double seqDuration) {
		
		if(imgL.numImages(name) > 1) {
			imgP = new ImagePlayer(name, animPeriod, seqDuration, true, imgL);
			
			isLooping = true;
		}
		
		else
			System.err.println(name + " is not a sequence of images");
	}
	
	/**
	 * change the sprite dimensions
	 * @param d The new Dimensions
	 */
	public void setImageDimension(Dimension d) {
		width = d.width;
		height =d.height;
	}
	
	/**
	 * It stops the loop of the sprite
	 */
	public void stopLooping() {
		if(isLooping) {
			imgP.stop();
			isLooping = false;
		}
	}
	
	/**
	 * It returns a rectangle that has the same dimensions and positions of the image of the sprite
	 * @return
	 */
	public Rectangle getMyRectangle() {
		return new Rectangle(posX,posY,width,height);
	}
	
	/**
	 * It updates the movements variables of the sprite
	 */
	public void updateSprite() {
		if(isActive) {
			posX += dx;
			posY += dy;
			if(isLooping)
				imgP.updateTick();	//change image
		}
	}
	
	/**
	 * It paints the sprite in a offscreen graphics, for buffering purpose only.
	 * @param g The offscreen graphics
	 */
	public void paintSprite(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //anti-alias setting
		if(isActive) {
			if(img!=null) {
				if(isLooping)
					img = imgP.getCurrentImage();
				if(rightDirection)
					g2d.drawImage(img, posX, posY, width, height, null);
				else
					g2d.drawImage(img, posX+width, posY, -(width), height, null);
			}
		}
		
	}

	/**
	 * Move the sprite following the modifications of the method updateSprite. It takes a boolean
	 * rightDirection as a parameter: true if facing right 
	 * @param rightDirection true if facing right
	 */
	public void move(boolean rightDirection) {
		this.rightDirection = rightDirection;
		if(rightDirection)
			dx = XSTEP;
		else
			dx = -XSTEP;
	}
	
	/**
	 * It moves up the screen the sprite
	 * @param rightDirection True if facing right
	 */
	public void moveUp(boolean rightDirection) {
		this.rightDirection = rightDirection;
		dy = -YSTEP;
	}
	
	/**
	 * Moves down the sprite
	 * @param rightDirection True if facing right
	 */
	public void moveDown(boolean rightDirection) {
		this.rightDirection = rightDirection;
		dy = YSTEP;
	}
	
	/**
	 * It sets the running animation facing right if the given parameter is true
	 * @param rightDirection True if the animation should be facing right
	 */
	public void run(boolean rightDirection) {
		this.rightDirection = rightDirection;
		if(dx!=0) {
			if(rightDirection)
				dx = XSTEP*2;
			else
				dx = -XSTEP*2;
		}
		if(dy!=0) {
			if(dy>0)
				dy = YSTEP*2;
			else
				dy = -YSTEP*2;
		}
	}
	
	
	/**
	 * It stops the movement of the sprite. It takes a boolean
	 * rightDirection as a parameter: true if facing right 
	 * @param rightDirection true if facing right
	 */
	public void stop(boolean rightDirection) {
		this.rightDirection=rightDirection;
		dx=0;
		dy=0;
	}
	
	/**
	 * 
	 * @return The y position
	 */
	public int getPosY() {
		return posY;
	}
	
	/**
	 * 
	 * @return If it's active
	 */
	public boolean isActive() {
		return isActive;
	}
	
	/**
	 * 
	 * @return The dx parameter, that is the step of movement in the x axis
	 */
	public int getDx() {
		return dx;
	}
	
	/**
	 * It sets the new movement 
	 * @param dx The steps of movement
	 */
	public void setDx(int dx) {
		this.dx = dx;
	}
	
	/**
	 * 
	 * @return The dy parameter, that is the step of movement in the y axis
	 */
	public int getDy() {
		return dy;
	}
	
	/**
	 * 
	 * @param dy Set the dy parameter, that is the step of movement in the y axis
	 */
	public void setDy(int dy) {
		this.dy = dy;
	}
	
	/**
	 * 
	 * @return If it's looping
	 */
	public boolean isLooping() {
		return isLooping;
	}
	
	/**
	 * 
	 * @param x Set the position in the x axis
	 */
	public void setPosX(int x) {
		posX = x;
	}
	
	/**
	 * 
	 * @return Return the position in x axis
	 */
	public int getPosX() {
		return posX;
	}
	
	/**
	 * 
	 * @param y Set the position in the y axis
	 */
	public void setPosY(int y) {
		posY = y;
	}
	
	/**
	 * 
	 * @return If it's facing right
	 */
	public boolean isRightDirection() {
		return rightDirection;
	}
	
	/**
	 * 
	 * @return The name of the sprite
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return The imageLoader of the sprite
	 */
	public ImageLoader getImageLoader() {
		return imgL;
	}
	
	/**
	 * 
	 * @return The imagePlayer that manages the animation of the sprite
	 */
	public ImagePlayer getImagePlayer() {
		return imgP;
	}
	
	/**
	 * 
	 * @return The width of the image
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * 
	 * @return The height of the image
	 */
	public int getHeight() {
		return height;
	}
}
