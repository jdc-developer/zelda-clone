package jdc.zelda.entities;

import jdc.zelda.Game;
import jdc.zelda.world.Camera;
import jdc.zelda.world.World;

import java.awt.*;

public class Bullet extends Entity {

    private double dx;
    private double dy;
    private double speed = 4;
    private int life = 50, curLife = 0;

    public Bullet(double x, double y, float width, float height, double dx, double dy) {
        super(x, y, width, height, null);
        this.dx = dx;
        this.dy = dy;
    }

    public void tick() {
        if (World.isFreeDynamic((int)(x + (dx*speed)), (int)(y + (dy * speed)), 3, 3)) {
            x += dx * speed;
            y += dy * speed;
        } else {

            Game.bullets.remove(this);
            //World.generateParticles(5, getX(), getY());
            return;
        }

        curLife++;

        if (curLife == life) Game.bullets.remove(this);
    }

    public void render(Graphics g) {
        g.setColor(Color.yellow);
        g.fillOval(getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight());
    }
}
