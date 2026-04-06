import javax.swing.*;
import java.awt.*;

public class ChampionUpgradesPanel extends JPanel {

    public ChampionUpgradesPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());
        add(RPGHelperTheme.createPlaceholderPanel(
                "Champion Upgrades",
                "Track leveling, gear goals, and promotion tasks.",
                new String[]{
                        "Pick upgrade targets by champion",
                        "Queue food, potions, and ascension mats",
                        "Monitor stat breakpoints and priorities"
                }
        ), BorderLayout.CENTER);
    }
}
