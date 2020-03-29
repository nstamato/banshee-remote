package org.nstamato.bansheeremote;

import android.graphics.Bitmap;

public class BansheeInstance {
	public String status;
	public Bitmap cover;
	public String track, artist, album;
	public int iseekposition, iseektotal;
	public boolean isCover;
	public String ip;
	public int port;
	
	public BansheeInstance(String status, Bitmap cover, String track, String artist, String album, int position, int total, boolean isCover, String ip, int port){
		this.status=status;
		this.cover = cover;
		this.track=track;
		this.artist=artist;
		this.album=album;
		this.iseekposition=position;
		this.iseektotal=total;
		this.ip=ip;
		this.port=port;
	}
}
