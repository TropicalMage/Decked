package decked.mechanics;

import android.content.Context;
import android.graphics.Rect;

/**
 * A card that deals damage and consumes energy.
 * @version v0.20
 */
public class AttackCard extends Card {
	private int damageDealt;
	private int energyCost;

	/** Constructor */
	public AttackCard(int newId) {
		super(newId);
	}

	/** Initializing Constructor */
	public AttackCard(Context myContext, int image, int newId, String name, Rect dim) {
		super(myContext, image, newId, name, dim);
	}

	/** POLY: Sets the relative ranges of each card when each card is being played */
	@Override
	public void setRelativeRange(int cardID, int currentLocation, Board board) {
		affectedSpaces.clear();
		// Check specific attacks to see if the card is a valid attack
		if(cardID == 5) {
			attackOne(currentLocation, board);
		} else if(cardID == 6) {
			attackTwo(currentLocation, board);
		} else if(cardID == 7) {
			attackThree(currentLocation, board);
		} else if(cardID == 8) {
			attackFour(currentLocation, board);
		}
		for (int i = 0; i < affectedSpaces.size(); i++) {
			if (affectedSpaces.get(i) == -1) {
				affectedSpaces.remove(i);
				i--;
			}
		}
	}

	/** POLY: Performs the card's actions when the card is played */
	@Override
	public void runCardAction(int resourceId, Player player, Player oppPlayer, Board board) {
		// Update the range for the attack card to the current location
		setRelativeRange(resourceId, player.getCurrentPos(), board);

		// if current position of OPPOSITE player = any element of affectedSpaces, subtract damageDealt from their health
		for(int i = 0; i < affectedSpaces.size(); i++) {
			if(affectedSpaces.get(i) == oppPlayer.getCurrentPos() && player.getEnergy() != 0) {
				// Opponent takes damage
				oppPlayer.takeDamage(damageDealt);
				// Exit loop if damage is dealt
				break;
			}
		}
		player.loseEnergy(energyCost);
	}
	
	/** POLY: Gets the priority of a card, AttackCard has priority of 1 */
	@Override
	public int getCardPriority() { return 1; }
	
	/** Hits to the left and to the right of the player */
	public void attackOne(int current, Board board) {
		damageDealt = 30;
		energyCost = 25;
		affectedSpaces.add(board.getRelativePos(current, -1, 0));
		affectedSpaces.add(board.getRelativePos(current, 0, 0));
		affectedSpaces.add(board.getRelativePos(current, 1, 0));
	}
	
	/** Hits everywhere around the player */
	public void attackTwo(int current, Board board) {
		damageDealt = 15;
		energyCost = 25;
		affectedSpaces.add(board.getRelativePos(current, -1, 1));
		affectedSpaces.add(board.getRelativePos(current, 0, 1));
		affectedSpaces.add(board.getRelativePos(current, 1, 1));
		affectedSpaces.add(board.getRelativePos(current, -1, 0));
		affectedSpaces.add(board.getRelativePos(current, 0, 0));
		affectedSpaces.add(board.getRelativePos(current, 1, 0));
		affectedSpaces.add(board.getRelativePos(current, -1, -1));
		affectedSpaces.add(board.getRelativePos(current, 0, -1));
		affectedSpaces.add(board.getRelativePos(current, 1, -1));
	}
	
	/** Hits in an H pattern with the center being the current square */
	public void attackThree(int current, Board board) {
		damageDealt = 25;
		energyCost = 40;
		affectedSpaces.add(board.getRelativePos(current, -1, 1));
		affectedSpaces.add(board.getRelativePos(current, 1, 1));
		affectedSpaces.add(board.getRelativePos(current, -1, 0));
		affectedSpaces.add(board.getRelativePos(current, 0, 0));
		affectedSpaces.add(board.getRelativePos(current, 1, 0));
		affectedSpaces.add(board.getRelativePos(current, -1, -1));
		affectedSpaces.add(board.getRelativePos(current, 1, -1));
	}
	
	/** Hits in an X pattern with the center being the current square */
	public void attackFour(int current, Board board) {
		damageDealt = 15;
		energyCost = 15;
		affectedSpaces.add(board.getRelativePos(current, -1, 1));
		affectedSpaces.add(board.getRelativePos(current, 1, 1));
		affectedSpaces.add(board.getRelativePos(current, 0, 0));
		affectedSpaces.add(board.getRelativePos(current, -1, -1));
		affectedSpaces.add(board.getRelativePos(current, 1, -1));
	}
}
