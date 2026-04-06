import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class HomePanel extends JPanel {

    private final GameResourceReader resourceReader;
    private final Map<String, JLabel> resourceValueLabels = new LinkedHashMap<>();
    private final JLabel resourceSourceLabel = new JLabel();

    public HomePanel(GameResourceReader resourceReader) {
        this.resourceReader = resourceReader;

        setOpaque(false);
        setLayout(new BorderLayout(14, 14));

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

        add(leftColumn, BorderLayout.WEST);
        add(centerColumn, BorderLayout.CENTER);
        add(rightColumn, BorderLayout.EAST);
    }

    private JComponent createBattleOptions() {
        JPanel panel = RPGHelperTheme.titledPanel("Battle Options");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(RPGHelperTheme.sectionLabel("Dungeons"));
        panel.add(RPGHelperTheme.check("Ice Golem"));
        panel.add(RPGHelperTheme.check("Spider"));
        panel.add(RPGHelperTheme.check("Fire Knight"));
        panel.add(RPGHelperTheme.check("Minotaur"));
        panel.add(Box.createVerticalStrut(12));
        panel.add(RPGHelperTheme.divider());
        panel.add(Box.createVerticalStrut(12));

        panel.add(RPGHelperTheme.sectionLabel("Campaign"));
        panel.add(RPGHelperTheme.check("Brutal - 12-3"));
        panel.add(Box.createVerticalStrut(12));
        panel.add(RPGHelperTheme.divider());
        panel.add(Box.createVerticalStrut(12));

        panel.add(RPGHelperTheme.sectionLabel("Arena"));
        panel.add(RPGHelperTheme.check("Classic Arena"));
        panel.add(RPGHelperTheme.check("Tag Team Arena"));
        panel.add(Box.createVerticalStrut(18));

        JPanel battleCount = new JPanel(new BorderLayout(8, 0));
        battleCount.setOpaque(false);

        JLabel label = new JLabel("Number of Battles:");
        label.setForeground(RPGHelperTheme.TEXT);
        label.setFont(RPGHelperTheme.VALUE);

        JComboBox<String> combo = new JComboBox<>(new String[]{"10", "20", "50", "100", "Unlimited"});
        combo.setSelectedIndex(1);
        RPGHelperTheme.styleCombo(combo);

        battleCount.add(label, BorderLayout.WEST);
        battleCount.add(combo, BorderLayout.EAST);
        panel.add(battleCount);
        panel.add(Box.createVerticalStrut(16));

        JCheckBox refillEnergy = RPGHelperTheme.check("Auto Refill Energy");
        refillEnergy.setSelected(true);
        JCheckBox refillKeys = RPGHelperTheme.check("Auto Refill Keys");
        refillKeys.setSelected(true);
        panel.add(refillEnergy);
        panel.add(refillKeys);
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalStrut(18));

        JButton start = new ThemedButton("Start");
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.setPreferredSize(new Dimension(0, 64));
        start.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        start.setFont(new Font("Segoe UI", Font.BOLD, 28));
        RPGHelperTheme.stylePrimaryButton(start);
        start.addActionListener(event -> JOptionPane.showMessageDialog(
                this,
                "The Home screen is wired up.\nNext we can attach real battle automation logic here.",
                "Start Button",
                JOptionPane.INFORMATION_MESSAGE
        ));
        panel.add(start);

        return panel;
    }

    private JComponent createAutoSellPanel() {
        JPanel panel = RPGHelperTheme.titledPanel("Auto-Sell Settings");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel row1 = RPGHelperTheme.transparentFlow();

        JLabel sellArtifacts = new JLabel("Sell Artifacts:");
        sellArtifacts.setForeground(new Color(236, 209, 145));
        sellArtifacts.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JComboBox<String> stars = new JComboBox<>(new String[]{"4* & Below", "5* & Below", "6* & Below"});
        stars.setSelectedItem("6* & Below");
        RPGHelperTheme.styleCombo(stars);

        JComboBox<String> rarity = new JComboBox<>(new String[]{"Common", "Rare", "Rare & Epic", "All"});
        rarity.setSelectedItem("Rare & Epic");
        RPGHelperTheme.styleCombo(rarity);

        row1.add(sellArtifacts);
        row1.add(stars);
        row1.add(rarity);
        panel.add(row1);
        panel.add(Box.createVerticalStrut(16));
        panel.add(RPGHelperTheme.divider());
        panel.add(Box.createVerticalStrut(16));

        panel.add(RPGHelperTheme.sectionLabel("Sell:"));
        JPanel sellGrid = new JPanel(new GridLayout(1, 3, 10, 10));
        sellGrid.setOpaque(false);
        sellGrid.add(RPGHelperTheme.iconCheck("5* Gear", "GEAR", new Color(70, 130, 180), true));
        sellGrid.add(RPGHelperTheme.iconCheck("Flat Stats", "STAT", new Color(130, 100, 170), true));
        sellGrid.add(RPGHelperTheme.iconCheck("Bad Substats", "SUB", new Color(180, 95, 95), true));
        panel.add(sellGrid);
        panel.add(Box.createVerticalStrut(18));

        panel.add(RPGHelperTheme.sectionLabel("Keep:"));
        JPanel keepGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        keepGrid.setOpaque(false);
        keepGrid.add(RPGHelperTheme.iconCheck("Speed Set", "SPD", new Color(64, 170, 120), true));
        keepGrid.add(RPGHelperTheme.iconCheck("Immortal Set", "IMM", new Color(190, 145, 65), true));
        keepGrid.add(RPGHelperTheme.iconCheck("Crit DMG %", "CRT", new Color(185, 90, 120), true));
        keepGrid.add(RPGHelperTheme.iconCheck("ACC %", "ACC", new Color(90, 140, 200), true));
        panel.add(keepGrid);
        panel.add(Box.createVerticalStrut(18));
        panel.add(RPGHelperTheme.divider());
        panel.add(Box.createVerticalStrut(14));

        panel.add(RPGHelperTheme.sectionLabel("Detected Gear Preview"));
        JPanel previewGrid = new JPanel(new GridLayout(1, 4, 12, 12));
        previewGrid.setOpaque(false);
        previewGrid.add(RPGHelperTheme.itemCard("Weapon", "6* Legendary", new Color(161, 99, 52)));
        previewGrid.add(RPGHelperTheme.itemCard("Helmet", "5* Epic", new Color(84, 94, 168)));
        previewGrid.add(RPGHelperTheme.itemCard("Gloves", "6* Rare", new Color(70, 130, 180)));
        previewGrid.add(RPGHelperTheme.itemCard("Boots", "Keep: SPD", new Color(64, 170, 120)));
        panel.add(previewGrid);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JComponent createLogPanel() {
        JPanel panel = RPGHelperTheme.titledPanel("Log");
        panel.setPreferredSize(new Dimension(0, 180));
        panel.setLayout(new BorderLayout());

        String[] columns = {"Time", "Entry"};
        Object[][] rows = {
                {"01:03:12", "Battle 115: Victory! (1m 42s)"},
                {"01:06:45", "Battle 116: Defeat..."},
                {"01:08:33", "Battle 117: Victory! (1m 46s)"}
        };

        DefaultTableModel model = new DefaultTableModel(rows, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(32);
        table.setBackground(RPGHelperTheme.PANEL);
        table.setForeground(RPGHelperTheme.TEXT);
        table.setSelectionBackground(RPGHelperTheme.brighten(RPGHelperTheme.PANEL, 0.15f));
        table.setGridColor(new Color(65, 81, 110));
        table.setFont(RPGHelperTheme.LABEL);
        table.getTableHeader().setBackground(RPGHelperTheme.PANEL_DARK);
        table.getTableHeader().setForeground(RPGHelperTheme.TEXT);
        table.getTableHeader().setFont(RPGHelperTheme.VALUE);
        table.setBorder(null);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(new Color(65, 81, 110), 1, true));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JComponent createStatsPanel() {
        JPanel panel = RPGHelperTheme.titledPanel("Statistics");
        panel.setPreferredSize(new Dimension(0, 330));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(RPGHelperTheme.statLine("Runs:", "120"));
        panel.add(RPGHelperTheme.divider());
        panel.add(RPGHelperTheme.statLine("Wins:", "115"));
        panel.add(RPGHelperTheme.divider());
        panel.add(RPGHelperTheme.statLine("Losses:", "5"));
        panel.add(RPGHelperTheme.divider());
        panel.add(RPGHelperTheme.statLine("Avg. Time:", "1m 45s"));
        panel.add(RPGHelperTheme.divider());
        panel.add(RPGHelperTheme.statLine("XP Earned:", "3,450,000"));
        panel.add(RPGHelperTheme.divider());
        panel.add(RPGHelperTheme.statLine("Silver Gained:", "1,750,000"));
        return panel;
    }

    private JComponent createResourcePanel() {
        JPanel panel = RPGHelperTheme.titledPanel("Resource Tracker");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createTrackedResourceLine("Energy", "845 / 130", "EN", new Color(77, 196, 104)));
        panel.add(RPGHelperTheme.divider());
        panel.add(createTrackedResourceLine("Blue Shards", "9", "B", new Color(80, 150, 255)));
        panel.add(createTrackedResourceLine("Void Shards", "3", "V", new Color(180, 90, 230)));
        panel.add(createTrackedResourceLine("Sacred Shards", "25", "S", new Color(235, 175, 50)));
        panel.add(RPGHelperTheme.divider());
        panel.add(createTrackedResourceLine("Silver", "27,480,000", "AG", new Color(180, 185, 195)));
        panel.add(RPGHelperTheme.divider());
        panel.add(createTrackedResourceLine("Gems", "1,320", "G", new Color(220, 70, 90)));
        panel.add(Box.createVerticalStrut(12));

        resourceSourceLabel.setForeground(RPGHelperTheme.MUTED);
        resourceSourceLabel.setFont(RPGHelperTheme.SMALL);
        resourceSourceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(resourceSourceLabel);
        panel.add(Box.createVerticalStrut(10));

        JButton syncButton = new ThemedButton("Sync Resources");
        syncButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        syncButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        RPGHelperTheme.styleSecondaryButton(syncButton);
        syncButton.addActionListener(event -> refreshResources());
        panel.add(syncButton);
        panel.add(Box.createVerticalGlue());

        refreshResources();
        return panel;
    }

    private JPanel createTrackedResourceLine(String key, String initialValue, String iconText, Color color) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(10, 0, 10, 0));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        JLabel iconLabel = new JLabel(RPGHelperTheme.createResourceIcon(iconText, color));
        JLabel keyLabel = new JLabel(key + ":");
        keyLabel.setForeground(RPGHelperTheme.TEXT);
        keyLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        left.add(iconLabel);
        left.add(keyLabel);

        JLabel valueLabel = new JLabel(initialValue);
        valueLabel.setForeground(RPGHelperTheme.TEXT);
        valueLabel.setFont(RPGHelperTheme.BIG);
        resourceValueLabels.put(key, valueLabel);

        row.add(left, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);
        return row;
    }

    private void refreshResources() {
        GameResourceSnapshot snapshot = resourceReader.readSnapshot();
        resourceValueLabels.get("Energy").setText(snapshot.getEnergy());
        resourceValueLabels.get("Blue Shards").setText(snapshot.getBlueShards());
        resourceValueLabels.get("Void Shards").setText(snapshot.getVoidShards());
        resourceValueLabels.get("Sacred Shards").setText(snapshot.getSacredShards());
        resourceValueLabels.get("Silver").setText(snapshot.getSilver());
        resourceValueLabels.get("Gems").setText(snapshot.getGems());
        resourceSourceLabel.setText("Source: " + snapshot.getSource());
    }
}
