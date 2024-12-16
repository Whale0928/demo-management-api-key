package app.demo.management.key.client;

import app.demo.management.key.jwt.TokenType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PermissionsType {
    READ("읽기"),
    WRITE("쓰기"),
    DELETE("삭제");

    private final String description;

    public static PermissionsType[] defaultPermissions() {
        return new PermissionsType[]{READ};
    }

    public static PermissionsType[] allPermissions() {
        return new PermissionsType[]{READ, WRITE, DELETE};
    }

    public static PermissionsType[] readOnlyPermissions() {
        return new PermissionsType[]{READ};
    }

    public static PermissionsType[] permissionsByTokenType(TokenType tokenType) {
        return switch (tokenType) {
            case FULL -> allPermissions();
            case LIMITED, TEMPORARY -> readOnlyPermissions();
        };
    }
}
