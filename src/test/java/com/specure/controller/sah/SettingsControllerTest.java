package com.specure.controller.sah;

import com.specure.advice.ControllerErrorAdvice;
import com.specure.constant.URIConstants;
import com.specure.common.enums.ClientType;
import com.specure.request.core.MobileSettingsRequest;
import com.specure.request.core.SettingRequest;
import com.specure.response.core.MeasurementServerResponseForSettings;
import com.specure.response.core.settings.*;
import com.specure.sah.TestConstants;
import com.specure.sah.TestUtils;
import com.specure.service.core.SettingsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class SettingsControllerTest {

    @MockBean
    SettingsService settingsService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        SettingsController settingsController = new SettingsController(settingsService);
        mockMvc = MockMvcBuilders.standaloneSetup(settingsController)
                .setControllerAdvice(new ControllerErrorAdvice())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void getSettingsForWevClient_WhenCalled_ExpectCorrectResponse() throws Exception {
        SettingRequest settingRequest = SettingRequest.builder().build();
        SettingsResponse settingsResponse = SettingsResponse.builder().build();
        when(settingsService.getSettingsByRequest(settingRequest)).thenReturn(settingsResponse);
        mockMvc.perform(MockMvcRequestBuilders
                .post(URIConstants.SETTINGS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(settingRequest))
        ).andExpect(status().isOk());
    }

    @Test
    public void showNewsList_whenCommonRequest_expectGetSettingsCalled() throws Exception {
        var request = getSettingsRequest();
        var response = getSettingsResponse();
        when(settingsService.getMobileSettings(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MOBILE + URIConstants.SETTINGS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.settings[0].terms_and_conditions.version").value(TestConstants.DEFAULT_TERM_AND_CONDITION_VERSION))
                .andExpect(jsonPath("$.settings[0].terms_and_conditions.url").value(TestConstants.DEFAULT_TERM_AND_CONDITION_URL))
                .andExpect(jsonPath("$.settings[0].terms_and_conditions.ndt_url").value(TestConstants.DEFAULT_TERM_AND_CONDITION_NDT_URL))
                .andExpect(jsonPath("$.settings[0].urls.url_share").value(TestConstants.DEFAULT_URLS_URL_SHARE))
                .andExpect(jsonPath("$.settings[0].urls.url_ipv6_check").value(TestConstants.DEFAULT_URLS_IPV6_CHECK))
                .andExpect(jsonPath("$.settings[0].urls.control_ipv4_only").value(TestConstants.DEFAULT_URLS_CONTROL_IPV4_ONLY))
                .andExpect(jsonPath("$.settings[0].urls.open_data_prefix").value(TestConstants.DEFAULT_URLS_OPEN_DATA_PREFIX))
                .andExpect(jsonPath("$.settings[0].urls.url_map_server").value(TestConstants.DEFAULT_URLS_URL_MAP_SERVER))
                .andExpect(jsonPath("$.settings[0].urls.url_ipv4_check").value(TestConstants.DEFAULT_URLS_URL_IPV4_CHECK))
                .andExpect(jsonPath("$.settings[0].urls.control_ipv6_only").value(TestConstants.DEFAULT_URLS_CONTROL_IPV6_ONLY))
                .andExpect(jsonPath("$.settings[0].urls.statistics").value(TestConstants.DEFAULT_URLS_STATISTICS))
                .andExpect(jsonPath("$.settings[0].qostesttype_desc[0].name").value(TestConstants.DEFAULT_QOS_TEST_TYPE_DESC_NAME))
                .andExpect(jsonPath("$.settings[0].qostesttype_desc[0].testType").value(TestConstants.DEFAULT_QOS_MEASUREMENT.toString()))
                .andExpect(jsonPath("$.settings[0].versions.control_server_version").value(TestConstants.DEFAULT_CONTROL_SERVER_VERSION))
                .andExpect(jsonPath("$.settings[0].servers[0].name").value(TestConstants.DEFAULT_TEST_SERVER_NAME))
                .andExpect(jsonPath("$.settings[0].servers[0].uuid").value(TestConstants.DEFAULT_SERVER_UUID))
                .andExpect(jsonPath("$.settings[0].servers_ws[0].name").value(TestConstants.DEFAULT_TEST_SERVER_WS_NAME))
                .andExpect(jsonPath("$.settings[0].servers_ws[0].uuid").value(TestConstants.DEFAULT_SERVER_WS_UUID))
                .andExpect(jsonPath("$.settings[0].servers_qos[0].name").value(TestConstants.DEFAULT_TEST_SERVER_QOS_NAME))
                .andExpect(jsonPath("$.settings[0].servers_qos[0].uuid").value(TestConstants.DEFAULT_SERVER_QOS_UUID))
                .andExpect(jsonPath("$.settings[0].history.devices[0]").value(TestConstants.DEFAULT_HISTORY_DEVICE))
                .andExpect(jsonPath("$.settings[0].history.networks[0]").value(TestConstants.DEFAULT_HISTORY_NETWORK))
                .andExpect(jsonPath("$.settings[0].uuid").value(TestConstants.DEFAULT_CLIENT_UUID_STRING.toString()))
                .andExpect(jsonPath("$.settings[0].map_server.port").value(TestConstants.DEFAULT_MAP_SERVER_PORT))
                .andExpect(jsonPath("$.settings[0].map_server.host").value(TestConstants.DEFAULT_MAP_SERVER_HOST))
                .andExpect(jsonPath("$.settings[0].map_server.ssl").value(TestConstants.DEFAULT_FLAG_TRUE));
    }

    private MobileSettingsResponse getSettingsResponse() {
        var mapServerResponse = MapServerSettingsResponse.builder()
                .port(TestConstants.DEFAULT_MAP_SERVER_PORT)
                .ssl(TestConstants.DEFAULT_FLAG_TRUE)
                .host(TestConstants.DEFAULT_MAP_SERVER_HOST)
                .build();

        var history = HistorySettingsResponse.builder()
                .networks(List.of(TestConstants.DEFAULT_HISTORY_NETWORK))
                .devices(List.of(TestConstants.DEFAULT_HISTORY_DEVICE))
                .build();

        var qosTestTypeDescResponse = QosMeasurementTypeDescription.builder()
                .testType(TestConstants.DEFAULT_QOS_MEASUREMENT)
                .name(TestConstants.DEFAULT_QOS_TEST_TYPE_DESC_NAME)
                .build();

        var server = MeasurementServerResponseForSettings.builder()
                .name(TestConstants.DEFAULT_TEST_SERVER_NAME)
                .uuid(TestConstants.DEFAULT_SERVER_UUID)
                .build();

        var serverWSResponseList = MeasurementServerResponseForSettings.builder()
                .name(TestConstants.DEFAULT_TEST_SERVER_WS_NAME)
                .uuid(TestConstants.DEFAULT_SERVER_WS_UUID)
                .build();

        var serverQoSResponseList = MeasurementServerResponseForSettings.builder()
                .name(TestConstants.DEFAULT_TEST_SERVER_QOS_NAME)
                .uuid(TestConstants.DEFAULT_SERVER_QOS_UUID)
                .build();

        var version = VersionsResponse.builder()
                .controlServerVersion(TestConstants.DEFAULT_CONTROL_SERVER_VERSION)
                .build();

        var termAndConditionsResponse = TermAndConditionsResponse.builder()
                .version(TestConstants.DEFAULT_TERM_AND_CONDITION_VERSION)
                .url(TestConstants.DEFAULT_TERM_AND_CONDITION_URL)
                .ndtUrl(TestConstants.DEFAULT_TERM_AND_CONDITION_NDT_URL)
                .build();

        var urls = UrlsResponse.builder()
                .urlMapServer(TestConstants.DEFAULT_URLS_URL_MAP_SERVER)
                .urlShare(TestConstants.DEFAULT_URLS_URL_SHARE)
                .urlIpv6Check(TestConstants.DEFAULT_URLS_IPV6_CHECK)
                .urlIpv4Check(TestConstants.DEFAULT_URLS_URL_IPV4_CHECK)
                .controlIpv4Only(TestConstants.DEFAULT_URLS_CONTROL_IPV4_ONLY)
                .statistics(TestConstants.DEFAULT_URLS_STATISTICS)
                .controlIpv6Only(TestConstants.DEFAULT_URLS_CONTROL_IPV6_ONLY)
                .openDataPrefix(TestConstants.DEFAULT_URLS_OPEN_DATA_PREFIX)
                .build();

        var setting = MobileSettingResponse.builder()
                .uuid(TestConstants.DEFAULT_CLIENT_UUID_STRING.toString())
                .history(history)
                .mapServerResponse(mapServerResponse)
                .qosTestTypeDescResponse(List.of(qosTestTypeDescResponse))
                .servers(List.of(server))
                .serverWSResponseList(List.of(serverWSResponseList))
                .serverQoSResponseList(List.of(serverQoSResponseList))
                .versions(version)
                .termAndConditionsResponse(termAndConditionsResponse)
                .urls(urls)
                .build();

        return MobileSettingsResponse.builder()
                .settings(List.of(setting))
                .build();
    }

    private MobileSettingsRequest getSettingsRequest() {
        return MobileSettingsRequest.builder()
                .type(ClientType.DESKTOP)
                .language(TestConstants.DEFAULT_LANGUAGE)
                .uuid(UUID.fromString(TestConstants.DEFAULT_CLIENT_UUID_STRING))
                .build();
    }
}
