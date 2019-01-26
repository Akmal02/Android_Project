package com.me.matchgame;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import java.util.*;

public class GameView extends View
{

	static class Block
	{

		/** Types of block */
		public static final int NONE = 0;
		public static final int RED = 1;
		public static final int BLUE = 2;
		public static final int GREEN = 3;
		public static final int YELLOW = 4;
		public static final int WHITE = 5;
		
		
		public static final int[] BLOCK_COLORS = {
			0,
			0xffff0000, 0xff0000ff, 0xff00ff00, 0xffffff00, 0xffffffff
		};

		public static final Paint blockPaint = new Paint();

		public static int getColor(int blockType)
		{
			try {
				return BLOCK_COLORS[blockType];
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new IllegalArgumentException("Invalid color index: "+blockType);
			}
		}

		public static void draw(int x, int y, int type, int blockSize, Canvas c)
		{
			blockPaint.setColor(Block.getColor(type));
			c.drawRect(x * blockSize, y * blockSize, (x + 1) * blockSize, (y + 1) * blockSize, blockPaint);
		}

	}

	class GameField
	{

		int columns, rows;
		int[][] blocks;

		public GameField(int columns, int rows)
		{
			this.columns = columns;
			this.rows = rows;
			clear();
		}

		public boolean gravityPullDown()
		{
			boolean changed = false;
			for (int i = 0; i < columns; i++) {
				for (int j = rows; j >= 0; j--) {
					if (!isEmpty(i, j) && isEmpty(i, j + 1)) {
						put(i, j + 1, get(i, j));
						put(i, j, 0);
						changed = true;
					}
				}
			}
			
			return changed;
		}


		public void clear()
		{
			blocks = new int[columns][rows];
			for (int i = 0; i < columns; i++)
			{
				for (int j = 0; j < rows; j++)
				{
					blocks[i][j] = 0;
				}
			}
		}

		public void put(int x, int y, int block)
		{
			try
			{
				blocks[x][y] = block;
			}
			catch (ArrayIndexOutOfBoundsException e)
			{}
		}

		public int get(int x, int y)
		{
			try
			{
				return blocks[x][y];
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				return -1;
			}
		}

		public boolean isEmpty(int x, int y)
		{
			return (get(x, y) == 0);
		}
		
		public boolean isOnBlock(int x, int y) {
			return (get(x, y) > 0);
		}
		
		public boolean checkMatches() {
			boolean[][] target = new boolean[columns][rows];
			boolean changed = false;
			for (int i = 0; i < columns; i++) {
				for (int j = 0; j < rows; j++) {
					int blockToCompare = get(i, j);
					if (blockToCompare > 0) {
						int conn = 0;
						conn += isSameType(blockToCompare, get(i - 1, j)) ? 1 : 0;
						conn += isSameType(blockToCompare, get(i + 1, j)) ? 1 : 0;
						conn += isSameType(blockToCompare, get(i, j - 1)) ? 1 : 0;
						conn += isSameType(blockToCompare, get(i, j + 1)) ? 1 : 0;
						if (conn >= 2)	{
							changed = true;
							target[i][j] = true;
							if (isSameType(blockToCompare, get(i - 1, j))) target[i - 1][j] = true;
							if (isSameType(blockToCompare, get(i + 1, j))) target[i + 1][j] = true;
							if (isSameType(blockToCompare, get(i, j - 1))) target[i][j - 1] = true;
							if (isSameType(blockToCompare, get(i, j + 1))) target[i][j + 1] = true;
						}
					}
				}
			}
			for (int i = 0; i < columns; i++) {
				for (int j = 0; j < rows; j++) {
					if (target[i][j]) put(i, j, Block.NONE);
				}
			}
			return changed;
		}
		
		private boolean isSameType(int b1, int b2) {
			return (b1 > 0 && b1 == b2);
		}
	}

	class FreshBlock
	{

		int x, y;
		int type;
		Random r;

		public FreshBlock()
		{
			r = new Random();
			create();
		}

		public void create()
		{
			x = r.nextInt(8);
			y = 0;
			type = r.nextInt(4) + 1;
		}

		public boolean drop(GameView.GameField field)
		{
			if (!field.isEmpty(x, y + 1)) return false;
			y++;
			return true;
		}

		public void putOnField(GameView.GameField field)
		{
			field.put(x, y, type);
		}
	}


	GameField field;
	int blockSize = 1;
	FreshBlock fb = new FreshBlock();
	int tick;

	public GameView(Context cont, AttributeSet attrs)
	{
		super(cont, attrs);
		field = new GameField(8, 13);
	}


	@Override
	public void onDraw(Canvas c)
	{
		blockSize = c.getWidth() / 8;

		for (int i = 0; i < field.columns; i++)
		{
			for (int j = 0; j < field.rows; j++)
			{
				if (!field.isEmpty(i, j))
					Block.draw(i, j, (field.get(i, j)), blockSize, c);
			}
		}


		Block.draw(fb.x, fb.y, fb.type, blockSize, c);

		if (!fb.drop(field))
		{			
			fb.putOnField(field);
			do {
				while (field.gravityPullDown()) {
					
				}
			} while (field.checkMatches());
			fb.create();
		}

		postInvalidateDelayed(20);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		fb.x = (int) (e.getX() / blockSize);
		return true;
	}


}
