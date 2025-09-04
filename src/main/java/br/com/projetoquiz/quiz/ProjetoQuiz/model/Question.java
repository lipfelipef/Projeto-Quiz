package br.com.projetoquiz.quiz.ProjetoQuiz.model;

import java.util.List;

public class Question {

    // A declaração do Enum deve ser pública para ser acessada de outras classes
    public enum Difficulty {
        FACIL,
        MEDIO,
        DIFICIL
    }

    private String questionText;
    private List<String> options;
    private int correctAnswerIndex;
    private Difficulty difficulty;
    private int points;

    public Question(String questionText, List<String> options, int correctAnswerIndex, Difficulty difficulty, int points) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.difficulty = difficulty;
        this.points = points;
    }

    // Getters
    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    // ===== MÉTODO QUE ESTAVA FALTANDO FOI ADICIONADO AQUI =====
    public int getPoints() {
        return points;
    }
}