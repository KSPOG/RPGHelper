import java.awt.*;

public class ScreenRegion {

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public ScreenRegion(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle toRectangle() {
        return new Rectangle(x, y, width, height);
    }

    public String serialize() {
        return x + "," + y + "," + width + "," + height;
    }

    public static ScreenRegion parse(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String[] parts = value.split(",");
        if (parts.length != 4) {
            return null;
        }

        try {
            return new ScreenRegion(
                    Integer.parseInt(parts[0].trim()),
                    Integer.parseInt(parts[1].trim()),
                    Integer.parseInt(parts[2].trim()),
                    Integer.parseInt(parts[3].trim())
            );
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    @Override
    public String toString() {
        return x + "," + y + " " + width + "x" + height;
    }
}
