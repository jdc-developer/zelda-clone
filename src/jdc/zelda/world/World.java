package jdc.zelda.world;

import jdc.zelda.Game;
import jdc.zelda.entities.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class World {

    private static  Tile[] tiles;
    public static int WIDTH, HEIGHT;
    public static final int TILE_SIZE = 16;

    public World(String path) {
        try {
            BufferedImage map = ImageIO.read(getClass().getResourceAsStream(path));
            int[] pixels = new int[map.getWidth() * map.getHeight()];
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();
            tiles = new Tile[map.getWidth() * map.getHeight()];
            map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
            for (int xx = 0; xx < map.getWidth(); xx++) {
                for (int yy = 0; yy < map.getHeight(); yy++) {
                    int pixel = pixels[xx + (yy * map.getWidth())];
                    tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);

                    switch (pixel) {
                        case 0xFF000000:
                            //Chão
                            tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                            break;
                        case 0xFFFFFFFF:
                            //Parede
                            tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
                            break;
                        case 0xFF1922d8:
                            //Player
                            Game.player.setX(xx*16);
                            Game.player.setY(yy*16);
                            //tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                            break;
                        case 0XFFFF0000:
                            //Enemy
                            Enemy en = new Enemy(xx*16, yy*16, 40, 48, Game.enemySpritesheet.getSprite(0, 72, Enemy.WIDTH, Enemy.HEIGHT));
                            Game.entities.add(en);
                            Game.enemies.add(en);
                            break;
                        case 0XFFFFA200:
                            //Weapon
                            Game.entities.add(new Weapon(xx*16, yy*16, 16, 16, Entity.WEAPON_EN));
                            break;
                        case 0XFFEB76C3:
                            //Potion
                            Potion potion = new Potion(xx*16, yy*16, 30, 30, Entity.LIFEPOTION_EN);
                            potion.setMask(10, 10, 10f, 15f);
                            Game.entities.add(potion);
                            break;
                        case 0XFFF8FB05:
                            //Ammo
                            Game.entities.add(new Ammo(xx*16, yy*16, 16, 16, Entity.AMMO_EN));
                            break;
                        default:
                            tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void renderMiniMap() {
        for (int i = 0; i < Game.minimapPixels.length; i++) {
            Game.minimapPixels[i] = 0;
        }

        for (int xx = 0; xx < WIDTH; xx++) {
            for(int yy = 0; yy < HEIGHT; yy++) {
                if (tiles[xx + (yy*WIDTH)] instanceof WallTile) {
                    Game.minimapPixels[xx + (yy*WIDTH)] = 0XFF0000;
                }
            }
        }

        int xPlayer = Game.player.getX() / 16;
        int yPlayer = Game.player.getY() / 16;

        Game.minimapPixels[xPlayer + (yPlayer * WIDTH)] = 0X0000FF;
    }

    public void render(Graphics g) {
        int xstart = Camera.x >> 4;
        int ystart = Camera.y >> 4;

        int xfinal = xstart + Game.WIDTH >> 3;
        int yfinal = ystart + Game.HEIGHT >> 3;

        for (int xx = xstart; xx <= WIDTH; xx++) {
            for (int yy = ystart; yy <= HEIGHT; yy++) {
                if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
                    continue;
                Tile tile = tiles[xx + (yy * WIDTH)];
                tile.render(g);
            }
        }
    }

    public static boolean isFree(int xnext, int ynext) {
        int x1 = xnext / TILE_SIZE;
        int y1 = ynext / TILE_SIZE;

        int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
        int y2 = ynext / TILE_SIZE;

        int x3 = xnext / TILE_SIZE;
        int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

        int x4 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
        int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

        if (!((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile) ||
                (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile) ||
                (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile) ||
                        (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile))) {
            return true;
        }

        if (Game.player.z > 0) return true;
        return false;
    }

    public static Tile[] getTiles() {
        return tiles;
    }
}
