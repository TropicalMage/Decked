package decked.mechanics;

import android.content.Context;
import android.graphics.Rect;

/**
 * A card that allows for other functionality, such as regaining energy and blocking attacks
 * @version v0.20
 */
public class UtilityCard extends Card{

	/** Constructor */
	public UtilityCard(int newId) {
		super(newId);
	}

	/** Initializing Constructor */
	public UtilityCard(Context myContext, int image, int newId, String name, Rect dim) {
		super(myContext, image, newId, name, dim);
	}

	/** POLY: Sets the relative ranges of each card when each card is being played */
	@Override
	public void setRelativeRange(int cardID, int currentLocation, Board board) {
		affectedSpaces.clear();
		if(cardID == 4 || cardID == 9) {
			affectedSpaces.add(currentLocation);
		}
	}

	/** POLY: Performs the card's actions when the card is played */
	@Override
	public void runCardAction(int resourceId, Player player, Player oppPlayer, Board board) {
		// Blocking
		if(resourceId == 4) {
			player.setIsBlocking(true);
		} 
		// Energy Regain
		else if(resourceId == 9)  {
			player.regainEnergy(15);
		}
	}
	
	/** POLY: Gets the priority of a card, UtilityCard has priority of 3 */
	@Override
	public int getCardPriority() { return 3; }
}
