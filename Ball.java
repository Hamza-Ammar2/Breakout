import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Ball {
    int SCREEN_WIDTH;
    int SCREEN_HEIGHT;
    int width = 15;
    int height = 15;
    float resistance = 0.9999f;
    int minSpeed = 10;
    Vector velocity = new Vector(0, 0);
    Vector position = new Vector(108, 105);
    Vector position_old = new Vector(100, 100);

    BufferedImage images;
    BufferedImage image;
    boolean out = false;

    Ball(int SCREEN_WIDTH, int SCREEN_HEIGHT, BufferedImage image) {
        this.SCREEN_WIDTH = SCREEN_WIDTH;
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;
        this.images = image.getSubimage(192*3/6, 192*3/12, 192*2/12, 192/12);
        chooseImage();
    }   
    
    private void chooseImage() {
        image = images.getSubimage(0, 0, images.getHeight()/2, images.getHeight()/2);
    }

    public void draw(Graphics g) {
        //g.setColor(Color.BLACK);

        //g.fillOval(position.x, position.y, width, height);
        g.drawImage(image, position.x, position.y, width, height, null);
    }

    public void update(float dt) {
        velocity.reset();
        velocity.add(position);
        velocity.subtract(position_old);

        position_old.equals(position);

        position.add(velocity);

        
        if (position.x < 0 | position.x + width > SCREEN_WIDTH) {
            position_old.x = position.x < 0 ? 0 : SCREEN_WIDTH - width;
            position.x = position.x < 0 ? -velocity.x : SCREEN_WIDTH - width + -velocity.x;
        }
        if (position.y < 0 | position.y + height > SCREEN_HEIGHT){
            if (position.y > 0)
                out = true;
            position_old.y = position.y < 0 ? 0 : SCREEN_HEIGHT - height;
            position.y = position.y < 0 ? -velocity.y : SCREEN_HEIGHT - height + -velocity.y;
        }
    }
}
