package org.xvpt.website.entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record RestBean<T>(int code, T data, String message) {
    @Contract("_ -> new")
    public static <T> @NotNull RestBean<T> success(T data) {
        return new RestBean<>(200, data, "Request successful");
    }

    @Contract(" -> new")
    public static <T> @NotNull RestBean<T> success() {
        return success(null); // no data
    }

    public static <T> @NotNull RestBean<T> unauthorized(String message) {
        return failure(401, message);
    }

    @Contract("_ -> new")
    public static <T> @NotNull RestBean<T> unauthorized(@NotNull RuntimeException exception) {
        return unauthorized(exception.getMessage());
    }

    public static <T> @NotNull RestBean<T> forbidden(String message) {
        return failure(403, message);
    }

    @Contract("_ -> new")
    public static <T> @NotNull RestBean<T> forbidden(@NotNull RuntimeException exception) {
        return forbidden(exception.getMessage());
    }


    public static <T> @NotNull RestBean<T> failure(int code, String message) {
        return new RestBean<>(code, null, message);
    }

    public static <T> @NotNull RestBean<T> failure(int code, @NotNull RuntimeException exception) {
        return failure(code, exception.getMessage());
    }

    @Contract("_ -> new")
    public static <T> @NotNull RestBean<T> badRequest(String message) {
        return failure(400, message);
    }

    @Contract(" -> new")
    public static <T> @NotNull RestBean<T> badRequest() {
        return badRequest("Bad request");
    }

    public String toJson() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
}