package jdc.zelda.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Spritesheet {

    private BufferedImage spritesheet;
    public static final int SPRITE_WIDTH = 274;
    public static final int SPRITE_HEIGHT = 219;

    public Spritesheet(String path) {
        try {
            spritesheet = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedImage getSprite(float x, float y, float w, float h) {
        return spritesheet.getSubimage((int)x, (int)y, (int)w, (int)h);
    }
}
