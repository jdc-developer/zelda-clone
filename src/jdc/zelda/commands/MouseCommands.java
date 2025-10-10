package jdc.zelda.commands;

import jdc.zelda.Game;
import jdc.zelda.entities.Player;
import jdc.zelda.world.Camera;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseCommands implements MouseListener {

    private Player player;

    public MouseCommands(Player player) {
        this.player = player;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (player.ammo > 0 && player.hasGun) {
            player.isMouseShooting = true;
            player.mx = e.getX() / Game.SCALE;
            player.my = e.getY() / Game.SCALE;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
