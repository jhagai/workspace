package fr.heyjojo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import fr.heyjojo.game.Collision.BlockType;
import fr.heyjojo.game.Element.ElementType;

public class IntersectorActor extends Actor {

	private Array<Element> obstacles;

	private boolean checkCollisions;

	Collision evaluatedCollision = new Collision();

	MyRectangle rect1 = new MyRectangle();
	MyRectangle rect2 = new MyRectangle();

	MyRectangle rect3 = new MyRectangle();
	MyRectangle rect4 = new MyRectangle();

	private BlockType elementABlocType;
	private BlockType elementBBlocType;

	boolean doUpdateElementAPosition;
	boolean doUpdateElementBPosition;

	Array<Element> dynamicElements = new Array<Element>();

	public IntersectorActor() {
		super();
	}

	public static enum CollisionType {
		HORIZONTAL, VERTICAL, BOTH;
	}

	public enum CollisionStep {
		PREPARE, COLLIDE, AFTER
	}

	private CollisionStep collisionStep = CollisionStep.PREPARE;

	float currentCollisionTime;

	boolean stepByStep = true;

	@Override
	public void act(float delta) {

		if (checkCollisions) {
			
			
			
			if (!stepByStep || collisionStep == CollisionStep.PREPARE) {
				// PREPARE
				for (Element dynaElement : dynamicElements) {
					dynaElement.prepareForCollisionDetection();
					
					MyRectangle prev = dynaElement.getPreviousBounds();
					MyRectangle curr = dynaElement.getCurrentBounds();
					
					Gdx.app.debug("Start dynaElement","-------------");
					debugRect(prev);
					debugRect(curr);
					Gdx.app.debug("End dynaElement","-------------");
					
				}

				for (Element obstacle : obstacles) {
					obstacle.prepareForCollisionDetection();
					
					MyRectangle prev = obstacle.getPreviousBounds();
					MyRectangle curr = obstacle.getCurrentBounds();
					
					Gdx.app.debug("Start obstacle","-------------");
					debugRect(prev);
					debugRect(curr);
					Gdx.app.debug("End obstacle","-------------");
				}
				currentCollisionTime = 0f;
				collisionStep = CollisionStep.COLLIDE;
			}

			if (!stepByStep || collisionStep == CollisionStep.COLLIDE) {

				Collision closestCollison;
				boolean hasMoreThanOneCollision;
				
				do {
					// FIND CLOSEST
					closestCollison = null;
					hasMoreThanOneCollision = false;
					for (int i = 0; i < dynamicElements.size; i++) {
						Element dynaElementA = dynamicElements.get(i);
						for (int j = i + 1; j < dynamicElements.size; j++) {
							Element dynaElementB = dynamicElements.get(j);
							if (evalCollision(dynaElementA, dynaElementB, currentCollisionTime) && (evaluatedCollision.doUpdateElementAPosition || evaluatedCollision.doUpdateElementBPosition)) {
								if (closestCollison == null) {
									closestCollison = new Collision(evaluatedCollision);
								} else if (closestCollison.getTime() >= evaluatedCollision.getTime()) {
									closestCollison = new Collision(evaluatedCollision);
									hasMoreThanOneCollision = true;
								}

							}
						}
						// @TODO Optimiser le traitement des élements statiques
						for (Element staticElementB : obstacles) {
							if (evalCollision(dynaElementA, staticElementB, currentCollisionTime) && (evaluatedCollision.doUpdateElementAPosition || evaluatedCollision.doUpdateElementBPosition)) {

								if (closestCollison == null) {
									closestCollison = new Collision(evaluatedCollision);
								} else if (closestCollison.getTime() >= evaluatedCollision.getTime()) {
									closestCollison = new Collision(evaluatedCollision);
									hasMoreThanOneCollision = true;
								}
							}
						}
					}

					// APPLY COLLISION
					if (closestCollison != null) {
						currentCollisionTime = closestCollison.getTime();

						closestCollison.elementA.updateBoundsAfterCollision(closestCollison.rectA, closestCollison.rectC, closestCollison.time,
								closestCollison.getElementABlocType());
						closestCollison.elementB.updateBoundsAfterCollision(closestCollison.rectB, closestCollison.rectD, closestCollison.time,
								closestCollison.getElementBBlocType());

						if (!hasMoreThanOneCollision && (closestCollison.doUpdateElementAPosition || closestCollison.doUpdateElementBPosition)) {
							if (closestCollison.doUpdateElementAPosition) {
							}
							if (closestCollison.doUpdateElementBPosition) {

							}
						} else {

						}

					}
					if (stepByStep) {
						for (Element dynaElement : dynamicElements) {
							dynaElement.updatePreviousBoundsAtPercent(currentCollisionTime);
						}

						for (Element element : obstacles) {
							element.updatePreviousBoundsAtPercent(currentCollisionTime);
						}
						if (closestCollison != null
								&& ((closestCollison.doUpdateElementAPosition || closestCollison.doUpdateElementBPosition) || hasMoreThanOneCollision)) {
							break;
						} else {
							collisionStep = CollisionStep.AFTER;
						}
						
					}

				} while (closestCollison != null
						&& ((closestCollison.doUpdateElementAPosition || closestCollison.doUpdateElementBPosition) || hasMoreThanOneCollision));

			}

			if (!stepByStep || collisionStep == CollisionStep.AFTER) {

				for (Element dynaElement : dynamicElements) {
					dynaElement.afterCollisionDetection();
				}

				for (Element element : obstacles) {
					element.afterCollisionDetection();
				}
				collisionStep = CollisionStep.PREPARE;
			}

		}
		checkCollisions = false;
	}

