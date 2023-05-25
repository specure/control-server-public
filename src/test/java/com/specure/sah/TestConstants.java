package com.specure.sah;

import com.specure.common.constant.Constants;
import com.specure.common.enums.*;
import com.specure.common.model.jpa.Provider;
import com.specure.common.response.LocationResponse;
import com.specure.enums.NetNeutralityStatus;
import com.specure.enums.PackageDescription;
import com.specure.enums.QoeCategory;
import com.specure.enums.QosMeasurement;
import com.specure.response.mobile.BasicTestHistoryMobileResponse;

import java.sql.Timestamp;
import java.time.*;
import java.util.*;

public interface TestConstants {
    String DEFAULT_TIME_ZONE = "Europe/Prague";
    String DEFAULT_SIM_MCC_MNC = "222-33";
    String DEFAULT_NETWORK_MCC_MNC = "333-44";
    String DEFAULT_NETWORK_COUNTRY = "DEFAULT_NETWORK_COUNTRY";
    String DEFAULT_SIM_COUNTRY = "DEFAULT_SIM_COUNTRY";
    Boolean DEFAULT_LOCATION_PERMISSION_GRANTED = Boolean.TRUE;
    Boolean DEFAULT_UUID_PERMISSION_GRANTED = Boolean.FALSE;
    Boolean DEFAULT_TELEPHONY_PERMISSION_GRANTED = Boolean.FALSE;
    Boolean DEFAULT_NETWORK_IS_ROAMING = Boolean.TRUE;
    Long DEFAULT_COLUMN_COUNT = 539L;
    String DEFAULT_INTERNET_PROTOCOL = "IpV4";
    String DEFAULT_NETWORK_CHANNEL_NUMBER = "2323";

    interface NetNeutrality {
        Long ID = 22L;
        NetNeutralityTestType DEFAULT_TYPE = NetNeutralityTestType.DNS;
        Double DEFAULT_OVERALL_QOS = 0.75D;
        Long DNS_ID = 223L;
        String DNS_TARGET = "DNS_TARGET";
        Long DNS_TIMEOUT = 12346566L;
        Long UDP_ID = 112L;
        Long UDP_TIMEOUT = 927222L;
        Long WEB_ID = 88L;
        String DNS_ENTRY_TYPE = "DNS_ENTRY_TYPE";
        String DNS_ACTUAL_RESOLVER = "DNS_RESOLVER";
        String DNS_EXPECTED_RESOLVER = "DNS_RESOLVER";
        DnsStatus DNS_EXPECTED_DNS_STATUS = DnsStatus.NOERROR;
        String DNS_EXPECTED_DNS_ENTRIES = "74.6.231.20;98.137.11.164";
        Long UDP_PORT_NUMBER = 1234L;
        Long UDP_MIN_NUMBER_OF_PACKETS_RECEIVED = 12L;
        Long UDP_NUMBER_OF_PACKETS_SENT = 20L;
        String WEB_TARGET = "WEB_TARGET";
        Long WEB_TIMEOUT = 200200L;
        Long WEB_EXPECTED_STATUS_CODE = 200L;
        String DNS_UUID = "18f4e360-2b7b-11ed-a261-0242ac120002";
        String WEB_UUID = "24abfef0-2b7b-11ed-a261-0242ac120002";
        String UDP_UUID = "2931dab2-2b7b-11ed-a261-0242ac120002";
        String DNS_RESULT_INFO = "DNS_RESULT_INFO";
        String DNS_TYPE_OF_RECORD = "DNS_TYPE_OF_RECORD";
        Long DNS_DURATION_NS = 2783738L;
        NetNeutralityStatus DNS_SUCCESS = NetNeutralityStatus.SUCCEED;
        String DNS_ACTUAL_DNS_RESULT_ENTRIES = "74.6.231.20;98.137.11.164";
        List<String> DNS_ACTUAL_DNS_RESULT_ENTRIES_LIST = List.of("74.6.231.20", "98.137.11.164");
        boolean DNS_TIMEOUT_EXCEEDED = false;
        Long UDP_DURATION_NS = 84442L;
        NetNeutralityStatus UDP_SUCCESS = NetNeutralityStatus.FAIL;
        Long UDP_ACTUAL_NUMBER_OF_PACKETS_RECEIVED = 2L;
        Long UDP_EXPECTED_NUMBER_OF_PACKETS_RECEIVED = 7L;
        Long WEB_DURATION_NS = 66666L;
        NetNeutralityStatus WEB_SUCCESS = NetNeutralityStatus.ERROR;
        Long WEB_ACTUAL_STATUS_CODE = 500L;
        boolean WEB_TIMEOUT_EXCEEDED = true;
        String DNS_ACTUAL_DNS_STATUS = "NOERROR";
        Long DNS_TIMESTAMP_INSTANT_MS = 1657881635454L;
        Long UDP_TIMESTAMP_INSTANT_MS = 1657011635454L;
        Long WEB_TIMESTAMP_INSTANT_MS = 1657021635454L;
        String DNS_MEASUREMENT_DATE = Instant.ofEpochMilli(TestConstants.NetNeutrality.DNS_TIMESTAMP_INSTANT_MS).toString();
        String UDP_MEASUREMENT_DATE = Instant.ofEpochMilli(TestConstants.NetNeutrality.UDP_TIMESTAMP_INSTANT_MS).toString();
        String WEB_MEASUREMENT_DATE = Instant.ofEpochMilli(TestConstants.NetNeutrality.WEB_TIMESTAMP_INSTANT_MS).toString();
        boolean WEB_IS_ACTIVE = true;
        boolean DNS_IS_ACTIVE = true;
        boolean UDP_IS_ACTIVE = true;
        String DNS_FAIL_REASON = "DNS_FAIL_REASON";
    }

    interface MobileModel {
        Long ID = 16L;
        String MARKETING_NAME = "iPhone 7 Plus";
        String MODEL = "iPhone9.21";
        String MANUFACTURER = "Apple";
        String CATEGORY = "iPhone";
    }

    interface RawProvider {
        Long ID = 162L;
        String RAW_NAME = "ProviderRawNAme";
        String COUNTRY = "COUNTRY";
        Long ASN = 123123L;
        String MCC_MNC = "894-344";
    }

    interface AdHocCampaignDowntimeResponse {
        Long ID = 81L;
        Timestamp START = new Timestamp(2021, 2, 23, 7, 11, 32, 634);
        ;
        Timestamp FINISH = new Timestamp(2021, 6, 12, 13, 44, 11, 132);
        ;
        Long DURATION = 1888L;


    }

    interface AdHocCampaignMeasurementResponse {
        Timestamp TIMESTAMP_ON_NET = new Timestamp(2022, 8, 13, 12, 22, 54, 434);
        Timestamp TIMESTAMP_OFF_NET = new Timestamp(2022, 9, 7, 16, 11, 32, 843);
        long DOWNLOAD_ON_NET = 33L;
        long UPLOAD_ON_NET = 88L;
        float LATENCY_ON_NET = 6.4f;
        float JITTER_ON_NET = 0.5f;
        float PACKET_ON_LOSS = 0.1f;
        long DOWNLOAD_OFF_NET = 42L;
        long UPLOAD_OFF_NET = 74L;
        float LATENCY_OFF_NET = 17.4f;
        float JITTER_OFF_NET = 0.81f;
        float PACKET_OFF_LOSS = 0.2f;
    }

    interface AdHocCampaignMeasurementsSummaryResponse {

        long AVERAGE_DOWNLOAD_SPEED_ON_NET = 5555L;
        long AVERAGE_UPLOAD_SPEED_ON_NET = 3564L;
        float AVERAGE_LATENCY_ON_NET = 44.5f;
        long TOTAL_ON_NET = 67L;
        long AVERAGE_DOWNLOAD_SPEED_OFF_NET = 444L;
        long AVERAGE_UPLOAD_SPEED_OFF_NET = 8766L;
        float AVERAGE_LATENCY_OFF_NET = 72.5f;
        long TOTAL_OFF_NET = 13L;
    }

