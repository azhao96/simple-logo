package guipackage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import model.Turtle;


/**
 * Returns one Node that contains the Turtle Canvas and two ColorPicker objects
 * (for background and pen)
 *
 * @author AnnieTang
 */

public class GUICanvas implements Observer{
	private static final String DOTTED_LINE = "Dotted Line";
	private static final String DASHED_LINE = "Dashed Line";
	private static final String SOLID_LINE = "Solid Line";
	private static final Color DEFAULT_BACKGROUND_COLOR = Color.BISQUE;
	private static final int TURTLE_SIZE = 20;
	private static final int CANVAS_WIDTH = 500;
	private static final int CANVAS_HEIGHT = 500;
	private static final int STARTING_X = CANVAS_WIDTH/2 - TURTLE_SIZE/2;
	private static final int STARTING_Y = CANVAS_HEIGHT/2 - TURTLE_SIZE/2;	
	private static final String DEFAULT_TURTLE = "turtle_outline.png";
	private static final int PEN_SCALE = 100;
	private static final int DEFAULT = 0;
	private Canvas canvasBackground;
	private Pane myCanvasRoot;
	private GraphicsContext gcBackground;
	private GraphicsContext gcStamps;
	private GraphicsContext gc;
	private Map<Turtle, List<GraphicsContext>> myTurtles;
	private List<Double[]> turtleParameters;	
	
	private ResourceBundle myResources;
	private GUICanvasPen myPen;
	private int penCounter;
	private String myBackgroundRGB;
	private Image turtleShape;
	private String turtleShapeName;
	private int myTurtleShapeIndex;
	private List<String> myPenPalette;
	private List<String> myBackgroundPalette;
	private List<String> myImagePalette;
	
	private HBox hbox;
	private GUICanvasRight canvasRight;
	private Group root;
	
	public GUICanvas(ResourceBundle myResources) {
		this.myResources = myResources;
		this.canvasRight = (GUICanvasRight) canvasRight;
		myTurtles = new HashMap<>();
		turtleParameters = new ArrayList<>();
		canvasBackground = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		gcBackground = canvasBackground.getGraphicsContext2D();
		gcBackground.setFill(DEFAULT_BACKGROUND_COLOR);
		gcBackground.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		turtleShape = new Image(getClass().getClassLoader().getResourceAsStream(DEFAULT_TURTLE));
		myPen = new GUICanvasPen();
		penCounter = 0;
		myCanvasRoot = new Pane(canvasBackground);
		hbox = new HBox();
		root = new Group(myCanvasRoot);
	}
	
	/**
	 * Creates the Canvas Node to be displayed.
	 * @return Canvas Node
	 */
	public Node createNode() {
		hbox.getChildren().add(root);
		return hbox;
	}
	/**
	 * Adds object to position right of the canvas.
	 * @param rightOfCanvas
	 */
	public void addObjectToRight(GUICanvasRight rightOfCanvas){
		canvasRight = rightOfCanvas;
		myPenPalette = canvasRight.getPenPalette();
		myBackgroundPalette = canvasRight.getBackgroundPalette();
		myImagePalette = canvasRight.getImagePalette();
		setDefaultShapeProperties();
		hbox.getChildren().add(canvasRight.createNode());
	}
	
	/**
	 * Sets default palette properties, such as pen settings, background color, and turtle image.
	 */
	public void setDefaultShapeProperties(){
		turtleShapeName = DEFAULT_TURTLE;
		myTurtleShapeIndex = getPaletteIndex(turtleShapeName, myImagePalette);
	}
	
