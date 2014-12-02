/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mc.program;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 *
 * @author nathanli
 */
public class Importer
{

    Boolean processedIdentifiers = false;
    String[] errors = new String[0];

    File sourceFile;

    int numberOfQuestions;
    int maxNumberOfOptions;
    String questionIdentifier;
    String optionIdentifier;
    Boolean upperOption;
    String answerIdentifier;
    String answerFormat;

    String[] questions;
    String[][] options;
    String[] answerLetters;
    String[] answerExplanations;

    ArrayList<String> sourceText = new ArrayList(0);

    public Importer(File file)
    {
        sourceFile = file;
    }

    public void importTest()
    {
        String fileName = sourceFile.getName();
        String fileType = fileName.substring(fileName.indexOf(".") + 1);
        fileType = fileType.toLowerCase();

        switch (fileType)
        {
            case "pdf":
                importPDF();
                break;
            case "txt":
                importTXT();
                break;
            case "doc":
                importDOC();
                break;
            case "docx":
                importDOCX();
                break;
        }
    }

    public void importPDF()
    {
        try
        {
            PDDocument pdf = PDDocument.load(sourceFile);

            PDFTextStripper stripper = new PDFTextStripper();

            String fileData = stripper.getText(pdf);

            InputStream stream = new ByteArrayInputStream(fileData.getBytes(StandardCharsets.UTF_8));
            Scanner scanner = new Scanner(stream);
            while (scanner.hasNext())
            {
                sourceText.add(scanner.next());
            }
            
            pdf.close();
            stream.close();
            scanner.close();
        }
        catch (Exception ex)
        {
            System.out.print(ex);
        }
    }

    public void importTXT()
    {
        try
        {
            Scanner scanner = new Scanner(sourceFile);

            while (scanner.hasNext())
            {
                sourceText.add(scanner.next()); // Removes spaces
            }
            
            scanner.close();
        }
        catch (Exception ex)
        {
            System.out.print(ex);
        }
    }

    public void importDOC()
    {
        try
        {
            // Set up objects for getting from .doc file
            FileInputStream fis = new FileInputStream(sourceFile.getAbsolutePath());
            HWPFDocument document = new HWPFDocument(fis);
            WordExtractor extractor = new WordExtractor(document);

            // Extract text
            String[] fileData = extractor.getParagraphText();

            // Put text into array list
            for (String fileData1 : fileData)
            {
                Scanner scanner = new Scanner(fileData1);
                while (scanner.hasNext())
                {
                    sourceText.add(scanner.next());
                }
            }
            
            fis.close();
            extractor.close();
        }
        catch (Exception ex)
        {
            System.out.print(ex);
        }
    }

