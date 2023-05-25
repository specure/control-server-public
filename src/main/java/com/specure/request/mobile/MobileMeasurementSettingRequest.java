package com.specure.request.mobile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.constant.ErrorMessage;
import com.specure.common.enums.MeasurementServerType;
import com.specure.enums.MeasurementType;
import com.specure.common.enums.Platform;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Data
public class MobileMeasurementSettingRequest {

    @NotBlank(message = ErrorMessage.CLIENT_UUID_REQUIRED)
    @JsonProperty("uuid")
    private String clientUuid;

    @JsonProperty("language")
    private String language;

    private String timezone;

    @ApiModelProperty(notes = "Platform of the client")
    @JsonProperty("platform")
    private Platform platform;

    @ApiModelProperty(notes = "ID of the measurement server which user prefers. Leave empty if no preference.", example = "12")
    @JsonProperty("prefer_server")
    private Long preferredServer;

    @ApiModelProperty(notes = "Define number of threads used by client. Send -1 or leave empty to use defaults value")
    @JsonProperty("num_threads")
    private Integer numberOfThreads;

    @JsonProperty("location")
    private LocationRequest location;

    @ApiModelProperty(notes = "Type of the server")
    @JsonProperty("client")
    private MeasurementServerType measurementServerType;

    @ApiModelProperty(notes = "Version of client", example = "3.0")
    @JsonProperty("client_version")
    private String clientVersion;

    @ApiModelProperty(notes = "Version of application", example = "3.0")
    @JsonProperty("app_version")
    private String appVersion;

    @ApiModelProperty(notes = "Type of the client")
    @JsonProperty("client_name")
    private String clientName;

    @JsonProperty("loopmode_info")
    private LoopModeInfo loopModeInfo;

    @JsonProperty("measurement_type_flag")
    private MeasurementType measurementType;

    @JsonProperty("telephony_permission_granted")
    private Boolean telephonyPermissionGranted;

    @JsonProperty("location_permission_granted")
    private Boolean locationPermissionGranted;

    @JsonProperty("uuid_permission_granted")
    private Boolean uuidPermissionGranted;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class LoopModeInfo {
        @ApiModelProperty(notes = "ID of the loop settings - internal DB ID", example = "457546")
        @JsonProperty("uid")
        private Long uid;

        @ApiModelProperty(notes = "UUID of the test", example = "37e57f4e-25df-4bcf-9151-9f6eff311279")
        @JsonProperty("test_uuid")
        private String testUuid;//TODO:Why we don't use this field?

        @ApiModelProperty(notes = "UUID of the client (not necessary to send in this object, it will be taken from /testRequest top level object)")
        @JsonProperty("client_uuid")
        private String clientUuid;
        ;//TODO:Why we don't use this field?

        @ApiModelProperty(notes = "Maximum delay between 2 tests in seconds")
        @JsonProperty("max_delay")
        private Integer maxDelay;

        @ApiModelProperty(notes = "Maximum movement between 2 tests in meters")
        @JsonProperty("max_movement")
        private Integer maxMovement;

        @ApiModelProperty(notes = "How many tets should be executed")
        @JsonProperty("max_tests")
        private Integer maxTests;

        @ApiModelProperty(notes = "Number of the test")
        @JsonProperty("test_counter")
        private Integer testCounter;

        @Deprecated
        @JsonProperty("text_counter")
        @ApiModelProperty(notes = "This is a legacy parent of `test_counter` property. Please do not use it")
        private Integer textCounter;

        @ApiModelProperty(notes = "Loop UUID of the test series with first test will be generated, so first test will be null there in request and new will be generated", example = "37e57f4e-25df-4bcf-9151-9f6eff311279")
        @JsonProperty("loop_uuid")
        private String loopUuid;
    }
}
