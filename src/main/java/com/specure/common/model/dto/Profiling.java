package com.specure.common.model.dto;

import java.sql.Timestamp;
import java.util.TimeZone;

public class Profiling {
    private Timestamp start;

    public void start() {
        setDefaultTimezone();
        start = new Timestamp(System.currentTimeMillis());
    }

    public Long finishAndGetDuration() {
        setDefaultTimezone();
        Timestamp finish = new Timestamp(System.currentTimeMillis());
        return (finish.getTime() - start.getTime());
    }
    private void setDefaultTimezone(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
