package com.learningcrew.linkup.common.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
public class StringToYearMonthConverter implements Converter<String, YearMonth> {

    @Override
    public YearMonth convert(String source) {
        return YearMonth.parse(source);
    }
}
