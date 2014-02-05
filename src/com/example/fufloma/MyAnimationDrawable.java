package com.example.fufloma;

import android.graphics.drawable.AnimationDrawable;
import android.os.SystemClock;

public class MyAnimationDrawable extends AnimationDrawable {

	private volatile int duration;
	private int direction;
	private int currentFrame;
	private boolean paused;

	public MyAnimationDrawable() {
		currentFrame = 0;
		paused = false;
		duration = 50;
		direction = +1;
	}

	@Override
	public void run() {
		nextFrame(false);
	}

    private void nextFrame(boolean unschedule) {
    	int next = currentFrame;
    	
    	if (!paused)
    		next += direction;
    	
        final int N = getNumberOfFrames();
        if (next >= N) {
            next = 0;
        }
        if (next < 0) {
        	next = N - 1;
        }
        
        setFrame(next, unschedule, true);
    }

    private void setFrame(int frame, boolean unschedule, boolean animate) {
        if (frame >= getNumberOfFrames()) {
            return;
        }
        currentFrame = frame;
        selectDrawable(frame);
        if (unschedule) {
            unscheduleSelf(this);
        }
        if (animate) {
            currentFrame = frame;
            scheduleSelf(this, SystemClock.uptimeMillis() + duration);
        }
    }
    
    public int getDuration() {
    	return duration;
    }

    public int getDirection() {
    	return direction;
    }
    
	public void setDuration(int duration) {
		if (duration < 0)
		{
			duration *= -1;
			direction = -1;
		} else
			direction = +1;
		
		this.duration = duration;

		unscheduleSelf(this);
		selectDrawable(currentFrame);
		scheduleSelf(this, SystemClock.uptimeMillis() + duration);
	}
	
	public void setCurrentFrame(int frame) {
		currentFrame = frame;
		selectDrawable(currentFrame);
	}
	
	public void prevFrame()
	{
    	int next = currentFrame - 1;
    	
        final int N = getNumberOfFrames();
        if (next < 0) {
        	next = N - 1;
        }
        
        setCurrentFrame(next);
	}
	
	public void nextFrame()
	{
    	int next = currentFrame + 1;
    	
        final int N = getNumberOfFrames();
        if (next >= N) {
            next = 0;
        }
        
        setCurrentFrame(next);
	}

	public void pause() {
		paused = true;
	}

	public void resume() {
		paused = false;
	}
}