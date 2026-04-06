import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class RaidClientService {

    private static final String[] PROCESS_NAMES = {
            "Raid.exe",
            "Raid Shadow Legends.exe",
            "PlariumPlay.exe"
    };

    public boolean isRaidRunning() {
        return getRunningProcessName() != null;
    }

    public String getRunningProcessName() {
        try {
            Process process = new ProcessBuilder("tasklist", "/fo", "csv", "/nh").start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    for (String processName : PROCESS_NAMES) {
                        if (line.toLowerCase().contains("\"" + processName.toLowerCase() + "\"")) {
                            return processName;
                        }
                    }
                }
            }
        } catch (IOException ignored) {
        }

        return null;
    }

    public String launchRaid(AppSettings settings) {
        String executablePath = settings.getRaidExecutablePath();
        if (executablePath == null || executablePath.trim().isEmpty()) {
            return "Set the Raid executable path first.";
        }

        File executable = new File(executablePath);
        if (!executable.exists()) {
            return "Executable not found: " + executablePath;
        }

        try {
            new ProcessBuilder(executable.getAbsolutePath()).start();
            return "Launch command sent to " + executable.getName() + ".";
        } catch (IOException exception) {
            return "Failed to launch Raid: " + exception.getMessage();
        }
    }

    public String describeStatus(AppSettings settings) {
        String runningProcess = getRunningProcessName();
        if (runningProcess != null) {
            return "Raid client detected: " + runningProcess;
        }

        if (settings.getRaidExecutablePath().isEmpty()) {
            return "Raid client not configured yet.";
        }

        return "Raid client not running.";
    }
}
