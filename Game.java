import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Game {
    int SCREEN_WIDTH;
    int SCREEN_HEIGHT;
    Player player;
    Ball ball;
    Blocks blocks;
    BufferedImage image;
    BufferedImage heart;
    BufferedImage deadheart;

    int score = 0;
    int lives = 3;

    Game(int SCREEN_WIDTH, int SCREEN_HEIGHT) {
        try {
            image = ImageIO.read(new File("graphics/blocks.png"));
            BufferedImage heartStates = ImageIO.read(new File("graphics/hearts.png"));
            heart = heartStates.getSubimage(0, 0, heartStates.getWidth()/2, heartStates.getHeight());
            deadheart = heartStates.getSubimage(heartStates.getHeight(), 0, heartStates.getWidth()/2, heartStates.getHeight());
        } catch (IOException e) {
        }
        this.SCREEN_WIDTH = SCREEN_WIDTH;
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;

        player = new Player(SCREEN_WIDTH, SCREEN_HEIGHT, image);
        ball = new Ball(SCREEN_WIDTH, SCREEN_HEIGHT, image);
        blocks = new Blocks(SCREEN_WIDTH, SCREEN_HEIGHT, image);
    }

    public void update(float dt) {
        player.collide(ball.position_old, ball.position, ball.width, ball.height);
        blocks.collide(ball.position_old, ball.position, ball.width, ball.height);
        player.update();
        if (ball.out) {
            ball.out = false;
            lives--;
            player.shrink();
        }
        ball.update(dt);
        score = blocks.score;
        player.grow(score);
    }

    public void draw(Graphics g) {
        drawScore(g);
        drawHearts(g);
        player.draw(g);
        blocks.draw(g);
        ball.draw(g);
    }

    private void drawScore(Graphics g) {
        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        g.setColor(Color.WHITE);
        String string = "Score: " + String.valueOf(score);

        g.drawString(string, SCREEN_WIDTH - string.length()*10 - 20, 25);
    }

    private void drawHearts(Graphics g) {
        int width = 20;
        int gap = 10;
        for (int i = 0; i < 3; i++) {
            BufferedImage heart = i + 1 > lives ? deadheart : this.heart;
            g.drawImage(heart, SCREEN_WIDTH - 250 + i*(width + gap), 10, width, width, null);
        }
    } 
}
