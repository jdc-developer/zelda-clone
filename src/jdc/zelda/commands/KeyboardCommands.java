package jdc.zelda.commands;

import jdc.zelda.entities.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardCommands implements KeyListener {

    private Player player;

    public KeyboardCommands(Player player) {
        this.player = player;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT ||
                e.getKeyCode() == KeyEvent.VK_D) {
            player.setRight(true);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT ||
                e.getKeyCode() == KeyEvent.VK_A) {
            player.setLeft(true);
        }

        if (e.getKeyCode() == KeyEvent.VK_UP ||
                e.getKeyCode() == KeyEvent.VK_W) {
            player.setUp(true);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN ||
                e.getKeyCode() == KeyEvent.VK_S) {
            player.setDown(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT ||
                e.getKeyCode() == KeyEvent.VK_D) {
            player.setRight(false);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT ||
                e.getKeyCode() == KeyEvent.VK_A) {
            player.setLeft(false);
        }

        if (e.getKeyCode() == KeyEvent.VK_UP ||
                e.getKeyCode() == KeyEvent.VK_W) {
            player.setUp(false);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN ||
                e.getKeyCode() == KeyEvent.VK_S) {
            player.setDown(false);
        }
    }
}
