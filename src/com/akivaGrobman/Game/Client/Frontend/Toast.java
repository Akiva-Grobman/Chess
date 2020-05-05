package com.akivaGrobman.Game.Client.Frontend;

import javax.swing.*;
import java.awt.*;

public class Toast {

    private Toast(JComponent component, Point location, String message, long duration) {
        if(component != null) {
            if(location == null) {
                location = component.getLocationOnScreen();
            }
            Point finalLocation = location;
            new Thread(() -> {
                Popup view = null;
                try {
                    Label tip = new Label(message);
                    tip.setForeground(Color.red);
                    tip.setBackground(Color.white);
                    view = PopupFactory.getSharedInstance().getPopup(component, tip, finalLocation.x + 30, finalLocation.y + component.getHeight() + 5);
                    view.show();
                    Thread.sleep(duration);
                } catch (InterruptedException ignore) {}
                view.hide();
            }).start();
        }
    }

    public static void showToast(JComponent component, String message) {
        new Toast(component, null, message, 2000);
    }

    public static void showToast(JComponent component, String message, Point location, long duration) {
        new Toast(component, location, message, duration);
    }

}

