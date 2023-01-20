package ru.nzhx.ValuesRestApi.model.dto;

import ru.nzhx.ValuesRestApi.model.Value;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.function.Function;

public class ValueDTO {

    private long id;

    private LocalDateTime date;

    @NotEmpty(message = "The 'value' field should be filled in.")
    private String value;

    public ValueDTO() {}

    public ValueDTO(String value) {
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
