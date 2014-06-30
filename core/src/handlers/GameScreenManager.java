package handlers;

import java.util.Stack;

import Screens.AbstractScreen;
import Screens.GameScreen;


import com.mygdx.game.MainGame;





public class GameScreenManager {
	
	private MainGame game;
	
	private Stack<AbstractScreen> gameScreens;
	
	public static final int playScreen = 101;

	public GameScreenManager(MainGame game) {
		this.game = game;
		gameScreens = new Stack<AbstractScreen>();
		pushScreen(playScreen);
	}
	
	public MainGame game(){return game;}
	
	public void update(float dt){
		if(gameScreens.peek() instanceof GameScreen){
			gameScreens.peek().update(dt);
		}
	}
	
	public void render(){
		gameScreens.peek().render();
	}
	
	private AbstractScreen getScreen(int screen){
		if(screen == playScreen) return new GameScreen(this);
		return null;
	}
	
	public void setScreen(int screen){
		popScreen();
		pushScreen(screen);
	}
	
	public void popScreen(){
		
			AbstractScreen g = gameScreens.pop();
			g.dispose();
		
		
	}
	
	public void pushScreen(int screen){
		gameScreens.push(getScreen(screen));
	}
	
	
	
	
	
	

}
