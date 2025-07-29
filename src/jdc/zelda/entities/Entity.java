package jdc.zelda.entities;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    protected double x;
    protected double y;
    protected float width;
    protected float height;

    private BufferedImage sprite;

    public Entity(double x, double y, float width, float height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
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
        g.drawImage(sprite, (int)x, (int)y, (int)width, (int)height, null);
    }
}
