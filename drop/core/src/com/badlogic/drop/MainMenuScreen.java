package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.I18NBundle;

public class MainMenuScreen implements ResetableScreen {

	private Drop game;

	OrthographicCamera camera;
	
	I18NBundle myBundle;

	public MainMenuScreen(final Drop game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		
		game.font.draw(game.batch, myBundle.format("main.welcome"), 100, 150);
		game.font.draw(game.batch, myBundle.format("main.directive"), 100, 100);
		
		game.batch.end();

		if (Gdx.input.isTouched()) {
			game.changeScreen();
			dispose();
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		myBundle = game.assetManager.get("i18n/GameBundle",
				I18NBundle.class);

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
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		game.assetManager.load("i18n/GameBundle", I18NBundle.class);
	}

}
