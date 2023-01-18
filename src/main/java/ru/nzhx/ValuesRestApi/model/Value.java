package ru.nzhx.ValuesRestApi.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "values")
public class Value {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "values_generator")
    @SequenceGenerator(name = "values_generator", sequenceName = "values_seq", allocationSize = 1)
    private long id;

    private LocalDateTime date;

    private String value;

    public Value() {}

    public Value(String value) {
        this.value = value;
    }

    public Value(LocalDateTime date, String value) {
        this.date = date;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Value value1 = (Value) o;
        return Objects.equals(value, value1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
