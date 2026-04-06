import javax.swing.*;
import java.awt.*;

public class AutoFarmPanel extends JPanel {

    public AutoFarmPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());
        add(RPGHelperTheme.createPlaceholderPanel(
                "Auto-Farm",
                "Build and launch repeatable farming rotations.",
                new String[]{
                        "Select dungeon and campaign presets",
                        "Configure refill and stop rules",
                        "Save reusable farming profiles"
                }
        ), BorderLayout.CENTER);
    }
}