	private void debugRect(MyRectangle rect) {
		Gdx.app.debug("Rect", "x=" + rect.getX() + ";y=" + rect.getY() + ";width=" + rect.getWidth() + ";height=" +rect.getHeight() );
	}

	private boolean evalCollision(Element elementA, Element elementB, float startTime) {

		boolean doCollide = false;

		if (elementA.getCurrentCollisionTime() < startTime) {
			elementA.updatePreviousBoundsAtPercent(startTime);
		}
		if (elementB.getCurrentCollisionTime() < startTime) {
			elementB.updatePreviousBoundsAtPercent(startTime);
		}

		if (elementA.getMovingBounds().overlaps(elementB.getMovingBounds())) {

			Float percent;

			if ((percent = checkHorizontaleCollision(elementB, elementA, rect2, rect1, rect4, rect3)) != null) {
				evaluatedCollision.init(elementA, elementB, percent, CollisionType.HORIZONTAL, rect1, rect2, rect3, rect4, elementBBlocType, elementABlocType,
						doUpdateElementAPosition, doUpdateElementBPosition);
				doCollide = true;
			} else if ((percent = checkHorizontaleCollision(elementA, elementB, rect1, rect2, rect3, rect4)) != null) {
				evaluatedCollision.init(elementA, elementB, percent, CollisionType.HORIZONTAL, rect1, rect2, rect3, rect4, elementABlocType, elementBBlocType,
						doUpdateElementAPosition, doUpdateElementBPosition);
				doCollide = true;
			} else if ((percent = checkVerticaleCollision(elementB, elementA, rect2, rect1, rect4, rect3)) != null) {
				evaluatedCollision.init(elementA, elementB, percent, CollisionType.VERTICAL, rect1, rect2, rect3, rect4, elementBBlocType, elementABlocType,
						doUpdateElementAPosition, doUpdateElementBPosition);
				doCollide = true;
			} else if ((percent = checkVerticaleCollision(elementA, elementB, rect1, rect2, rect3, rect4)) != null) {
				evaluatedCollision.init(elementA, elementB, percent, CollisionType.VERTICAL, rect1, rect2, rect3, rect4, elementABlocType, elementBBlocType,
						doUpdateElementAPosition, doUpdateElementBPosition);
				doCollide = true;
			}

			if (percent != null) {
				// Une collision a eu lieu => arrête le calcule
				evaluatedCollision.setTime(startTime + (percent * (1 - startTime)));
			}
		}

		return doCollide;
	}

	/**
	 * Vérifie si le coté droit de l'élément A rentre en collision avec le coté
	 * gauche de l'élément B.
	 * 
	 * @param elementA
	 * @param elementB
	 * @param rectiA
	 * @param rectiB
	 * @return
	 */
	Float checkHorizontaleCollision(Element elementA, Element elementB, MyRectangle rectiA, MyRectangle rectiB, MyRectangle rectiC, MyRectangle rectiD) {

		Float result = null;

		if (elementA.getPreviousBounds().getXRight() <= elementB.getPreviousBounds().getX()
				&& elementA.getCurrentBounds().getXRight() > elementB.getCurrentBounds().getX()) {
			float percent = calcHorizontaleCollision(elementA, elementB, rectiA, rectiB);

			if ((rectiA.getYUp() > rectiB.getY() && rectiA.getY() < rectiB.getYUp())
					|| (rectiA.getYUp() == rectiB.getY() && elementA.getCurrentBounds().getYUp() > elementB.getCurrentBounds().getY())
					|| (rectiA.getY() == rectiB.getYUp() && elementA.getCurrentBounds().getY() < elementB.getCurrentBounds().getYUp())) {
				// Collision sur la gauche.
				result = percent;
				rectiC.set(elementA.getCurrentBounds());
				rectiD.set(elementB.getCurrentBounds());
				elementABlocType = null;
				elementBBlocType = null;
				doUpdateElementAPosition = false;
				doUpdateElementBPosition = false;
				if (elementA.getType() == ElementType.DYNAMIC && !elementA.blockedLeft) {
					if (elementB.blockedRight || elementB.getType() != ElementType.DYNAMIC) {
						// L'élément B ne peut pas être bougé => on ajuste
						// l'élément A par rapport au B
						rectiC.setX(elementB.getCurrentBounds().getX() - elementA.getCurrentBounds().getWidth());
						elementABlocType = BlockType.RIGHT;
						doUpdateElementAPosition = true;
					} else {
						// L'élément B peut être bougé, => compromis
						rectiC.setX(rectiA.getX());
						rectiD.setX(rectiB.getX());
						doUpdateElementAPosition = true;
						doUpdateElementBPosition = true;
					}
				} else if (elementB.getType() == ElementType.DYNAMIC && !elementB.blockedRight) {
					// l'élément A ne peut pas être bougé => on ajuste l'élément
					// B
					rectiD.setX(elementA.getCurrentBounds().getX() + elementA.getCurrentBounds().getWidth());
					elementBBlocType = BlockType.LEFT;
					doUpdateElementBPosition = true;
				}
			}
		}
		return result;
	}

