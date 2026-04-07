import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class WindowsOcrService {

    public String readText(BufferedImage image) throws IOException {
        File tempFile = File.createTempFile("rpg-helper-ocr-", ".png");
        try {
            ImageIO.write(prepareImage(image), "png", tempFile);
            return runOcrScript(tempFile);
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    private BufferedImage prepareImage(BufferedImage image) {
        int scaledWidth = Math.max(image.getWidth() * 3, image.getWidth());
        int scaledHeight = Math.max(image.getHeight() * 3, image.getHeight());
        BufferedImage prepared = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = prepared.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, scaledWidth, scaledHeight);
        g2.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
        g2.dispose();
        return prepared;
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
