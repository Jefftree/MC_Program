/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mc.program;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author
 * nathanli
 */
public class Test {

    private String inputFile;
    ArrayList<Question> questionList = new ArrayList<Question>();

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public ArrayList<Question> getQuestionList() {
        return questionList;
    }
    
    public static Boolean isLetter(String input)
    {
        return ("A".equals(input) || "B".equals(input) || "C".equals(input) || "D".equals(input) || "E".equals(input));
    }
    

    public Test(String file, String cluster) throws FileNotFoundException {
        inputFile = file;

        Scanner input2 = new Scanner(new FileInputStream("resources/" + cluster + "/" + file));
        while (input2.hasNextLine()) {
            String q = input2.nextLine();
            String c = input2.nextLine();
            ArrayList<String> choice = new ArrayList<String>();

            while (c.length() > 1 || !isLetter(c)) {
                choice.add(c);
                c = input2.nextLine();
            }
            String ansL = c;
            String ansD = input2.nextLine();
            Question aQuestion = new Question(q, choice, ansL, ansD);
            questionList.add(aQuestion);
        }
        input2.close();

    }

    public Test(Test[] a, String cluster) {
        inputFile = cluster;

        for (Test t : a) {
            //for each question within a test
            for (Question s : t.getQuestionList()) {
                questionList.add(s);
            }
        }
    }

    public void shuffle() {
        long seed = System.nanoTime();
        Collections.shuffle(questionList, new Random(seed));
    }
}
