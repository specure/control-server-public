package com.specure.model.jpa;

import com.specure.common.model.jpa.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cache extends BaseEntity {

    @Id
    private String id;

    private String value;
}
