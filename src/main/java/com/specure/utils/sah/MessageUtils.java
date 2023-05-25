package com.specure.utils.sah;

import com.specure.config.ApplicationProperties;
import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class MessageUtils {

    public Locale getLocaleFormLanguage(String language, ApplicationProperties.LanguageProperties languageProperties) {
        return Locale.forLanguageTag(getSafeLanguage(language, languageProperties));
    }

    public String getSafeLanguage(String language, ApplicationProperties.LanguageProperties languageProperties) {
        if (languageProperties.getSupportedLanguages().contains(language)) {
            return language;
        } else {
            return languageProperties.getDefaultLanguage();
        }
    }
}
