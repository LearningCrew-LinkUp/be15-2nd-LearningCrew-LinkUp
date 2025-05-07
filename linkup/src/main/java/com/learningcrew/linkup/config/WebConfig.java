package com.learningcrew.linkup.config;

import com.learningcrew.linkup.common.util.StringToYearMonthConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;


@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final StringToYearMonthConverter stringToYearMonthConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToYearMonthConverter);
    }

    @Value("${image.image-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}

