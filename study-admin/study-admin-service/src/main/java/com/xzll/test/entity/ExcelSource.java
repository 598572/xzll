package com.xzll.test.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ColumnWidth(20)
public class ExcelSource {


    @ExcelProperty(value = "标题")
    private String biaoti;

    @ExcelProperty(value = "发布媒体")
    private String fabumeiti;

    @ExcelProperty(value = "链接地址")
    private String lianjie;


    @ExcelProperty(value = "发布时间")
    private String fabushijian;

    @ExcelProperty(value = "摘要")
    private String zhaiyao;

}
