package decked.mechanics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class ToggleButton extends Drawable {
	private Bitmap onBmp;
	private boolean on = false;
	
	public ToggleButton(Context context, int image, int pressedImg, Rect dim) {
		super(context, image, dim);
		setPressedBmp(context, pressedImg);
	}
	
	@Override
	public void render(Canvas canvas) {
		if (on)
			canvas.drawBitmap(onBmp, dimensions.left, dimensions.top, null);
		else
			canvas.drawBitmap(bmp, dimensions.left, dimensions.top, null);
	}
	
	public void setPressedBmp(Context context, int image) {
        Bitmap tempBitmap = BitmapFactory.decodeResource(context.getResources(), image);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(tempBitmap, dimensions.width(), dimensions.height(), false);
        onBmp = scaledBitmap;
	}
	
	public boolean isOn() { return on; }
	
	public void turnOn() { on = true; }
	public void turnOff() { on = false; }
	public void toggle() { on = !on; }
}

