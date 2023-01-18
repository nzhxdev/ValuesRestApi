package ru.nzhx.ValuesRestApi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nzhx.ValuesRestApi.model.QueryParameter;
import ru.nzhx.ValuesRestApi.model.Value;
import ru.nzhx.ValuesRestApi.repositories.ValuesRepository;
import ru.nzhx.ValuesRestApi.util.ValueNotFoundException;

import java.time.LocalDateTime;
import java.util.*;
@Service
@Transactional(readOnly = true)
public class ValuesService {
    private final ValuesRepository valuesRepository;

    @Autowired
    public ValuesService(ValuesRepository valuesRepository) { this.valuesRepository = valuesRepository; }

    public List<Value> getValues(Map<String, String> parametersFromURLQuery) {
        QueryParameter queryParameter = new QueryParameter(parametersFromURLQuery);
        return valuesRepository.findAll(queryParameter, Sort.by(queryParameter.getSortingType(), queryParameter.getSortField()));
    }

    @Transactional
    public void createValues(int numberOfValues) {
        List<Value> listValuesForSaveToDB = createListValuesForSaveToDB(numberOfValues);
        valuesRepository.saveAll(listValuesForSaveToDB);
    }

    public List<Value> createListValuesForSaveToDB(int numberOfValues) {
        List<Value> values = new ArrayList<>();
        LocalDateTime currentDateTime = LocalDateTime.now();
        for (int i = 1; i <= numberOfValues; i++) {
            values.add(new Value(currentDateTime, "Value " + i));
        }
        return values;
    }

    @Transactional
    public void updateValue(Long id, Value updatedValue) {
        checkExistValue(id);
        updatedValue.setDate(LocalDateTime.now());
        updatedValue.setId(id);
        valuesRepository.save(updatedValue);
    }

    public void checkExistValue(Long id) {
        valuesRepository.findById(id).orElseThrow(ValueNotFoundException::new);
    }

    @Transactional
    public void deleteAll() {
        List<Value> valuesToDelete = valuesRepository.findAll();
        if (!valuesToDelete.isEmpty()) {
            valuesRepository.deleteAll(valuesToDelete);
        }
    }
}
