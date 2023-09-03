import java.awt.*;
import java.awt.image.BufferedImage;

public class Blocks {
    int SCREEN_WIDTH;
    int SCREEN_HEIGHT;
    int marginX = 40;
    int marginY = 40;
    int gap = 40;

    int width = 50;
    int height = 25;
    BufferedImage Image;
    int score = 0;

    Block[] blocks = new Block[15];

    Blocks(int SCREEN_WIDTH, int SCREEN_HEIGHT, BufferedImage image) {
        this.Image = image;
        this.SCREEN_WIDTH = SCREEN_WIDTH;
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;

        int num = (SCREEN_WIDTH - 2*marginX)/(gap + width);
        marginX = (SCREEN_WIDTH + marginX - num*(width + gap))/2;
        for (int i = 0; i < blocks.length; i++) {
            int j = i/num;
            int k = i - j*num;
            int x = marginX + k*(width + gap);
            int y = marginY + j*(height + gap);
            if (blocks.length - j*num < num)
                x += (SCREEN_WIDTH + gap - 2*marginX - (blocks.length - j*num)*(gap + width))/2;

            blocks[i] = new Block(x, y, width, height);
        }
    }

    public void collide(Vector position_old, Vector position, int width, int height) {
        for (Block block :  blocks) 
            block.collide(position_old, position, width, height);
    }

    public void draw(Graphics g) {
        for (Block block : blocks)
            block.draw(g);
    }

    
    public class Block {
        int x = 0;
        int y = 0;
        int width = 80;
        int height = 40;
        BufferedImage image;

        Particles particles;
        boolean dead = false;
        char type;

        Block(int x, int y, int width, int height) {
            chooseImage();
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;

            particles = new Particles(x + this.width/2, y + this.height/2, getColor(type));
        }

        private void chooseImage() {
            int Height = 192/12;
            int Width = 192/6;
            int type = (int) (3*Math.random());
            int i = 0;
            int j = 0;
            switch(type) {
                case 0:
                    this.type = 'b';
                    break;
                case 1:
                    this.type = 'g';
                    j = 4;
                    break;
                case 2:
                    this.type = 'r';
                    i = 2;
            }

            image = Image.getSubimage(j*Width, i*Height, Width, Height);
        }

        private Color getColor(char type) {
            switch(type) {
                case 'r':
                    return Color.RED;
                case 'g':
                    return Color.GREEN;
                case 'b':
                    return Color.BLUE;
            }
            return Color.BLACK;
        }

        private int[] getPicCoord(char type) {
            int i = 0;
            int j = 0;
            switch(type) {
                case 'b':
                    break;
                case 'g':
                    j = 4;
                    break;
                case 'r':
                    i = 2;
            }
            return new int[]{i, j};
        }


        private void updateState() {
            int Height = 192/12;
            int Width = 192/6;
            int i;
            int j;

            switch(type) {
                case 'b':
                    dead = true;
                    break;
                case 'r':
                    type = 'g';
                    i = getPicCoord(type)[0];
                    j = getPicCoord(type)[1];
                    image = Image.getSubimage(j*Width, i*Height, Width, Height);
                    particles.waitingcolor = getColor(type);
                    break;
                case 'g':
                    type = 'b';
                    i = getPicCoord(type)[0];
                    j = getPicCoord(type)[1];
                    image = Image.getSubimage(j*Width, i*Height, Width, Height);
                    particles.waitingcolor = getColor(type);
                    break;
            }
        }



        public void draw(Graphics g) {
            if (!dead) {
                g.drawImage(image, x, y, width, height, null);
            }
            particles.draw(g);
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

        private Vector getUnity(Vector vector) {
            int x = 1;
            int y = 1;
            if (vector.y < 0)
                y = -1;
            if (vector.x < 0)
                x = -1;
            return new Vector(x, y);
        }

        public void collide(Vector position_old, Vector position, int width, int height) {
            if (dead) return;
            if (position.x + width > x && position.x < x + this.width && 
                position.y + height > y && position.y < y + this.height) {
                int diffX = (position.x + width/2) - (x + this.width/2);
                int diffY = (position.y + height/2) - (y + this.height/2);
                Vector velocity = new Vector(position.x - position_old.x, position.y - position_old.y);

                double angle = getAngle(-diffY, diffX);
                double thetaRight = Math.atan2(this.height/2, this.width/2);
                double thetaUp = Math.PI - thetaRight;

                if (((angle > 0 && angle < thetaRight) | (angle < 0 && angle > -thetaRight))) {
                    if (velocity.x > 0) {
                        Vector unit = getUnity(velocity);
                        position.subtract(unit);
                        position_old.subtract(unit);
                        collide(position_old, position, width, height);
                        return;
                    }

                    position_old.x = x + this.width;
                    position.x = x + this.width + -velocity.x;
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
                } else if (((angle > 0 && angle > thetaUp) | (angle <= 0 && angle < -thetaUp))) {
                    if (velocity.x < 0) {
                        Vector unit = getUnity(velocity);
                        position.subtract(unit);
                        position_old.subtract(unit);
                        collide(position_old, position, width, height);
                        return;
                    }

                    position_old.x = x - width;
                    position.x = x - width + -velocity.x;
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

                particles.spawned = true;
                updateState();
                score += 20;
            }
        }
    }
}