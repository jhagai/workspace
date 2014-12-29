package fr.heyjojo.game;


public class MyRectangle {
	private float x;

	private float y;

	private float width;

	private float height;

	public void set(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void set(MyRectangle rect) {
		set(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}
	
	public boolean contains (float x, float y) {
		return this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;
	}
	
	public boolean overlaps (MyRectangle r) {
		return x < r.x + r.width && x + width > r.x && y < r.y + r.height && y + height > r.y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getXRight() {
		return x + width;
	}

	public float getYUp() {
		return y + height;
	}
}
