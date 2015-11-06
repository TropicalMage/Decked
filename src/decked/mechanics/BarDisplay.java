package decked.mechanics;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * A display for bars, used for health and energy.
 * @version v0.20
 */

public class BarDisplay {
	private int maxBar; // width
	private int x;
	private int y;
	private int height;
	private int barColor;
	
    private TextDisplay title;
    private TextDisplay value;
	
	/** Initialized Constructor */
	public BarDisplay(Context context, int newX, int newY, int newMaxWidth, int newHeight, int color, String newTitle, Paint paint) {
		Paint titlePaint = new Paint(paint);
		titlePaint.setTextAlign(Paint.Align.RIGHT);
		
		Paint valuePaint = new Paint(paint);
		valuePaint.setTextAlign(Paint.Align.CENTER);
		
		maxBar = newMaxWidth + newX;
		x = newX;
		y = newY;
		height = newHeight + newY;
		barColor = color;

		title = new TextDisplay(context, newTitle, titlePaint, newX, y + (int) paint.getTextSize());
		value = new TextDisplay(context, "", valuePaint, newX + newMaxWidth/2, height + (int) paint.getTextSize());
	}
	
	/** Draws the bar display */
	public void render(Canvas canvas, Paint textPaint, int currentValue, int maxValue) {
		// Bar display for current and maximum values
		RectF maxBarRect = new RectF(x, y, maxBar, height);
		RectF barRect = new RectF(x, y, (int) ((maxBar - x) * ((double) currentValue/100)) + x, height);
	    Paint paint = new Paint();
	    // Draw border under the rectangles
	    drawBorder(canvas, maxBarRect);
	    // Color is gray for maxBarRect
	    paint.setColor(0xff888888);
	    canvas.drawRoundRect(maxBarRect, height/2, height/2, paint);
	    if(currentValue != 0) {
	    	drawBorder(canvas, barRect);
	    	paint.setColor(barColor);
	    	canvas.drawRoundRect(barRect, height/2, height/2, paint);
	    }
	    
	    title.render(canvas);
	    value.setText("( " + currentValue + " / " + maxValue + ")");
	    value.render(canvas);
	}
	
	/** Method to draw a border for a RectF */
	public void drawBorder(Canvas canvas, RectF rect) {
		RectF border = new RectF();
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		border.left = rect.left - 1;
		border.top = rect.top - 1;
		border.right = rect.right + 1;
		border.bottom = rect.bottom + 1;
		canvas.drawRoundRect(border, height/2, height/2, paint);
	}
	/** Gettor */
	public int getBarHeight() { return height; }
}
