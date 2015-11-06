package decked.mechanics;

import android.content.Context;
import android.graphics.Rect;

/**
 * A card that allows a player to move around on a board
 * @version v0.20
 */
public class MoveCard extends Card{
	/** Constructor */
	public MoveCard(int newId) {
		super(newId);
	}

	/** Initializing Constructor */
	public MoveCard(Context myContext, int image, int newId, String name, Rect dim) {
		super(myContext, image, newId, name, dim);
	}

	/** POLY: Sets the relative ranges of each card when each card is being played */
	@Override
	public void setRelativeRange(int cardID, int currentLocation, Board board) {
		affectedSpaces.clear();
		if(cardID == 0) {
			affectedSpaces.add(moveUp(currentLocation, board));
		} else if(cardID == 1) {
			affectedSpaces.add(moveDown(currentLocation, board));
		} else if(cardID == 2) {
			affectedSpaces.add(moveLeft(currentLocation, board));
		} else if(cardID == 3) {
			affectedSpaces.add(moveRight(currentLocation, board));
		}
	}

	/** POLY: Performs the card's actions when the card is played */
	@Override
	public void runCardAction(int resourceId, Player character, Player opponent, Board board) {
		// Move the player in a direction according to which card it is
		if(resourceId == 0) {
			character.setCurrentPos(moveUp(character.getCurrentPos(), board));
		} else if(resourceId == 1) {
			character.setCurrentPos(moveDown(character.getCurrentPos(), board));
		} else if(resourceId == 2) {
			character.setCurrentPos(moveLeft(character.getCurrentPos(), board));
		} else if(resourceId == 3) {
			character.setCurrentPos(moveRight(character.getCurrentPos(), board));
		}
	}
	
	/** POLY: Gets the priority of a card, MoveCard has priority of 2 */
	@Override
	public int getCardPriority() { return 2; }
	
	/** Returns the relative position */
	public int moveRight(int current, Board board) {
		current = board.moveRight(current);
		return current;
	}

	/** Returns the relative position */
	public int moveLeft(int current, Board board) {
		current = board.moveLeft(current);
		return current;
	}

	/** Returns the relative position */
	public int moveUp(int current, Board board) {
		current = board.moveUp(current);
		return current;
	}

	/** Returns the relative position */
	public int moveDown(int current, Board board) {
		current = board.moveDown(current);
		return current;
	}
}
