package jdc.zelda.world;

import jdc.zelda.ZeldaGame;
import jdc.zelda.entities.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class World {

    private Tile[] tiles;
    public static int WIDTH, HEIGHT;

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
                            tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_WALL);
                            break;
                        case 0xFF1922d8:
                            //Player
                            ZeldaGame.player.setX(xx*16);
                            ZeldaGame.player.setY(yy*16);
                            tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                            break;
                        case 0XFFFF0000:
                            //Enemy
                            ZeldaGame.entities.add(new Enemy(xx*16, yy*16, 16, 16, Entity.ENEMY_EN));
                            break;
                        case 0XFFFFA200:
                            //Weapon
                            ZeldaGame.entities.add(new Weapon(xx*16, yy*16, 16, 16, Entity.WEAPON_EN));
                            break;
                        case 0XFFEB76C3:
                            //Potion
                            ZeldaGame.entities.add(new Potion(xx*16, yy*16, 16, 16, Entity.LIFEPOTION_EN));
                            break;
                        case 0XFFF8FB05:
                            //Ammo
                            ZeldaGame.entities.add(new Ammo(xx*16, yy*16, 16, 16, Entity.AMMO_EN));
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

    public void render(Graphics g) {
        int xstart = Camera.x >> 4;
        int ystart = Camera.y >> 4;

        int xfinal = xstart + ZeldaGame.WIDTH >> 4;
        int yfinal = ystart + ZeldaGame.HEIGHT >> 3;

        for (int xx = xstart; xx <= xfinal; xx++) {
            for (int yy = ystart; yy <= yfinal; yy++) {
                if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
                    continue;
                Tile tile = tiles[xx + (yy * WIDTH)];
                tile.render(g);
            }
        }
    }
}
