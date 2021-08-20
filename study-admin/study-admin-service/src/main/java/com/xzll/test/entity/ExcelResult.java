package com.xzll.test.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ColumnWidth(12)
@ContentRowHeight(value = 300)
public class ExcelResult {



    @ExcelProperty(value = "序号")
	private Integer num;

    @ExcelProperty(value = "发布时间")
    private String fabushijian;

    @ExcelProperty(value = "投诉平台")
    private String tousupingtai;

    @ExcelProperty(value = "摘要&链接")
	@ColumnWidth(56)
	private String zhaiyaolianjie;

	@ExcelProperty(value = "链接")
	@ColumnWidth(12)
	private String danlingyihang;


    @ExcelProperty(value = "等级")
    private String dengji;

    @ExcelProperty(value = "责任处理部门")
    private String chulibumen="客服中心";

	@ExcelProperty(value = "学员信息")
	@ColumnWidth(22)
    private String xueyuanxinxi;

	@ExcelProperty(value = "处理过程描述")
	@ColumnWidth(60)
	private String chuliguochengmiaosu;



}
