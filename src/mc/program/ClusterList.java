/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mc.program;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jeffrey
 */
public class ClusterList {

    private static ArrayList<Cluster> list = new ArrayList<Cluster>();
    private static DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int rowIndex, int mColIndex) {
            return false;
        }
    };

    /**
     * @return TableModel for the selectionPanel table. Includes all the
     * clusters and their respective score history.
     */
    public static DefaultTableModel getModel() {
        return model;
    }

    /**
     * Loads all the test names to their specific cluster.
     */
    public static void loadList() throws FileNotFoundException {

        if (Config.isTrial()) {
            list.add(new Cluster("Trial"));
        } else {
            File[] fList = new File("resources").listFiles();
            for (File file : fList) {
                if (file.isDirectory() && (!(file.getName().equals("Sounds") || file.getName().equals("Trial")))) {
                    list.add(new Cluster(file.getName()));
                }
            }
        }
        Object[] temp = Cluster.getAllModel().toArray();
        Arrays.sort(temp);
        for (int i = 0; i < temp.length; i++) {
            Cluster.getAllModel().setElementAt(temp[i], i);
        }

        //add column header
        for (String s : new String[]{"Cluster", "Tests Attempted", "Average %"}) {
            model.addColumn(s);
        }
        model.setColumnCount(3);
        model.setRowCount(list.size() + 1);

        for (int i = 0; i < list.size(); i++) {
            model.setValueAt(list.get(i).getName(), i, 0);
        }
        model.setValueAt("All", list.size(), 0);

    }

    /**
     * @return ArrayList of all the clusters.
     */
    public static ArrayList<Cluster> getList() {
        return list;
    }
}