	/**
	 * Updates the Canvas when the Turtle is updated.
	 */
	@Override
	public void update(Observable o, Object args) {
		Turtle turtle = (Turtle) o;
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
	public void resetCanvas(Turtle turtle) {
		myTurtles.get(turtle).get(0).clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		myTurtles.get(turtle).get(1).clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		drawTurtle(turtle);
		root.getChildren().remove(turtle.getImageView());
		turtle.doneResetting();
	}
	
	/**
	 * Keeps track of the old coordinates after updating the Turtle.
	 */
	public void setOldCoordinates(Turtle turtle, double x, double y, double direction) {
		Double[] coordinates = new Double[3];
		coordinates[0] = x;
		coordinates[1] = y;
		coordinates[2] = direction;
		if (turtleParameters.size() < turtle.getID() + 1)
			turtleParameters.add((int) turtle.getID(), coordinates);
		else turtleParameters.set((int) turtle.getID(), coordinates);
	}
	
	private void addTurtleToMap(Turtle turtle){
		if (!myTurtles.containsKey(turtle)) {
			Canvas turtleCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
			Canvas drawingCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
			GraphicsContext drawingGC = drawingCanvas.getGraphicsContext2D();
			drawingGC.setFill(myPen.getMyPenColor());
			myCanvasRoot.getChildren().addAll(turtleCanvas, drawingCanvas);
			myTurtles.put(turtle, new ArrayList<GraphicsContext>(
					Arrays.asList(turtleCanvas.getGraphicsContext2D(), drawingGC)));
			int myX = STARTING_X;
			int myY = STARTING_Y;
			setOldCoordinates(turtle, myX, myY, DEFAULT);
			turtle.setImageView(createTurtleImageView(turtle, myX, myY));
			root.getChildren().add(turtle.getImageView());
		}
	}
	
	/**
	 * Clears the previous instance of the Turtle on the canvas.
	 */
	public void clearPreviousTurtle(Turtle turtle) {
		GraphicsContext gc = myTurtles.get(turtle).get(0);
		double myOldX = turtleParameters.get((int) turtle.getID())[0].doubleValue();
		double myOldY = turtleParameters.get((int) turtle.getID())[1].doubleValue();
		double myOldDirection = turtleParameters.get((int) turtle.getID())[2].doubleValue();
		gc.save(); // saves the current state on stack, including the current transform
		Rotate r = new Rotate(myOldDirection, myOldX + TURTLE_SIZE/2, myOldY + TURTLE_SIZE/2);
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
		gc.clearRect(myOldX, myOldY, TURTLE_SIZE, TURTLE_SIZE);
		gc.restore();
	}

	/**
	 * Draws the turtle onto canvas based on turtle's X and Y values and its direction.
	 */
	public void drawTurtle(Turtle turtle) {
		GraphicsContext gc = myTurtles.get(turtle).get(0);
		GraphicsContext gcDrawing = myTurtles.get(turtle).get(1);
		double myX = turtle.getCurX() + CANVAS_WIDTH/2 - TURTLE_SIZE/2;
		double myY = -(turtle.getCurY() - CANVAS_HEIGHT/2 + TURTLE_SIZE/2);
		gc.save(); // saves the current state on stack, including the current transform
		Rotate r = new Rotate(turtle.getDirection(), myX + TURTLE_SIZE/2, myY + TURTLE_SIZE/2);
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
		if (turtle.showing()) {			
			ImageView currentImageView = turtle.getImageView();
			root.getChildren().remove(currentImageView);
			currentImageView.setX(myX);
			currentImageView.setY(myY);
			root.getChildren().add(currentImageView);
			gc.drawImage(turtleShape, myX, myY, TURTLE_SIZE, TURTLE_SIZE);
		}
		if (!turtle.isPenUp()) {
			drawLine(gcDrawing, myX, myY);
		}
		gc.restore();
		setOldCoordinates(turtle, myX, myY, turtle.getDirection());
	}
	
	private ImageView createTurtleImageView(Turtle turtle, double x, double y){
		ImageView turtleImage = new ImageView(turtleShape);
		turtleImage.setFitHeight(TURTLE_SIZE);
		turtleImage.setPreserveRatio(true);
		turtleImage.setX(x);
		turtleImage.setY(y);
		turtleImage.setOnMouseEntered(event -> {
				 canvasRight.showTurtleState(turtle);
		});
		turtleImage.setOnMouseClicked(event -> {
			turtle.setActive(!turtle.isActive());
		});
		return turtleImage;
	}
	
	private void drawLine(GraphicsContext gcDrawing, double myX, double myY) {
		int scaledPen = (int) myPen.getMyPenSize() * PEN_SCALE;
		switch (myPen.getMyPenType()){
			case SOLID_LINE: {
				drawOval(gcDrawing, myX, myY);
			}
			case DASHED_LINE: {
				if (penCounter < scaledPen / 2) {
					drawOval(gcDrawing, myX, myY);
				} else if (penCounter == scaledPen) {
					penCounter = DEFAULT;
				}
				penCounter++;
			}
			case DOTTED_LINE: {
				if (penCounter == scaledPen / 2) {
					drawOval(gcDrawing, myX, myY);
				} else if (penCounter == scaledPen) {
					drawOval(gcDrawing, myX, myY);
					penCounter = DEFAULT;
				}
				penCounter++;
			}
		}
	}


	private void drawOval(GraphicsContext gcDrawing, double myX, double myY) {
		long penSize = Math.round(myPen.getMyPenSize());
		gcDrawing.fillOval(myX + TURTLE_SIZE/2 - penSize/2, myY + TURTLE_SIZE/2 - penSize/2,
				penSize, penSize);
	}
	
	public void drawStamps() {
		for (Turtle turtle: myTurtles.keySet()) {
			if (turtle.isActive()) {
				gcStamps.drawImage(turtleShape, turtle.getCurX(), turtle.getCurY());
			}
		}
	}
	
	public void clearStamps() {
		clearGraphicsContext(gc);
	}
	
	public void clearGraphicsContext(GraphicsContext gc) {
		gc.clearRect(DEFAULT, DEFAULT, CANVAS_WIDTH, CANVAS_HEIGHT);
	}

	/**
	 * @return GraphicsContext for Canvas Background
	 */
	public GraphicsContext getBackgroundGraphicsContext(){
		return gcBackground;
	}	
	
	/**
	 * returns index in given palette of given turtle image name.
	 * @param String imageName
	 * @param GUIComboBox palette
	 */
	public int getPaletteIndex(String imageName, List<String> whichPalette){
		for(String turtleName:whichPalette){
			if(turtleName.equals(imageName)){
				return whichPalette.indexOf(turtleName);
			}
		}
		return -1;
	}	
	
	/**
	 * Sets Pen color based on index within palette.
	 * @param index of color in palette.
	 */
	public void setPenColor(int index){
		myPen.setMyPenColorIndex(index);
		String[] rgb = myPenPalette.get(index).split(" ");
		Color col = Color.rgb(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
		setPenColor(col, myPenPalette.get(index));
	}
	
	/**
	 * Sets Pen color based on User preference.
	 * @param Color that user chose.
	 */
	public void setPenColor(Color c, String penColorName) {
		myPen.setMyPenColor(c);
		myPen.setMyPenRGB(penColorName);
		myPen.setMyPenColorIndex(getPaletteIndex(penColorName, myPenPalette)); 
		for(List<GraphicsContext> lst: myTurtles.values()){
			if (lst != null){
				GraphicsContext gcPen = lst.get(1);
				gcPen.setFill(c);
			}
		}
	}
	/**
	 * Sets background color based on index within palette.
	 * @param index of color in palette.
	 */
	public void setBackgroundColor(int index) {
		String[] rgb = myBackgroundPalette.get(index).split(" ");
		setBackgroundColor(Color.rgb(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])), 
				myBackgroundPalette.get(index));
	}
	
