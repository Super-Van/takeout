package com.van.takeout.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class Utils {
    public static R makeFieldErrors(BindingResult bindingResult) {
        R r = R.error("校验有误");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            r.add(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return r;
    }
}
