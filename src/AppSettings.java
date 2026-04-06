public class AppSettings {

    private String raidExecutablePath = "";
    private boolean autoLaunchRaidOnStartup;

    public String getRaidExecutablePath() {
        return raidExecutablePath;
    }

    public void setRaidExecutablePath(String raidExecutablePath) {
        this.raidExecutablePath = raidExecutablePath == null ? "" : raidExecutablePath.trim();
    }

    public boolean isAutoLaunchRaidOnStartup() {
        return autoLaunchRaidOnStartup;
    }

    public void setAutoLaunchRaidOnStartup(boolean autoLaunchRaidOnStartup) {
        this.autoLaunchRaidOnStartup = autoLaunchRaidOnStartup;
    }
}
