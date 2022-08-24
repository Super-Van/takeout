package com.van.takeout.util;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class R<T> implements Serializable {
    private static final long serialVersionUID = -4805188353281784016L;
    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //成功条件下的数据

    private Map<String, Object> map = new HashMap(); //成功或失败条件下的动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
