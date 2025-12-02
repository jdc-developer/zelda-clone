package jdc.zelda.entities;

import jdc.zelda.Game;
import jdc.zelda.world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class NPC extends Entity {

    public static final float WIDTH = 24.6f;
    public static final float HEIGHT = 28f;

    private BufferedImage[] sprites;

    public String[] phrases = new String[2];

    private int frames = 0, maxFrames = 30, animationIndex = 0, maxAnimationIndex = 2;
    public static boolean showMessage = false;
    public boolean shown = false;

    public int curIndex = 0;
    public int phraseIndex = 0;
    public int time = 0;
    public int maxTime = 5;

    public NPC(double x, double y, float width, float height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        sprites = new BufferedImage[3];

        for (int i = 0; i < 3; i++) {
            sprites[i] = Game.npcSpritesheet.getSprite(214 + (i*WIDTH), 131f, WIDTH, HEIGHT);
        }

        phrases[0] = "Olá! Seja bem-vindo ao jogo.";
        phrases[1] = "Do que você precisa?";
    }

    @Override
    public void tick() {
        int xPlayer = Game.player.getX();
        int yPlayer = Game.player.getY();

        if (Math.abs(xPlayer - getX()) < 20 && Math.abs(yPlayer - getY()) < 20) {
            if (!shown) {
                shown = true;
                showMessage = true;
            }
        }

        if (showMessage) {

            time++;
            if (time >= maxTime) {
                time = 0;
                if (curIndex < phrases[phraseIndex].length()) curIndex++;
                else if (phraseIndex < phrases.length - 1) {
                    phraseIndex++;
                    curIndex = 0;
                }
            }
        }

        frames++;

        if (frames == maxFrames) {
            frames = 0;
            animationIndex++;

            if (animationIndex > maxAnimationIndex) animationIndex = 0;
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(sprites[animationIndex], getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight(), null);

        if (showMessage) {
            g.setColor(Color.white);
            g.fillRect(9, 9, Game.WIDTH - 18, Game.HEIGHT - 18);
            g.setColor(Color.blue);
            g.fillRect(10, 10, Game.WIDTH - 20, Game.HEIGHT - 20);
            g.setFont(new Font("Arial", Font.BOLD, 9));
            g.setColor(Color.white);
            g.drawString(phrases[phraseIndex].substring(0, curIndex), 50, 40);

            g.drawString("Pressione enter para fechar", 50, 142);
        }
    }
}
