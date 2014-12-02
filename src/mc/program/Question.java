/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mc.program;

import java.util.ArrayList;

/**
 *
 * @author Jeffrey
 */
public class Question
{

    private String question;
    private ArrayList<String> choices = new ArrayList<String>();
    private String ansLetter;
    private String ansDescription;

    public Question(String q, ArrayList<String> choice, String ansLet, String ansDes)
    {
        question = q;
        choices = choice;
        ansLetter = ansLet;
        ansDescription = ansDes;
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public ArrayList<String> getChoices()
    {
        return choices;
    }

    public void setChoices(ArrayList<String> choices)
    {
        this.choices = choices;
    }

    public String getAnsLetter()
    {
        return ansLetter;
    }

    public void setAnsLetter(String ansLetter)
    {
        this.ansLetter = ansLetter;
    }

    public String getAnsDescription()
    {
        return ansDescription;
    }

    public void setAnsDescription(String ansDescription)
    {
        this.ansDescription = ansDescription;
    }
}
