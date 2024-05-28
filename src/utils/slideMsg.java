package utils;
import javax.swing.*;

import bases.variables;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class slideMsg extends JFrame {
    private final float MAX_OPACITY = 0.8f;
    private final float OPACITY_INCREMENT = 0.05f;
    private final int FADE_REFRESH_RATE = 1;
    private final int WINDOW_RADIUS = 5;
    private final int CHARACTER_LENGTH_MULTIPLIER = 16;
    
    public slideMsg(JFrame owner, String toastText) {
        setLayout(new GridBagLayout());
        setUndecorated(true);
        setFocusableWindowState(false);
        setOpacity(0.1f);
        JLabel msgLabel = new JLabel(toastText);
        msgLabel.setIcon(new ImageIcon(slideMsg.class.getResource("/assets/noti.png")));
        Font font15 = new Font("Roboto", Font.BOLD, 16);
        msgLabel.setFont(font15);
        msgLabel.setForeground(Color.WHITE);
        msgLabel.setOpaque(false);
        add(msgLabel);
        setSize(toastText.length() * CHARACTER_LENGTH_MULTIPLIER, 40);
        int x = (int) (owner.getLocation().getX() + (owner.getWidth() / 2) - (getWidth() / 2));
        int y = (int) owner.getLocation().getY();
        setLocation(new Point(x, y));
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), WINDOW_RADIUS, WINDOW_RADIUS));
        getContentPane().setBackground(Color.decode(variables.primaryColor));
    }
    
    public void slideDown(int oneThirdScreenHeight) {
    	final Timer timer = new Timer(FADE_REFRESH_RATE, null);
        timer.setRepeats(true);
        final int targetY = oneThirdScreenHeight;
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Point currentLoc = getLocation();
                if (currentLoc.y <= targetY) {
                    setLocation(currentLoc.x, currentLoc.y + 6); 
                } else {
                    timer.stop();
                }
            }
        });
        timer.start();
    }
    
    public void slideUp() {
        final Timer timer = new Timer(FADE_REFRESH_RATE, null);
        timer.setRepeats(true);
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Point currentLoc = getLocation();
                if (currentLoc.y >= -getHeight()) {
                    setLocation(currentLoc.x, currentLoc.y - 2);
                } else {
                    timer.stop();
                    setVisible(false);
                    dispose();
                }
            }
        });
        timer.start();
    }

    public void fadeIn() {
        setOpacity(0);
        setVisible(true);
        final Timer timer = new Timer(FADE_REFRESH_RATE, null);
        timer.setRepeats(true);
        timer.addActionListener(new ActionListener() {
            private float opacity = 0;
            public void actionPerformed(ActionEvent e) {
                opacity += OPACITY_INCREMENT;
                setOpacity(Math.min(opacity, MAX_OPACITY));
                if (opacity >= MAX_OPACITY) {
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    public void fadeOut() {
        final Timer timer = new Timer(FADE_REFRESH_RATE, null);
        timer.setRepeats(true);
        timer.addActionListener(new ActionListener() {
            private float opacity = MAX_OPACITY;
            public void actionPerformed(ActionEvent e) {
                opacity -= OPACITY_INCREMENT;
                setOpacity(Math.max(opacity, 0));
                if (opacity <= 0) {
                    timer.stop();
                    setVisible(false);
                    dispose();
                }
            }
        });
        setOpacity(MAX_OPACITY);
        timer.start();
    }

    public static void create(final JFrame owner, final String toastText, final int waitSec) {
    	new Thread(new Runnable() {
            public void run() {
                try {
                    slideMsg toastFrame = new slideMsg(owner, toastText);
                    toastFrame.fadeIn();
                    int oneThirdScreenHeight = (int) (owner.getLocation().getY() + 80);
                    toastFrame.slideDown(oneThirdScreenHeight);
                    Thread.sleep(waitSec * 1000);
                    toastFrame.slideUp();
                    toastFrame.fadeOut();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }
}