package fr.eljojo.breakout.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fr.eljojo.breakout.Breakout;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Casse-briques";
		config.useGL30 = false;
		
		
		new LwjglApplication(new Breakout(), config);
	}
}