    public void importDOCX()
    {
        try
        {
            // Set up objects for getting from .docx file
            FileInputStream fis = new FileInputStream(sourceFile.getAbsolutePath());
            XWPFDocument document = new XWPFDocument(fis);
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);

            // Extract text
            String fileData = extractor.getText();

            // Put text into array list
            Scanner scanner = new Scanner(fileData);
            while (scanner.hasNext())
            {
                sourceText.add(scanner.next());
            }

            fis.close();
            extractor.close();
        }
        catch (Exception ex)
        {
            System.out.print(ex);
        }
    }

    public String getPreview(int num)
    {
        String output = "";

        for (int i = 0;i < Math.min(num, sourceText.size());i++)
        {
            output += sourceText.get(i);
            output += " ";
        }

        return output;
    }

    public void removeSpaces()
    {
        questionIdentifier = questionIdentifier.trim();
        optionIdentifier = optionIdentifier.trim();
        answerIdentifier = answerIdentifier.trim();
    }

    public void removeFirstCharacter()
    {
        // [65-90] are upper case letters
        // [97,122] are lower case letters
        int ascii = (int) optionIdentifier.charAt(0);
        upperOption = (ascii >= 65 && ascii <= 90);

        questionIdentifier = questionIdentifier.substring(1);
        optionIdentifier = optionIdentifier.substring(1);
        answerIdentifier = answerIdentifier.substring(1);
    }

    public int getIndexAnswerLetter(String input, String[] letters)
    {
        for (int i = 0;i < maxNumberOfOptions;i++)
        {
            // If the input is the identifier
            if (input.equals(letters[i] + optionIdentifier))
            {
                return i;
            }
        }
        return -1;
    }

    public Boolean convertTest()
    {
        try
        {
            // Instantiate the arrays for holding the test information
            questions = new String[numberOfQuestions];
            options = new String[numberOfQuestions][maxNumberOfOptions];
            
            for (int i = 0; i < numberOfQuestions; i++)
            {
                for (int ii = 0; ii < maxNumberOfOptions; ii++)
                {
                    options[i][ii] = "";
                }
            }
            
            answerLetters = new String[numberOfQuestions];
            answerExplanations = new String[numberOfQuestions];

            // Process the identifiers to remove the provided digit and any spaces
            if (!processedIdentifiers)
            {
                removeSpaces();
                removeFirstCharacter();
                processedIdentifiers = true;
            }

            // Remove any text at the beginning that isn't part of the questions
            while (!sourceText.get(0).equals("1" + questionIdentifier))
            {
                sourceText.remove(0);
            }

            // Start adding questions to the arrays
            // Variables for adding questions to arrays
            int currentQuestion = 0;
            int index = 0;
            int toDo = -1;
            String[] letter = new String[maxNumberOfOptions];

            for (int i = 0;i < maxNumberOfOptions;i++)
            {
                if (upperOption)
                {
                    letter[i] = String.valueOf((char) (65 + i));
                }
                else
                {
                    letter[i] = String.valueOf((char) (97 + i));
                }
            }

            // <editor-fold defaultstate="collapsed" desc="Code for if the MC was written with options in order, just holding it here because it took a long time to write">
            /*
             // Exit this loop (and start answer processing) when
             // the last question is taken in
             while (true)
             {
             // All questions before the last question
             if (currentQuestion < numberOfQuestions - 1)
             {
             // Get current question
             if (sourceText.get(index).equals((currentQuestion + 1) + questionIdentifier))
             {
             // This is the case of encountering the current question number
             // Advance to next entry
             index++;

             // Add to question until we reach the first option
             while (!sourceText.get(index).equals(letter[currentOption] + optionIdentifier))
             {
             questions[currentQuestion] += sourceText.get(index);
             index++;
             }

             // Add every option before the last option (prevents arrayOutOfBounds in nested while statement 
             for (int i = 0;i < maxNumberOfOptions - 1;i++)
             {
             // Add to option until we reach next option or next question
             while (!(sourceText.get(index).equals(letter[currentOption + 1] + optionIdentifier) || sourceText.get(index).equals((currentQuestion + 2) + questionIdentifier)))
             {
             options[currentQuestion][currentOption] += sourceText.get(index);
             index++;
             }
             currentOption++;
             }

             // Add last option until we reach the next question
             while (!sourceText.get(index).equals((currentQuestion + 2) + questionIdentifier))
             {
             options[currentQuestion][currentOption] += sourceText.get(index);
             index++;
             }

             // Reset current option
             currentOption = 0;

             // Advance to next question
             currentQuestion++;
             }
             }
             else // Last question
             {
             // Get current question
             if (sourceText.get(index).equals((currentQuestion + 1) + questionIdentifier))
             {
             // This is the case of encountering the current question number
             // Add to question until we reach the first option
             while (!sourceText.get(index).equals(letter[currentOption] + optionIdentifier))
             {
             questions[currentQuestion] += sourceText.get(index);
             index++;
             }

             // Add every option before the last option (prevents arrayOutOfBounds in nested while statement 
             for (int i = 0;i < maxNumberOfOptions - 1;i++)
             {
             // Add to option until we reach next option or first answer
             while (!(sourceText.get(index).equals(letter[currentOption + 1] + optionIdentifier) || sourceText.get(index).equals("1" + answerIdentifier)))
             {
             options[currentQuestion][currentOption] += sourceText.get(index);
             index++;
             }
             currentOption++;
             }

             // Add last option until we reach the first answer
             while (!sourceText.get(index).equals("1" + answerIdentifier))
             {
             options[currentQuestion][currentOption] += sourceText.get(index);
             index++;
             }

             // Got the last question, so leave the loop
             // index is currently the position of the first answer identifier
             break;
             }
             }
             }
             */
            // </editor-fold>
            
            // Process questions and options
            for (;index < sourceText.size();index++)
            {
                // Check for current question
                if (sourceText.get(index).equals(Integer.toString(currentQuestion + 1) + questionIdentifier)) // Is the question number
                {
                    toDo = -1;
                }
                // Check for next question
                else if (sourceText.get(index).equals(Integer.toString(currentQuestion + 2) + questionIdentifier)) // Is the next number
                {
                    toDo = -1;
                    currentQuestion++;
                }
                // Check for answer letter
                else if (getIndexAnswerLetter(sourceText.get(index), letter) >= 0)
                {
                    toDo = getIndexAnswerLetter(sourceText.get(index), letter);
                }
                // Means none of the above, thus must be actual info
                else
                {
                    if (toDo == -1)
                    {
                        questions[currentQuestion] += sourceText.get(index) + " ";
                    }
                    else
                    {
                        options[currentQuestion][toDo] += sourceText.get(index) + " ";
                    }
                }

                // Checks if it's the last question and we encountered the first answer identifier
                if (currentQuestion + 1 == numberOfQuestions && sourceText.get(index + 1).equals("1" + answerIdentifier))
                {
                    index++; // Puts current index on the first answer identifier
                    break;
                }
            }

            // Process answers
            // Start adding answers
            // Variable for adding answers to arrays
            int currentAnswer = 0;

            // Starting from the first answer identifier, until the end of the array list
            while (index < sourceText.size())
            {
                // Get current answer letter
                if (sourceText.get(index).equals((currentAnswer + 1) + answerIdentifier))
                {
                    // Reached the current answer identifier
                    // Advance to next entry to get input
                    index++;

                    // Get the answer letter
                    answerLetters[currentAnswer] = sourceText.get(index);

                    // Convert to upper case
                    answerLetters[currentAnswer] = answerLetters[currentAnswer].toUpperCase();

                    // Currently on the answer letter
                    // Advance to next entry
                    index++;

                    // Depending on the answer format, we add an explanation
                    if (answerFormat.equals("Answer"))
                    {
                        answerExplanations[currentAnswer] = "No explanation.";
                        // Means index is on the next answer identifier
                    }
                    else
                    {
                        // If we're not on the last question, we go until we find the next identifier
                        if (currentAnswer < numberOfQuestions - 1)
                        {
                            while (!sourceText.get(index).equals((currentAnswer + 2) + answerIdentifier))
                            {
                                answerExplanations[currentAnswer] += sourceText.get(index) + " ";
                                index++;
                            }
                            // Currently index is on the next answer identifier
                        }
                        else // we're on the last answer
                        {
                            while (index < sourceText.size())
                            {
                                answerExplanations[currentAnswer] += sourceText.get(index) + " ";
                                index++;
                            }
                        }
                    }
                    // Advance to next answer
                    currentAnswer++;
                }

            }
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return false;
        }
    }

    public String checkName(String desiredPathString)
    {
        if (new File(desiredPathString).isFile())
        {
            // Get the directory path for the file
            String directory = desiredPathString.substring(0, desiredPathString.lastIndexOf("/"));

            // Get the file name for the file including extension
            String fileName = desiredPathString.substring(desiredPathString.lastIndexOf("/") + 1);

            // Check if there is a number attached in the form -#
            if (fileName.lastIndexOf("-") > 0) // Checks for the -
            {
                // Check for a number between the "-" and "." 
                String possibleNumber = fileName.substring(fileName.lastIndexOf("-") + 1, fileName.lastIndexOf("."));

                try
                {
                    int number = Integer.parseInt(possibleNumber);
                    number++;
                    fileName = fileName.substring(0, fileName.lastIndexOf("-")) + "-" + number + fileName.substring(fileName.lastIndexOf("."), fileName.length());
                }
                catch (Exception e)
                {
                    // Not the numbering we use, so use the base case
                    fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "-1" + fileName.substring(fileName.lastIndexOf("."), fileName.length());
                }
            }
            else // No previous numbering attached, so add the base case of -1
            {
                fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "-1" + fileName.substring(fileName.lastIndexOf("."), fileName.length());
            }

            // Check this new name to see if it works, recursive until we get something we can use
            return checkName(directory + "/" + fileName);
        }
        else
        {
            return desiredPathString;
        }
    }

    public Boolean saveTest(File saveFile)
    {
        String newSourceFileName = sourceFile.getName();

        // Change to .txt file
        newSourceFileName = newSourceFileName.substring(0, newSourceFileName.lastIndexOf("."));
        newSourceFileName += ".txt";
        String filePath = checkName(saveFile.getPath() + "/" + newSourceFileName);
        File destination = new File(filePath);
        try
        {
            PrintWriter pw = new PrintWriter(destination);

            for (int i = 0;i < numberOfQuestions;i++)
            {
                // Print question
                pw.println(questions[i].substring(4));

                // Print options
                for (int option = 0;option < maxNumberOfOptions;option++)
                {
                    // If the option isn't empty, we'll print it
                    if (!options[i][option].equals(""))
                    {
                        pw.println(options[i][option] + "\u200B"); // add space to ensure length  > 1
                    }
                    else
                    {
                        break;
                    }
                }

                // Print answer letter
                pw.println(answerLetters[i]);

                // Print answer explanation
                pw.println(answerExplanations[i].substring(4));
            }

            pw.close();

            return true;
        }
        catch (Exception ex)
        {
            System.out.println(ex);
            
            // Get rid of the file made since we failed
            destination.delete();
            return false;
        }
    }

    public void setNumberOfQuestions(int numberOfQuestions)
    {
        this.numberOfQuestions = numberOfQuestions;
    }

    public void setMaxNumberOfOptions(int maxNumberOfOptions)
    {
        this.maxNumberOfOptions = maxNumberOfOptions;
    }

    public void setQuestionIdentifier(String questionIdentifier)
    {
        this.questionIdentifier = questionIdentifier;
    }

    public void setOptionIdentifier(String optionIdentifier)
    {
        this.optionIdentifier = optionIdentifier;
    }

    public void setAnwserIdentifier(String anwserIdentifier)
    {
        this.answerIdentifier = anwserIdentifier;
    }

    public void setAnswerFormat(String answerFormat)
    {
        this.answerFormat = answerFormat;
    }

}
