package com.me.cube;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import java.util.*;

public class CubeView extends View
{


	private int playerChip, compChip;

	private boolean running;

	private int n, x;

	public CubeView(Context ctx, AttributeSet attrs)
	{
		super(ctx, attrs);
	}


	public void send(int chip1, int chip2)
	{
		this.playerChip = chip1;
		this.compChip = chip2;
		running = true;
		x = 0;
		n = 0;
		postInvalidate();

	}

	public boolean isInAnimation()
	{
		return running;
	}

	@Override
	public void onDraw(final Canvas c)
	{		
		if (running)
		{
			int oneThird = c.getHeight() / 3;
			int chipSize = oneThird * 2;
			int onePart = oneThird / 2;
			int halfWidth = c.getWidth() / 2;

			Paint p = new Paint();
			p.setColor(Color.GRAY);

			RectF rect = new RectF(- chipSize + x, onePart, x, onePart + chipSize);
			c.drawRoundRect(rect, onePart, onePart, p);

			RectF rect2 = new RectF(c.getWidth() - x, onePart, c.getWidth() - x + chipSize, onePart + chipSize);
			c.drawRoundRect(rect2, onePart, onePart, p);

			p.setColor(Color.GREEN);
			p.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));			
			p.setTextSize(50);

			int cx = onePart + chipSize - (chipSize - 50) / 2;
			c.drawText("" + playerChip + " vs " + compChip, 20, cx, p);

			if ((x += (n++ / 2)) > halfWidth)
			{
				running = false;
			}
			else
			{
				postDelayed(new Runnable() {
						public void run()
						{
							invalidate();
						}
					}, 20);
			}
		}
	}
}
