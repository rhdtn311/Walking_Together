package backend.server.security.exception;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException() {
        super("유효하지 않은 입력입니다.");
    }
}
