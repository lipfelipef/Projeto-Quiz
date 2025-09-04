package br.com.projetoquiz.quiz.ProjetoQuiz.model;

public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private String pin; // Usando PIN em vez de pergunta/resposta

    public User() {}
    
    // Construtor para novos usuários
    public User(String username, String password, String email, String pin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.pin = pin;
    }

    // Construtor completo
    public User(int id, String username, String password, String email, String pin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.pin = pin;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
}