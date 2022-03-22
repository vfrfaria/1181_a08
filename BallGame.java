import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The program is a simple clicking game which counts the hits and misses of
 * the clicks.
 */
public class BallGame extends Application {

   private static Circle ball;
   private static int ballX;
   private static int ballY;
   private static double ballDirection;
   private static BallAnimation animation;

   private static int hits = 0;
   private static int misses = 0;
   private static Text hitsText;
   private static Text missesText;

   private static Text gameOverLabel;
   private static VBox root;
   private static Pane window;

   private static boolean gamePaused = false;
   private static boolean gameEnded = false;

   /**
    * Launches the program.
    * @param args none.
    */
   public static void main(String[] args) {
      launch(args);
   }

   public void start(Stage stage) {
      root = new VBox();

      initializeGame();

      stage.setTitle("Ball Game");
      stage.setScene(new Scene(root));
      stage.show();
   }

   /**
    * Builds the GUI and starts the first round.
    */
   public void initializeGame() {
      createWindowElements();
      createButtons();
      startRound();
   }

   /**
    * Creates the elements which will be displayed inside the 'window',
    * which is the black background.
    */
   public void createWindowElements() {
      hitsText = new Text("Hits: " + hits);
      hitsText.setFont(Font.font(20));
      hitsText.setFill(Color.WHITE);

      missesText = new Text("Misses: " + misses);
      missesText.setFont(Font.font(20));
      missesText.setFill(Color.WHITE);

      HBox score = new HBox(hitsText, missesText);
      score.setSpacing(10);
      score.setPadding(new Insets(10));

      Rectangle background = new Rectangle(0,0,400,300);
      background.setFill(Color.BLACK);

      ball = new Circle(ballX, ballY, 20);
      ball.setFill(Color.WHITE);

      gameOverLabel = new Text(120, 150, "Game Over");
      gameOverLabel.setFont(Font.font(36));
      gameOverLabel.setFill(Color.WHITE);

      window = new Pane(background, score, ball);
      root.getChildren().add(window);
   }

   /**
    * Creates the elements of the footer: the 'Pause' and 'Reset' buttons.
    */
   public void createButtons() {
      Button pause = new Button("Pause");
      Button reset = new Button("Reset");

      HBox footer = new HBox(pause, reset);
      footer.setSpacing(10);
      footer.setPadding(new Insets(5));
      footer.setAlignment(Pos.BOTTOM_RIGHT);

      root.getChildren().add(footer);

      animation = new BallAnimation();

      // Use of lambda functions demonstrated on textbook at Chapter 10.7.2.
      pause.setOnAction(e -> pauseGame());
      reset.setOnAction(e -> resetGame());
      ball.setOnMouseClicked(e -> ballClicked());
   }

   /**
    * Pauses/restores the game.
    */
   public static void pauseGame() {
      if (!gameEnded) {
         if (gamePaused) {
            animation.start();
            gamePaused = false;
         } else {
            animation.stop();
            gamePaused = true;
         }
      }
   }

   /**
    * Resets the game and starts a new round.
    */
   public static void resetGame() {
      misses = 0;
      hits = 0;
      missesText.setText("Misses: " + misses);
      hitsText.setText("Hits: " + hits);
      gameEnded = false;
      window.getChildren().remove(gameOverLabel);
      startRound();
   }

   /**
    * Starts a new round or ends the game if more than 5 misses.
    */
   public static void startRound() {
      if (misses >= 5) {
         endGame();
      } else {
         runRound();
      }
   }

   /**
    * Ends the game.
    */
   public static void endGame() {
       root.getChildren().remove(ball);
       animation.stop();
       window.getChildren().add(gameOverLabel);
       gameEnded = true;
   }

   /**
    * Describes the handling of clicking the ball.
    */
   public static void ballClicked() {
       hits += 1;
       hitsText.setText("Hits: " + hits);
       animation.stop();
       startRound();
   }

   /**
    * Sets basic parameters/actions for starting a round.
    */
   public static void runRound() {
       // 200 and 150 are the coordinates for the center of the window.
       ballX = 200;
       ballY = 150;

       ball.setCenterX(ballX);
       ball.setCenterY(ballY);

       // Random number will determine direction of movement on moveBall() method.
       ballDirection = Math.random() * 10;

       animation.start();
    }



   /**
    * Describes the handling of the BallAnimation. Moves the ball and checks
    * if it has exited the window.
    */
   public class BallAnimation extends AnimationTimer {

      @Override
      public void handle(long l) {
         moveBall();
         checkLostRound();
      }

      /**
       * Displaces (moves) the ball on the X axis.
       */
      public void moveBall() {
         boolean moveRight = ballDirection > 5;
         int xDisplacement;

         // 2 and -2 determine the ball's movement speed.
         if (moveRight) {
            xDisplacement = 2;
         } else {
            xDisplacement = -2;
         }
         ball.setCenterX(ballX += xDisplacement);
      }

      /**
       * Checks if ball has exited the window, meaning the lost of the round.
       */
      public void checkLostRound() {
         // 420 and -20 are the X coordinates of window boundaries.
         if (ball.getCenterX() > 420 || ball.getCenterX() < -20) {
            misses += 1;
            missesText.setText("Misses: " + misses);
            animation.stop();
            startRound();
         }
      }
   }
}