package c2.elastic.bucket.ContentService.exception;

public class ContentServiceInvalidInputException extends RuntimeException {
    public ContentServiceInvalidInputException(String message) {
        super(message);
    }

    public ContentServiceInvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
