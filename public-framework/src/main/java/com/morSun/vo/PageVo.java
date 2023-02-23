package com.morSun.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PageVo {
    private  List rows;
    private Long total;
}
