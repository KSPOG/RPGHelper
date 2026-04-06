import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RaidClientService {

    private static final String[] PROCESS_NAMES = {
            "Raid.exe",
            "Raid Shadow Legends.exe",
            "PlariumPlay.exe"
    };
    private static final String[] EXECUTABLE_NAMES = {
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
        File executable = resolveLaunchTarget(settings.getRaidExecutablePath());
        if (executable == null) {
            String executablePath = settings.getRaidExecutablePath();
            if (executablePath == null || executablePath.trim().isEmpty()) {
                return "Set the Raid executable path first.";
            }

            File configuredPath = new File(executablePath);
            if (!configuredPath.exists()) {
                return "Path not found: " + executablePath;
            }

            if (configuredPath.isDirectory()) {
                return "No supported executable was found in that folder. Point to the game .exe or the Plarium Play install folder.";
            }

            return "That path is not a supported Raid executable: " + executablePath;
        }

        try {
            new ProcessBuilder(executable.getAbsolutePath()).start();
            return "Launch command sent to " + executable.getName() + ".";
        } catch (IOException exception) {
            return "Failed to launch Raid: " + exception.getMessage();
        }
    }

    public String describeConfiguredTarget(AppSettings settings) {
        File executable = resolveLaunchTarget(settings.getRaidExecutablePath());
        if (executable == null) {
            if (settings.getRaidExecutablePath().isEmpty()) {
                return "No launch target configured.";
            }
            return "Configured path is not launchable yet.";
        }

        return "Launch target: " + executable.getAbsolutePath();
    }

    private File resolveLaunchTarget(String configuredPath) {
        if (configuredPath == null || configuredPath.trim().isEmpty()) {
            return null;
        }

        File path = new File(configuredPath.trim());
        if (!path.exists()) {
            return null;
        }

        if (path.isFile()) {
            return isSupportedExecutable(path) ? path : null;
        }

        List<File> candidates = new ArrayList<>();
        for (String executableName : EXECUTABLE_NAMES) {
            candidates.add(new File(path, executableName));
            candidates.add(new File(new File(path, "build"), executableName));
            candidates.add(new File(new File(path, "Raid"), executableName));
            candidates.add(new File(new File(path, "PlariumPlay"), executableName));
        }

        File[] children = path.listFiles();
        if (children != null) {
            for (File child : children) {
                if (child.isFile() && isSupportedExecutable(child)) {
                    candidates.add(child);
                } else if (child.isDirectory()) {
                    for (String executableName : EXECUTABLE_NAMES) {
                        candidates.add(new File(child, executableName));
                    }
                }
            }
        }

        for (File candidate : candidates) {
            if (candidate.exists() && candidate.isFile() && isSupportedExecutable(candidate)) {
                return candidate;
            }
        }

        return null;
    }

    private boolean isSupportedExecutable(File file) {
        String name = file.getName();
        for (String executableName : EXECUTABLE_NAMES) {
            if (executableName.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public String describeStatus(AppSettings settings) {
        String runningProcess = getRunningProcessName();
        if (runningProcess != null) {
            return "Raid client detected: " + runningProcess;
        }

        if (settings.getRaidExecutablePath().isEmpty()) {
            return "Set the Raid executable path first.";
        }

        File executable = resolveLaunchTarget(settings.getRaidExecutablePath());
        if (executable == null) {
            return "Raid path is set, but no supported executable was found.";
        }

        return "Raid client not running. " + executable.getName() + " is ready to launch.";
    }
}
