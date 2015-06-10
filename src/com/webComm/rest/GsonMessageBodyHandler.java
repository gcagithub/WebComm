package com.webComm.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

//import org.codehaus.jackson.annotate.JsonIgnore;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GsonMessageBodyHandler<T> implements MessageBodyWriter<T>, MessageBodyReader<T> {
    private static ThreadLocal<Boolean> skipDataProtection = new ThreadLocal<Boolean>();

    public GsonMessageBodyHandler() {
    }

    private GsonBuilder getGson() {
        GsonBuilder gson = new GsonBuilder();
        gson.setDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
        gson.setPrettyPrinting();

        if (skipDataProtection.get() == null || !skipDataProtection.get()) {
            gson.addSerializationExclusionStrategy((ExclusionStrategy) new GsonExclusionStrategy());
        }

        return gson;
    }

    @Override
    public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {

        Reader entityReader = new InputStreamReader(entityStream, "UTF-8");
        Type targetType;
        if (Collection.class.isAssignableFrom(type)) {
            targetType = genericType;
        } else {
            targetType = type;
        }

        return getGson().create().<T>fromJson(entityReader, targetType);
    }

    @Override
    public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {

        // convert all except Strings
        if (!String.class.isAssignableFrom(type)) {
            entityStream.write(getGson().create().toJson(t).getBytes("UTF-8"));
        } else {
            entityStream.write(((String) t).getBytes("UTF-8"));
        }
    }

    public static void setSkipDataProtection(boolean status) {
        skipDataProtection.set(status);
    }


    private static class GsonExclusionStrategy implements ExclusionStrategy {
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            if (field.getAnnotation(JsonIgnore.class) != null) {
                return true;
            } else {
                return false;
            }
        }

    }

}