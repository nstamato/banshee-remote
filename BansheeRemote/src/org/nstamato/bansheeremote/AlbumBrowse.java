package org.nstamato.bansheeremote;


import java.io.File;
import java.util.ArrayList;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AlbumBrowse extends ListActivity{
	ArrayList<String> albumList;
    ArrayAdapter<String> albumListAdapter; 
    final String filenameDB = "banshee.db";
    SQLiteDatabase bansheeDB;
    public ImageView icon;
    public TextView title;
    public String titleText="";
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		setResult(resultCode,data);
		if(resultCode==RESULT_OK)
			finish();
	}
    
	public void onCreate(Bundle savedInstanceState) {
		//this.setFastScrollEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse);
        Bundle extras = getIntent().getExtras();
        this.titleText = extras.getString("Artist");
        this.icon = (ImageView)this.findViewById(R.id.icon);
        this.title = (TextView)this.findViewById(R.id.title);
        this.title.setText(titleText);
        this.icon.setImageResource(R.drawable.album);
        this.albumList = new ArrayList<String>();
        this.albumListAdapter = new ArrayAdapter<String>(this,R.layout.list_item,albumList);
        //this.header = new ArrayList<String>();
        //this.header.add("title");
        //this.headerAdapter = new ArrayAdapter<String>(this,R.layout.list_item,header);
        //setListAdapter(headerAdapter);
        setListAdapter(albumListAdapter);
        File db = null;
		try{
			//db = getFileStreamPath(filenameDB);
			db = Environment.getExternalStorageDirectory();
			String path = db.getAbsolutePath()+'/'+filenameDB;
			bansheeDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
			String query = "SELECT Title FROM CoreAlbums";
			String[] params = null;
			if(!titleText.equals("Albums")){
				query+=" WHERE ArtistName=?";
				params = new String[]{titleText};
			}
			query+=" ORDER BY Title";
			
			Cursor c = bansheeDB.rawQuery(query,params);
			//int columnCount = c.getColumnCount();
			int name = c.getColumnIndex("Title");
			//int count = c.getCount();
			c.moveToFirst();
			while(c.isAfterLast()==false){
				String artist = c.getString(name);
				if(artist!=null)
					albumList.add(c.getString(name));
				c.moveToNext();
				//artistList.add("hello");
			}
			c.close();
			//artistList.add("hello");
			//Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
			//String result = c.getString(name);
		}catch(SQLiteException e){
			//Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
			Toast.makeText(this,"Something went wrong. Make sure the banshee database file is on your sd-card.",Toast.LENGTH_LONG).show();
		}
		catch(Exception e){
			//Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
			Toast.makeText(this,"Something went wrong. Make sure the banshee database file is on your sd-card.",Toast.LENGTH_LONG).show();
		}
		ListView l = getListView();
		l.setTextFilterEnabled(true);
		l.setFastScrollEnabled(true);
    }
	
	public void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	String selected = this.albumListAdapter.getItem(position);
    	String query = "SELECT AlbumID FROM CoreAlbums WHERE Title=?";
    	String[] params = new String[]{selected};
    	if(!titleText.equals("Albums")){
			query+=" AND ArtistName=?";
			params = new String[]{selected,titleText};
		}
			
		Cursor c = bansheeDB.rawQuery(query,params);
		//int columnCount = c.getColumnCount();
		int albumIDColumn = c.getColumnIndex("AlbumID");
		//int count = c.getCount();
		c.moveToFirst();
		int albumID = c.getInt(albumIDColumn);
    	//Toast.makeText(this,selected,Toast.LENGTH_SHORT).show();
    	Intent i = new Intent(this,SongBrowse.class);
    	i.putExtra("Album",selected);
    	i.putExtra("AlbumID",albumID);
    	startActivityForResult(i,2);
	}

}
