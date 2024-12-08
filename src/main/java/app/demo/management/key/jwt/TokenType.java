package app.demo.management.key.jwt;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenType {
    ACCESS(0, "USER_ACCESS", "사용자 요청 인증을 위한 일반 액세스 토큰"),
    REFRESH(1, "USER_REFRESH", "액세스 토큰 갱신을 위한 리프레시 토큰"),
    TEMPORARY(2, "USER_TEMP", "특정한 단기적 사용 목적을 위한 임시 토큰");

    private final int value;
    private final String subject;
    private final String description;

    public static TokenType fromValue(int intValue) {
        for (TokenType type : TokenType.values()) {
            if (type.getValue() == intValue) {
                return type;
            }
        }
        throw new IllegalArgumentException("값에 대한 TokenType이 없습니다.: " + intValue);
    }
}
