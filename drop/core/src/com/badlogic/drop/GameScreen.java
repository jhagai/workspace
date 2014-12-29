package com.badlogic.drop;

import java.util.Iterator;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements ResetableScreen {

	final Drop game;

	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;

	private OrthographicCamera camera;

	private Rectangle bucket;

	private Vector3 touchPos = new Vector3();

	private Array<PoolableRectangle> raindrops;

	private boolean keyLeft;

	private boolean keyRight;

	private int mouseX;

	private int mouseY;

	private boolean doTrackMouse = false;

	private I18NBundle myBundle;

	Controller controller;

	int dropsGathered;

	private Pool<PoolableRectangle> rectanglePool = new Pool<PoolableRectangle>() {

		@Override
		protected PoolableRectangle newObject() {

			return new PoolableRectangle();
		}

	};

	private InputAdapter inputAdapter = new InputAdapter() {
		@Override
		public boolean keyDown(int keycode) {
			switch (keycode) {
			case Keys.LEFT:
				keyLeft = true;
				break;
			case Keys.RIGHT:
				keyRight = true;
				break;
			default:
				break;
			}
			return true;
		}

		@Override
		public boolean keyUp(int keycode) {
			switch (keycode) {
			case Keys.LEFT:
				keyLeft = false;
				break;
			case Keys.RIGHT:
				keyRight = false;
				break;
			default:
				break;
			}
			return false;
		}

		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			doTrackMouse = false;
			return true;
		}

		public boolean touchDragged(int screenX, int screenY, int pointer) {
			doTrackMouse = true;
			mouseX = screenX;
			mouseY = screenY;
			return true;
		}
	};

	private long lastDropTime;

	private ControllerListener controllerAdapter = new ControllerListener() {

		@Override
		public boolean ySliderMoved(Controller controller, int sliderCode,
				boolean value) {
			Gdx.app.debug("ySliderMoved", "sliderCode = " + sliderCode
					+ " value = " + value);
			return true;
		}

		@Override
		public boolean xSliderMoved(Controller controller, int sliderCode,
				boolean value) {
			Gdx.app.debug("xSliderMoved", "sliderCode = " + sliderCode
					+ " value = " + value);
			return true;
		}

		@Override
		public boolean povMoved(Controller controller, int povCode,
				PovDirection value) {
			Gdx.app.debug("povMoved", "povCode = " + povCode
					+ " PovDirection = " + value);

			switch (value) {
			case east:
				keyRight = true;
				break;
			case west:
				keyLeft = true;
				break;
			default:
				keyLeft = false;
				keyRight = false;

			}
			return true;
		}

		@Override
		public void disconnected(Controller controller) {
			Gdx.app.debug("disconnected", "controller.getName() = "
					+ controller.getName());
		}

		@Override
		public void connected(Controller controller) {
			Gdx.app.debug("connected",
					"controller.getName() = " + controller.getName());
		}

		@Override
		public boolean buttonUp(Controller controller, int buttonCode) {
			Gdx.app.debug("buttonUp", "buttonCode = " + buttonCode);
			return true;
		}

		@Override
		public boolean buttonDown(Controller controller, int buttonCode) {
			Gdx.app.debug("buttonDown", "buttonCode = " + buttonCode);
			return true;
		}

		@Override
		public boolean axisMoved(Controller controller, int axisCode,
				float value) {
			Gdx.app.debug("axisMoved", "axisCode = " + axisCode + " value = "
					+ value);
			return true;
		}

		@Override
		public boolean accelerometerMoved(Controller controller,
				int accelerometerCode, Vector3 value) {
			Gdx.app.debug("accelerometerMoved", "accelerometerCode = "
					+ accelerometerCode + " value = " + value);
			return false;
		}
	};

	public GameScreen(final Drop game) {

		dropsGathered = 0;

		this.game = game;

		Controllers.addListener(controllerAdapter);
		Gdx.input.setInputProcessor(inputAdapter);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		// start the playback of the background music immediately

		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;
		raindrops = new Array<PoolableRectangle>();

		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}

	private void spawnRaindrop() {
		PoolableRectangle raindrop = rectanglePool.obtain();
		raindrop.x = MathUtils.random(0, 800 - 64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void dispose() {
		game.batch.dispose();
		rectanglePool.clear();
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.font.draw(game.batch, myBundle.format("game.hud", dropsGathered),
				0, 480);
		game.batch.draw(bucketImage, bucket.x, bucket.y);
		for (Rectangle raindrop : raindrops) {
			game.batch.draw(dropImage, raindrop.x, raindrop.y);
		}

		game.batch.end();

		if (doTrackMouse) {
			touchPos.set(mouseX, mouseY, 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}

		if (keyLeft)
			bucket.x -= 200 * delta;
		if (keyRight)
			bucket.x += 200 * delta;

		if (bucket.x < 0)
			bucket.x = 0;
		if (bucket.x > 800 - 64)
			bucket.x = 800 - 64;

		if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnRaindrop();

		Iterator<PoolableRectangle> iter = raindrops.iterator();

		while (iter.hasNext()) {
			PoolableRectangle raindrop = iter.next();
			raindrop.y -= 200 * delta;
			if (raindrop.y + 64 < 0) {
				rectanglePool.free(raindrop);
				iter.remove();
			} else if (raindrop.overlaps(bucket)) {
				dropsGathered++;
				rectanglePool.free(raindrop);
				dropSound.play();
				iter.remove();
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		dropImage = game.assetManager.get("droplet.png", Texture.class);
		bucketImage = game.assetManager.get("bucket.png", Texture.class);
		dropSound = game.assetManager.get("drop.wav", Sound.class);
		rainMusic = game.assetManager.get("rain.mp3", Music.class);
		myBundle = game.assetManager.get("i18n/GameBundle", I18NBundle.class);

		rainMusic.setLooping(true);
		rainMusic.play();
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

	@Override
	public void init() {
		game.assetManager.load("droplet.png", Texture.class);
		game.assetManager.load("bucket.png", Texture.class);
		game.assetManager.load("drop.wav", Sound.class);
		game.assetManager.load("rain.mp3", Music.class);
		game.assetManager.load("i18n/GameBundle", I18NBundle.class);
	}
}
