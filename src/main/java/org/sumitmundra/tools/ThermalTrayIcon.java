package org.sumitmundra.tools;

import org.tinylog.Logger;

import java.awt.*;
import java.net.URL;

public class ThermalTrayIcon {


    public static void main(String[] args) {
        TrayIcon trayIcon = null;
        boolean trayAdded = false;
        if (SystemTray.isSupported()) {
            Logger.info("System Tray is supported");
            SystemTray tray = SystemTray.getSystemTray();
            URL resource = ThermalTrayIcon.class.getResource("/Thermometer.png");
            Image image = Toolkit.getDefaultToolkit().getImage(resource);

            Logger.info("Found icon image");
            trayIcon = new TrayIcon(image, "Reading thermal state...");
            try {
                tray.add(trayIcon);
                trayAdded = true;
                Logger.debug("Tray icon added");
            } catch (AWTException e) {
                Logger.error("Failed to add tray icon");
                Logger.error(e);
            }
        }

        Long refreshRate = null;
        if (args.length > 0) {
            refreshRate = Long.parseLong(args[0]);
        }
        if (trayAdded) {
            new RefreshRunner(trayIcon, refreshRate).init();
        }
    }


}