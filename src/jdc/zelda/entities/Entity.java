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

    public int getX() {
        return (int)x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public int getY() {
        return (int)y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return (int)width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public int getHeight() {
        return (int)height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(sprite, getX(), getY(), getWidth(), getHeight(), null);
    }
}
