package ru.nzhx.ValuesRestApi.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.nzhx.ValuesRestApi.model.dto.ValueDTO;
import ru.nzhx.ValuesRestApi.model.Value;
import ru.nzhx.ValuesRestApi.services.ValuesService;
import ru.nzhx.ValuesRestApi.util.ValueNotUpdatedException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
@Validated
public class ValuesController {
    private final ValuesService valuesService;
    private final ModelMapper modelMapper;
    private static final long MINIMUM_NUMBER_OF_VALUES_TO_CREATE = 1;

    @Autowired
    public ValuesController(ValuesService valuesService, ModelMapper modelMapper) {
        this.valuesService = valuesService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/values")
    public ResponseEntity<HttpStatus> createValues(@RequestParam("number") @Min(value = MINIMUM_NUMBER_OF_VALUES_TO_CREATE,
            message = "Field value 'number' should be greater then 0.") Integer numberOfValues) {
        valuesService.createValues(numberOfValues);
        return ResponseEntity.created(URI.create("#")).build();
    }

    @DeleteMapping("/values")
    public ResponseEntity<HttpStatus> deleteValues() {
        valuesService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/values/{id}")
    public ResponseEntity<HttpStatus> updateValue(
            @PathVariable("id") @Min(value = 1, message = "Field value 'id' should be greater then 0.") Long id,
            @RequestBody @Valid ValueDTO updatedValueDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors)
                errorMessage.append(error.getDefaultMessage());
            throw new ValueNotUpdatedException(errorMessage.toString());
        }
        Value updatedValue = convertToValue(updatedValueDTO);
        valuesService.updateValue(id, updatedValue);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/values")
    public ResponseEntity<List<ValueDTO>> getValues(@RequestParam Map<String, String> parametersFromURLQuery)
            throws NumberFormatException, DateTimeParseException {
        List<ValueDTO> values  = valuesService.getValues(parametersFromURLQuery)
                .stream()
                .map(this::convertToValueDTO)
                .collect(Collectors.toList());
        if (values.isEmpty())
            return new ResponseEntity<>(values, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(values, HttpStatus.OK);
    }

    private Value convertToValue(ValueDTO personDTO) {
        return modelMapper.map(personDTO, Value.class);
    }

    private ValueDTO convertToValueDTO(Value person) {
        return modelMapper.map(person, ValueDTO.class);
    }
}
