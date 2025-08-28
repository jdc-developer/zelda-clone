package jdc.zelda.graphics;

import jdc.zelda.entities.Player;

import java.awt.*;

public class UI {

    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(10, 10, 70, 10);
        g.setColor(Color.green);
        g.fillRect(10,10, (int)((Player.life / Player.MAX_LIFE) * 70), 10);
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 8));
        g.drawString((int)Player.life + "/" + (int)Player.MAX_LIFE,12, 18);
    }
}
