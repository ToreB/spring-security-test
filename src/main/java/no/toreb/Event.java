package no.toreb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Event {

    private final String type;

    private final String description;

    private final String extra;

    @JsonCreator
    public Event(@JsonProperty("type") final String type,
                 @JsonProperty("description") final String description,
                 @JsonProperty("extra") final String extra) {
        this.type = type;
        this.description = description;
        this.extra = extra;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getExtra() {
        return extra;
    }
}
