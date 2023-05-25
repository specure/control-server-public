package com.specure.utils.sah;

import com.specure.common.model.elastic.BasicTest;
import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class TimeUtils {

    public String getTimeStringFromTest(BasicTest test, Locale locale) {
        Date date = Optional.ofNullable(test.getTimestamp())
                .map(Timestamp::toInstant)
                .map(Date::from)
                .orElse(null);
        TimeZone timeZone = getTimeZoneFromBasicTest(test);
        return getTimeString(date, timeZone, locale);
    }

    public String getTimeString(Date date, TimeZone timeZone, Locale locale) {
        if (date == null)
            return null;
        final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(date);
    }

    public Double formatToSeconds(Long timeNs) {
        return timeNs != null && timeNs > 0 ? Math.round(timeNs / 1000000.0) / 1000.0 : 0.000;
    }

    public Long formatToSecondsRound(Long timeNs) {
        String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(timeNs),
                TimeUnit.MILLISECONDS.toSeconds(timeNs) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeNs)));
        return timeNs != null && timeNs > 0 ? TimeUnit.NANOSECONDS.toSeconds(timeNs) : 0;
    }

    public static TimeZone getTimeZoneFromBasicTest(BasicTest test) {
        return TimeZone.getTimeZone(ZonedDateTime.parse(test.getMeasurementDate()).getZone());
    }
}
