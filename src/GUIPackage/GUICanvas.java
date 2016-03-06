package GUIPackage;

import Model.Turtle;
import Controller.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

import java.io.File;

/**
 * Returns one Node that contains the Turtle Canvas and two ColorPicker objects
 * (for background and pen)
 *
 * @author AnnieTang
 */

public class GUICanvas{
	private static final int TURTLE_SIZE = 20;
	private static final int CANVAS_WIDTH = 500;
	private static final int CANVAS_HEIGHT = 500;
	private static final int STARTING_X = CANVAS_WIDTH/2 - TURTLE_SIZE/2;
	private static final int STARTING_Y = CANVAS_HEIGHT/2 - TURTLE_SIZE/2;
	private static final String PATH_DELIMITER = "/";
	private static final String DEFAULT_TURTLE = "turtle_outline.png";
	private double myX;
	private double myY;
	private double myOldX;
	private double myOldY;
	private double myOldDirection;
	private Canvas canvasBackground;
	private Pane myRoot;
	private GraphicsContext gcBackground;
	private GraphicsContext gcDrawing;
	private GraphicsContext gc;
	private Map<TurtleObserver, List<GraphicsContext>> myTurtles;
	private List<Double[]> oldTurtleCoordinates;
	private Image turtleImage;
	
	public GUICanvas() {
	}
	
	
	public void init() {
		myTurtles = new HashMap<TurtleObserver, List<GraphicsContext>>();
		oldTurtleCoordinates = new ArrayList<Double[]>();
		canvasBackground = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		gcBackground = canvasBackground.getGraphicsContext2D();
		gcBackground.setFill(Color.BISQUE);
		gcBackground.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		addDefaultTurtles();
		myRoot = new Pane(canvasBackground);
	}
	/**
	 * Creates the Canvas Node to be displayed.
	 * @return Canvas Node
	 */
	public Node createNode() {
		return myRoot;
	}
	
	/**
	 * Updates the Canvas when the Turtle is updated.
	 */
	public void update(TurtleObserver turtle) {
		if (turtle.shouldReset()) {
			resetCanvas(turtle);
		} else {
			addTurtleToMap(turtle);
			clearPreviousTurtle(turtle);
			drawTurtle(turtle);
		}
	}
	
	/**
	 * Resets Canvas. Deletes all of the Turtle's trails and changes the Turtle back to default.
	 */
	public void resetCanvas(TurtleObserver turtle) {
		myTurtles.get(turtle).get(0).clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		myTurtles.get(turtle).get(1).clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		drawTurtle(turtle);
	}
	
	/**
	 * Keeps track of the old coordinates after updating the Turtle.
	 */
	public void setOldCoordinates(TurtleObserver turtle, double x, double y) {
		Double[] coordinates = new Double[2];
		coordinates[0] = x;
		coordinates[1] = y;
		if (oldTurtleCoordinates.size() < turtle.getID() + 1)
			oldTurtleCoordinates.add((int) turtle.getID(), coordinates);
		else oldTurtleCoordinates.set((int) turtle.getID(), coordinates);
	}
	
	/**
	 * Places default turtle on canvas.
	 */
	private void addDefaultTurtles(){
//		for (Turtle t: myTurtles.keySet()) {
		turtleImage = new Image(getClass().getClassLoader().getResourceAsStream(DEFAULT_TURTLE));
//			GraphicsContext turtleGC = myTurtles.get(t).get(0);
//			turtleGC.drawImage(turtleImage, myX, myY, TURTLE_SIZE, TURTLE_SIZE);
//		setOldCoordinates(t, myX, myY);
//		}
	}
	
