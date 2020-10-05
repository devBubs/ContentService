package c2.elastic.bucket.ContentService.exception;

public class ContentServiceException extends RuntimeException {
    public ContentServiceException(String message) {
        super(message);
    }

    public ContentServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
