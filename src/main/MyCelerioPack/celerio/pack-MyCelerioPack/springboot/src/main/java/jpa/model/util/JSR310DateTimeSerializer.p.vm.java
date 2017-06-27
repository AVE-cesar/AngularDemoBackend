$output.java("${configuration.rootPackage}.jpa.model.util", "JSR310DateTimeSerializer")##

$output.require("java.io.IOException")##
$output.require("java.time.ZoneId")##
$output.require("java.time.format.DateTimeFormatter")##
$output.require("java.time.temporal.TemporalAccessor")##

$output.require("com.fasterxml.jackson.core.JsonGenerator")##
$output.require("com.fasterxml.jackson.databind.JsonSerializer")##
$output.require("com.fasterxml.jackson.databind.SerializerProvider")##

public final class JSR310DateTimeSerializer extends JsonSerializer<TemporalAccessor> {

    private static final DateTimeFormatter ISOFormatter =
        DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    public static final JSR310DateTimeSerializer INSTANCE = new JSR310DateTimeSerializer();

    private JSR310DateTimeSerializer() {}

    @Override
    public void serialize(TemporalAccessor value, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
        generator.writeString(ISOFormatter.format(value));
    }
}
