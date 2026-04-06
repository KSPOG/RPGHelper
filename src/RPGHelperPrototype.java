import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class RPGHelperPrototype extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel screenContainer = new JPanel(cardLayout);
    private final Map<String, JButton> navigationButtons = new LinkedHashMap<>();
    private final GameResourceReader resourceReader = new ScreenCaptureGameResourceReader(new MockGameResourceReader());

    public RPGHelperPrototype() {
        setTitle("RPG Helper");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1450, 900);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 760));

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(RPGHelperTheme.BG);
        root.setBorder(new EmptyBorder(14, 14, 14, 14));
        setContentPane(root);

        registerScreens();

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createBody(), BorderLayout.CENTER);
        root.add(createFooter(), BorderLayout.SOUTH);

        showScreen("Home");
    }

    private void registerScreens() {
        screenContainer.setOpaque(false);
        screenContainer.add(new HomePanel(resourceReader), "Home");
        screenContainer.add(new AutoFarmPanel(), "Auto-Farm");
        screenContainer.add(new ChampionUpgradesPanel(), "Champion Upgrades");
        screenContainer.add(new ForgePanel(), "Forge");
        screenContainer.add(new QuestsEventPanel(), "Quests / Event");
        screenContainer.add(new SettingsPanel(), "Settings");
    }

    private JComponent createHeader() {
        JPanel wrapper = RPGHelperTheme.panel(new BorderLayout(12, 12));
        wrapper.setPreferredSize(new Dimension(0, 92));

        JPanel left = new JPanel(new BorderLayout(10, 0));
        left.setOpaque(false);

        JLabel appIcon = new JLabel("RH");
        appIcon.setForeground(RPGHelperTheme.TEXT);
        appIcon.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel title = new JLabel("RPG Helper");
        title.setForeground(RPGHelperTheme.TEXT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        left.add(appIcon, BorderLayout.WEST);
        left.add(title, BorderLayout.CENTER);

        JPanel tabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        tabs.setOpaque(false);

        String[] tabNames = {"Home", "Auto-Farm", "Champion Upgrades", "Forge", "Quests / Event", "Settings"};
        for (String tabName : tabNames) {
            JButton button = new ThemedButton(tabName);
            RPGHelperTheme.styleTab(button, "Home".equals(tabName));
            button.addActionListener(event -> showScreen(tabName));
            navigationButtons.put(tabName, button);
            tabs.add(button);
        }

        JPanel content = new JPanel(new BorderLayout(0, 10));
        content.setOpaque(false);
        content.add(left, BorderLayout.NORTH);
        content.add(tabs, BorderLayout.SOUTH);

        wrapper.add(content, BorderLayout.WEST);
        return wrapper;
    }

    private JComponent createBody() {
        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        body.add(screenContainer, BorderLayout.CENTER);
        return body;
    }

    private JComponent createFooter() {
        JPanel footer = RPGHelperTheme.panel(new BorderLayout(12, 0));
        footer.setPreferredSize(new Dimension(0, 74));
        footer.setBorder(new CompoundBorder(
                new LineBorder(new Color(72, 88, 118), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);

        JButton helpButton = RPGHelperTheme.footerButton("?");
        helpButton.addActionListener(event -> JOptionPane.showMessageDialog(
                this,
                "Use the top navigation buttons to switch between the new panel files.",
                "Help",
                JOptionPane.INFORMATION_MESSAGE
        ));
        left.add(helpButton);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        center.setOpaque(false);
        center.add(new JLabel(makeFooterTitle()));
        center.add(RPGHelperTheme.footerResourceChip("Energy", "845/130", new Color(77, 196, 104)));
        center.add(RPGHelperTheme.footerResourceChip("Silver", "27.48M", new Color(180, 185, 195)));
        center.add(RPGHelperTheme.footerResourceChip("Gems", "1320", new Color(220, 70, 90)));
        center.add(RPGHelperTheme.footerResourceChip("Blue", "9", new Color(80, 150, 255)));
        center.add(RPGHelperTheme.footerResourceChip("Void", "3", new Color(180, 90, 230)));
        center.add(RPGHelperTheme.footerResourceChip("Sacred", "25", new Color(235, 175, 50)));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        JButton homeButton = RPGHelperTheme.footerButton("H");
        homeButton.addActionListener(event -> showScreen("Home"));
        right.add(homeButton);

        JButton settingsButton = RPGHelperTheme.footerButton("S");
        settingsButton.addActionListener(event -> showScreen("Settings"));
        right.add(settingsButton);

        JButton exitButton = RPGHelperTheme.footerButton("X");
        exitButton.addActionListener(event -> dispose());
        right.add(exitButton);

        footer.add(left, BorderLayout.WEST);
        footer.add(center, BorderLayout.CENTER);
        footer.add(right, BorderLayout.EAST);
        return footer;
    }

    private void showScreen(String screenName) {
        cardLayout.show(screenContainer, screenName);
        for (Map.Entry<String, JButton> entry : navigationButtons.entrySet()) {
            RPGHelperTheme.styleTab(entry.getValue(), entry.getKey().equals(screenName));
        }
    }

    private String makeFooterTitle() {
        return "Client Readout:";
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> new RPGHelperPrototype().setVisible(true));
    }
}
