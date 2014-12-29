package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class LoadingScreen extends ScreenAdapter {

	private Drop game;

	private OrthographicCamera camera;

	private AssetManager loadingScreenManager;

	private Texture loadingScreen;

	private Screen nextScreen;

	public void setNextScreen(Screen nextScreen) {
		this.nextScreen = nextScreen;
	}

	public LoadingScreen(Drop game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
	}

	public void init(Screen nexScreen) {
		this.nextScreen = nexScreen;
		loadingScreen = null;
		loadingScreenManager = new AssetManager();
		loadingScreenManager.load("loading.png", Texture.class);
	}

	@Override
	public void render(float delta) {

		if (!game.assetManager.update()) {
			// display loading screen
			loadingScreenManager.finishLoading();
			if (loadingScreen == null) {
				loadingScreen = loadingScreenManager.get("loading.png",
						Texture.class);
			}
			Gdx.gl.glClearColor(0, 0, 0.2f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			camera.update();

			game.batch.setProjectionMatrix(camera.combined);
			game.batch.begin();
			game.batch.draw(loadingScreen, 0, 0);
			game.batch.end();
		} else {
			game.setScreen(nextScreen);
			dispose();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		loadingScreenManager.dispose();
	}

}
