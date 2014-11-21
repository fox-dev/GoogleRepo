package Screens;

import static helpers.B2DVars.PPM;
import static helpers.B2DVars.CL;

import java.util.Random;

import objects.Lamp;
import objects.LedgeLeft;
import objects.LedgeMiddle;
import objects.LedgeRight;
import objects.LeftWall;
import objects.Player;
import objects.RightWall;
import objects.StaticSprite;
import objects.Wall;
import Lights.ConeLight;
import Lights.PointLight;
import Lights.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MainGame;

import handlers.AnimatedBackground;
import handlers.Background;
import handlers.GameScreenManager;
import handlers.Middleground;
import handlers.MyInput;
import helpers.AssetLoader;
import helpers.B2DVars;

public class Menu extends AbstractScreen {

	// ButtonStuff
	Skin skin;
	Stage stage;
	TextButton textButton;
	Table menuTable;
	Viewport v;
	//

	public static final float STEP = 1 / 60f;
	public float runTime = 0f;

	Boolean b = false;
	Boolean c = false;

	public boolean state, lastState;

	float ASPECT_RATIO = (float) MainGame.V_WIDTH / (float) MainGame.V_HEIGHT;
	private Rectangle viewport;

	private World world;
	private Box2DDebugRenderer b2dr;

	private Player player;

	private OrthographicCamera b2dCam;

	BitmapFont font;

	private float fadeIn = 0f;
	private boolean fadeOut = false;

	// BGSTUFF
	private Background[] backgrounds;
	private Middleground middleBgLeft, mLRepeat, middleBgRight, mRRepeat;
	// WALLSTUFF
	private LeftWall leftWall, lWallRepeat;
	private RightWall rightWall, rWallRepeat;
	private Wall[] foreground;

	// LIGHTSTUFF
	int lights = 0;
	int grow = 49;
	private RayHandler handler;
	//ConeLight td;

	// LEDGESTUFF
	float depth = 0, score = 0, lastDepth = 0, lastStop = 0, multi = 1;
	float x = 0, y = 0, x2 = 0, y2 = 0;
	long lastSprite = 0;
	long lastSprite2 = 0;
	long nextSprite = 2, nextSprite2 = 3;

	// ARRAYSTUFF
	private DelayedRemovalArray<StaticSprite> ledgeList = new DelayedRemovalArray<StaticSprite>();
	private DelayedRemovalArray<ConeLight> lightList = new DelayedRemovalArray<ConeLight>();
	private DelayedRemovalArray<PointLight> lightListP = new DelayedRemovalArray<PointLight>();

	// CAMSTUFF
	private float camZoom = 0.90f;

	// WALLSTUFF
	private long wallInterval = 0;

	int r = randInt(5000, 40000);

	boolean down = false;
	private boolean wallEvent = false;

