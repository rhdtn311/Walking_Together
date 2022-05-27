package backend.server.exception;

public enum ErrorCode {

    // auth
    LOGIN_USER_NOT_FOUND_EXCEPTION(401L, "존재하지 않는 회원입니다.", 401),
    AUTHORIZATION_FAIL_EXCEPTION(403L, "요청 권한이 존재하지 않습니다.", 403),

    // Activity
    ACTIVITY_ALREADY_IN_PROGRESS(400L, "이미 진행 중인 활동이 있습니다.", 400),
    PARTNER_NOT_FOUND(404L, "파트너가 존재하지 않습니다.", 404),
    MEMBER_NOT_FOUND(405L, "사용자가 존재하지 않습니다.", 404),
    ACTIVITY_NOT_FOUND(404L, "활동이 존재하지 않습니다.", 404),
    ACTIVITY_ALREADY_DONE(407L, "이미 종료된 활동입니다.", 400),
    MINIMUM_ACTIVITY_TIME_NOT_SATISFY(500L, "최소 활동 시간을 초과하지 못했습니다.", 400),
    MINIMUM_ACTIVITY_DISTANCE_NOT_SATISFY (501L,"최소 활동 거리를 초과하지 못했습니다.",400),
    ACTIVITY_ABNORMAL_DONE_WITHOUT_MINIMUM_TIME(502L, "활동이 비정상적으로 종료되었고 시간을 충족시키지 못했습니다.", 400),
    ACTIVITY_ABNORMAL_DONE_WITHOUT_MINIMUM_DISTANCE(503L,"활동이 비정상적으로 종료되었고 거리를 충족시키지 못했습니다.",400),
    ACTIVITY_DONE_PHOTO_NOT_SEND(405L, "종료 사진이 전송되지 않았습니다.", 400),
    ACTIVITY_MAP_PHOTO_NOT_SEND(406L, "맵 경로 사진이 전송되지 않았습니다.", 400),

    // Feed
    REVIEW_NOT_RECEIVE(405L, "소감문을 작성해주세요.", 405),
    ACTIVE_ACTIVITY_NOT_WRITE_REVIEW(406L, "진행 중인 활동은 소감문 작성이 불가능합니다.", 400),

    // MyPage
    PARTNER_HAVE_ACTIVITY(400L, "활동을 가지고 있는 파트너입니다.", 400),

    // Notice
    DATA_NOT_FOUND_IN_PAGE(400L, "해당 페이지에 데이터가 존재하지 않습니다.", 404),
    NOTICE_NOT_FOUND(400L, "존재하지 않는 게시물입니다.", 404),

    // User
    STDID_DUPLICATION(409L, "중복된 회원입니다.", 409),
    EMAIL_DUPLICATION(409L, "중복된 이메일입니다.", 409),
    PHONE_NUMBER_DUPLICATION(409L, "중복된 휴대폰 번호입니다.", 409);

    private final Long code;
    private final String message;
    private final int status;

    ErrorCode(final Long code, final String message, final int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public Long getCode() {
        return this.code;
    }

    public int getStatus() {
        return this.status;
    }
}
