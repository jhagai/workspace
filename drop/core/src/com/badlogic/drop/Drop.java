package com.badlogic.drop;

import java.util.Iterator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Drop extends Game {

	public SpriteBatch batch;
	public BitmapFont font;

	public AssetManager assetManager = new AssetManager();

	public MainMenuScreen mainMenu;
	public LoadingScreen loadingScreen;
	public GameScreen gameScreen;
	public Array<ResetableScreen> screens = new Array<ResetableScreen>();

	public Iterator<ResetableScreen> iterator;

	public Drop() {
	}

	public void create() {
		batch = new SpriteBatch();
		// Use LibGDX's default Arial font.
		font = new BitmapFont();

		mainMenu = new MainMenuScreen(this);
		loadingScreen = new LoadingScreen(this);
		gameScreen = new GameScreen(this);

		screens.add(mainMenu);
		screens.add(gameScreen);

		iterator = screens.iterator();
		changeScreen();
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
		assetManager.dispose();
	}

	public void changeScreen() {
		
		ResetableScreen screen = iterator.next();
		assetManager.dispose();
		assetManager = new AssetManager();
		screen.init();
		loadingScreen.init(screen);
		setScreen(loadingScreen);
	}
}