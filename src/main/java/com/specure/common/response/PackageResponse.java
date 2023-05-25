package com.specure.common.response;

import com.specure.common.enums.PortType;
import com.specure.enums.PackageDescription;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PackageResponse {
    private Long id;
    private String advertisedName;
    private String nativeName;
    private Long groupId;
    private ProviderResponse provider;
    private PackageDescription packageDescription;
    private String packageType;
    private TechnologyResponse technology;
    private Long threshold;
    private Long download;
    private Long upload;
    private Long throttleSpeedDownload;
    private Long throttleSpeedUpload;
    private PortType readyForPort;
    private boolean isAssignedToPort;
    private boolean visibleOnPublicPortal;
}
