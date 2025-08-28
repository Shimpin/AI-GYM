package com.situ.kdz2026.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JsonResult<T> {
    private int code;
    private boolean success;
    private String msg;
    private T data;

    public static JsonResult<?> success() {
        return success(null);
    }

    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<>(200, true, "操作成功", data);
    }
    public static <T> JsonResult<T> success(T data,String msg) {
        return new JsonResult<>(200, true, msg, data);
    }

    public static JsonResult<?> fail(int code, String msg) {
        return new JsonResult<>(code, false, msg, null);
    }

    public static JsonResult<?> fail(String msg) {
        return fail(500, msg);
    }
}
