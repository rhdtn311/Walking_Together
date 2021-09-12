package backend.server.DTO.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseDTO {
    private String message;
    private Object data;
    private final Long status = 200L;

    @Builder
    public ResponseDTO(final String message,
                       final Object data) {
        this.message = message;
        this.data = data;
    }
}
