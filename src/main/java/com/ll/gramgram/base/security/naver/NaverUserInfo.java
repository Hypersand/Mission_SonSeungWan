package com.ll.gramgram.base.security.naver;

import java.util.Map;

public class NaverUserInfo {
    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    private Map<String, Object> attributes;

    public String getId() {
        return (String) attributes.get("id");
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

}
