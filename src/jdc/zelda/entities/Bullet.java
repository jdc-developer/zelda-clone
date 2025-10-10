package jdc.zelda.entities;

import jdc.zelda.Game;
import jdc.zelda.world.Camera;

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
        x += dx * speed;
        y += dy * speed;
        curLife++;

        if (curLife == life) Game.bullets.remove(this);
    }

    public void render(Graphics g) {
        g.setColor(Color.yellow);
        g.fillOval(getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight());
    }
}
