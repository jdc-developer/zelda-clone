package jdc.zelda.entities;

import jdc.zelda.Game;
import jdc.zelda.world.AStar;
import jdc.zelda.world.Camera;
import jdc.zelda.world.Vector2i;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy extends Entity {

    public static final float WIDTH = 40.0f;
    public static final float HEIGHT = 48f;
    public static final int SUBSCALE = 5;

    private float speed = 1.5f;

    private int frames = 0, maxFrames = 10, animationIndex = 0, maxAnimationIndex = 9;
    private BufferedImage[] sprites;

    private int life = 10;

    public static boolean isTakingDamage = false;
    private static final int DAMAGE_TIME = 1500;
    private static final int DAMAGE_ANIMATION_INTERCURRENCY = 100;
    private Long damageStartTime = 0L;
    private Long lastDamageAnimationTime = 0L;

    public Enemy(double x, double y, float width, float height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        setMask(5, 5, 35, 43);
        sprites = new BufferedImage[11];

        for (int i = 0; i < 11; i++) {
            sprites[i] = Game.enemySpritesheet.getSprite(0 + (i*WIDTH), 72f, WIDTH, HEIGHT);
        }

        depth = 0;
    }

    public void tick() {

        /*
        if (this.calculateDistance(getX(), getY(), Game.player.getX(), Game.player.getY()) < 200) {
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
                    //Sound.hurtSound.play();
                    Game.player.life--;
                    Player.isTakingDamage = true;
                    System.out.println(Game.player.life);
                }

                //if (Game.player.life == 0) System.exit(1);
            }
        }*/

        if (!isCollidingWithPlayer()) {
            if (path == null || path.isEmpty()) {
                Vector2i start = new Vector2i(getX() / 16, getY() / 16);
                Vector2i end = new Vector2i(Game.player.getX() / 16, Game.player.getY() / 16);
                path = AStar.findPath(Game.world, start, end);
            }
        } else if (new Random().nextInt(100) < 5) {
            //Sound.hurtSound.play();
            Game.player.life--;
            Player.isTakingDamage = true;
            System.out.println(Game.player.life);
        }

        followPath(path);

        frames++;

        isCollidingWithBullet();
        
        if (life <= 0) {
            Game.enemies.remove(this);
            Game.entities.remove(this);
        }

        if (frames == maxFrames) {
            frames = 0;
            animationIndex++;

            if (animationIndex > maxAnimationIndex) animationIndex = 0;
        }

    }

    private void isCollidingWithBullet() {
        for (int i = 0; i < Game.bullets.size(); i++) {
            Entity e = Game.bullets.get(i);
            if (Entity.isColliding(this, e)) {
                life -= 5;
                isTakingDamage = true;
                Game.bullets.remove(e);
                return;
            }
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
        g.drawImage(sprites[animationIndex], getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight(), null);
        //g.setColor(Color.BLUE);
        //g.fillRect(getX() + getMaskX() - Camera.x, getY() + getMaskY() - Camera.y, getMaskW(), getMaskH());
    }

    public boolean isCollidingWithPlayer() {
        Rectangle currentEnemy = new Rectangle(getX() + getMaskX(), getY() + getMaskY(), getMaskW(), getMaskH());
        Rectangle playerRect = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);

        return currentEnemy.intersects(playerRect);
    }
}
