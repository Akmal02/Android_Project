package com.me.mygame;

import android.content.*;
import android.util.*;
import android.view.*;
import android.graphics.*;
import java.util.Random;

public class GameView extends View
{
	public static final int TILE_SIZE = 24;
	public static final int TILE_X = 14;
	public static final int TILE_Y = 8;
	public static final byte[] probs = {
		0, 0, 0, 0, 0, 0, 0, 1, 1, 1,
		1, 1, 1, 2, 2, 2, 2, 2, 3, 3
	};
	
	Bitmap grass, water, box, ground;
	Bitmap person;
	int playerX,playerY;
	Bitmap gameMap;
	byte[][] mapdata;
	Random random;

	Bitmap crosshair;
	volatile boolean cOn;
	int cx, cy;

	public GameView(Context ctx, AttributeSet attrs)
	{
		super(ctx, attrs);

		playerX = 0;
		playerY = 0;
		random = new Random(123456L);

		mapdata = new byte[TILE_X][TILE_Y];

	
		loadBitmaps(ctx);
		update();
		
	}

	private void loadBitmaps(Context ctx)
	{
		BitmapFactory.Options opts = new BitmapFactory.Options();
		grass = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.grass, opts);
		water = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.water, opts);
		box = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.box, opts);
		ground = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ground, opts);

		person = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.person, opts);
		person = Bitmap.createScaledBitmap(person, 60, 60, true);

		crosshair = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.crosshair, opts);
		crosshair = Bitmap.createScaledBitmap(crosshair, 60, 60, true);

		Log.i("MyGame", String.format("Bitmap size : %d x %d", box.getWidth(), box.getHeight()));
	}



	private void buildGameMap()
	{
		if (gameMap != null) gameMap.recycle();
		Bitmap unscaledMap = Bitmap.createBitmap(TILE_X * TILE_SIZE, TILE_Y * TILE_SIZE, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(unscaledMap);
		byte[][] data = mapdata;
		for (int i = 0; i < TILE_X; i++)
		{
			for (int j = 0; j < TILE_Y; j++)
			{
				switch (data[i][j])
				{
					case 0:
						c.drawBitmap(grass, i * TILE_SIZE, j * TILE_SIZE, null);
						break;
					case 1:
						c.drawBitmap(ground, i * TILE_SIZE, j * TILE_SIZE, null);
						break;
					case 2:
						c.drawBitmap(water, i * TILE_SIZE, j * TILE_SIZE, null);
						break;
					case 3:
						c.drawBitmap(box, i * TILE_SIZE, j * TILE_SIZE, null);
						break;
				}
			}
		}

		gameMap = Bitmap.createScaledBitmap(unscaledMap, TILE_X * 60, TILE_Y * 60, false);
		unscaledMap.recycle();
	}

	public void update()
	{
		playerX = random.nextInt(TILE_X);
		playerY = random.nextInt(TILE_Y);
		randomMap();
		buildGameMap();
		postInvalidate();
	}
	
	public void randomMap() {
		for (int i = 0; i < TILE_X; i++) {
			for (int j = 0; j < TILE_Y; j++) {
				mapdata[i][j] = probs[random.nextInt(20)];
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		Log.i("MyGame", String.format("Screen size : %d x %d", w, h));
		float scale = w / 8;
		Log.i("MyGame", String.format("Scale factor relative to screen : %.2f pixels per tile (%f x %f tiles)", (float) scale, (float) 8, (float) (h / scale)));
	}


	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		synchronized (this)
		{
			switch (event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					cOn = true;
					cx = (int) Math.floor(event.getX() / 60);
					cy = (int) Math.floor(event.getY() / 60);
					break;
				case MotionEvent.ACTION_UP:
//					cOn = false;
					break;
			}
		}
		postInvalidate();
		return true;
	}


	@Override
	protected void onDraw(Canvas c)
	{
		long a = System.currentTimeMillis();

		c.drawBitmap(gameMap, 0, 0, null);
		c.drawBitmap(person, playerX * 60,  playerY * 60, null);

		if (cOn)
			c.drawBitmap(crosshair, cx * 60, cy * 60, null);
		Log.i("MyGame", String.format("Update time : %d ms", System.currentTimeMillis() - a));
	}

}
