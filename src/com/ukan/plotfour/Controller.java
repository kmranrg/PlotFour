package com.ukan.plotfour;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	private static final int CIRCLE_DIAMETER = 80; //80 pixel
	private static final String discColor1 = "#24303E";
	private static final String discColor2 = "#4CAA88";


	private static String PLAYER_ONE  = "Player One";
	private static String PLAYER_TWO = "Player Two";

	private boolean isPlayerOneTurn =  true;

	private Disc[][] insertedDiscsArray = new Disc[ROWS][COLUMNS]; // for structural changes i.e. for developers

	@FXML
	public GridPane rootGridPane;

	@FXML
	public Pane insertedDiscsPane;

	@FXML
	public Label playerNameLabel;

	public void createPlayground(){

		Shape rectangleWithHoles = createGameStructuralGrid();
		rootGridPane.add(rectangleWithHoles,0,1);

		List<Rectangle> rectangleList = createClickableColumns();

		for (Rectangle rectangle: rectangleList) {
			rootGridPane.add(rectangle, 0, 1);
		}
	}

	private Shape createGameStructuralGrid(){

		Shape rectangleWithHoles = new Rectangle((COLUMNS + 1) * CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++) {
				Circle circle = new Circle();
				circle.setRadius(CIRCLE_DIAMETER / 2);
				circle.setCenterX(CIRCLE_DIAMETER / 2);
				circle.setCenterY(CIRCLE_DIAMETER / 2);

				circle.setTranslateX(col * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
				circle.setTranslateY(row * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);

				rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
			}
		}

		rectangleWithHoles.setFill(Color.WHITE);

		return rectangleWithHoles;
	}

	private List<Rectangle> createClickableColumns() {

		List<Rectangle> rectangleList = new ArrayList<>();

		for (int col = 0; col < COLUMNS; col++) {

			Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setTranslateX(col * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);

			rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee26")));
			rectangle.setOnMouseExited(event ->  rectangle.setFill(Color.TRANSPARENT));

			final int column = col;
			rectangle.setOnMouseClicked(event -> {
				insertDisc(new Disc(isPlayerOneTurn), column);
			});

			rectangleList.add(rectangle);
		}

		return rectangleList;
	}

	private void insertDisc(Disc disc, int column) {

		int row = ROWS - 1;
		while (row > 0) {
			if(insertedDiscsArray[row][column] == null)
				break;

			row--;
		}

		if (row < 0)    //if whole column is occupied, we can't insert any more disc
			return;

		insertedDiscsArray[row][column] = disc; // for structural changes i.e. for developers
		insertedDiscsPane.getChildren().add(disc);

		disc.setTranslateX(column * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);

		int currentRow = row;
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), disc);
		translateTransition.setToY(row * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
		translateTransition.setOnFinished(event -> {

		//	if(gameEnded(currentRow, column)) {
				// do something
		//	}

			isPlayerOneTurn = !isPlayerOneTurn;
			playerNameLabel.setText(isPlayerOneTurn? PLAYER_ONE : PLAYER_TWO);

		});

		translateTransition.play();

	}

	private void gameEnded(int row, int column) {

		//TODO

	}

	private static class Disc extends Circle {

		private final boolean isPlayerOneMove;

		public Disc(boolean isPlayerOneMove) {

			this.isPlayerOneMove = isPlayerOneMove;
			setRadius(CIRCLE_DIAMETER / 2);
			setFill(isPlayerOneMove? Color.valueOf(discColor1): Color.valueOf(discColor2));
			setCenterX(CIRCLE_DIAMETER / 2);
			setCenterY(CIRCLE_DIAMETER / 2);
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
