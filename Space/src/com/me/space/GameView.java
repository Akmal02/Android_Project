package com.me.space;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import java.util.*;

public class GameView extends View
{

    private final String LOGTAG = "SpaceGame";

	private Bitmap spaceShip, bullet;
	private final int FRAME_RATE = 16;
	private int sx, sy;
	private int tx, ty;
	private int bx, by;
	private double scale;
	private int dispX, dispY;
	private Paint p;

    private int starCount = 35;
    private ArrayList<Point> star;
	
	public GameView(Context cont) 
	{
		super(cont);
		p = new Paint();

		spaceShip = BitmapFactory.decodeResource(getResources(), R.drawable.ship);
		bullet = BitmapFactory.decodeResource(getResources(), R.drawable.bullet);

		DisplayMetrics dm = cont.getResources().getDisplayMetrics();
		dispX = dm.widthPixels;
		dispY = dm.heightPixels;
		
		Log.wtf(LOGTAG, "Screem Dim : "+dispX+"x"+dispY);

		scale = dispX / 240;

		spaceShip = Bitmap.createScaledBitmap(spaceShip, (int) (40 * scale), (int) (40 * scale), true);
        bullet = Bitmap.createScaledBitmap(bullet, (int) (5 * scale), (int) (30 * scale), true);

		sx = dispX / 2 - spaceShip.getWidth() / 2;
		sy = dispY;

		tx = sx;
		ty = dispY / 2;

        bx = sx + spaceShip.getWidth() / 2 - bullet.getWidth() / 2;
		by = sy;


		p = new Paint();
		p.setColor(Color.WHITE);
		p.setAlpha(128);

		Random rnd = new Random();
		star = new ArrayList<Point>();

		for (int i=0; i <= starCount; i++)
		{
			star.add(new Point(rnd.nextInt(dispX), rnd.nextInt(dispY)));
		}

	}
	
	@Override
	protected void onDraw(Canvas c)
	{
		int dx = (tx - sx) / 4;
		int dy = (ty - sy) / 4;
		sx += dx;
		sy += dy;
		by -= 50;

		for (int i=0; i <= starCount; i++)
		{
     		c.drawCircle((float) star.get(i).x, (float) star.get(i).y, dispX / 100, p);
			star.get(i).y += ((i % 5) + 1) * 1;
			if (star.get(i).y > dispY) 
			{
				star.get(i).y = 0;
			}
		}

		c.drawBitmap(bullet, bx, by, null);
		c.drawBitmap(spaceShip, sx, sy , null);

		if (by < -bullet.getHeight()) 
		{
			bx = sx + spaceShip.getWidth() / 2 - bullet.getWidth() / 2;
			by = sy;
		}


		postInvalidateDelayed(FRAME_RATE);
	}


	public boolean onTouchEvent(MotionEvent e) 
	{
//		int action = e.getAction();
		int index = e.getActionIndex();

		tx = (int) e.getX(index) - spaceShip.getWidth() / 2;
		ty = (int) e.getY(index) - spaceShip.getHeight();

		return true;
	}


}
