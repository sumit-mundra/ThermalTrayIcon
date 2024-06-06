package org.sumitmundra.tools;

import org.tinylog.Logger;

import java.awt.*;
import java.net.URL;
import java.util.Timer;

public class ThermalTrayIcon {

    public static final Timer timer = new Timer("TooltipRefreshTimer", false);
    public static final long DEFAULT_REFRESH_RATE = 5000L;

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
        if (trayAdded) {
            long refreshRate = DEFAULT_REFRESH_RATE;
            if (args.length > 0) {
                refreshRate = Long.parseLong(args[0]);
            }
            timer.scheduleAtFixedRate(new RefreshRunner(trayIcon), 100L, refreshRate);
        }
    }


}