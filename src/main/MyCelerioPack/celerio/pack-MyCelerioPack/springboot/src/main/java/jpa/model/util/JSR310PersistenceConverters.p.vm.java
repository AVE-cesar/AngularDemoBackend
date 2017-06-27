$output.java("${configuration.rootPackage}.jpa.model.util", "JSR310PersistenceConverters")##

$output.require("${configuration.rootPackage}.jpa.model.util.JSR310DateConverters.DateToLocalDateTimeConverter")##

$output.require("${configuration.rootPackage}.jpa.model.util.JSR310DateConverters.DateToZonedDateTimeConverter")##
$output.require("${configuration.rootPackage}.jpa.model.util.JSR310DateConverters.LocalDateTimeToDateConverter")##
$output.require("${configuration.rootPackage}.jpa.model.util.JSR310DateConverters.ZonedDateTimeToDateConverter")##

$output.require("java.time.LocalDate")##
$output.require("java.time.LocalDateTime")##
$output.require("java.time.ZonedDateTime")##
$output.require("java.util.Date")##

$output.require("javax.persistence.AttributeConverter")##
$output.require("javax.persistence.Converter")##

public final class JSR310PersistenceConverters {

    private JSR310PersistenceConverters() {}

    @Converter(autoApply = true)
    public static class LocalDateConverter implements AttributeConverter<LocalDate, java.sql.Date> {

        @Override
        public java.sql.Date convertToDatabaseColumn(LocalDate date) {
            return date == null ? null : java.sql.Date.valueOf(date);
        }

        @Override
        public LocalDate convertToEntityAttribute(java.sql.Date date) {
            return date == null ? null : date.toLocalDate();
        }
    }

    @Converter(autoApply = true)
    public static class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Date> {

        @Override
        public Date convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
            return ZonedDateTimeToDateConverter.INSTANCE.convert(zonedDateTime);
        }

        @Override
        public ZonedDateTime convertToEntityAttribute(Date date) {
            return DateToZonedDateTimeConverter.INSTANCE.convert(date);
        }
    }

    @Converter(autoApply = true)
    public static class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Date> {

        @Override
        public Date convertToDatabaseColumn(LocalDateTime localDateTime) {
            return LocalDateTimeToDateConverter.INSTANCE.convert(localDateTime);
        }

        @Override
        public LocalDateTime convertToEntityAttribute(Date date) {
            return DateToLocalDateTimeConverter.INSTANCE.convert(date);
        }
    }
}
