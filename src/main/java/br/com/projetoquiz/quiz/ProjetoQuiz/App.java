package br.com.projetoquiz.quiz.ProjetoQuiz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font; // <-- IMPORT NOVO
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // ----- LINHA NOVA PARA CARREGAR A FONTE -----
        // Usando o nome exato do seu arquivo de fonte
        Font.loadFont(getClass().getResourceAsStream("/fonts/Fredoka_SemiExpanded-Regular.ttf"), 10);

        scene = new Scene(loadFXML("primary"), 1280, 720);
        stage.setScene(scene);
        stage.setTitle("Quiz de Desenhos Animados");
        stage.setResizable(false);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/br/com/projetoquiz/quiz/ProjetoQuiz/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setPrefWidth(400); 
        alert.setResizable(true); 
        alert.showAndWait();
    }
}