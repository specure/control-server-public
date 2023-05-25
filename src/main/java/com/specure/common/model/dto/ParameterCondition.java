package com.specure.common.model.dto;

import com.specure.common.enums.Condition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class ParameterCondition {
    private Condition condition;
    private double value;

    public boolean isTrue(double param) {
        switch (condition) {
            case MORE:
                return param > value;
            case LESS:
                return param < value;
            case MORE_OR_EQUAL:
                return param >= value;
            case LESS_OR_EQUAL:
                return param <= value;
        }
        return false;
    }
}
