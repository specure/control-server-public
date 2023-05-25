package com.specure.request.sah;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public class SignalRequest {

    @NotNull(message = "UUID of client must be not null")
    @ApiModelProperty(notes = "UUID of client", example = "68796996-5f40-11eb-ae93-0242ac130002")
    private final UUID uuid;

    @ApiModelProperty(notes = "Time zone of client", example = "Europe/Prague")
    private final String timezone;

    @ApiModelProperty(notes = "Time instant of client", example = "1571665024591")
    private final Long time;
}
