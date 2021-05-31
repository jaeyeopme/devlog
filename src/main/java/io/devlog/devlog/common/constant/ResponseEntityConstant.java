package io.devlog.devlog.common.constant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityConstant {

    public static ResponseEntity<HttpStatus> RESPONSE_OK = ResponseEntity.status(HttpStatus.OK).build();
    public static ResponseEntity<HttpStatus> RESPONSE_CREATED = ResponseEntity.status(HttpStatus.CREATED).build();

}
