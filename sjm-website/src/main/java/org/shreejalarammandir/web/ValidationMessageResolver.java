package org.shreejalarammandir.web;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ValidationMessageResolver {

    private final MessageSource messageSource;

    public ValidationMessageResolver(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String resolveKey(String key) {
        return messageSource.getMessage(key, null, key, LocaleContextHolder.getLocale());
    }
}
