package Screens;



import handlers.GameScreenManager;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MainGame;


public abstract class AbstractScreen implements Screen{
	protected GameScreenManager gsm;
	protected MainGame game;
	
	protected SpriteBatch sb;
	protected OrthographicCamera cam; //player
	
	protected AbstractScreen(GameScreenManager gsm){
		this.gsm = gsm;
		game = gsm.game();
		sb = game.getSpriteBatch();
		cam = game.getCamera();
		
	}
	
	public abstract void handleInput();
	public abstract void update(float dt);
	public abstract void render();
	public abstract void dispose();

}
