package br.com.projetoquiz.quiz.ProjetoQuiz.model;

public class Score {
    private String username;
    private int score;

    public Score(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }
}