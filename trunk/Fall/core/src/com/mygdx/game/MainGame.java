package com.mygdx.game;


import helpers.AssetLoader;
import handlers.GameScreenManager;
import handlers.MyInput;
import handlers.MyInputProcessor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGame extends Game {
	public static final String TITLE = "Game";
	public static final int V_WIDTH = 320;
	public static final int V_HEIGHT = 480;
	public static final int SCALE = 2;
	
	public static final float STEP = 1 / 60f;
	private float accum;
	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	
	private GameScreenManager gsm;
	
	

	
	@Override
	public void create () {
		//Gdx.input.setInputProcessor(new MyInputProcessor());
		
		AssetLoader.load();
		
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		
		
		gsm = new GameScreenManager(this);
		
		
		gsm.set();
		
		


	}

	@Override
	public void render () 
	{
			
			gsm.update(STEP);
			gsm.render();
			MyInput.update();
	}
	
	public void dispose()
	{
		super.dispose();
		AssetLoader.dispose();
		sb.dispose();
		
	}
	public void pause(){}
	public void resume(){}
	
	public SpriteBatch getSpriteBatch(){return sb;}
	public OrthographicCamera getCamera(){ return cam; }
	public OrthographicCamera getHudCam(){ return hudCam; }
	
}
