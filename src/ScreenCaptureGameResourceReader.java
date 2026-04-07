import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScreenCaptureGameResourceReader implements GameResourceReader {

    private final GameResourceReader fallbackReader;
    private final AppSettings settings;
    private final RaidClientService raidClientService;
    private final WindowsOcrService windowsOcrService = new WindowsOcrService();
    private GameResourceSnapshot lastGoodSnapshot;

    public ScreenCaptureGameResourceReader(
            GameResourceReader fallbackReader,
            AppSettings settings,
            RaidClientService raidClientService
    ) {
        this.fallbackReader = fallbackReader;
        this.settings = settings;
        this.raidClientService = raidClientService;
    }

    @Override
    public GameResourceSnapshot readSnapshot() {
        GameResourceSnapshot fallback = fallbackReader.readSnapshot();
        if (!raidClientService.isRaidRunning()) {
            String source;
            if (settings.getRaidExecutablePath().isEmpty()) {
                source = "Raid executable not configured yet.";
            } else {
                source = "Raid configured, but the client is not running.";
            }

            return new GameResourceSnapshot(
                    fallback.getEnergy(),
                    fallback.getBlueShards(),
                    fallback.getVoidShards(),
                    fallback.getSacredShards(),
                    fallback.getSilver(),
                    fallback.getGems(),
                    source
            );
        }

        try {
            ParsedHudValues parsedHudValues = readCalibratedValues();
            if (!parsedHudValues.hasAnyValue()) {
                BufferedImage capture = captureHudStrip();
                String ocrText = windowsOcrService.readText(capture);
                parsedHudValues = parseHudValues(ocrText);
                parsedHudValues.sourceText = buildSourceText(parsedHudValues, ocrText);
            }

            GameResourceSnapshot snapshot = new GameResourceSnapshot(
                    chooseValue(parsedHudValues.energy, fallback.getEnergy(), lastGoodSnapshot == null ? null : lastGoodSnapshot.getEnergy()),
                    fallback.getBlueShards(),
                    fallback.getVoidShards(),
                    fallback.getSacredShards(),
                    chooseValue(parsedHudValues.silver, fallback.getSilver(), lastGoodSnapshot == null ? null : lastGoodSnapshot.getSilver()),
                    chooseValue(parsedHudValues.gems, fallback.getGems(), lastGoodSnapshot == null ? null : lastGoodSnapshot.getGems()),
                    parsedHudValues.sourceText
            );

            lastGoodSnapshot = snapshot;
            return snapshot;
        } catch (Exception exception) {
            return new GameResourceSnapshot(
                    lastGoodSnapshot != null ? lastGoodSnapshot.getEnergy() : fallback.getEnergy(),
                    fallback.getBlueShards(),
                    fallback.getVoidShards(),
                    fallback.getSacredShards(),
                    lastGoodSnapshot != null ? lastGoodSnapshot.getSilver() : fallback.getSilver(),
                    lastGoodSnapshot != null ? lastGoodSnapshot.getGems() : fallback.getGems(),
                    "OCR failed: " + exception.getMessage()
            );
        }
    }

    @Override
    public String getReaderName() {
        return "Screen Capture Reader";
    }

    private BufferedImage captureHudStrip() throws AWTException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (screenSize.width * 0.42);
        int y = 0;
        int width = screenSize.width - x;
        int height = Math.min(110, screenSize.height);
        Rectangle captureArea = new Rectangle(x, y, width, height);
        return new Robot().createScreenCapture(captureArea);
    }

    private ParsedHudValues readCalibratedValues() throws AWTException, IOException {
        ParsedHudValues parsed = new ParsedHudValues();
        Robot robot = new Robot();
        List<String> sourceParts = new ArrayList<>();

        if (settings.getEnergyRegion() != null) {
            String energyText = windowsOcrService.readText(robot.createScreenCapture(settings.getEnergyRegion().toRectangle()));
            parsed.energy = extractEnergy(energyText);
            sourceParts.add("energy=" + valueOrUnknown(parsed.energy));
        }

        if (settings.getSilverRegion() != null) {
            String silverText = windowsOcrService.readText(robot.createScreenCapture(settings.getSilverRegion().toRectangle()));
            parsed.silver = extractSilver(silverText);
            sourceParts.add("silver=" + valueOrUnknown(parsed.silver));
        }

        if (settings.getGemsRegion() != null) {
            String gemsText = windowsOcrService.readText(robot.createScreenCapture(settings.getGemsRegion().toRectangle()));
            parsed.gems = extractGems(gemsText);
            sourceParts.add("gems=" + valueOrUnknown(parsed.gems));
        }

        parsed.sourceText = sourceParts.isEmpty()
                ? "No calibrated regions yet. Using fallback HUD OCR."
                : "Calibrated OCR: " + joinSourceParts(sourceParts);
        return parsed;
    }

    private ParsedHudValues parseHudValues(String ocrText) {
        ParsedHudValues parsed = new ParsedHudValues();
        if (ocrText == null || ocrText.trim().isEmpty()) {
            return parsed;
        }

        List<String> slashPairs = new ArrayList<>();
        Matcher slashMatcher = Pattern.compile("(\\d[\\d,\\.]{0,8}\\s*/\\s*\\d[\\d,\\.]{0,5})").matcher(ocrText);
        while (slashMatcher.find()) {
            slashPairs.add(normalizeSlashPair(slashMatcher.group(1)));
        }

        int bestEnergyScore = -1;
        for (String pair : slashPairs) {
            String[] parts = pair.split("/");
            if (parts.length != 2) {
                continue;
            }

            int numerator = safeParseNumber(parts[0]);
            int denominator = safeParseNumber(parts[1]);
            if (denominator <= 0) {
                continue;
            }

            if (numerator > bestEnergyScore) {
                bestEnergyScore = numerator;
                parsed.energy = pair;
            }
        }

        Matcher amountMatcher = Pattern.compile("(\\d[\\d,\\.]{0,8}(?:\\s?[MK])?)").matcher(ocrText);
        List<String> amounts = new ArrayList<>();
        while (amountMatcher.find()) {
            String value = amountMatcher.group(1).replace(" ", "");
            if (value.contains("/")) {
                continue;
            }
            amounts.add(value);
        }

        parsed.silver = findBestSilver(amounts);
        parsed.gems = findBestGems(amounts, parsed.energy);
        return parsed;
    }

    private String findBestSilver(List<String> amounts) {
        for (String amount : amounts) {
            String upper = amount.toUpperCase();
            if (upper.endsWith("M") || upper.endsWith("K")) {
                return upper;
            }
        }
        return null;
    }

    private String findBestGems(List<String> amounts, String energy) {
        int energyDenominator = -1;
        if (energy != null && energy.contains("/")) {
            energyDenominator = safeParseNumber(energy.split("/")[1]);
        }

        for (String amount : amounts) {
            String upper = amount.toUpperCase();
            if (upper.endsWith("M") || upper.endsWith("K")) {
                continue;
            }

            int value = safeParseNumber(upper);
            if (value <= 0) {
                continue;
            }

            if (energyDenominator > 0 && value == energyDenominator) {
                continue;
            }

            if (value < 5000) {
                return Integer.toString(value);
            }
        }

        return null;
    }

    private String extractEnergy(String text) {
        Matcher matcher = Pattern.compile("(\\d[\\d,\\.]{0,8}\\s*/\\s*\\d[\\d,\\.]{0,5})").matcher(text == null ? "" : text);
        return matcher.find() ? normalizeSlashPair(matcher.group(1)) : null;
    }

    private String extractSilver(String text) {
        Matcher matcher = Pattern.compile("(\\d[\\d,\\.]{0,8}(?:\\s?[MK])?)", Pattern.CASE_INSENSITIVE).matcher(text == null ? "" : text);
        while (matcher.find()) {
            String candidate = matcher.group(1).replace(" ", "").toUpperCase();
            if (candidate.endsWith("M") || candidate.endsWith("K") || safeParseNumber(candidate) >= 100000) {
                return candidate;
            }
        }
        return null;
    }

    private String extractGems(String text) {
        Matcher matcher = Pattern.compile("(\\d{1,4})").matcher(text == null ? "" : text);
        while (matcher.find()) {
            int value = safeParseNumber(matcher.group(1));
            if (value > 0 && value < 10000) {
                return Integer.toString(value);
            }
        }
        return null;
    }

    private String chooseValue(String primary, String fallback, String cached) {
        if (primary != null && !primary.trim().isEmpty()) {
            return primary;
        }
        if (cached != null && !cached.trim().isEmpty()) {
            return cached;
        }
        return fallback;
    }

    private String joinSourceParts(List<String> parts) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parts.size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(parts.get(i));
        }
        return builder.toString();
    }

    private String buildSourceText(ParsedHudValues parsedHudValues, String ocrText) {
        return "HUD OCR: energy="
                + valueOrUnknown(parsedHudValues.energy)
                + ", silver="
                + valueOrUnknown(parsedHudValues.silver)
                + ", gems="
                + valueOrUnknown(parsedHudValues.gems)
                + " | OCR text: "
                + shorten(ocrText, 90);
    }

    private String normalizeSlashPair(String rawPair) {
        return rawPair.replaceAll("\\s+", "");
    }

    private int safeParseNumber(String value) {
        String digitsOnly = value.replaceAll("[^0-9]", "");
        if (digitsOnly.isEmpty()) {
            return -1;
        }
        try {
            return Integer.parseInt(digitsOnly);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private String valueOrUnknown(String value) {
        return value == null ? "?" : value;
    }

    private String shorten(String text, int maxLength) {
        if (text == null) {
            return "";
        }

        String normalized = text.replaceAll("\\s+", " ").trim();
        if (normalized.length() <= maxLength) {
            return normalized;
        }

        return normalized.substring(0, maxLength) + "...";
    }

    private static class ParsedHudValues {
        private String energy;
        private String silver;
        private String gems;
        private String sourceText;

        private boolean hasAnyValue() {
            return energy != null || silver != null || gems != null;
        }
    }
}
