package com.game.myGame;

import java.util.HashMap;

import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class loads all the images and stores them in some data structure.
 * @author MatteoValerio
 *
 */
public class ImageLoader {

	private HashMap<String,ArrayList<BufferedImage>> imagesMap = new HashMap<>();	// the key is the image's name, the value is an ArrayList 
																					// Buffered image object associated with it
	
	private HashMap<String,ArrayList<String>> gNamesMap = new HashMap<>();	// the key is the name of the character/object, the value
																			// a List of the name of the files associated with it
	
	/**
	 * get a BufferedImage. If there's only one it returns the first.
	 * @param name Image's name
	 * @return the first or the only one BufferedImage
	 */
	public BufferedImage getImage(String name) {
		return imagesMap.get(name).get(0);
	}
	
	/**
	 * Get a specific image at a specific position
	 * @param name Image's name
	 * @param posn The position of the image
	 * @return The BufferedImage requested
	 */
	public BufferedImage getImage(String name, int posn) {
		return imagesMap.get(name).get(posn);
	}
	
	/**
	 * Get the list of images associated to a specific name
	 * @param name The name to which is associated the list of images
	 * @return The list of images
	 */
	public ArrayList<BufferedImage> getImages(String name) {
		return imagesMap.get(name);
	}
	
	/**
	 * Loads a single image from a specific path
	 * @param path The path in which the image is stored
	 * @return A copy of the BufferedImage loaded
	 */
	public BufferedImage loadImage(String path) {
		
		try {
			BufferedImage im = ImageIO.read(new File(path));
			
			Graphics2D g2d = im.createGraphics();
			g2d.drawImage(im, 0, 0, null);
			g2d.dispose();
			return im;
		} catch (IOException e) {
			System.err.println("Loading problem for " + path);
			return null;
		}
	}
	
	
	/**
	 * It loads a sheetImages at it returns an array list of buffered images with them
	 * @param path File path. Just the name without the extension and the number
	 * @param number Number of images in the sprite sheet
	 * @return An arraylist with all the buffered images.
	 */
	public ArrayList<BufferedImage> loadSheetImages(String path, int number) {
		
		if(number<=0) {
			System.err.println("Incorrect number of images");
			return null;
		}
		
		ArrayList<BufferedImage> img = new ArrayList<>(number);
		for(int i=0;i<number;i++) {
			img.add(loadImage(path+(i+1)+".png"));
		}
		return img;
	}
	
	/**
	  * Method to insertImages in the hash map
	 * @param name The key name associated to the list of images
	 * @param listImg an ArrayList of images. It can be one image.
	 * @throws IllegalArgumentException if the list of images listImg is null.
	 */
	public void insertImages(String name, ArrayList<BufferedImage> listImg) throws IllegalArgumentException{
		if(listImg==null) {
			throw new IllegalArgumentException("List is null");
		}
		System.out.println("Loading image:"+name + " "+listImg.size());
		imagesMap.put(name, listImg);
	}
	
	/**
	 * It links the file name associated to a certain character. For example Santa->{run, stand, shoot, punch}
	 * @param keyName The character/object name
	 * @param files The list of filenames
	 */
	public void linkName(String keyName, ArrayList<String> files) {
		gNamesMap.put(keyName, files);
	}
	
	/**
	 * Method that gives the number of images in the arrayList linked with the key name
	 * @param name The key
	 * @return The number of images 
	 */
	public int numImages(String name) {
		return imagesMap.get(name).size()-1;
	}
}
