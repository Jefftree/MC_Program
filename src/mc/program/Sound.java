/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mc.program;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Nathan & Jeffrey
 */
public class Sound {

    public static void playCorrect() {

        int a = new Random().nextInt(100);
        if (a <= 80) {
            playSound("right");
        } else {
            playSound("right2");
        }
    }

    public static void playWrong() {

        int a = new Random().nextInt(100);
        if (a <= 80) {
            playSound("wrong");
        }else {
            playSound("wrong2");
        }
    }

    public static void playSound(String name) {
        if (Config.getSound()) {
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(Sound.class.getResourceAsStream("sounds/" + name + ".wav"));
                Clip clip = AudioSystem.getClip();
                clip.open(stream);
                clip.start();
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException ex) {
                Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
