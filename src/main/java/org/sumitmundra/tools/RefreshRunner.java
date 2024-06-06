package org.sumitmundra.tools;

import org.tinylog.Logger;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class RefreshRunner extends TimerTask {

    public static final String PASSWORD_TEXT = "Password:";
    private final TrayIcon trayIcon;
    private String password;
    public static final String[] cmdArray = {"/bin/sh", "-c",
            "sudo -S powermetrics --samplers smc -i 1 -n 1 | grep -e \"temperature\" -e \"rpm\""};

    public RefreshRunner(TrayIcon trayIcon) {
        this.trayIcon = trayIcon;
    }

    @Override
    public void run() {
        if (trayIcon != null) {
            try {
                String tooltip = executeCommand();
                trayIcon.setToolTip(tooltip);
                Logger.debug("Tooltip updated with: \n{}", tooltip);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String executeCommand() throws InterruptedException, IOException {
        String password = openPasswordDialog();
        Process process = Runtime.getRuntime().exec(cmdArray);
        OutputStream outputStream = process.getOutputStream();
        outputStream.write(password.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder sb = new StringBuilder();
        sb.append(reader.lines()
                .collect(Collectors.joining("\n")));
        BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String error = errReader.lines()
                .filter(l -> (!l.startsWith(PASSWORD_TEXT))).collect(Collectors.joining("\n"));
        if (!error.isEmpty()) {
            Logger.error(error);
            sb.delete(0, sb.length());
            sb.append("Error while reading data, check logs at ${HOME}/trayicon.txt");
        }
        process.destroy();
        return sb.toString();
    }

    private String openPasswordDialog() {
        if (password != null) {
            return password;
        }
        PasswordDialog dialog = new PasswordDialog();
        password = dialog.getInputBuffer().toString();
        return password;
    }
}
