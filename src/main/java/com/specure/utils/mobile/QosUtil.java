package com.specure.utils.mobile;


import com.specure.common.enums.TestType;
import com.specure.common.exception.HstoreParseException;
import com.specure.common.model.dto.qos.*;
import com.specure.common.model.jpa.Measurement;
import com.specure.common.model.jpa.MeasurementQos;
import com.specure.common.repository.MeasurementRepository;
import com.specure.common.utils.hstoreparser.Hstore;
import com.specure.common.utils.testscript.TestScriptInterpreter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.specure.config.ApplicationProperties;
import com.specure.dto.sah.qos.QosTestDesc;
import com.specure.dto.sah.qos.QosTestResult;
import com.specure.mapper.mobile.MobileQosTestResultMapper;
import com.specure.repository.core.MeasurementQosRepository;
import com.specure.repository.mobile.QosTestDescRepository;
import com.specure.repository.mobile.QosTestObjectiveRepository;
import com.specure.request.core.CapabilitiesRequest;
import com.specure.response.mobile.QosMeasurementsResponse;
import com.specure.response.sah.ErrorContainerResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.context.MessageSource;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class QosUtil {
    private final static Integer PERCENTAGE_MULTIPLIER = 100;

    public static final Hstore HSTORE_PARSER = new Hstore(HttpProxyResult.class, NonTransparentProxyResult.class,
            DnsResult.class, TcpResult.class, UdpResult.class, WebsiteResult.class, VoipResult.class, TracerouteResult.class);

    /**
     * compares test results with expected results and increases success/failure counter
     *
     * @param testResult    the test result
     * @param result        the parsed test result
     * @param resultKeys    result key map
     * @param testType      test type
     * @param resultOptions result options
     * @throws HstoreParseException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void compareTestResults(final QosTestResult testResult, final AbstractResult<?> result,
                                          final Map<TestType, TreeSet<ResultDesc>> resultKeys, final TestType testType,
                                          final ResultOptions resultOptions, final ObjectMapper objectMapper) throws HstoreParseException, IllegalArgumentException, IllegalAccessException, JsonProcessingException {

        //if expected resuls not null, compare them to the test results
        if (testResult.getResult() != null) {
            //create a parsed abstract result set sorted by priority
            final Set<AbstractResult<?>> expResultSet = new TreeSet<>(Comparator.comparing(AbstractResult::getPriority));

            int priority = Integer.MAX_VALUE;

            if (StringUtils.isNotBlank(testResult.getQosTestObjective().getResults())) {
                Class<? extends AbstractResult<?>> testClass = testResult.getQosTestObjective().getTestType().getClazz();
                List<? extends AbstractResult<?>> expectedResults = objectMapper.readerForListOf(testClass)
                        .readValue(testResult.getQosTestObjective().getResults());
                for (final AbstractResult<?> expResult : expectedResults) {
                    //parse hstore string to object
                    if (expResult.getPriority() == Integer.MAX_VALUE) {
                        expResult.setPriority(priority--);
                    }
                    expResultSet.add(expResult);
                }
            }

            for (final AbstractResult<?> expResult : expResultSet) {
                //compare expected result to test result and save the returned id
                ResultDesc resultDesc = ResultComparer.compare(result, expResult, QosUtil.HSTORE_PARSER, resultOptions);
                if (resultDesc != null) {
                    resultDesc.addTestResultUid(testResult.getId());
                    resultDesc.setTestType(testType);

                    final ResultHolder resultHolder = calculateResultCounter(testResult, expResult, resultDesc);

                    //check if there is a result message
                    if (resultHolder != null) {
                        TreeSet<ResultDesc> resultDescSet;
                        if (resultKeys.containsKey(testType)) {
                            resultDescSet = resultKeys.get(testType);
                        } else {
                            resultDescSet = new TreeSet<>();
                            resultKeys.put(testType, resultDescSet);
                        }

                        resultDescSet.add(resultDesc);

                        testResult.getResultKeyMap().put(resultDesc.getKey(), resultHolder.resultKeyType);

                        if (AbstractResult.BEHAVIOUR_ABORT.equals(resultHolder.event)) {
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * calculates and set the specific result counter
     *
     * @param testResult test result
     * @param expResult  expected test result
     * @param resultDesc result description
     * @return result type string, can be:
     * <ul>
     * 	<li>{@link ResultDesc#STATUS_CODE_SUCCESS}</li>
     * 	<li>{@link ResultDesc#STATUS_CODE_FAILURE}</li>
     * 	<li>{@link ResultDesc#STATUS_CODE_INFO}</li>
     * </ul>
     */
    public static ResultHolder calculateResultCounter(final QosTestResult testResult, final AbstractResult<?> expResult, final ResultDesc resultDesc) {
        String resultKeyType = null;
        String event = AbstractResult.BEHAVIOUR_NOTHING;

        //increase the failure or success counter of this result object
        if (resultDesc.getStatusCode().equals(ResultDesc.STATUS_CODE_SUCCESS)) {
            if (expResult.getOnSuccess() != null) {
                testResult.setSuccessCount(testResult.getSuccessCount() + 1);
                if (AbstractResult.RESULT_TYPE_DEFAULT.equals(expResult.getSuccessType())) {
                    resultKeyType = ResultDesc.STATUS_CODE_SUCCESS;
                } else {
                    resultKeyType = ResultDesc.STATUS_CODE_INFO;
                }

                event = expResult.getOnSuccessBehaivour();
            }
        } else if (resultDesc.getStatusCode().equals(ResultDesc.STATUS_CODE_FAILURE)) {
            if (expResult.getOnFailure() != null) {
                testResult.setFailureCount(testResult.getFailureCount() + 1);
                if (AbstractResult.RESULT_TYPE_DEFAULT.equals(expResult.getFailureType())) {
                    resultKeyType = ResultDesc.STATUS_CODE_FAILURE;
                } else {
                    resultKeyType = ResultDesc.STATUS_CODE_INFO;
                }

                event = expResult.getOnFailureBehaivour();
            }
        } else {
            resultKeyType = ResultDesc.STATUS_CODE_INFO;
            event = AbstractResult.BEHAVIOUR_NOTHING;
        }

        return resultKeyType != null ? new ResultHolder(resultKeyType, event) : null;
    }

    public static void evaluate(
            final QosMeasurementsResponse.QosMeasurementsResponseBuilder answer,
            final MobileQosTestResultMapper mobileQosTestResultMapper,
            final MessageSource messageSource,
            final ApplicationProperties applicationProperties,
            final MeasurementRepository testRepository,
            final MeasurementQosRepository measurementQosRepository,
            final UUID uuid,
            final boolean isOpenTestUuid,
            final ObjectMapper objectMapper,
            final QosTestDescRepository qosTestDescRepository,
            final Locale locale,
            final ErrorContainerResponse errorList,
            final CapabilitiesRequest capabilities,
            final QosTestObjectiveRepository qosTestObjectiveRepository
    ) throws SQLException, HstoreParseException, JSONException, IllegalArgumentException, IllegalAccessException, JsonProcessingException {
        // Load Language Files for Client
        Optional<Measurement> optionalTest = Optional.empty();

        if (uuid != null) {
            optionalTest = testRepository.findByOpenTestUuid(uuid.toString());
        }

        final long timeStampFullEval = System.currentTimeMillis();

        if (optionalTest != null && optionalTest.isPresent()) {
            final ResultOptions resultOptions = new ResultOptions(locale);
            final List<QosMeasurementsResponse.QosTestResultItem> resultList = new ArrayList<>();
            Measurement test = optionalTest.get();
            MeasurementQos measurementQos = measurementQosRepository.findByOpenTestUuid(test.getOpenTestUuid())
                    .orElseThrow();
            List<QosTestResult> testResultList = Stream.of(
                            measurementQos.getDnsTestResults(),
                            measurementQos.getTcpTestResults(),
                            measurementQos.getHttpProxyTestResults(),
                            measurementQos.getTracerouteTestResults(),
                            measurementQos.getUdpTestResults(),
                            measurementQos.getVoipTestResults(),
                            measurementQos.getNonTransparentProxyTestResults(),
                            measurementQos.getWebsiteTestResults()
                    )
                    .flatMap(Collection::stream)
                    .map(mobileQosTestResultMapper::testResultToQosTestResult)
                    .collect(Collectors.toList());
            testResultList.forEach(qosTestResult -> {
                qosTestResult.setQosTestObjective(qosTestObjectiveRepository.getOne(qosTestResult.getQosTestObjectiveId()));
                qosTestResult.setTestUid(test.getId());
            });
            if (testResultList.isEmpty()) {
                throw new UnsupportedOperationException("test " + test + " has no result list");
            }
            //map that contains all test types and their result descriptions determined by the test result <-> test objectives comparison
            Map<TestType, TreeSet<ResultDesc>> resultKeys = new HashMap<>();

            //test description set:
            Set<String> testDescSet = new TreeSet<>();
            //test summary set:
            Set<String> testSummarySet = new TreeSet<>();


            //Staring timestamp for evaluation time measurement
            final long timeStampEval = System.currentTimeMillis();

            //iterate through all result entries
            for (final QosTestResult testResult : testResultList) {

                //reset test counters
                testResult.setFailureCount(0);
                testResult.setSuccessCount(0);

                //get the correct class of the result;
                TestType testType = testResult.getQosTestObjective().getTestType();

                if (testType == null) {
                    continue;
                }

                Class<? extends AbstractResult<?>> clazz = testType.getClazz();
                //parse hstore data
                if (testResult.getQosTestObjective().getResults() != null) {
                    AbstractResult<?> result = objectMapper.readValue(testResult.getResult(), clazz);
                    result.setResultMap(objectMapper.readValue(testResult.getResult(), new TypeReference<>() {
                    }));

                    //add each test description key to the testDescSet (to fetch it later from the db)
                    if (testResult.getQosTestObjective().getTestDescription() != null) {
                        testDescSet.add(testResult.getQosTestObjective().getTestDescription());
                    }
                    if (testResult.getQosTestObjective().getTestSummary() != null) {
                        testSummarySet.add(testResult.getQosTestObjective().getTestSummary());
                    }
                    testResult.setResult(objectMapper.writeValueAsString(result));

                    //compare test results
                    compareTestResults(testResult, result, resultKeys, testType, resultOptions, objectMapper);
                }
            }

            //ending timestamp for evaluation time measurement
            final long timeStampEvalEnd = System.currentTimeMillis();

            //-------------------------------------------------------------
            //fetch all result strings from the db

            //FIRST: get all test descriptions
            testDescSet.addAll(testSummarySet);

            for (TestType value : TestType.values()) {
                testDescSet.add(value.getDescriptionKey());
                testDescSet.add(value.getNameKey());
            }

            resultKeys.values().forEach(v -> v.forEach(resultDesc -> testDescSet.add(resultDesc.getKey())));

            Map<String, String> testDescMap = qosTestDescRepository.findByKeysAndLocales(locale.getLanguage(), applicationProperties.getLanguage().getSupportedLanguages(), testDescSet)
                    .stream()
                    .collect(Collectors.toMap(QosTestDesc::getDescKey, QosTestDesc::getValue, (x1, x2) -> x1));

            for (QosTestResult testResult : testResultList) {
                //and set the test results + put each one to the result list json array
                String preParsedDesc = testDescMap.get(testResult.getQosTestObjective().getTestDescription());
                AbstractResult<?> result = objectMapper.readValue(testResult.getResult(), testResult.getQosTestObjective().getTestType().getClazz());
                result.setResultMap(objectMapper.readValue(testResult.getResult(), new TypeReference<>() {
                }));
                if (preParsedDesc != null) {
                    String description = String.valueOf(TestScriptInterpreter.interprete(
                            preParsedDesc,
                            QosUtil.HSTORE_PARSER,
                            result,
                            true,
                            resultOptions
                    ));
                    testResult.setTestDescription(description);
                }

                //do the same for the test summary:
                String preParsedSummary = testDescMap.get(testResult.getQosTestObjective().getTestSummary());
                if (preParsedSummary != null) {
                    String description = String.valueOf(TestScriptInterpreter.interprete(
                            preParsedSummary,
                            QosUtil.HSTORE_PARSER,
                            result,
                            true,
                            resultOptions
                    ));
                    testResult.setTestSummary(description);
                }

                resultList.add(mobileQosTestResultMapper.toQosTestResultItem(testResult, isOpenTestUuid));
            }


            answer.testResultDetails(resultList);


            List<QosMeasurementsResponse.QosTestResultDescItem> resultDescArray = new ArrayList<>();

            //SECOND: fetch all test result descriptions
            for (TestType testType : resultKeys.keySet()) {
                TreeSet<ResultDesc> descSet = resultKeys.get(testType);

                //fetch results to same object
                for (ResultDesc resultDesc : descSet) {
                    resultDesc.setValue(testDescMap.get(resultDesc.getKey()));
                }

                //another tree set for duplicate entries:
                //TODO: there must be a better solution
                //(the issue is: compareTo() method returns diffrent values depending on the .value attribute (if it's set or not))
                TreeSet<ResultDesc> descSetNew = new TreeSet<>();
                //add fetched results to json

                for (ResultDesc desc : descSet) {
                    if (capabilities != null && capabilities.getQos() != null && !capabilities.getQos().isSupportsInfo()) {
                        if (ResultDesc.STATUS_CODE_INFO.equals(desc.getStatusCode())) {
                            continue;
                        }
                    }

                    if (!descSetNew.contains(desc)) {
                        descSetNew.add(desc);
                    } else {
                        for (ResultDesc d : descSetNew) {
                            if (d.compareTo(desc) == 0) {
                                d.getTestResultUidList().addAll(desc.getTestResultUidList());
                            }
                        }
                    }
                }

                for (ResultDesc desc : descSetNew) {
                    if (desc.getValue() != null) {
                        resultDescArray.add(new QosMeasurementsResponse.QosTestResultDescItem(desc.getTestResultUidList(), desc.getTestType().name(), desc.getKey(), desc.getStatusCode(), desc.getParsedValue()));
                    }
                }
            }

            //put result descriptions to json
            answer.testResultDetailDesc(resultDescArray);

            List<QosMeasurementsResponse.QosTestResultTestDescItem> testTypeDescArray = new ArrayList<>();
            for (TestType desc : TestType.values()) {
                testTypeDescArray.add(new QosMeasurementsResponse.QosTestResultTestDescItem(testDescMap.get(desc.getNameKey()), desc, testDescMap.get(desc.getDescriptionKey())));
            }

            //put result descriptions to json
            answer.testResultDetailTestDesc(testTypeDescArray);
            answer.evalTimes(new QosMeasurementsResponse.EvalTimes(timeStampEvalEnd - timeStampEval, System.currentTimeMillis() - timeStampFullEval));
        } else {
            errorList.addErrorString(messageSource.getMessage("ERROR_REQUEST_TEST_RESULT_DETAIL_NO_UUID", null, locale));
        }
    }

    /**
     * @author lb
     */
    public static class ResultHolder {
        final String resultKeyType;
        final String event;

        public ResultHolder(final String resultKeyType, final String event) {
            this.resultKeyType = resultKeyType;
            this.event = event;
        }

        public String getResultKeyType() {
            return resultKeyType;
        }

        public String getEvent() {
            return event;
        }
    }

    public static Float calculatePercentage(Float qos) {
        return Objects.nonNull(qos) ?
                qos * PERCENTAGE_MULTIPLIER
                : null;
    }
}
