package com.van.takeout.util;

import com.van.takeout.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ControllerAdvice
@ResponseBody
public class CommonExceptionHandler {
    /**
     * @param exception 完整性约束，具体得看是哪一种
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> uniqueUsername(SQLIntegrityConstraintViolationException exception) {
        log.info(exception.getMessage());
        //目前唯一约束仅用户名有
        if (exception.getMessage().contains("Duplicate entry")) {
            String str = exception.getMessage().split(" ")[2];
            return R.error(str.substring(1, str.length() - 1) + "已存在");
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> custom(CustomException exception) {
        log.info(exception.getMessage());
        return R.error(exception.getMessage());
    }
}
