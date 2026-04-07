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
    private final Map<String, JLabel> footerResourceLabels = new LinkedHashMap<>();
    private final AppSettingsStore settingsStore = new AppSettingsStore();
    private final AppSettings settings = settingsStore.load();
    private final RaidClientService raidClientService = new RaidClientService();
    private final AppLogService logService = new AppLogService();
    private final MutableMockGameResourceReader debugReader = new MutableMockGameResourceReader();
    private final ScreenCaptureGameResourceReader resourceReader = new ScreenCaptureGameResourceReader(debugReader, settings, raidClientService);
    private final HomePanel homePanel = new HomePanel(resourceReader, logService);
    private final SettingsPanel settingsPanel = new SettingsPanel(settings, settingsStore, raidClientService, resourceReader, new SettingsChangeListener() {
        @Override
        public void onSettingsSaved(AppSettings updatedSettings) {
            refreshDebugData();
        }
    }, logService);
    private final DebugHotfixFrame debugHotfixFrame = new DebugHotfixFrame(this, debugReader, logService);
    private final LogFrame logFrame = new LogFrame(this, logService);

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

        seedInitialLogs();
        registerDebugShortcut();
        showScreen("Home");
        scheduleStartupTasks();
    }

    private void registerScreens() {
        screenContainer.setOpaque(false);
        screenContainer.add(homePanel, "Home");
        screenContainer.add(new AutoFarmPanel(logService), "Auto-Farm");
        screenContainer.add(new ChampionUpgradesPanel(), "Champion Upgrades");
        screenContainer.add(new ForgePanel(), "Forge");
        screenContainer.add(new QuestsEventPanel(), "Quests / Event");
        screenContainer.add(settingsPanel, "Settings");
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

        JButton debugButton = RPGHelperTheme.footerButton("D");
        debugButton.addActionListener(event -> openDebugHotfixWindow());
        left.add(debugButton);

        JButton logButton = RPGHelperTheme.footerButton("L");
        logButton.addActionListener(event -> openLogWindow());
        left.add(logButton);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        center.setOpaque(false);
        center.add(new JLabel(makeFooterTitle()));
        center.add(createFooterResourceChip("Energy", "845/130", new Color(77, 196, 104)));
        center.add(createFooterResourceChip("Silver", "27.48M", new Color(180, 185, 195)));
        center.add(createFooterResourceChip("Gems", "1320", new Color(220, 70, 90)));
        center.add(createFooterResourceChip("Ancient", "9", new Color(80, 150, 255)));
        center.add(createFooterResourceChip("Void", "3", new Color(180, 90, 230)));
        center.add(createFooterResourceChip("Sacred", "25", new Color(235, 175, 50)));

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

    public void showScreen(String screenName) {
        cardLayout.show(screenContainer, screenName);
        for (Map.Entry<String, JButton> entry : navigationButtons.entrySet()) {
            RPGHelperTheme.styleTab(entry.getValue(), entry.getKey().equals(screenName));
        }
    }

    public void refreshDebugData() {
        GameResourceSnapshot snapshot = resourceReader.readSnapshot();
        homePanel.refreshResources(snapshot);
        refreshFooterResources(snapshot);
        settingsPanel.refreshStatus();
        logFrame.refreshEntries();
        repaint();
    }

    private void scheduleStartupTasks() {
        Timer startupTimer = new Timer(500, event -> {
            launchRaidIfConfigured();
            refreshDebugData();
        });
        startupTimer.setRepeats(false);
        startupTimer.start();
    }

    private void launchRaidIfConfigured() {
        if (!settings.isAutoLaunchRaidOnStartup()) {
            logService.log("Startup: auto-launch is disabled.");
            return;
        }

        if (settings.getRaidExecutablePath().isEmpty()) {
            logService.log("Startup: auto-launch is enabled, but no Raid path is configured.");
            return;
        }

        if (raidClientService.isRaidRunning()) {
            logService.log("Startup: Raid is already running, so no launch was needed.");
            return;
        }

        String launchMessage = raidClientService.launchRaid(settings);
        logService.log("Startup auto-launch: " + launchMessage);
    }

    private void registerDebugShortcut() {
        JRootPane rootPane = getRootPane();
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("F12"), "openDebugHotfix");
        rootPane.getActionMap().put("openDebugHotfix", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent event) {
                openDebugHotfixWindow();
            }
        });
    }

    private void openDebugHotfixWindow() {
        debugHotfixFrame.setVisible(true);
        debugHotfixFrame.toFront();
    }

    private void openLogWindow() {
        logFrame.refreshEntries();
        logFrame.setVisible(true);
        logFrame.toFront();
    }

    private void seedInitialLogs() {
        logService.log("RPG Helper started.");
        logService.log("Startup setting: auto-launch is " + (settings.isAutoLaunchRaidOnStartup() ? "enabled" : "disabled") + ".");
        logService.log("Example: Replacing Kael with Deacon Armstrong champion.");
    }

    private String makeFooterTitle() {
        return "Client Readout:";
    }

    private JPanel createFooterResourceChip(String name, String initialValue, Color color) {
        JPanel chip = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        chip.setOpaque(true);
        chip.setBackground(RPGHelperTheme.PANEL);
        chip.setBorder(new CompoundBorder(
                new LineBorder(new Color(72, 88, 118), 1, true),
                new EmptyBorder(4, 8, 4, 8)
        ));

        JLabel icon = new JLabel(RPGHelperTheme.createBadgeIcon(name.substring(0, Math.min(2, name.length())).toUpperCase(), color, 18, 18));
        JLabel text = new JLabel(name + " " + initialValue);
        text.setForeground(RPGHelperTheme.TEXT);
        text.setFont(new Font("Segoe UI", Font.BOLD, 12));
        footerResourceLabels.put(name, text);

        chip.add(icon);
        chip.add(text);
        return chip;
    }

    private void refreshFooterResources(GameResourceSnapshot snapshot) {
        footerResourceLabels.get("Energy").setText("Energy " + snapshot.getEnergy());
        footerResourceLabels.get("Silver").setText("Silver " + snapshot.getSilver());
        footerResourceLabels.get("Gems").setText("Gems " + snapshot.getGems());
        footerResourceLabels.get("Ancient").setText("Ancient " + snapshot.getBlueShards());
        footerResourceLabels.get("Void").setText("Void " + snapshot.getVoidShards());
        footerResourceLabels.get("Sacred").setText("Sacred " + snapshot.getSacredShards());
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> new RPGHelperPrototype().setVisible(true));
    }
}