	private float calcHorizontaleCollision(Element elementA, Element elementB, MyRectangle rectiA, MyRectangle rectiB) {
		float x0LeftB = elementB.getPreviousBounds().getX();
		float x0RightA = elementA.getPreviousBounds().getXRight();

		float deltaXRightA = elementA.getDeltaXRight();
		float deltaXLeftB = elementB.getDeltaXLeft();

		double percent = Math.abs((double)(x0LeftB - x0RightA) / (double)(deltaXRightA - deltaXLeftB));

		elementA.getBoundsAtPercent(percent, rectiA);
		elementB.getBoundsAtPercent(percent, rectiB);
		return (float)(percent);
	}

	/**
	 * Teste si le haut de l'élément A rentre en collision avec le bas de
	 * l'élément B.
	 * 
	 * @param elementA
	 * @param elementB
	 * @param rectiA
	 * @param rectiB
	 * @return
	 */
	Float checkVerticaleCollision(Element elementA, Element elementB, MyRectangle rectiA, MyRectangle rectiB, MyRectangle rectiC, MyRectangle rectiD) {

		Float result = null;

		if (elementA.getPreviousBounds().getYUp() <= elementB.getPreviousBounds().getY()
				&& elementA.getCurrentBounds().getYUp() > elementB.getCurrentBounds().getY()) {
			float percent = calcVerticalCollision(elementA, elementB, rectiA, rectiB);

			if ((rectiA.getXRight() > rectiB.getX() && rectiA.getX() < rectiB.getXRight())
					|| (rectiA.getXRight() == rectiB.getX() && elementA.getCurrentBounds().getXRight() > rectiB.getX())
					|| (rectiA.getX() == rectiB.getXRight() && elementA.getCurrentBounds().getX() < rectiB.getXRight())) {
				result = percent;
				rectiC.set(elementA.getCurrentBounds());
				rectiD.set(elementB.getCurrentBounds());
				elementABlocType = null;
				elementBBlocType = null;
				doUpdateElementAPosition = false;
				doUpdateElementBPosition = false;
				if (elementA.getType() == ElementType.DYNAMIC && !elementA.blockedDown) {
					if (elementB.blockedUp || elementB.getType() != ElementType.DYNAMIC) {
						// L'élément B ne peut pas être bougé => on ajuste
						// l'élément A par rapport au B
						rectiC.setY(elementB.getCurrentBounds().getY() - elementA.getCurrentBounds().getHeight());
						elementABlocType = BlockType.UP;
						doUpdateElementAPosition = true;
					} else {
						// L'élément B peut être bougé, => compromis
						rectiC.setY(rectiA.getY());
						rectiD.setY(rectiB.getY());
						doUpdateElementAPosition = true;
						doUpdateElementBPosition = true;
					}
				} else if (elementB.getType() == ElementType.DYNAMIC && !elementB.blockedUp) {
					// l'élément A ne peut pas être bougé => on ajuste l'élément
					// B
					rectiD.setY(elementA.getCurrentBounds().getY() + elementA.getCurrentBounds().getHeight());
					elementBBlocType = BlockType.DOWN;
					doUpdateElementBPosition = true;
				}
			}

		}

		return result;
	}

	private float calcVerticalCollision(Element elementA, Element elementB, MyRectangle rectiA, MyRectangle rectiB) {
		float y0DownB = elementB.getPreviousBounds().getY();
		float y0UpA = elementA.getPreviousBounds().getYUp();

		float deltaYUpA = elementA.getDeltaYUp();
		float deltaYDownB = elementB.getDeltaYDown();

		float percent = Math.abs((y0DownB - y0UpA) / (deltaYUpA - deltaYDownB));

		elementA.getBoundsAtPercent(percent, rectiA);
		elementB.getBoundsAtPercent(percent, rectiB);
		return percent;
	}

	public void setCheckCollisions(boolean checkCollisions) {
		this.checkCollisions = checkCollisions;
	}

	public void setDynamicElements(Array<Element> dynamicElements) {
		this.dynamicElements = dynamicElements;
	}

	public Array<Element> getObstacles() {
		return obstacles;
	}

	public void setObstacles(Array<Element> obstacles) {
		this.obstacles = obstacles;
	}

}
