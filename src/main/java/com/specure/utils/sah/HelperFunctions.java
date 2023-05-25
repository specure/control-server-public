package com.specure.utils.sah;


import com.google.common.net.InetAddresses;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.xbill.DNS.Record;
import org.xbill.DNS.*;

import java.net.InetAddress;
import java.util.List;
import java.util.Locale;

@UtilityClass
public class HelperFunctions {

    private static final int DNS_TIMEOUT = 1;
    private static Logger logger = LoggerFactory.getLogger(HelperFunctions.class);

    public String anonymizeIp(final InetAddress inetAddress) {
        try {
            final byte[] address = inetAddress.getAddress();
            address[address.length - 1] = 0;
            if (address.length > 4) {
                for (int i = 6; i < address.length; i++)
                    address[i] = 0;
            }

            String result = InetAddresses.toAddrString(InetAddress.getByAddress(address));
            if (address.length == 4)
                result = result.replaceFirst(".0$", "");
            return result;
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getRoamingType(final MessageSource messageSource, final int roamingType, Locale locale) {
        switch (roamingType) {
            case 0:
                return messageSource.getMessage("value_roaming_none", null, locale);
            case 1:
                return messageSource.getMessage("value_roaming_national", null, locale);
            case 2:
                return messageSource.getMessage("value_roaming_international", null, locale);
        }
        return "?";
    }

    public static String getReverseDNS(final InetAddress adr) {
        try {
            final Name name = ReverseMap.fromAddress(adr);

            final Lookup lookup = new Lookup(name, Type.PTR);
            SimpleResolver simpleResolver = new SimpleResolver();
            simpleResolver.setTimeout(DNS_TIMEOUT);
            lookup.setResolver(simpleResolver);
            lookup.setCache(null);
            final Record[] records = lookup.run();
            if (lookup.getResult() == Lookup.SUCCESSFUL)
                for (final Record record : records)
                    if (record instanceof PTRRecord) {
                        final PTRRecord ptr = (PTRRecord) record;
                        return ptr.getTarget().toString();
                    }
        } catch (final Exception e) {
        }
        return null;
    }

    public static String getASName(final long asn) {
        try {
            final Name postfix = Name.fromConstantString("asn.cymru.com.");
            final Name name = new Name(String.format("AS%d", asn), postfix);
            System.out.println("lookup: " + name);

            final Lookup lookup = new Lookup(name, Type.TXT);
            lookup.setResolver(new SimpleResolver());
            lookup.setCache(null);
            final Record[] records = lookup.run();
            if (lookup.getResult() == Lookup.SUCCESSFUL)
                for (final Record record : records)
                    if (record instanceof TXTRecord) {
                        final TXTRecord txt = (TXTRecord) record;
                        @SuppressWarnings("unchecked") final List<String> strings = txt.getStrings();
                        if (strings != null && !strings.isEmpty()) {
                            System.out.println(strings);

                            final String result = strings.get(0);
                            final String[] parts = result.split(" ?\\| ?");
                            if (parts != null && parts.length >= 1)
                                return parts[4];
                        }
                    }
        } catch (final Exception e) {
        }
        return null;
    }

    public static String getAScountry(final long asn) {
        try {
            final Name postfix = Name.fromConstantString("asn.cymru.com.");
            final Name name = new Name(String.format("AS%d", asn), postfix);
            System.out.println("lookup: " + name);

            final Lookup lookup = new Lookup(name, Type.TXT);
            lookup.setResolver(new SimpleResolver());
            lookup.setCache(null);
            final Record[] records = lookup.run();
            if (lookup.getResult() == Lookup.SUCCESSFUL)
                for (final Record record : records)
                    if (record instanceof TXTRecord) {
                        final TXTRecord txt = (TXTRecord) record;
                        @SuppressWarnings("unchecked") final List<String> strings = txt.getStrings();
                        if (strings != null && !strings.isEmpty()) {
                            final String result = strings.get(0);
                            final String[] parts = result.split(" ?\\| ?");
                            if (parts != null && parts.length >= 1)
                                return parts[1];
                        }
                    }
        } catch (final Exception e) {
        }
        return null;
    }
    public static String getNetworkTypeName(final Integer type) {
        if (type == null)
            return "UNKNOWN";
        switch (type) {
            case 1:
            case 16:
                return "2G (GSM)";
            case 2:
                return "2G (EDGE)";
            case 3:
                return "3G (UMTS)";
            case 4:
                return "2G (CDMA)";
            case 5:
                return "2G (EVDO_0)";
            case 6:
                return "2G (EVDO_A)";
            case 7:
                return "2G (1xRTT)";
            case 8:
                return "3G (HSDPA)";
            case 9:
                return "3G (HSUPA)";
            case 10:
                return "3G (HSPA)";
            case 11:
                return "2G (IDEN)";
            case 12:
                return "2G (EVDO_B)";
            case 13:
                return "4G (LTE)";
            case 14:
                return "2G (EHRPD)";
            case 15:
                return "3G (HSPA+)";
            case 19:
                return "4G (LTE CA)";
            case 20:
                return "5G (NR)";
            case 40:
                return "4G (+5G)";
            case 41:
                return "5G (NR)";
            case 42:
                return "5G (NR)";
            case 97:
                return "CLI";
            case 98:
                return "BROWSER";
            case 99:
                return "WLAN";
            case 101:
                return "2G/3G";
            case 102:
                return "3G/4G";
            case 103:
                return "2G/4G";
            case 104:
                return "2G/3G/4G";
            case 105:
                return "MOBILE";
            case 106:
                return "Ethernet";
            case 107:
                return "Bluetooth";
            default:
                return "UNKNOWN";
        }
    }
}
