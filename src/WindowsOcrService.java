import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class WindowsOcrService {

    public String readText(BufferedImage image) throws IOException {
        File tempFile = File.createTempFile("rpg-helper-ocr-", ".png");
        try {
            ImageIO.write(image, "png", tempFile);
            return runOcrScript(tempFile);
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    private String runOcrScript(File imageFile) throws IOException {
        File scriptFile = new File("scripts", "ocr-image.ps1");
        if (!scriptFile.exists()) {
            throw new IOException("OCR script not found: " + scriptFile.getAbsolutePath());
        }

        Process process = new ProcessBuilder(
                "powershell",
                "-NoProfile",
                "-ExecutionPolicy",
                "Bypass",
                "-File",
                scriptFile.getAbsolutePath(),
                imageFile.getAbsolutePath()
        ).redirectErrorStream(true).start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (output.length() > 0) {
                    output.append(' ');
                }
                output.append(line.trim());
            }
        }

        try {
            process.waitFor();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IOException("OCR process was interrupted.", exception);
        }

        return output.toString().trim();
    }
}
