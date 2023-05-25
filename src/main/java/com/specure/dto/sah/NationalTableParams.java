package com.specure.dto.sah;

import com.specure.common.enums.NetworkType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class NationalTableParams {
    private final boolean mno;
    private final List<NetworkType> networkTypes;
}
