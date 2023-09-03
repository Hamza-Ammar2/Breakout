import java.awt.*;

public class Particles {
    int CENTER_X;
    int CENTER_Y;

    int a = 255;
    Color color;
    Color waitingcolor;
    int offset = 100;
    int ySpeed = 5;
    int size = 10;
    boolean spawned = false;

    Vector[] particles = new Vector[30];

    Particles(int CENTER_X, int CENTER_Y, Color color) {
        this.CENTER_X = CENTER_X;
        this.CENTER_Y = CENTER_Y;
        this.color = color;

        createParticles();
    }

    public void draw(Graphics g) {
        if (!spawned) return;
        if (a < 50){
            createParticles();
            spawned = false;
            color = waitingcolor;
            a = 255;
            return;
        }
        a -= 10;

        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), a));
        for (Vector particle : particles) {
            particle.y += ySpeed;
            g.fillOval(particle.x, particle.y, size, size);
        }
    }

    private void createParticles() {
        for (int i = 0; i < particles.length; i++) {
            int x = (int) (CENTER_X + (offset/2 - (Math.random()*offset)));
            int y = (int) (CENTER_Y + (offset/2 - (Math.random()*offset)));
            particles[i] = new Vector(x, y);
        }
    }
} 