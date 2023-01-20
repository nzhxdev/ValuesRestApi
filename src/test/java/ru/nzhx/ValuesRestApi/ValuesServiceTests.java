package ru.nzhx.ValuesRestApi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.nzhx.ValuesRestApi.model.QueryParameter;
import ru.nzhx.ValuesRestApi.model.Value;
import ru.nzhx.ValuesRestApi.repositories.ValuesRepository;
import ru.nzhx.ValuesRestApi.services.ValuesService;
import ru.nzhx.ValuesRestApi.util.ValueNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class ValuesServiceTests {
    @Mock
    private  ValuesRepository valuesRepository;
    @InjectMocks
    private  ValuesService valuesService;

    private static final int NUMBER_OF_VALUES = 2;
    private static final int WRONG_NUMBER_OF_VALUES = 0;
    private static final long UPDATED_VALUE_ID = 1L;

    @Test
    @DisplayName("Testing the function of getting the list of values from the database")
    public void successfulGetValues() {
        Map<String, String> parametersFromURLQuery = new HashMap<>();
        QueryParameter queryParameter = new QueryParameter(parametersFromURLQuery);
        List<Value> valuesForMock = new ArrayList<>();
        given(valuesRepository.findAll(queryParameter, Sort.by(queryParameter.getSortingType(), queryParameter.getSortField())))
                .willReturn(valuesForMock);

        List<Value> values = valuesService.getValues(parametersFromURLQuery);

        verify(valuesRepository).findAll(queryParameter, Sort.by(queryParameter.getSortingType(), queryParameter.getSortField()));
        assertEquals(valuesForMock, values);
    }

    @Test
    @DisplayName("Testing the function of creating a given number of values in the database")
    public void successfulCreateValues() {
        valuesService.createValues(NUMBER_OF_VALUES);

        verify(valuesRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("Testing a function to create a list of values if the number of values is greater than 0")
    public void successfulCreateListValuesForSaveToDB() {
        List<Value> listValuesForSaveToDB = valuesService.createListValuesForSaveToDB(NUMBER_OF_VALUES);

        assertEquals(NUMBER_OF_VALUES, listValuesForSaveToDB.size());
    }

    @Test
    @DisplayName("Testing a function to create a list of values if the number of values is less than 0")
    public void unsuccessfulCreateValuesIfNumberOfValueLessThenOne() {
        assertThrows(NumberFormatException.class, () -> valuesService.createValues(WRONG_NUMBER_OF_VALUES));
        verify(valuesRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Testing the function of updating the value in the database, if it exists")
    public void successfulUpdateValue() {
        Value updateValue = new Value("Value 2");
        Value valueFromTable = new Value("Value 1");
        valueFromTable.setId(UPDATED_VALUE_ID);
        given(valuesRepository.findById(UPDATED_VALUE_ID)).willReturn(Optional.of(valueFromTable));

        valuesService.updateValue(UPDATED_VALUE_ID, updateValue);

        verify(valuesRepository).save(updateValue);
    }

    @Test
    @DisplayName("Testing the function of updating the value in the database, if it doesn't exist")
    public void unsuccessfulUpdateValueIfValueWithSpecifiedIdDoesntExist() {
        Value updateValue = new Value("Value 2");
        given(valuesRepository.findById(UPDATED_VALUE_ID)).willReturn(Optional.empty());

        assertThrows(ValueNotFoundException.class, () -> valuesService.updateValue(UPDATED_VALUE_ID, updateValue));
        verify(valuesRepository, never()).save(updateValue);
    }

    @Test
    @DisplayName("Testing the function of removing values from the database, if they exist")
    public void successfulDeleteAll() {
        List<Value> valuesToDelete = new ArrayList<>();
        valuesToDelete.add(new Value("Value 1"));
        given(valuesRepository.findAll()).willReturn(valuesToDelete);

        valuesService.deleteAll();

        verify(valuesRepository).deleteAll(valuesToDelete);
    }

    @Test
    @DisplayName("Testing the function of removing values from the database, if they don't exist")
    public void unsuccessfulDeleteAllIfValuesDontExist() {
        List<Value> emptyListOfValues = new ArrayList<>();
        given(valuesRepository.findAll()).willReturn(emptyListOfValues);

        valuesService.deleteAll();

        verify(valuesRepository, never()).deleteAll(emptyListOfValues);
    }
}
