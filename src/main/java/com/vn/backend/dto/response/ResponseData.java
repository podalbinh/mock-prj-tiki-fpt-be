package com.vn.backend.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.vn.backend.configs.DeviceInfoContext;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
public class ResponseData<T> implements Serializable {
    private static final long serialVersionUID = 2405172041950251807L;

    private final String device;
    private final int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ResponseData(int code, String device, T data){
        this.code = code;
        this.device = device;
        this.data = data;
    }

    public static <T> ResponseData<T> success( T data) {
        return new ResponseData<>(HttpStatus.OK.value(), DeviceInfoContext.get() , data);
    }

    public static <T> ResponseData<T> created(T data) {
        return new ResponseData<>(HttpStatus.CREATED.value(), DeviceInfoContext.get(), data);
    }

    public static <T> ResponseData<T> badRequest(T data) {
        return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), DeviceInfoContext.get(), data);
    }

    public static <T> ResponseData<T> notFound(T data) {
        return new ResponseData<>(HttpStatus.NOT_FOUND.value(), DeviceInfoContext.get(), data);
    }

    public static <T> ResponseData<T> error(T data) {
        return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), DeviceInfoContext.get(), data);
    }

    public static <T> ResponseData<T> unauthorized(T data) {
        return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), DeviceInfoContext.get(), data);
    }


}
