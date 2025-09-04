module br.com.projetoquiz.quiz.ProjetoQuiz {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;
    requires javafx.media;
    requires transitive javafx.graphics;
    requires jbcrypt;

    opens br.com.projetoquiz.quiz.ProjetoQuiz to javafx.fxml;

    exports br.com.projetoquiz.quiz.ProjetoQuiz;
    exports br.com.projetoquiz.quiz.ProjetoQuiz.model;
    exports br.com.projetoquiz.quiz.ProjetoQuiz.dao;
    exports br.com.projetoquiz.quiz.ProjetoQuiz.util;
}