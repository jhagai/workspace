package fr.heyjojo.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import fr.heyjojo.game.Element.ElementType;

public class GameScreen implements Screen {

	InputAdapter inputAdapter = new InputAdapter() {
		@Override
		public boolean keyDown(int keycode) {
			switch (keycode) {
			case Keys.LEFT:
				keyLeft = true;
				break;
			case Keys.RIGHT:
				keyRight = true;
				break;
			case Keys.N:
				keyN = true;
				break;
			case Keys.D:
				keyD = true;
				break;
			case Keys.C:
				intersectorActor.setCheckCollisions(true);
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
			case Keys.N:
				keyN = false;
				break;
			case Keys.D:
				keyD = false;
				break;
			default:
				break;
			}
			return false;
		}

		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			elementToDrag = null;
			return true;
		}

		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			mouseX = screenX;
			mouseY = -1 * screenY + 480;
			Element elementToDelete = null;
			if (button == Input.Buttons.RIGHT) {
				for (Element element : dynaElements) {
					if (element.getCurrentBounds().contains(mouseX, mouseY)) {
						elementToDelete = element;
						dynaElements.removeValue(element, true);
						break;
					}
				}
				if (elementToDelete == null) {
					for (Element obstacle : obstacles) {
						if (obstacle.getCurrentBounds().contains(mouseX, mouseY)) {
							elementToDelete = obstacle;
							obstacles.removeValue(obstacle, true);
							// Gdx.app.debug("TrackMouse",
							// "my debug message");
							break;
						}
					}
				}
			}
			return true;
		}

		public boolean touchDragged(int screenX, int screenY, int pointer) {
			mouseX = screenX;
			mouseY = -1 * screenY + 480;
			if (elementToDrag == null) {
				for (Element element : dynaElements) {
					if (element.getCurrentBounds().contains(mouseX, mouseY)) {
						elementToDrag = element;
						intersectorActor.setCheckCollisions(false);
						// Gdx.app.debug("TrackMouse", "my debug message");
						break;
					}
				}
				if (elementToDrag == null) {
					for (Element obstacle : obstacles) {
						if (obstacle.getCurrentBounds().contains(mouseX, mouseY)) {
							elementToDrag = obstacle;
							intersectorActor.setCheckCollisions(false);
							// Gdx.app.debug("TrackMouse",
							// "my debug message");
							break;
						}
					}
				}
			}
			return true;
		}

	};

	FPSLogger fpsLogger;

	ShapeRenderer renderer;

	Stage stage;

	private boolean keyN;

	private boolean keyD;

	private boolean keyUp;

	private boolean keyDown;

	private boolean keyLeft;

	private boolean keyRight;

	private Vector2 mousePosition;

	private int mouseX;

	private int mouseY;

	private Element elementToDrag = null;

	Array<Element> dynaElements = new Array<Element>();

	IntersectorActor intersectorActor = new IntersectorActor();

	Array<Element> obstacles = new Array<Element>();

	public GameScreen() {

		fpsLogger = new FPSLogger();
		stage = new Stage(new ScreenViewport());
		renderer = new ShapeRenderer();
		renderer.setAutoShapeType(true);

		Element dyna1 = createDynamicElement(150, 215, 50, 50);
		dyna1.getCurrentBounds().set(424, 227, 50, 50);
		
		dynaElements.add(dyna1);

		Element dyna2 = createDynamicElement(514, 225, 50, 50);
		dyna2.getCurrentBounds().set(207, 225, 50, 50);
		
		intersectorActor.setDynamicElements(dynaElements);

		dynaElements.add(dyna2);
		
		Actor line = new Actor() {

			public void draw(Batch batch, float parentAlpha) {
				renderer.set(ShapeType.Line);
				renderer.setColor(getStage().getDebugColor());
				renderer.line(getX(), getY(), getX() + getWidth(), getY() + getHeight());
			}

		};
		line.setBounds(200, 200, 100, 100);

		Element obstacle = createObstacle(649, 194, 100, 100);
		obstacle.getCurrentBounds().set(208,199,100,100);
		// Element obstacle2 = createObstacle(250, 90, 100, 100);
		// Element obstacle3 = createObstacle(350, 290, 100, 100);

		obstacles.add(obstacle);
		// obstacles.add(obstacle2);
		// obstacles.add(obstacle3);

		intersectorActor.setObstacles(obstacles);

		Actor elementCreator = new Actor() {
			@Override
			public void act(float delta) {
				if (keyN) {
					Element element = createObstacle(450, 290, 100, 100);
					obstacles.add(element);
					keyN = false;
				} else if (keyD) {
					Element element = createDynamicElement(0, 0, 50, 50);
					dynaElements.add(element);
					keyD = false;
				}
			}
		};

		Actor elementRenderer = new Actor() {
			@Override
			public void draw(Batch batch, float parentAlpha) {

				Color color = Color.GREEN;
				for (Element element : dynaElements) {
					MyRectangle rect = element.getCurrentBounds();
					renderer.setColor(color);
					renderer.set(ShapeType.Filled);
					renderer.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
					drawMovingBounds(element);
					color = Color.WHITE;
				}

				for (Element obstacle : obstacles) {
					MyRectangle rect = obstacle.getCurrentBounds();
					renderer.setColor(Color.CYAN);
					renderer.set(ShapeType.Line);
					renderer.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
					drawMovingBounds(obstacle);
				}

			}

			private void drawMovingBounds(Element element) {
				MyRectangle currentBounds = element.getCurrentBounds();
				MyRectangle previousBounds = element.getPreviousBounds();

				if (previousBounds.getX() != currentBounds.getX() || previousBounds.getY() != currentBounds.getY()) {
					MyRectangle squareLeft = previousBounds;
					MyRectangle squareRight = currentBounds;
					MyRectangle squareDown = previousBounds;
					MyRectangle squareUp = currentBounds;

					if (currentBounds.getX() < previousBounds.getX()) {
						squareLeft = currentBounds;
					}
					if (currentBounds.getX() + currentBounds.getWidth() < previousBounds.getX() + previousBounds.getWidth()) {
						squareRight = previousBounds;
					}
					if (currentBounds.getY() < previousBounds.getY()) {
						squareDown = currentBounds;
					}
					if (currentBounds.getY() + currentBounds.getHeight() < previousBounds.getY() + previousBounds.getHeight()) {
						squareUp = previousBounds;
					}

					float[] vertices = new float[12];

					if (squareLeft == squareDown) {
						vertices[0] = squareLeft.getX() + squareLeft.getWidth();
						vertices[1] = squareLeft.getY();
						vertices[2] = squareRight.getX() + squareRight.getWidth();
						vertices[3] = squareRight.getY();
						vertices[4] = squareRight.getX() + squareRight.getWidth();
						vertices[5] = squareRight.getY() + squareRight.getHeight();
						vertices[6] = squareRight.getX();
						vertices[7] = squareRight.getY() + squareRight.getHeight();
						vertices[8] = squareLeft.getX();
						vertices[9] = squareLeft.getY() + squareLeft.getHeight();
						vertices[10] = squareLeft.getX();
						vertices[11] = squareLeft.getY();
					} else {
						vertices[0] = squareLeft.getX();
						vertices[1] = squareLeft.getY();
						vertices[2] = squareRight.getX();
						vertices[3] = squareRight.getY();
						vertices[4] = squareRight.getX() + squareRight.getWidth();
						vertices[5] = squareRight.getY();
						vertices[6] = squareRight.getX() + squareRight.getWidth();
						vertices[7] = squareRight.getY() + squareRight.getHeight();
						vertices[8] = squareLeft.getX() + squareLeft.getWidth();
						vertices[9] = squareLeft.getY() + squareLeft.getHeight();

						vertices[10] = squareLeft.getX();
						vertices[11] = squareLeft.getY() + squareLeft.getHeight();
					}
					renderer.setColor(Color.MAGENTA);
					renderer.set(ShapeType.Line);
					renderer.polygon(vertices);
				}
			}
		};

		Actor elementActor = new Actor() {
			@Override
			public void act(float delta) {

				if (elementToDrag != null) {
					moveElement(elementToDrag);
				}
			}

			private void moveElement(Element element) {
				MyRectangle rect = element.getCurrentBounds();
				float squareNewX = mouseX - (rect.getWidth() / 2);
				float squareNewY = mouseY - (rect.getHeight() / 2);

				if (mouseX < 0) {
					squareNewX = -1 * (rect.getWidth() / 2);
				} else if (mouseX > 800) {
					squareNewX = 800 - (rect.getWidth() / 2);
				}

				if (mouseY < 0) {
					squareNewY = -1 * (rect.getHeight() / 2);
				} else if (mouseY > 480) {
					squareNewY = 480 - (rect.getHeight() / 2);
				}

				rect.setX(squareNewX);
				rect.setY(squareNewY);
			}
		};

		stage.addActor(elementActor);
		stage.addActor(intersectorActor);
		stage.addActor(elementCreator);
		stage.addActor(elementRenderer);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		Gdx.input.setInputProcessor(inputAdapter);
	}

	private Element createDynamicElement(float x, float y, float width, float height) {
		Element element = new Element(ElementType.DYNAMIC) {
		};
		element.init(x, y, width, height);
		return element;
	}

	private Element createObstacle(float x, float y, float w, float h) {
		Element obstacle2 = new Element(ElementType.KINEMATIC) {
		};
		obstacle2.init(x, y, w, h);
		return obstacle2;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		renderer.begin();

		stage.draw();

		renderer.end();
		// fpsLogger.log();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
