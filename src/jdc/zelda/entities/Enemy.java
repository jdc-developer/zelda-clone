package jdc.zelda.entities;

import jdc.zelda.Game;
import jdc.zelda.world.World;

import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    private float speed = 0.6f;

    public Enemy(double x, double y, float width, float height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    public void tick() {
        if (x < Game.player.getX() && World.isFree((int)(getX() + speed), getY())) x+=speed;
        else if (x > Game.player.getX() && World.isFree((int)(getX() - speed), getY())) x-=speed;

        if (y < Game.player.getY() && World.isFree(getX(), (int)(getY() + speed))) y+=speed;
        else if (y > Game.player.getY() && World.isFree(getX(), (int)(getY() - speed))) y-=speed;
    }
}
