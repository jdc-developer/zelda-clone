package jdc.zelda.entities;

import jdc.zelda.ZeldaGame;
import jdc.zelda.world.Camera;
import jdc.zelda.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    public static final float WIDTH = 90.0f;
    public static final float HEIGHT = 97.5f;
    public static final int SUBSCALE = 5;

    public boolean right, up, left, down;
    public int rightDir = 0, leftDir = 1;
    public int dir = rightDir;
    public float speed = 1.5f;

    private int frames = 0, maxFrames = 5, animationIndex = 0, maxAnimationIndex = 9;
    private boolean moved = false;
    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;

    public Player(int x, int y, BufferedImage sprite) {
        super(x, y, WIDTH / SUBSCALE, HEIGHT / SUBSCALE, sprite);

        rightPlayer = new BufferedImage[10];
        leftPlayer = new BufferedImage[10];

        for (int i = 0; i < 10; i++) {
            rightPlayer[i] = ZeldaGame.spritesheet.getSprite(0 + (i*WIDTH), 682.5f, WIDTH, HEIGHT);
        }

        for (int i = 0; i < 10; i++) {
            leftPlayer[i] = ZeldaGame.spritesheet.getSprite(0 + (i*WIDTH), 487.5f, WIDTH, HEIGHT);
        }

    }

    public void tick() {
        moved = false;
        if (right) {
            moved = true;
            dir = rightDir;
            x += speed;
        }
        else if (left) {
            moved = true;
            dir = leftDir;
            x -= speed;
        }

        if (up) {
            moved = true;
            y -= speed;
        }
        else if (down) {
            moved = true;
            y += speed;
        }

        if (moved) {
            frames++;

            if (frames == maxFrames) {
                frames = 0;
                animationIndex++;

                if (animationIndex > maxAnimationIndex) animationIndex = 0;
            }
        }

        Camera.x = Camera.clamp(this.getX() - (ZeldaGame.WIDTH / 2), 0, World.WIDTH*16 - ZeldaGame.WIDTH);
        Camera.y = Camera.clamp(this.getY() - (ZeldaGame.HEIGHT / 2), 0, World.HEIGHT*16 - ZeldaGame.HEIGHT);
    }

    public void render(Graphics g) {
        if (dir == rightDir) {
            g.drawImage(rightPlayer[animationIndex], getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight(), null);
        } else if (dir == leftDir) {
            g.drawImage(leftPlayer[animationIndex], getX() - Camera.x, getY() -  Camera.y, getWidth(), getHeight(),null);
        } else {

        }

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
