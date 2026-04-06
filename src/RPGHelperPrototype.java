import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;

public class RPGHelperPrototype extends JFrame {

    private final Color BG = new Color(28, 37, 54);
    private final Color PANEL = new Color(35, 48, 72);
    private final Color PANEL_DARK = new Color(24, 33, 51);
    private final Color ACCENT = new Color(78, 150, 196);
    private final Color TEXT = new Color(230, 235, 245);
    private final Color MUTED = new Color(180, 190, 205);
    private final Color SUCCESS = new Color(90, 200, 120);
    private final Font TITLE = new Font("Segoe UI", Font.BOLD, 18);
    private final Font LABEL = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font VALUE = new Font("Segoe UI", Font.BOLD, 14);
    private final Font BIG = new Font("Segoe UI", Font.BOLD, 20);
    private final Font SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    public RPGHelperPrototype() {
        setTitle("RPG Helper");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1450, 900);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 760));

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(14, 14, 14, 14));
        setContentPane(root);

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createBody(), BorderLayout.CENTER);
        root.add(createFooter(), BorderLayout.SOUTH);
    }

    private JComponent createHeader() {
        JPanel wrapper = panel(new BorderLayout(12, 12));
        wrapper.setPreferredSize(new Dimension(0, 92));

        JPanel left = new JPanel(new BorderLayout(10, 0));
        left.setOpaque(false);

        JLabel appIcon = new JLabel("⚙");
        appIcon.setForeground(TEXT);
        appIcon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 28));

        JLabel title = new JLabel("RPG Helper");
        title.setForeground(TEXT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        left.add(appIcon, BorderLayout.WEST);
        left.add(title, BorderLayout.CENTER);

        JPanel tabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        tabs.setOpaque(false);
        String[] tabNames = {"Home", "Auto-Farm", "Champion Upgrades", "Forge", "Quests / Event", "Settings"};
        for (int i = 0; i < tabNames.length; i++) {
            JButton tab = new JButton(tabNames[i]);
            styleTab(tab, i == 0);
            tabs.add(tab);
        }

        JPanel content = new JPanel(new BorderLayout(0, 10));
        content.setOpaque(false);
        content.add(left, BorderLayout.NORTH);
        content.add(tabs, BorderLayout.SOUTH);

        wrapper.add(content, BorderLayout.WEST);
        return wrapper;
    }

    private JComponent createBody() {
        JPanel body = new JPanel(new BorderLayout(14, 14));
        body.setOpaque(false);

        JPanel leftColumn = new JPanel(new BorderLayout(0, 14));
        leftColumn.setOpaque(false);
        leftColumn.setPreferredSize(new Dimension(390, 0));
        leftColumn.add(createBattleOptions(), BorderLayout.CENTER);

        JPanel centerColumn = new JPanel(new BorderLayout(0, 14));
        centerColumn.setOpaque(false);
        centerColumn.add(createAutoSellPanel(), BorderLayout.CENTER);
        centerColumn.add(createLogPanel(), BorderLayout.SOUTH);

        JPanel rightColumn = new JPanel(new BorderLayout(0, 14));
        rightColumn.setOpaque(false);
        rightColumn.setPreferredSize(new Dimension(360, 0));
        rightColumn.add(createStatsPanel(), BorderLayout.NORTH);
        rightColumn.add(createResourcePanel(), BorderLayout.CENTER);

        body.add(leftColumn, BorderLayout.WEST);
        body.add(centerColumn, BorderLayout.CENTER);
        body.add(rightColumn, BorderLayout.EAST);
        return body;
    }

    private JComponent createBattleOptions() {
        JPanel p = titledPanel("Battle Options");
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        p.add(sectionLabel("Dungeons"));
        p.add(check("Ice Golem"));
        p.add(check("Spider"));
        p.add(check("Fire Knight"));
        p.add(check("Minotaur"));
        p.add(Box.createVerticalStrut(12));
        p.add(divider());
        p.add(Box.createVerticalStrut(12));

        p.add(sectionLabel("Campaign"));
        p.add(check("Brutal - 12-3"));
        p.add(Box.createVerticalStrut(12));
        p.add(divider());
        p.add(Box.createVerticalStrut(12));

        p.add(sectionLabel("Arena"));
        p.add(check("Classic Arena"));
        p.add(check("Tag Team Arena"));
        p.add(Box.createVerticalStrut(18));

        JPanel battleCount = new JPanel(new BorderLayout(8, 0));
        battleCount.setOpaque(false);
        JLabel label = new JLabel("Number of Battles:");
        label.setForeground(TEXT);
        label.setFont(VALUE);
        JComboBox<String> combo = new JComboBox<>(new String[]{"10", "20", "50", "100", "Unlimited"});
        combo.setSelectedIndex(1);
        styleCombo(combo);
        battleCount.add(label, BorderLayout.WEST);
        battleCount.add(combo, BorderLayout.EAST);
        p.add(battleCount);
        p.add(Box.createVerticalStrut(16));

        JCheckBox refillEnergy = check("Auto Refill Energy");
        refillEnergy.setSelected(true);
        JCheckBox refillKeys = check("Auto Refill Keys");
        refillKeys.setSelected(true);
        p.add(refillEnergy);
        p.add(refillKeys);
        p.add(Box.createVerticalGlue());
        p.add(Box.createVerticalStrut(18));

        JButton start = new JButton("Start");
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.setPreferredSize(new Dimension(0, 64));
        start.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        start.setBackground(ACCENT);
        start.setForeground(Color.WHITE);
        start.setFocusPainted(false);
        start.setFont(new Font("Segoe UI", Font.BOLD, 28));
        start.setBorder(new CompoundBorder(new LineBorder(brighten(ACCENT, 0.15f), 1, true), new EmptyBorder(10, 10, 10, 10)));
        p.add(start);

        return p;
    }

    private JComponent createAutoSellPanel() {
        JPanel p = titledPanel("Auto-Sell Settings");
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JPanel row1 = transparentFlow();
        JLabel sellArtifacts = new JLabel("Sell Artifacts:");
        sellArtifacts.setForeground(new Color(236, 209, 145));
        sellArtifacts.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JComboBox<String> stars = new JComboBox<>(new String[]{"4★ & Below", "5★ & Below", "6★ & Below"});
        stars.setSelectedItem("6★ & Below");
        styleCombo(stars);
        JComboBox<String> rarity = new JComboBox<>(new String[]{"Common", "Rare", "Rare & Epic", "All"});
        rarity.setSelectedItem("Rare & Epic");
        styleCombo(rarity);
        row1.add(sellArtifacts);
        row1.add(stars);
        row1.add(rarity);
        p.add(row1);
        p.add(Box.createVerticalStrut(16));
        p.add(divider());
        p.add(Box.createVerticalStrut(16));

        p.add(sectionLabel("Sell:"));
        JPanel sellGrid = new JPanel(new GridLayout(1, 3, 10, 10));
        sellGrid.setOpaque(false);
        sellGrid.add(iconCheck("5★ Gear", "GEAR", new Color(70, 130, 180), true));
        sellGrid.add(iconCheck("Flat Stats", "STAT", new Color(130, 100, 170), true));
        sellGrid.add(iconCheck("Bad Substats", "SUB", new Color(180, 95, 95), true));
        p.add(sellGrid);
        p.add(Box.createVerticalStrut(18));

        p.add(sectionLabel("Keep:"));
        JPanel keepGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        keepGrid.setOpaque(false);
        keepGrid.add(iconCheck("Speed Set", "SPD", new Color(64, 170, 120), true));
        keepGrid.add(iconCheck("Immortal Set", "IMM", new Color(190, 145, 65), true));
        keepGrid.add(iconCheck("Crit DMG %", "CRT", new Color(185, 90, 120), true));
        keepGrid.add(iconCheck("ACC %", "ACC", new Color(90, 140, 200), true));
        p.add(keepGrid);
        p.add(Box.createVerticalStrut(18));
        p.add(divider());
        p.add(Box.createVerticalStrut(14));

        p.add(sectionLabel("Detected Gear Preview"));
        JPanel previewGrid = new JPanel(new GridLayout(1, 4, 12, 12));
        previewGrid.setOpaque(false);
        previewGrid.add(itemCard("Weapon", "6★ Legendary", new Color(161, 99, 52)));
        previewGrid.add(itemCard("Helmet", "5★ Epic", new Color(84, 94, 168)));
        previewGrid.add(itemCard("Gloves", "6★ Rare", new Color(70, 130, 180)));
        previewGrid.add(itemCard("Boots", "Keep: SPD", new Color(64, 170, 120)));
        p.add(previewGrid);
        p.add(Box.createVerticalGlue());

        return p;
    }

    private JComponent createLogPanel() {
        JPanel p = titledPanel("Log");
        p.setPreferredSize(new Dimension(0, 180));
        p.setLayout(new BorderLayout());

        String[] columns = {"Time", "Entry"};
        Object[][] rows = {
                {"01:03:12", "Battle 115: Victory! (1m 42s)"},
                {"01:06:45", "Battle 116: Defeat..."},
                {"01:08:33", "Battle 117: Victory! (1m 46s)"}
        };

        DefaultTableModel model = new DefaultTableModel(rows, columns) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(model);
        table.setRowHeight(32);
        table.setBackground(PANEL);
        table.setForeground(TEXT);
        table.setSelectionBackground(brighten(PANEL, 0.15f));
        table.setGridColor(new Color(65, 81, 110));
        table.setFont(LABEL);
        table.getTableHeader().setBackground(PANEL_DARK);
        table.getTableHeader().setForeground(TEXT);
        table.getTableHeader().setFont(VALUE);
        table.setBorder(null);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(new Color(65, 81, 110), 1, true));
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JComponent createStatsPanel() {
        JPanel p = titledPanel("Statistics");
        p.setPreferredSize(new Dimension(0, 330));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        p.add(statLine("Runs:", "120"));
        p.add(divider());
        p.add(statLine("Wins:", "115"));
        p.add(divider());
        p.add(statLine("Losses:", "5"));
        p.add(divider());
        p.add(statLine("Avg. Time:", "1m 45s"));
        p.add(divider());
        p.add(statLine("XP Earned:", "3,450,000"));
        p.add(divider());
        p.add(statLine("Silver Gained:", "1,750,000"));
        return p;
    }

    private JComponent createResourcePanel() {
        JPanel p = titledPanel("Resource Tracker");
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        p.add(resourceLine("Energy", "845 / 130", createResourceIcon("EN", new Color(77, 196, 104))));
        p.add(divider());
        p.add(resourceLine("Blue Shards", "9", createResourceIcon("B", new Color(80, 150, 255))));
        p.add(resourceLine("Void Shards", "3", createResourceIcon("V", new Color(180, 90, 230))));
        p.add(resourceLine("Sacred Shards", "25", createResourceIcon("S", new Color(235, 175, 50))));
        p.add(divider());
        p.add(resourceLine("Silver", "27,480,000", createResourceIcon("Ag", new Color(180, 185, 195))));
        p.add(divider());
        p.add(resourceLine("Gems", "1,320", createResourceIcon("G", new Color(220, 70, 90))));
        p.add(Box.createVerticalGlue());
        return p;
    }

    private JComponent createFooter() {
        JPanel footer = panel(new BorderLayout(12, 0));
        footer.setPreferredSize(new Dimension(0, 74));
        footer.setBorder(new CompoundBorder(new LineBorder(new Color(72, 88, 118), 1, true), new EmptyBorder(10, 12, 10, 12)));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        left.add(footerButton("?"));

        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        center.setOpaque(false);
        center.add(new JLabel(makeFooterTitle()));
        center.add(footerResourceChip("Energy", "845/130", new Color(77, 196, 104)));
        center.add(footerResourceChip("Silver", "27.48M", new Color(180, 185, 195)));
        center.add(footerResourceChip("Gems", "1320", new Color(220, 70, 90)));
        center.add(footerResourceChip("Blue", "9", new Color(80, 150, 255)));
        center.add(footerResourceChip("Void", "3", new Color(180, 90, 230)));
        center.add(footerResourceChip("Sacred", "25", new Color(235, 175, 50)));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.add(footerButton("◔"));
        right.add(footerButton("☰"));
        right.add(footerButton("⚙"));

        footer.add(left, BorderLayout.WEST);
        footer.add(center, BorderLayout.CENTER);
        footer.add(right, BorderLayout.EAST);
        return footer;
    }

    private JPanel titledPanel(String title) {
        JPanel outer = panel(new BorderLayout());
        outer.setBorder(new CompoundBorder(
                new LineBorder(new Color(72, 88, 118), 1, true),
                new EmptyBorder(14, 14, 14, 14)
        ));

        JLabel lbl = new JLabel(title);
        lbl.setForeground(TEXT);
        lbl.setFont(TITLE);
        lbl.setBorder(new EmptyBorder(0, 0, 14, 0));
        outer.add(lbl, BorderLayout.NORTH);

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        outer.add(inner, BorderLayout.CENTER);
        return outer;
    }

    private JPanel panel(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setBackground(PANEL_DARK);
        return p;
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT);
        l.setFont(new Font("Segoe UI", Font.BOLD, 16));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JCheckBox check(String text) {
        JCheckBox c = new JCheckBox(text);
        c.setOpaque(false);
        c.setForeground(TEXT);
        c.setFont(LABEL);
        c.setFocusPainted(false);
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setBorder(new EmptyBorder(4, 0, 4, 0));
        return c;
    }

    private JCheckBox checkSelected(String text) {
        JCheckBox c = check(text);
        c.setSelected(true);
        return c;
    }

    private JPanel transparentFlow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    private JComboBox<String> styleCombo(JComboBox<String> combo) {
        combo.setBackground(PANEL);
        combo.setForeground(TEXT);
        combo.setFont(LABEL);
        combo.setFocusable(false);
        return combo;
    }

    private JComponent divider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(65, 81, 110));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    private JPanel statLine(String key, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(8, 0, 8, 0));

        JLabel k = new JLabel(key);
        k.setForeground(MUTED);
        k.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel v = new JLabel(value);
        v.setForeground(TEXT);
        v.setFont(BIG);

        row.add(k, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        return row;
    }

    private JPanel resourceLine(String key, String value, Icon icon) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(10, 0, 10, 0));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        JLabel k = new JLabel(key + ":");
        k.setForeground(TEXT);
        k.setFont(new Font("Segoe UI", Font.BOLD, 15));
        left.add(iconLabel);
        left.add(k);

        JLabel v = new JLabel(value);
        v.setForeground(TEXT);
        v.setFont(BIG);

        row.add(left, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        return row;
    }

    private JPanel iconCheck(String text, String iconText, Color color, boolean selected) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        wrapper.setOpaque(false);
        JCheckBox box = check(text);
        box.setSelected(selected);
        box.setBorder(new EmptyBorder(0, 0, 0, 0));
        wrapper.add(new JLabel(createBadgeIcon(iconText, color, 24, 24)));
        wrapper.add(box);
        return wrapper;
    }

    private JPanel itemCard(String title, String subtitle, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setOpaque(true);
        card.setBackground(PANEL);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(72, 88, 118), 1, true), new EmptyBorder(10, 10, 10, 10)));

        JLabel icon = new JLabel(createBadgeIcon(title.substring(0, Math.min(2, title.length())).toUpperCase(), color, 46, 46));
        icon.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel name = new JLabel(title, SwingConstants.CENTER);
        name.setForeground(TEXT);
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel desc = new JLabel(subtitle, SwingConstants.CENTER);
        desc.setForeground(MUTED);
        desc.setFont(SMALL);

        card.add(icon, BorderLayout.NORTH);
        card.add(name, BorderLayout.CENTER);
        card.add(desc, BorderLayout.SOUTH);
        return card;
    }

    private Icon createResourceIcon(String text, Color color) {
        return createBadgeIcon(text, color, 24, 24);
    }

    private Icon createBadgeIcon(String text, Color color, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.fillRoundRect(0, 0, width - 1, height - 1, 10, 10);
        g2.setColor(brighten(color, 0.2f));
        g2.drawRoundRect(0, 0, width - 1, height - 1, 10, 10);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, Math.max(10, height / 3)));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        g2.drawString(text, (width - textWidth) / 2, (height + textHeight) / 2 - 2);
        g2.dispose();
        return new ImageIcon(img);
    }

    private String makeFooterTitle() {
        return "Client Readout:";
    }

    private JPanel footerResourceChip(String name, String value, Color color) {
        JPanel chip = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        chip.setOpaque(true);
        chip.setBackground(PANEL);
        chip.setBorder(new CompoundBorder(new LineBorder(new Color(72, 88, 118), 1, true), new EmptyBorder(4, 8, 4, 8)));

        JLabel icon = new JLabel(createBadgeIcon(name.substring(0, Math.min(2, name.length())).toUpperCase(), color, 18, 18));
        JLabel text = new JLabel(name + " " + value);
        text.setForeground(TEXT);
        text.setFont(new Font("Segoe UI", Font.BOLD, 12));

        chip.add(icon);
        chip.add(text);
        return chip;
    }

    private JButton footerButton(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(42, 34));
        b.setBackground(PANEL);
        b.setForeground(TEXT);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        b.setBorder(new LineBorder(new Color(72, 88, 118), 1, true));
        return b;
    }

    private void styleTab(JButton button, boolean active) {
        button.setForeground(TEXT);
        button.setBackground(active ? ACCENT : PANEL);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBorder(new CompoundBorder(new LineBorder(new Color(72, 88, 118), 1, true), new EmptyBorder(10, 18, 10, 18)));
    }

    private Color brighten(Color color, float factor) {
        int r = Math.min(255, (int) (color.getRed() * (1 + factor)));
        int g = Math.min(255, (int) (color.getGreen() * (1 + factor)));
        int b = Math.min(255, (int) (color.getBlue() * (1 + factor)));
        return new Color(r, g, b);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new RPGHelperPrototype().setVisible(true));
    }
}
