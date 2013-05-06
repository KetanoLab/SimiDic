package com.zomwi.simidic;

import com.zomwi.simidic.ui.MainFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws Exception {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        MainFrame v = new MainFrame();
        v.setVisible(true);
    }
}
