import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SettingsPanel extends JPanel {

    private final AppSettings settings;
    private final AppSettingsStore settingsStore;
    private final RaidClientService raidClientService;
    private final SettingsChangeListener settingsChangeListener;
    private final AppLogService logService;

    private final JTextField executablePathField = new JTextField();
    private final JCheckBox autoLaunchCheck = RPGHelperTheme.check("Launch Raid on app startup");
    private final JLabel statusLabel = new JLabel();

    public SettingsPanel(
            AppSettings settings,
            AppSettingsStore settingsStore,
            RaidClientService raidClientService,
            SettingsChangeListener settingsChangeListener,
            AppLogService logService
    ) {
        this.settings = settings;
        this.settingsStore = settingsStore;
        this.raidClientService = raidClientService;
        this.settingsChangeListener = settingsChangeListener;
        this.logService = logService;

        setOpaque(false);
        setLayout(new BorderLayout(14, 14));
        add(createRaidClientPanel(), BorderLayout.CENTER);
    }

    private JComponent createRaidClientPanel() {
        JPanel panel = RPGHelperTheme.titledPanel("Settings");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Raid Client Integration");
        title.setForeground(RPGHelperTheme.TEXT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(12));

        JTextArea description = new JTextArea(
                "Set the Raid executable path, choose whether the game should launch when RPG Helper starts, and check whether the client is currently running."
        );
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setFocusable(false);
        description.setOpaque(false);
        description.setForeground(RPGHelperTheme.MUTED);
        description.setFont(RPGHelperTheme.LABEL);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(description);
        panel.add(Box.createVerticalStrut(22));

        panel.add(buildFieldLabel("Raid executable path"));
        executablePathField.setText(settings.getRaidExecutablePath());
        executablePathField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        executablePathField.setBackground(RPGHelperTheme.PANEL);
        executablePathField.setForeground(RPGHelperTheme.TEXT);
        executablePathField.setCaretColor(RPGHelperTheme.TEXT);
        executablePathField.setBorder(new EmptyBorder(10, 12, 10, 12));
        executablePathField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(executablePathField);
        panel.add(Box.createVerticalStrut(12));

        autoLaunchCheck.setSelected(settings.isAutoLaunchRaidOnStartup());
        panel.add(autoLaunchCheck);
        panel.add(Box.createVerticalStrut(16));

        statusLabel.setForeground(RPGHelperTheme.TEXT);
        statusLabel.setFont(RPGHelperTheme.VALUE);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(statusLabel);
        panel.add(Box.createVerticalStrut(20));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton saveButton = new ThemedButton("Save Settings");
        RPGHelperTheme.stylePrimaryButton(saveButton);
        saveButton.addActionListener(event -> saveSettings());

        JButton launchButton = new ThemedButton("Launch Raid Now");
        RPGHelperTheme.styleSecondaryButton(launchButton);
        launchButton.addActionListener(event -> launchRaidNow());

        JButton refreshButton = new ThemedButton("Refresh Status");
        RPGHelperTheme.styleSecondaryButton(refreshButton);
        refreshButton.addActionListener(event -> refreshStatus());

        buttonRow.add(saveButton);
        buttonRow.add(launchButton);
        buttonRow.add(refreshButton);
        panel.add(buttonRow);
        panel.add(Box.createVerticalGlue());

        refreshStatus();
        return panel;
    }

    private JLabel buildFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(RPGHelperTheme.TEXT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void saveSettings() {
        settings.setRaidExecutablePath(executablePathField.getText());
        settings.setAutoLaunchRaidOnStartup(autoLaunchCheck.isSelected());

        try {
            settingsStore.save(settings);
            settingsChangeListener.onSettingsSaved(settings);
            statusLabel.setText("Settings saved. " + raidClientService.describeStatus(settings));
            logService.log("Saved Raid settings. Auto launch is " + (settings.isAutoLaunchRaidOnStartup() ? "enabled" : "disabled") + ".");
        } catch (Exception exception) {
            statusLabel.setText("Failed to save settings: " + exception.getMessage());
            logService.log("Failed to save settings: " + exception.getMessage());
        }
    }

    private void launchRaidNow() {
        settings.setRaidExecutablePath(executablePathField.getText());
        settings.setAutoLaunchRaidOnStartup(autoLaunchCheck.isSelected());
        String message = raidClientService.launchRaid(settings);
        statusLabel.setText(message);
        logService.log(message);
    }

    public void refreshStatus() {
        statusLabel.setText(raidClientService.describeStatus(settings));
    }
}
