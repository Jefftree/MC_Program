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
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jeffrey
 */
public class Scoring {

    private static int completed = 0;
    private static int correct = 0;
    private static boolean saved = true;
    private static Object[][] table;
    private static String scoreFile = "resources/scoreHistory.txt";

    public static int getCompleted() {
        return completed;
    }

    public static void setCompleted(int completed) {
        Scoring.completed = completed;
    }

    public static int getCorrect() {
        return correct;
    }

    public static void setCorrect(int correct) {
        Scoring.correct = correct;
    }

    public static boolean saved() {
        return saved;
    }

    public static void saved(boolean b) {
        saved = b;
    }

    public static void updateHistory() throws FileNotFoundException {
        //Load All the Scores
        Scanner hisScanner = new Scanner(new FileInputStream(scoreFile));
        ArrayList<ArrayList<String>> allScores = new ArrayList<ArrayList<String>>(0);
        while (hisScanner.hasNext()) {
            ArrayList<String> each = new ArrayList<String>(0);
            for (int i = 0; i < 4; i++) {
                if (hisScanner.hasNext()) {
                    each.add(hisScanner.nextLine());
                }
            }
            allScores.add(each);
        }
        hisScanner.close();

        //Reset Table for loading in new statistics
        for (Cluster cluster : ClusterList.getList()) {
            cluster.reset();
        }
        Double allPercentage = 0.0;
        Double allPercentage2= 0.0;

        //Load all the scores, scoreHistory table changed to counter and percentage within the cluster class.
        for (ArrayList score : allScores) {

            for (Cluster cluster : ClusterList.getList()) {
                if ((cluster.getExams().contains(score.get(0))) | (score.get(0).equals(cluster.getName()))) {
                    cluster.setCounter(cluster.getCounter() + 1);
                    if (Integer.parseInt((String) score.get(2)) != 0) {
                        cluster.addPercentage(Integer.parseInt((String) score.get(1)));
                        cluster.addPercentage2(Integer.parseInt((String) score.get(2)));
                        allPercentage += Double.parseDouble((String) score.get(1));
                        allPercentage2+= Double.parseDouble((String) score.get(2));
                    }
                    break;
                }
            }
        }

        for (int i = 0; i < ClusterList.getModel().getRowCount() - 1; i++) {
            Double thisPercent = Double.isNaN(ClusterList.getList().get(i).getPercentage()/ClusterList.getList().get(i).getPercentage2()) ? 0.00 : ClusterList.getList().get(i).getPercentage()/ClusterList.getList().get(i).getPercentage2();
            thisPercent = Math.rint(thisPercent * 1000)/10;
            ClusterList.getModel().setValueAt(String.valueOf(ClusterList.getList().get(i).getCounter()), i, 1);
            ClusterList.getModel().setValueAt(String.valueOf(thisPercent) + " %", i, 2);
        }
        ClusterList.getModel().setValueAt(String.valueOf(allScores.size()), ClusterList.getModel().getRowCount() - 1, 1);
        allPercentage = Double.isNaN(Math.rint(allPercentage / allPercentage2)) ? 0.00 : Math.rint((allPercentage / allPercentage2) * 1000) / 10;

        ClusterList.getModel().setValueAt(String.valueOf(allPercentage) + " %", ClusterList.getModel().getRowCount() - 1, 2);

    }

    public static void loadTable(JTable historyTable) {
        historyTable.setModel(new DefaultTableModel(returnTable(), new Object[]{
            "MC Name", "Correct", "Completed", "Percentage", "Date Completed"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        // Formatting
        historyTable.scrollRectToVisible(historyTable.getCellRect(0, 0, true));
        historyTable.getColumnModel().getColumn(0).setPreferredWidth(15);
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(15);
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(15);
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(15);
        historyTable.setAutoCreateRowSorter(true);
    }

    public static Object[][] returnTable() {
        Scanner historyScanner;
        ArrayList<String> history = new ArrayList<String>();

        try {
            historyScanner = new Scanner(new FileInputStream(scoreFile));
            while (historyScanner.hasNext()) {
                history.add(historyScanner.nextLine());
            }
            historyScanner.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        table = new String[history.size() / 4][5];

        for (int i = 0; i < history.size() / 4; i++) {
            table[i][0] = history.get(i * 4);
            table[i][1] = history.get(i * 4 + 1);
            table[i][2] = history.get(i * 4 + 2);
            table[i][3] = String.valueOf(Math.rint(Double.parseDouble(history.get(i * 4 + 1)) / Double.parseDouble(history.get(i * 4 + 2)) * 1000) / 10);
            if (table[i][3].equals("NaN")) {
                table[i][3] = "0.0";
            }
            table[i][4] = history.get(i * 4 + 3);
        }

        return table;
    }

    public static void resetHistory() throws FileNotFoundException {
        PrintWriter historyWriter = new PrintWriter(new FileOutputStream(scoreFile));
        historyWriter.close();
        for (Cluster cluster : ClusterList.getList()) {
            cluster.reset();
        }
    }

    public static void saveScore(Test test) {
        try {
            if (!Scoring.saved() & Scoring.getCompleted() > 0) {
                PrintWriter historyWriter = new PrintWriter(new FileOutputStream(scoreFile, true));
                historyWriter.println(test.getInputFile());
                historyWriter.println(Scoring.getCorrect());
                historyWriter.println(Scoring.getCompleted());
                historyWriter.println(new Date(System.currentTimeMillis()));
                historyWriter.flush();
                historyWriter.close();
                saved = true;
            }
        } catch (FileNotFoundException e) {

        }
    }
}
