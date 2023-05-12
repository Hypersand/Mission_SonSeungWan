package com.ll.gramgram.boundedContext.likeablePerson.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ToListSearchForm {

    private final String gender;
    private final Integer attractiveTypeCode;

    private final Integer sortCode;

}
