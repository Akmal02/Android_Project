package com.test.doodle;

import android.content.*;
import android.graphics.*;
import android.net.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.test.doodle.*;
import java.io.*;
import java.util.*;
import android.net.Uri;
import android.provider.MediaStore.Images;

public class DoodleView extends View
{
    private static final float TOUCH_TOLERANCE = 0.0f;

	private Bitmap bitmap;
	private Canvas bitmapCanvas;
	private Paint paintScreen;
	private Paint paintLine;
	private HashMap<Integer, Path>pathMap;
	private HashMap<Integer, Point>prevPointMap;

	public DoodleView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		paintScreen = new Paint();
		paintLine = new Paint();
		paintLine.setAntiAlias(true);
		paintLine.setColor(Color.BLACK);

		paintLine.setStyle(Paint.Style.STROKE);
		paintLine.setStrokeWidth(5);
		paintLine.setStrokeCap(Paint.Cap.ROUND);

		pathMap = new HashMap<Integer, Path>();
		prevPointMap = new HashMap<Integer, Point>();

	}

	public void onSizeChanged(int w, int h, int oldW, int oldH)
	{
		bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);
		bitmap.eraseColor(Color.WHITE);

	}

	public void clear()
	{
		pathMap.clear();
		prevPointMap.clear();
		bitmap.eraseColor(Color.WHITE);
		invalidate();
	}

	public void setDrawingColor(int color)
	{
		paintLine.setColor(color);
	}

	public int getDrawingColor()
	{
		return paintLine.getColor();
	}

	public void setLineWidth(int width)
	{
		paintLine.setStrokeWidth(width);
	}

	public int getLineWidth()
	{
		return (int) paintLine.getStrokeWidth();
	}

	protected void onDraw(Canvas canvas)
	{
		canvas.drawBitmap(bitmap, 0, 0, paintScreen);

		for (Integer key : pathMap.keySet())
		    canvas.drawPath(pathMap.get(key), paintLine);

	}

	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getActionMasked();
		int actionIndex = event.getActionIndex();

		if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN)
		{
			touchStarted(event.getX(actionIndex), event.getY(actionIndex), event.getPointerId(actionIndex));
		}
		else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP)
		{
			touchEnded(event.getPointerId(actionIndex));
		}
		else
		{
			touchMoved(event);
		}

		invalidate();
		return true;

	}

	private void touchStarted(float x, float y, int lineID)
	{

		Path path;
		Point point;
		if (pathMap.containsKey(lineID))
		{
			path = pathMap.get(lineID);
			path.reset();
			point = prevPointMap.get(lineID);

		}
		else
		{
			path = new Path();
			pathMap.put(lineID, path);
			point = new Point();
			prevPointMap.put(lineID, point);

		}
		path.moveTo(x, y);
		point.x = (int) x;
		point.y = (int) y;

	}

	private void touchMoved(MotionEvent event)
	{
		for (int i=0; i < event.getPointerCount(); i++)
		{
			int pointerID = event.getPointerId(i);
			int pointerIndex = event.findPointerIndex(pointerID);

			if (pathMap.containsKey(pointerID))
			{
				float newX = event.getX(pointerIndex);
				float newY = event.getY(pointerIndex);
				Path path = pathMap.get(pointerID);
				Point point = prevPointMap.get(pointerID);

				float deltaX = Math.abs(newX - point.x);
				float deltaY = Math.abs(newY - point.y);

				if (deltaX >= TOUCH_TOLERANCE || deltaY >= TOUCH_TOLERANCE)
				{
					path.quadTo(point.x, point.y, (newX + point.x) / 2, (newY + point.y) / 2);

					point.x = (int) newX;
					point.y = (int) newY;
				}
			}
		}
	}
	
	private void touchEnded(int lineID)
	{
		Path path = pathMap.get(lineID);
		bitmapCanvas.drawPath(path, paintLine);
		path.reset();
	}


	public void saveImage()
	{
		String fileName = "Doodle" + System.currentTimeMillis();
		
		ContentValues values = new ContentValues();
		values.put(Images.Media.TITLE, fileName);
		values.put(Images.Media.DATE_ADDED, System.currentTimeMillis());
		values.put(Images.Media.MIME_TYPE, "image/jpg");
		
		Uri uri = getContext().getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
		
		try
		{
			OutputStream outStream = getContext().getContentResolver().openOutputStream(uri);
			
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			
			outStream.flush();
			outStream.close();
			
			Toast message = Toast.makeText(getContext(), R.string.message_saved, Toast.LENGTH_SHORT);
			message.setGravity(Gravity.CENTER, message.getXOffset()/2, message.getYOffset()/2);
			message.show();
			
		}
		catch (IOException ex)
		{
			Toast message = Toast.makeText(getContext(), R.string.message_error_saving, Toast.LENGTH_SHORT);
			message.setGravity(Gravity.CENTER, message.getXOffset()/2, message.getYOffset()/2);
			message.show();
		}
	}
}
