package br.com.projetoquiz.quiz.ProjetoQuiz;

import java.io.IOException;
import java.util.List;
import br.com.projetoquiz.quiz.ProjetoQuiz.dao.ScoreDAO;
import br.com.projetoquiz.quiz.ProjetoQuiz.model.Score;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class RankingController {

    @FXML
    private TableView<Score> rankingTable;

    @FXML
    private TableColumn<Score, String> playerColumn;

    @FXML
    private TableColumn<Score, Integer> scoreColumn;

    @FXML
    private Button backButton;

    // O método initialize é chamado automaticamente quando a tela é carregada
    @FXML
    public void initialize() {
        // 1. Configura as colunas para saberem de onde pegar os dados do objeto Score
        // A coluna "playerColumn" vai chamar o método getUsername() do objeto Score
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        // A coluna "scoreColumn" vai chamar o método getScore() do objeto Score
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        // 2. Busca os dados no banco de dados
        loadRankingData();
    }

    private void loadRankingData() {
        ScoreDAO scoreDAO = new ScoreDAO();
        List<Score> topScores = scoreDAO.getTopScores();

        // 3. Preenche a tabela com os dados
        // O TableView precisa de uma "ObservableList", um tipo especial de lista
        ObservableList<Score> rankingData = FXCollections.observableArrayList(topScores);
        rankingTable.setItems(rankingData);
    }

    @FXML
    private void handleBackButtonAction() {
        // Lógica para fechar a janela de ranking e voltar para a tela de login
        try {
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.close();

            // Abre a tela de login
            Stage loginStage = new Stage();
            loginStage.setTitle("Quiz de Desenhos Animados");
            Scene scene = new Scene(App.loadFXML("primary"), 1280, 720);
            loginStage.setScene(scene);
            loginStage.setResizable(false);
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}