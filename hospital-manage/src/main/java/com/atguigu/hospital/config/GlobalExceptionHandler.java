package com.atguigu.hospital.config;

import com.atguigu.hospital.util.YyghException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String error(Exception e){
        e.printStackTrace();
        return "error";
    }

    /**
     * Custom Exception Handling Method
     * @param e
     * @return
     */
    @ExceptionHandler(YyghException.class)
    public String error(YyghException e, Model model){
        model.addAttribute("message", e.getMessage());
        return "error";
    }
}
