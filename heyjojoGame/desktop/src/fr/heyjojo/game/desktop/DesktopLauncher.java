package fr.heyjojo.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fr.heyjojo.game.HeyjojoGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "HeyjojoGame";
		config.width = 800;
		config.height = 480;
		//config.height = 800;
		config.resizable = false;
		new LwjglApplication(new HeyjojoGame(), config);
	}
}
