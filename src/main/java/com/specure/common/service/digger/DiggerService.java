package com.specure.common.service.digger;

import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Traits;
import com.specure.common.config.MaxMindConfig;
import com.specure.multitenant.MultiTenantManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Slf4j
@AllArgsConstructor
@Service
public class DiggerService {

    private final MaxMindConfig maxMindConfig;
    private final DiggerCommandPerformerService digger;
    private final MultiTenantManager multiTenantManager;

    public Long digASN(InetAddress address) {
        log.debug("DiggerService:digASN started with tenant = {}, address = {}", multiTenantManager.getCurrentTenant(), address);
        var ip6Postfix = "origin6.asn.cymru.com";
        var ip4Postfix = "origin.asn.cymru.com";
        String postfix = (address instanceof Inet6Address) ? ip6Postfix : ip4Postfix;
        var digCommandGetASN = "dig -t txt %s.%s +short";
        var command = String.format(digCommandGetASN, reverseIp(address), postfix);
        Long asn = getAsnFromDigResult(digger.dig(command));
        log.debug("DiggerService:digASN finished with tenant = {}, asn = {}", multiTenantManager.getCurrentTenant(), asn);
        return asn;
    }

    public Long digASN(String ipAddress) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            return digASN(inetAddress);
        } catch (UnknownHostException unknownHostException) {
            log.debug("final during getting provider by old approach: " + unknownHostException.getMessage());
            return null;
        }
    }

    public String getProviderByASN(Long asn) {
        log.debug("DiggerService:getProviderByASN started with tenant = {}, asn = {}", multiTenantManager.getCurrentTenant(), asn);
        if (Objects.isNull(asn)) {
            return null;
        }
        if (asn == 0L) {
            return "Reserved ASN = 0";
        }
        var digCommandGetProviderName = "dig -t txt AS%s.asn.cymru.com. +short";
        var command = String.format(digCommandGetProviderName, asn);
        String providerName = getProviderNameFromDigResult(digger.dig(command));
        log.debug("DiggerService:getProviderByASN finished with tenant = {}, providerName = {}", multiTenantManager.getCurrentTenant(), providerName);
        return providerName;
    }

    public String reverseIp(InetAddress address) {
        log.debug("DiggerService:reverseIp started with tenant = {}, address = {}", multiTenantManager.getCurrentTenant(), address);
        final byte[] addr = address.getAddress();
        if (addr.length == 4) {
            List<String> listOfParts = Arrays.asList(address.toString().substring(1).split("\\."));
            Collections.reverse(listOfParts);
            return String.join(".", listOfParts);
        }
        final var sb = new StringBuilder();
        final var nibbles = new int[2];
        for (int i = addr.length - 1; i >= 0; i--) {
            nibbles[0] = (addr[i] & 0xFF) >> 4;
            nibbles[1] = addr[i] & 0xFF & 0xF;
            for (int j = nibbles.length - 1; j >= 0; j--) {
                sb.append(Integer.toHexString(nibbles[j]));
                if (i > 0 || j > 0)
                    sb.append(".");
            }
        }
        String reverseIp = sb.toString();
        log.debug("DiggerService:reverseIp finished with tenant = {}, reverseIp = {}", multiTenantManager.getCurrentTenant(), reverseIp);
        return reverseIp;
    }

    public Long getAsnFromDigResult(String result) {
        log.debug("DiggerService:getAsnFromDigResult started with tenant = {}, result = {}", multiTenantManager.getCurrentTenant(), result);
        if (result.isEmpty()) {
            return 0L;
        }
        String asn = result.substring(1).split(" ")[0];
        Long asnValue = Long.valueOf(asn);
        log.debug("DiggerService:getAsnFromDigResult finished with tenant = {}, asn = {}", multiTenantManager.getCurrentTenant(), asnValue);
        return asnValue;
    }

    public String getProviderNameFromDigResult(String result) {
        log.debug("DiggerService:getProviderNameFromDigResult started with tenant = {}, result = {}", multiTenantManager.getCurrentTenant(), result);
        String[] rawList = result.split("\\|");
        int lastIndex = rawList.length - 1;
        String rawProviderName = rawList[lastIndex].trim();
        String providerName = rawProviderName.substring(0, rawProviderName.length() - 1);
        log.debug("DiggerService:getProviderNameFromDigResult finished with tenant = {}, providerName = {}", multiTenantManager.getCurrentTenant(), providerName);
        return providerName;
    }

    public Optional<String> getProviderByIpAddress(String ip) {
        log.debug("DiggerService:getProviderByIpAddress started with tenant = {}, ip = {}", multiTenantManager.getCurrentTenant(), ip);
        try {
            var response = getCityResponseByIpAddress(ip).get();
            Optional<String> providerIsp = Optional.ofNullable(response.getTraits()).map(Traits::getIsp);
            log.debug("DiggerService:getProviderByIpAddress finished successfully with tenant = {}, providerIsp = {}", multiTenantManager.getCurrentTenant(), providerIsp);
            return providerIsp;
        } catch (NoSuchElementException e) {
            log.debug("attempt to get provider via external API: " + e.getMessage());
            try {
                var ipAddress = InetAddress.getByName(ip);
                var asn = digASN(ipAddress);
                var providerName = getProviderByASN(asn);
                return Optional.of(providerName);
            } catch (UnknownHostException unknownHostException) {
                log.debug("final during getting provider by old approach: " + e.getMessage());
            }
            return Optional.empty();
        }
    }

    @Cacheable("cityResponse")
    public Optional<CityResponse> getCityResponseByIpAddress(String ip) {
        log.debug("DiggerService:getCityResponseByIpAddress started with tenant = {}, ip = {}", multiTenantManager.getCurrentTenant(), ip);
        try (WebServiceClient client = new WebServiceClient.Builder(maxMindConfig.getAccountId(), maxMindConfig.getLicenseKey()).build()) {
            var ipAddress = InetAddress.getByName(ip);
            CityResponse response = client.city(ipAddress);
            log.debug("DiggerService:getCityResponseByIpAddress finished with tenant = {}, cityResponse = {}", multiTenantManager.getCurrentTenant(), response);
            return Optional.of(response);
        } catch (IOException | GeoIp2Exception e) {
            log.error("DiggerService:getCityResponseByIpAddress finished ERROR with tenant = {}", multiTenantManager.getCurrentTenant(), e);
            return Optional.empty();
        }
    }
}
