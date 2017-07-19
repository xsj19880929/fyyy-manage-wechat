package com.suryani.manage.util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import net.sf.json.JSONObject;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class JsonMessageConvertor extends AbstractHttpMessageConverter<Object> implements GenericHttpMessageConverter<Object> {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public JsonMessageConvertor() {
        super(new MediaType("application", "json", DEFAULT_CHARSET), new MediaType("application", "*+json", DEFAULT_CHARSET));
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return canRead(clazz, null, mediaType);
    }

    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return super.canRead(mediaType);
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return readInternal(contextClass, inputMessage);
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        JSONObject jsonObj = JSONObject.fromObject(inputMessage.getBody().toString());
        return JSONObject.toBean(jsonObj, clazz);
    }

    @Override
    protected void writeInternal(Object t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        String s=JSONObject.fromObject(t).toString();
        
        outputMessage.getBody().write(s.getBytes());
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

}
