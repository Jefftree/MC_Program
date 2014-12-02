/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mc.program;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeffrey
 */
public class Config {

    private static String webURL;
    private static String versionNum;
    private static boolean shuffle;
    private static boolean collate;
    private static boolean sound;
    private static int timePerQuestion;
    private static boolean trial;
    private static String operatingSystem;
    private static String updaterVersion;
    private static String secretLink;
    private static boolean hasResources;

    public static String getUpdaterVersion() {
        return updaterVersion;
    }

    /**
     * Loads all the settings from the general.txt file.
     */
    public static void loadConfig() {
        Scanner configScanner;
        ArrayList<String> history = new ArrayList<String>();

        try {
            configScanner = new Scanner(new FileInputStream("config/general.txt"));
            webURL = configScanner.nextLine();
            String temp = configScanner.nextLine();
            versionNum = temp.substring(8);
            shuffle = (configScanner.nextLine().substring(8).equals("1"));
            collate = (configScanner.nextLine().substring(8).equals("1"));
            sound = (configScanner.nextLine().substring(6).equals("1"));

            int amountOfTime = Integer.parseInt(configScanner.nextLine().substring(5));
            timePerQuestion = ((amountOfTime >= 0) | (amountOfTime <= 59)) ? amountOfTime : 20;
            updaterVersion = configScanner.nextLine().substring(8);
            secretLink = configScanner.nextLine().substring(4)+".php";
            hasResources = (configScanner.nextLine().substring(10).equals("1"));

            configScanner.close();
        } catch (FileNotFoundException ex) {
            webURL = "http://jying,ca/proj/mc/update.html";
            versionNum = "0";
            shuffle = collate = sound = false;
            timePerQuestion = 20;
            updaterVersion="1";
            secretLink="moo.php";
            hasResources=true;
        }

        trial = false;
        operatingSystem = (System.getProperty("os.name")).toUpperCase();
    }

    public static String getSecretLink() {
        return secretLink;
    }
    
    public static boolean hasLatestResources(){
        return hasResources;
    }
    
    public static void setResources(boolean b){
        hasResources = b;
    }

    /**
     * Saves all the settings to the general.txt file.
     */
    public static void saveConfig() {
        try {
            PrintWriter configWriter = new PrintWriter(new FileOutputStream("config/general.txt", false));
            configWriter.println(webURL);
            configWriter.println("version=" + versionNum);
            configWriter.println("shuffle=" + (shuffle == false ? "0" : "1"));
            configWriter.println("collate=" + (collate == false ? "0" : "1"));
            configWriter.println("sound=" + (sound == false ? "0" : "1"));
            configWriter.println("time=" + String.valueOf(timePerQuestion));
            configWriter.println("updater=" + updaterVersion);
            configWriter.println("cow=" + secretLink.substring(0,secretLink.length()-4));
                        configWriter.println("resources="+ (hasResources == false ?"0":"1"));
            configWriter.println("");
            configWriter.println("WARNING! Modifying this file may cause the program to stop functioning.");
            configWriter.flush();
            configWriter.close();
        } catch (FileNotFoundException e) {
        }
    }

    /**
     * Returns the trial mode boolean. True = in trial mode
     */
    public static boolean isTrial() {
        return trial;
    }

    /**
     * Changes the trial mode boolean
     */
    public static void setTrial(boolean trial) {
        Config.trial = trial;
    }

    /**
     * Returns OS
     */
    public static String getOperatingSystem() {
        return operatingSystem;
    }

    /**
     * Sets OS of user
     */
    public static void setOperatingSystem(String operatingSystem) {
        Config.operatingSystem = operatingSystem;
    }

    /**
     * Returns time per question in seconds. 0 represents not timed.
     */
    public static int getTimePerQuestion() {
        return timePerQuestion;
    }

    /**
     * Sets time per question in seconds. 0 represents unlimited.
     */
    public static void setTimePerQuestion(int i) {
        timePerQuestion = i;
    }

    /**
     * Returns version of program
     */
    public static String getVersion() {
        return versionNum;
    }

    /**
     * Sets version of program
     */
    public static void setVersion(String ver) {
        versionNum = ver.trim();
    }

    /**
     * Get website URL
     */
    public static String getURL() {
        return webURL;
    }

    /**
     * Shuffle boolean. 1= shuffle
     */
    public static boolean getShuffle() {
        return shuffle;
    }

    public static void setShuffle(boolean choice) {
        shuffle = choice;
    }

    /**
     * Collate boolean. 0=all questions then all answers. 1=
     * question,answer,question,answer
     */
    public static boolean getCollate() {
        return collate;
    }

    public static void setCollate(boolean choice) {
        collate = choice;
    }

    /**
     * Sound boolean. 1 = on.
     */
    public static boolean getSound() {
        return sound;
    }

    public static void setSound(boolean choice) {
        sound = choice;
    }

}
