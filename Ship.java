package finalproject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cse131.NotYetImplementedException;

/**
 * @author Mariah Yelenick and Adam Kern
 * @version 11/18/18
 */
public class Ship {

	/**
	 * Create an instance of the Ship class, recording all necessary information into
	 * any instance variables you choose to create
	 * 
	 * @param topLeftX the x coordinate of the Ship's uppermost, leftmost space
	 * @param topLeftY the y coordinate of the Ship's uppermost, leftmost space
	 * @param length the number of spaces the Ship occupies
	 * @param isHorizontal if true, the Ship is placed horizontally, else the Ship is placed vertically
	 */

	int topLeftX;
	int topLeftY;
	int length;
	boolean isHorizontal;
	int numHits = 0;
	Set<Integer> xCoord = new HashSet<Integer>();
	Set<Integer> yCoord = new HashSet<Integer>();

	
	public Ship(int topLeftX, int topLeftY, int length, boolean isHorizontal) {
		this.topLeftX = topLeftX;
		this.topLeftY = topLeftY;
		this.length = length;
		this.isHorizontal = isHorizontal;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Ship [topLeftX=" + topLeftX + ", topLeftY=" + topLeftY + ", length=" + getLength() + ", isHorizontal="
				+ isHorizontal + ", numHits=" + numHits + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isHorizontal ? 1231 : 1237);
		result = prime * result + length;
		result = prime * result + numHits;
		result = prime * result + topLeftX;
		result = prime * result + topLeftY;
		result = prime * result + ((xCoord == null) ? 0 : xCoord.hashCode());
		result = prime * result + ((yCoord == null) ? 0 : yCoord.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ship other = (Ship) obj;
		if (isHorizontal != other.isHorizontal)
			return false;
		if (length != other.length)
			return false;
		if (numHits != other.numHits)
			return false;
		if (topLeftX != other.topLeftX)
			return false;
		if (topLeftY != other.topLeftY)
			return false;
		if (xCoord == null) {
			if (other.xCoord != null)
				return false;
		} 
		else if (!xCoord.equals(other.xCoord))
			return false;
		if (yCoord == null) {
			if (other.yCoord != null)
				return false;
		} 
		else if (!yCoord.equals(other.yCoord)) {
			return false;

		}
		
		return true;
	}


	/**
	 * Check if the Ship has been sunk
	 * This should only be true if the Ship was hit in all the spaces it occupies
	 * 
	 * @return true if the Ship has been sunk
	 */
	public boolean isSunk() {
		if(this.isHorizontal == true) {
			if(xCoord.size() == this.length) {
				return true;
			}
		}
		else if(this.isHorizontal == false) {
			if(yCoord.size() == this.length)
			return true;
		}

		return false;
	}
	
	/**
	 * Check if the Ship occupies a space at (x, y)
	 * Note: Be sure to update the hits array so that you can check if the Ship is sunk!
	 * 
	 * @param x the x coordinate to shoot at
	 * @param y the y coordinate to shoot at
	 * @return true if this Ship occupies that spot (hit), false otherwise (miss)
	 */
	
	public  boolean isHit(int x, int y) {
		if(x == topLeftX && y == topLeftY) {
			xCoord.add(x);
			yCoord.add(y);
			return true;
		}
		if(isHorizontal == true) {
			if(x > topLeftX + length) {
				return false;
			}
			else if(x == topLeftX && y == topLeftY) {
				xCoord.add(x);
				return true;
			}
			else if((x - topLeftX) < length && (x- topLeftX) > -1 && y == topLeftY && x >= 0 && y >= 0) {
				xCoord.add(x);
				return true;
			}
		}

		else if(isHorizontal == false) {
			if(y > topLeftY + length) {
				return false;
			}
			else if(x == topLeftX && y == topLeftY) {
				yCoord.add(y);
				return true;
			}
			else if((y - topLeftY) < length  && (y- topLeftY) > -1 && x == topLeftX && x >= 0 && y >= 0) {
				yCoord.add(y);
				return true;
			}
		}
		return false;
	}


//Getters for HumanPlayer class
	/**
	 * @return length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return topLeftX
	 */
	public int getTopLeftX() {
		return topLeftX;
	}
	/**
	 * @return topLeftY
	 */
	public int getTopLeftY() {
		return topLeftY;
	}
	/**
	 * @return isHorizontal
	 */
	public boolean getIsHorizontal() {
		return isHorizontal;
	}
}
