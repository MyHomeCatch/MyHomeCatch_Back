package org.scoula.common.response;

import java.util.Map;

public class CommonResponse {
    public static Map<String, Object> response(String message) {
        return Map.of("message", message);
    }

}

