package com.specure.service.sah.impl;

import com.specure.common.constant.AdminSetting;
import com.specure.common.model.jpa.Settings;
import com.specure.common.repository.SettingsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.common.base.Strings;
import com.specure.config.ApplicationProperties;
import com.specure.constant.Config;
import com.specure.common.enums.Platform;
import com.specure.enums.QosMeasurement;
import com.specure.exception.NotSupportedClientVersionException;
import com.specure.common.model.jpa.Client;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.core.ClientRepository;
import com.specure.request.core.CapabilitiesRequest;
import com.specure.request.core.MobileSettingsRequest;
import com.specure.request.core.SettingRequest;
import com.specure.response.core.MeasurementServerResponseForSettings;
import com.specure.response.core.MeasurementSettingsResponse;
import com.specure.response.core.settings.*;
import com.specure.service.core.ClientService;
import com.specure.service.core.MeasurementServerService;
import com.specure.service.core.SettingsService;
import com.specure.service.sah.BasicTestService;
import com.specure.utils.core.LongUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {

    @Value("${git.branch}")
    private String branch;

    @Value("${git.commit.id.describe}")
    private String describe;

    private final ClientRepository clientRepository;
    private final ClientService clientService;
    private final SettingsRepository settingsRepository;
    @Qualifier("basicMeasurementServerService")
    private final MeasurementServerService basicMeasurementServerService;
    private final BasicTestService basicTestService;
    private final MultiTenantManager multiTenantManager;
    private final ApplicationProperties applicationProperties;

    @Override
    public SettingsResponse getSettingsByRequest(SettingRequest settingRequest) {
        log.debug("SettingsServiceImpl:getSettingsByRequest started with tenant = {}, settingRequest = {}", multiTenantManager.getCurrentTenant(), settingRequest);
        String uuid = UUID.randomUUID().toString();
        clientRepository.save(Client.builder().uuid(uuid).build());
        AdvertisedSpeedOption advertisedSpeedOption1 = AdvertisedSpeedOption.builder()
                .enabled(true)
                .maxSpeedDownKbps(100000)
                .maxSpeedUpKbps(100000)
                .minSpeedDownKbps(0)
                .minSpeedUpKbps(0)
                .name("xdsl")
                .uid(1)
                .build();
        AdvertisedSpeedOption advertisedSpeedOption2 = AdvertisedSpeedOption.builder()
                .enabled(true)
                .maxSpeedDownKbps(1000000)
                .maxSpeedUpKbps(1000000)
                .minSpeedDownKbps(0)
                .minSpeedUpKbps(0)
                .name("ethernet")
                .uid(2)
                .build();
        AdvertisedSpeedOption advertisedSpeedOption3 = AdvertisedSpeedOption.builder()
                .enabled(true)
                .maxSpeedDownKbps(108000)
                .maxSpeedUpKbps(400000)
                .minSpeedDownKbps(0)
                .minSpeedUpKbps(0)
                .name("docsis")
                .uid(3)
                .build();

        List<AdvertisedSpeedOption> advertisedSpeedOptionList = List.of(advertisedSpeedOption1, advertisedSpeedOption2, advertisedSpeedOption3);

        List<QosMeasurementTypeDescription> qosMeasurementTypeDescriptionList = List.of(
                QosMeasurementTypeDescription.builder()
                        .name(QosMeasurement.WEBSITE.getDescription())
                        .testType(QosMeasurement.WEBSITE).build(),
                QosMeasurementTypeDescription.builder()
                        .name(QosMeasurement.HTTP_PROXY.getDescription())
                        .testType(QosMeasurement.HTTP_PROXY).build(),
                QosMeasurementTypeDescription.builder()
                        .name(QosMeasurement.NON_TRANSPARENT_PROXY.getDescription())
                        .testType(QosMeasurement.NON_TRANSPARENT_PROXY).build(),
                QosMeasurementTypeDescription.builder()
                        .name(QosMeasurement.DNS.getDescription())
                        .testType(QosMeasurement.DNS).build(),
                QosMeasurementTypeDescription.builder()
                        .name(QosMeasurement.TCP.getDescription())
                        .testType(QosMeasurement.TCP).build(),
                QosMeasurementTypeDescription.builder()
                        .name(QosMeasurement.UDP.getDescription())
                        .testType(QosMeasurement.UDP).build(),
                QosMeasurementTypeDescription.builder()
                        .name(QosMeasurement.VOIP.getDescription())
                        .testType(QosMeasurement.VOIP).build(),
                QosMeasurementTypeDescription.builder()
                        .name(QosMeasurement.TRACEROUTE.getDescription())
                        .testType(QosMeasurement.TRACEROUTE).build()
        );

        SettingResponse settingResponse = SettingResponse.builder()
                .advertisedSpeedOption(advertisedSpeedOptionList)
                .history(HistorySettingsResponse.builder()
                        .devices(Collections.emptyList())
                        .networks(Collections.emptyList())
                        .build())
                .mapServer(MapServerSettingsResponse.builder()
                        .host("nettest.org")
                        .port(443L)
                        .ssl(true)
                        .build())
                .qosTestTypeDesc(qosMeasurementTypeDescriptionList)
                .surveySettings(SurveySettingsResponse.builder()
                        .dateStarted(1520153008000L)
                        .isActiveService(false)
                        .surveyUrl("null")
                        .build())
                .urls(UrlsResponse.builder()
                        .controlIpv4Only("c01ipv4.nettest.org")
                        .controlIpv6Only("c01ipv6.nettest.org")
                        .openDataPrefix("https://nettest.org/en/opentest/")
                        .statistics("https://nettest.org/en/statistics")
                        .urlIpv4Check("https://c01ipv4.nettest.org/RMBTControlServer/ip")
                        .urlIpv6Check("https://c01ipv6.nettest.org/RMBTControlServer/ip")
                        .build())
                .uuid(uuid)
                .versions(VersionsResponse.builder()
                        .controlServerVersion("master_build_ver.2.6.0-101-ga1f28720b")
                        .build())
                .build();

        SettingsResponse settingsResponse = SettingsResponse.builder()
                .settings(Collections.singletonList(settingResponse))
                .errors(Collections.emptyList())
                .build();
        log.debug("SettingsServiceImpl:getSettingsByRequest finished with tenant = {}, settingsResponse = {}", multiTenantManager.getCurrentTenant(), settingResponse);
        return settingsResponse;
    }

    @Override
    public MobileSettingsResponse getMobileSettings(MobileSettingsRequest request) {
        log.debug("SettingsServiceImpl:getMobileSettings started with tenant = {}, mobileSettingsRequest = {}");
        var lang = request.getLanguage();

        if (!Config.SUPPORTED_CLIENT_NAMES.contains(request.getName())) {//TODO remove to enum
            throw new NotSupportedClientVersionException(request.getName());
        }

        Map<String, String> settings = getSettingsURLMapByLanguageAndKeys(lang);

        var setting = MobileSettingResponse.builder()
                .termAndConditionsResponse(getTermAndConditionResponse(request.getPlatform(), request.getSoftwareVersionName(), settings))
                .qosTestTypeDescResponse(getQosMeasurementTypeDescriptionResponse())
                .urls(getUrlsResponse(settings))
                .history(basicTestService.getHistoryResponseByClientUuid(request.getUuid()))
                .servers(getServers(request.getCapabilities()))
                .serverWSResponseList(basicMeasurementServerService.getServers(Config.SERVER_WS_TEST_SERVER_TYPES))
                .serverQoSResponseList(basicMeasurementServerService.getServers(Config.SERVER_QOS_TEST_SERVER_TYPES))
                .mapServerResponse(getMapServerResponse(settings))
                .versions(getVersionResponse())
                .measurement(getMeasurementSettings(settings, request.getPlatform()));


        Client savedClient = clientService.updateClientSettings(request);
        if (Objects.nonNull(savedClient)) {
            setting.uuid(savedClient.getUuid());
        }

        return MobileSettingsResponse.builder()
                .settings(List.of(setting.build()))
                .build();
    }

    private MeasurementSettingsResponse getMeasurementSettings(Map<String, String> settings, String platform) {
        String numThreadsKeyByPlatform = getNumberOfThreadsKeyByPlatform(platform);
        return MeasurementSettingsResponse.builder()
                .measurementDuration(settings.getOrDefault(AdminSetting.MEASUREMENT_DURATION_KEY, String.valueOf(applicationProperties.getDuration())))
                .measurementNumPings(settings.getOrDefault(AdminSetting.MEASUREMENT_NUM_PINGS_KEY, String.valueOf(applicationProperties.getPings())))
                .measurementNumThreads(settings.getOrDefault(numThreadsKeyByPlatform, String.valueOf(applicationProperties.getThreads())))
                .build();
    }

    private String getNumberOfThreadsKeyByPlatform(String platform) {
        if (Objects.nonNull(platform) && platform.equalsIgnoreCase(Platform.IOS.name().toLowerCase())) {
            return AdminSetting.MEASUREMENT_NUM_THREADS_IOS_KEY;
        }
        return AdminSetting.MEASUREMENT_NUM_THREADS_ANDROID_KEY;
    }

    private List<QosMeasurementTypeDescription> getQosMeasurementTypeDescriptionResponse() {
        return Arrays.stream(QosMeasurement.values())
                .map(QosMeasurementTypeDescription::fromQosMeasurement)
                .collect(Collectors.toList());
    }


    @Override
    public Map<String, String> getSettingsMap() {
        return settingsRepository.findAll().stream()
                .collect(Collectors.toMap(Settings::getKey, Settings::getValue, (val1, val2) -> val1));
    }

    private ObjectMapper getSettingsMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Boolean.class, ToStringSerializer.instance);
        mapper.registerModule(module);
        return mapper;
    }

    private VersionsResponse getVersionResponse() {
        return VersionsResponse.builder()
                .controlServerVersion(String.format("%s_%s", branch, describe))
                .build();
    }

    private TermAndConditionsResponse getTermAndConditionResponse(String platform, String softwareVersionName, Map<String, String> settings) {
        String tcUrl = null;
        String tcNdtUrl = null;
        Long tcVersion = null;
        if (Objects.nonNull(platform) && platform.equalsIgnoreCase("android")) {
            tcUrl = settings.get("tc_url_android");
            tcNdtUrl = settings.get("tc_ndt_url_android");
            tcVersion = LongUtils.parseLong(settings.get("tc_version_android"));

            if (Objects.nonNull(tcUrl) && Objects.nonNull(softwareVersionName) && softwareVersionName.startsWith("4.")) {
                String newUrl = settings.get("tc_url_android_v4");
                if (!Strings.isNullOrEmpty(newUrl)) {
                    tcUrl = newUrl;
                }
            }
        } else if (Objects.nonNull(platform) && platform.equalsIgnoreCase("ios")) {
            tcUrl = settings.get("tc_url_ios");
            tcVersion = LongUtils.parseLong(settings.get("tc_version_ios"));
        }
        if (Objects.isNull(tcVersion)) {
            tcVersion = LongUtils.parseLong(settings.get("tc_version"));
        }
        if (Objects.isNull(tcUrl)) {
            tcUrl = settings.get("tc_url");
        }

        return TermAndConditionsResponse.builder()
                .version(tcVersion)
                .ndtUrl(tcNdtUrl)
                .url(tcUrl)
                .build();
    }

    private MapServerSettingsResponse getMapServerResponse(Map<String, String> settings) {
        return MapServerSettingsResponse.builder()
                .host(settings.get("host_map_server"))
                .ssl(Boolean.parseBoolean(settings.get("ssl_map_server")))
                .port(LongUtils.parseLong(settings.get("port_map_server")))
                .build();
    }

    private List<MeasurementServerResponseForSettings> getServers(CapabilitiesRequest capabilitiesRequest) {
        var isRmbtHttp = Optional.ofNullable(capabilitiesRequest)
                .map(CapabilitiesRequest::isRmbtHttp)
                .orElse(false);
        if (isRmbtHttp) {
            return basicMeasurementServerService.getServers(Config.SERVER_TEST_SERVER_TYPES);
        } else {
            return basicMeasurementServerService.getServers(Config.SERVER_HTTP_TEST_SERVER_TYPES);
        }
    }

    private UrlsResponse getUrlsResponse(Map<String, String> settings) {
        return UrlsResponse.builder()
                .openDataPrefix(settings.get("url_open_data_prefix"))
                .urlShare(settings.get("url_share"))
                .statistics(settings.get("url_statistics"))
                .controlIpv4Only(settings.get("control_ipv4_only"))
                .controlIpv6Only(settings.get("control_ipv6_only"))
                .urlIpv4Check(settings.get("url_ipv4_check"))
                .urlIpv6Check(settings.get("url_ipv6_check"))
                .urlMapServer(settings.get("url_map_server"))
                .build();
    }

    private Map<String, String> getSettingsURLMapByLanguageAndKeys(String lang) {
        return settingsRepository.findAllByLangOrLangIsNullAndKeyIn(lang, AdminSetting.ADMIN_SETTINGS_KEYS).stream()
                .collect(Collectors.toMap(Settings::getKey, Settings::getValue, (val1, val2) -> val1));
    }
}
