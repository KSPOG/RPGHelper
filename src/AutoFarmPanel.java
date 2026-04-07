import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AutoFarmPanel extends JPanel {

    private final AppLogService logService;

    private final JTextField energyField = createInput("1399");
    private final JTextField energyPerRunField = createInput("8");
    private final JTextField commonCountField = createInput("12");
    private final JTextField uncommonCountField = createInput("8");
    private final JTextField rareCountField = createInput("4");
    private final JTextField commonRunsField = createInput("6");
    private final JTextField uncommonRunsField = createInput("18");
    private final JTextField rareRunsField = createInput("54");

    private final JLabel availableRunsLabel = createValueLabel();
    private final JLabel commonResultLabel = createValueLabel();
    private final JLabel uncommonResultLabel = createValueLabel();
    private final JLabel rareResultLabel = createValueLabel();
    private final JLabel totalEnergyNeededLabel = createValueLabel();
    private final JLabel summaryLabel = createSummaryLabel();

    public AutoFarmPanel(AppLogService logService) {
        this.logService = logService;

        setOpaque(false);
        setLayout(new BorderLayout(14, 14));
        add(createCalculatorPanel(), BorderLayout.CENTER);
        calculateRuns();
    }

    private JComponent createCalculatorPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(14, 14));
        wrapper.setOpaque(false);

        JPanel configurationPanel = RPGHelperTheme.titledPanel("Auto-Farm Calculator");
        configurationPanel.setLayout(new BoxLayout(configurationPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Food Run Estimator");
        title.setForeground(RPGHelperTheme.TEXT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        configurationPanel.add(title);
        configurationPanel.add(Box.createVerticalStrut(10));

        JTextArea description = new JTextArea(
                "Estimate how many common, uncommon, and rare champions you can run with your current energy. The default runs-per-champion values are editable so you can tune them to your own farming setup."
        );
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setFocusable(false);
        description.setOpaque(false);
        description.setForeground(RPGHelperTheme.MUTED);
        description.setFont(RPGHelperTheme.LABEL);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);
        configurationPanel.add(description);
        configurationPanel.add(Box.createVerticalStrut(18));

        configurationPanel.add(createInputRow("Current energy", energyField));
        configurationPanel.add(createInputRow("Energy cost per run", energyPerRunField));
        configurationPanel.add(Box.createVerticalStrut(14));
        configurationPanel.add(RPGHelperTheme.divider());
        configurationPanel.add(Box.createVerticalStrut(14));

        configurationPanel.add(createSectionHeader("Champion stock"));
        configurationPanel.add(createInputRow("Common champions", commonCountField));
        configurationPanel.add(createInputRow("Uncommon champions", uncommonCountField));
        configurationPanel.add(createInputRow("Rare champions", rareCountField));
        configurationPanel.add(Box.createVerticalStrut(14));

        configurationPanel.add(createSectionHeader("Runs needed per champion"));
        configurationPanel.add(createInputRow("Common runs", commonRunsField));
        configurationPanel.add(createInputRow("Uncommon runs", uncommonRunsField));
        configurationPanel.add(createInputRow("Rare runs", rareRunsField));
        configurationPanel.add(Box.createVerticalStrut(18));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton calculateButton = new ThemedButton("Calculate Runs");
        RPGHelperTheme.stylePrimaryButton(calculateButton);
        calculateButton.addActionListener(event -> calculateRuns());

        JButton resetButton = new ThemedButton("Reset Defaults");
        RPGHelperTheme.styleSecondaryButton(resetButton);
        resetButton.addActionListener(event -> resetDefaults());

        buttonRow.add(calculateButton);
        buttonRow.add(resetButton);
        configurationPanel.add(buttonRow);

        JPanel resultsPanel = RPGHelperTheme.titledPanel("Results");
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.add(createResultLine("Available runs", availableRunsLabel));
        resultsPanel.add(RPGHelperTheme.divider());
        resultsPanel.add(createResultLine("Common coverage", commonResultLabel));
        resultsPanel.add(createResultLine("Uncommon coverage", uncommonResultLabel));
        resultsPanel.add(createResultLine("Rare coverage", rareResultLabel));
        resultsPanel.add(RPGHelperTheme.divider());
        resultsPanel.add(createResultLine("Total energy needed", totalEnergyNeededLabel));
        resultsPanel.add(Box.createVerticalStrut(12));
        resultsPanel.add(summaryLabel);

        wrapper.add(configurationPanel, BorderLayout.WEST);
        wrapper.add(resultsPanel, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createInputRow(String labelText, JTextField field) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(4, 0, 4, 0));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setForeground(RPGHelperTheme.TEXT);
        label.setFont(RPGHelperTheme.VALUE);

        row.add(label, BorderLayout.WEST);
        row.add(field, BorderLayout.EAST);
        return row;
    }

    private JPanel createResultLine(String labelText, JLabel valueLabel) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(8, 0, 8, 0));

        JLabel label = new JLabel(labelText);
        label.setForeground(RPGHelperTheme.MUTED);
        label.setFont(RPGHelperTheme.VALUE);

        row.add(label, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);
        return row;
    }

    private JLabel createSectionHeader(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(RPGHelperTheme.TEXT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 8, 0));
        return label;
    }

    private JTextField createInput(String value) {
        JTextField field = new JTextField(value);
        field.setPreferredSize(new Dimension(120, 34));
        field.setBackground(RPGHelperTheme.PANEL);
        field.setForeground(RPGHelperTheme.TEXT);
        field.setCaretColor(RPGHelperTheme.TEXT);
        field.setBorder(new EmptyBorder(8, 10, 8, 10));
        field.setHorizontalAlignment(SwingConstants.RIGHT);
        return field;
    }

    private JLabel createValueLabel() {
        JLabel label = new JLabel("-");
        label.setForeground(RPGHelperTheme.TEXT);
        label.setFont(RPGHelperTheme.BIG);
        return label;
    }

    private JLabel createSummaryLabel() {
        JLabel label = new JLabel();
        label.setForeground(RPGHelperTheme.MUTED);
        label.setFont(RPGHelperTheme.LABEL);
        return label;
    }

    private void calculateRuns() {
        try {
            int energy = parseNonNegative(energyField.getText());
            int energyPerRun = parsePositive(energyPerRunField.getText());
            int commonCount = parseNonNegative(commonCountField.getText());
            int uncommonCount = parseNonNegative(uncommonCountField.getText());
            int rareCount = parseNonNegative(rareCountField.getText());
            int commonRuns = parseNonNegative(commonRunsField.getText());
            int uncommonRuns = parseNonNegative(uncommonRunsField.getText());
            int rareRuns = parseNonNegative(rareRunsField.getText());

            int availableRuns = energy / energyPerRun;
            int totalRunsNeeded = (commonCount * commonRuns) + (uncommonCount * uncommonRuns) + (rareCount * rareRuns);

            availableRunsLabel.setText(Integer.toString(availableRuns));
            commonResultLabel.setText(formatCoverage(commonCount, commonRuns, availableRuns));
            uncommonResultLabel.setText(formatCoverage(uncommonCount, uncommonRuns, availableRuns));
            rareResultLabel.setText(formatCoverage(rareCount, rareRuns, availableRuns));
            totalEnergyNeededLabel.setText(Integer.toString(totalRunsNeeded * energyPerRun));

            if (availableRuns >= totalRunsNeeded) {
                summaryLabel.setText("You have enough energy to finish the current common, uncommon, and rare food queue.");
            } else {
                summaryLabel.setText("You are short by " + ((totalRunsNeeded - availableRuns) * energyPerRun) + " energy for the full queue.");
            }

            logService.log(
                    "Calculated farm runs: availableRuns=" + availableRuns
                            + ", common=" + commonCount + "x" + commonRuns
                            + ", uncommon=" + uncommonCount + "x" + uncommonRuns
                            + ", rare=" + rareCount + "x" + rareRuns
            );
        } catch (IllegalArgumentException exception) {
            availableRunsLabel.setText("-");
            commonResultLabel.setText("-");
            uncommonResultLabel.setText("-");
            rareResultLabel.setText("-");
            totalEnergyNeededLabel.setText("-");
            summaryLabel.setText(exception.getMessage());
            logService.log("Farm calculator error: " + exception.getMessage());
        }
    }

    private String formatCoverage(int championCount, int runsPerChampion, int availableRuns) {
        if (championCount <= 0 || runsPerChampion <= 0) {
            return "0 / " + championCount + " complete";
        }

        int completeChampions = availableRuns / runsPerChampion;
        if (completeChampions > championCount) {
            completeChampions = championCount;
        }
        return completeChampions + " / " + championCount + " complete";
    }

    private void resetDefaults() {
        energyField.setText("1399");
        energyPerRunField.setText("8");
        commonCountField.setText("12");
        uncommonCountField.setText("8");
        rareCountField.setText("4");
        commonRunsField.setText("6");
        uncommonRunsField.setText("18");
        rareRunsField.setText("54");
        calculateRuns();
    }

    private int parseNonNegative(String value) {
        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed < 0) {
                throw new IllegalArgumentException("Values cannot be negative.");
            }
            return parsed;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Enter whole numbers only in the farm calculator.");
        }
    }

    private int parsePositive(String value) {
        int parsed = parseNonNegative(value);
        if (parsed == 0) {
            throw new IllegalArgumentException("Energy cost per run must be greater than zero.");
        }
        return parsed;
    }
}
