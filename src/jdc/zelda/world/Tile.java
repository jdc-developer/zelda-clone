package jdc.zelda.world;

import jdc.zelda.ZeldaGame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {

    public static BufferedImage TILE_FLOOR = ZeldaGame.tileset.getSprite(1, 1, 16, 16);
    public static BufferedImage TILE_WALL = ZeldaGame.tileset.getSprite(281, 342, 16, 16);

    private BufferedImage sprite;
    private int x, y;

    public Tile(int x, int y, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public void render(Graphics g) {
        g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
    }
}
