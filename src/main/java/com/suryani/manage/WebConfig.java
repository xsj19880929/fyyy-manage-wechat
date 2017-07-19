package com.suryani.manage;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.quidsi.core.platform.DefaultSiteWebConfig;
import com.quidsi.core.platform.web.site.SiteSettings;
import com.quidsi.core.util.Convert;
import com.suryani.manage.common.IPProxyStrategy;
import com.suryani.manage.interceptor.AccessInterceptor;
import com.suryani.manage.interceptor.JspViewInterceptor;
import com.suryani.manage.interceptor.LogOpetationRequiredInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import java.text.SimpleDateFormat;
import java.util.List;

@Configuration
public class WebConfig extends DefaultSiteWebConfig {
    @Inject
    EntityManagerFactory entityManagerFactory;

    @Bean
    public List<HttpMessageConverter<?>> messageConverters() {
        List<HttpMessageConverter<?>> converters = super.messageConverters();
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.setDateFormat(new SimpleDateFormat(Convert.DATE_FORMAT_DATETIME));
                mapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()));
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                mapper.configure(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME, true);
                ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(mapper);
            }
        }
        return converters;
    }

    @Bean
    public SiteSettings siteSettings() {
        SiteSettings settings = new SiteSettings();
        settings.setErrorPage("forward:/error/internal-error");
        settings.setResourceNotFoundPage("forward:/error/resource-not-found");
        return settings;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**", "/images/**", "/img/**", "/js/**", "/font/**").addResourceLocations("/css/", "/images/", "/img/", "/js/", "/font/");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer cfg = new TilesConfigurer();
        cfg.setDefinitions(new String[]{"/WEB-INF/tiles/cfg/tiles-defs.xml"});
        return cfg;
    }

    @Bean
    public TilesViewResolver tilesViewResolver() {
        TilesViewResolver resolver = new TilesViewResolver();
        resolver.setViewClass(org.springframework.web.servlet.view.tiles3.TilesView.class);
        resolver.setContentType("text/html; charset=UTF-8");
        resolver.setOrder(0);
        return resolver;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(5 * 1024 * 1024);
        return multipartResolver;
    }

    @Bean
    public AccessInterceptor accessInterceptor() {
        AccessInterceptor interceptor = new AccessInterceptor();
        interceptor.addFreeAccessUrl("/passport/login");
        interceptor.addFreeAccessUrl("/passport/logout");
        interceptor.addFreeAccessUrl("/error/resource-not-found");
        interceptor.addFreeAccessUrl("/error/internal-error");
        interceptor.addFreeAccessUrl("/upload/callback");
        interceptor.addFreeAccessUrl("ysq/upload/uploadImg");
        return interceptor;
    }

    @Bean
    public JspViewInterceptor jspViewInterceptor() {
        return new JspViewInterceptor();
    }

    @Bean
    public IPProxyStrategy ipProxyStrategy() {
        return new IPProxyStrategy();
    }

    @Bean
    public LogOpetationRequiredInterceptor logOpetationRequiredInterceptor() {
        return new LogOpetationRequiredInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        OpenEntityManagerInViewInterceptor interceptor = new OpenEntityManagerInViewInterceptor();
        interceptor.setEntityManagerFactory(entityManagerFactory);
        registry.addWebRequestInterceptor(interceptor);
        registry.addInterceptor(accessInterceptor());
        registry.addInterceptor(jspViewInterceptor());
        registry.addInterceptor(logOpetationRequiredInterceptor());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/passport/login");
    }
}
