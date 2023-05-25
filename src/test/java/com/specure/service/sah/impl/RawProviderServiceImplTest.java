package com.specure.service.sah.impl;

import com.specure.common.model.jpa.RawProvider;
import com.specure.common.repository.RawProviderRepository;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Traits;
import com.specure.sah.TestConstants;
import com.specure.service.admin.RawProviderService;
import com.specure.service.admin.impl.RawProviderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class RawProviderServiceImplTest {

    @MockBean
    private RawProviderRepository rawProviderRepository;
    private RawProviderService rawProviderService;

    @Mock
    private RawProvider rawProvider;
    @Mock
    private CityResponse cityResponse;
    @Mock
    private Country country;
    @Mock
    private Traits traits;
    @Mock
    private RawProvider updatedRawProvider;


    @BeforeEach
    public void setUp() throws Exception {
        rawProviderService = new RawProviderServiceImpl(rawProviderRepository);
    }

    @Test
    void getProvider_correctInvocation_Provider() {
        when(rawProviderRepository.findTopByCountryAndRawName(TestConstants.DEFAULT_COUNTRY, TestConstants.DEFAULT_PROVIDER_ISP_RAW_ID))
                .thenReturn(Optional.of(rawProvider));
        when(cityResponse.getTraits()).thenReturn(traits);
        when(cityResponse.getCountry()).thenReturn(country);
        when(country.getName()).thenReturn(TestConstants.DEFAULT_COUNTRY);
        when(traits.getIsp()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_RAW_ID);

        var result = rawProviderService.getRawProvider(cityResponse, TestConstants.DEFAULT_ASN);

        assertEquals(rawProvider, result);
    }

    @Test
    void getRawProvider_rawProviderExists_expectedRawProvider() {
        when(rawProviderRepository.findTopByCountryAndRawName(TestConstants.DEFAULT_COUNTRY, TestConstants.DEFAULT_PROVIDER_ISP_RAW_ID))
                .thenReturn(Optional.of(rawProvider));
        when(cityResponse.getTraits()).thenReturn(traits);
        when(cityResponse.getCountry()).thenReturn(country);
        when(country.getName()).thenReturn(TestConstants.DEFAULT_COUNTRY);
        when(traits.getIsp()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_RAW_ID);

        var result = rawProviderService.getRawProvider(cityResponse, TestConstants.DEFAULT_ASN, TestConstants.DEFAULT_NETWORK_MCC_MNC);

        assertEquals(rawProvider, result);
    }

    @Test
    void getRawProvider_rawProviderNotExists_expectedRawProvider() {
        when(cityResponse.getTraits()).thenReturn(traits);
        when(cityResponse.getCountry()).thenReturn(country);
        when(country.getName()).thenReturn(TestConstants.DEFAULT_COUNTRY);
        when(traits.getIsp()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_RAW_ID);
        var expectedProvider = RawProvider.builder()
                .rawName(TestConstants.DEFAULT_PROVIDER_ISP_RAW_ID)
                .country(TestConstants.DEFAULT_COUNTRY)
                .asn(String.valueOf(TestConstants.DEFAULT_ASN))
                .mccMnc(TestConstants.DEFAULT_NETWORK_MCC_MNC)
                .build();
        when(rawProviderRepository.existsByCountryAndRawName(TestConstants.DEFAULT_COUNTRY, TestConstants.DEFAULT_PROVIDER_NAME)).thenReturn(false);
        when(rawProviderRepository.save(expectedProvider)).thenReturn(expectedProvider);

        var result = rawProviderService.getRawProvider(cityResponse, TestConstants.DEFAULT_ASN, TestConstants.DEFAULT_NETWORK_MCC_MNC);

        assertEquals(expectedProvider, result);
    }

    @Test
    void getRawProvider_differentMccMnc_expectedRawProvider() {
        when(rawProviderRepository.findTopByCountryAndRawName(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.RawProvider.RAW_NAME)).thenReturn(Optional.of(rawProvider));
        when(rawProvider.getMccMnc()).thenReturn(null);
        when(rawProviderRepository.save(rawProvider)).thenReturn(updatedRawProvider);

        var result = rawProviderService.getRawProvider(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.RawProvider.RAW_NAME, TestConstants.DEFAULT_SIM_MCC_MNC);

        verify(rawProvider).setMccMnc(TestConstants.DEFAULT_SIM_MCC_MNC);
        verify(rawProviderRepository).save(rawProvider);
        assertEquals(updatedRawProvider, result);
    }

    @Test
    void getRawProvider_equalsMccMnc_expectedRawProvider() {
        when(rawProviderRepository.findTopByCountryAndRawName(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.RawProvider.RAW_NAME)).thenReturn(Optional.of(rawProvider));
        when(rawProvider.getMccMnc()).thenReturn(TestConstants.DEFAULT_SIM_MCC_MNC);

        var result = rawProviderService.getRawProvider(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.RawProvider.RAW_NAME, TestConstants.DEFAULT_SIM_MCC_MNC);

        verify(rawProvider, times(0)).setMccMnc(TestConstants.DEFAULT_SIM_MCC_MNC);
        verify(rawProviderRepository, times(0)).save(rawProvider);
        assertEquals(rawProvider, result);
    }
}
