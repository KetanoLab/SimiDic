package com.zomwi.simidic.util;

import com.zomwi.simidic.db.DataBase;
import com.zomwi.simidic.ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.StringTokenizer;

public class GeneratorDatabaseTask extends SwingWorker<Void, Void> {

    private File file;
    private JTextArea log;
    private String nameDatabase;
    // Buttons
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    // JFrame
    private MainFrame frame;

    public GeneratorDatabaseTask(File file, JTextArea log, String nameDatabase, JButton button1, JButton button2, JButton button3, JButton button4, MainFrame frame) {
        this.file = file;
        this.log = log;
        this.nameDatabase = nameDatabase;
        this.button1 = button1;
        this.button2 = button2;
        this.button3 = button3;
        this.button4 = button4;
        this.frame = frame;
    }

    @Override
    public Void doInBackground() throws SQLException, ClassNotFoundException {
        setProgress(0);
        // Generate String
        String string = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                string += line + "\n";
            }
            in.close();
        } catch (IOException e) {
            log.append(R.string.failedToReadFile + ".\n");
        }
        log.append(R.string.generatedString + "...\n");

        // Count registers --------------------------------------------------
        int numberWords = 0;
        StringTokenizer stX = new StringTokenizer(string);
        while (stX.hasMoreTokens()) {
            stX.nextToken("\n");
            numberWords++;
        }
        log.append(numberWords + " " + R.string.registers + "...\n");

        // Really generate --------------------------------------------------
        int countNumberWords = 0;
        int percentage = 0;
        int natural = 1;
        StringTokenizer st = new StringTokenizer(string);
        DataBase dataBase = new DataBase(nameDatabase);

        while (st.hasMoreTokens()) {
            String linea = st.nextToken("\n");
            StringTokenizer st2 = new StringTokenizer(linea, "\t");
            String word = st2.nextToken();
            String meaning = st2.nextToken();
            // ---------------
            String summary = meaning;
            
            //ABB's code to convert Pango markup to plain text 
            summary = summary.replaceAll("&lt;", "<");
            summary = summary.replaceAll("&gt;", ">");
            summary = summary.replaceAll("&amp;", "&");
            summary = summary.replaceAll("\\n", " ");
            summary = summary.replaceAll("</?br>", " ");
            summary = summary.replaceAll("</?[a-zA-Z]+[^>]*>", ""); //strip all HTML codes like <...> </...>
            summary = summary.replaceAll(" {2,}", " "); 
            
            if (summary.length() > 48) {
            	summary = truncate(summary, 45) + "...";
            }
                     
            meaning = meaning.replaceAll("<span +(foreground|color|fgcolor)([^<]+)</span>", "<font color$2</font>");
            meaning = meaning.replaceAll("<span +(font_size|size)([^<]+)</span>", "<font size$2</font>");
            meaning = meaning.replaceAll("<span +(font_family|face)([^<]+)</span>", "<font face$2</font>");
            meaning = meaning.replaceAll("\\n", "<br>");

            //if (str1.toLowerCase().contains(str2.toLowerCase())
            //ABB code end


            dataBase.insertWord(word, meaning, summary); // insert word
            countNumberWords++;

            percentage = (int) (countNumberWords * 100 / numberWords);
            setProgress(percentage);

            if (percentage == natural) {
                log.append(countNumberWords + " " + R.string.wordsGenerated + " (" + percentage + "%)...\n");
                log.setCaretPosition(log.getDocument().getLength());
                natural++;
            }
        }
        dataBase.close();

        return null;
    }

    public static String truncate(String value, int length) {
        if (value != null && value.length() > length)
            value = value.substring(0, length);
        return value;
    }

    @Override
    public void done() {
        Toolkit.getDefaultToolkit().beep();
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        button4.setEnabled(true);
        frame.setCursor(null); // turn off the wait cursor
        log.append(R.string.finalized + ".\n");
        log.setCaretPosition(log.getDocument().getLength());
        frame.generateJSON(); // MainFrame
    }
}
