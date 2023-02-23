package com.morSun.vo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @package_name: com.morSun.vo.excel
 * @date: 2023/1/6
 * @week: 星期五
 * @message: 分类信息返回体，excel使用
 * @author: morSun
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EasyCategory {

    @ExcelProperty("状态： 0正常，1禁用")
    private String status;
    @ExcelProperty("分类名")
    private String name;
    @ExcelProperty("描述")
    private String description;
}
