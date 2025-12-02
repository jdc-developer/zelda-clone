package jdc.zelda.entities;

import jdc.zelda.Game;
import jdc.zelda.world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Particle extends Entity {

    public int lifeTime = 15;
    public int curLife = 0;

    public int speed = 2;
    public double dx;
    public double dy;

    public Particle(double x, double y, float width, float height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        dx = Game.rand.nextGaussian();
        dy = Game.rand.nextGaussian();
    }

    @Override
    public void tick() {
        x += dx*speed;
        y += dy * speed;
        curLife++;
        if (lifeTime == curLife) Game.entities.remove(this);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight());
    }
}
