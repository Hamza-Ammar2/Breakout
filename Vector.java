public class Vector {
    int x = 0;
    int y = 0;

    Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector vector) {
        x += vector.x;
        y += vector.y;
    }

    public void subtract(Vector vector) {
        x -= vector.x;
        y -= vector.y;
    }

    public void multiply(float num) {
        x *= num;
        y *= num;
    }

    public int getLength() {
        return (int) Math.hypot((double) x, (double) y);
    }

    public void reset() {
        x = 0;
        y = 0;
    }

    public void equals(Vector vector) {
        x = vector.x;
        y = vector.y;
    }


    public void normalize() {
        int length = getLength();
        x /= length;
        y /= length;
    }
}