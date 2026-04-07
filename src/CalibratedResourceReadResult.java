public class CalibratedResourceReadResult {

    private final String energyValue;
    private final String energyRawText;
    private final String silverValue;
    private final String silverRawText;
    private final String gemsValue;
    private final String gemsRawText;
    private final String sourceSummary;

    public CalibratedResourceReadResult(
            String energyValue,
            String energyRawText,
            String silverValue,
            String silverRawText,
            String gemsValue,
            String gemsRawText,
            String sourceSummary
    ) {
        this.energyValue = energyValue;
        this.energyRawText = energyRawText;
        this.silverValue = silverValue;
        this.silverRawText = silverRawText;
        this.gemsValue = gemsValue;
        this.gemsRawText = gemsRawText;
        this.sourceSummary = sourceSummary;
    }

    public String getEnergyValue() {
        return energyValue;
    }

    public String getEnergyRawText() {
        return energyRawText;
    }

    public String getSilverValue() {
        return silverValue;
    }

    public String getSilverRawText() {
        return silverRawText;
    }

    public String getGemsValue() {
        return gemsValue;
    }

    public String getGemsRawText() {
        return gemsRawText;
    }

    public String getSourceSummary() {
        return sourceSummary;
    }
}
