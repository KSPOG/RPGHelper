import javax.swing.*;
import java.awt.*;

public class ForgePanel extends JPanel {

    public ForgePanel() {
        setOpaque(false);
        setLayout(new BorderLayout());
        add(RPGHelperTheme.createPlaceholderPanel(
                "Forge",
                "Manage crafting, materials, and set recipes.",
                new String[]{
                        "Review forge materials and costs",
                        "Choose target sets to craft",
                        "Add batch crafting automation rules"
                }
        ), BorderLayout.CENTER);
    }
}
