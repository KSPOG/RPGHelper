public class CalibratedResourceReadResult {

    private final String energyValue;
    private final String energyRawText;
    private final String silverValue;
    private final String silverRawText;
    private final String gemsValue;
    private final String gemsRawText;
    private final String blueShardsValue;
    private final String blueShardsRawText;
    private final String voidShardsValue;
    private final String voidShardsRawText;
    private final String sacredShardsValue;
    private final String sacredShardsRawText;
    private final String sourceSummary;

    public CalibratedResourceReadResult(
            String energyValue,
            String energyRawText,
            String silverValue,
            String silverRawText,
            String gemsValue,
            String gemsRawText,
            String blueShardsValue,
            String blueShardsRawText,
            String voidShardsValue,
            String voidShardsRawText,
            String sacredShardsValue,
            String sacredShardsRawText,
            String sourceSummary
    ) {
        this.energyValue = energyValue;
        this.energyRawText = energyRawText;
        this.silverValue = silverValue;
        this.silverRawText = silverRawText;
        this.gemsValue = gemsValue;
        this.gemsRawText = gemsRawText;
        this.blueShardsValue = blueShardsValue;
        this.blueShardsRawText = blueShardsRawText;
        this.voidShardsValue = voidShardsValue;
        this.voidShardsRawText = voidShardsRawText;
        this.sacredShardsValue = sacredShardsValue;
        this.sacredShardsRawText = sacredShardsRawText;
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

    public String getBlueShardsValue() {
        return blueShardsValue;
    }

    public String getBlueShardsRawText() {
        return blueShardsRawText;
    }

    public String getVoidShardsValue() {
        return voidShardsValue;
    }

    public String getVoidShardsRawText() {
        return voidShardsRawText;
    }

    public String getSacredShardsValue() {
        return sacredShardsValue;
    }

    public String getSacredShardsRawText() {
        return sacredShardsRawText;
    }

    public String getSourceSummary() {
        return sourceSummary;
    }
}
