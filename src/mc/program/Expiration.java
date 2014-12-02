/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mc.program;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nathanli
 */
public class Expiration
{

    String user = "Guest";
    Calendar startDate = new GregorianCalendar();
    Calendar endDate = new GregorianCalendar(2015, 5, 30, 23, 23, 59);
    int randomKey = 6969;
    int systemKey = 6969;
    Boolean first = false;
    ArrayList<String> error = new ArrayList();

    public Expiration()
    {
        try
        {
            try
            {
                Scanner input = new Scanner(new FileInputStream("config/expiration.txt"));

                user = input.nextLine();

                startDate.setTime(new Date(decrypt(ReadIn(input, Integer.parseInt(input.nextLine())), user)));
                endDate.setTime(new Date(decrypt(ReadIn(input, Integer.parseInt(input.nextLine())), user)));
                randomKey = Integer.parseInt(decrypt(ReadIn(input, Integer.parseInt(input.nextLine())), user));
                first = Boolean.parseBoolean(decrypt(ReadIn(input, Integer.parseInt(input.nextLine())), user));

                input.close();
            }
            catch (Exception ex)
            {
                error.add("No/bad expiration file");
            }

            if (first)
            {
                try
                {
                    if (new File(getWorkingDirectory() + "/DECA MC Program/DO NOT DELETE.txt").exists())
                    {
                        error.add("System key exists before first run");
                    }
                    else // System key doesn't exist, so we make one
                    {
                        createSystemKeyFile();
                    }
                }
                catch (Exception e)
                {
                    error.add("No/bad system key file creation");
                }
            }
            else
            {
                try
                {
                    String access = getWorkingDirectory() + "/DECA MC Program/DO NOT DELETE.txt";
                    Scanner scan = new Scanner(new File(access));
                    systemKey = scan.nextInt();
                    scan.close();
                }
                catch (Exception ex)
                {
                    error.add("No/bad system key file");
                }

            }
        }
        catch (Exception e)
        {
        }
    }

    public void resetError()
    {
        error.removeAll(error);
    }

    public void setUser(String a)
    {
        user = a;
    }

    private String getWorkingDirectory()
    {
        String os = System.getProperty("os.name").toLowerCase();

        String wD = "";

        if (os.contains("win"))
        {
            wD = System.getenv("AppData");
        }
        else if (os.contains("mac"))
        {
            wD = System.getProperty("user.home") + "/Library/Application Support";
        }

        return wD;
    }

    public final void createSystemKeyFile()
    {

        File f = new File(getWorkingDirectory() + "/DECA MC Program");
        f.mkdirs();

        try
        {
            Random random = new Random();
            int temp = random.nextInt();
            temp = Math.abs(temp);

            PrintWriter writer = new PrintWriter(new FileOutputStream(getWorkingDirectory() + "/DECA MC Program/DO NOT DELETE.txt", false));
            writer.print(temp);
            systemKey = temp;
            randomKey = temp;
            writer.close();

            printExpirationFile();
        }
        catch (FileNotFoundException ex)
        {
            //System.out.println ("createSystemKeyFile error");
        }
    }

    private ArrayList<Integer> ReadIn(Scanner scanner, int number)
    {
        ArrayList<Integer> result = new ArrayList();

        for (int i = 0; i < number; i++)
        {
            result.add(Integer.parseInt(scanner.nextLine()));
        }

        return result;
    }

    private static void PrintOut(PrintWriter printWriter, ArrayList<Integer> output)
    {
        printWriter.println(output.size());
        for (Integer output1 : output)
        {
            printWriter.println(output1);
        }
    }

    public Integer sum(String input)
    {
        int value = 0;

        for (int i = 0;i < input.length();i++)
        {
            value += (int) input.charAt(i);
        }

        return value;
    }

    public ArrayList<Integer> encrypt(String input, String key)
    {
        ArrayList<Integer> result = new ArrayList();

        for (int i = 0;i < input.length();i++)
        {
            result.add((int) input.charAt(i) + sum(key));
        }

        return result;
    }

    private String decrypt(ArrayList<Integer> input, String key)
    {
        String result = "";

        for (Integer input1 : input)
        {
            int letter = input1 - sum(key);
            result += (char) letter;
        }

        return result;
    }

    public void printExpirationFile()
    {

        try
        {
            startDate = new GregorianCalendar();
            PrintWriter writer = new PrintWriter(new FileOutputStream("config/expiration.txt", false));

            writer.println(user);
            PrintOut(writer, encrypt(startDate.getTime().toString(), user));
            PrintOut(writer, encrypt(endDate.getTime().toString(), user));
            PrintOut(writer, encrypt(Integer.toString(randomKey), user));
            PrintOut(writer, encrypt("false", user));

            writer.close();
        }
        catch (Exception ex)
        {
            error.add("No/bad printout");
        }
    }

    public Boolean checkValidity()
    {
        try
        {
            if (error.size() > 0)
            {
                System.out.println("array");
                return false;
            }
            else
            {
                Calendar accessDate = new GregorianCalendar();

                if (startDate.before(accessDate) && endDate.after(accessDate))
                {
                    if (randomKey == systemKey)
                    {
                        return true;
                    }
                    else
                    {
                        error.add("Keys don't match");
                        return false;
                    }
                }
                else
                {
                    error.add("Outside of use time");
                    return false;
                }
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
