package com.specure.service.core;

public interface FieldAnonymizerFilter {

    String getIpAddressFilter(String ipAddress, String ipHash);

    String saveIpAddressFilter(String ipAddress, String openTestUuid);

    void refreshAnonymizedIpAddress(String ipAddress, String openTestUuid);

    String getWifiSsidFilter(String ipHash);

    String saveWifiSsidFilter(String wifiSsid, String openTestUuid);
}
