package jdc.zelda.entities;

import jdc.zelda.Game;
import jdc.zelda.world.Camera;
import jdc.zelda.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    public static final float WIDTH = 40.0f;
    public static final float HEIGHT = 48f;
    public static final int SUBSCALE = 5;

    private float speed = 1.5f;

    private int frames = 0, maxFrames = 10, animationIndex = 0, maxAnimationIndex = 9;
    private BufferedImage[] sprites;

    public Enemy(double x, double y, float width, float height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        setMask(5, 5, 35, 43);
        sprites = new BufferedImage[11];

        for (int i = 0; i < 11; i++) {
            sprites[i] = Game.enemySpritesheet.getSprite(0 + (i*WIDTH), 72f, WIDTH, HEIGHT);
        }
    }

    public void tick() {
        if (!isCollidingWithPlayer()) {
            if (Game.rand.nextInt(100) < 30) {
                if (x < Game.player.getX() && World.isFree((int)(getX() + speed), getY()) &&
                        !isColliding((int)(getX() + speed), getY())) x+=speed;
                else if (x > Game.player.getX() && World.isFree((int)(getX() - speed), getY()) &&
                        !isColliding((int)(getX() - speed), getY())) x-=speed;

                if (y < Game.player.getY() && World.isFree(getX(), (int)(getY() + speed)) &&
                        !isColliding(getX(), (int)(getY() + speed))) y+=speed;
                else if (y > Game.player.getY() && World.isFree(getX(), (int)(getY() - speed)) &&
                        !isColliding(getX(), (int)(getY() - speed))) y-=speed;
            }
        } else {
            if (Game.rand.nextInt(100) < 20) {
                Game.player.life--;
                Player.isTakingDamage = true;
                System.out.println(Game.player.life);
            }

            //if (Game.player.life == 0) System.exit(1);
        }


        frames++;

        if (frames == maxFrames) {
            frames = 0;
            animationIndex++;

            if (animationIndex > maxAnimationIndex) animationIndex = 0;
        }

    }

    public void render(Graphics g) {
        //super.render(g);
        g.drawImage(sprites[animationIndex], getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight(), null);
        //g.setColor(Color.BLUE);
        //g.fillRect(getX() + maskx - Camera.x, getY() + masky - Camera.y, maskw, maskh);
    }

    public boolean isCollidingWithPlayer() {
        Rectangle currentEnemy = new Rectangle(getX() + getMaskX(), getY() + getMaskY(), getMaskW(), getMaskH());
        Rectangle playerRect = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);

        return currentEnemy.intersects(playerRect);
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
}
