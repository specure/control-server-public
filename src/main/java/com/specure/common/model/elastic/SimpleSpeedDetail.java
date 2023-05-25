package com.specure.common.model.elastic;

import com.specure.common.enums.Direction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleSpeedDetail implements Serializable {
  @Enumerated(EnumType.STRING)
  private Direction direction;
  private Long time;
  private Integer bytes;
  private Integer thread;
}