	public Menu(final GameScreenManager gsm) {
		super(gsm);
		cam.position.set(0, 0, 0);
		cam.update();

		sb = new SpriteBatch();

		
		int x = 48;
		int y = 25;
		// button stuff
		resized(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		v = new FitViewport(viewport.width, viewport.height);
		stage = new Stage(v);
		
		//stage.setViewport(new FitViewport(viewport.width, viewport.height));
		
		skin = new Skin();
		// Generate a 1x1 white texture and store it in the skin named "white".
		Pixmap pixmap = new Pixmap(x, y, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();

		skin.add("white", new Texture(pixmap));

		// Store the default libgdx font under the name "default".
		BitmapFont bfont = new BitmapFont();
		
		skin.add("default", bfont);

		// Configure a TextButtonStyle and name it "default". Skin resources are
		// stored by type, so this doesn't overwrite the font.
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.LIGHT_GRAY);
		// textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);

		textButtonStyle.font = skin.getFont("default");

		skin.add("default", textButtonStyle);

		// Create a button with the "default" TextButtonStyle. A 3rd parameter
		// can be used to specify a name other than "default".
		textButton = new TextButton("PLAY", textButtonStyle);
		//textButton.setSize(x, y);
		textButton.setBounds(MainGame.V_WIDTH/2 - x/2 , MainGame.V_HEIGHT/5, x, y);
		
		
		//textButton.setPosition(MainGame.V_WIDTH/2,MainGame.V_HEIGHT/2);
		textButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.app.log("my app", "Pressed"); // ** Usually used to start
													// Game, etc. **//
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.app.log("my app", "Released");

				// Gdx.input.setInputProcessor(new MyInputProcessor());
				stage.clear();
				gsm.setScreen(101);
				gsm.set();
			

			}
		});

		
		resized(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.getViewport().setCamera(hudCam);
		//stage.setViewport(new FitViewport(viewport.width, viewport.height));
	
	
		//menuTable.setBounds(MainGame.V_WIDTH/2, MainGame.V_HEIGHT/2, 100, 100);
		stage.addActor(textButton);
		Gdx.input.setInputProcessor(stage);

		//

		world = new World(new Vector2(0, -9.81f), true);

		b2dr = new Box2DDebugRenderer();

		font = new BitmapFont();

		createPlayer();

		init();
		// set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, MainGame.V_WIDTH / PPM, MainGame.V_HEIGHT
				/ PPM);

		box2dLight.RayHandler.useDiffuseLight(true);
		handler = new RayHandler(world, viewport);
		handler.setAmbientLight(0.0f, 0.0f, 0.0f, 0.1f);
		handler.setShadows(true);
		handler.setAmbientLight(0.3f);

		//td = new ConeLight(handler, 40, Color.GRAY, 100 / PPM,
		//		player.getPosition().x, player.getPosition().y + 120 / PPM,
		//		270, 15);

	}

	public void render() {
		
		// System.out.println(Gdx.graphics.getWidth());
		// System.out.println(MainGame.V_WIDTH);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		resized(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
				(int) viewport.width, (int) viewport.height);
		
		
		
		//System.out.println(stage.getViewport().getWorldWidth() + "!!!!!!!!!!!!!" +viewport.width);
	
		

		
		world.step(1 / 60f, 1, 1);
		player.update(1 / 60f);
		//td.setPosition(player.getPosition().x, player.getPosition().y + 3 / PPM);

		// player.renderSample(sb);

		if (middleBgRight.getYPosition() > B2DVars.TRUE_HEIGHT) {
			middleBgRight.setYPosition(mRRepeat.getYPosition()
					- mRRepeat.getHeight());
		}
		if (middleBgLeft.getYPosition() > B2DVars.TRUE_HEIGHT) {
			middleBgLeft.setYPosition(mLRepeat.getYPosition()
					- mLRepeat.getHeight());
		}
		if (mRRepeat.getYPosition() > B2DVars.TRUE_HEIGHT) {
			mRRepeat.setYPosition(middleBgRight.getYPosition()
					- middleBgRight.getHeight());
		}
		if (mLRepeat.getYPosition() > B2DVars.TRUE_HEIGHT) {
			mLRepeat.setYPosition(middleBgLeft.getYPosition()
					- middleBgLeft.getHeight());
		}

		for (Background temp : backgrounds) {
			if (temp instanceof AnimatedBackground)
				temp.update(runTime);
			else
				temp.update(1 / 60f);
			temp.render(sb);
		}

		Array<Body> tempBodies = new Array<Body>();
		world.getBodies(tempBodies);
		for (Body b : tempBodies) {
			if (b.getPosition().y > b2dCam.position.y + MainGame.V_HEIGHT / PPM) {
				if (b.getUserData() instanceof StaticSprite) {
					ledgeList.removeValue((StaticSprite) b.getUserData(), true);
				}

				world.destroyBody(b);
				// obstacleList.removeValue(b, true);
			}
		}

		for (ConeLight l : lightList) {
			if (l.getPosition().y > b2dCam.position.y + MainGame.V_HEIGHT / PPM
					+ 100 / PPM) {
				l.setActive(false);
				handler.lightList.removeValue(l, true);
				lightList.removeValue(l, true);
				lights--;

			}
		}

		for (PointLight l : lightListP) {
			if (l.getPosition().y > b2dCam.position.y + MainGame.V_HEIGHT / PPM
					+ 100 / PPM) {
				l.setActive(false);
				handler.lightList.removeValue(l, true);
				lightListP.removeValue(l, true);
				lights--;

			}
		}

		if (rightWall.getYPosition() > cam.position.y / PPM + MainGame.V_HEIGHT
				/ 2 / PPM) {
			rightWall.setPosition(rWallRepeat.getYPosition()
					- rightWall.getHeight() / PPM);
		}
		if (leftWall.getYPosition() > cam.position.y / PPM + MainGame.V_HEIGHT
				/ 2 / PPM) {
			leftWall.setPosition(lWallRepeat.getYPosition()
					- leftWall.getHeight() / PPM);
		}
		if (rWallRepeat.getYPosition() > cam.position.y / PPM
				+ MainGame.V_HEIGHT / 2 / PPM) {
			rWallRepeat.setPosition(rightWall.getYPosition()
					- rWallRepeat.getHeight() / PPM);
		}
		if (lWallRepeat.getYPosition() > cam.position.y / PPM
				+ MainGame.V_HEIGHT / 2 / PPM) {
			lWallRepeat.setPosition(leftWall.getYPosition()
					- lWallRepeat.getHeight() / PPM);
		}

		for (Wall temp : foreground) {
			temp.render(sb);
		}

		x = player.getPosition().y;
		y = player.getPosition().y;

		if (Math.abs(x - x2) / PPM >= nextSprite / PPM) {
			if (leftWall.getBody().getPosition().x < 40 / PPM) {
				addObstacles();
			}

			x2 = x;
			nextSprite = (long) randInt(3, 7);
		}

		if (Math.abs(y - y2) / PPM >= nextSprite / PPM) {

			addPlatforms();
			y2 = y;
			nextSprite2 = (long) randInt(2, 6);
		}

		leftWall.getBody().setLinearVelocity(
				new Vector2(leftWall.getBody().getLinearVelocity().x, player
						.getBody().getLinearVelocity().y));
		rightWall.getBody().setLinearVelocity(
				new Vector2(rightWall.getBody().getLinearVelocity().x, player
						.getBody().getLinearVelocity().y));

		cam.position.set(player.getPosition().x * PPM, (player.getPosition().y)
				* PPM - CL, 0);

		cam.zoom = camZoom;
		hudCam.zoom = camZoom;
		hudCam.update();
		cam.update();
		sb.setProjectionMatrix(cam.combined);
		

		for (StaticSprite ledge : ledgeList) {
			ledge.update();
			ledge.render(sb);
		}

		// System.out.println(runTime);

		if ((int) runTime % 4 == 0) {
			b = true;
		} else {
			b = false;
		}

		if ((int) runTime % 4 == 0) {
			c = true;
		}

		// /FIX THIS///
		if (b == true) {

			state = true;
			if (justSwitchA()) {
				player.setRT(0f);
			}
			player.stoppingA();
			/*
			 * Vector2 vel = player.getBody().getLinearVelocity(); vel.y = -1f;
			 * player.getBody().setLinearVelocity(vel);
			 */
			player.getBody().setLinearDamping(4f);

		} else {
			state = false;
			if (justSwitchB()) {
				player.setRT(0f);
			}

			player.fallingA();
			player.getBody().setLinearDamping(0f);

		}
		// ////////////

		font.setUseIntegerPositions(false);

		player.render(sb);
		handler.setBlurNum(0);
		handler.setCombinedMatrix(b2dCam.combined);
		handler.updateAndRender();

		b2dCam.position.set(player.getPosition().x, player.getPosition().y - CL
				/ PPM, 0);
		b2dCam.zoom = camZoom;
		b2dCam.update();

		wallEvent();
		capVelocity();

		font.setColor(1f, 1f, 1f, fadeIn);

		if (fadeIn < 1f && fadeOut == false) {
			fadeIn += 0.01f;
			if (fadeIn >= 1f) {
				fadeIn = 1f;
				fadeOut = true;
			}

		}
		if (fadeOut) {
			fadeIn -= 0.01f;
			if (fadeIn <= 0f) {
				fadeIn = 0.01f;
				fadeOut = false;
			}
		}

		sb.begin();
		float w = font.getBounds("START").width;
		float h = font.getBounds("START").height;

		font.draw(sb, "START", cam.position.x - w / 2, cam.position.y + h / 2);
		sb.end();

		

		// stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
		sb.setProjectionMatrix(hudCam.combined);
		stage.act();
		stage.draw();
	

		
		sb.setProjectionMatrix(cam.combined);
	}

	public void capVelocity() {
		if (Math.abs(player.getBody().getLinearVelocity().y) > 6f) {

			Vector2 vel = player.getBody().getLinearVelocity();
			vel.y = -6f;
			player.getBody().setLinearVelocity(vel);

		}
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	public void update(float dt) {
		runTime += dt;
		// System.out.println("MENU");
		lastState = state;
		// handleInput();

	}

	

	public void handleInput() {
		if (MyInput.isPressed(MyInput.BUTTON1)) {
			System.out.println("pressed z");

			// gsm.setScreen(101);
			// gsm.set();

			// if(cl.isPlayerOnGround()){
			// playerBody.applyForceToCenter(0,200,true);
			// player.getBody().applyForceToCenter(0,200,true);

			// }
		}

	}

	public void createPlayer() {
		// create player body
		BodyDef bdef = new BodyDef();
		bdef.position.set(160 / PPM, 300 / PPM);
		bdef.type = BodyType.DynamicBody;

		Body pBody = world.createBody(bdef);

		MassData md = pBody.getMassData();
		md.mass = 1;
		pBody.setMassData(md);

		// create box shape for player collision box
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((float) (7.5 / PPM), 10 / PPM);

		// create fixture for player
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		// fdef.restitution = 1f;
		fdef.friction = 0f;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;

		pBody.createFixture(fdef).setUserData("Player");
		shape.dispose();

		// create foot sensor
		shape = new PolygonShape();
		shape.setAsBox((float) (5.5 / PPM), 2 / PPM, new Vector2(0, -10 / PPM),
				0);

		// create fixture for foot
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;
		fdef.isSensor = true;

		pBody.createFixture(fdef).setUserData("foot");
		shape.dispose();

		// create player
		player = new Player(pBody);
		pBody.setUserData(player);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

	}

	public void resized(int width, int height) {
		float aspectRatio;

		aspectRatio = (float) width / (float) height;

		float scale = 1f;
		Vector2 crop = new Vector2(0f, 0f);

		if (aspectRatio > ASPECT_RATIO) {
			scale = (float) height / (float) MainGame.V_HEIGHT;
			crop.x = (width - MainGame.V_WIDTH * scale) / 2f;
		} else if (aspectRatio < ASPECT_RATIO) {
			scale = (float) width / (float) MainGame.V_WIDTH;
			crop.y = (height - MainGame.V_HEIGHT * scale) / 2f;
		} else {
			scale = (float) width / (float) MainGame.V_WIDTH;
		}

		float w = (float) MainGame.V_WIDTH * scale;
		float h = (float) MainGame.V_HEIGHT * scale;
		viewport = new Rectangle(crop.x, crop.y, w, h);
		
		
	}

	public void init() {

		// l1 = new ParallaxLayer(AssetLoader.waterFallBG, new Vector2(0f, 0f),
		// new Vector2(0f,0));

		// b = new ParallaxBackground(new ParallaxLayer[]{l1}, 320, 480,new
		// Vector2(0,0), cam);

		// create platform
		BodyDef bdef = new BodyDef();

		// bdef.position.set(160/PPM,120/PPM);
		// bdef.type = BodyType.StaticBody;
		// Body body = world.createBody(bdef);

		PolygonShape shape = new PolygonShape();
		// shape.setAsBox(50/PPM, 5/PPM); //half width half height, so 100, 10

		FixtureDef fdef = new FixtureDef();
		// fdef.shape = shape;
		// fdef.filter.categoryBits = B2DVars.BIT_GROUND;
		// fdef.filter.maskBits = B2DVars.BIT_BOX | B2DVars.BIT_BALL;
		// fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		// body.createFixture(fdef).setUserData("Ground");
		shape.dispose();

		createPlayer();
		// b.setPlayer(player);

		TextureRegion[] bgAnim = { AssetLoader.waterFallBG,
				AssetLoader.waterFallBG2 };
		AnimatedBackground bg = new AnimatedBackground(bgAnim, cam);

		middleBgLeft = new Middleground(AssetLoader.middleBGLeft, cam, player,
				0, 0, 1f);
		mLRepeat = new Middleground(AssetLoader.middleBGLeft, cam, player, 0,
				middleBgLeft.getYPosition() - middleBgLeft.getHeight(), 1f);

		middleBgRight = new Middleground(AssetLoader.middleBGRight, cam,
				player, B2DVars.TRUE_WIDTH, 0, 1f);
		mRRepeat = new Middleground(AssetLoader.middleBGRight, cam, player,
				B2DVars.TRUE_WIDTH, middleBgRight.getYPosition()
						- middleBgRight.getHeight(), 1f);

		Background[] bgs = { middleBgLeft, mLRepeat, middleBgRight, mRRepeat,
				bg };
		backgrounds = bgs;

		createWalls();
	}

	public void createWalls() {
		// Left wall
		BodyDef bdef = new BodyDef();
		bdef.position.set(0 / PPM, 0 + 200 / PPM);
		bdef.type = BodyType.KinematicBody;
		Body lWall = world.createBody(bdef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(2 / PPM, (MainGame.V_HEIGHT + 50 / 2) / PPM); // half
																		// width
																		// half
																		// height,
																		// so
																		// 100,
																		// 10

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_GROUND;
		// fdef.filter.maskBits = B2DVars.BIT_BOX | B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		lWall.createFixture(fdef).setUserData("LeftWall");

		leftWall = new LeftWall(lWall);
		leftWall.setPosition(cam.position.y - B2DVars.TRUE_WIDTH / 2 / PPM);
		lWallRepeat = new LeftWall(lWall);
		lWallRepeat.setPosition(leftWall.getYPosition() - leftWall.getHeight()
				/ PPM);

		// Right wall
		bdef = new BodyDef();
		bdef.position.set((MainGame.V_WIDTH) / PPM, 0 + 200 / PPM);
		bdef.type = BodyType.KinematicBody;
		Body rWall = world.createBody(bdef);

		shape.dispose();
		shape = new PolygonShape();
		shape.setAsBox(2 / PPM, (MainGame.V_HEIGHT + 50 / 2) / PPM); // half
																		// width
																		// half
																		// height,
																		// so
																		// 100,
																		// 10

		fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_GROUND;
		// fdef.filter.maskBits = B2DVars.BIT_BOX | B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		rWall.createFixture(fdef).setUserData("RightWall");

		rightWall = new RightWall(rWall);
		rightWall.setPosition(cam.position.y - B2DVars.TRUE_WIDTH / 2 / PPM);
		rWallRepeat = new RightWall(rWall);
		rWallRepeat.setPosition(rightWall.getYPosition()
				- rightWall.getHeight() / PPM);
		shape.dispose();

		Wall[] temp = { rightWall, rWallRepeat, leftWall, lWallRepeat };
		foreground = temp;
	}

	public float getWallPos() {
		if (Math.random() > .5) {
			return leftWall.getPosition().x + ((MainGame.V_WIDTH / 10) / PPM);
		} else {
			return rightWall.getPosition().x - ((MainGame.V_WIDTH / 10) / PPM);
		}
	}

	public void addObstacles() {
		BodyDef def = new BodyDef();
		// def.position.set(randInt(0,(320-(Game.V_WIDTH/5)-3))/PPM,(cam.position.y
		// - (Game.V_HEIGHT*2)/PPM));
		// def.position.set(((Game.V_WIDTH/5)+3)/PPM,(cam.position.y -
		// (Game.V_HEIGHT*2)/PPM));
		def.position.set(
				randInt(((MainGame.V_WIDTH / 5) + 3),
						(320 - (MainGame.V_WIDTH / 5) - 3)) / PPM,
				(b2dCam.position.y - (MainGame.V_HEIGHT * 2) / PPM));

		boolean redo = false;
		System.out.println("+" + def.position.x);

		if (def.position.x == 1.6) {
			System.out.println("Redoing1");
			redo = true;
			// def.position.set(randInt(((MainGame.V_WIDTH/5)+3),(320-(MainGame.V_WIDTH/5)-3))/PPM,(b2dCam.position.y
			// - (MainGame.V_HEIGHT*2)/PPM));

		} else if (def.position.x >= 1.6f && def.position.x <= 2.4f) {
			System.out.println("Redoing2");
			redo = true;
			// def.position.set(randInt(((MainGame.V_WIDTH/5)+3),(320-(MainGame.V_WIDTH/5)-3))/PPM,(b2dCam.position.y
			// - (MainGame.V_HEIGHT*2)/PPM));

		}

		else if (def.position.x <= 1.6f && def.position.x >= 0.9f) {
			System.out.println("Redoing3");
			redo = true;
			// def.position.set(randInt(((MainGame.V_WIDTH/5)+3),(320-(MainGame.V_WIDTH/5)-3))/PPM,(b2dCam.position.y
			// - (MainGame.V_HEIGHT*2)/PPM));

		} else {
			redo = false;
		}

		// def.position.set(0.9f,(b2dCam.position.y -
		// (MainGame.V_HEIGHT*2)/PPM));
		// def.position.set(2.4f,(b2dCam.position.y -
		// (MainGame.V_HEIGHT*2)/PPM));

		System.out.println("+" + def.position.x);

		if (redo == false) {
			def.type = BodyType.StaticBody;

			Body body = world.createBody(def);

			PolygonShape shape = new PolygonShape();
			float width = randInt(6, 7);
			shape.setAsBox((MainGame.V_WIDTH / width) / PPM, 5 / PPM); // half
																		// width
																		// half
																		// height,
																		// so
																		// 100,
																		// 10
			width = (MainGame.V_WIDTH / width) / PPM;

			System.out.println("@@@@@@@" + width);

			FixtureDef fdef = new FixtureDef();
			fdef.shape = shape;
			fdef.density = 0.4f;
			fdef.filter.categoryBits = B2DVars.BIT_GROUND;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			body.createFixture(fdef).setUserData("Ground");

			// ConeLight t;
			// t = new ConeLight(handler, 40, Color.GRAY,800/PPM,
			// body.getPosition().x, body.getPosition().y + 120/PPM, 270, 30);

			PointLight d;
			d = new PointLight(handler, 40, Color.valueOf("A9E2FF"),
					grow / PPM, player.getPosition().x, player.getPosition().y);

			d.setXray(true);
			d.setSoft(true);
			// Light.setContactFilter(B2DVars.BIT_BALL, (short) 0,
			// B2DVars.BIT_BALL);
			// t.isSoft();

			LedgeMiddle temp = new LedgeMiddle(body, width, d);
			body.setUserData(temp);
			ledgeList.add(temp);
			// obstacleList.add(body);
			// lightList.add(t);
			shape.dispose();
		}

	}

	public void addPlatforms() {

		if (Math.random() <= .25) {
			addLeftPlatform();
			addRightPlatform();
		} else {
			if (Math.random() < .5)
				addLeftPlatform();
			else
				addRightPlatform();
		}

	}

	public void addLeftPlatform() {
		// adding platforms to the walls
		BodyDef def = new BodyDef();
		def.position.set(leftWall.getPosition().x
				+ ((MainGame.V_WIDTH / 10) / PPM),
				(cam.position.y / PPM - (MainGame.V_HEIGHT * 2) / PPM));

		def.type = BodyType.StaticBody;
		Body body = world.createBody(def);

		PolygonShape shape2 = new PolygonShape();
		shape2.setAsBox((MainGame.V_WIDTH / 10) / PPM, 5 / PPM); // half width
																	// half
																	// height,
																	// so 100,
																	// 10

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape2;
		fdef.density = 0.4f;
		fdef.filter.categoryBits = B2DVars.BIT_GROUND;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		body.createFixture(fdef).setUserData("Wall Platform");
		shape2.dispose();

		ConeLight t;
		t = new ConeLight(handler, 40, Color.GRAY, 800 / PPM,
				body.getPosition().x, body.getPosition().y + 120 / PPM, 270, 20);

		// PointLight d;
		// d = new PointLight(handler, 40, Color.LIGHT_GRAY, grow/PPM,
		// player.getPosition().x, player.getPosition().y);

		t.setSoft(true);

		Lamp lamp = new Lamp(t);

		LedgeLeft temp = new LedgeLeft(body, leftWall, lamp);
		body.setUserData(temp);
		ledgeList.add(temp);
		lightList.add(t);
	}

	public void addRightPlatform() {
		// adding platforms to the walls
		BodyDef def = new BodyDef();
		def.position.set(rightWall.getPosition().x
				- ((MainGame.V_WIDTH / 10) / PPM),
				(cam.position.y / PPM - (MainGame.V_HEIGHT * 2) / PPM));

		def.type = BodyType.StaticBody;
		Body body = world.createBody(def);

		PolygonShape shape2 = new PolygonShape();
		shape2.setAsBox((MainGame.V_WIDTH / 10) / PPM, 5 / PPM); // half width
																	// half
																	// height,
																	// so 100,
																	// 10

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape2;
		fdef.density = 0.4f;
		fdef.filter.categoryBits = B2DVars.BIT_GROUND;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		body.createFixture(fdef).setUserData("Wall Platform");
		shape2.dispose();

		ConeLight t;
		t = new ConeLight(handler, 40, Color.GRAY, 800 / PPM,
				body.getPosition().x, body.getPosition().y + 120 / PPM, 270, 20);
		t.setSoft(true);

		Lamp lamp = new Lamp(t);

		// PointLight d;
		// d = new PointLight(handler, 40, Color.LIGHT_GRAY, grow/PPM,
		// player.getPosition().x, player.getPosition().y);

		LedgeRight temp = new LedgeRight(body, rightWall, lamp);
		body.setUserData(temp);
		ledgeList.add(temp);
		lightList.add(t);
	}

	public void reset() {
		lastDepth = 0;
		lastStop = 0;
		score = 0;
	}

	public void wallEvent() {

		long s = System.currentTimeMillis();

		// System.out.println(r);

		if (s - wallInterval > r) {

			r = randInt(10000, 40000);

			wallEvent = !wallEvent;
			wallInterval = s;
		}
		if (wallEvent) {
			wallEvent = false;
			System.out.println(leftWall.getBody().getPosition().x);
			System.out.println("GOING---------------------------------------");
			if (!AssetLoader.caveIn.isPlaying()) {
				AssetLoader.caveIn.play();
			}
			leftWall.getBody().setLinearVelocity(
					new Vector2(10 / PPM,
							player.getBody().getLinearVelocity().y));
			rightWall.getBody().setLinearVelocity(
					new Vector2(-10 / PPM,
							player.getBody().getLinearVelocity().y));
		}

		else if (leftWall.getBody().getPosition().x > 60 / PPM) {

			// if(!AssetLoader.caveIn.isPlaying()){
			// AssetLoader.caveIn.play();
			// }
			// System.out.println("RESTORING---------------------------------------");
			leftWall.getBody().setLinearVelocity(
					new Vector2(-10 / PPM,
							player.getBody().getLinearVelocity().y));
			rightWall.getBody().setLinearVelocity(
					new Vector2(10 / PPM,
							player.getBody().getLinearVelocity().y));
		} else if (leftWall.getBody().getPosition().x <= 0 / PPM) {
			// System.out.println("STOPPED---------------------------------------");
			leftWall.getBody()
					.setLinearVelocity(
							new Vector2(0 / PPM, player.getBody()
									.getLinearVelocity().y));
			rightWall.getBody()
					.setLinearVelocity(
							new Vector2(0 / PPM, player.getBody()
									.getLinearVelocity().y));
		}

	}

	public static int randInt(int min, int max) {

		// Usually this can be a field rather than a method variable
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		return rand.nextInt((max - min) + 1) + min;
	}

	public boolean isStateA() {
		return state;
	}

	public boolean justSwitchA() {
		return state && !lastState;
	}

	public boolean justSwitchB() {
		return !state && lastState;
	}

	@Override
	public void resize(int width, int height) {
		System.out.println("CALLED !!!!");
		
		
	}
	
public void dispose(){
		
		Array<Body> temps = new Array<Body>();
		
		AssetLoader.caveIn.stop();
		
		world.getBodies(temps);
		
		for(Body b : temps){
			world.destroyBody(b);
			System.out.println(world.getBodyCount() + "-----------------------------------------------------------" + temps.size);
		}
		temps.clear();
		
		
		handler.dispose();
		world.dispose();
		b2dr.dispose();
		font.dispose();
		stage.dispose();
		skin.dispose();
		
		sb.dispose();
		
	
		
		
		
		
		
	}

}
