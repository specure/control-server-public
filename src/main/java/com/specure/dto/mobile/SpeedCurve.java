package com.specure.dto.mobile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
@Builder
public class SpeedCurve {
    public List<MobileGraph> download;
    public List<MobileGraph> upload;
}
