package fr.heyjojo.game;


public class Bounds {
	MyRectangle previousBounds;
	MyRectangle currentBounds;
	MyRectangle movingBounds;

	public Bounds() {
		super();
		currentBounds = new MyRectangle();
		previousBounds = new MyRectangle();
		movingBounds = new MyRectangle();
	}

	public void calcMovingBounds() {

		final float minX = Math
				.min(currentBounds.getX(), previousBounds.getX());
		final float minY = Math
				.min(currentBounds.getY(), previousBounds.getY());
		final float maxX = Math.max(currentBounds.getXRight(),
				previousBounds.getXRight());
		final float maxY = Math
				.max(currentBounds.getYUp(), previousBounds.getYUp());

		movingBounds.set(minX, minY, maxX - minX, maxY - minY);
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

	public MyRectangle getBoundsAtPercent(final float percent,
			MyRectangle rectangle) {
		rectangle.set(previousBounds.getX() + (percent * getDeltaX()),
				previousBounds.getY() + (percent * getDeltaY()),
				getWidthAtPercent(percent), getHeightAtPercent(percent));
		return rectangle;
	}

	public float getWidthAtPercent(final float percent) {
		if (getDeltaWidth() != 0) {
			return previousBounds.getWidth() + (getDeltaWidth() * percent);
		} else {
			return currentBounds.getWidth();
		}
	}

	public float getHeightAtPercent(final float percent) {
		if (getDeltaHeight() != 0) {
			return previousBounds.getHeight() + (getDeltaHeight() * percent);
		} else {
			return currentBounds.getHeight();
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

}
