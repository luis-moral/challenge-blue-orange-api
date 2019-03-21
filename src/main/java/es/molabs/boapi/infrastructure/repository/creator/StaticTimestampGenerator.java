package es.molabs.boapi.infrastructure.repository.creator;

public class StaticTimestampGenerator implements TimestampGenerator {

    private static long TIMESTAMP_GENERATOR = 1;

    @Override
    public long nextId() {
        return TIMESTAMP_GENERATOR++;
    }

    public long currentId() {
        return TIMESTAMP_GENERATOR;
    }
}
