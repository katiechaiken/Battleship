package finalproject;

import java.awt.Color;

import cse131.ArgsProcessor;
import cse131.NotYetImplementedException;
import sedgewick.StdDraw;

public class OnePlayerBattleship implements Battleship {

	private ArgsProcessor ap;
	private Player p1;
	private boolean numRandomShips;
	private int totalNumShips;
	private int width;
	private int height;

	/**
	 * The main method that gets the starting parameters for a game,
	 * creates an instance of the OnePlayerBattleship class,
	 * and plays the game!
	 * @param args
	 */
	public static void main(String[] args) {
		ArgsProcessor ap = new ArgsProcessor(args);

		String name = ap.nextString("What is the player's name?");
		int length = ap.nextInt("What is the length of the board?");
		int width = ap.nextInt("What is the width of the board?");
		int numShips = ap.nextInt("How many ships should each player have?");
		boolean randomShips = ap.nextBoolean("Should the ships be placed randomly? Type true or false");
		Battleship bs = new OnePlayerBattleship(length, width, randomShips, numShips, name, ap);
		Player winner = bs.play();
		System.out.println(winner.getName() + " won!");
		StdDraw.setPenColor(Color.BLACK);
		StdDraw.setPenRadius(.5);
		StdDraw.text(0.5, 0.06, winner.getName() + " won!");
	}

	/**
	 * Create an instance of the OnePlayerBattleship class
	 * Create a player with the given name
	 * Place all ships, randomly or manually
	 * 
	 * NOTE: We've created the player for you, in order to deal with the ArgsProcessor that needs to be passed
	 * 
	 * @param width width of the board (# cols)
	 * @param height height of the board (# rows)
	 * @param randomShips whether or not the ships should be placed randomly
	 * @param playerName the name of the Player who will be playing the game
	 */
	public OnePlayerBattleship(int width, int height, boolean randomShips, int numShips, String playerName, ArgsProcessor ap) {
		p1 = new HumanPlayer(playerName, width, height, ap); // DON'T CHANGE THIS
		this.numRandomShips = randomShips;
		this.totalNumShips = numShips;
		this.width = width;
		this.height = height;
		if(this.numRandomShips ==  false) {
			for(int i = 0; i < this.totalNumShips; i++) {
				int x = ap.nextInt("Top left x coordinate");
				int y = ap.nextInt("Top left y coordinate");
				int length = ap.nextInt("Length of ship");
				boolean isHorizontal = ap.nextBoolean("true for horizontal, false for vertical");
				Ship s = new Ship(x, y, length, isHorizontal);

				while(p1.isValidShipToAdd(s) == false) {
					System.out.println("That ship didn't work, enter another one");
					x = ap.nextInt("Top left x coordinate");
					y = ap.nextInt("Top left y coordinate");
					length = ap.nextInt("Length of ship");
					isHorizontal = ap.nextBoolean("true for horizontal, false for vertical");
					s = new Ship(x, y, length, isHorizontal);
				}

				p1.addShip(s);
			}

		}
	}


	@Override
	/**
	 * Start/play the game!
	 * @return the player who won the game
	 */
	public Player play() {
		if(this.numRandomShips == true) {
			int randLength;
			for(int i = 0; i < this.totalNumShips; i++) {
				if(this.height > this.width) {
					randLength = (int)(Math.random() * width);
					while(randLength < 3) {
						randLength = (int)(Math.random() * width);
					}
				}
				else{
					randLength = (int)(Math.random() * height);
					while(randLength < 3) {
						randLength = (int)(Math.random() * height);
					}
				}

				p1.addRandomShip(randLength);

			}

		}



		while(turn(p1) == true) {
			turn(p1);
		}
		return  p1;
	}

	@Override
	/**
	 * Play one turn of Battleship -- this consists of one player making one move
	 * 		1) Print out the board for the given player
	 * 		2) Get the move (row, col) from the given player
	 * 		3) Check if that move was a hit or miss on the other player (will be the same player in OnePlayerBattleship)
	 * 		4) Update the player's radar
	 * Note: be sure to print out "You sunk my battleship!" if a hit sinks a ship
	 * 
	 * @param p the player whose turn it is
	 * @return true if the game is over, false otherwise
	 */
	public boolean turn(Player p) {
		StdDraw.text(0.5, 0, " ");
		System.out.println("Current Radar: ");
		p.printRadar();
		int firstAfloat = p.numShipsStillAfloat();
		int array[] = p.getTargetLocation();
		p.recordHitOrMiss(array[0], array[1], p.respondToFire(array[0], array[1]));
		int nowAfloat = p.numShipsStillAfloat();
		if(nowAfloat == firstAfloat - 1) {
			System.out.println("You sunk my battleship!");	
			StdDraw.setPenColor(Color.BLACK);
			if(nowAfloat == 0) {
				System.out.println("Final radar: ");
				p.printRadar();
				return false;
			}

		}
		if(nowAfloat == 0) {
			return false;
		}
		return true;

	}

	/**
	 * We've implemented this for you since there's only one player, you can leave this alone!
	 */
	@Override
	public Player getPlayerOne() {
		return p1;
	}

	/**
	 * We've implemented this for you since there's only one player, you can leave this alone!
	 */
	@Override
	public Player getPlayerTwo() {
		return null;
	}

}
