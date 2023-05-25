package com.specure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.specure.dto.mobile.QoeClassificationThresholds;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum QoeCategory {
    STREAMING_AUDIO_STREAMING("streaming_audio_streaming", 500L, 300L, 100L, 101L, 100L, 100L, 100000000L, 200000000L, 500000000L),
    VIDEO_SD("video_sd", 4000L, 1000L, 300L, 400L, 100L, 100L, 100000000L, 200000000L, 500000000L),
    VIDEO_HD("video_hd", 10000L, 4000L, 2000L, 1000L, 400L, 200L, 50000000L, 100000000L, 250000000L),
    VIDEO_UHD("video_uhd", 40000L, 25000L, 15000L, 4000L, 2500L, 1500L, 25000000L, 50000000L, 100000000L),
    GAMING("gaming", 8000L, 4000L, 2000L, 8000L, 4000L, 2000L, 10000000L, 10000000L, 50000000L),
    GAMING_CLOUD("gaming_cloud", 40000L, 25000L, 10000L, 8000L, 4000L, 2000L, 10000000L, 10000000L, 50000000L),
    GAMING_STREAMING("gaming_streaming", 8000L, 4000L, 2000L, 60000L, 30000L, 10000L, 10000000L, 10000000L, 50000000L),
    GAMING_DOWNLOAD("gaming_download", 100000L, 50000L, 20000L, 10000L, 5000L, 2000L, 100000000L, 200000000L, 500000000L),
    VOIP("voip", 300L, 200L, 100L, 300L, 200L, 100L, 25000000L, 50000000L, 100000000L),
    VIDEO_TELEPHONY("video_telephony", 5000L, 2000L, 1000L, 5000L, 2000L, 1000L, 25000000L, 50000000L, 100000000L),
    VIDEO_CONFERENCING("video_conferencing", 20000L, 6000L, 3000L, 20000L, 6000L, 3000L, 25000000L, 50000000L, 100000000L),
    MESSAGING("messaging", 500L, 200L, 100L, 500L, 200L, 100L, 50000000L, 100000000L, 250000000L),
    WEB("web", 50000L, 20000L, 10000L, 25000L, 10000L, 5000L, 15000000L, 20000000L, 60000000L),
    CLOUD("cloud", 500000L, 100000L, 50000L, 500000L, 100000L, 50000L, 5000000L, 10000000L, 25000000L);

    private final String value;

    private final Long download4;

    private final Long download3;

    private final Long download2;

    private final Long upload4;

    private final Long upload3;

    private final Long upload2;

    private final Long ping4;

    private final Long ping3;

    private final Long ping2;

    @JsonCreator
    public static QoeCategory forValue(String value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return QoeCategory.valueOf(value.toUpperCase());
    }

    public static List<QoeClassificationThresholds> getQoeClassificationThreshold() {
        return Stream.of(QoeCategory.values())
                .map(val -> QoeClassificationThresholds.builder()
                        .qoeCategory(val)
                        .thresholds(Map.of(
                                QoeCriteria.PING, new Long[]{val.getPing4(), val.getPing3(), val.getPing2()},
                                QoeCriteria.DOWN, new Long[]{val.getDownload4(), val.getDownload3(), val.getDownload2()},
                                QoeCriteria.UP, new Long[]{val.getUpload4(), val.getUpload3(), val.getUpload2()}))
                        .build())
                .collect(Collectors.toList());
    }
}
