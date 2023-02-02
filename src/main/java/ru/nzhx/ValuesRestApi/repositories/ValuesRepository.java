package ru.nzhx.ValuesRestApi.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.nzhx.ValuesRestApi.util.QueryParameter;
import ru.nzhx.ValuesRestApi.model.Value;

import java.util.List;

public interface ValuesRepository extends JpaRepository<Value, Long> {
    @Query("select v from Value v where (:#{#qp.getValueContains} is null or v.value like :#{#qp.getValueContains}) and" +
            "(:#{#qp.getValueEquals} is null or v.value = :#{#qp.getValueEquals}) and" +
            "(cast(cast(:#{#qp.getDateEquals} as string) as date) is null or v.date = :#{#qp.getDateEquals}) and" +
            "(cast(cast(:#{#qp.getDateGreaterThan} as string) as date) is null or v.date > :#{#qp.getDateGreaterThan}) and" +
            "(cast(cast(:#{#qp.getDateLessThan} as string) as date) is null or v.date < :#{#qp.getDateLessThan}) and" +
            "(cast(:#{#qp.getIdEquals} as long)  = 0 or v.id = :#{#qp.getIdEquals}) and" +
            "(cast(:#{#qp.getIdRangeFrom} as long) = 0 or (cast(:#{#qp.getIdRangeTo} as long) = 0) or v.id between :#{#qp.getIdRangeFrom} and :#{#qp.getIdRangeTo}) and" +
            "(cast(:#{#qp.getIdGreaterThan} as long) = 0 or v.id > :#{#qp.getIdGreaterThan}) and" +
            "(cast(:#{#qp.getIdLessThan} as long) = 0 or v.id < :#{#qp.getIdLessThan})")
    List<Value> findAll(QueryParameter qp, Sort sort);
}
