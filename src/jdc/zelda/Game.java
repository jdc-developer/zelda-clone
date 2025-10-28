package jdc.zelda;

import jdc.zelda.commands.KeyboardCommands;
import jdc.zelda.commands.MouseCommands;
import jdc.zelda.entities.Bullet;
import jdc.zelda.entities.Enemy;
import jdc.zelda.entities.Entity;
import jdc.zelda.entities.Player;
import jdc.zelda.graphics.Spritesheet;
import jdc.zelda.graphics.UI;
import jdc.zelda.ui.GameMenu;
import jdc.zelda.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Canvas implements Runnable {

    public boolean isRunning;
    public Thread thread;
    private static JFrame frame;
    public static final int WIDTH = 240;
    public static final int HEIGHT = 160;
    public static final int SCALE = 3;

    private BufferedImage image;

    public static List<Entity> entities;
    public static List<Enemy> enemies;
    public static List<Bullet> bullets;
    public static Spritesheet spritesheet;
    public static Spritesheet enemySpritesheet;
    public static Spritesheet tileset;
    public static Player player;

    public static World world;

    public static Random rand;
    public UI ui;

    private int CUR_LEVEL = 1, MAX_LEVEL = 2;

    public static String gameState = "MENU";
    private boolean showMessageGameOver = true;
    private int framesGameOver = 0;

    public static boolean restartGame = false;
    public static boolean saveGame = false;

    public static int mx, my;

    private GameMenu menu;

    public int[] pixels;

    public int xx, yy;

    /*public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("angel-wish.ttf");
    public Font font;*/

    public Game() {
        //Sound.musicBackground.loop();
        rand = new Random();
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();
        ui = new UI();

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        entities = new ArrayList<>();
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        spritesheet = new Spritesheet("/spritesheet.png");
        enemySpritesheet = new Spritesheet("/enemies.png");
        tileset = new Spritesheet("/tileset.png");
        player = new Player(0, 0, spritesheet.getSprite(0, 0, Player.WIDTH, Player.HEIGHT));
        world = new World("/level-1.png");
        entities.add(player);
        menu = new GameMenu();

        /*try {
            font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(70f);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/

        MouseCommands mouseCommands = new MouseCommands(player);
        addKeyListener(new KeyboardCommands(player, menu));
        addMouseListener(mouseCommands);
        addMouseMotionListener(mouseCommands);
    }

    public static void restart(String level) {
        entities = new ArrayList<>();
        enemies = new ArrayList<>();
        player.setX(0);
        player.setY(0);
        player.life = Player.MAX_LIFE;
        entities.add(player);
        world = new World("/" + level);
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
        Game game = new Game();
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
        if (gameState.equals("NORMAL")) {

            if (saveGame) {
                saveGame = false;
                String [] opt1 = {"level"};
                int[] opt2 = {this.CUR_LEVEL};
                GameMenu.saveGame(opt1, opt2,10);
                System.out.println("Game saved");
            }

            Game.restartGame = false;
            for(int i = 0; i < bullets.size(); i++) {
                Bullet e = bullets.get(i);
                e.tick();
            }

            for(int i = 0; i < entities.size(); i++) {
                Entity e = entities.get(i);
                e.tick();
            }

            if (enemies.isEmpty()) {
                CUR_LEVEL++;
                if (CUR_LEVEL > MAX_LEVEL)  CUR_LEVEL = 1;
                String newWorld = "level-"+CUR_LEVEL+".png";
                Game.restart(newWorld);
            }
        } else if (gameState.equals("GAME_OVER")) {
            framesGameOver++;
            if(framesGameOver == 30) {
                framesGameOver = 0;
                if (showMessageGameOver)
                    showMessageGameOver = false;
                else
                    showMessageGameOver = true;
            }

            if (restartGame) {
                restartGame = false;
                gameState = "NORMAL";
                CUR_LEVEL = 1;
                String newWorld = "level-"+CUR_LEVEL+".png";
                Game.restart(newWorld);
            }
        } else if (gameState.equals("MENU")) {
            menu.tick();
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

        for(int i = 0; i < bullets.size(); i++) {
            Bullet e = bullets.get(i);
            e.render(g2);
        }
        ui.render(g2);

        /***/
        g2.dispose();
        g = bs.getDrawGraphics();

        //drawRectExmaple(xx, yy);

        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        g.setFont(new Font("arial", Font.BOLD, 17));
        g.setColor(Color.white);
        g.drawString("Munição: " + player.getAmmo(), 30, 20);
        /*g.setFont(font);
        g.drawString("Teste new font", 20, 70);*/

        Graphics2D g22 = (Graphics2D) g;
        if (gameState.equals("GAME_OVER")) {
            g22.setColor(new Color(0, 0, 0, 100));
            g22.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
            g22.setFont(new Font("arial", Font.BOLD, 38));
            g22.setColor(Color.white);
            g22.drawString("Game Over", (WIDTH * SCALE) / 2 - 100, (HEIGHT * SCALE) / 2 - 20);

            if (showMessageGameOver) {
                g22.setFont(new Font("arial", Font.BOLD, 18));
                g22.drawString("Pressione enter para reiniciar", (WIDTH * SCALE) / 2 - 120, (HEIGHT * SCALE) / 2 + 20);
            }
        } else if (gameState.equals("MENU")) {
            menu.render(g22);
        }

        /*double angleMouse = Math.atan2(200+25 - my, 200+25 - mx);
        g22.rotate(angleMouse, 200 + 25, 200 + 25);
        g22.setColor(Color.red);
        g22.fillRect(200, 200, 50, 50);*/

        bs.show();
    }

    /*private void drawRectExmaple(int xoff, int yoff) {
        for (int xx = 0; xx< 32; xx++) {
            for (int yy = 0; yy< 32; yy++) {
                int xOff = xx + xoff;
                int yOff = yy + yoff;

                if (xOff < 0 || yOff < 0 || xOff >= WIDTH || yOff >= HEIGHT)
                    continue;

                pixels[xOff + (yOff*WIDTH)] = 0xff0000;
            }
        }
    }*/

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta =  0;

        int frames = 0;
        double timer = System.currentTimeMillis();
        requestFocus();

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