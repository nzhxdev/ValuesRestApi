package ru.nzhx.ValuesRestApi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import ru.nzhx.ValuesRestApi.controllers.ValuesController;
import ru.nzhx.ValuesRestApi.model.Value;
import ru.nzhx.ValuesRestApi.model.dto.ValueDTO;
import ru.nzhx.ValuesRestApi.services.ValuesService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ValuesControllerTests {
    @Mock
    private ValuesService valuesService;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private ValuesController valuesController;

    @Test
    @DisplayName("Testing a function call to create values and return response")
    public void successfulCreateValues() {
        int numberOfValueToCreate = 10;

        ResponseEntity<HttpStatus> response = valuesController.createValues(numberOfValueToCreate);

        verify(valuesService).createValues(numberOfValueToCreate);
        assertEquals(ResponseEntity.ok(HttpStatus.CREATED), response);
    }

    @Test
    @DisplayName("Testing a function call to delete values and the return response")
    public void successfulDeleteValues() {
        ResponseEntity<HttpStatus> response = valuesController.deleteValues();

        verify(valuesService).deleteAll();
        assertEquals(ResponseEntity.ok(HttpStatus.NO_CONTENT), response);
    }
    
    @Test
    @DisplayName("Testing a function call to update the value and the return response")
    public void successfulUpdateValue() {
        long updatedValueId = 1L;
        ValueDTO updatedValueDTO = new ValueDTO("Updated value");
        Value updatedValue = new Value(updatedValueDTO.getValue());
        given(modelMapper.map(updatedValueDTO, Value.class)).willReturn(updatedValue);

        ResponseEntity<HttpStatus> response = valuesController.updateValue(updatedValueId, updatedValueDTO, bindingResult);

        verify(valuesService).updateValue(updatedValueId,updatedValue);
        assertEquals(ResponseEntity.ok(HttpStatus.OK), response);
    }

    @Test
    @DisplayName("Testing the function call to get the values if they exist and the return response")
    public void successfulGetValues() {
        Map<String, String> parametersFromURLQuery = new HashMap<>();
        Value value = new Value("Value1");
        ValueDTO valueDTO = new ValueDTO(value.getValue());
        List<Value> valuesFromTable = new ArrayList<>();
        valuesFromTable.add(value);
        given(valuesService.getValues(parametersFromURLQuery)).willReturn(valuesFromTable);
        given(modelMapper.map(value, ValueDTO.class)).willReturn(valueDTO);

        ResponseEntity<List<ValueDTO>> response = valuesController.getValues(new HashMap<>());

        verify(valuesService).getValues(parametersFromURLQuery);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Testing a function call to get values if they don't exist and the return response")
    public void unsuccessfulGetValuesIfValuesDontExist() {
        Map<String, String> parametersFromURLQuery = new HashMap<>();
        List<Value> valuesFromTable = new ArrayList<>();
        given(valuesService.getValues(parametersFromURLQuery)).willReturn(valuesFromTable);

        ResponseEntity<List<ValueDTO>> response = valuesController.getValues(new HashMap<>());

        verify(valuesService).getValues(parametersFromURLQuery);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