    String DEFAULT_OPEN_TEST_UUID_STRING = "6136c67b-c1ad-4c2f-9c86-39328c5dbf35";
    UUID DEFAULT_OPEN_TEST_UUID = UUID.fromString(DEFAULT_OPEN_TEST_UUID_STRING);
    String DEFAULT_OPEN_TEST_UUID_WITH_PREFIX = "O6136c67b-c1ad-4c2f-9c86-39328c5dbf35";
    String DEFAULT_NETWORK_OPERATOR_NAME = "SAHN";
    String DEFAULT_SIM_OPERATOR_NAME = "SIM";
    Integer DEFAULT_DOWNLOAD_SPEED = 1;
    Integer DEFAULT_UPLOAD_SPEED = 2;
    Long DEFAULT_DOWNLOAD_SPEED_FOR_PACKAGE = 1L;
    Long DEFAULT_UPLOAD_SPEED_FOR_PACKAGE = 2L;
    Long DEFAULT_PING_MEDIAN = 58420361L;
    Float DEFAULT_PING = Double.valueOf(DEFAULT_PING_MEDIAN / Constants.PING_CONVERSION_MULTIPLICATOR).floatValue();
    Integer DEFAULT_SIGNAL_STRENGTH = -16;
    Float DEFAULT_JITTER = 1.5f;
    Float DEFAULT_PACKET_LOSS = 1f;
    Integer DEFAULT_PAGE = 1;
    Integer DEFAULT_PAGE_SIZE = 10;
    String DEFAULT_SORT = "measurementDate,desc";
    String DEFAULT_SORT_PROPERTY = "measurementDate";
    Long DEFAULT_ID = 2L;
    Long DEFAULT_QOS_TEST_OBJECTIVE_ID = 5L;
    Long DEFAULT_PROVIDER_ID = 6L;
    Long DEFAULT_PROVIDER_ID_SECOND = 7L;
    Long DEFAULT_PROVIDER_ID_THIRD = 17L;
    Provider DEFAULT_PROVIDER = Provider.builder()
            .id(DEFAULT_PROVIDER_ID)
            .name(TestConstants.DEFAULT_PROVIDER_NAME)
            .build();
    String DEFAULT_PROBE_ID = "DEFAULT_PROBE_ID";
    String DEFAULT_PROBE_ID_2 = "DEFAULT_PROBE_ID_2";
    PackageDescription DEFAULT_PACKAGE_DESCRIPTION = PackageDescription.RESIDENTIAL;
    PackageType DEFAULT_PACKAGE_TYPE = PackageType.FIXED_BROADBAND;
    String DEFAULT_PACKAGE_TYPE_STRING = DEFAULT_PACKAGE_TYPE.getValue();
    String DEFAULT_ADVERTISED_NAME = "DEFAULT_ADVERTISED_NAME";
    Technology DEFAULT_TECHNOLOGY = Technology.ADSL;
    String DEFAULT_TEXT_MORE_63_CHARS = "DEFAULT_TEXT_MORE_THEN_50_CHARS_DEFAULT_TEXT_MORE_THEN_50_CHARS123";
    Long DEFAULT_THRESHOLD = 100L;
    Integer DEFAULT_THROTTLE = 200;
    Long DEFAULT_THROTTLE_SPEED_DOWNLOAD_FOR_PACKAGE = 200L;
    Long DEFAULT_THROTTLE_SPEED_UPLOAD_FOR_PACKAGE = 200L;
    String DEFAULT_KEY = "DEFAULT_KEY";
    Long DEFAULT_LONG_VALUE = 10L;
    String DEFAULT_MOBILE_TECHNOLOGY = "DEFAULT_MOBILE_TECHNOLOGY";
    Long DEFAULT_VALUE = 10L;
    String DEFAULT_FIELD = "DEFAULT_FIELD";
    String DEFAULT_UNIT = "DEFAULT_UNIT";
    LocalDate DEFAULT_DATE_FROM = LocalDate.of(2020, 3, 20);
    LocalDate DEFAULT_DATE_TO = LocalDate.of(2020, 3, 24);
    String DEFAULT_DATE_TIME_FROM_STRING = "2020-03-20T00:00:00Z";
    String DEFAULT_DATE_TIME_TO_STRING = "2020-03-24T23:59:00Z";
    LocalTime DEFAULT_TIME_FROM = LocalTime.of(0, 0);
    LocalTime DEFAULT_TIME_TO = LocalTime.of(23, 59);
    LocalDateTime DEFAULT_DATE_TIME_FROM = LocalDateTime.of(DEFAULT_DATE_FROM, DEFAULT_TIME_FROM);
    LocalDateTime DEFAULT_DATE_TIME_TO = LocalDateTime.of(DEFAULT_DATE_TO, DEFAULT_TIME_TO);
    String DEFAULT_DATE_FROM_STRING = "2020-03-20";
    String DEFAULT_DATE_TO_STRING = "2020-03-24";
    String DEFAULT_PORT = "DEFAULT_PORT";
    String DEFAULT_PACKAGES_REQUEST_PARAMETER = "1";
    List<String> DEFAULT_PACKAGES = Collections.singletonList(DEFAULT_PACKAGES_REQUEST_PARAMETER);
    Long DEFAULT_PORT_ID = 1L;
    Long DEFAULT_PACKAGE_ID = 2L;
    Long DEFAULT_GROUP_ID = 4L;
    List<String> DEFAULT_PACKAGE_IDS = Collections.singletonList(DEFAULT_PACKAGE_ID.toString());
    List<String> DEFAULT_PORTS = Collections.singletonList(DEFAULT_PORT);
    List<String> DEFAULT_FIELDS = Collections.singletonList(DEFAULT_FIELD);
    Status DEFAULT_PROBE_STATUS = Status.SHIPPED;
    ProbePurpose DEFAULT_PROBE_PURPOSE = ProbePurpose.SITE;
    ProbeType DEFAULT_PROBE_TYPE = ProbeType.OPEN_NETTEST_BEATBOX_M;
    String DEFAULT_PROBE_TYPE_NAME = "DEFAULT_PROBE_TYPE_NAME";
    String DEFAULT_PROBE_TYPE_URL = "DEFAULT_PROBE_TYPE_URL";
    Integer DEFAULT_NUMBER_FIXED_PORTS = 6;
    Integer DEFAULT_NUMBER_MOBILE_PORTS = 2;
    Integer DEFAULT_TIMEZONE_OFFSET = 0;
    ServerNetworkType DEFAULT_SERVER_TYPE = ServerNetworkType.ON_NET;
    Long DEFAULT_SITE_ID = 1L;
    String DEFAULT_SITE_ADVERTISED_ID = "DEFAULT_SITE_ADVERTISED_ID";
    String DEFAULT_SITE_NAME = "DEFAULT_SITE_NAME";
    String DEFAULT_SITE_ADDRESS = "DEFAULT_SITE_ADDRESS";
    String DEFAULT_OPERATOR = "DEFAULT_OPERATOR";
    String DEFAULT_COMMENT = "DEFAULT_COMMENT";
    Integer DEFAULT_COUNT = 2;
    Float DEFAULT_COORDINATES_LATITUDE = 123.123F;
    Float DEFAULT_COORDINATES_LONGITUDE = 321.321F;
    Double DEFAULT_LATITUDE = 123.123D;
    Double DEFAULT_LATITUDE_SECOND = 15D;
    Double DEFAULT_LONGITUDE = 321.321D;
    Double DEFAULT_LONGITUDE_SECOND = 30D;
    Long DEFAULT_LIVE_TIME = 12345L;
    String DEFAULT_PROBE_PORT = "2M";
    String DEFAULT_IP = "123.12.12.12";
    String DEFAULT_IP_FOR_PROVIDER = "176.37.47.123";
    String DEFAULT_PROBE_PORT_INT = "sim2";
    String DEFAULT_LANGUAGE = "en";
    String DEFAULT_UUID = "540607f2-2a43-4019-af89-fa6d42b9a14f";
    String DEFAULT_MOBILE_MEASUREMENT_TOKEN = DEFAULT_OPEN_TEST_UUID_STRING + "_1589301534_8ar2pr6yrs2pdeH3c0Xjp357rRw=";
    Long DEFAULT_MEASUREMENT_SERVER_ID = 1L;
    String DEFAULT_TEST_RESULT_URL = "http://localhost:8080/testRequest";
    String DEFAULT_QOS_TEST_RESULT_URL = "http://localhost:8080/QosTestRequest";
    Integer DEFAULT_TEST_DURATION = 5;
    String DEFAULT_MEASUREMENT_SERVER_NAME = "EKIP Server (Podgorica)";
    String DEFAULT_MEASUREMENT_SERVER_CITY = "DEFAULT_MEASUREMENT_SERVER_CITY";
    Integer DEFAULT_TEST_WEIGHT = 0;
    String DEFAULT_MEASUREMENT_SERVER_ADDRESS = "ekip-m01.customers.nettest.org";
    Integer DEFAULT_NUM_TEST_THREADS = 20;
    Integer DEFAULT_NUM_TEST_THREADS_UL = 20;
    Integer DEFAULT_MEASUREMENT_SERVER_PORT = 442;
    Integer DEFAULT_MEASUREMENT_SERVER_PORT_SSL = 5231;
    Boolean DEFAULT_IS_MEASUREMENT_SERVER_ENCRYPTED = true;
    String DEFAULT_MEASUREMENT_SERVER_TOKEN = "22c4b536-ab0f-4fcc-b5eb-9b8052335438_1589282586_2/jcdrb5OeSeaS9n2tXWOXBZSM0=";
    Integer DEFAULT_TEST_NUM_PINGS = 10;
    Long DEFAULT_TEST_ID = 12615919L;
    String DEFAULT_TOKEN = "f934d7b3-ce36-44c0-9a80-1d3fecb674a0_1589301534_8ar2pr6yrs2pdeH3c0Xjp357rRw=";
    Integer DEFAULT_NETWORK_TYPE = 98;
    String DEFAULT_PROVIDER_NAME = "BATELCO";
    String DEFAULT_PROVIDER_NAME_SECOND = "ABATELCO";
    String DEFAULT_PROVIDER_NAME_THIRD = "CBATELCO";
    String DEFAULT_GEO_PROVIDER_NAME = "LANETUA-AS, UA";
    MeasurementServerType DEFAULT_CLIENT = MeasurementServerType.RMBTws;
    Map<String, String> DEFAULT_HEADER = new HashMap<>();
    Long DEFAULT_ASN = 39608L;
    ProbePortStatus DEFAULT_PORT_STATUS = ProbePortStatus.UP;
    String DEFAULT_TENANT = "sah";
    String DEFAULT_PACKAGE_ADVERTISED_NAME = "package name";
    long DEFAULT_TODAY_AMOUNT_OF_MEASUREMENTS = 123L;
    long DEFAULT_THIS_WEEK_AMOUNT_OF_MEASUREMENTS = 1234L;
    long DEFAULT_THIS_MONTH_AMOUNT_OF_MEASUREMENTS = 12345L;
    long DEFAULT_THIS_YEAR_AMOUNT_OF_MEASUREMENTS = 123456L;
    long DEFAULT_PACKAGE_COUNTER = 5L;
    long DEFAULT_PROBE_COUNTER = 6L;
    long DEFAULT_PORT_COUNTER = 7L;
    long DEFAULT_STATUS_COUNTER = 8L;
    String DEFAULT_AD_HOC_CAMPAIGN = "DefaultAdHocCampaign";
    String DEFAULT_AD_HOC_CAMPAIGN_UUID_FIRST = "c32436fa-2a01-11ec-9621-0242ac130002";
    String DEFAULT_AD_HOC_CAMPAIGN_UUID_SECOND = "b9f40a4e-2a1c-11ec-8d3d-0242ac130003";
    String DEFAULT_AD_HOC_CAMPAIGN_2 = "DefaultAdHocCampaign2";
    String DEFAULT_AD_HOC_CAMPAIGN_DOWNTIME_CREATE_DATE1 = "22-01-2020 10:14:00 AM";
    String DEFAULT_AD_HOC_CAMPAIGN_DOWNTIME_CREATE_DATE2 = "22-01-2020 10:15:00 AM";
    String DEFAULT_AD_HOC_CAMPAIGN_START_FIRST = "2020-01-21 10:15:00";
    String DEFAULT_AD_HOC_CAMPAIGN_START_SECOND = "2021-02-12 12:15:00";
    Timestamp DEFAULT_AD_HOC_CAMPAIGN_START_TIMESTAMP_FIRST = Timestamp.valueOf(DEFAULT_AD_HOC_CAMPAIGN_START_FIRST);
    Timestamp DEFAULT_AD_HOC_CAMPAIGN_START_TIMESTAMP_SECOND = Timestamp.valueOf(DEFAULT_AD_HOC_CAMPAIGN_START_SECOND);
    String DEFAULT_AD_HOC_CAMPAIGN_FINISH_FIRST = "2020-02-23 10:15:00";
    String DEFAULT_AD_HOC_CAMPAIGN_FINISH_SECOND = "2021-12-23 13:15:00";
    String DEFAULT_DOWNTIME_STATUS_CHANGED = "2021-12-23 13:15:00";
    Timestamp DEFAULT_AD_HOC_CAMPAIGN_FINISH_TIMESTAMP_FIRST = Timestamp.valueOf(DEFAULT_AD_HOC_CAMPAIGN_FINISH_FIRST);
    Timestamp DEFAULT_AD_HOC_CAMPAIGN_FINISH_TIMESTAMP_SECOND = Timestamp.valueOf(DEFAULT_AD_HOC_CAMPAIGN_FINISH_SECOND);
    Timestamp DEFAULT_DOWNTIME_STATUS_CHANGED_TIMESTAMP = Timestamp.valueOf(DEFAULT_DOWNTIME_STATUS_CHANGED);
    String DEFAULT_NEWS_TITLE_DE = "DEFAULT_NEWS_TITLE_DE";
    String DEFAULT_NEWS_TEXT_DE = "DEFAULT_NEWS_TEXT_DE";
    String DEFAULT_NEWS_TITLE_EN = "DEFAULT_NEWS_TITLE_EN";
    String DEFAULT_NEWS_TEXT_EN = "DEFAULT_NEWS_TEXT_EN";
    Long DEFAULT_LAST_NEWS_UID = 13L;
    String DEFAULT_PLATFORM = "DEFAULT PLATFORM";
    Long DEFAULT_SOFTWARE_VERSION_CODE = 1L;
    String DEFAULT_NEWS_UUID = "8fa5fb1c-5bf6-11eb-ae93-0242ac130002";
    Long DEFAULT_UID = 2L;
    String DEFAULT_NEWS_TITLE = "DEFAULT NEWS TITLE";
    String DEFAULT_NEWS_TEXT = "DEFAULT NEWS TEXT";
    Long DEFAULT_MAX_SOFTWARE_VERSION = 5L;
    Long DEFAULT_MIN_SOFTWARE_VERSION = 3L;
    String DEFAULT_TEST_SERVER_NAME = "DEFAULT TEST SERVER NAME";
    String DEFAULT_TEST_SERVER_WS_NAME = "DEFAULT TEST SERVER WS NAME";
    String DEFAULT_TEST_SERVER_QOS_NAME = "DEFAULT TEST SERVER QOS NAME";
    String DEFAULT_QOS_TEST_TYPE_DESC_NAME = "DEFAULT QOS TEST TYPE DESC NAME";
    QosMeasurement DEFAULT_QOS_MEASUREMENT = QosMeasurement.DNS;
    TestType DEFAULT_TEST_TYPE = TestType.DNS;
    Long DEFAULT_TERM_AND_CONDITION_VERSION = 2L;
    Long DEFAULT_TERM_AND_CONDITION_VERSION_IOS = 3L;
    Long DEFAULT_TERM_AND_CONDITION_VERSION_ANDROID = 5L;
    String DEFAULT_TERM_AND_CONDITION_URL = "DEFAULT TERM AND CONDITION URL";
    String DEFAULT_TERM_AND_CONDITION_URL_ANDROID = "DEFAULT_TERM_AND_CONDITION_URL_ANDROID";
    String DEFAULT_TERM_AND_CONDITION_URL_IOS = "DEFAULT_TERM_AND_CONDITION_URL_IOS";
    String DEFAULT_TERM_AND_CONDITION_NDT_URL = "DEFAULT TERM AND CONDITION NDT URL";
    String DEFAULT_URLS_URL_SHARE = "DEFAULT URLS URL SHARE";
    String DEFAULT_URLS_IPV6_CHECK = "DEFAULT URLS IPV6 CHECK";
    String DEFAULT_URLS_CONTROL_IPV4_ONLY = "DEFAULT URLS CONTROL IPV4 ONLY";
    String DEFAULT_URLS_OPEN_DATA_PREFIX = "DEFAULT URLS OPEN DATA PREFIX";
    String DEFAULT_URLS_URL_MAP_SERVER = "DEFAULT URLS URL MAP SERVER";
    String DEFAULT_URLS_URL_IPV4_CHECK = "DEFAULT URLS URL IPV4 CHECK";
    String DEFAULT_URLS_URL_IPV6_CHECK = "DEFAULT URLS IPV6 CHECK";
    String DEFAULT_MEASUREMENT_REQUEST_RESULT_URL = "DEFAULT_MEASUREMENT_REQUEST_RESULT_URL";
    String DEFAULT_MEASUREMENT_REQUEST_RESULT_QOS_URL = "DEFAULT_MEASUREMENT_REQUEST_RESULT_QOS_URL";
    String DEFAULT_MEASUREMENT_REQUEST_MEASUREMENT_DURATION = "21";
    String DEFAULT_MEASUREMENT_REQUEST_MEASUREMENT_NUM_THREADS_IOS = "2";
    String DEFAULT_MEASUREMENT_REQUEST_MEASUREMENT_NUM_THREADS_ANDROID = "28";
    String DEFAULT_MEASUREMENT_REQUEST_MEASUREMENT_NUM_THREADS_WEB = "17";
    String DEFAULT_MEASUREMENT_REQUEST_MEASUREMENT_NUM_PINGS = "10";
    String DEFAULT_SIGNAL_TEST_REQUEST_RESULT_URL = "DEFAULT_SIGNAL_TEST_REQUEST_URL";
    String DEFAULT_CAMPAIGN_DURATION_DAYS = "200000000";
    String DEFAULT_URLS_CONTROL_IPV6_ONLY = "DEFAULT URLS CONTROL IPV6 ONLY";
    String DEFAULT_URLS_STATISTICS = "DEFAULT URLS STATISTICS";
    String DEFAULT_SERVER_UUID = "DEFAULT_SERVER_UUID";
    String DEFAULT_SERVER_WS_UUID = "DEFAULT_SERVER_WS_UUID";
    String DEFAULT_SERVER_QOS_UUID = "DEFAULT_SERVER_QOS_UUID";
    String DEFAULT_HISTORY_DEVICE = "DEFAULT HISTORY DEVICE";
    String DEFAULT_HISTORY_NETWORK = "DEFAULT HISTORY NETWORK";
    String DEFAULT_CLIENT_UUID_STRING = "9e1ccb36-59e3-11eb-ae93-0242ac130002";
    UUID DEFAULT_CLIENT_UUID = UUID.fromString(DEFAULT_CLIENT_UUID_STRING);
    Long DEFAULT_MAP_SERVER_PORT = 443L;
    String DEFAULT_MAP_SERVER_HOST = "DEFAULT MAP SERVER HOST";
    boolean DEFAULT_FLAG_TRUE = true;
    String DEFAULT_TC_URL_ANDROID_VALUE = "DEFAULT_TC_URL_ANDROID_VALUE";
    String DEFAULT_TC_NDT_URL_ANDROID_VALUE = "DEFAULT_TC_NDT_URL_ANDROID_VALUE";
    String DEFAULT_TC_VERSION_ANDROID_VALUE = Long.valueOf(5).toString();
    String DEFAULT_TC_URL_IOS_VALUE = "DEFAULT_TC_URL_IOS_VALUE";
    String DEFAULT_TC_VERSION_IOS_VALUE = Long.valueOf(3).toString();
    String DEFAULT_TC_VERSION_VALUE = Long.valueOf(9).toString();
    String DEFAULT_TC_URL_VALUE = "DEFAULT_TC_URL_VALUE";
    String DEFAULT_ANDROID_PLATFORM = "ANDROID";
    String DEFAULT_IOS_PLATFORM = "IOS";
    String DEFAULT_SETTINGS_KEY = "tc_url";
    String DEFAULT_SETTINGS_KEY_SECOND = "DEFAULT_SETTINGS_KEY_SECOND";
    String DEFAULT_SETTINGS_VALUE = "DEFAULT_SETTINGS_VALUE";
    String DEFAULT_SETTINGS_VALUE_SECOND = "DEFAULT_SETTINGS_VALUE_SECOND";
    Long DEFAULT_TIME = 1650458564491L;
    String DEFAULT_RESULT_URL = "DEFAULT_RESULT_URL";
    String DEFAULT_URL = "DEFAULT_RESULT_URL";
    Double DEFAULT_TEST_SERVER_LATITUDE = 56.9;
    Double DEFAULT_TEST_SERVER_LONGITUDE = 87.3;
    ClientType DEFAULT_CLIENT_TYPE = ClientType.MOBILE;
    String DEFAULT_TEXT = "DEFAULT TEXT";
    String DEFAULT_CLIENT_NAME = "RMBT";
    String DEFAULT_TC_URL_ANDROID_V4_VALUE = "DEFAULT_TC_URL_ANDROID_V4_VALUE";
    String DEFAULT_OPEN_DATA_FILE_EXTENSION = "csv";
    String DEFAULT_OPEN_DATA_YEAR = "2021";
    String DEFAULT_OPEN_DATA_MONTH = "12";
    Date DEFAULT_DATE = new Date(LocalDate.of(2020, 3, 20).toEpochDay());
    long DEFAULT_LAST_DOWN_PING = 30L;
    Long DEFAULT_PING_SHORTEST = 1000L;
    Long DEFAULT_DOWNLOAD_DURATION_NANOS = 1000000000L;
    Long DEFAULT_UPLOAD_DURATION_NANOS = 2000000000L;
    Long DEFAULT_TEST_BYTES_DOWNLOAD = 560L;
    Long DEFAULT_TEST_BYTES_UPLOAD = 1320L;
    Integer DEFAULT_SPEED_DOWNLOAD = Math.toIntExact(Math.round(((double) DEFAULT_TEST_BYTES_DOWNLOAD / (double) DEFAULT_DOWNLOAD_DURATION_NANOS * 1e9 * 8.0) / 1e3));
    Integer DEFAULT_SPEED_UPLOAD = Math.toIntExact(Math.round(((double) DEFAULT_TEST_BYTES_UPLOAD / (double) DEFAULT_UPLOAD_DURATION_NANOS * 1e9 * 8.0) / 1e3));
    String DEFAULT_DEVICE = "DEFAULT_DEVICE";

