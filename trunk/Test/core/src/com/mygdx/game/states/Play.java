package com.mygdx.game.states;

import static handlers.B2DVars.PPM;

import java.util.Iterator;
import java.util.Random;

import handlers.B2DVars;
import handlers.GameStateManager;






import handlers.MyContactListener;
import handlers.MyInput;
import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.mygdx.main.Game;



public class Play extends GameState{
	private boolean debug = true;
	float x = 0;
	float y = 0;
	long lastSprite = 0;
	long lastSprite2 = 0;
	
	int lights = 0;
	
	
	//private BitmapFont font = new BitmapFont();
	
	private Array<Body> obstacleList = new Array<Body>();
	private Array<ConeLight> lightList = new Array<ConeLight>();
	private Iterator<Body> iterator;
	private Iterator<ConeLight> iterator2;
	
	
	private World world;
	private Box2DDebugRenderer b2dr;
	
	private OrthographicCamera b2dCam;
	
	RayHandler handler;
	
	private Body playerBody;
	
	private Body leftWall;
	private Body rightWall;
	
	private MyContactListener cl;
	
	public Play(GameStateManager gsm){
		super(gsm);
		
		world = new World(new Vector2(0, -9.81f), true);
		
		cl = new MyContactListener();
		world.setContactListener(cl);
		
		b2dr = new Box2DDebugRenderer();
		
		
		
		
		//static body - don't move, unaffected by forces (ground)
		//kinematic body - don't get affected by forces (moving platform)
		//dynamic body - always get affected by forces (player)
		
		//create platform
		BodyDef bdef = new BodyDef(); 
		bdef.position.set(160/PPM,120/PPM);
		bdef.type = BodyType.StaticBody;
		Body body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(50/PPM, 5/PPM); //half width half height, so 100, 10
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_GROUND;
		//fdef.filter.maskBits = B2DVars.BIT_BOX | B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		body.createFixture(fdef).setUserData("Ground");
		
		
		
		//create player
		bdef.position.set(160/PPM, 300/PPM);
		bdef.type = BodyType.DynamicBody;
		playerBody = world.createBody(bdef);
		
		shape.setAsBox(5/PPM, 5/PPM);
		fdef.shape = shape;
		//fdef.restitution = 1f;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;
		playerBody.createFixture(fdef).setUserData("Player");
		
		
		
		/*
		//create ball
		bdef.position.set(153/PPM, 220/PPM);
		body = world.createBody(bdef);
		
		
		CircleShape cshape = new CircleShape();
		cshape.setRadius(5/PPM);
		fdef.shape = cshape;
		//fdef.restitution = 0.2f;
		fdef.filter.categoryBits = B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;
		body.createFixture(fdef).setUserData("Ball");
		*/
		
		
		//create foot sensor
		shape.setAsBox(2 / PPM, 2 / PPM, new Vector2(0, -5/PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;
		fdef.isSensor = true;
		playerBody.createFixture(fdef).setUserData("foot");
		
		
		
		//Left wall
		bdef = new BodyDef(); 
		bdef.position.set(0/PPM,0+200/PPM);
		bdef.type = BodyType.KinematicBody;
		leftWall = world.createBody(bdef);
		
		shape = new PolygonShape();
		shape.setAsBox(2/PPM, (Game.V_HEIGHT+50/2)/PPM); //half width half height, so 100, 10
		
		fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_GROUND;
		//fdef.filter.maskBits = B2DVars.BIT_BOX | B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		leftWall.createFixture(fdef).setUserData("LeftWall");

		//Right wall
		bdef = new BodyDef(); 
		bdef.position.set((Game.V_WIDTH)/PPM,0+200/PPM);
		bdef.type = BodyType.KinematicBody;
		rightWall = world.createBody(bdef);
		
		shape = new PolygonShape();
		shape.setAsBox(2/PPM, (Game.V_HEIGHT+50/2)/PPM); //half width half height, so 100, 10
		
		fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_GROUND;
		//fdef.filter.maskBits = B2DVars.BIT_BOX | B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		rightWall.createFixture(fdef).setUserData("RightWall");
		
		
		
		//set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);
		
		
		
		handler = new RayHandler(world);
		handler.setAmbientLight(0.0f, 0.0f, 0.0f,1.0f);
	
		//ConeLight t;
		//t = new ConeLight(handler, 10, Color.CYAN, 100/PPM, body.getPosition().x, body.getPosition().y,270,35);
		
		
		
		
		//new PointLight(handler, 50, Color.CYAN, 500/PPM, playerBody.getPosition().x, playerBody.getPosition().y);
		
		
	
		
	}
		
	
	public void handleInput(){
		if(MyInput.isPressed(MyInput.BUTTON1)){
			//System.out.println("pressed z");
			//if(cl.isPlayerOnGround()){
				playerBody.applyForceToCenter(0,200,true);
			 	
		
	
			//}
		}
		if(MyInput.isDown(MyInput.BUTTON1)){
			Vector2 vel = playerBody.getLinearVelocity();
		 	vel.y = -1f;
			playerBody.setLinearVelocity(vel);
		}
		
		if(MyInput.isDown(MyInput.BUTTON2)){
			//System.out.println("hold x");
			Vector2 vel = playerBody.getLinearVelocity();
			vel.x = -1f;
			playerBody.setLinearVelocity(vel);
			
		}
		if(MyInput.isDown(MyInput.BUTTON3)){
			//System.out.println("hold c");
			Vector2 vel = playerBody.getLinearVelocity();
			vel.x = 1f;
			playerBody.setLinearVelocity(vel);
			
		}
	}
	
	public static int randInt(int min, int max) {

	    // Usually this can be a field rather than a method variable
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    return rand.nextInt((max - min) + 1) + min;
	}
	
	

	
	public void addObstacles(){
		BodyDef def = new BodyDef(); 
		//def.position.set(randInt(0,(320-(Game.V_WIDTH/5)-3))/PPM,(cam.position.y - (Game.V_HEIGHT*2)/PPM));
		//def.position.set(((Game.V_WIDTH/5)+3)/PPM,(cam.position.y - (Game.V_HEIGHT*2)/PPM));
		def.position.set(randInt(((Game.V_WIDTH/5)+3),(320-(Game.V_WIDTH/5)-3))/PPM,(cam.position.y - (Game.V_HEIGHT*2)/PPM));

		def.type = BodyType.StaticBody;
		Body body = world.createBody(def);
		body = world.createBody(def);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((Game.V_WIDTH/5)/PPM, 5/PPM); //half width half height, so 100, 10
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 0.4f;
		fdef.filter.categoryBits = B2DVars.BIT_GROUND;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		body.createFixture(fdef).setUserData("Ground");
	
		
		
	
		ConeLight t;
		t = new ConeLight(handler, 10, Color.GRAY,1000/PPM, body.getPosition().x, body.getPosition().y + 120/PPM, 270, 36);
		//Light.setContactFilter(B2DVars.BIT_BALL, (short) 0, B2DVars.BIT_BALL);
		
		
		
		
		
		
		
		
		lightList.add(t);
		obstacleList.add(body);
		
		
		
		//System.out.println("Y object: " + body.getPosition().y);
		
		
		
		
		
	}
	
	public void update(float dt){
		//System.out.println("Wally: "+ leftWall.getPosition().y);
		//System.out.println("Playery: "+ playerBody.getPosition().y);
		//System.out.println("Difference: " +  (leftWall.getPosition().y - playerBody.getPosition().y));
		iterator = obstacleList.iterator();
		iterator2 = lightList.iterator();
		while(iterator.hasNext()){
			Body o = iterator.next();
			ConeLight s = iterator2.next();
			if(o.getPosition().y > b2dCam.position.y + Game.V_HEIGHT/PPM){
				world.destroyBody(o);
				s.setActive(false);
				iterator.remove();
				iterator2.remove();
				handler.lightList.removeValue(s, true);
				lights--;
				
			}
			
			
			
			
		}
		
		
		
		
		
		long now = System.currentTimeMillis(); // or some other function to get the current time
		long now2 = System.currentTimeMillis();
		
		  if (now - lastSprite > 1000) {
			  
			  
			x = playerBody.getPosition().y;


			 
			///System.out.println("x: " + x);
		    lastSprite = now;
		  }
		  
		  if (now - lastSprite2 > 2000) {
				
			 y = playerBody.getPosition().y;
			  
			//System.out.println("y: " + y);
		    lastSprite2 = now2;
		  }
		  
		  if(Math.abs(x-y)/PPM > 3/PPM && (x != 0f) && (y != 0f)){
			  addObstacles();
			  
			  //lastSprite2 = now2;
			 // System.out.println("Abs: " + Math.abs(x-y));
			  x = 0f;
			  y = 0f;
		  }
		  
		handleInput();
		if(Math.abs(playerBody.getLinearVelocity().y) > 10f){
		//playerBody.applyForceToCenter(0,200,true);
			Vector2 vel = playerBody.getLinearVelocity();
			vel.y = -10f;
			playerBody.setLinearVelocity(vel);
			
		}
		handler.render();
		System.out.println("Vp: " + playerBody.getLinearVelocity().y);
		
		leftWall.setTransform(0, playerBody.getPosition().y, 0);
		leftWall.setLinearVelocity(playerBody.getLinearVelocity());
		rightWall.setTransform((Game.V_WIDTH)/PPM, playerBody.getPosition().y, 0);
		rightWall.setLinearVelocity(playerBody.getLinearVelocity());
		
		
		//System.out.println("Size: " + handler.lightList.size);
		
		world.step(dt, 1, 1);
		
		
		

	
	}
	
	public void render(){
		//clear screens
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		/*
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		font.draw(sb, "play state", 100, 100);
		sb.end();
		*/
		
		cam.position.set(playerBody.getPosition().x,
				playerBody.getPosition().y
				,
				0);
		cam.update();
		
		b2dCam.position.set(
				playerBody.getPosition().x,playerBody.getPosition().y - 100/PPM
				,
				0
			);
		b2dCam.update();
			
		sb.setProjectionMatrix(cam.combined);
		sb.setProjectionMatrix(hudCam.combined);
		
		handler.setCombinedMatrix(b2dCam.combined);
		
		handler.updateAndRender();
			
		
		//System.out.println("x: " + cam.position.x/PPM);
		//System.out.println("y: " + cam.position.y*100);
		//System.out.println(playerBody.getLinearVelocity().y);
		
		
		
		
		// draw box2d
		if(debug) {
			b2dr.render(world, b2dCam.combined);
		}
		
	}
	
	public void dispose(){
		handler.dispose();
	}
		
}
