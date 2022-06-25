package advisor.external.exceptions;

public class UnknownCategoryException extends Exception{
    public UnknownCategoryException() {
        super();
    }

    public UnknownCategoryException(String message) {
        super(message);
    }

    public UnknownCategoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownCategoryException(Throwable cause) {
        super(cause);
    }

    protected UnknownCategoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
