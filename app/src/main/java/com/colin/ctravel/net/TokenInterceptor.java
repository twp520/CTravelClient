package com.colin.ctravel.net;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.code() != 200)
            return response;
        Response.Builder builder = response.newBuilder();
        Response clone = builder.build();
        ResponseBody body = clone.body();
        if (body != null) {
            MediaType mediaType = body.contentType();
            if (mediaType != null && isText(mediaType)) {
                String resp = body.string();
                try {
                    JSONObject object = new JSONObject(resp);
                    int code = object.optInt("code");
                    /*if (code == NetConstant.Companion.RESULT_CODE_TOKEN_ERROR() || code == NetConstant.Companion.RESULT_CODE_TOKEN_ERROR())
                        throw new TokenException();
                    if (code == NetConstant.Companion.getRESULT_CODE_SUCCESS() || code == NetConstant.Companion.getRESULT_CODE_REGISTED()) {
                        body = ResponseBody.create(mediaType, resp);
                        return response.newBuilder().body(body).build();
                    } else {
                        throw new ApiException(code, object.optString("msg"));
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }


    private boolean isText(MediaType mediaType) {
        return mediaType.type() != null && mediaType.type().equals("text") || mediaType.subtype() != null &&
                (mediaType.subtype().equals("json") || mediaType.subtype().equals("xml") || mediaType.subtype().equals("html")
                        || mediaType.subtype().equals("webviewhtml") || mediaType.subtype().equals("x-www-form-urlencoded"));
    }
}
