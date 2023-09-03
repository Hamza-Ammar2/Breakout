import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Panel extends JPanel implements Runnable {
    Thread gameThread;
    final static int ratio = 302/129;
    final static int SCREEN_HEIGHT = 500;
    final static int SCREEN_WIDTH = SCREEN_HEIGHT*ratio;

    Image image;
    Graphics graphics;
    Dimension dimensions = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT); 
    Game game = new Game(SCREEN_WIDTH, SCREEN_HEIGHT);
    Image background;

    Panel() {
        this.setFocusable(true);
        this.setPreferredSize(dimensions);
        this.addKeyListener(new KeyControl());

        background = new ImageIcon("graphics/background.png").getImage();

        gameThread = new Thread(this);
        gameThread.start();
    }

    private void draw(Graphics g) {
        g.drawImage(background, 0, 0, SCREEN_WIDTH + 5, SCREEN_HEIGHT + 5, null);
        game.draw(g);
    }


    public void paint(Graphics g) {
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }


    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta < 1) continue;
            delta--;

            game.update((float) (1/amountOfTicks));
            repaint();
        }
    }


    private class KeyControl extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == e.VK_RIGHT) 
                game.player.changeSpeed(true, true);
            if (e.getKeyCode() == e.VK_LEFT)
                game.player.changeSpeed(false, true);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == e.VK_RIGHT) 
                game.player.changeSpeed(true, false);
            if (e.getKeyCode() == e.VK_LEFT)
                game.player.changeSpeed(false, false);
        }
    }
}