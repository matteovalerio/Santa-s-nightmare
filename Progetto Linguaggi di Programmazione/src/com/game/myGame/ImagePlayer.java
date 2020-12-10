package com.game.myGame;

import java.awt.image.BufferedImage;

/**
 * This class manages the animation of the images
 * @author MatteoValerio
 *
 */

public class ImagePlayer {

	private String name;
	private int animPeriod;
	private double seqDuration;
	private boolean isRepeating;
	private int numImages;
	private ImageLoader imgL;
	private int imPosition = 0;
	private boolean ticksIgnored = false;
	private long animTotalTime;
	private int showPeriod;
	
	/**
	 * Public constructor.
	 * @param name Name of the sprite
	 * @param animPeriod In milliseconds. The ticking period
	 * @param seqDuration in seconds. The total duration of the animation
	 * @param isRepeating True if it's repeating
	 * @param imgL The image loader
	 */
	public ImagePlayer(String name, int animPeriod, double seqDuration, boolean isRepeating, ImageLoader imgL) {
		this.animPeriod=animPeriod;
		this.imgL=imgL;
		this.isRepeating=isRepeating;
		this.name=name;
		this.seqDuration=seqDuration;
		numImages = imgL.numImages(name);
		animTotalTime = System.currentTimeMillis();
		showPeriod = (int)(1000*numImages/seqDuration);
	}
	
	/**
	 * It updates the image with a tick.
	 */
	public void updateTick() {
		if(!ticksIgnored) {
			
			//calcolates the position to return on the base of the animPeriod and seqDuration
			
			animTotalTime = (animTotalTime+animPeriod)%(long)(1000*seqDuration);
			
			imPosition = (int)(animTotalTime/showPeriod);
			
			if(imPosition==(numImages-1) && !isRepeating) //stop sequence
				ticksIgnored = true;
		}
	}
	
	/**
	 * 
	 * @return The current BufferedImage calculated by updateTick  
	 */
	public BufferedImage getCurrentImage() {
		if(numImages!=0) {
			if(imPosition>numImages)
				imPosition = 0;
			return imgL.getImage(name, imPosition);
		}
		else 
			return null;
	}
	
	/**
	 * Stop the animation
	 */
	public void stop() {
		ticksIgnored = true;
	}
	
	/**
	 * Resume the animation
	 */
	public void resume() {
		ticksIgnored = false;
	}
	
	/**
	 * Restart the animation from the given position
	 * @param imPosition The position to start at
	 */
	public void restartAt(int imPosition) {
		this.imPosition = imPosition;
		ticksIgnored = false;
	}
}
