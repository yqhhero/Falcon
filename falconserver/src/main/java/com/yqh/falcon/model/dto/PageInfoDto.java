package com.yqh.falcon.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageInfoDto<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private Integer pages;
    private T data;
}
