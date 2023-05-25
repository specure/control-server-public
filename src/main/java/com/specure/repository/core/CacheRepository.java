package com.specure.repository.core;

import com.specure.model.jpa.Cache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface CacheRepository extends JpaRepository<Cache, String> {

    @Transactional
    void deleteByCreatedDateBefore(Date expiryDate);
}
