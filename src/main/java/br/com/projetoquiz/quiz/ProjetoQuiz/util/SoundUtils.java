package br.com.projetoquiz.quiz.ProjetoQuiz.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class SoundUtils {

    public static void playSound(String soundFile) {
        try {
            // Busca o recurso de som na pasta /sounds/ que criamos
            URL resource = SoundUtils.class.getResource("/sounds/" + soundFile);
            
            if (resource != null) {
                Media media = new Media(resource.toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
            } else {
                System.err.println("Não foi possível encontrar o arquivo de som: " + soundFile);
            }
        } catch (Exception e) {
            System.err.println("Erro ao tocar o som: " + soundFile);
            e.printStackTrace();
        }
    }
}