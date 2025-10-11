package jdc.zelda.ui;

import jdc.zelda.Game;

import java.awt.*;

public class GameMenu {

    private String[] options = {"Novo Jogo", "Carregar Jogo", "Sair"};

    public int currentOption = 0;
    public int maxOptions = options.length - 1;

    public boolean up, down;

    public void tick() {
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
    }

    public void render(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0,0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 38));
        g.drawString("Zelda Clone", (Game.WIDTH * Game.SCALE) / 2 - 100, (Game.HEIGHT * Game.SCALE) / 2 - 100);

        g.setFont(new Font("arial", Font.BOLD, 18));
        g.drawString("Novo Jogo", (Game.WIDTH * Game.SCALE) / 2 - 40, (Game.HEIGHT * Game.SCALE) / 2 - 60);
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
