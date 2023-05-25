package com.specure.utils.sah;

import com.vdurmont.semver4j.Requirement;
import com.vdurmont.semver4j.Semver;
import com.vdurmont.semver4j.SemverException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidateUtils {

    public void validateClientVersion(String configVersion, String clientVersion) {
        String versionString = clientVersion.length() == 3 ? clientVersion + ".0" : clientVersion;
        Semver semverVersion = new Semver(versionString, Semver.SemverType.NPM);
        Requirement requirement = Requirement.buildNPM(configVersion);

        if (!semverVersion.satisfies(requirement)) {
            throw new SemverException("Requirement not satisfied");
        }
    }
}
