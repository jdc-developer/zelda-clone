package jdc.zelda.entities;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    private int x;
    private int y;
    private float width;
    private float height;

    private BufferedImage sprite;

    public Entity(int x, int y, float width, float height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(sprite, x, y, null);
    }
}
