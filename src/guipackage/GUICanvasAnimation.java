package guipackage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Animation.Status;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class GUICanvasAnimation {
	private static final double ANIMATION_THRESHOLD = 0.5;
	private static final int DEFAULT_ROTATE_SPEED = 1000;
	private static final int DEFAULT_PATH_SPEED = 15;
	private Queue<Animation> myAnimationQueue;
	private Animation myAnimation;
	private double pathSpeed;
	private double rotateSpeed;
	private boolean animate;
	
	public GUICanvasAnimation() {
		myAnimation = new SequentialTransition();
		myAnimationQueue = new LinkedList<Animation>();
		pathSpeed = DEFAULT_PATH_SPEED;
		rotateSpeed = DEFAULT_ROTATE_SPEED;
		animate = true;
	}
	
	protected void runAnimation() {
		if (myAnimation.getStatus() == Status.STOPPED) {
			playNextAnimation();
		}
	}
	
	protected void addAnimation(Node agent, double oldX, double oldY,
			double x, double y, double direction) {
		SequentialTransition newAnimation = new SequentialTransition();
		
		Path path = new Path();
		path.getElements().addAll(new MoveTo(oldX, oldY), new LineTo(x, y));
		if (oldX != x || oldY != y) {
			PathTransition pt = new PathTransition(Duration.millis(pathSpeed), path, agent);
			newAnimation.getChildren().add(pt);
		}
		
		if (direction != 0) {
			RotateTransition rt = new RotateTransition(Duration.millis(rotateSpeed), agent);
			rt.setByAngle((int) direction);
			newAnimation.getChildren().add(rt);
		}
		newAnimation.setOnFinished(event -> playNextAnimation());
		myAnimationQueue.add(newAnimation);
		runAnimation();
	}
	
	protected void playNextAnimation() {
		if (!myAnimationQueue.isEmpty()) {
			myAnimation = myAnimationQueue.poll();
			play();
		}
	}
	
	protected void setSpeed(double multiplier) {
		if (multiplier <= ANIMATION_THRESHOLD) {
			animate = false;
		} else {
			animate = true;
			pathSpeed = DEFAULT_PATH_SPEED * multiplier;
			rotateSpeed = DEFAULT_ROTATE_SPEED * multiplier;
		}
	}
	
	protected void play() {
		myAnimation.play();
	}
	
	protected boolean willAnimate() {
		return animate;
	}
}
