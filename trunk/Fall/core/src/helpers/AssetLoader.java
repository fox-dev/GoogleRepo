package helpers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {
	private HashMap<String, Texture> textures;
	
	public static Texture player, cliffWalls, ledgeA, ledgeB, ledge;
	
	//public static TextureRegion playerLeft, playerMid, playerRight;
	public static TextureRegion cliffJumper1, cliffJumper2, cliffJumper3, cliffJumper4, 
								wallLeft, wallRight, ledgeLeft, ledgeRight, ledgeMiddle;
	
	public static Music bgm;
	
	public static Music caveIn;
	
	public static Sound hit;
	
	public AssetLoader(){
		textures = new HashMap <String, Texture>();
	}
	
	public static void load()
	{
		player = new Texture(Gdx.files.internal("data/basejumperanimateRED.png"));
		cliffWalls = new Texture(Gdx.files.internal("data/cliff-wallsblue.png"));
		ledge = new Texture(Gdx.files.internal("data/platform.png"));
		ledgeA = new Texture(Gdx.files.internal("data/ledgeA.png"));
		ledgeB = new Texture(Gdx.files.internal("data/ledgeB.png"));
		
		cliffJumper1 = new TextureRegion(player, 0, 0, 30 ,30);
		cliffJumper2 = new TextureRegion(player, 31, 0, 30, 30);
		cliffJumper3 = new TextureRegion(player, 68, 0, 30, 30);
		cliffJumper4 = new TextureRegion(player, 103, 0, 30, 30);
		
		wallLeft = new TextureRegion(cliffWalls, 0, 0,  120, 480);
		wallRight = new TextureRegion(cliffWalls, 200, 0, 120, 480);
		
		ledgeLeft = new TextureRegion(ledgeA);
		ledgeRight = new TextureRegion(ledgeB);
		ledgeMiddle = new TextureRegion(ledge);
		
		bgm = Gdx.audio.newMusic(Gdx.files.internal("data/Fall.wav"));
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
		
		
	}

}