	private void addTurtleToMap(TurtleObserver turtle){
		if (!myTurtles.containsKey(turtle)) {
			Canvas turtleCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
			Canvas drawingCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
			myRoot.getChildren().addAll(turtleCanvas, drawingCanvas);
			myTurtles.put(turtle, new ArrayList<GraphicsContext>(
					Arrays.asList(turtleCanvas.getGraphicsContext2D(),
							drawingCanvas.getGraphicsContext2D())));
			int myX = STARTING_X;
			int myY = STARTING_Y;
			setOldCoordinates(turtle, myX, myY);
		}
	}
	
	/**
	 * Sets user-inputed image as the Canvas turtle.
	 * @param file that contains image
	 */
	public void setTurtleImage(File file){
		String filePath = file.getPath();
		String[] splitFilePath = filePath.split(PATH_DELIMITER);
		String fileName = splitFilePath[splitFilePath.length - 1];
		turtleImage = new Image(getClass().getClassLoader().getResourceAsStream(fileName));
//		if (turtle == null) 
//			gc.drawImage(turtleImage, myX, myY, TURTLE_SIZE, TURTLE_SIZE);
//		else drawTurtle();
	}
	
	public void setTurtleImage(Image image){
		turtleImage = image;
//		if (turtle == null) 
//			gc.drawImage(turtleImage, myX, myY, TURTLE_SIZE, TURTLE_SIZE);
//		else drawTurtle();
	}
	
	/**
	 * Clears the previous instance of the Turtle on the canvas.
	 */
	public void clearPreviousTurtle(TurtleObserver turtle) {
		GraphicsContext gc = myTurtles.get(turtle).get(0);
		gc.save(); // saves the current state on stack, including the current transform
		Rotate r = new Rotate(myOldDirection, myOldX + TURTLE_SIZE/2, myOldY + TURTLE_SIZE/2);
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
		gc.clearRect(oldTurtleCoordinates.get((int) turtle.getID())[0].doubleValue(),
				oldTurtleCoordinates.get((int) turtle.getID())[1].doubleValue(), TURTLE_SIZE, TURTLE_SIZE);
		gc.restore();
	}

	/**
	 * Draws the turtle onto canvas based on turtle's X and Y values and its direction.
	 */
	public void drawTurtle(TurtleObserver turtle) {
		GraphicsContext gc = myTurtles.get(turtle).get(0);
		GraphicsContext gcDrawing = myTurtles.get(turtle).get(1);
		double myX = turtle.getX() + CANVAS_WIDTH/2 - TURTLE_SIZE/2;
		double myY = -(turtle.getY() - CANVAS_HEIGHT/2 + TURTLE_SIZE/2);
		gc.save(); // saves the current state on stack, including the current transform
		Rotate r = new Rotate(turtle.getDirection(), myX + TURTLE_SIZE/2, myY + TURTLE_SIZE/2);
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
		if (turtle.getShowing()) {
			gc.drawImage(turtleImage, myX, myY, TURTLE_SIZE, TURTLE_SIZE);
		}
		if (turtle.getPenDown()) {
			gcDrawing.fillOval(myX + TURTLE_SIZE/2, myY + TURTLE_SIZE/2, 3, 3);
		}
		gc.restore();
		setOldCoordinates(turtle, myX, myY);
		myOldDirection = turtle.getDirection();
	}
	
	/**
	 * @return GraphicsContext for Canvas Background
	 */
	public GraphicsContext getBackgroundGraphicsContext(){
		return gcBackground;
	}
	
	/**
	 * Sets Pen color based on User preference.
	 * @param Color that user chose.
	 */
	protected void setPenColor(Color c) {
		gcDrawing.setFill(c);
	}
	
	public int getWidth(){
		return CANVAS_WIDTH;
	}
	
	public int getHeight(){
		return CANVAS_HEIGHT;
	}
	
//	public String getCoordinateString(){
//		return Math.round(turtle.getCurX()) + "," + Math.round(turtle.getCurY());
//	}
	
	public String getHeadingString(){
		return "" + myOldDirection%360;
	}
	
//	public boolean getPenDownStatus(){
//		return !turtle.isPenUp();
//	}

}
