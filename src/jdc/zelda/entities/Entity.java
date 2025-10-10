package jdc.zelda.entities;

import jdc.zelda.world.Camera;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {

    public static BufferedImage LIFEPOTION_EN;
    public static BufferedImage WEAPON_EN;
    public static BufferedImage WEAPON_LEFT;
    public static BufferedImage AMMO_EN;

    protected double x;
    protected double y;
    protected float width;
    protected float height;

    private BufferedImage sprite;

    protected float maskX, maskY, maskW, maskH;

    public Entity(double x, double y, float width, float height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;

        this.maskX = 0;
        this.maskY = 0;
        this.maskW = width;
        this.maskH = height;

        try {
            LIFEPOTION_EN = ImageIO.read(getClass().getResourceAsStream("/healing_potion.png"));
            WEAPON_EN = ImageIO.read(getClass().getResourceAsStream("/gun.png"));
            WEAPON_LEFT = ImageIO.read(getClass().getResourceAsStream("/gun-left.png"));
            AMMO_EN = ImageIO.read(getClass().getResourceAsStream("/ammo.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setMask(float maskX, float maskY, float maskW, float maskH) {
        this.maskX = maskX;
        this.maskY = maskY;
        this.maskW = maskW;
        this.maskH = maskH;
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

    public int getMaskX() {
        return (int) maskX;
    }

    public void setMaskX(float maskX) {
        this.maskX = maskX;
    }

    public int getMaskY() {
        return (int) maskY;
    }

    public void setMaskY(float maskY) {
        this.maskY = maskY;
    }

    public int getMaskW() {
        return (int) maskW;
    }

    public void setMaskW(float maskW) {
        this.maskW = maskW;
    }

    public int getMaskH() {
        return (int) maskH;
    }

    public void setMaskH(float maskH) {
        this.maskH = maskH;
    }

    public void tick() {

    }

    public static boolean isColliding(Entity e1, Entity e2) {
        Rectangle rec1 = new Rectangle(e1.getX() + e1.getMaskX(), e1.getY() + e1.getMaskY(), e1.getMaskW(), e1.getMaskH());
        Rectangle rec2 = new Rectangle(e2.getX() + e2.getMaskX(), e2.getY() + e2.getMaskY(), e2.getMaskW(), e2.getMaskH());

        return rec1.intersects(rec2);
    }

    public void render(Graphics g) {
        g.drawImage(sprite, getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight(), null);
        //g.setColor(Color.red);
        //g.fillRect(getX() + getMaskX() - Camera.x, getY() + getMaskY() - Camera.y, getMaskW(), getMaskH());
    }
}
