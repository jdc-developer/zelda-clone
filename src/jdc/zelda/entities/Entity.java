package jdc.zelda.entities;

import jdc.zelda.world.Camera;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {

    public static BufferedImage LIFEPOTION_EN;
    public static BufferedImage WEAPON_EN;
    public static BufferedImage ENEMY_EN;
    public static BufferedImage AMMO_EN;

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

        try {
            LIFEPOTION_EN = ImageIO.read(getClass().getResourceAsStream("/healing_potion.png"));
            WEAPON_EN = ImageIO.read(getClass().getResourceAsStream("/gun.png"));
            ENEMY_EN = ImageIO.read(getClass().getResourceAsStream("/enemy.png"));
            AMMO_EN = ImageIO.read(getClass().getResourceAsStream("/ammo.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        g.drawImage(sprite, getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight(), null);
    }
}
