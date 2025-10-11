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

    private static final int DAMAGE_TIME = 1500;
    private static final int DAMAGE_ANIMATION_INTERCURRENCY = 100;
    private Long damageStartTime = 0L;
    private Long lastDamageAnimationTime = 0L;

    public boolean right, up, left, down;
    public int rightDir = 0, leftDir = 1;
    public int dir = rightDir;
    public float speed = 1.5f;
    public float life = MAX_LIFE;

    private int frames = 0, maxFrames = 5, animationIndex = 0, maxAnimationIndex = 9;
    private boolean moved = false;
    public static boolean isTakingDamage = false;
    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;

    public boolean hasGun;
    public boolean isShooting;
    public boolean isMouseShooting;

    public int mx, my;

    public int ammo;

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
        checkCollisionAmmo();
        checkCollisionGun();

        Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH*16 - Game.WIDTH);
        Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT*16 - Game.HEIGHT);

        if (hasGun && isShooting && ammo > 0) {
            ammo--;
            isShooting = false;
            int dx;
            int px;
            int py = 4;

            if (dir == rightDir) {
                px = 24;
                dx = 1;
            }
            else {
                px = -14;
                dx = -1;
            }

            Bullet bullet = new Bullet(getX() + px, getY() + py,3,3, dx, 0);
            Game.bullets.add(bullet);
        }

        if (hasGun && isMouseShooting && ammo > 0) {
            ammo--;
            isMouseShooting = false;

            int px = 0, py = 4;
            double angle = 0;
            if (dir == rightDir) {
                px = 24;
                angle = Math.atan2(my - (this.getY()+py - Camera.y), mx - (getX()+px - Camera.x));
            }
            else {
                px = -14;
                angle = Math.atan2(my - (this.getY()+py - Camera.y), mx - (getX()+px - Camera.x));
            }

            double dx = Math.cos(angle);
            double dy = Math.sin(angle);

            Bullet bullet = new Bullet(getX() + px, getY() + py,3,3, dx, dy);
            Game.bullets.add(bullet);
        }

        if (life <= 0) {
            life = 0;
            Game.gameState = "GAME_OVER";
        }
    }

    public void render(Graphics g) {
        if (isTakingDamage) {
            if (damageStartTime == 0) damageStartTime = System.currentTimeMillis();
            if (damageStartTime + DAMAGE_TIME > System.currentTimeMillis()) {
                if (lastDamageAnimationTime == 0) lastDamageAnimationTime = System.currentTimeMillis();
                if (lastDamageAnimationTime + DAMAGE_ANIMATION_INTERCURRENCY > System.currentTimeMillis()) return;
                else if (lastDamageAnimationTime + (DAMAGE_ANIMATION_INTERCURRENCY * 2) < System.currentTimeMillis()) lastDamageAnimationTime = System.currentTimeMillis();
            } else {
                lastDamageAnimationTime = 0L;
                damageStartTime = 0L;
                isTakingDamage = false;
            }
        }

        if (dir == rightDir) {
            g.drawImage(rightPlayer[animationIndex], getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight(), null);
            if (hasGun) {
                g.drawImage(Entity.WEAPON_EN, this.getX() - Camera.x + 15, this.getY() - Camera.y + 2, 16, 16, null);
            }
        } else if (dir == leftDir) {
            g.drawImage(leftPlayer[animationIndex], getX() - Camera.x, getY() -  Camera.y, getWidth(), getHeight(),null);
            if (hasGun) {
                g.drawImage(Entity.WEAPON_LEFT, this.getX() - Camera.x - 13, this.getY() - Camera.y + 2, 16, 16, null);
            }
        }

    }

    public void checkDamageAnimation() {

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

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
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

    public void checkCollisionAmmo() {
        for (int i = 0; i < Game.entities.size(); i ++) {
            Entity e = Game.entities.get(i);
            if (e instanceof Ammo) {
                if (Entity.isColliding(this, e)) {
                    ammo += 10;
                    Game.entities.remove(e);
                }
            }
        }
    }

    public void checkCollisionGun() {
        for (int i = 0; i < Game.entities.size(); i ++) {
            Entity e = Game.entities.get(i);
            if (e instanceof Weapon) {
                if (Entity.isColliding(this, e)) {
                    hasGun = true;
                    Game.entities.remove(e);
                }
            }
        }
    }
}
