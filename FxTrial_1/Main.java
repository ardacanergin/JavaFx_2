import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Pane oluştur
        Pane pane = new Pane();
        pane.setPrefSize(400, 400); // Pane boyutunu ayarla

        // Metni oluştur
        Text text = new Text("Aptal!");
        text.setFont(Font.font("Arial", FontWeight.BOLD, 24)); // Yazı tipi ve boyutunu ayarla
        
        // Metnin konumunu ayarla (tam ortaya gelecek şekilde)
        double textWidth = text.getLayoutBounds().getWidth();
        double textHeight = text.getLayoutBounds().getHeight();
        text.setLayoutX((400 - textWidth) / 2);
        text.setLayoutY((400 - textHeight) / 2);

        // Pane'e metni ekle
        pane.getChildren().add(text);

        // Sahneyi oluştur ve pane'i sahneye ekle
        Scene scene = new Scene(pane, 400, 400);

        // Sahneyi göster
        primaryStage.setScene(scene);
        primaryStage.setTitle("Aptal!");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
