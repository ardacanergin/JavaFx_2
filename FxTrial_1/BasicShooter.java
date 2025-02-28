
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BasicShooter extends Application {

	private static int w = 250;
	private static int h = 200;
	private static int point = 0;
	private Pane pane;
	private Circle enemy;
	private List<Circle> bullets;
	private List<Circle> targets;
	private List<Circle> bulletsOfTargets;
	private Label scoreLabel;
	private Rectangle player;
	private long lastSpawnTime = 0;
	private int score = 0;
	private AnimationTimer gameLoop;
	private int life = 3;
	private Label lifeLabel;

	public void start(Stage primary) {

		pane = new Pane();
		Scene scene = new Scene(pane, 700, 700);
		scene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));
		scene.setOnKeyReleased(e -> handleKeyRelease(e.getCode()));

		scoreLabel = new Label("Score:0");
		scoreLabel.setLayoutX(10);
		scoreLabel.setLayoutY(10);

		lifeLabel = new Label("Remaming Life: 3");
		lifeLabel.setLayoutX(10);
		lifeLabel.setLayoutY(20);

		pane.getChildren().add(scoreLabel);
		pane.getChildren().add(lifeLabel);

		primary.setScene(scene);
		primary.setTitle("ShotterDemo");
		primary.show();

		player = new Rectangle(30, 60, Color.RED);
		player.setX((pane.getWidth() - player.getWidth()) / 2);
		player.setY((pane.getHeight() - player.getHeight()) - 10);

		pane.getChildren().add(player);

		bullets = new ArrayList<>();
		targets = new ArrayList<>();
		bulletsOfTargets = new ArrayList<>();

		startGameLoop();

	}

	public void startGameLoop() {
		gameLoop = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update(); // update movement
				spawnTarget(now);
			}

		};
		gameLoop.start();
	}

	public void handleKeyPress(KeyCode code) {

		keys.put(code, true);

		if (code == KeyCode.SPACE) {
			Circle bullet = new Circle(10, Color.ORANGE);
			bullet.setCenterY(player.getY() + 10);
			bullet.setCenterX(player.getX() + player.getWidth() / 2);
			bullets.add(bullet);
			pane.getChildren().add(bullet);

		}
	}

	public void update() {

		movePlayer();
		moveBullets();
		checkCollisions();
		checkGameOver();
		moveTargetBullets();

	}

	private void checkGameOver() {
		if (score == 10 || life == 0) {

			Label over = new Label();
			over.setText("Game Over!");
			over.setFont(Font.font("Arial", FontWeight.BOLD, 24));

			Scene tempScene = new Scene(new Group(over));
			tempScene.setFill(Color.TRANSPARENT);
			tempScene.snapshot(null);

			double textWidth = over.getLayoutBounds().getWidth();
			double textHeight = over.getLayoutBounds().getHeight();
			double x = (pane.getWidth() - textWidth) / 2;
			double y = (pane.getHeight() - textHeight) / 2;
			over.setLayoutX(x);
			over.setLayoutY(y);

			pane.getChildren().add(over);

			gameLoop.stop();

		}

	}

	public void movePlayer() {

		if (isKeyPressed(KeyCode.LEFT) && player.getX() > 0) {
			player.setX(player.getX() - 5);
		}

		if (isKeyPressed(KeyCode.RIGHT) && player.getX() + player.getWidth() < pane.getWidth()) {
			player.setX(player.getX() + 5);
		}
		if (isKeyPressed(KeyCode.UP) && player.getY() > pane.getHeight() / 2) {
			player.setY(player.getY() - 5);
		}

		if (isKeyPressed(KeyCode.DOWN) && player.getY() < pane.getHeight()) {
			player.setY(player.getY() + 5);
		}
	}

	public void moveBullets() {
		for (Circle bullet : bullets) {
			bullet.setCenterY(bullet.getCenterY() - 12);
		}
	}

	public void moveTargetBullets() {
		for (Circle bulletOfTargets : bulletsOfTargets) {
			bulletOfTargets.setCenterY(bulletOfTargets.getCenterY() + 5);
		}
	}

	public void checkCollisions() {
		List<Circle> bulletsToRemove = new ArrayList<>();
		List<Circle> targetsToRemove = new ArrayList<>();
		List<Circle> bulletsOfTargetsToRemove = new ArrayList<>();

		for (Circle bullet : bullets) {
			for (Circle target : targets) {
				if (bullet.getBoundsInParent().intersects(target.getBoundsInParent())) {
					bulletsToRemove.add(bullet);
					targetsToRemove.add(target);
					score++;
					scoreLabel.setText("Score:" + score + "/10");
				}
			}
		}

		for (Circle bulletOfTargets : bulletsOfTargets) {
			if (bulletOfTargets.getBoundsInParent().intersects(player.getBoundsInParent())) {
				bulletsOfTargetsToRemove.add(bulletOfTargets);
				if (life > 0) {
					life--;
					lifeLabel.setText("Remainig Life:" + life);
				}
			}
		}
		bulletsOfTargets.removeAll(bulletsOfTargetsToRemove);
		bullets.removeAll(bulletsToRemove);
		targets.removeAll(targetsToRemove);

		pane.getChildren().removeAll(targetsToRemove);
		pane.getChildren().removeAll(bulletsToRemove);
		pane.getChildren().removeAll(bulletsOfTargetsToRemove);
	}

	public void spawnTarget(long now) {

		Circle target = new Circle(30, Color.BLUE);
		if (now - lastSpawnTime >= Duration.seconds(2).toMillis() * 1000000) {

			double x = (Math.random() * (pane.getWidth() - 100)) + 50;
			
			double y = (Math.random() * pane.getHeight() / 2) + 50;
			target.setCenterX(x);
			target.setCenterY(y);
			pane.getChildren().add(target);
			targets.add(target);

			
			lastSpawnTime = now;

		}
		
		if (now - lastSpawnTime >= Duration.seconds(2).toMillis() * 1000000) {
			Circle targetBullet = new Circle(20, Color.BLACK);
			targetBullet.setCenterX(target.getCenterX());
			targetBullet.setCenterY(target.getCenterY() + target.getRadius());
			bulletsOfTargets.add(targetBullet);
			pane.getChildren().add(targetBullet);
		}
	}

	private boolean isKeyPressed(KeyCode key) {

		return keys.getOrDefault(key, false);
	}

	private void handleKeyRelease(KeyCode code) {
		keys.put(code, false);
	}

	private final List<KeyCode> pressedKeys = new ArrayList<>();
	private final Map<KeyCode, Boolean> keys = new HashMap<>();

	public static void main(String[] args) {
		launch();
	}
}
