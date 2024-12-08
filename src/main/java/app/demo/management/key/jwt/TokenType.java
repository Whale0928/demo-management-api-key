package app.demo.management.key.jwt;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TokenType {
    FULL(0, "FULL", "모든 권한 허용"),
    LIMITED(1, "LIMITED", "제한적 권한 허용"),
    TEMPORARY(2, "TEMPORARY", "일시적 권한 허용");

    private final int level;
    private final String authority;
    private final String description;

    public static TokenType fromValue(int source) {
        return Arrays.stream(TokenType.values())
                .filter(type -> type.getLevel() == source)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("값에 대한 TokenType이 없습니다.: " + source));
    }
}
