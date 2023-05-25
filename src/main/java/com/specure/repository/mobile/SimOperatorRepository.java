package com.specure.repository.mobile;

import com.specure.model.jpa.SimOperator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SimOperatorRepository extends JpaRepository<SimOperator, Long> {

    List<SimOperator> findByMccMncOrderByValidFromDesc(String mccMnc);
}
