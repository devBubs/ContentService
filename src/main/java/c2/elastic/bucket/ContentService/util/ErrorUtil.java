package c2.elastic.bucket.ContentService.util;

import c2.elastic.bucket.GenBucket.model.GenericError;
import c2.elastic.bucket.GenBucket.model.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ErrorUtil {

    public <T> ResponseEntity<GenericResponse<T>> getErrorResponse(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildGenericResponse(e));
    }

    public <T> ResponseEntity<GenericResponse<T>> getFaultResponse(String message) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildGenericResponse(message));
    }

    private <T> GenericResponse<T> buildGenericResponse(Exception e) {
        GenericError error = GenericError.builder()
                .errorMessage(e.getMessage())
                .build();
        return GenericResponse.<T>builder()
                .errors(Collections.singletonList(error))
                .build();
    }

    private <T> GenericResponse<T> buildGenericResponse(String message) {
        GenericError error = GenericError.builder()
                .errorMessage(message)
                .build();
        return GenericResponse.<T>builder()
                .errors(Collections.singletonList(error))
                .build();

    }
}