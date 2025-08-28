package jdc.zelda.entities;

import jdc.zelda.Game;
import jdc.zelda.world.Camera;
import jdc.zelda.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    public static final float MAX_LIFE = 100f;
    public static final float WIDTH = 90.0f;
    public static final float HEIGHT = 97.5f;
    public static final int SUBSCALE = 5;

    public boolean right, up, left, down;
    public int rightDir = 0, leftDir = 1;
    public int dir = rightDir;
    public float speed = 1.5f;
    public static float life = MAX_LIFE;

    private int frames = 0, maxFrames = 5, animationIndex = 0, maxAnimationIndex = 9;
    private boolean moved = false;
    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;

    public Player(int x, int y, BufferedImage sprite) {
        super(x, y, WIDTH / SUBSCALE, HEIGHT / SUBSCALE, sprite);

        rightPlayer = new BufferedImage[10];
        leftPlayer = new BufferedImage[10];

        for (int i = 0; i < 10; i++) {
            rightPlayer[i] = Game.spritesheet.getSprite(0 + (i*WIDTH), 682.5f, WIDTH, HEIGHT);
        }

        for (int i = 0; i < 10; i++) {
            leftPlayer[i] = Game.spritesheet.getSprite(0 + (i*WIDTH), 487.5f, WIDTH, HEIGHT);
        }

    }

    public void tick() {
        moved = false;
        if (right && World.isFree((int)(getX() + speed), getY())) {
            moved = true;
            dir = rightDir;
            x += speed;
        }
        else if (left && World.isFree((int)(getX() - speed), getY())) {
            moved = true;
            dir = leftDir;
            x -= speed;
        }

        if (up && World.isFree(getX(), (int)(getY() - speed))) {
            moved = true;
            y -= speed;
        }
        else if (down && World.isFree(getX(), (int)(getY() + speed))) {
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

        checkCollisionPotion();

        Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH*16 - Game.WIDTH);
        Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT*16 - Game.HEIGHT);
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

    public void checkCollisionPotion() {
        for (int i = 0; i < Game.entities.size(); i ++) {
            Entity e = Game.entities.get(i);
            if (e instanceof Potion) {
                if (Entity.isColliding(this, e)) {
                    life += 10;
                    if (life > 100) life = 100;
                    Game.entities.remove(e);
                }
            }
        }
    }
}
