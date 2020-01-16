package com.s1243808733.app.http.response;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

public class JsonResponseParser implements ResponseParser<String> {

    @Override
    public void beforeRequest(UriRequest request) throws Throwable {
    }

    @Override
    public void afterRequest(UriRequest request) throws Throwable {
    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        return new Gson().fromJson(result, resultClass);

    }
}


