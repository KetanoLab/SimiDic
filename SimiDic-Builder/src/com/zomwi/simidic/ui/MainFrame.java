package com.zomwi.simidic.ui;

import com.zomwi.simidic.db.DataBase;
import com.zomwi.simidic.util.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class MainFrame extends JFrame implements PropertyChangeListener {

    // DateBase
    private DataBase dataBase;
    // Task
    private GeneratorDatabaseTask task;
    // Files
    private File wordsFile;
    // GUI Components **************************************************
    // Dialog
    private IfoDialog ifoDialog;
    // Panels
    private JPanel panelIfo;    // Information IFO file
    private JPanel panelTab;    // Information TAB file
    // FileChoosers
    private JFileChooser ifoFileChooser;
    private JFileChooser tabFileChooser;
    // Buttons
    private JButton openIfoButton;
    private JButton editIfoButton;
    private JButton openTabButton;
    private JButton generateButton;
    // Labels
    private JLabel nameLabel;
    private JLabel authorLabel;
    private JLabel beginLanguageLabel;
    private JLabel endLanguageLabel;
    private JLabel descriptionLabel;
    private JLabel infoTabLabel;
    // TextArea
    private JTextArea log;
    private JScrollPane logScrollPane;
    // ProgressBar
    private JProgressBar progressBar;
    // End GUI Components **************************************************

    public MainFrame() throws ClassNotFoundException, SQLException {
        ifoDialog = new IfoDialog(this);
        configWindow();
        initComponents();
        log.append(R.string.app_name + " " + R.string.app_version + "\n" + R.string.ketanolab + "\n" + R.string.app_author + "\n");
        log.append("------------------------------------\n");
    }

    private void configWindow() {
        setTitle(R.string.app_name);
        setLayout(null);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        panelIfo = new JPanel();
        panelTab = new JPanel();
        nameLabel = new JLabel();
        authorLabel = new JLabel();
        descriptionLabel = new JLabel();
        beginLanguageLabel = new JLabel();
        endLanguageLabel = new JLabel();
        ifoFileChooser = new JFileChooser();
        tabFileChooser = new JFileChooser();
        log = new JTextArea();
        logScrollPane = new JScrollPane(log);
        openIfoButton = new JButton();
        editIfoButton = new JButton();
        openTabButton = new JButton();
        generateButton = new JButton();

        // IFO Panel **************************************************
        panelIfo.setLayout(null);
        panelIfo.setBounds(10, 10, Constants.WIDTH_PANEL, 200);
        panelIfo.setBorder(BorderFactory.createTitledBorder(R.string.dictionary));
        // Extras
        JLabel nameInfoLabel = new JLabel(R.string.name, JLabel.RIGHT);
        JLabel authorInfoLabel = new JLabel(R.string.author, JLabel.RIGHT);
        JLabel descriptionInfoLabel = new JLabel(R.string.description, JLabel.RIGHT);
        JLabel beginLanguageInfoLabel = new JLabel(R.string.beginLanguage, JLabel.RIGHT);
        JLabel endLanguageInfoLabel = new JLabel(R.string.endLanguage, JLabel.RIGHT);
        nameInfoLabel.setBounds(20, 25, 65, Constants.HEIGHT_NORMAL);
        authorInfoLabel.setBounds(20, 45, 65, Constants.HEIGHT_NORMAL);
        descriptionInfoLabel.setBounds(20, 65, 65, Constants.HEIGHT_NORMAL);
        beginLanguageInfoLabel.setBounds(20, 85, 65, Constants.HEIGHT_NORMAL);
        endLanguageInfoLabel.setBounds(20, 105, 65, Constants.HEIGHT_NORMAL);
        nameInfoLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        authorInfoLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        descriptionInfoLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        beginLanguageInfoLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        endLanguageInfoLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        // Labels
        nameLabel.setBounds(90, 25, 190, Constants.HEIGHT_NORMAL);
        authorLabel.setBounds(90, 45, 190, Constants.HEIGHT_NORMAL);
        descriptionLabel.setBounds(90, 65, 190, Constants.HEIGHT_NORMAL);
        beginLanguageLabel.setBounds(90, 85, 190, Constants.HEIGHT_NORMAL);
        endLanguageLabel.setBounds(90, 105, 190, Constants.HEIGHT_NORMAL);
        // Buttons
        openIfoButton.setText(R.string.openIfoFile);
        openIfoButton.setBounds(50, 160, 90, Constants.HEIGHT_NORMAL);
        openIfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFileChooserIfo();
            }
        });
        editIfoButton.setText(R.string.edit);
        editIfoButton.setBounds(150, 160, 90, Constants.HEIGHT_NORMAL);
        editIfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openIfoDialog();
            }
        });
        // FileChooser
        FileFilter ifoFileFilter = new ExtensionFileFilter(R.string.ifoFiles, new String[]{"IFO"});
        ifoFileChooser.setFileFilter(ifoFileFilter);
        // Add
        panelIfo.add(nameInfoLabel);
        panelIfo.add(authorInfoLabel);
        panelIfo.add(descriptionInfoLabel);
        panelIfo.add(beginLanguageInfoLabel);
        panelIfo.add(endLanguageInfoLabel);
        panelIfo.add(nameLabel);
        panelIfo.add(authorLabel);
        panelIfo.add(descriptionLabel);
        panelIfo.add(beginLanguageLabel);
        panelIfo.add(endLanguageLabel);
        panelIfo.add(openIfoButton);
        panelIfo.add(editIfoButton);

        // TAB Panel **************************************************
        panelTab.setLayout(null);
        panelTab.setBounds(10, 210, Constants.WIDTH_PANEL, 80);
        panelTab.setBorder(BorderFactory.createTitledBorder(R.string.words));
        // Label
        infoTabLabel = new JLabel(R.string.select_words_file);
        infoTabLabel.setBounds(20, 20, 240, 20);
        // Button
        openTabButton.setText(R.string.open);
        openTabButton.setBounds(100, 40, 90, Constants.HEIGHT_NORMAL);
        openTabButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFileChooserTab();
            }
        });
        // FileChooser
        FileFilter tabFileFilter = new ExtensionFileFilter(R.string.tabOrTxtFiles, new String[]{"TXT", "TAB"});
        tabFileChooser.setFileFilter(tabFileFilter);
        // Add
        panelTab.add(infoTabLabel);
        panelTab.add(openTabButton);

        // Generate Button **************************************************
        generateButton.setText(R.string.generate);
        generateButton.setBounds(Constants.X1, 295, Constants.WIDTH_PANEL, 30);
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    generate();
                } catch (ClassNotFoundException e1) {
                    log.append("ERROR " + e1.toString() + "\n");
                } catch (SQLException e1) {
                    log.append("ERROR " + e1.toString() + "\n");
                }
            }
        });
        // Log **************************************************
        log.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        log.setEditable(false);
        logScrollPane.setBounds(Constants.X3, Constants.Y1, Constants.WIDTH_PANEL, 315);
        // ProgressBar **************************************************
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBounds(Constants.X1, 335, Constants.WIDTH_HIGH, 30);

        // Add general **************************************************
        add(panelIfo);
        add(panelTab);
        add(generateButton);
        add(logScrollPane);
        add(progressBar);
    }

    private void openIfoDialog() {
        ifoDialog.setData(nameLabel.getText(), authorLabel.getText(), descriptionLabel.getText(), beginLanguageLabel.getText(), endLanguageLabel.getText());
        ifoDialog.setVisible(true);
    }

    public void openFileChooserIfo() {
        int returnVal = ifoFileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = ifoFileChooser.getSelectedFile();
            log.append(R.string.fileOpened + ":\n - " + file.getName() + "\n");
            readFileIfo(file);
        } else {
            log.append(R.string.openFileCanceled + ".\n");
        }
        log.setCaretPosition(log.getDocument().getLength());
    }

    private void readFileIfo(File file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String str;
            while ((str = in.readLine()) != null) {
                String x = str.substring(str.indexOf("=") + 1, str.length()).trim();
                //mTextAreaLog.append(str + "\n");
                if (str.contains("bookname")) {
                    nameLabel.setText(x);
                }
                if (str.contains("author")) {
                    authorLabel.setText(x);
                }
                if (str.contains("description")) {
                    descriptionLabel.setText(x);
                }
            }
            in.close();
        } catch (IOException e) {
            log.append(R.string.failedToOpenFile + ".\n");
        }
    }

    public void openFileChooserTab() {
        int returnVal = tabFileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            wordsFile = tabFileChooser.getSelectedFile();
            log.append(R.string.fileOpened + ":\n - " + wordsFile.getName() + "\n");
            infoTabLabel.setText(wordsFile.getName());
        } else {
            log.append(R.string.openFileCanceled + ".\n");
        }
        log.setCaretPosition(log.getDocument().getLength());
    }

    public void generate() throws ClassNotFoundException, SQLException {
        if (requisite()) {
            // Performance
            generateButton.setEnabled(false);
            openIfoButton.setEnabled(false);
            openTabButton.setEnabled(false);
            editIfoButton.setEnabled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            // Get data
            String name = nameLabel.getText().trim();
            String author = authorLabel.getText().trim();
            String description = descriptionLabel.getText().trim();
            String beginLanguage = beginLanguageLabel.getText().trim();
            String endLanguage = endLanguageLabel.getText().trim();
            // Database

            String nameDatabase = Util.generateDatabaseName(beginLanguage, endLanguage, author);
            dataBase = new DataBase(nameDatabase);
            dataBase.create();
            dataBase.insertInfo(name, author, description, beginLanguage, endLanguage);
            dataBase.close();
            // Task
            task = new GeneratorDatabaseTask(wordsFile, log, nameDatabase, generateButton, openIfoButton, openTabButton, editIfoButton, this);
            task.addPropertyChangeListener(this);
            task.execute();
        } else {
            log.append(R.string.insertAllData + ".\n");
        }
    }

    private boolean requisite() {
        if (nameLabel.getText().length() > 0 &&
                authorLabel.getText().length() > 3 &&
                beginLanguageLabel.getText().length() > 3 &&
                endLanguageLabel.getText().length() > 3 &&
                wordsFile != null) {
            return true;
        }
        return false;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        }
    }

    public void setDataKernel(String name, String author, String description, String beginLanguage, String endLanguage) {
        nameLabel.setText(name);
        authorLabel.setText(author);
        descriptionLabel.setText(description);
        beginLanguageLabel.setText(beginLanguage);
        endLanguageLabel.setText(endLanguage);
    }

    public void generateJSON() {
        // Get data
        String name = nameLabel.getText().trim();
        String author = authorLabel.getText().trim();
        String description = descriptionLabel.getText().trim();
        String beginLanguage = beginLanguageLabel.getText().trim();
        String endLanguage = endLanguageLabel.getText().trim();

        String nameDatabase = Util.generateDatabaseName(beginLanguage, endLanguage, author);

        log.append("\nJSON Generado. -------------\n");
        log.append("    {\n");
        log.append("        \"name\": \"" + name + "\",\n");
        log.append("        \"author\": \"" + author + "\",\n");
        log.append("        \"description\": \"" + description + "\",\n");
        log.append("        \"file\": \"" + nameDatabase + ".db\",\n");
        log.append("        \"url\": \"?"  + "\",\n");
        log.append("        \"size\": \"?" + "\"\n");
        log.append("    }");

    }

}
