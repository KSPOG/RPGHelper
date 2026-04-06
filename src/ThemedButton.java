import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ThemedButton extends JButton {

    private Color normalBackground = RPGHelperTheme.PANEL;
    private Color hoverBackground = RPGHelperTheme.brighten(RPGHelperTheme.PANEL, 0.12f);
    private Color pressedBackground = RPGHelperTheme.brighten(RPGHelperTheme.PANEL, 0.22f);
    private Color borderColor = new Color(72, 88, 118);
    private int arc = 12;

    public ThemedButton(String text) {
        super(text);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(RPGHelperTheme.TEXT);
        setFont(RPGHelperTheme.LABEL);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(10, 18, 10, 18));
    }

    public void applyTheme(Color background, Color foreground, Color borderColor, int arc) {
        this.normalBackground = background;
        this.hoverBackground = RPGHelperTheme.brighten(background, 0.12f);
        this.pressedBackground = RPGHelperTheme.brighten(background, 0.22f);
        this.borderColor = borderColor;
        this.arc = arc;
        setForeground(foreground);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ButtonModel model = getModel();
        if (model.isPressed()) {
            g2.setColor(pressedBackground);
        } else if (model.isRollover()) {
            g2.setColor(hoverBackground);
        } else {
            g2.setColor(normalBackground);
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
        g2.dispose();

        super.paintComponent(graphics);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
    }
}
