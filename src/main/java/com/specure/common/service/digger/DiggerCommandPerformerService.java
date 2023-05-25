package com.specure.common.service.digger;

import com.specure.multitenant.MultiTenantManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiggerCommandPerformerService {

    private final MultiTenantManager multiTenantManager;

    public String dig(String command) {
        log.debug("DiggerCommandPerformerService:dig started with tenant = {}, command = {}", multiTenantManager.getCurrentTenant(), command);
        StringBuilder sb = new StringBuilder();

        BufferedReader reader = null;
        Process p;

        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String digResult = sb.toString();
        log.debug("DiggerCommandPerformerService:dig finished with tenant = {}, digResult = {}", multiTenantManager.getCurrentTenant(), digResult);
        return digResult;
    }
}
