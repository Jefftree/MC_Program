/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mc.program;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;

/**
 *
 * @author
 * Jeffrey
 */
public class Cluster {

    public static DefaultListModel allModel = new DefaultListModel(); // ListModel containing all exams.
    ArrayList<String> exam = new ArrayList<>(); //ArrayList of exam names in string format
    DefaultListModel model = new DefaultListModel(); //ListModel that contains exams for this specific cluster.
    private String name; // name of Cluster
    private int counter = 0; // Amount of exams completed in this cluster.
    private int totalPercentage = 0; //Questions correct in the cluster.
    private int totalPercentage2 = 0; //Questions answered in the cluster.

    public Cluster(String path) throws FileNotFoundException, NullPointerException{
        name = path;
        File[] fList = new File("resources/" + path).listFiles();
        for (File file : fList) {
            //if the file isn't files.txt, add it to the exam bank
            if (file.isFile() && (!(file.getName().equals("files.txt")||file.getName().equals(".DS_Store")))) {
                exam.add(file.getName());
            }
            if (Config.isTrial()){
                break;
            }
        }
        for (String s:exam) {
            model.addElement(s);
            allModel.addElement(s);
        }

    }
    /**
     * @return Amount of exams within this Cluster.
     */
    public int getSize() {
        return exam.size();
    }
/**
     * @return ArrayList of exams within this cluster.
     */
    public ArrayList getExams() {
        return exam;
    }

    /**
     * @return ListModel for the selected Cluster.
     */
    public DefaultListModel getModel() {
        return model;
    }

    /**
     * @return ListModel that includes all clusters. For use when the "All" tab is selected.
     */
    public static DefaultListModel getAllModel() {
        return allModel;
    }

    /**
     * Resets the amount of completed tests and percentages in this cluster for recalculation.
     */
    public void reset() {
        totalPercentage = 0;
        totalPercentage2 = 0;
        counter = 0;
    }

    /**
     * This is for the score percentages for each cluster on the selectionPanel (Home Page)
     *
     * @return The average percentage score on a cluster. Returns 0 if the cluster was never attempted.
     */
    public double getPercentage() {
        return totalPercentage;
    }
    
    public double getPercentage2() {
        return totalPercentage2;
    }

    /**
     * This is for the score percentages for each cluster on the selectionPanel (Home Page)
     *
     * @param percentage A percentage for a test to add to the Cluster. All added percentages will be averaged when getPercentage() is called.
     */
    public void addPercentage(int percentage) {
        totalPercentage += percentage;
    }
    
    public void addPercentage2(int percentage) {
        totalPercentage2 += percentage;
    }
    
    /**
     * This is for the # of tests completed for each cluster on the selectionPanel (Home Page).
     *
     * @return The amount of tests completed within this cluster.
     */
    public int getCounter() {
        return counter;
    }

    /**
     * This is for the # of tests completed for each cluster on the selectionPanel (Home Page).
     *
     * @param count Represents # of tests for this cluster.
     */
    public void setCounter(int count) {
        counter = count;
    }

    /**
     * Returns the name of a cluster in string format.
     *
     * @return Name of cluster.
     */
    public String getName() {
        return name;
    }
}
