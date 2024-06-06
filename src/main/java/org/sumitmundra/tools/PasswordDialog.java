package org.sumitmundra.tools;

import org.tinylog.Logger;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.awt.event.KeyEvent.VK_ENTER;

public class PasswordDialog {
    private final Dialog dialog;

    public StringBuffer getInputBuffer() {
        return inputBuffer;
    }

    private StringBuffer inputBuffer;
    private final TextField passwordField;

    PasswordDialog() {

        GraphicsConfiguration graphicsConfiguration = null;
        Frame frame = new Frame("ThermalTrayIcon", graphicsConfiguration);
        dialog = new Dialog(frame, "Input needed", true);
        dialog.setLayout(new FlowLayout());
        passwordField = new TextField(50);
        passwordField.setEchoChar('*');
        dialog.add(passwordField);
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_ENTER) {
                    String text = passwordField.getText();
                    inputBuffer = new StringBuffer(text);
                    dialog.setVisible(false);
                    dialog.getOwner().dispose();
                }
            }
        });
        Button okButton = new Button("OK");
        okButton.addActionListener(e -> {
            dialog.setVisible(false);
            dialog.getOwner().dispose();
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.addActionListener(e -> {
            dialog.setVisible(false);
            dialog.getOwner().dispose();
            System.exit(-1);
        });
        dialog.add(okButton);
        dialog.add(cancelButton);
        dialog.setSize(500, 100);
        dialog.setVisible(true);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Logger.debug("Closing window");
                super.windowClosing(e);
                System.exit(-1);
            }
        });

    }

}