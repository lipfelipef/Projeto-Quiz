package br.com.projetoquiz.quiz.ProjetoQuiz;

import java.io.IOException;
import br.com.projetoquiz.quiz.ProjetoQuiz.dao.UserDAO;
import br.com.projetoquiz.quiz.ProjetoQuiz.model.User;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private PasswordField pinField;

    @FXML
    private void handleRegisterButtonAction() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String pin = pinField.getText().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            App.showAlert(AlertType.WARNING, "Campos Vazios", "Usuário, e-mail e senha são obrigatórios.");
            return;
        }

        // ----- NOVA VALIDAÇÃO DE TAMANHO DO USUÁRIO -----
        if (username.length() < 5) {
            App.showAlert(AlertType.WARNING, "Usuário Inválido", "O nome de usuário deve ter no mínimo 5 caracteres.");
            return;
        }

        // ----- NOVA VALIDAÇÃO DE TAMANHO DA SENHA -----
        if (password.length() < 5) {
            App.showAlert(AlertType.WARNING, "Senha Inválida", "A senha deve ter no mínimo 5 caracteres.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            App.showAlert(AlertType.ERROR, "Erro de Senha", "As senhas digitadas não conferem.");
            return;
        }
        if (!email.contains("@")) {
            App.showAlert(AlertType.ERROR, "E-mail Inválido", "Por favor, insira um e-mail válido.");
            return;
        }
        if (!pin.isEmpty() && (!pin.matches("\\d{4}"))) {
            App.showAlert(AlertType.ERROR, "PIN Inválido", "O PIN é opcional, mas se preenchido, deve conter exatamente 4 números.");
            return;
        }

        UserDAO userDAO = new UserDAO();
        if (userDAO.userOrEmailExists(username, email)) {
            App.showAlert(AlertType.ERROR, "Erro no Cadastro", "O nome de usuário ou o e-mail informado já está em uso.");
            return;
        }

        try {
            User newUser = new User(username, password, email, pin);
            userDAO.saveUser(newUser);
            App.showAlert(AlertType.INFORMATION, "Cadastro Concluído", "Usuário cadastrado com sucesso!");
            handleBackButtonAction();
        } catch (Exception e) {
            App.showAlert(AlertType.ERROR, "Erro no Banco de Dados", "Ocorreu um erro inesperado ao salvar os dados.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButtonAction() {
        try {
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.close();
            Stage stage = new Stage();
            stage.setTitle("Quiz de Desenhos Animados");
            Scene scene = new Scene(App.loadFXML("primary"), 1280, 720);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}