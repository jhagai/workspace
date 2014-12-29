package fr.heyjojo.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HeyjojoGame extends Game {
	SpriteBatch batch;
	Texture img;

	@Override
	public void create() {
		batch = new SpriteBatch();
		
		GameScreen gameScreen = new GameScreen();
		
		img = new Texture("badlogic.jpg");
		setScreen(gameScreen);
	}

	@Override
	public void render() {
		super.render(); // important!
	}

	
	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
	}
}
