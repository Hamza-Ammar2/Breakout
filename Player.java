import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.*;

public class Player {
    int SCREEN_HEIGHT;
    int SCREEN_WIDTH;
    int x = 0;
    int y = 0;
    int width = 100;
    int height = 25;
    int xSpeed = 0;
    int speed = 10;
    BufferedImage images;
    BufferedImage image;
    boolean canBigger = true;
    boolean canBiggest = true;

    Player(int SCREEN_WIDTH, int SCREEN_HEIGHT, BufferedImage image) {
        this.images = image.getSubimage(0, 4*(image.getHeight()/12), image.getWidth(), 192*2/12);
        chooseImage(0, 1, 2, 1);
        this.SCREEN_WIDTH = SCREEN_WIDTH;
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;

        y = SCREEN_HEIGHT - 2*height;
        x = SCREEN_WIDTH/2 - width/2;
    }

    private void chooseImage(int i, int j, int width, int height) {
        int Height = 192/12;
        int Width = 192/6;

        image = images.getSubimage(Width*j, i*Height, Width*width, Height*height);
    }


    public void draw(Graphics g) {
        g.setColor(Color.BLUE);

        //g.fillRect(x, y, width, height);
        g.drawImage(image, x, y, width, height, null);
    }

    public void update() {
        x += xSpeed;
    }

    private Vector getUnity(Vector vector) {
        int x = 1;
        int y = 1;
        if (vector.y < 0)
            y = -1;
        if (vector.x < 0)
            x = -1;
        return new Vector(x, y);
    }

    public void changeSpeed(boolean isRight, boolean isPressed) {
        if (!isPressed) {
            if ((xSpeed > 0) != isRight)
                return;
            xSpeed = 0;
            return;
        }

        if (isRight)
            xSpeed = speed;
        else
            xSpeed = -speed;
    }

    private double getAngle(int y, int x) {
        int X = x < 0 ? -x : x;
        int Y = y < 0 ? -y : y;

        double angle = Math.atan2(Y, X);
        if (x < 0)
            angle = Math.PI - angle;
        if (y < 0)
            angle *= -1;
        return angle;
    }
    
    public void collide(Vector position_old, Vector position, int width, int height) {
        if (position.x + width > x && position.x < x + this.width && 
            position.y + height > y && position.y < y + this.height) {
            int diffX = (position.x + width/2) - (x + this.width/2);
            int diffY = (position.y + height/2) - (y + this.height/2);
            Vector velocity = new Vector(position.x - position_old.x, position.y - position_old.y);

            double angle = getAngle(-diffY, diffX);
            double thetaRight = Math.atan2(this.height/2, this.width/2);
            double thetaUp = Math.PI - thetaRight;

            if ((angle >= 0 && angle < thetaRight) | (angle < 0 && angle > -thetaRight)) {
                if (velocity.x > 0) {
                    Vector unit = getUnity(velocity);
                    position.subtract(unit);
                    position_old.subtract(unit);
                    collide(position_old, position, width, height);
                    return;
                }

                position_old.x = x + this.width + xSpeed;
                position.x = x + this.width + -velocity.x + xSpeed;
                if (velocity.y > 0)
                    position.y = position_old.y - velocity.y;
            } else if (angle >= thetaRight && angle <= thetaUp) {
                if (velocity.y < 0) {
                    Vector unit = getUnity(velocity);
                    position.subtract(unit);
                    position_old.subtract(unit);
                    collide(position_old, position, width, height);
                    return;
                }

                position_old.y = y - height;
                position.y = y - height + -velocity.y;
            } else if ((angle >= 0 && angle > thetaUp) | (angle < 0 && angle < -thetaUp)) {
                if (velocity.x < 0) {
                    Vector unit = getUnity(velocity);
                    position.subtract(unit);
                    position_old.subtract(unit);
                    collide(position_old, position, width, height);
                    return;
                }

                position_old.x = x - width + xSpeed;
                position.x = x - width + -velocity.x + xSpeed;
                if (velocity.y > 0)
                    position.y = position_old.y - velocity.y;
            } else if (angle < -thetaRight && angle > -thetaUp) {
                if (velocity.y > 0) {
                    Vector unit = getUnity(velocity);
                    position.subtract(unit);
                    position_old.subtract(unit);
                    collide(position_old, position, width, height);
                    return;
                }

                position_old.y = y + this.height;
                position.y = y + this.height + -velocity.y;
            }
        }
    }

    public void grow(int score) {
        if (score > 100 && width < 150 && canBigger) {
            width = 150;
            chooseImage(0, 3, width/(height*2), 1);
            canBigger = false;
        }

        if (score > 300 && width < 200 && canBiggest) {
            width = 200;
            chooseImage(1, 0, width/(height*2), 1);
            canBiggest = false;
        }
    }

    public void shrink() {
        if (this.width <= this.height*2) return;
        this.width -= 50;
        if (this.width <= 50) 
            chooseImage(0, 0, 1, 1);
        else if (this.width > 50 && this.width < 150) 
            chooseImage(0, 1, 2, 1);
        else if (this.width >= 150)
            chooseImage(0, 3, 3, 1);
    }
}