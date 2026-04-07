import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ScreenRegionSelectionOverlay extends JWindow {

    private final String resourceName;
    private final ScreenRegionSelectionListener listener;
    private Point startPoint;
    private Point currentPoint;

    public ScreenRegionSelectionOverlay(Window owner, String resourceName, ScreenRegionSelectionListener listener) {
        super(owner);
        this.resourceName = resourceName;
        this.listener = listener;

        setAlwaysOnTop(true);
        setBackground(new Color(0, 0, 0, 30));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);

        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                startPoint = event.getPoint();
                currentPoint = event.getPoint();
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                currentPoint = event.getPoint();
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                currentPoint = event.getPoint();
                ScreenRegion region = buildRegion();
                dispose();
                if (region != null) {
                    listener.onRegionSelected(region);
                }
            }
        };

        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setColor(new Color(0, 0, 0, 90));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2.drawString("Drag to select the " + resourceName + " number region. Press ESC to cancel.", 24, 36);

        ScreenRegion region = buildRegion();
        if (region != null) {
            Rectangle rectangle = region.toRectangle();
            g2.setColor(new Color(78, 150, 196, 60));
            g2.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            g2.setColor(RPGHelperTheme.ACCENT);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }

        g2.dispose();
    }

    private ScreenRegion buildRegion() {
        if (startPoint == null || currentPoint == null) {
            return null;
        }

        int x = Math.min(startPoint.x, currentPoint.x);
        int y = Math.min(startPoint.y, currentPoint.y);
        int width = Math.abs(currentPoint.x - startPoint.x);
        int height = Math.abs(currentPoint.y - startPoint.y);
        if (width < 5 || height < 5) {
            return null;
        }

        return new ScreenRegion(x, y, width, height);
    }
}