	/**
	 * Sets background color based on User preference.
	 * @param Color that user chose.
	 */
	public void setBackgroundColor(Color col, String backgroundColorName){
		myBackgroundRGB = backgroundColorName;
		gcBackground.setFill(col);
		gcBackground.fillRect(DEFAULT, DEFAULT, CANVAS_WIDTH, CANVAS_HEIGHT);
	}
	
	public Color stringToColor(String colorString) {
		String[] rgb = colorString.split(" ");
		Color col = Color.rgb((int) Double.parseDouble(rgb[0]),
				(int) Double.parseDouble(rgb[1]), (int) Double.parseDouble(rgb[2]));
		return col;
	}
	
	/**
	 * Returns current background color of canvas.
	 */
	public String getBackgroundColor(){
		return myBackgroundRGB;
	}
	
	/**
	 * Update both pen and background color palette at given index to given RGB color.
	 * @param Space separated string of RGB values
	 * @param index of color in palette that user wants to change
	 */
	public void setPalette(String RGB, int index){
		canvasRight.changePalettes(RGB, index);
	}
	
	/**
	 * Sets Turtle shape/image based on index within palette.
	 * @param index of image in palette.
	 */
	public void setTurtleShape(int index) {
		myTurtleShapeIndex = index;
		String imageName = myImagePalette.get(index);
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
		setTurtleShape(image, imageName);
	}
	
	/**
	 * Sets user-inputed image as the Canvas turtle.
	 * @param Image
	 */
	public void setTurtleShape(Image image, String imageName){
		turtleShape = image;
		turtleShapeName = imageName;
		myTurtleShapeIndex = getPaletteIndex(imageName, myImagePalette);
		for(Turtle key: myTurtles.keySet()){
			if (key == null){
				gc.drawImage(turtleShape, STARTING_X, STARTING_Y, TURTLE_SIZE, TURTLE_SIZE);
			}
			else drawTurtle(key);
		}
	}
	
	public Image stringToImage(String imageString) {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageString));
		return image;
	}
	
	/**
	 * returns current turtle image filename
	 * @return
	 */
	public String getTurtleImageName(){
		return turtleShapeName;
	}
	
	/**
	 * Returns current index of shape/image of turtle;
	 */
	public int getTurtleShapeIndex() {
		return myTurtleShapeIndex;
	}
	
	/**
	 * returns current pen color as space separated RGB
	 * @return
	 */
	public GUICanvasPen getPen() {
		return myPen;
	}

	public void setPenStatus(String penUp) {
		for (Turtle t: myTurtles.keySet()) {
			if (t.isActive() && penUp.equals(myResources.getString("PenUp"))) {
				t.setPenUp(true);
			} else if (t.isActive() && penUp.equals(myResources.getString("PenDown"))){
				t.setPenUp(false);
			}
		}
	}
	
	public int getWidth(){
		return CANVAS_WIDTH;
	}
	
	public int getHeight(){
		return CANVAS_HEIGHT;
	}

}