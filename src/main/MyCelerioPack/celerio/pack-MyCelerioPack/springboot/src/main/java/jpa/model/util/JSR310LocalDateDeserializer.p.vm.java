$output.java("${configuration.rootPackage}.jpa.model.util", "JSR310LocalDateDeserializer")##

$output.require("java.io.IOException")##
$output.require("java.time.LocalDate")##
$output.require("java.time.format.DateTimeFormatter")##
$output.require("java.time.format.DateTimeFormatterBuilder")##

$output.require("com.fasterxml.jackson.core.JsonParser")##
$output.require("com.fasterxml.jackson.core.JsonToken")##
$output.require("com.fasterxml.jackson.databind.DeserializationContext")##
$output.require("com.fasterxml.jackson.databind.JsonDeserializer")##

/**
 * Custom Jackson deserializer for transforming a JSON object (using the ISO 8601 date formatwith optional time)
 * to a JSR310 LocalDate object.
 */
public class JSR310LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    public static final JSR310LocalDateDeserializer INSTANCE = new JSR310LocalDateDeserializer();

    private JSR310LocalDateDeserializer() {}

    private static final DateTimeFormatter ISO_DATE_OPTIONAL_TIME;

    static {
        ISO_DATE_OPTIONAL_TIME = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .optionalStart()
            .appendLiteral('T')
            .append(DateTimeFormatter.ISO_OFFSET_TIME)
            .toFormatter();
    }

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    	if (JsonToken.START_ARRAY.equals(parser.getCurrentToken())) {
    		if (parser.nextToken() == JsonToken.END_ARRAY) {
                return null;
            }
            int year = parser.getIntValue();

            parser.nextToken();
            int month = parser.getIntValue();

            parser.nextToken();
            int day = parser.getIntValue();

            if (parser.nextToken() != JsonToken.END_ARRAY) {
                throw context.wrongTokenException(parser, JsonToken.END_ARRAY, "Expected array to end.");
            }
            return LocalDate.of(year, month, day);
		} else if (JsonToken.VALUE_STRING.equals(parser.getCurrentToken())) {
			String string = parser.getText().trim();
            if (string.length() == 0) {
                return null;
            }
            return LocalDate.parse(string, ISO_DATE_OPTIONAL_TIME);
		} else {
			throw context.wrongTokenException(parser, JsonToken.START_ARRAY, "Expected array or string.");
		}
    }
}
