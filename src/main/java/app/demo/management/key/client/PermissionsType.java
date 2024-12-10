package app.demo.management.key.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PermissionsType {
    READ("읽기"),
    WRITE("쓰기"),
    DELETE("삭제");

    private final String description;
}
