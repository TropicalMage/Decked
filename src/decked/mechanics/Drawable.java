package decked.mechanics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Drawable {
	protected Rect dimensions;
	protected Bitmap bmp;

	public Drawable() {}
	
	/** Initialize a drawable with dimensions and image */
	public Drawable(Context context, int image, Rect dim) {
		dimensions = dim;
		setBitmap(context, image);
	}

	/** Initialize a slot with dimensions */
	public Drawable(Rect dim) {
		dimensions = dim;
	}

	/** Draw */
	public void render(Canvas canvas) {
		canvas.drawBitmap(bmp, dimensions.left, dimensions.top, null);
	}
	
	/** Gettors */
	public Tuple<Integer, Integer> getCoords() {
		return new Tuple<Integer, Integer>(dimensions.left, dimensions.top);
	}
	public Rect getDim() { return dimensions; }
	public Bitmap getBitmap() { return bmp; }
	
	/** Settors */
	public void setPos(Tuple<Integer, Integer> coords) {
		dimensions.left = coords.x;
		dimensions.top = coords.y;
	}
	public void setBitmap(Context myContext, int image) {
        Bitmap tempBitmap = BitmapFactory.decodeResource(myContext.getResources(), image);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(tempBitmap, dimensions.width(), dimensions.height(), false);
		bmp = scaledBitmap;
	}
}
