import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SettingsPanel extends JPanel {

    private final AppSettings settings;
    private final AppSettingsStore settingsStore;
    private final RaidClientService raidClientService;
    private final ScreenCaptureGameResourceReader screenCaptureGameResourceReader;
    private final SettingsChangeListener settingsChangeListener;
    private final AppLogService logService;

    private final JTextField executablePathField = new JTextField();
    private final JCheckBox autoLaunchCheck = RPGHelperTheme.check("Launch Raid on app startup");
    private final JLabel statusLabel = new JLabel();
    private final JLabel saveLocationLabel = new JLabel();
    private final JLabel energyRegionLabel = new JLabel();
    private final JLabel silverRegionLabel = new JLabel();
    private final JLabel gemsRegionLabel = new JLabel();
    private final JLabel energyTestLabel = new JLabel();
    private final JLabel silverTestLabel = new JLabel();
    private final JLabel gemsTestLabel = new JLabel();
    private final JLabel calibrationSummaryLabel = new JLabel();

    public SettingsPanel(
            AppSettings settings,
            AppSettingsStore settingsStore,
            RaidClientService raidClientService,
            ScreenCaptureGameResourceReader screenCaptureGameResourceReader,
            SettingsChangeListener settingsChangeListener,
            AppLogService logService
    ) {
        this.settings = settings;
        this.settingsStore = settingsStore;
        this.raidClientService = raidClientService;
        this.screenCaptureGameResourceReader = screenCaptureGameResourceReader;
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
                "Set the Raid executable path, choose whether the game should launch when RPG Helper starts, and check whether the client is currently running. You can point this to the game .exe or to a Raid / Plarium install folder."
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

        panel.add(buildFieldLabel("Resource capture calibration"));
        panel.add(Box.createVerticalStrut(8));
        panel.add(createCalibrationRow("Energy", energyRegionLabel, new Runnable() {
            @Override
            public void run() {
                startCalibration("Energy", new ScreenRegionSelectionListener() {
                    @Override
                    public void onRegionSelected(ScreenRegion region) {
                        settings.setEnergyRegion(region);
                        energyRegionLabel.setText(formatRegion(region));
                        persistCalibration("energy");
                    }
                });
            }
        }));
        panel.add(createCalibrationRow("Silver", silverRegionLabel, new Runnable() {
            @Override
            public void run() {
                startCalibration("Silver", new ScreenRegionSelectionListener() {
                    @Override
                    public void onRegionSelected(ScreenRegion region) {
                        settings.setSilverRegion(region);
                        silverRegionLabel.setText(formatRegion(region));
                        persistCalibration("silver");
                    }
                });
            }
        }));
        panel.add(createCalibrationRow("Gems", gemsRegionLabel, new Runnable() {
            @Override
            public void run() {
                startCalibration("Gems", new ScreenRegionSelectionListener() {
                    @Override
                    public void onRegionSelected(ScreenRegion region) {
                        settings.setGemsRegion(region);
                        gemsRegionLabel.setText(formatRegion(region));
                        persistCalibration("gems");
                    }
                });
            }
        }));
        panel.add(Box.createVerticalStrut(16));

        JPanel calibrationTestRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        calibrationTestRow.setOpaque(false);
        calibrationTestRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton testCalibrationButton = new ThemedButton("Test Calibration");
        RPGHelperTheme.styleSecondaryButton(testCalibrationButton);
        testCalibrationButton.addActionListener(event -> testCalibration());
        calibrationTestRow.add(testCalibrationButton);
        panel.add(calibrationTestRow);
        panel.add(Box.createVerticalStrut(10));

        panel.add(buildDebugLine("Energy OCR", energyTestLabel));
        panel.add(buildDebugLine("Silver OCR", silverTestLabel));
        panel.add(buildDebugLine("Gems OCR", gemsTestLabel));
        calibrationSummaryLabel.setForeground(RPGHelperTheme.MUTED);
        calibrationSummaryLabel.setFont(RPGHelperTheme.SMALL);
        calibrationSummaryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        calibrationSummaryLabel.setText("Run Test Calibration to inspect raw OCR results.");
        panel.add(calibrationSummaryLabel);
        panel.add(Box.createVerticalStrut(16));

        saveLocationLabel.setForeground(RPGHelperTheme.MUTED);
        saveLocationLabel.setFont(RPGHelperTheme.SMALL);
        saveLocationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveLocationLabel.setText("Settings file: " + settingsStore.getSettingsFileLocation());
        panel.add(saveLocationLabel);
        panel.add(Box.createVerticalStrut(10));

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

        energyRegionLabel.setText(formatRegion(settings.getEnergyRegion()));
        silverRegionLabel.setText(formatRegion(settings.getSilverRegion()));
        gemsRegionLabel.setText(formatRegion(settings.getGemsRegion()));
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

    private JPanel buildDebugLine(String labelText, JLabel valueLabel) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(new EmptyBorder(3, 0, 3, 0));

        JLabel label = new JLabel(labelText + ":");
        label.setForeground(RPGHelperTheme.TEXT);
        label.setFont(RPGHelperTheme.VALUE);

        valueLabel.setForeground(RPGHelperTheme.MUTED);
        valueLabel.setFont(RPGHelperTheme.SMALL);

        row.add(label, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.CENTER);
        return row;
    }

    private JPanel createCalibrationRow(String name, JLabel valueLabel, Runnable action) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(new EmptyBorder(4, 0, 4, 0));

        JLabel nameLabel = new JLabel(name + " region:");
        nameLabel.setForeground(RPGHelperTheme.TEXT);
        nameLabel.setFont(RPGHelperTheme.VALUE);

        valueLabel.setForeground(RPGHelperTheme.MUTED);
        valueLabel.setFont(RPGHelperTheme.SMALL);

        JButton calibrateButton = new ThemedButton("Calibrate");
        RPGHelperTheme.styleSecondaryButton(calibrateButton);
        calibrateButton.addActionListener(event -> action.run());

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(valueLabel, BorderLayout.CENTER);

        row.add(nameLabel, BorderLayout.WEST);
        row.add(center, BorderLayout.CENTER);
        row.add(calibrateButton, BorderLayout.EAST);
        return row;
    }

    private void startCalibration(String resourceName, ScreenRegionSelectionListener listener) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        ScreenRegionSelectionOverlay overlay = new ScreenRegionSelectionOverlay(owner, resourceName, listener);
        overlay.getRootPane().registerKeyboardAction(event -> overlay.dispose(),
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        overlay.setVisible(true);
    }

    private void persistCalibration(String resourceName) {
        try {
            settingsStore.save(settings);
            settingsChangeListener.onSettingsSaved(settings);
            statusLabel.setText("Saved " + resourceName + " calibration. " + raidClientService.describeStatus(settings));
            logService.log("Saved " + resourceName + " calibration region.");
        } catch (Exception exception) {
            statusLabel.setText("Failed to save " + resourceName + " calibration: " + exception.getMessage());
            logService.log("Failed to save " + resourceName + " calibration: " + exception.getMessage());
        }
    }

    private String formatRegion(ScreenRegion region) {
        return region == null ? "Not calibrated" : region.toString();
    }

    private void testCalibration() {
        CalibratedResourceReadResult result = screenCaptureGameResourceReader.testCalibratedRead();
        energyTestLabel.setText(result.getEnergyValue() + " | raw: " + result.getEnergyRawText());
        silverTestLabel.setText(result.getSilverValue() + " | raw: " + result.getSilverRawText());
        gemsTestLabel.setText(result.getGemsValue() + " | raw: " + result.getGemsRawText());
        calibrationSummaryLabel.setText(result.getSourceSummary());
        logService.log("Ran calibration test for energy, silver, and gems.");
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
        statusLabel.setText(raidClientService.describeStatus(settings) + " " + raidClientService.describeConfiguredTarget(settings));
    }
}
