import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChampionUpgradesPanel extends JPanel {

    private final AppLogService logService;

    private final JTextField commonField = createInput("60");
    private final JTextField uncommonField = createInput("24");
    private final JTextField rareField = createInput("8");

    private final JTextField target4Field = createInput("2");
    private final JTextField target5Field = createInput("1");
    private final JTextField target6Field = createInput("0");

    private final JLabel twoStarFoodLabel = createValueLabel();
    private final JLabel threeStarFoodLabel = createValueLabel();
    private final JLabel fourStarFoodLabel = createValueLabel();
    private final JLabel fiveStarFoodLabel = createValueLabel();

    private final JLabel requiredRareFoodLabel = createValueLabel();
    private final JLabel requiredUncommonFoodLabel = createValueLabel();
    private final JLabel requiredCommonFoodLabel = createValueLabel();
    private final JLabel shortageLabel = createSummaryLabel();

    public ChampionUpgradesPanel(AppLogService logService) {
        this.logService = logService;

        setOpaque(false);
        setLayout(new BorderLayout(14, 14));
        add(createPlannerPanel(), BorderLayout.CENTER);
        calculatePlan();
    }

    private JComponent createPlannerPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(14, 14));
        wrapper.setOpaque(false);

        JPanel inventoryPanel = RPGHelperTheme.titledPanel("Champion Upgrades");
        inventoryPanel.setLayout(new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Food Promotion Planner");
        title.setForeground(RPGHelperTheme.TEXT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        inventoryPanel.add(title);
        inventoryPanel.add(Box.createVerticalStrut(10));

        JTextArea description = new JTextArea(
                "Estimate how many food champions you can build from your current common, uncommon, and rare stock, then compare that against the food needed to promote target rare champions to 4-star, 5-star, and 6-star."
        );
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setFocusable(false);
        description.setOpaque(false);
        description.setForeground(RPGHelperTheme.MUTED);
        description.setFont(RPGHelperTheme.LABEL);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);
        inventoryPanel.add(description);
        inventoryPanel.add(Box.createVerticalStrut(18));

        inventoryPanel.add(createSectionHeader("Available food champions"));
        inventoryPanel.add(createInputRow("Common (1-star)", commonField));
        inventoryPanel.add(createInputRow("Uncommon (2-star)", uncommonField));
        inventoryPanel.add(createInputRow("Rare (3-star)", rareField));
        inventoryPanel.add(Box.createVerticalStrut(14));
        inventoryPanel.add(RPGHelperTheme.divider());
        inventoryPanel.add(Box.createVerticalStrut(14));

        inventoryPanel.add(createSectionHeader("Promotion targets"));
        inventoryPanel.add(createInputRow("Rare champions to 4-star", target4Field));
        inventoryPanel.add(createInputRow("Rare champions to 5-star", target5Field));
        inventoryPanel.add(createInputRow("Rare champions to 6-star", target6Field));
        inventoryPanel.add(Box.createVerticalStrut(18));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton calculateButton = new ThemedButton("Calculate Upgrade Plan");
        RPGHelperTheme.stylePrimaryButton(calculateButton);
        calculateButton.addActionListener(event -> calculatePlan());

        JButton resetButton = new ThemedButton("Reset Example");
        RPGHelperTheme.styleSecondaryButton(resetButton);
        resetButton.addActionListener(event -> resetDefaults());

        buttonRow.add(calculateButton);
        buttonRow.add(resetButton);
        inventoryPanel.add(buttonRow);

        JPanel resultsPanel = RPGHelperTheme.titledPanel("Promotion Math");
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.add(createResultLine("Buildable 2-star food", twoStarFoodLabel));
        resultsPanel.add(createResultLine("Buildable 3-star food", threeStarFoodLabel));
        resultsPanel.add(createResultLine("Buildable 4-star food", fourStarFoodLabel));
        resultsPanel.add(createResultLine("Buildable 5-star food", fiveStarFoodLabel));
        resultsPanel.add(Box.createVerticalStrut(10));
        resultsPanel.add(RPGHelperTheme.divider());
        resultsPanel.add(Box.createVerticalStrut(10));
        resultsPanel.add(createResultLine("Rare food needed", requiredRareFoodLabel));
        resultsPanel.add(createResultLine("Uncommon equivalent", requiredUncommonFoodLabel));
        resultsPanel.add(createResultLine("Common equivalent", requiredCommonFoodLabel));
        resultsPanel.add(Box.createVerticalStrut(12));
        resultsPanel.add(shortageLabel);

        wrapper.add(inventoryPanel, BorderLayout.WEST);
        wrapper.add(resultsPanel, BorderLayout.CENTER);
        return wrapper;
    }

    private void calculatePlan() {
        try {
            int common = parseNonNegative(commonField.getText());
            int uncommon = parseNonNegative(uncommonField.getText());
            int rare = parseNonNegative(rareField.getText());

            int target4 = parseNonNegative(target4Field.getText());
            int target5 = parseNonNegative(target5Field.getText());
            int target6 = parseNonNegative(target6Field.getText());

            int buildableTwoStarFood = common / 2;
            int buildableThreeStarFood = rare + ((uncommon + buildableTwoStarFood) / 3);
            int buildableFourStarFood = buildableThreeStarFood / 4;
            int buildableFiveStarFood = buildableFourStarFood / 5;

            int rareFoodNeeded = (target4 * 3) + (target5 * 19) + (target6 * 119);
            int uncommonEquivalent = rareFoodNeeded * 3;
            int commonEquivalent = rareFoodNeeded * 6;

            twoStarFoodLabel.setText(Integer.toString(buildableTwoStarFood));
            threeStarFoodLabel.setText(Integer.toString(buildableThreeStarFood));
            fourStarFoodLabel.setText(Integer.toString(buildableFourStarFood));
            fiveStarFoodLabel.setText(Integer.toString(buildableFiveStarFood));
            requiredRareFoodLabel.setText(Integer.toString(rareFoodNeeded));
            requiredUncommonFoodLabel.setText(Integer.toString(uncommonEquivalent));
            requiredCommonFoodLabel.setText(Integer.toString(commonEquivalent));

            int remainingRareFood = buildableThreeStarFood - rareFoodNeeded;
            if (remainingRareFood >= 0) {
                shortageLabel.setText("You currently have enough food value for the selected promotion targets. Spare 3-star food equivalent: " + remainingRareFood);
            } else {
                shortageLabel.setText("You are missing " + Math.abs(remainingRareFood) + " 3-star food equivalents to complete the selected promotion plan.");
            }

            logService.log(
                    "Calculated champion upgrade plan: common=" + common
                            + ", uncommon=" + uncommon
                            + ", rare=" + rare
                            + ", targets(4/5/6)=" + target4 + "/" + target5 + "/" + target6
                            + ", buildable3star=" + buildableThreeStarFood
                            + ", rareFoodNeeded=" + rareFoodNeeded
            );
        } catch (IllegalArgumentException exception) {
            twoStarFoodLabel.setText("-");
            threeStarFoodLabel.setText("-");
            fourStarFoodLabel.setText("-");
            fiveStarFoodLabel.setText("-");
            requiredRareFoodLabel.setText("-");
            requiredUncommonFoodLabel.setText("-");
            requiredCommonFoodLabel.setText("-");
            shortageLabel.setText(exception.getMessage());
            logService.log("Champion upgrade planner error: " + exception.getMessage());
        }
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

    private void resetDefaults() {
        commonField.setText("60");
        uncommonField.setText("24");
        rareField.setText("8");
        target4Field.setText("2");
        target5Field.setText("1");
        target6Field.setText("0");
        calculatePlan();
    }

    private int parseNonNegative(String value) {
        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed < 0) {
                throw new IllegalArgumentException("Values cannot be negative.");
            }
            return parsed;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Enter whole numbers only in the champion upgrade planner.");
        }
    }
}
