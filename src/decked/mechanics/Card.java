package decked.mechanics;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * A card that deals damage and consumes energy.
 * @version v0.11
 */
public class Card extends Drawable {
	private int id;
	private String name;
	
	protected List<Integer> affectedSpaces = new ArrayList<Integer>();
	
	/** Constructor */
	public Card(int newId) {
		this.id = newId;
	}

	/** Initializing Constructor */
	public Card(Context myContext, int image, int newId, String name, Rect dim) {
		super(myContext, image, dim);
		this.name = name;
		this.id = newId;
	}
	
	/** Render at a specific spot */
	public void render(Canvas canvas, Tuple<Integer, Integer> coords) {
		canvas.drawBitmap(bmp, coords.x, coords.y, null);
	}

	/** POLY: Sets the relative ranges of each card when each card is being played */
	public void setRelativeRange(int resourceId, int currentLocation, Board board) {}
	
	/** POLY: Performs the card's actions when the card is played */
	public void runCardAction(int resourceId, Player character, Player opponent, Board board) {}
	
	/** POLY: Gets the priority of a card */
	public int getCardPriority() { return -1; }
	
	/** Gettors */
	public int getId() { return id; }
	public String getName() { return name; }
	public List<Integer> getAffectedSpaces() { return affectedSpaces; }

	/** Settors */
	public void setName(String newName) { name = newName; }
}