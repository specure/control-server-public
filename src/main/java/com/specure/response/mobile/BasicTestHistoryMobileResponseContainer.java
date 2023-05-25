package com.specure.response.mobile;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class BasicTestHistoryMobileResponseContainer {

    private List<BasicTestHistoryMobileResponse> tests;
}
