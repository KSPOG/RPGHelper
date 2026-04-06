import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {

    public SettingsPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());
        add(RPGHelperTheme.createPlaceholderPanel(
                "Settings",
                "Control app preferences, client hooks, and defaults.",
                new String[]{
                        "Set client path and window options",
                        "Choose notification and safety behavior",
                        "Store profiles and startup defaults"
                }
        ), BorderLayout.CENTER);
    }
}
