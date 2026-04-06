import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public final class RPGHelperTheme {

    public static final Color BG = new Color(28, 37, 54);
    public static final Color PANEL = new Color(35, 48, 72);
    public static final Color PANEL_DARK = new Color(24, 33, 51);
    public static final Color ACCENT = new Color(78, 150, 196);
    public static final Color TEXT = new Color(230, 235, 245);
    public static final Color MUTED = new Color(180, 190, 205);
    public static final Color SUCCESS = new Color(90, 200, 120);

    public static final Font TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font LABEL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font VALUE = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font BIG = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    private RPGHelperTheme() {
    }

    public static JPanel panel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(PANEL_DARK);
        return panel;
    }

    public static JPanel titledPanel(String title) {
        JPanel outer = panel(new BorderLayout());
        outer.setBorder(new CompoundBorder(
                new LineBorder(new Color(72, 88, 118), 1, true),
                new EmptyBorder(14, 14, 14, 14)
        ));

        JLabel label = new JLabel(title);
        label.setForeground(TEXT);
        label.setFont(TITLE);
        label.setBorder(new EmptyBorder(0, 0, 14, 0));
        outer.add(label, BorderLayout.NORTH);

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        outer.add(inner, BorderLayout.CENTER);
        return outer;
    }

    public static JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    public static JCheckBox check(String text) {
        JCheckBox checkbox = new JCheckBox(text);
        checkbox.setOpaque(false);
        checkbox.setForeground(TEXT);
        checkbox.setFont(LABEL);
        checkbox.setFocusPainted(false);
        checkbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkbox.setBorder(new EmptyBorder(4, 0, 4, 0));
        return checkbox;
    }

    public static JPanel transparentFlow() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    public static JComboBox<String> styleCombo(JComboBox<String> combo) {
        combo.setBackground(PANEL);
        combo.setForeground(TEXT);
        combo.setFont(LABEL);
        combo.setFocusable(false);
        return combo;
    }

    public static JComponent divider() {
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(65, 81, 110));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return separator;
    }

    public static JPanel statLine(String key, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(8, 0, 8, 0));

        JLabel keyLabel = new JLabel(key);
        keyLabel.setForeground(MUTED);
        keyLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(TEXT);
        valueLabel.setFont(BIG);

        row.add(keyLabel, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);
        return row;
    }

    public static JPanel resourceLine(String key, String value, Icon icon) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(10, 0, 10, 0));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        JLabel keyLabel = new JLabel(key + ":");
        keyLabel.setForeground(TEXT);
        keyLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        left.add(iconLabel);
        left.add(keyLabel);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(TEXT);
        valueLabel.setFont(BIG);

        row.add(left, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);
        return row;
    }

    public static JPanel iconCheck(String text, String iconText, Color color, boolean selected) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        wrapper.setOpaque(false);

        JCheckBox checkbox = check(text);
        checkbox.setSelected(selected);
        checkbox.setBorder(new EmptyBorder(0, 0, 0, 0));

        wrapper.add(new JLabel(createBadgeIcon(iconText, color, 24, 24)));
        wrapper.add(checkbox);
        return wrapper;
    }

    public static JPanel itemCard(String title, String subtitle, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setOpaque(true);
        card.setBackground(PANEL);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(72, 88, 118), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel icon = new JLabel(createBadgeIcon(title.substring(0, Math.min(2, title.length())).toUpperCase(), color, 46, 46));
        icon.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel name = new JLabel(title, SwingConstants.CENTER);
        name.setForeground(TEXT);
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel description = new JLabel(subtitle, SwingConstants.CENTER);
        description.setForeground(MUTED);
        description.setFont(SMALL);

        card.add(icon, BorderLayout.NORTH);
        card.add(name, BorderLayout.CENTER);
        card.add(description, BorderLayout.SOUTH);
        return card;
    }

    public static Icon createResourceIcon(String text, Color color) {
        return createBadgeIcon(text, color, 24, 24);
    }

    public static Icon createBadgeIcon(String text, Color color, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.fillRoundRect(0, 0, width - 1, height - 1, 10, 10);
        g2.setColor(brighten(color, 0.2f));
        g2.drawRoundRect(0, 0, width - 1, height - 1, 10, 10);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, Math.max(10, height / 3)));

        FontMetrics fontMetrics = g2.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);
        int textHeight = fontMetrics.getAscent();
        g2.drawString(text, (width - textWidth) / 2, (height + textHeight) / 2 - 2);
        g2.dispose();
        return new ImageIcon(image);
    }

    public static JPanel footerResourceChip(String name, String value, Color color) {
        JPanel chip = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        chip.setOpaque(true);
        chip.setBackground(PANEL);
        chip.setBorder(new CompoundBorder(
                new LineBorder(new Color(72, 88, 118), 1, true),
                new EmptyBorder(4, 8, 4, 8)
        ));

        JLabel icon = new JLabel(createBadgeIcon(name.substring(0, Math.min(2, name.length())).toUpperCase(), color, 18, 18));
        JLabel text = new JLabel(name + " " + value);
        text.setForeground(TEXT);
        text.setFont(new Font("Segoe UI", Font.BOLD, 12));

        chip.add(icon);
        chip.add(text);
        return chip;
    }

    public static JButton footerButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(42, 34));
        button.setBackground(PANEL);
        button.setForeground(TEXT);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        button.setBorder(new LineBorder(new Color(72, 88, 118), 1, true));
        return button;
    }

    public static void styleTab(JButton button, boolean active) {
        button.setForeground(TEXT);
        button.setBackground(active ? ACCENT : PANEL);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBorder(new CompoundBorder(
                new LineBorder(new Color(72, 88, 118), 1, true),
                new EmptyBorder(10, 18, 10, 18)
        ));
    }

    public static JPanel createPlaceholderPanel(String title, String summary, String[] features) {
        JPanel wrapper = new JPanel(new BorderLayout(14, 14));
        wrapper.setOpaque(false);

        JPanel hero = titledPanel(title);
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));

        JLabel headline = new JLabel(summary);
        headline.setForeground(TEXT);
        headline.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headline.setAlignmentX(Component.LEFT_ALIGNMENT);
        hero.add(headline);
        hero.add(Box.createVerticalStrut(12));

        JTextArea description = new JTextArea(
                "This button now opens its own dedicated screen, so we can keep building each workflow independently."
        );
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setFocusable(false);
        description.setOpaque(false);
        description.setForeground(MUTED);
        description.setFont(LABEL);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);
        hero.add(description);
        hero.add(Box.createVerticalStrut(18));

        hero.add(sectionLabel("Planned Controls"));
        hero.add(Box.createVerticalStrut(10));

        for (String feature : features) {
            hero.add(check(feature));
        }

        hero.add(Box.createVerticalGlue());

        JPanel quickStats = titledPanel("Status");
        quickStats.setLayout(new GridLayout(1, 3, 12, 12));
        quickStats.add(itemCard("Ready", "Navigation linked", SUCCESS));
        quickStats.add(itemCard("Scope", "Separate file", ACCENT));
        quickStats.add(itemCard("Next", "Add logic", new Color(190, 145, 65)));

        wrapper.add(hero, BorderLayout.CENTER);
        wrapper.add(quickStats, BorderLayout.SOUTH);
        return wrapper;
    }

    public static Color brighten(Color color, float factor) {
        int red = Math.min(255, (int) (color.getRed() * (1 + factor)));
        int green = Math.min(255, (int) (color.getGreen() * (1 + factor)));
        int blue = Math.min(255, (int) (color.getBlue() * (1 + factor)));
        return new Color(red, green, blue);
    }
}
