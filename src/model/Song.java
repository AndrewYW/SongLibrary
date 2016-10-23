/**
 * Song library
 * Andrew Wang
 * Rumzi Tadros
 */
package model;

public class Song implements Comparable<Song>{
	private String title;
	private String artist;
	private String album;
	private String year;
	
	
	public Song(String t, String art, String alb, String y){
		this.title = t;
		this.artist = art;
		this.album = alb;
		this.year = y;
	}
	public Song(String t, String art){
		this(t, art, "", "");
	}
	public String getTitle(){
		return this.title;
	}
	public String getArtist(){
		return this.artist;
	}
	public String getAlbum(){
		return this.album;
	}
	public String getYear(){
		return this.year;
	}
	public void setAlbum(String a){
		this.album = a;
	}
	public void setYear(String y){
		this.year = y;
	}
	
	public String toString() {
		return this.title;
	}
	@Override
	public int compareTo(Song s) {
		if(this.title.trim().toUpperCase().compareTo(s.getTitle().trim().toUpperCase()) == 0){
			if(this.artist.trim().toUpperCase().compareTo(s.getArtist().trim().toUpperCase()) == 0){
				return 0;
			}
			else if(this.artist.trim().toUpperCase().compareTo(s.getArtist().trim().toUpperCase()) > 0){
				return 1;
			}
			else{
				return -1;
			}
		}
		else if(this.title.trim().toUpperCase().compareTo(s.getTitle().trim().toUpperCase()) > 0){
			return 1;
		}
		else{
			return -1;
		}
	}
}