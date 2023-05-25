package com.specure.common.specification;

import com.specure.common.model.jpa.Provider;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@EqualsAndHashCode
@ToString
public class ProviderSpecification implements Specification<Provider> {

    private Boolean ispActive;
    private Boolean mnoActive;
    private List<String> countries;
    private String name;

    @Override
    public Predicate toPredicate(Root<Provider> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (Strings.isNotBlank(name)) {
            if (name.startsWith("-")) {
                predicates.add(criteriaBuilder.notLike(root.get("name"), "%" + name.substring(1) + "%"));
            } else if (name.startsWith("!")) {
                predicates.add(criteriaBuilder.equal(root.get("name"), name.substring(1)));
            } else {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
        }
        if (Objects.nonNull(ispActive)) {
            predicates.add(criteriaBuilder.equal(root.get("ispActive"), ispActive));
        }
        if (Objects.nonNull(mnoActive)) {
            predicates.add(criteriaBuilder.equal(root.get("mnoActive"), mnoActive));
        }
        if (Objects.nonNull(countries) && !countries.isEmpty()) {
            predicates.add(root.get("country").in(countries));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
