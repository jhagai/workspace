package fr.heyjojo.game;

import fr.heyjojo.game.Collision.BlockType;

public class Element {

	private MyRectangle currentBounds;

	private MyRectangle previousBounds;

	private MyRectangle movingBounds;

	private double currentCollisionTime;

	public boolean blockedLeft;
	public boolean blockedRight;
	public boolean blockedUp;
	public boolean blockedDown;

	public enum ElementType {
		STATIC, DYNAMIC, KINEMATIC;
	}

	final ElementType type;

	public Element(final ElementType type) {
		super();
		currentBounds = new MyRectangle();
		previousBounds = new MyRectangle();
		movingBounds = new MyRectangle();
		this.type = type;
	}

	public void init(float x, float y, float width, float height) {
		currentBounds.set(x, y, width, height);
		previousBounds.set(currentBounds);
	}

	public MyRectangle getMovingBounds() {
		return movingBounds;
	}

	public void setMovingBounds(MyRectangle movingBounds) {
		this.movingBounds = movingBounds;
	}

	public MyRectangle getCurrentBounds() {
		return currentBounds;
	}

	public MyRectangle getPreviousBounds() {
		return previousBounds;
	}

	public float getDeltaX() {
		return currentBounds.getX() - previousBounds.getX();
	}

	public float getDeltaY() {
		return currentBounds.getY() - previousBounds.getY();
	}

	public float getDeltaWidth() {
		return currentBounds.getWidth() - previousBounds.getWidth();
	}

	public float getDeltaHeight() {
		return currentBounds.getHeight() - previousBounds.getHeight();
	}

	public ElementType getType() {
		return type;
	}

	public MyRectangle getBoundsAtGlobalPercent(final double percent, MyRectangle rectangle) {

		double newLocalPercent = (percent - currentCollisionTime) / (1 - currentCollisionTime);

		rectangle.set((float) (previousBounds.getX() + (newLocalPercent * getDeltaX())), (float) (previousBounds.getY() + (newLocalPercent * getDeltaY())),
				getWidthAtPercent(newLocalPercent), getHeightAtPercent(newLocalPercent));

		return rectangle;
	}

	public void updatePreviousBoundsAtPercent(final double percent) {
		getBoundsAtGlobalPercent(percent, previousBounds);
		currentCollisionTime = percent;
		calcMovingBounds();
	}

	public MyRectangle getBoundsAtPercent(final double percent, MyRectangle rectangle) {

		rectangle.set((float) (previousBounds.getX() + (percent * getDeltaX())), (float) (previousBounds.getY() + (percent * getDeltaY())),
				getWidthAtPercent(percent), getHeightAtPercent(percent));

		return rectangle;
	}

	public MyRectangle getBoundsAtPercent(final float percent, MyRectangle rectangle) {

		rectangle.set((float) (previousBounds.getX() + (percent * getDeltaX())), (float) (previousBounds.getY() + (percent * getDeltaY())),
				getWidthAtPercent(percent), getHeightAtPercent(percent));

		return rectangle;
	}

	public float getWidthAtPercent(final double percent) {
		if (getDeltaWidth() != 0) {
			return (float) (previousBounds.getWidth() + (getDeltaWidth() * percent));
		} else {
			return currentBounds.getWidth();
		}
	}

	private float getWidthAtPercent(final float percent) {
		if (getDeltaWidth() != 0) {
			return (float) (previousBounds.getWidth() + (getDeltaWidth() * percent));
		} else {
			return currentBounds.getWidth();
		}
	}

	public float getHeightAtPercent(final double percent) {
		if (getDeltaHeight() != 0) {
			return (float) (previousBounds.getHeight() + (getDeltaHeight() * percent));
		} else {
			return currentBounds.getHeight();
		}
	}

	private float getHeightAtPercent(final float percent) {
		if (getDeltaHeight() != 0) {
			return (float) (previousBounds.getHeight() + (getDeltaHeight() * percent));
		} else {
			return currentBounds.getHeight();
		}
	}

	public void calcMovingBounds() {
		calcMovingBounds(previousBounds, currentBounds, movingBounds);
	}

	public static void calcMovingBounds(MyRectangle previous, MyRectangle current, MyRectangle move) {

		final float minX = Math.min(current.getX(), previous.getX());
		final float minY = Math.min(current.getY(), previous.getY());
		final float maxX = Math.max(current.getXRight(), previous.getXRight());
		final float maxY = Math.max(current.getYUp(), previous.getYUp());

		move.set(minX, minY, maxX - minX, maxY - minY);
	}

	public void updatePreviousBounds(MyRectangle newPreviousRect) {
		previousBounds.set(newPreviousRect);
		calcMovingBounds();
	}

	public void updateBoundsAfterCollision(MyRectangle newPreviousRect, MyRectangle newCurrentRect, double collisionTime, BlockType blockType) {
		currentBounds.set(newCurrentRect);
		previousBounds.set(newPreviousRect);

		calcMovingBounds();

		this.currentCollisionTime = collisionTime;

		if (blockType != null) {
			switch (blockType) {
			case DOWN:
				blockedDown = true;
				break;
			case LEFT:
				blockedLeft = true;
				break;
			case RIGHT:
				blockedRight = true;
				break;
			case UP:
				blockedUp = true;
				break;
			default:
				break;
			}
		}

	}

	public float getDeltaXLeft() {
		return getDeltaX();
	}

	public float getDeltaXRight() {
		return currentBounds.getXRight() - previousBounds.getXRight();
	}

	public float getDeltaYDown() {
		return getDeltaY();
	}

	public float getDeltaYUp() {
		return currentBounds.getYUp() - previousBounds.getYUp();
	}

	public void prepareForCollisionDetection() {
		currentCollisionTime = 0f;
		blockedLeft = false;
		blockedRight = false;
		blockedUp = false;
		blockedDown = false;
		calcMovingBounds();
	}

	public void afterCollisionDetection() {
		previousBounds.set(currentBounds);
	}

	public double getCurrentCollisionTime() {
		return currentCollisionTime;
	}

	public void setCurrentCollisionTime(double currentCollisionTime) {
		this.currentCollisionTime = currentCollisionTime;
	}

}
