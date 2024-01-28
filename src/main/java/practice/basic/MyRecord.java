package practice.basic;

import org.springframework.util.StringUtils;

public record MyRecord(String id, String name, String occupation) {
    public MyRecord {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("id can not be null");
        }
    }
}
