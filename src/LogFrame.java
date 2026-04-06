import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LogFrame extends JFrame {

    private final AppLogService logService;
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Time", "Entry"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public LogFrame(JFrame owner, AppLogService logService) {
        this.logService = logService;

        setTitle("RPG Helper Log");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(760, 420);
        setLocationRelativeTo(owner);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(RPGHelperTheme.BG);
        root.setBorder(new EmptyBorder(14, 14, 14, 14));
        setContentPane(root);

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setBackground(RPGHelperTheme.PANEL);
        table.setForeground(RPGHelperTheme.TEXT);
        table.setSelectionBackground(RPGHelperTheme.brighten(RPGHelperTheme.PANEL, 0.15f));
        table.setGridColor(new Color(65, 81, 110));
        table.setFont(RPGHelperTheme.LABEL);
        table.getTableHeader().setBackground(RPGHelperTheme.PANEL_DARK);
        table.getTableHeader().setForeground(RPGHelperTheme.TEXT);
        table.getTableHeader().setFont(RPGHelperTheme.VALUE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(72, 88, 118)));
        root.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new ThemedButton("Refresh Log");
        RPGHelperTheme.styleSecondaryButton(refreshButton);
        refreshButton.addActionListener(event -> refreshEntries());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        footer.setOpaque(false);
        footer.add(refreshButton);
        root.add(footer, BorderLayout.SOUTH);

        refreshEntries();
    }

    public void refreshEntries() {
        model.setRowCount(0);
        List<LogEntry> entries = logService.getEntries();
        for (LogEntry entry : entries) {
            model.addRow(new Object[]{entry.getTime(), entry.getMessage()});
        }
    }
}
