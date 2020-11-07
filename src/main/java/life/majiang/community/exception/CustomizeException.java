package life.majiang.community.exception;

public class CustomizeException extends RuntimeException {
    private String message;
    private Integer code;

    public CustomizeException(CustomizeErrorCode errorCode) {
        message = errorCode.getErrMessage();
        code = errorCode.getErrCode();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() { return code; }
}
