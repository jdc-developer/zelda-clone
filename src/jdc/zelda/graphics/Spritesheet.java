package jdc.zelda.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Spritesheet {

    private BufferedImage spritesheet;
    public static final int SPRITE_WIDTH = 274;
    public static final int SPRITE_HEIGHT = 219;

    public Spritesheet(String path) {
        try {
            URL url = getClass().getResource(path);
            spritesheet = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedImage getSprite(int x, int y, float w, float h) {
        return spritesheet.getSubimage(x, y, (int)w, (int)h);
    }
}
