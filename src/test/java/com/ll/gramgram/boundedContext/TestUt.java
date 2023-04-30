package com.ll.gramgram.boundedContext;

import com.ll.gramgram.standard.util.Ut;

public class TestUt {
    public static boolean setFieldValue(Object o, String fieldName, Object value) {
        return Ut.reflection.setFieldValue(o, fieldName, value);
    }
}