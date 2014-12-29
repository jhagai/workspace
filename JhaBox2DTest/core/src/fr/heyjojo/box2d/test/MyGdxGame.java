package fr.heyjojo.box2d.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	/** the camera **/
	protected OrthographicCamera camera;

	/** the renderer **/
	protected Box2DDebugRenderer renderer;

	BitmapFont font;

	/** our box2D world **/
	protected World world;

	/** ground body to connect the mouse joint to **/
	protected Body groundBody;

	/** our mouse joint **/
	protected MouseJoint mouseJoint = null;

	/** a hit body **/
	protected Body hitBody = null;

	/** temp vector **/
	protected Vector2 tmp = new Vector2();

	boolean keyLeft = false;

	boolean keyRight = false;

	boolean keyUp = false;

	boolean keyDown = false;

	Body squareBody;

	InputAdapter InputAdapter = new InputAdapter() {
		public boolean keyDown(int keycode) {
			switch (keycode) {
			case Keys.LEFT:
				keyLeft = true;
				break;
			case Keys.RIGHT:
				keyRight = true;
				break;
			case Keys.UP:
				keyUp = true;
				break;
			case Keys.DOWN:
				keyDown = true;
				break;
			}
			return true;
		}

		public boolean keyUp(int keycode) {
			switch (keycode) {
			case Keys.LEFT:
				keyLeft = false;
				break;
			case Keys.RIGHT:
				keyRight = false;
				break;
			case Keys.UP:
				keyUp = false;
				break;
			case Keys.DOWN:
				keyDown = false;
				break;
			}
			return true;
		}
	};

	@Override
	public void create() {
		// setup the camera. In Box2D we operate on a
		// meter scale, pixels won't do it. So we use
		// an orthographic camera with a viewport of
		// 48 meters in width and 32 meters in height.
		// We also position the camera so that it
		// looks at (0,16) (that's where the middle of the
		// screen will be located).
		camera = new OrthographicCamera(48, 32);
		camera.position.set(0, 15, 0);

		// create the debug renderer
		renderer = new Box2DDebugRenderer();

		// create the world
		world = new World(new Vector2(0, -10), true);

		// we also need an invisible zero size ground body
		// to which we can connect the mouse joint
		BodyDef bodyDef = new BodyDef();
		groundBody = world.createBody(bodyDef);

		// Create a polygon shape
		PolygonShape groundBox = new PolygonShape();
		// Set the polygon shape as a box which is twice the size of our view
		// port and 20 high
		// (setAsBox takes half-width and half-height as arguments)
		groundBox.setAsBox(camera.viewportWidth, 10.0f);
		// Create a fixture from our polygon shape and add it to our ground body
		groundBody.createFixture(groundBox, 0.0f);
		// Clean up after ourselves
		groundBox.dispose();

		// call abstract method to populate the world
		createWorld(world);

		batch = new SpriteBatch();
		font = new BitmapFont();
		Gdx.input.setInputProcessor(InputAdapter);
	}

	public void createWorld(World world) {
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		// We set our body to dynamic, for something like ground which doesn't
		// move we would set it to StaticBody
		bodyDef.type = BodyType.DynamicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(0, 15);
		bodyDef.bullet = true;
		
		// Create our body in the world using our body definition
		Body circleBody = world.createBody(bodyDef);

		// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(1f);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 10f;
		fixtureDef.restitution = 0f; // Make it bounce a little bit

		// Create our fixture and attach it to the body
		circleBody.createFixture(fixtureDef);
		circleBody.setFixedRotation(true);

		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		circle.dispose();

		bodyDef.position.set(-1.5f, 20);
		bodyDef.bullet = true;
		
		// Create our body in the world using our body definition
		squareBody = world.createBody(bodyDef);
		PolygonShape rectangle = new PolygonShape();
		rectangle.setAsBox(1f, 2f);

		FixtureDef rectFixtureDef = new FixtureDef();
		rectFixtureDef.shape = rectangle;
		rectFixtureDef.density = 0.5f;
		rectFixtureDef.friction = 10f;
		rectFixtureDef.restitution = 0f;

		squareBody.createFixture(rectFixtureDef);
		squareBody.setFixedRotation(true);
		rectangle.dispose();
	}

	@Override
	public void render() {

		Vector2 vel = squareBody.getLinearVelocity();
		Vector2 pos = squareBody.getPosition();

		if (keyLeft) {
			//squareBody.applyLinearImpulse(-1.0f, 0, pos.x, pos.y, true);
			squareBody.setLinearVelocity(-10f, squareBody.getLinearVelocity().y);
			// squareBody.applyForceToCenter(-1.0f, 0.0f, true);
		} else if (keyRight) {
			//squareBody.applyLinearImpulse(1f, 0, pos.x, pos.y, true);
			squareBody.setLinearVelocity(10f, squareBody.getLinearVelocity().y);
			// squareBody.applyForceToCenter(1.0f, 1.0f, true);
		} else {
			//squareBody.applyLinearImpulse(0f, 0, pos.x, pos.y, true);
			squareBody.setLinearVelocity(0, squareBody.getLinearVelocity().y);

		}

		// update the world with a fixed time step
		long startTime = TimeUtils.nanoTime();
		world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
		float updateTime = (TimeUtils.nanoTime() - startTime) / 1000000000.0f;

		startTime = TimeUtils.nanoTime();
		// clear the screen and setup the projection matrix
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		// render the world using the debug renderer
		renderer.render(world, camera.combined);
		float renderTime = (TimeUtils.nanoTime() - startTime) / 1000000000.0f;

		batch.begin();
		font.draw(batch, "fps:" + Gdx.graphics.getFramesPerSecond() + ", update: " + updateTime + ", render: " + renderTime, 0, 20);
		batch.end();
	}
}
