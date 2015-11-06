package decked.mechanics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class TextDisplay {
	private String text;
	private Paint paint;
	private int x;
	private int y;

	/** Initializing Constructor */
	public TextDisplay(Context context, String string, Paint newPaint, int newX, int newY) {
		text = string;
		paint = newPaint;
		x = newX;
		y = newY;
	}

	/** Draw */
	public void render(Canvas canvas) {
		canvas.drawText(text, x, y, paint);
	}
	
	/** Gettors */
	public int getX() { return x; }
	public int getY() { return y; }
	
	/** Settors */
	public void setX(int newX) { x = newX; }
	public void setY(int newY) { y = newY; }
	public void setText(String newText) { text = newText; }
}
