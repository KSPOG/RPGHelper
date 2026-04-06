public class MockGameResourceReader implements GameResourceReader {

    @Override
    public GameResourceSnapshot readSnapshot() {
        return GameResourceSnapshot.demo();
    }

    @Override
    public String getReaderName() {
        return "Mock Reader";
    }
}
