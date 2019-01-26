package com.me.shapes;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import java.util.*;

public class ShapesView extends View
{
	Bitmap offScreen;
	Canvas canvas;
	int width, height;
	Paint paint;
	Random random;
	
	public ShapesView(Context cont, AttributeSet attrs) {
		super(cont, attrs);
		DisplayMetrics dm = cont.getResources().getDisplayMetrics();
		width = dm.widthPixels;
		height = dm.heightPixels;
		offScreen = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(offScreen);
		paint = new Paint();
		random = new Random();
	}
	
	private void setRandomColor() {
		paint.setColor(random.nextInt(0xffffff) | 0xff000000);
	}

	public void clear()
	{
		paint.setColor(Color.BLACK);
		canvas.drawRect(0, 0, width, height, paint);
	}

	public void drawCircle()
	{
		setRandomColor();
		canvas.drawCircle(random.nextInt(width), random.nextInt(height), 60, paint);
	}

	public void drawRect()
	{
		setRandomColor();
		int x = random.nextInt(width);
		int y = random.nextInt(height);
		canvas.drawRect(x, y, x + 50, y + 50, paint);
		
	}

	public void update()
	{
		postInvalidate();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.drawBitmap(offScreen, 0, 0, null);
	}
	
}
