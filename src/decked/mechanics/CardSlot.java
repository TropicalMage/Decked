package decked.mechanics;

import com.agpfd.decked.R;

import android.content.Context;
import android.graphics.Rect;
/**
 * A slot to contain a card.
 * @version v0.12
 */
public class CardSlot extends Drawable {
	private int currentSlot = -1;
	private Card containerCard;

	/** Initialize a slot with dimensions */
	public CardSlot(Context context, int image, Rect dim) {
		super(context, image, dim);
		containerCard = null;
	}

	/** Initialize a slot */
	public CardSlot(Context context, Card card) {
		super(context, R.drawable.card_slot, new Rect(card.getDim()));
		containerCard = card;
	}
	
	/** Gettors */
	public int getCurrentSlot(){ return currentSlot; }
	public Card getCCard() { return containerCard; }
	
	/** Settors */
	public void setCurrentSlot(int newSlot) { currentSlot = newSlot; }
	public void setCCard(Card card) {
		card.getDim().left = dimensions.left;
		card.getDim().top = dimensions.top;
		containerCard = card;
	}
	
	/** Empty cCard */
	public void removeCCard() { containerCard = null; }
}