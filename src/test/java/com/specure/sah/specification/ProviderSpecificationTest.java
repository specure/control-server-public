package com.specure.sah.specification;

import com.specure.common.model.jpa.Provider;
import com.specure.common.specification.ProviderSpecification;
import com.specure.sah.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.criteria.*;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ProviderSpecificationTest {

    private ProviderSpecification providerSpecification;

    @Mock
    private Root<Provider> root;
    @Mock
    private CriteriaQuery<?> query;
    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private Predicate predicateIsActive;
    @Mock
    private Path<Object> activePath;
    @Mock
    private Path<Object> countryPath;
    @Mock
    private Predicate predicateIsNotActive;
    @Mock
    private Predicate countryPredicate;
    @Mock
    private Predicate nameIsEqualsPredicate;
    @Mock
    private Predicate nameContainsPredicate;
    @Mock
    private Predicate nameNotContainsPredicate;
    @Mock
    private Path<String> namePath;

    @BeforeEach
    void setUp() {
    }

    @Test
    void toPredicate_nameIsEquals_expectPredicate() {
        providerSpecification = ProviderSpecification.builder()
                .name("!" + TestConstants.DEFAULT_PROVIDER_NAME)
                .build();
        when(root.<String>get("name")).thenReturn(namePath);
        when(criteriaBuilder.equal(namePath, TestConstants.DEFAULT_PROVIDER_NAME)).thenReturn(nameIsEqualsPredicate);

        providerSpecification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).and(nameIsEqualsPredicate);
    }

    @Test
    void toPredicate_nameContains_expectPredicate() {
        providerSpecification = ProviderSpecification.builder()
                .name(TestConstants.DEFAULT_PROVIDER_NAME)
                .build();
        when(root.<String>get("name")).thenReturn(namePath);
        when(criteriaBuilder.like(namePath, "%" + TestConstants.DEFAULT_PROVIDER_NAME + "%")).thenReturn(nameContainsPredicate);

        providerSpecification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).and(nameContainsPredicate);
    }

    @Test
    void toPredicate_nameNotContains_expectPredicate() {
        providerSpecification = ProviderSpecification.builder()
                .name("-" + TestConstants.DEFAULT_PROVIDER_NAME)
                .build();
        when(root.<String>get("name")).thenReturn(namePath);
        when(criteriaBuilder.notLike(namePath, "%" + TestConstants.DEFAULT_PROVIDER_NAME + "%")).thenReturn(nameNotContainsPredicate);

        providerSpecification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).and(nameNotContainsPredicate);
    }

    @Test
    void toPredicate_isIspActiveTrue_expectPredicate() {
        providerSpecification = ProviderSpecification.builder()
                .ispActive(true)
                .build();
        when(root.get("ispActive")).thenReturn(activePath);
        when(criteriaBuilder.equal(activePath, true)).thenReturn(predicateIsActive);

        providerSpecification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).and(predicateIsActive);
    }

    @Test
    void toPredicate_isMnoActiveTrue_expectPredicate() {
        providerSpecification = ProviderSpecification.builder()
                .mnoActive(true)
                .build();
        when(root.get("mnoActive")).thenReturn(activePath);
        when(criteriaBuilder.equal(activePath, true)).thenReturn(predicateIsActive);

        providerSpecification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).and(predicateIsActive);
    }

    @Test
    void toPredicate_isIspActiveFalse_expectPredicate() {
        providerSpecification = ProviderSpecification.builder()
                .ispActive(false)
                .build();
        when(root.get("ispActive")).thenReturn(activePath);
        when(criteriaBuilder.equal(activePath, false)).thenReturn(predicateIsNotActive);

        providerSpecification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).and(predicateIsNotActive);
    }

    @Test
    void toPredicate_isMnoActiveFalse_expectPredicate() {
        providerSpecification = ProviderSpecification.builder()
                .mnoActive(false)
                .build();
        when(root.get("mnoActive")).thenReturn(activePath);
        when(criteriaBuilder.equal(activePath, false)).thenReturn(predicateIsNotActive);

        providerSpecification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).and(predicateIsNotActive);
    }

    @Test
    void toPredicate_countryNotEmpty_expectPredicate() {
        providerSpecification = ProviderSpecification.builder()
                .countries(List.of(TestConstants.DEFAULT_COUNTRY))
                .build();
        when(root.get("country")).thenReturn(countryPath);
        when(countryPath.in(List.of(TestConstants.DEFAULT_COUNTRY))).thenReturn(countryPredicate);

        providerSpecification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).and(countryPredicate);
    }
}
