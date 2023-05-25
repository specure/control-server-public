package com.specure.common.constant;

public interface ErrorMessage {
    String MEASUREMENT_NOT_FOUND_BY_UUID = "The history of your measurement results is not available.";
    String BAD_CAMPAIGN_STATUS = "Ad-hoc campaign status %s is wrong";
    String BAD_PACKAGE_TYPE_NAME = "There is no defined packageType %s.";
    String PROBE_PORT_NOT_FOUND_BY_NAME_AND_PROBE_ID = "There is no probe port was found by name %s and probe id %s.";
    String MOBILE_MODEL_NOT_FOUND_BY_ID = "Mobile model not found with id %s";
    String MOBILE_MODEL_NOT_FOUND_BY_MODEL = "Mobile model not found with model %s";
    String PROVIDER_NOT_FOUND_BY_ID = "Provider was not found by id %s";
    String WRONG_NAME_OF_USER_EXPERIENCE_PARAMETER = "Wrong name of user experience parameter %s";
}
