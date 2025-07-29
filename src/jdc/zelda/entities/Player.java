package jdc.zelda.entities;

import java.awt.image.BufferedImage;

public class Player extends Entity {

    public boolean right, up, left, down;
    public float speed = 2.5f;

    public Player(int x, int y, float width, float height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    public void tick() {
        if (right) x+=speed;
        else if (left) x-=speed;

        if (up) y-=speed;
        else if (down) y+=speed;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
}
