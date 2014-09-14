package objects;

import com.badlogic.gdx.physics.box2d.Body;

import helpers.B2DVars;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Wall {
	
	protected Body body;
	protected float width, height, yPos;
	protected TextureRegion wallTexture;
	
	public Wall(Body body){
		this.body = body;
		
	}
	
	public void render(SpriteBatch sb)
	{
		sb.begin();
		sb.draw(wallTexture, 
				body.getPosition().x * B2DVars.PPM - width / 2,
				yPos * B2DVars.PPM
				);
		sb.end();
		
		//System.out.println(width + " " + height);
	}
	
	public void setPosition(float y)
	{
		yPos = y;
	}
	
	public Body getBody(){return body;}
	public Vector2 getPosition(){return body.getPosition();}

	public float getWidth(){return width;}
	public float getHeight(){return height;}
	public float getYPosition(){return yPos;}
	
}