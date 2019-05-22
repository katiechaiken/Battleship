package finalproject;
import java.awt.Color;

import sedgewick.StdDraw;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


import cse131.ArgsProcessor;
import cse131.NotYetImplementedException;
import sedgewick.StdDraw;

public class HumanPlayer implements Player {

	private final ArgsProcessor ap; // Don't change this!
	private final String name;
	private final int height;
	private final int width;
	private final String[][] boardHits;
	private final boolean[][] shipLocation;
	private final Set<Ship> setup;
	/**
	 * Creates an instance of the HumanPlayer class
	 * Note that we already dealt with taking in an ArgsProcessor object
	 * 		We know you've never seen this before, which is why it's given to you
	 * 		You can treat this ArgsProcessor (ap) throughout the HumanPlayer class
	 * 			like any other ArgsProcessor you've used in 131
	 * We leave the rest of the constructor to you
	 * 
	 * @param name the name of the player
	 * @param height the height of the boardHits
	 * @param width the width of the boardHits
	 * @param ap the ArgsProcessor object
	 */
	public HumanPlayer(String name, int height, int width, ArgsProcessor ap) {
		this.ap = ap;
		this.name = name;
		this.height = height;
		this.width = width;
		this.boardHits = new String[this.width][this.height]; //contains hits
		this.shipLocation = new boolean[this.width][this.height]; //contains where exactly ships are
		this.setup = new HashSet<Ship>();
	}




	@Override
	/**
	 * @return the name of the player
	 */
	public String getName() {
		return this.name;
	}


	@Override
	/**
	 * Tell how many of the player's ships have been sunk / how many have not yet been sunk
	 * 
	 * @return the number of ships still afloat
	 */
	public int numShipsStillAfloat() {
		int sunkCount = 0;
		for(Ship s : setup) {
			if(s.isSunk() == true) {
				sunkCount++;
			}
		}
		return setup.size()-sunkCount;

	}


	@Override
	/**
	 * Tell whether a given ship s will intersect any of the existing ships
	 * @param s the ship to add
	 * @return whether the ship intersects any other ships on the boardHits
	 */
	public boolean isValidShipToAdd(Ship s) {
		if (setup.contains(s)) {
			return false;
		}
		else if (s.getIsHorizontal() == true && s.getTopLeftX() + s.getLength() > this.width ) {
			return false;
		}
		else if(s.getTopLeftX() < 0 || s.getTopLeftY() < 0) {
			return false;
		}
		else if(s.getIsHorizontal() == false && s.getTopLeftY() + s.getLength() > this.height ) {
			return false;
		}



		for(int i = 0; i < s.getLength(); i++) {
			for(int j = 0; j < s.getLength(); j++) {
				if(s.getIsHorizontal() == true) {
					if(shipLocation[s.getTopLeftX() + i][s.getTopLeftY()] == true) {
						return false;
					}		
				}
				else if(s.getIsHorizontal() == false) {
					if(shipLocation[s.getTopLeftX()][s.getTopLeftY() + i] == true) {
						return false;
					}
				}

			}
		}

		return true;
	}

	@Override
	/**
	 * Add a ship to whatever collection of ships the Player uses to keep track of its ships
	 * Note: This method should only return true upon successful addition to the collection
	 * 
	 * @param s the ship to be added
	 * @return whether the ship was successfully added to the collection
	 */
	public boolean addShip(Ship s) {

		if(isValidShipToAdd(s) == true) {
			for(int i = 0; i < s.getLength(); i++) {
				if(s.isHorizontal == true) {
					shipLocation[s.getTopLeftX() + i][s.getTopLeftY()] = true;

				}
				else {
					shipLocation[s.getTopLeftX()][s.getTopLeftY() + i] = true;
				}
			}
			setup.add(s);

			return true;

		}
		else {
			return false;
		}
	}

	@Override
	/**
	 * Tell whether any of the player's ships were hit given an (x, y) firing location
	 * And record that hit on the ship
	 * 
	 * @param x the x location the other player shot at
	 * @param y the y location the other player shot at
	 * @return true if hit, false if miss
	 */
	public boolean respondToFire(int x, int y) {

		for(Ship s : setup) {
			if(s.isHit(x, y) == true) {
				boardHits[x][y] = "X"; //marking as hit on the boardHits

				return true;
			}
		}
		return false;


	}