    String DEFAULT_MARKETING_NAME = "DEFAULT_DEVICE";
    String DEFAULT_TAG = "DEFAULT_TAG";
    String DEFAULT_MODEL = "DEFAULT_MODEL";
    Platform DEFAULT_PLATFORM_MOBILE = Platform.ANDROID;
    String DEFAULT_PRODUCT = "DEFAULT_PRODUCT";
    String DEFAULT_WIFI_SSID = "DEFAULT_WIFI_SSID";
    String DEFAULT_WIFI_BSSID = "DEFAULT_WIFI_BSSID";
    Integer DEFAULT_SIM_COUNT = 1;
    MeasurementStatus DEFAULT_STATUS = MeasurementStatus.FINISHED;
    Integer DEFAULT_SLOT = 5;
    Integer DEFAULT_WAIT = 13;
    String DEFAULT_TEST_SET_VERSION = "3.0";
    Long DEFAULT_TEST_SERVER_ID = 5L;

    Integer DEFAULT_CLASSIFICATION_COUNT = 2;
    String DEFAULT_TEST_RESULT_RESPONSE_SHARE_SUBJECT = "NetTest result - Jul 15, 2022, 12:25:45 PM";
    String DEFAULT_TEST_RESULT_RESPONSE_SHARE_TEXT_DUAL_SIM_TRUE_SIGNAL_STRENGTH_NOT_NULL = "My Result:\n" +
            "Date/time: Jul 15, 2022, 12:25:45 PM\n" +
            "Download: 2,100 Mbps\n" +
            "Upload: 1,000 Mbps\n" +
            "Ping: 58 ms\n" +
            "Signal strength: -111 dBm\n" +
            "Network type: Dual SIM\n" +
            "Platform: \n" +
            "Model: \n" +
            "https://nettest.com/en/test/results/6136c67b-c1ad-4c2f-9c86-39328c5dbf35\n" +
            "\n";
    String DEFAULT_TEST_RESULT_RESPONSE_SHARE_TEXT_SHARE_URL = "My Result:\n" +
            "Date/time: Jul 15, 2022, 12:25:45 PM\n" +
            "Download: 2,100 Mbps\n" +
            "Upload: 1,000 Mbps\n" +
            "Ping: 58 ms\n" +
            "Signal strength: -111 dBm\n" +
            "Network type: Dual SIM\n" +
            "Platform: \n" +
            "Model: \n" +
            "DEFAULT TEXT6136c67b-c1ad-4c2f-9c86-39328c5dbf35\n" +
            "\n";
    String DEFAULT_TEST_RESULT_RESPONSE_SHARE_TEXT_DUAL_SIM_FALSE_LTE_RSRP_NOT_NULL = "My Result:\n" +
            "Date/time: Jul 15, 2022, 12:25:45 PM\n" +
            "Download: 2,100 Mbps\n" +
            "Upload: 1,000 Mbps\n" +
            "Ping: 58 ms\n" +
            "Signal strength (RSRP): -5 dBm\n" +
            "Network type: 4G (LTE)\n" +
            "Operator: DEFAULT_TELEPHONY_NETWORK_OPERATOR_NAME\n" +
            "Mobile network: DEFAULT_TELEPHONY_NETWORK_OPERATOR_NAME\n" +
            "Platform: \n" +
            "Model: \n" +
            "https://nettest.com/en/test/results/6136c67b-c1ad-4c2f-9c86-39328c5dbf35\n" +
            "\n";
    Integer DEFAULT_TEST_RESULT_MEASUREMENT_RESPONSE_CLASSIFICATION = 2;
    Integer DEFAULT_TEST_RESULT_MEASUREMENT_RESPONSE_SIGNAL_STRENGTH_CLASSIFICATION = 1;
    String DEFAULT_TEST_RESULT_MEASUREMENT_RESPONSE_SIGNAL_STRENGTH_TITLE = "Signal";
    String DEFAULT_TEST_RESULT_MEASUREMENT_RESPONSE_SIGNAL_LTE_RSRP_TITLE = "Signal (RSRP)";
    String DEFAULT_TEST_RESULT_MEASUREMENT_RESPONSE_DOWNLOAD_TITLE = "Download";
    String DEFAULT_TEST_RESULT_MEASUREMENT_RESPONSE_SIGNAL_UPLOAD_TITLE = "Upload";
    String DEFAULT_TEST_RESULT_MEASUREENT_RESPONSE_SIGNAL_PING_TITLE = "Ping";
    Integer DEFAULT_NEXT_TEST_SLOT = 4;
    Long DEFAULT_TIME_INSTANT = 1657880745454L;
    Timestamp DEFAULT_TIMESTAMP = new Timestamp(DEFAULT_TIME_INSTANT);
    String DEFAULT_TIMESTAMP_STRING = DEFAULT_TIMESTAMP.toString();
    String DEFAULT_MEASUREMENT_DATE = ZonedDateTime.ofInstant(TestConstants.DEFAULT_TIMESTAMP.toInstant(), ZoneId.of(TestConstants.DEFAULT_TIME_ZONE)).toString();
    Integer DEFAULT_TEST_SIM_COUNT = 1;
    Integer DEFAULT_RESULT_DOWNLOAD_SPEED = 2122000;
    Integer DEFAULT_RESULT_UPLOAD_SPEED = 1020032;
    Integer DEFAULT_SIGNAL_STRENGTH_FIRST = -111;
    Integer DEFAULT_SIGNAL_STRENGTH_SECOND = -98;
    String DEFAULT_TEST_RESULT_DETAIL_SIGNAL_STRENGTH_VALUE = DEFAULT_SIGNAL_STRENGTH_FIRST + " dBm";
    String DEFAULT_TEST_RESULT_DETAIL_TIME_STRING = "Jul 15, 2022, 12:25:45 PM";
    String DEFAULT_TEST_RESULT_DETAIL_OPEN_TEST_UUID = "O6136c67b-c1ad-4c2f-9c86-39328c5dbf35";
    NetworkType DEFAULT_NETWORK_TYPE_LTE = NetworkType.LTE;
    String DEFAULT_NETWORK_TYPE_LTE_NAME = "LTE";
    String DEFAULT_NETWORK_TYPE_LTE_CATEGORY = "4G";
    String DEFAULT_NETWORK_TYPE_LTE_VALUE = "LTE";
    String DEFAULT_NETWORK_TYPE_LTE_RESULT_DETAIL_RESPONSE_VALUE = "4G (LTE)";
    String DEFAULT_NET_ITEM_RESPONSE_NETWORK_TYPE_TITLE = "Connection";
    String DEFAULT_NETWORK_TYPE_TITLE = "Network type";
    String DEFAULT_NET_ITEM_RESPONSE_WIFI_SSID_TITLE = "WLAN SSID";
    String DEFAULT_NET_ITEM_RESPONSE_OPERATOR_NAME_TITLE = "Operator";
    String DEFAULT_NET_ITEM_RESPONSE_ROAMING_TITLE = "Roaming";
    String DEFAULT_TEST_RESULT_DETAIL_PING_MEDIAN_VALUE = "58 ms";
    String DEFAULT_TEST_RESULT_DETAIL_WIFI_LINK_SPEED = "100 Mbps";
    String DEFAULT_TEST_RESULT_DETAIL_TOTAL_BYTES = "0.56 MB";
    String DEFAULT_TEST_RESULT_DETAIL_TOTAL_BYTES_IF = "0.11 MB";
    String DEFAULT_TEST_RESULT_DETAIL_TEST_DL_IF_BYTES_DOWNLOAD = "0.13 MB";
    String DEFAULT_TEST_RESULT_DETAIL_TEST_DL_IF_BYTES_UPLOAD = "0.27 MB";
    String DEFAULT_TEST_RESULT_DETAIL_TEST_UL_IF_BYTES_DOWNLOAD = "0.33 MB";
    String DEFAULT_TEST_RESULT_DETAIL_TEST_UL_IF_BYTES_UPLOAD = "2.2 MB";
    String DEFAULT_TEST_RESULT_DETAIL_TIME_DL = "0.002 s";
    String DEFAULT_TEST_RESULT_DETAIL_TIME_UL = "0.004 s";
    String DEFAULT_TEST_RESULT_DETAIL_DURATION_DL = "18 s";
    String DEFAULT_TEST_RESULT_DETAIL_DURATION_UL = "17 s";
    String DEFAULT_TEST_RESULT_DETAIL_DURATION = "1000 s";
    String DEFAULT_TEST_LOCATION_LINK_NAME = "DEFAULT_TEST_LOCATION_LINK_NAME";
    String DEFAULT_TEST_RESULT_DETAIL_LOCATION = "N 56°54.000'  E 87°18.000' (DEFAULT_PROVIDER, +/- 19 m)";
    String DEFAULT_TEST_RESULT_DETAIL_MOTION = "14 m";
    String DEFAULT_TEST_RESULT_DETAIL_GEO_ALTITUDE = "33 m";
    String DEFAULT_TEST_RESULT_DETAIL_GEO_SPEED = "94 km/h";
    String DEFAULT_TEST_RESULT_DETAIL_DTM_LEVEL = "12 m";
    String DEFAULT_TEST_RESULT_DETAIL_LAND_COVER = "100 (Artificial surfaces)";
    String DEFAULT_TEST_RESULT_DETAIL_SETTLEMENT_TYPE = "3 (settlement area)";
    String DEFAULT_TEST_RESULT_DETAIL_SPEED_DOWNLOAD_NDT = "0.23 Mbps";
    String DEFAULT_TEST_RESULT_DETAIL_SPEED_UPLOAD_NDT = "1.2 Mbps";
    String DEFAULT_TEST_RESULT_DETAIL_FREQUENCY_DL = "940 MHz";
    String DEFAULT_TEST_RESULT_DETAIL_RADIO_BAND = "8 (GSM 900)";
    String DEFAULT_TEST_RESULT_MEASUREMENT_RESPONSE_SIGNAL_PING_TITLE = "Ping";
    String DEFAULT_TEST_RESULT_DETAIL_SPEED_UPLOAD_VALUE = "1,000 Mbps";
    String DEFAULT_TEST_RESULT_DETAIL_SPEED_DOWNLOAD_VALUE = "2,100 Mbps";
    Integer DEFAULT_LTE_RSRP_FIRST = -5;
    Integer DEFAULT_LTE_RSRP_SECOND = -7;
    String DEFAULT_TELEPHONY_NETWORK_OPERATOR_NAME = "DEFAULT_TELEPHONY_NETWORK_OPERATOR_NAME";
    Integer DEFAULT_NETWORK_TYPE_LTE_ID = 13;
    QoeCategory DEFAULT_QOE_CATEGORY = QoeCategory.STREAMING_AUDIO_STREAMING;
    Integer DEFAULT_QOE_CLASSIFICATION = 4;
    Double DEFAULT_QUALITY = 1d;
    String DEFAULT_TEST_NDT_MAIN = "DEFAULT_TEST_NDT_MAIN";
    String DEFAULT_TEST_NDT_STAT = "DEFAULT_TEST_NDT_STAT";
    String DEFAULT_TEST_NDT_DIAG = "DEFAULT_TEST_NDT_DIAG";
    Integer DEFAULT_LTE_RSRQ_FIRST = -11;
    Integer DEFAULT_LTE_RSRQ_SECOND = -9;
    Double DEFAULT_ALTITUDE = 33.3;
    Double DEFAULT_SPEED = 26.0;
    String DEFAULT_TEST_RESULT_DETAIL_OPEN_TEST_UUID_TITLE = "Open Test ID";
    String DEFAULT_TEST_RESULT_DETAIL_OPEN_UUID = "P04cb9392-846b-11eb-8dcd-0242ac130003";
    String DEFAULT_TEST_RESULT_DETAIL_OPEN_UUID_TITLE = "Open User ID";
    String DEFAULT_TEST_RESULT_DETAIL_TIMEZONE = "UTC+1h";
    Platform DEFAULT_TEST_PLATFORM = Platform.ANDROID;
    MeasurementServerType DEFAULT_TEST_SERVER_SERVER_TYPE = MeasurementServerType.RMBT;
    String DEFAULT_CLIENT_VERSION = "0.1";
    String DEFAULT_APP_VERSION = "0.2";
    String DEFAULT_TEST_RESULT_DETAIL_SIGNAL_RSRQ_VALUE = DEFAULT_LTE_RSRQ_FIRST + " dB";
    String DEFAULT_TEST_RESULT_DETAIL_SIGNAL_RSRP_VALUE = DEFAULT_LTE_RSRP_FIRST + " dBm";
    Float DEFAULT_OVERALL_QOS = 0.5F;
    Float DEFAULT_OVERALL_QOS_PERCENTAGE = 50F;
    Integer DEFAULT_CONCURRENCY_GROUP = 15;
    String DEFAULT_CALL_DURATION = "100000000";
    String DEFAULT_CONN_TIMEOUT = "5000000";
    String DEFAULT_DOWNLOAD_TIMEOUT = "3000000";
    String DEFAULT_HOST = "DEFAULT_HOST";
    String DEFAULT_IN_NUM_PACKETS = "2";
    String DEFAULT_IN_PORT = "5060";
    String DEFAULT_OUT_NUM_PACKETS = "3";
    String DEFAULT_OUT_PORT = "5061";
    String DEFAULT_RANGE = "300";
    String DEFAULT_RECORD = "A";
    String DEFAULT_REQUEST = "GET / HTTR/7.9";
    String DEFAULT_RESOLVER = "8.8.8.8";
    String DEFAULT_TIMEOUT = "7000000";
    String DEFAULT_TEST_DESCRIPTION = "DEFAULT_TEST_DESCRIPTION";
    Integer DEFAULT_SUCCESS_COUNT = 1;
    Integer DEFAULT_FAILURE_COUNT = 0;
    Integer DEFAULT_TOTAL_COUNT = 1;
    String DEFAULT_TEST_SUMMARY = "DEFAULT_TEST_SUMMARY";
    String DEFAULT_TEST_RESULT_KEY = "traceroute.success";
    String DEFAULT_TEST_RESULT_VALUE = "ok";
    String DEFAULT_RESULT = "{\"" + DEFAULT_TEST_RESULT_KEY + "\":\"" + DEFAULT_TEST_RESULT_VALUE + "\"}";
    Integer DEFAULT_SPEED_DETAILS_BYTES_FIRST = 1230;
    String DEFAULT_SPEED_DETAILS_STRING = "[{\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 32323400,\n" +
            "              \"bytes\" : 4096,\n" +
            "              \"thread\" : 2\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 32769600,\n" +
            "              \"bytes\" : 4096,\n" +
            "              \"thread\" : 1\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 39011800,\n" +
            "              \"bytes\" : 4096,\n" +
            "              \"thread\" : 0\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 63147986,\n" +
            "              \"bytes\" : 4096,\n" +
            "              \"thread\" : 1\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 66107124,\n" +
            "              \"bytes\" : 4096,\n" +
            "              \"thread\" : 2\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 75108982,\n" +
            "              \"bytes\" : 4096,\n" +
            "              \"thread\" : 0\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 133416900,\n" +
            "              \"bytes\" : 69632,\n" +
            "              \"thread\" : 2\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 134185800,\n" +
            "              \"bytes\" : 86016,\n" +
            "              \"thread\" : 1\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 139455600,\n" +
            "              \"bytes\" : 81920,\n" +
            "              \"thread\" : 0\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 173899708,\n" +
            "              \"bytes\" : 69632,\n" +
            "              \"thread\" : 2\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 174807349,\n" +
            "              \"bytes\" : 122880,\n" +
            "              \"thread\" : 1\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 177446098,\n" +
            "              \"bytes\" : 61440,\n" +
            "              \"thread\" : 0\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 235269200,\n" +
            "              \"bytes\" : 176128,\n" +
            "              \"thread\" : 1\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 239840000,\n" +
            "              \"bytes\" : 151552,\n" +
            "              \"thread\" : 2\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 241586000,\n" +
            "              \"bytes\" : 163840,\n" +
            "              \"thread\" : 0\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 276762854,\n" +
            "              \"bytes\" : 135168,\n" +
            "              \"thread\" : 2\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 277838063,\n" +
            "              \"bytes\" : 118784,\n" +
            "              \"thread\" : 0\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 280133213,\n" +
            "              \"bytes\" : 188416,\n" +
            "              \"thread\" : 1\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 344746600,\n" +
            "              \"bytes\" : 225280,\n" +
            "              \"thread\" : 0\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 349045500,\n" +
            "              \"bytes\" : 253952,\n" +
            "              \"thread\" : 1\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 352725400,\n" +
            "              \"bytes\" : 217088,\n" +
            "              \"thread\" : 2\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 377237169,\n" +
            "              \"bytes\" : 184320,\n" +
            "              \"thread\" : 2\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 378984129,\n" +
            "              \"bytes\" : 163840,\n" +
            "              \"thread\" : 0\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 382061940,\n" +
            "              \"bytes\" : 237568,\n" +
            "              \"thread\" : 1\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 450074300,\n" +
            "              \"bytes\" : 335872,\n" +
            "              \"thread\" : 1\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 458366100,\n" +
            "              \"bytes\" : 282624,\n" +
            "              \"thread\" : 0\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 464645900,\n" +
            "              \"bytes\" : 299008,\n" +
            "              \"thread\" : 2\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 480133455,\n" +
            "              \"bytes\" : 241664,\n" +
            "              \"thread\" : 2\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 482538944,\n" +
            "              \"bytes\" : 221184,\n" +
            "              \"thread\" : 0\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 484144985,\n" +
            "              \"bytes\" : 290816,\n" +
            "              \"thread\" : 1\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 557959900,\n" +
            "              \"bytes\" : 421888,\n" +
            "              \"thread\" : 1\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 561597900,\n" +
            "              \"bytes\" : 389120,\n" +
            "              \"thread\" : 0\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 571059900,\n" +
            "              \"bytes\" : 389120,\n" +
            "              \"thread\" : 2\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 584328281,\n" +
            "              \"bytes\" : 299008,\n" +
            "              \"thread\" : 2\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 584925791,\n" +
            "              \"bytes\" : 278528,\n" +
            "              \"thread\" : 0\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 595539019,\n" +
            "              \"bytes\" : 352256,\n" +
            "              \"thread\" : 1\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 665210000,\n" +
            "              \"bytes\" : 499712,\n" +
            "              \"thread\" : 1\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 669424500,\n" +
            "              \"bytes\" : 475136,\n" +
            "              \"thread\" : 0\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 673385600,\n" +
            "              \"bytes\" : 475136,\n" +
            "              \"thread\" : 2\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 685817788,\n" +
            "              \"bytes\" : 348160,\n" +
            "              \"thread\" : 2\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 686258558,\n" +
            "              \"bytes\" : 327680,\n" +
            "              \"thread\" : 0\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"upload\",\n" +
            "              \"time\" : 697555855,\n" +
            "              \"bytes\" : 405504,\n" +
            "              \"thread\" : 1\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 771375600,\n" +
            "              \"bytes\" : 573440,\n" +
            "              \"thread\" : 1\n" +
            "            },\n" +
            "            {\n" +
            "              \"direction\" : \"download\",\n" +
            "              \"time\" : 773800100,\n" +
            "              \"bytes\" : 552960,\n" +
            "              \"thread\" : 2\n" +
            "            }]";
    String DEFAULT_MOBILE_GRAPH_RESPONSE = "MobileGraphResponse(speedCurve=SpeedCurve(download=[MobileGraph(bytesTotal=102292, timeElapsed=74), MobileGraph(bytesTotal=149369, timeElapsed=96), MobileGraph(bytesTotal=243572, timeElapsed=136), MobileGraph(bytesTotal=337888, timeElapsed=176), MobileGraph(bytesTotal=390805, timeElapsed=198), MobileGraph(bytesTotal=481739, timeElapsed=240), MobileGraph(bytesTotal=571627, timeElapsed=282), MobileGraph(bytesTotal=615706, timeElapsed=306), MobileGraph(bytesTotal=697588, timeElapsed=348), MobileGraph(bytesTotal=782121, timeElapsed=391), MobileGraph(bytesTotal=831491, timeElapsed=415), MobileGraph(bytesTotal=929277, timeElapsed=457), MobileGraph(bytesTotal=1027562, timeElapsed=499), MobileGraph(bytesTotal=1090467, timeElapsed=523), MobileGraph(bytesTotal=1194514, timeElapsed=564), MobileGraph(bytesTotal=1297310, timeElapsed=605), MobileGraph(bytesTotal=1351386, timeElapsed=628), MobileGraph(bytesTotal=1432366, timeElapsed=670), MobileGraph(bytesTotal=1509237, timeElapsed=711)], upload=[MobileGraph(bytesTotal=112186, timeElapsed=111), MobileGraph(bytesTotal=161466, timeElapsed=133), MobileGraph(bytesTotal=246409, timeElapsed=176), MobileGraph(bytesTotal=328605, timeElapsed=216), MobileGraph(bytesTotal=367413, timeElapsed=237), MobileGraph(bytesTotal=433327, timeElapsed=278), MobileGraph(bytesTotal=498800, timeElapsed=318), MobileGraph(bytesTotal=528893, timeElapsed=339), MobileGraph(bytesTotal=590648, timeElapsed=380), MobileGraph(bytesTotal=652470, timeElapsed=420), MobileGraph(bytesTotal=687300, timeElapsed=442), MobileGraph(bytesTotal=754911, timeElapsed=483), MobileGraph(bytesTotal=821732, timeElapsed=523), MobileGraph(bytesTotal=859758, timeElapsed=546), MobileGraph(bytesTotal=923928, timeElapsed=587), MobileGraph(bytesTotal=987664, timeElapsed=627), MobileGraph(bytesTotal=1019278, timeElapsed=650)]))";
    String DEFAULT_COUNTRY = "Norway";
    String DEFAULT_COUNTY = "Oslo";
    String DEFAULT_CITY = "Oslo";
    String DEFAULT_POSTAL_CODE = "400000";
    int DEFAULT_TCP_RESULT_SUCCESS_COUNT = 2;
    int DEFAULT_TCP_RESULT_FAILED_COUNT = 4;
    float DEFAULT_TCP_RESULT_RATE = (float) DEFAULT_TCP_RESULT_SUCCESS_COUNT / (DEFAULT_TCP_RESULT_SUCCESS_COUNT + DEFAULT_TCP_RESULT_FAILED_COUNT);
    int DEFAULT_HTTP_PROXY_RESULT_SUCCESS_COUNT = 4;
    int DEFAULT_HTTP_PROXY_RESULT_FAILED_COUNT = 5;
    float DEFAULT_HTTP_RESULT_RATE = (float) DEFAULT_HTTP_PROXY_RESULT_SUCCESS_COUNT / (DEFAULT_HTTP_PROXY_RESULT_SUCCESS_COUNT + DEFAULT_HTTP_PROXY_RESULT_FAILED_COUNT);
    int DEFAULT_VOIP_RESULT_SUCCESS_COUNT = 1;
    int DEFAULT_VOIP_RESULT_FAILED_COUNT = 2;
    float DEFAULT_VOIP_RESULT_RATE = (float) DEFAULT_VOIP_RESULT_SUCCESS_COUNT / (DEFAULT_VOIP_RESULT_SUCCESS_COUNT + DEFAULT_VOIP_RESULT_FAILED_COUNT);
    int DEFAULT_TRACEROUTE_RESULT_SUCCESS_COUNT = 0;
    int DEFAULT_TRACEROUTE_RESULT_FAILED_COUNT = 0;
    float DEFAULT_TRACEROUTE_RESULT_RATE = 0;
    int DEFAULT_WEBSITE_RESULT_SUCCESS_COUNT = 10;
    int DEFAULT_WEBSITE_RESULT_FAILED_COUNT = 13;
    float DEFAULT_WEBSITE_RESULT_RATE = (float) DEFAULT_WEBSITE_RESULT_SUCCESS_COUNT / (DEFAULT_WEBSITE_RESULT_SUCCESS_COUNT + DEFAULT_WEBSITE_RESULT_FAILED_COUNT);
    int DEFAULT_NON_TRANSPARENT_PROXY_RESULT_SUCCESS_COUNT = 1;
    int DEFAULT_NON_TRANSPARENT_PROXY_RESULT_FAILED_COUNT = 2;
    float DEFAULT_NON_TRANSPARENT_PROXY_RESULT_RATE = (float) DEFAULT_NON_TRANSPARENT_PROXY_RESULT_SUCCESS_COUNT / (DEFAULT_NON_TRANSPARENT_PROXY_RESULT_SUCCESS_COUNT + DEFAULT_NON_TRANSPARENT_PROXY_RESULT_FAILED_COUNT);
    int DEFAULT_DNS_RESULT_SUCCESS_COUNT = 1;
    int DEFAULT_DNS_RESULT_FAILED_COUNT = 1;
    float DEFAULT_DNS_RESULT_RATE = (float) DEFAULT_DNS_RESULT_SUCCESS_COUNT / (DEFAULT_DNS_RESULT_SUCCESS_COUNT + DEFAULT_DNS_RESULT_FAILED_COUNT);
    int DEFAULT_UDP_RESULT_SUCCESS_COUNT = 11;
    int DEFAULT_UDP_RESULT_FAILED_COUNT = 12;
    float DEFAULT_UDP_RESULT_RATE = (float) DEFAULT_UDP_RESULT_SUCCESS_COUNT / (DEFAULT_UDP_RESULT_SUCCESS_COUNT + DEFAULT_UDP_RESULT_FAILED_COUNT);
    float DEFAULT_TOTAL_SUCCESS_RESULT_QOS = DEFAULT_TCP_RESULT_SUCCESS_COUNT + DEFAULT_HTTP_PROXY_RESULT_SUCCESS_COUNT +
            DEFAULT_VOIP_RESULT_SUCCESS_COUNT + DEFAULT_TRACEROUTE_RESULT_SUCCESS_COUNT +
            DEFAULT_WEBSITE_RESULT_SUCCESS_COUNT + DEFAULT_NON_TRANSPARENT_PROXY_RESULT_SUCCESS_COUNT +
            DEFAULT_DNS_RESULT_SUCCESS_COUNT + DEFAULT_UDP_RESULT_SUCCESS_COUNT;
    float DEFAULT_TOTAL_FAILED_RESULT_QOS = DEFAULT_TCP_RESULT_FAILED_COUNT + DEFAULT_HTTP_PROXY_RESULT_FAILED_COUNT +
            DEFAULT_VOIP_RESULT_FAILED_COUNT + DEFAULT_TRACEROUTE_RESULT_FAILED_COUNT +
            DEFAULT_WEBSITE_RESULT_FAILED_COUNT + DEFAULT_NON_TRANSPARENT_PROXY_RESULT_FAILED_COUNT +
            DEFAULT_DNS_RESULT_FAILED_COUNT + DEFAULT_UDP_RESULT_FAILED_COUNT;
    float DEFAULT_TOTAL_OVERALL_RESULT_QOS = DEFAULT_TOTAL_SUCCESS_RESULT_QOS / (DEFAULT_TOTAL_SUCCESS_RESULT_QOS + DEFAULT_TOTAL_FAILED_RESULT_QOS);
    String DEFAULT_TEST_SERVER_NAME_TITLE = "Testserver name";
    String DEFAULT_MUNICIPALITY = "Bashkia Prrenjas";
    String DEFAULT_TENANT_QOS_INDEX = "DEFAULT_TENANT_QOS_INDEX";
    String DEFAULT_MUNICIPALITY_CODE = "4123";
    String DEFAULT_LOOP_MODE_UUID_STRING = "5e8da266-15d0-11ec-82a8-0242ac130003";
    UUID DEFAULT_LOOP_MODE_UUID = UUID.fromString("5e8da266-15d0-11ec-82a8-0242ac130003");
    Integer DEFAULT_MAX_DELAY = 5;
    Integer DEFAULT_MAX_MOVEMENTS = 10;
    Integer DEFAULT_MAX_TESTS = 7;
    Integer DEFAULT_TEST_COUNTER = 2;
    String DEFAULT_INDEX_NAME = "DEFAULT_INDEX_NAME";
    String DEFAULT_KEEP_ALIVE_INDEX_NAME = "DEFAULT_INDEX_NAME";
    UserExperienceQuality DEFAULT_USER_EXPERIENCE_QUALITY = UserExperienceQuality.GOOD;
    UserExperienceCategory DEFAULT_USER_EXPERIENCE_CATEGORY = UserExperienceCategory.VIDEO;
    String DEFAULT_BASIC_TEST_HISTORY_MOBILE_RESPONSE_JSON = "{" +
            "\"test_uuid\":\"6136c67b-c1ad-4c2f-9c86-39328c5dbf35\"," +
            "\"speed_upload\":2," +
            "\"speed_download\":1," +
            "\"ping\":58.42036," +
            "\"voip_result_jitter_millis\":1.5," +
            "\"voip_result_packet_loss_percents\":1.0," +
            "\"network_type\":\"4G\"," +
            "\"qos\":50.0," +
            "\"qosTestResultCounters\":null," +
            "\"measurement_date\":null," +
            "\"device\":\"DEFAULT_DEVICE\"," +
            "\"loop_mode_uuid\":null," +
            "\"userExperienceMetrics\":null," +
            "\"radioSignals\":null," +
            "\"networkName\":\"LTE\"," +
            "\"measurementServerName\":\"EKIP Server (Podgorica)\"," +
            "\"location\":{" +
            "\"lat\":123.123," +
            "\"long\":321.321," +
            "\"city\":\"Oslo\"," +
            "\"country\":\"Norway\"," +
            "\"county\":\"Oslo\"," +
            "\"postalCode\":\"400000\"}," +
            "\"measurementServerCity\":\"Oslo\"," +
            "\"operator\":\"DEFAULT_OPERATOR\"," +
            "\"platform\":\"ANDROID\"," +
            "\"appVersion\":\"0.2\"" +
            "}";
    BasicTestHistoryMobileResponse DEFAULT_BASIC_TEST_HISTORY_MOBILE_RESPONSE = BasicTestHistoryMobileResponse.builder()
            .openTestUuid(DEFAULT_OPEN_TEST_UUID_STRING)
            .uploadSpeed(DEFAULT_UPLOAD_SPEED)
            .downloadSpeed(DEFAULT_DOWNLOAD_SPEED)
            .qos(DEFAULT_OVERALL_QOS_PERCENTAGE)
            .ping(DEFAULT_PING)
            .jitter(DEFAULT_JITTER)
            .packetLoss(DEFAULT_PACKET_LOSS)
            .networkType(DEFAULT_NETWORK_TYPE_LTE_CATEGORY)
            .networkName(DEFAULT_NETWORK_TYPE_LTE_NAME)
            .measurementServerName(DEFAULT_MEASUREMENT_SERVER_NAME)
            .location(LocationResponse.builder()
                    .city(DEFAULT_CITY)
                    .country(DEFAULT_COUNTRY)
                    .county(DEFAULT_COUNTY)
                    .postalCode(DEFAULT_POSTAL_CODE)
                    .latitude(DEFAULT_LATITUDE)
                    .longitude(DEFAULT_LONGITUDE)
                    .build())
            .device(DEFAULT_DEVICE)
            .operator(TestConstants.DEFAULT_OPERATOR)
            .measurementServerCity(TestConstants.DEFAULT_CITY)
            .platform(TestConstants.DEFAULT_TEST_PLATFORM.name())
            .appVersion(TestConstants.DEFAULT_APP_VERSION)
            .build();
    String DEFAULT_AD_HOC_CAMPAIGN_NAME_FIRST = "DEFAULT_AD_HOC_CAMPAIGN_NAME_FIRST";
    String DEFAULT_AD_HOC_CAMPAIGN_NAME_SECOND = "DEFAULT_AD_HOC_CAMPAIGN_NAME_SECOND";
    String DEFAULT_AD_HOC_CAMPAIGN_DESCRIPTION_FIRST = "AD_HOC_CAMPAIGN_DESCRIPTION_FIRST";
    String DEFAULT_AD_HOC_CAMPAIGN_DESCRIPTION_SECOND = "AD_HOC_CAMPAIGN_DESCRIPTION_SECOND";
    String DEFAULT_AD_HOC_CAMPAIGN_LOCATION_FIRST = "Cluj-Test";
    String DEFAULT_AD_HOC_CAMPAIGN_LOCATION_SECOND = "Drygalskiweg, 1210 Wien, Österreich";
    MeasurementServerType DEFAULT_MEASUREMENT_SERVER_TYPE = MeasurementServerType.RMBTws;
    String DEFAULT_GIT_BRANCH = "DEFAULT_GIT_BRANCH";
    String DEFAULT_GIT_COMMIT_ID_DESCRIBE = "DEFAULT_GIT_COMMIT_ID_DESCRIBE";
    String DEFAULT_CONTROL_SERVER_VERSION = String.format("%s_%s", DEFAULT_GIT_BRANCH, DEFAULT_GIT_COMMIT_ID_DESCRIBE);
    String DEFAULT_TELEPHONY_NETWORK_OPERATOR = "310-260";
    PortType DEFAULT_PORT_TYPE = PortType.FIXED;
    int DEFAULT_GRAPH_HOUR = 12;
    AdHocCampaignStatus DEFAULT_AD_HOC_CAMPAIGN_STATUS = AdHocCampaignStatus.RUNNING;
    boolean DEFAULT_PROBE_ASSIGNED = true;
    String DEFAULT_MCC_MNC = "332-33";
    String DEFAULT_MCC = "444";
    String DEFAULT_MNC = "322";
    String DEFAULT_MCC_MNC_SECOND = DEFAULT_MCC + "-" + DEFAULT_MNC;
    boolean DEFAULT_MOBILE_NETWORK_OPERATOR = true;
    String DEFAULT_PROVIDER_ALIAS = "DEFAULT_PROVIDER_ALIAS";
    Long DEFAULT_PROBE_KEEP_ALIVE_ID = 88L;
    String DEFAULT_TESTED_IP = "DEFAULT_TESTED_IP";
    String DEFAULT_MEASUREMENT_SERVER_SECRET_KEY = "CUuHa7PD84gkLK6xopSX9j7Y8kgp688MGAeauupS3T0WoSg9N81Edoxam96F0rE7Nr5pQBLEzupUXkpwMXDa5JtlD3sziz4QQh62";
    Long DEFAULT_NATIONAL_OPERATOR_ID = 33L;
    String DEFAULT_NATIONAL_OPERATOR_ALIAS = "DEFAULT_NATIONAL_OPERATOR_ALIAS";
    String DEFAULT_NATIONAL_OPERATOR_NAME = "DEFAULT_NATIONAL_OPERATOR_NAME";
    String DEFAULT_IP_V6 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
    String DEFAULT_IP_V4 = "123.12.12.12";
    String DEFAULT_PROVIDER_ISP_RAW_ID = "BATELCO";
    String DEFAULT_PROVIDER_ISP_NAME = "DEFAULT_PROVIDER_ISP_NAME";
    Boolean DEFAULT_PROVIDER_MOBILE_NETWORK_OPERATOR = Boolean.TRUE;
    Long DEFAULT_PROVIDER_ASN = 3245L;
    String DEFAULT_PROVIDER_ASN_STRING = String.valueOf(DEFAULT_PROVIDER_ASN);
    String DEFAULT_NODE_ID = "DEFAULT_NODE_ID";
    Long DEFAULT_TASK_ID = 12366L;
}
