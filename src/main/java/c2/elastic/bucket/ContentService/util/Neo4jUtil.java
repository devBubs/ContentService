package c2.elastic.bucket.ContentService.util;

import c2.elastic.bucket.ContentService.exception.ContentServiceException;
import c2.elastic.bucket.ContentService.exception.ContentServiceInvalidInputException;
import org.neo4j.driver.v1.exceptions.ClientException;
import org.springframework.stereotype.Component;

import static c2.elastic.bucket.ContentService.constants.Neo4jConstants.CONSTRAINT_VALIDATION_ERROR_CODE;

@Component
public class Neo4jUtil {
    public void handleClientException(ClientException e) {
        String errorCode = e.code();
        if (CONSTRAINT_VALIDATION_ERROR_CODE.equals(errorCode)) {
            throw new ContentServiceInvalidInputException(e.getMessage(), e);
        }
        throw new ContentServiceException(e.getMessage(), e);
    }
}
