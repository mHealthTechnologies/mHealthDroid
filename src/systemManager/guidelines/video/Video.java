package systemManager.guidelines.video;

import android.widget.VideoView;

public class Video {

	VideoView videoHolder;
			
	/**
     * Constructor. Create a new Video object
     */
	public Video(VideoView videoView){
		
		videoHolder = videoView;
	}
	
	/**
     * Method to set the video path to be reproduced 
     */
	public void setVideoPath(String path){
		
		videoHolder.setVideoPath(path);
	}
	
	/**
     * Method to reproduce video
     */
	public void start(){
		
		videoHolder.start();
	}
	
	/**
     * Method to resume video
     */
	public void resume(){
		
		if((!videoHolder.isActivated()) && (!videoHolder.isPlaying()))
			videoHolder.resume();
	}
	
	/**
     * Method to pause video
     */
	public void pause(){
		
		if(videoHolder.isPlaying())
			videoHolder.start();
	}
}
