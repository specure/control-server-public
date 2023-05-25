package com.specure.service.mobile.impl;


import com.specure.common.enums.ServerTechForMeasurement;
import com.specure.common.enums.TestType;
import com.specure.common.exception.HstoreParseException;
import com.specure.common.model.elastic.BasicQosTest;
import com.specure.common.model.jpa.MeasurementQos;
import com.specure.common.model.jpa.MeasurementServer;
import com.specure.common.repository.MeasurementRepository;
import com.specure.common.repository.MeasurementServerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.specure.config.ApplicationProperties;
import com.specure.constant.Constants;
import com.specure.constant.ErrorMessage;
import com.specure.exception.QoSMeasurementServerNotFoundByUuidException;
import com.specure.mapper.core.MeasurementQosMapper;
import com.specure.mapper.mobile.MobileQosTestObjectiveMapper;
import com.specure.mapper.mobile.MobileQosTestResultMapper;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.core.MeasurementQosRepository;
import com.specure.repository.mobile.QosTestDescRepository;
import com.specure.repository.mobile.QosTestObjectiveRepository;
import com.specure.request.core.*;
import com.specure.response.mobile.MobileMeasurementQosResponse;
import com.specure.response.mobile.MobileQosParamsResponse;
import com.specure.response.mobile.OverallQosMeasurementResponse;
import com.specure.response.mobile.QosMeasurementsResponse;
import com.specure.response.sah.ErrorContainerResponse;
import com.specure.service.BasicQosTestService;
import com.specure.service.mobile.MobileQosMeasurementService;
import com.specure.utils.mobile.QosUtil;
import com.specure.utils.sah.MessageUtils;
import com.specure.utils.sah.ValidateUtils;
import com.vdurmont.semver4j.SemverException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MobileQosMeasurementServiceImpl implements MobileQosMeasurementService {
    private static final Logger logger = LoggerFactory.getLogger(MobileQosMeasurementServiceImpl.class);
    private final QosTestObjectiveRepository qosTestObjectiveRepository;
    private final MobileQosTestObjectiveMapper mobileQosTestObjectiveMapper;
    private final ApplicationProperties applicationProperties;
    private final MeasurementRepository testRepository;
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;
    private final MobileQosTestResultMapper mobileQosTestResultMapper;
    private final QosTestDescRepository qosTestDescRepository;
    private final BasicQosTestService basicQosTestService;
    private final MeasurementQosRepository measurementQosRepository;
    private final MeasurementQosMapper mobileMeasurementQosMapper;
    private final MeasurementServerRepository measurementServerRepository;
    private final MultiTenantManager multiTenantManager;


    @Override
    public OverallQosMeasurementResponse saveQosMeasurementResult(MeasurementQosRequest request) {
        logger.info("MobileQosMeasurementServiceImpl:saveQosMeasurementResult started with tenant = {}, MeasurementQosRequest = {}", multiTenantManager.getCurrentTenant(), request);
        final OverallQosMeasurementResponse response = new OverallQosMeasurementResponse();
        final String lang = request.getClientLanguage();
        Locale locale = MessageUtils.getLocaleFormLanguage(lang, applicationProperties.getLanguage());

        if (StringUtils.isNotBlank(request.getTestToken())) {
            final String[] token = request.getTestToken().split("_");

            try {
                final UUID testUuid = UUID.fromString(token[0]);
                if (token.length > 2 && token[2].length() > 0) { // && hmac.equals(token[2])) (can be different server keys)
                    final Set<String> clientNames = applicationProperties.getClientNames();
                    ValidateUtils.validateClientVersion(applicationProperties.getVersion(), request.getClientVersion());
                    if (clientNames.contains(request.getClientName())) { //save qos test results:
                        MeasurementQos measurementQos = mobileMeasurementQosMapper.measurementQosRequestToMeasurementQosMobile(request);
                        measurementQos.setOpenTestUuid(testUuid.toString());
                        saveMeasurementQosToDatabaseAsync(measurementQos, multiTenantManager.getCurrentTenant());
                        BasicQosTest savedBasicQosTest = basicQosTestService.saveMeasurementQosMobileToElastic(measurementQos);
                        response.setOverallQos(QosUtil.calculatePercentage(savedBasicQosTest.getOverallQos()));
                        response.setQosTestResultCounters(savedBasicQosTest.getQosTestResultCounters());
                    } else {
                        response.addErrorString(messageSource.getMessage("ERROR_CLIENT_VERSION", null, locale));
                    }
                } else {
                    response.addErrorString(messageSource.getMessage("ERROR_TEST_TOKEN_MALFORMED", null, locale));
                }
            } catch (final IllegalArgumentException e) {
                logger.error(e.getMessage(), e);
                response.addErrorString(messageSource.getMessage("ERROR_TEST_TOKEN_MALFORMED", null, locale));
            } catch (SemverException e) {
                response.addErrorString(messageSource.getMessage("ERROR_CLIENT_VERSION", null, locale));
            }
        } else {
            response.addErrorString(messageSource.getMessage("ERROR_TEST_TOKEN_MISSING", null, locale));
        }
        logger.debug("MobileQosMeasurementServiceImpl:saveQosMeasurementResult finished with tenant = {}, overallQosMeasurementResponse = {}", multiTenantManager.getCurrentTenant(), response);
        return response;
    }

    @Override
    public QosMeasurementsResponse getQosResult(UUID qosTestUuid, String language, CapabilitiesRequest capabilitiesRequest) {
        logger.debug("MobileQosMeasurementServiceImpl:getQosResult with tenant = {}, openTestUuid = {}, language = {}, capabilitiesRequest = {}",
                multiTenantManager.getCurrentTenant(), qosTestUuid, language, capabilitiesRequest);
        final ErrorContainerResponse errorList = new ErrorContainerResponse();
        Locale locale = MessageUtils.getLocaleFormLanguage(language, applicationProperties.getLanguage());
        QosMeasurementsResponse.QosMeasurementsResponseBuilder answer = QosMeasurementsResponse.builder();
        try {
            QosUtil.evaluate(
                    answer,
                    mobileQosTestResultMapper,
                    messageSource,
                    applicationProperties,
                    testRepository,
                    measurementQosRepository,
                    qosTestUuid,
                    false,
                    objectMapper,
                    qosTestDescRepository,
                    locale,
                    errorList,
                    capabilitiesRequest,
                    qosTestObjectiveRepository
            );
        } catch (final JSONException | IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            errorList.addErrorString(messageSource.getMessage("ERROR_REQUEST_JSON", null, locale));
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            errorList.addErrorString(messageSource.getMessage("ERROR_DB_CONNECTION", null, locale));
        } catch (HstoreParseException e) {
            logger.error(e.getMessage(), e);
            errorList.addErrorString(e.getMessage());
        } catch (IllegalAccessException | JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        } catch (UnsupportedOperationException e) {
            logger.error(String.format(ErrorMessage.QOS_TEST_RESULT_FOR_TEST_NOT_FOUND, qosTestUuid));
            errorList.addErrorString(messageSource.getMessage("ERROR_REQUEST_QOS_RESULT_DETAIL_NO_UUID", null, locale));
        }

        QosMeasurementsResponse response = answer.build();
        response.getError().addAll(errorList.getError());
        logger.debug("MobileQosMeasurementServiceImpl:getQosResult finished with tenant = {}, qosMeasurementsResponse = {}", multiTenantManager.getCurrentTenant(), response);
        return response;
    }

    @Override
    public QosMeasurementsResponse evaluateQosByOpenTestUUID(UUID openTestUUID, String lang) {
        logger.debug("MobileQosMeasurementServiceImpl:evaluateQosByOpenTestUUID with tenant = {}, openTestUuid = {}, lang = {}", multiTenantManager.getCurrentTenant(), openTestUUID, lang);
        Locale locale = MessageUtils.getLocaleFormLanguage(lang, applicationProperties.getLanguage());
        QosMeasurementsResponse.QosMeasurementsResponseBuilder answer = QosMeasurementsResponse.builder();
        final ErrorContainerResponse errorList = new ErrorContainerResponse();
        try {
            QosUtil.evaluate(
                    answer,
                    mobileQosTestResultMapper,
                    messageSource,
                    applicationProperties,
                    testRepository,
                    measurementQosRepository,
                    openTestUUID,
                    true,
                    objectMapper,
                    qosTestDescRepository,
                    locale,
                    errorList,
                    getDefaultCapabilitiesRequest(),
                    qosTestObjectiveRepository
            );
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            errorList.addErrorString(messageSource.getMessage("ERROR_DB_CONNECTION", null, locale));
        } catch (IllegalAccessException | JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        } catch (UnsupportedOperationException e) {
            logger.error(e.getMessage(), e);
            errorList.addErrorString(messageSource.getMessage("ERROR_REQUEST_QOS_RESULT_DETAIL_NO_UUID", null, locale));
        } catch (HstoreParseException e) {
            logger.error(e.getMessage(), e);
            errorList.addErrorString(e.getMessage());
        }
        QosMeasurementsResponse response = answer.build();
        response.getError().addAll(errorList.getError());
        logger.debug("MobileQosMeasurementServiceImpl:evaluateQosByOpenTestUUID finished with tenant = {}, qosMeasurementsResponse = {}", multiTenantManager.getCurrentTenant(), response);
        return response;
    }

    @Override
    public MobileMeasurementQosResponse getQosParameters(MeasurementQosParametersRequest measurementQosParametersRequest, Map<String, String> headers) {
        logger.debug("MobileQosMeasurementServiceImpl:getQosParameters with tenant = {}, measurementQosParametersRequest = {}, headers = {}",
                multiTenantManager, measurementQosParametersRequest, StringUtils.join(headers));
        Map<TestType, List<MobileQosParamsResponse>> objectives = new HashMap<>();
        Optional<MeasurementServer> measurementServer = measurementServerRepository.findByClientUUID(measurementQosParametersRequest.getUuid());
        if (measurementServer.isEmpty()) {
            throw new QoSMeasurementServerNotFoundByUuidException(measurementQosParametersRequest.getUuid());
        }
        String serverAddress = measurementServer.get().getWebAddress();
        String serverPort = String.valueOf(ServerTechForMeasurement.QOS_TECH.getDefaultSslPort());
        qosTestObjectiveRepository.findAll().forEach(qosTestObjective -> {
            qosTestObjective.setTestServerAddress(serverAddress);
            qosTestObjective.setTestServerPort(serverPort);
            List<MobileQosParamsResponse> paramsList;

            if (objectives.containsKey(qosTestObjective.getTestType())) {
                paramsList = objectives.get(qosTestObjective.getTestType());
            } else {
                paramsList = new ArrayList<>();
                objectives.put(qosTestObjective.getTestType(), paramsList);
            }

            MobileQosParamsResponse params = mobileQosTestObjectiveMapper.qosTestObjectiveToQosParamsResponse(qosTestObjective);

            paramsList.add(params);
        });

        MobileMeasurementQosResponse mobileMeasurementQosResponse = MobileMeasurementQosResponse.builder()
                .objectives(objectives)
                .testDuration(applicationProperties.getDuration())
                .testNumThreads(applicationProperties.getThreads())
                .testNumPings(applicationProperties.getPings())
                .error(Collections.emptyList())
                .build();
        logger.debug("MobileQosMeasurementServiceImpl:getQosParameters finished with tenant = {}, mobileMeasurementQosResponse = {}", multiTenantManager.getCurrentTenant(), mobileMeasurementQosResponse);
        return mobileMeasurementQosResponse;
    }

    private CapabilitiesRequest getDefaultCapabilitiesRequest() {
        return CapabilitiesRequest.builder()
                .classification(ClassificationRequest.builder().count(Constants.DEFAULT_CLASSIFICATION_COUNT).build())
                .qos(QosRequest.builder().supportsInfo(Constants.DEFAULT_QOS_SUPPORTS_INFO).build())
                .rmbtHttp(Constants.DEFAULT_RMBT_HTTP)
                .build();
    }

    private void saveMeasurementQosToDatabaseAsync(MeasurementQos measurementQos, String tenant) {
        logger.trace("MobileQosMeasurementServiceImpl:saveMeasurementQosToDatabaseAsync started with tenant = {}, measurementQos = {}", tenant, measurementQos);
        var one = new Thread(() -> {
            multiTenantManager.setCurrentTenant(tenant);
            measurementQosRepository.save(measurementQos);
        });
        one.start();
        logger.trace("MobileQosMeasurementServiceImpl:saveMeasurementQosToDatabaseAsync finished with tenant = {}", tenant);
    }
}
