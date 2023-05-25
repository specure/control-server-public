package com.specure.controller.mobile;

import com.specure.advice.SahBackendAdvice;
import com.specure.constant.URIConstants;
import com.specure.dto.mobile.MobileGraph;
import com.specure.dto.mobile.SpeedCurve;
import com.specure.response.mobile.MobileGraphResponse;
import com.specure.sah.TestConstants;
import com.specure.service.mobile.MobileGraphService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
public class MobileGraphControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private MobileGraphService mobileGraphService;

    @Before
    public void setUp() {
        MobileGraphController mobileGraphController = new MobileGraphController(mobileGraphService);
        mockMvc = MockMvcBuilders.standaloneSetup(mobileGraphController)
                .setControllerAdvice(new SahBackendAdvice())
                .build();
    }

    @Test
    public void processResult_whenCommonData_expectMobileGraphResponse() throws Exception {
        var response = getMobileGraphResponse();
        when(mobileGraphService.getMobileGraph(TestConstants.DEFAULT_UUID)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get(URIConstants.MOBILE + URIConstants.GRAPHS + URIConstants.UUID, TestConstants.DEFAULT_UUID)
                .contentType(MediaType.APPLICATION_JSON)

        )
                .andDo(print())
                .andExpect(jsonPath("$.speed_curve.download[0].bytes_total").value(TestConstants.DEFAULT_TEST_BYTES_DOWNLOAD))
                .andExpect(jsonPath("$.speed_curve.download[0].time_elapsed").value(Math.round(TestConstants.DEFAULT_DOWNLOAD_DURATION_NANOS / 1e6)))
                .andExpect(jsonPath("$.speed_curve.upload[0].bytes_total").value(TestConstants.DEFAULT_TEST_BYTES_UPLOAD))
                .andExpect(jsonPath("$.speed_curve.upload[0].time_elapsed").value(Math.round(TestConstants.DEFAULT_UPLOAD_DURATION_NANOS / 1e6)));
    }

    private MobileGraphResponse getMobileGraphResponse() {
        var download = List.of(
                MobileGraph.builder()
                        .bytesTotal(TestConstants.DEFAULT_TEST_BYTES_DOWNLOAD.doubleValue())
                        .timeElapsed(TestConstants.DEFAULT_DOWNLOAD_DURATION_NANOS.doubleValue())
                        .build()
        );
        var upload = List.of(
                MobileGraph.builder()
                        .bytesTotal(TestConstants.DEFAULT_TEST_BYTES_UPLOAD.doubleValue())
                        .timeElapsed(TestConstants.DEFAULT_UPLOAD_DURATION_NANOS.doubleValue())
                        .build()
        );
        return MobileGraphResponse.builder()
                .speedCurve(new SpeedCurve(download, upload))
                .build();
    }
}
