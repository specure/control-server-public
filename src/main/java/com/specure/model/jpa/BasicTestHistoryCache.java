package com.specure.model.jpa;

import com.specure.common.model.jpa.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "measurement_cache")
public class BasicTestHistoryCache extends BaseEntity {

    @Id
    @Column(name = "open_test_uuid")
    private String openTestUuid;

    @Column(name = "basic_test_history")
    private String basicTestHistory;
}
