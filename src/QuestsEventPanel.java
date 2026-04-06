import javax.swing.*;
import java.awt.*;

public class QuestsEventPanel extends JPanel {

    public QuestsEventPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());
        add(RPGHelperTheme.createPlaceholderPanel(
                "Quests / Event",
                "Keep event tasks and daily quest runs in one place.",
                new String[]{
                        "Track limited-time objectives",
                        "Mark daily and weekly quest completion",
                        "Prioritize rewards by event value"
                }
        ), BorderLayout.CENTER);
    }
}
