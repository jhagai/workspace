package fr.heyjojo.game;

import fr.heyjojo.game.IntersectorActor.CollisionType;

public class Collision {
	public Element elementA;
	public Element elementB;

	public Float time;

	public MyRectangle rectA = new MyRectangle();
	public MyRectangle rectB = new MyRectangle();

	public MyRectangle rectC = new MyRectangle();
	public MyRectangle rectD = new MyRectangle();

	public enum BlockType {
		LEFT, RIGHT, UP, DOWN;
	}

	public BlockType elementABlocType;

	public BlockType elementBBlocType;

	public Float timeInter;

	public CollisionType collisionType;

	public boolean doUpdateElementAPosition;

	public boolean doUpdateElementBPosition;

	public Collision(Element elementA, Element elementB) {
		init(elementA, elementB, null, null, null, null, null, null, null, null, false, false);
	}

	public Collision(Collision collision) {
		init(collision);
	}

	public Collision() {
	}

	public void init(Element elementA, Element elementB, Float time, CollisionType collisionType, MyRectangle rectA, MyRectangle rectB, MyRectangle rectC,
			MyRectangle rectD, BlockType elementABlocType, BlockType elementBBlocType, boolean doUpdateElementAPosition, boolean doUpdateElementBPosition) {
		this.elementA = elementA;
		this.elementB = elementB;
		this.time = time;
		this.collisionType = collisionType;
		this.rectA.set(rectA);
		this.rectB.set(rectB);
		this.rectC.set(rectC);
		this.rectD.set(rectD);
		this.elementABlocType = elementABlocType;
		this.elementBBlocType = elementBBlocType;
		this.doUpdateElementAPosition = doUpdateElementAPosition;
		this.doUpdateElementBPosition = doUpdateElementBPosition;

	}

	public void init(Collision collision) {
		init(collision.elementA, collision.elementB, collision.time, collision.collisionType, collision.rectA, collision.rectB, collision.rectC,
				collision.rectD, collision.elementABlocType, collision.elementBBlocType, collision.doUpdateElementAPosition, collision.doUpdateElementBPosition);
	}

	public Element getElementA() {
		return elementA;
	}

	public void setElementA(Element elementA) {
		this.elementA = elementA;
	}

	public Element getElementB() {
		return elementB;
	}

	public void setElementB(Element elementB) {
		this.elementB = elementB;
	}

	public CollisionType getCollisionType() {
		return collisionType;
	}

	public void setCollisionType(CollisionType collisionType) {
		this.collisionType = collisionType;
	}

	public void setTime(Float time) {
		this.time = time;
	}

	public Float getTime() {
		return time;
	}

	public MyRectangle getRectA() {
		return rectA;
	}

	public void setRectA(MyRectangle rectA) {
		this.rectA = rectA;
	}

	public MyRectangle getRectB() {
		return rectB;
	}

	public void setRectB(MyRectangle rectB) {
		this.rectB = rectB;
	}

	public MyRectangle getRectC() {
		return rectC;
	}

	public void setRectC(MyRectangle rectC) {
		this.rectC = rectC;
	}

	public MyRectangle getRectD() {
		return rectD;
	}

	public void setRectD(MyRectangle rectD) {
		this.rectD = rectD;
	}

	public BlockType getElementABlocType() {
		return elementABlocType;
	}

	public void setElementABlocType(BlockType elementABlocType) {
		this.elementABlocType = elementABlocType;
	}

	public BlockType getElementBBlocType() {
		return elementBBlocType;
	}

	public void setElementBBlocType(BlockType elementBBlocType) {
		this.elementBBlocType = elementBBlocType;
	}

}
