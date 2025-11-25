package jdc.zelda.entities;

import jdc.zelda.Game;
import jdc.zelda.world.Camera;
import jdc.zelda.world.Node;
import jdc.zelda.world.Vector2i;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class Entity {

    public static BufferedImage LIFEPOTION_EN;
    public static BufferedImage WEAPON_EN;
    public static BufferedImage WEAPON_LEFT;
    public static BufferedImage AMMO_EN;

    protected double x;
    protected double y;
    protected float width;
    protected float height;
    protected int z;

    public int depth;

    protected List<Node> path;

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

    public static Comparator<Entity> depthComparator = new Comparator<Entity>(){

        @Override
        public int compare(Entity o1, Entity o2) {
            if (o1.depth < o2.depth) return - 1;
            if (o1.depth > o2.depth) return + 1;
            return 0;
        }
    };

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

    public double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static boolean isColliding(Entity e1, Entity e2) {
        Rectangle rec1 = new Rectangle(e1.getX() + e1.getMaskX(), e1.getY() + e1.getMaskY(), e1.getMaskW(), e1.getMaskH());
        Rectangle rec2 = new Rectangle(e2.getX() + e2.getMaskX(), e2.getY() + e2.getMaskY(), e2.getMaskW(), e2.getMaskH());

        if (rec1.intersects(rec2) && e1.z == e2.z) {
            return true;
        }

        return false;
    }

    public void followPath(List<Node> path) {
        if (path != null) {
            if (!path.isEmpty()) {
                Vector2i target = path.get(path.size() - 1).tile;
                //xprev = x;
                //yprev = y;

                if (x < target.x * 16) x++;
                else if (x > target.x * 16) x--;

                if (y < target.y * 16) y++;
                else if (y > target.y * 16) y--;

                if (x == target.x * 16 && y == target.y * 16) {
                    path.remove(path.size() - 1);
                }
            }
        }
    }

    public boolean isColliding(int xnext, int ynext) {
        Rectangle currentEnemy = new Rectangle(xnext + getMaskX(), ynext + getMaskY(), getMaskW(), getMaskH());

        for (int i = 0; i < Game.enemies.size(); i++) {
            Enemy e = Game.enemies.get(i);
            if (e == this)
                continue;
            Rectangle targetEnemy = new Rectangle(e.getX() + getMaskX(), e.getY() + getMaskY(), getMaskW(), getMaskH());
            if (currentEnemy.intersects(targetEnemy)) return true;
        }

        return false;
    }

    public void render(Graphics g) {
        g.drawImage(sprite, getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight(), null);
        //g.setColor(Color.red);
        //g.fillRect(getX() + getMaskX() - Camera.x, getY() + getMaskY() - Camera.y, getMaskW(), getMaskH());
    }
}
