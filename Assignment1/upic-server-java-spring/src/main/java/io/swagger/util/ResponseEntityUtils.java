package io.swagger.util;

import io.swagger.model.ResponseMsg;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ResponseEntityUtils {
    public static ResponseEntity<ResponseMsg> buildBadRequest(String message) {
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setMessage(message);
        return ResponseEntity.badRequest().body(responseMsg);
    }

    public static ResponseEntity<ResponseMsg> buildNotFoundRequest(String message) {
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setMessage(message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMsg);
    }

    public static ResponseEntity<ResponseMsg> buildBadRequestForInvalidMediaTypeJson(String valueOfHeaderAccept) {
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setMessage(String.format(
            "Expect request header filed Accept contains <application/json> or <*/*>, but it was <%s>.",
            valueOfHeaderAccept));
        return ResponseEntity.badRequest().body(responseMsg);
    }

    private ResponseEntityUtils() {
        throw new UnsupportedOperationException();
    }
}
