package jdc.zelda.ui;

import jdc.zelda.Game;
import jdc.zelda.world.World;

import java.awt.*;
import java.io.*;

public class GameMenu {

    private static final String SAVE_GAME_FILE = "save.txt";

    private String[] options = {"Novo Jogo", "Carregar Jogo", "Sair"};

    public int currentOption = 0;
    public int maxOptions = options.length - 1;

    public boolean up, down, enter;

    public static boolean pause, saveExists, saveGame;

    public void tick() {
        File file = new File(SAVE_GAME_FILE);
        if (file.exists()) saveExists = true;
        else saveExists = false;

        if (up) {
            up = false;
            currentOption--;
            if (currentOption < 0) currentOption = maxOptions;
        }

        if (down) {
            down = false;
            currentOption++;
            if (currentOption > maxOptions) currentOption = 0;
        }

        if (enter) {
            enter = false;
            if (options[currentOption].equals("Novo Jogo") || options[currentOption].equals("Continuar")) {
                pause = false;
                Game.gameState = "NORMAL";
                file.delete();
            }
            else if (options[currentOption].equals("Sair")) System.exit(0);
            else if(options[currentOption].equals("Carregar Jogo")) {
                if (file.exists()) {
                    String saver = loadGame(10);
                    applySave(saver);
                }
            }
        }
    }

    public static void applySave(String str) {
        String[] spl = str.split("/");
        for (int i = 0; i < spl.length; i++) {
            String[] spl2 = spl[i].split(":");
            switch (spl2[0]) {
                case "level":
                    Game.restart("level-" + spl2[1] + ".png");
                    Game.gameState = "NORMAL";
                    pause = false;
                    break;
            }
        }
    }

    public static String loadGame(int encode) {
        String line = "";
        File file = new File(SAVE_GAME_FILE);

        if (file.exists()) {
            try {
                String singleLine = null;
                BufferedReader bufferedReader = new BufferedReader(new FileReader(SAVE_GAME_FILE));
                while ((singleLine = bufferedReader.readLine()) != null) {
                    String[] trans = singleLine.split(":");
                    char[] val = trans[1].toCharArray();
                    trans[1] = "";

                    for (int i = 0; i < val.length; i++) {
                        val[i] -= encode;
                        trans[1] += val[i];
                    }

                    line += trans[0];
                    line += ":";
                    line += trans[1];
                    line += "/";
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return line;
    }

    public static void saveGame(String[] val1, int[] val2, int encode) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(SAVE_GAME_FILE));
            for (int i = 0; i < val1.length; i++) {
                String current = val1[i];
                current += ":";
                char[] value = Integer.toString(val2[i]).toCharArray();

                for (int n = 0; n < value.length; n++) {
                    value[n] += encode;
                    current += value[n];
                }

                bufferedWriter.write(current);
                if (i < val1.length - 1)
                    bufferedWriter.newLine();

                bufferedWriter.flush();
                bufferedWriter.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void render(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0,0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 38));
        g.drawString("Zelda Clone", (Game.WIDTH * Game.SCALE) / 2 - 100, (Game.HEIGHT * Game.SCALE) / 2 - 100);

        g.setFont(new Font("arial", Font.BOLD, 18));

        String gameStr = "Novo Jogo";
        if (pause) gameStr = "Resumir";

        g.drawString(gameStr, (Game.WIDTH * Game.SCALE) / 2 - 40, (Game.HEIGHT * Game.SCALE) / 2 - 60);
        g.drawString("Carregar Jogo", (Game.WIDTH * Game.SCALE) / 2 - 40, (Game.HEIGHT * Game.SCALE) / 2 - 40);
        g.drawString("Sair", (Game.WIDTH * Game.SCALE) / 2 - 40, (Game.HEIGHT * Game.SCALE) / 2 - 20);

        if (options[currentOption].equals("Novo Jogo")) {
            g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 60, (Game.HEIGHT * Game.SCALE) / 2 - 60);
        } else if (options[currentOption].equals("Carregar Jogo")) {
            g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 60, (Game.HEIGHT * Game.SCALE) / 2 - 40);
        } else if (options[currentOption].equals("Sair")) {
            g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 60, (Game.HEIGHT * Game.SCALE) / 2 - 20);
        }
    }
}
