package jdc.zelda;

import jdc.zelda.commands.KeyboardCommands;
import jdc.zelda.entities.Entity;
import jdc.zelda.entities.Player;
import jdc.zelda.graphics.Spritesheet;
import jdc.zelda.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ZeldaGame extends Canvas implements Runnable {

    public boolean isRunning;
    public Thread thread;
    private static JFrame frame;
    public static final int WIDTH = 240;
    public static final int HEIGHT = 160;
    public static final int SCALE = 3;

    private BufferedImage image;

    public static List<Entity> entities;
    public static Spritesheet spritesheet;
    public static Spritesheet tileset;
    public static Player player;

    public static World world;

    public ZeldaGame() {
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        entities = new ArrayList<>();
        spritesheet = new Spritesheet("/spritesheet.png");
        tileset = new Spritesheet("/tileset.png");
        player = new Player(0, 0, spritesheet.getSprite(0, 0, Player.WIDTH, Player.HEIGHT));
        world = new World("/map.png");
        entities.add(player);
        addKeyListener(new KeyboardCommands(player));
    }

    public static BufferedImage scaleImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    public void initFrame() {
        frame = new JFrame("Zelda Clone");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        ZeldaGame game = new ZeldaGame();
        game.start();
    }

    public synchronized void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void tick() {
        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.tick();
        }
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();
        g.setColor(new Color(19, 19, 19));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //RENDER GAME
        Graphics2D g2 = (Graphics2D) g;
        world.render(g);
        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.render(g2);
        }

        /***/
        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        bs.show();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta =  0;

        int frames = 0;
        double timer = System.currentTimeMillis();

        while(isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if (delta >= 1) {
                tick();
                render();
                frames++;
                delta--;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                System.out.println("FPS: " + frames);
                frames = 0;
                timer += 1000;
            }
        }

        stop();
    }
}