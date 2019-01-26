package com.me.tasker;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.content.*;

public class MainActivity extends Activity
{
	
	ListView lv;
	
	MyArrayAdapter adapter;
	ArrayList<String> listOfString;
	
	List<String> items = Arrays.<String> asList("Cat", "Dog", "Monkey", "Tiger", "Lion", "Chicken", "Duck", "Goose",
		"Giraffe", "Fish", "Iguana", "Elephant", "Fox", "Ant", "Pig", "Bird", "Snail", "Snake", "Butterfly",
		"Donkey", "Piranha", "Penguin", "Rabbit", "Wolf", "Raccoon", "Crocodile", "CatDog™", "Pokemon™", "Flea");
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		lv = (ListView) findViewById(R.id.list);
		
		listOfString = new ArrayList<String>();
	
		adapter = new MyArrayAdapter(this, R.layout.event_pane,  R.id.text, listOfString);
		Button b = new Button(this);
		b.setText("Clear all");
		b.setGravity(Gravity.CENTER);
		
		b.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					listOfString.clear();
					adapter.notifyDataSetChanged();
				}

			
		});
		
		lv.addFooterView(b);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					Toast.makeText(MainActivity.this, "Selected position "+p3, Toast.LENGTH_SHORT).show();
				}

		});
		
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					/*
					new AlertDialog.Builder(MainActivity.this)
						.setMessage("Long pressed "+p3)
						.setPositiveButton("OK", null)
						.create().show();
					*/
					
					listOfString.remove(p3);
					adapter.notifyDataSetChanged();
					return true;
				}

			
		});
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId()) {
			case R.id.menu_add_new:
				final EditText e = new EditText(this);
				
				new AlertDialog.Builder(this)
					.setMessage("Enter a new animal name")
					.setView(e)
					.setPositiveButton(android.R.string.ok, null)
					.create().show();
				break;
					
			case R.id.menu_add_event:
				listOfString.add(0, items.get((int) Math.floor(Math.random() * items.size())));
				adapter.notifyDataSetChanged();
				lv.smoothScrollToPosition(0);
				break;
		}
		return true;
	}
	
}