	@Override
	/**
	 * Randomly place a ship of the given length in a valid location
	 * Be sure to add it to the collection of ships for the game
	 * Note: The random ship should NOT OVERLAP any existing ships
	 * 
	 * @param length the length of the ship to be added
	 */
	public boolean addRandomShip(int length) {
		boolean isHorizontal;
		int topLeftX;
		int topLeftY;
		if(Math.random() < 0.5) {
			isHorizontal = false; //vertical
		}
		else {
			isHorizontal = true; //horizontal
		}

		boolean isValidCheck = false;
		while(isValidCheck == false) {
			topLeftX  = (int)(Math.random() * this.width);
			topLeftY = (int)(Math.random() * this.height);
			Ship s = new Ship(topLeftX, topLeftY, length, isHorizontal);
			if(this.isValidShipToAdd(s)) {
				isValidCheck = true;
				addShip(s);
				setup.add(s);
				return true;
			}
		}
		return false;

	}


	/**
	 * Ask the user for the location they want to fire at
	 * 
	 * @return an integer array of length 2 which contains the (x,y) location the player wants to fire at
	 */
	@Override
	public int[] getTargetLocation() {
		int[] array = new int [2];
		int x = ap.nextInt("Where do you want the x-coordinate?");
		int y = ap.nextInt("Where do you want the y-coordinate?");

		while((x < 0 || x >= this.width) || (y >= this.height || y < 0)) {
			x = ap.nextInt("Where do you want the x-coordinate?");
			y = ap.nextInt("Where do you want the y-coordinate?");

		}

		array[0] = x;
		array[1] = y;
		return array;

		// What is the maximum value acceptable. 
		//Think about what the maximum value allowed in an array is compared to the length.
	}


	@Override
	/**
	 * Record on the boardHits/radar whether a shot made was a hit or a miss
	 * 
	 * @param x the x location to record if hit or miss
	 * @param y the y location to record if hit or miss
	 * @param isHit whether the shot was a hit (true) or miss (false)
	 */
	public void recordHitOrMiss(int x, int y, boolean isHit) {
		if(isHit == true) {
			boardHits[x][y] = "X";
		}
		else if(isHit == false) {
			boardHits[x][y] = "O";
		}
	}


	@Override
	/**
	 * Given the length of a ship, decide where on the boardHits to place that ship
	 * Note: The decision must be valid, i.e. the ship does not overlap any other ships or go off the boardHits, etc
	 * 
	 * @param sl the length of the ship to place
	 * @return a Ship instance with the desired (x, y) location, length, and orientation
	 */
	public Ship decideShipPlacement(int length) {
		int topLeftX = ap.nextInt("What is the top left x");
		int topLeftY = ap.nextInt("What is the top left y");
		boolean isHorizontal = ap.nextBoolean("true for horizontal, false for vertical");
		Ship s = new Ship(topLeftX, topLeftY, length, isHorizontal);
		while(isValidShipToAdd(s) != true) {
			topLeftX = ap.nextInt("What is the top left x");
			topLeftY = ap.nextInt("What is the top left y");
			isHorizontal = ap.nextBoolean("true for horizontal, false for vertical");
			s = new Ship(topLeftX, topLeftY, length, isHorizontal);
		}
		return s;
	}
	@Override
	/**
	 * Prints out the radar indicating where the user has fired,
	 * and whether those fires were hits or misses
	 */
	public void printRadar() {

		for(int i = 0; i < this.height; i++) {
			for(int j = 0; j < this.width; j++) {
				if(boardHits[j][i] == "X") {
					System.out.print("X");
				}
				else if(boardHits[j][i] == "O") {
					System.out.print("o");
				}
				else {
					System.out.print(".");
				}
			}
			System.out.println();
		}
		double newY = 0;
		double newX = 0;
		StdDraw.setPenColor(Color.GRAY);
		StdDraw.filledRectangle(.5, .5, 1, 1);
		for(int i = 0; i < this.width; i++) {
			newY = 0;
			for(int j = 0; j < this.height; j++) {
				if(boardHits[i][j] == "X") {
					StdDraw.setPenColor(Color.RED);
					StdDraw.filledRectangle(0.02 + newX, 1 - newY, .01*(10/this.width), .01*(10/this.height) );
					newY += 0.1 * (10/this.height);
				}
				else if(boardHits[i][j] == "O") {
					StdDraw.setPenColor(Color.BLACK);
					StdDraw.filledRectangle(.02 + newX, 1 - newY, .01*(10/this.width), .01*(10/this.height) );
					newY += 0.1 * (10/this.height);
				}
				else {
					StdDraw.setPenColor(Color.WHITE);
					StdDraw.filledRectangle(0.02 + newX, 1 - newY, .01*(10/this.width), .01*(10/this.height) );
					newY += 0.1 * (10/this.width);
				}

			}
			newX += 0.1 * (10/this.width);
		}	

	}
}
