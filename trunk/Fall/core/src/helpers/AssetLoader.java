package helpers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {
	public static Preferences prefs;
	
	private HashMap<String, Texture> textures;
	
	public static Texture player, cliffWalls, ledgeA, ledgeB, logoTexture, bg, bg2, mbg, lampTexture, menu;
	
	//middle ledge textures
	public static Texture ledgeO, ledgeP, ledgeBL, ledgeR, ledgeOR, ledgePI, ledgeG, ledgeLB;
	
	//Background textures
	public static TextureRegion waterFallBG, waterFallBG2, middleBGLeft, middleBGRight;
	
	//public static TextureRegion playerLeft, playerMid, playerRight;
	public static TextureRegion logo, cliffJumper1, cliffJumper2, cliffJumper3, cliffJumper4, 
								wallLeft, wallRight, ledgeLeft, ledgeRight, lamp, menuGO;
	
	//middle ledege textures
	public static TextureRegion ledgePurple, ledgeOrange, ledgeBlue, ledgeGreen, ledgeRed, ledgeOrangeRed, ledgePink,
								ledgeLightBlue;
	
	public static Music bgm;
	
	public static Music caveIn;
	
	public static Sound hit;
	
	public AssetLoader(){
		textures = new HashMap <String, Texture>();
	}
	
	public static void load()
	{
		//Scores
		// Create (or retrieve existing) preferences file
		prefs = Gdx.app.getPreferences("Fall");
		
		// Provide default high score of 0
		if (!prefs.contains("highScore")) {
		    prefs.putInteger("highScore", 0);
		}
		
		//
	
		menu = new Texture(Gdx.files.internal("data/Menubox.png"));
		logoTexture = new Texture(Gdx.files.internal("data/CrypticFoxLogoFinal.png"));
		player = new Texture(Gdx.files.internal("data/basejumperanimatewhite.png"));
		cliffWalls = new Texture(Gdx.files.internal("data/cliff-wallsblue.png"));
		
		//Middle Ledge Sprites
		ledgeOR = new Texture(Gdx.files.internal("data/platformB2.png"));
		ledgePI = new Texture(Gdx.files.internal("data/platformB3.png"));
		ledgeG = new Texture(Gdx.files.internal("data/platformB4.png"));
		ledgeLB = new Texture(Gdx.files.internal("data/platformB6.png"));
		ledgeR = new Texture(Gdx.files.internal("data/platformB7.png"));
		ledgeO = new Texture(Gdx.files.internal("data/platformBcrystal.png"));
		ledgeP = new Texture(Gdx.files.internal("data/platformB8.png"));
		ledgeBL = new Texture(Gdx.files.internal("data/platformB5.png"));
	
		ledgeA = new Texture(Gdx.files.internal("data/ledgeA.png"));
		ledgeB = new Texture(Gdx.files.internal("data/ledgeB.png"));
		
		ledgeP = new Texture(Gdx.files.internal("data/platformB8.png"));
		
		lampTexture = new Texture(Gdx.files.internal("data/lamp2.png"));
		
		bg = new Texture(Gdx.files.internal("data/waterfallbackdropA1.png"));
		bg2 = new Texture(Gdx.files.internal("data/waterfallbackdropA2.png"));
		mbg = new Texture(Gdx.files.internal("data/cliff-walls-midgroundNEW.png"));
		
		logo = new TextureRegion(logoTexture);
		
		cliffJumper1 = new TextureRegion(player, 0, 0, 30 ,30);
		
		//Background textures
		waterFallBG = new TextureRegion(bg);
		waterFallBG2 = new TextureRegion(bg2);
		middleBGLeft = new TextureRegion(mbg, 0, 0, 155, 480);
		middleBGRight = new TextureRegion(mbg, 160, 0, 165, 480);
		
		//Midde ledge textures
		ledgePurple = new TextureRegion(ledgeP);
		ledgeOrange = new TextureRegion(ledgeO);
		ledgeBlue = new TextureRegion(ledgeBL);
		ledgeGreen = new TextureRegion(ledgeG);
		ledgeRed = new TextureRegion(ledgeR);
		ledgeOrangeRed = new TextureRegion(ledgeOR);
		ledgePink = new TextureRegion(ledgePI);
		ledgeLightBlue = new TextureRegion(ledgeLB);
		
		
		cliffJumper2 = new TextureRegion(player, 31, 0, 30, 30);
		cliffJumper3 = new TextureRegion(player, 68, 0, 30, 30);
		cliffJumper4 = new TextureRegion(player, 103, 0, 30, 30);
		
		wallLeft = new TextureRegion(cliffWalls, 0, 0,  120, 480);
		wallRight = new TextureRegion(cliffWalls, 200, 0, 120, 480);
		
		ledgeLeft = new TextureRegion(ledgeA);
		ledgeRight = new TextureRegion(ledgeB);
		
		lamp = new TextureRegion(lampTexture);
		
		bgm = Gdx.audio.newMusic(Gdx.files.internal("data/DST-DreamInGreen (mp3cut.net).mp3"));
		caveIn = Gdx.audio.newMusic(Gdx.files.internal("data/caveIn.mp3"));
		hit = Gdx.audio.newSound(Gdx.files.internal("data/hit.mp3"));
		
		/*
		playerLeft = new TextureRegion(texture, 4, 20, 43, 47);
		playerMid = new TextureRegion(texture, 62, 20, 44, 47);
		playerRight = new TextureRegion(texture, 119, 20, 44, 47);
		
		playerLeft.flip(false, true);
		playerMid.flip(false, true);
		playerRight.flip(false, true);*/
		
	}
	
	public void loadTexture(String path, String key)
	{
		Texture tex = new Texture(Gdx.files.internal(path));
		textures.put(key,tex);
	}
	
	public Texture getTexture(String key)
	{
		return textures.get(key);
	}
	
	public void disposeTexture(String key)
	{
		Texture tex = textures.get(key);
		if(tex != null) tex.dispose();
		
		
	}
	
	public static void dispose()
	{
		player.dispose();
		cliffWalls.dispose();
		caveIn.dispose();
		hit.dispose();
		ledgeO.dispose();
		ledgeP.dispose();
		ledgeBL.dispose();
		ledgeR.dispose();
		ledgeOR.dispose();
		ledgePI.dispose();
		ledgeG.dispose();
		ledgeLB.dispose();
		ledgeA.dispose();
		ledgeB.dispose();
		lampTexture.dispose();
		logoTexture.dispose();
		bg.dispose();
		bg2.dispose();
		mbg.dispose();
		
		
	}
	
	// Receives an integer and maps it to the String highScore in prefs
	public static void setHighScore(int val) {
	    prefs.putInteger("highScore", val);
	    prefs.flush();
	}
	
	// Retrieves the current high score
	public static int getHighScore() {
	    return prefs.getInteger("highScore");
	}

}
