package com.specure.common.specification;

import com.specure.common.model.jpa.Provider;
import com.specure.common.model.jpa.RawProvider;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@EqualsAndHashCode
@ToString
public class RawProviderSpecification implements Specification<RawProvider> {

    private Boolean isMappedToActiveProvider;
    private List<String> countries;
    private String rawName;
    private String providerName;


    @Override
    public Predicate toPredicate(Root<RawProvider> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (Strings.isNotBlank(rawName)) {
            if (rawName.startsWith("-")) {
                predicates.add(criteriaBuilder.notLike(root.get("rawName"), "%" + rawName.substring(1) + "%"));
            } else if (rawName.startsWith("!")) {
                predicates.add(criteriaBuilder.equal(root.get("rawName"), rawName.substring(1)));
            } else {
                predicates.add(criteriaBuilder.like(root.get("rawName"), "%" + rawName + "%"));
            }
        }
        if (Strings.isNotBlank(providerName)) {
            if (providerName.startsWith("-")) {
                predicates.add(criteriaBuilder.notLike(root.join("provider").get("name"), "%" + providerName.substring(1) + "%"));
            } else if (providerName.startsWith("!")) {
                predicates.add(criteriaBuilder.equal(root.join("provider").get("name"), providerName.substring(1)));
            } else {
                predicates.add(criteriaBuilder.like(root.join("provider").get("name"), "%" + providerName + "%"));
            }
        }
        if (Objects.nonNull(countries) && !countries.isEmpty()) {
            predicates.add(root.get("country").in(countries));
        }
        if (Objects.nonNull(isMappedToActiveProvider)) {
            Join<Provider, RawProvider> providerRawProviderJoin = root.join("provider");
            predicates.add(criteriaBuilder.or(criteriaBuilder.equal(providerRawProviderJoin.get("mnoActive"), true),
                    criteriaBuilder.equal(providerRawProviderJoin.get("ispActive"), true)));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
