package ru.nzhx.ValuesRestApi.model;

import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QueryParameter {
    private String valueContains, valueEquals, sortField;
    private LocalDateTime dateEquals, dateGreaterThan, dateLessThan;
    private long idEquals, idGreaterThan, idLessThan, idRangeFrom, idRangeTo;
    private Sort.Direction sortingType;
    private static final String DEFAULT_DIRECTION = "asc.id";
    private static final int INDEX_ID_FROM = 0;
    private static final int INDEX_ID_TO = 1;
    private static final int SORTING_TYPE = 1;
    private static final int SORT_FIELD = 2;

    public QueryParameter(Map<String, String> parameters) {
        this.valueContains = getConcatString(parameters.get("filter.value.ctn"));
        this.valueEquals = parameters.get("filter.value.eq");

        this.dateEquals = validationDate(parameters.get("filter.date.eq"));
        this.dateGreaterThan = validationDate(parameters.get("filter.date.gt"));
        this.dateLessThan = validationDate(parameters.get("filter.date.lt"));

        this.idEquals = validationId(parameters.get("filter.id.eq"));
        this.idGreaterThan = validationId(parameters.get("filter.id.gt"));
        this.idLessThan = validationId(parameters.get("filter.id.lt"));
        this.idRangeFrom = getListOfRange(parameters.get("filter.id.in")).get(INDEX_ID_FROM);
        this.idRangeTo = getListOfRange(parameters.get("filter.id.in")).get(INDEX_ID_TO);

        this.sortingType = getSortingType(getSortParameter(parameters.get("order"), SORTING_TYPE));
        this.sortField = getSortParameter(parameters.get("order"), SORT_FIELD);
    }

    public String getValueContains() {
        return valueContains;
    }

    public String getValueEquals() {
        return valueEquals;
    }

    public String getSortField() {
        return sortField;
    }

    public LocalDateTime getDateEquals() {
        return dateEquals;
    }

    public LocalDateTime getDateGreaterThan() {
        return dateGreaterThan;
    }

    public LocalDateTime getDateLessThan() {
        return dateLessThan;
    }

    public long getIdEquals() {
        return idEquals;
    }

    public long getIdGreaterThan() {
        return idGreaterThan;
    }

    public long getIdLessThan() {
        return idLessThan;
    }

    public long getIdRangeFrom() {
        return idRangeFrom;
    }

    public long getIdRangeTo() {
        return idRangeTo;
    }

    public Sort.Direction getSortingType() {
        return sortingType;
    }

    private LocalDateTime validationDate(String requestDate) {
        return requestDate != null ? LocalDateTime.parse(requestDate.replace(" ", "T")) : null;
    }

    private Long validationId(String requestId) {
        return requestId != null ? checkIdFormat(requestId) : 0L;
    }

    private Long checkIdFormat(String inputId) {
        long id = Long.parseLong(inputId);
        if (id < 1)
            throw new NumberFormatException("Field value 'id' should be greater then 0.");
        return id;
    }

    private String getSortParameter(String sortParametersFromRequest, int sortOption) {
        if (sortParametersFromRequest == null)
            sortParametersFromRequest = DEFAULT_DIRECTION;
        Matcher matcher = Pattern.compile("^(\\w+)\\.(\\w+)$").matcher(sortParametersFromRequest);
        return matcher.find() ? matcher.group(sortOption) : "";
    }

    private Sort.Direction getSortingType(String sort) {
        return sort.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    }

    private String getConcatString(String value) {
        return value != null ? "%"+ value +"%" : null;
    }

    private List<Long> getListOfRange(String range) {
        if (range != null){
            return Arrays.stream(range.split("-"))
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .collect(Collectors.toList());
        } else {
            return Arrays.stream(new long[]{0L, 0L})
                    .boxed()
                    .collect(Collectors.toList());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryParameter parameter = (QueryParameter) o;
        return idEquals == parameter.idEquals && idGreaterThan == parameter.idGreaterThan && idLessThan == parameter.idLessThan && idRangeFrom == parameter.idRangeFrom && idRangeTo == parameter.idRangeTo && Objects.equals(valueContains, parameter.valueContains) && Objects.equals(valueEquals, parameter.valueEquals) && Objects.equals(sortField, parameter.sortField) && Objects.equals(dateEquals, parameter.dateEquals) && Objects.equals(dateGreaterThan, parameter.dateGreaterThan) && Objects.equals(dateLessThan, parameter.dateLessThan) && sortingType == parameter.sortingType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueContains, valueEquals, sortField, dateEquals, dateGreaterThan, dateLessThan, idEquals, idGreaterThan, idLessThan, idRangeFrom, idRangeTo, sortingType);
    }
}
