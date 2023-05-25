package com.specure.service.sah;

import com.specure.common.model.jpa.ProbePort;

public interface ProbePortService {

    ProbePort getProbePortByNameAndProbeId(String name, String probeId);
}
