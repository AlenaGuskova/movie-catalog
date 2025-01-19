package com.reksoft.moviecatalog.common.local;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalMessageServiceImpl implements LocalMessageService {

    private final MessageSource messageSource;

    @Override
    public String getMessage(String messageCode) {
        var locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageCode, null, locale);
    }
}
