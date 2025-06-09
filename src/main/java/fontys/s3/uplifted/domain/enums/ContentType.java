package fontys.s3.uplifted.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ContentType {
    FILE,
    QUIZ,
    VIDEO,
    LINK;

    @JsonCreator
    public static ContentType fromString(String value) {
        return value == null ? null : ContentType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
