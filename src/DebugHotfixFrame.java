import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class DebugHotfixFrame extends JFrame {

    private final RPGHelperPrototype app;
    private final MutableMockGameResourceReader debugReader;
    private final AppLogService logService;
    private final Map<String, JTextField> resourceFields = new LinkedHashMap<>();

    public DebugHotfixFrame(RPGHelperPrototype app, MutableMockGameResourceReader debugReader, AppLogService logService) {
        this.app = app;
        this.debugReader = debugReader;
        this.logService = logService;

        setTitle("RPG Helper Debug");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(420, 520);
        setLocationRelativeTo(app);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(RPGHelperTheme.BG);
        root.setBorder(new EmptyBorder(14, 14, 14, 14));
        setContentPane(root);

        root.add(createNavigationPanel(), BorderLayout.NORTH);
        root.add(createResourceEditor(), BorderLayout.CENTER);
        root.add(createActionsPanel(), BorderLayout.SOUTH);
    }

    private JComponent createNavigationPanel() {
        JPanel panel = RPGHelperTheme.titledPanel("Debug Navigation");
        panel.setLayout(new GridLayout(2, 3, 8, 8));

        String[] screens = {"Home", "Auto-Farm", "Champion Upgrades", "Forge", "Quests / Event", "Settings"};
        for (String screen : screens) {
            JButton button = new ThemedButton(screen);
            RPGHelperTheme.styleSecondaryButton(button);
            button.addActionListener(event -> app.showScreen(screen));
            panel.add(button);
        }

        return panel;
    }

    private JComponent createResourceEditor() {
        JPanel panel = RPGHelperTheme.titledPanel("Hotfix Resources");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        addResourceField(panel, "Energy", debugReader.readSnapshot().getEnergy());
        addResourceField(panel, "Ancient Shards", debugReader.readSnapshot().getBlueShards());
        addResourceField(panel, "Void Shards", debugReader.readSnapshot().getVoidShards());
        addResourceField(panel, "Sacred Shards", debugReader.readSnapshot().getSacredShards());
        addResourceField(panel, "Silver", debugReader.readSnapshot().getSilver());
        addResourceField(panel, "Gems", debugReader.readSnapshot().getGems());

        return panel;
    }

    private void addResourceField(JPanel panel, String name, String value) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(6, 0, 6, 0));

        JLabel label = new JLabel(name + ":");
        label.setForeground(RPGHelperTheme.TEXT);
        label.setFont(RPGHelperTheme.VALUE);

        JTextField field = new JTextField(value);
        field.setBackground(RPGHelperTheme.PANEL);
        field.setForeground(RPGHelperTheme.TEXT);
        field.setCaretColor(RPGHelperTheme.TEXT);
        field.setBorder(new EmptyBorder(8, 10, 8, 10));

        resourceFields.put(name, field);
        row.add(label, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        panel.add(row);
    }

    private JComponent createActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panel.setOpaque(false);

        JButton resetButton = new ThemedButton("Reset Demo");
        RPGHelperTheme.styleSecondaryButton(resetButton);
        resetButton.addActionListener(event -> resetFields());

        JButton applyButton = new ThemedButton("Apply Hotfix");
        RPGHelperTheme.stylePrimaryButton(applyButton);
        applyButton.addActionListener(event -> applyHotfix());

        panel.add(resetButton);
        panel.add(applyButton);
        return panel;
    }

    private void resetFields() {
        GameResourceSnapshot demo = GameResourceSnapshot.demo();
        resourceFields.get("Energy").setText(demo.getEnergy());
        resourceFields.get("Ancient Shards").setText(demo.getBlueShards());
        resourceFields.get("Void Shards").setText(demo.getVoidShards());
        resourceFields.get("Sacred Shards").setText(demo.getSacredShards());
        resourceFields.get("Silver").setText(demo.getSilver());
        resourceFields.get("Gems").setText(demo.getGems());
    }

    private void applyHotfix() {
        debugReader.updateSnapshot(
                resourceFields.get("Energy").getText().trim(),
                resourceFields.get("Ancient Shards").getText().trim(),
                resourceFields.get("Void Shards").getText().trim(),
                resourceFields.get("Sacred Shards").getText().trim(),
                resourceFields.get("Silver").getText().trim(),
                resourceFields.get("Gems").getText().trim(),
                "Debug hotfix override"
        );
        logService.log("Applied debug hotfix resource override.");
        app.refreshDebugData();
    }
}
