package com.me.tasker;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MyArrayAdapter extends ArrayAdapter<String>
{

	public MyArrayAdapter(Context cont, int resource, int textViewResId, List<String> strings)
	{
		super(cont, resource, textViewResId, strings);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		/*
		 LinearLayout ll = new LinearLayout(getContext());
		 ll.setPadding(20, 20, 20, 20);

		 TextView tv = new TextView(getContext());
		 tv.setText("Item "+position+"!!!");
		 tv.setTextSize(20);
		 tv.setTextColor(0xffffffff);

		 ll.addView(tv);

		 return ll;
		 */

		View v = LayoutInflater.from(getContext()).inflate(R.layout.event_pane, null);

		((TextView) v.findViewById(R.id.text)).setText(getItem(position).toString());
		int color = (getItem(position).hashCode() % 0xffffff) | 0xff000000;

		((FrameLayout) v.findViewById(R.id.textBox)).setBackgroundColor(color);
		((TextView) v.findViewById(R.id.textBoxTextView)).setText("" + (getCount() - position));
		if (((color & 0xff00) > 0xcc00) || ((color & 0xff0000) > 0xcc0000) || ((color & 0xff) > 0xcc))
		{
			((TextView) v.findViewById(R.id.textBoxTextView)).setTextColor(0xff000000);
		}
		return v;
	}

}
