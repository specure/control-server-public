package com.specure.common.exception;


import com.specure.common.constant.ErrorMessage;

public class AdHocCampaignStatusException extends RuntimeException {

    public AdHocCampaignStatusException(String badAdHocCampaignStatus) {
        super(String.format(ErrorMessage.BAD_CAMPAIGN_STATUS, badAdHocCampaignStatus));
    }
}
