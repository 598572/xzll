package com.xzll.test.config;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import java.util.List;


@Slf4j
public class CustomerRowWriteHandler implements CellWriteHandler {


	private Workbook getWorkbook(WriteSheetHolder writeSheetHolder) {
		return writeSheetHolder.getSheet().getWorkbook();
	}

	/**
	 * 设置超链接风格
	 *
	 * @param writeSheetHolder
	 * @return
	 */
	private CellStyle getLinkStyle(WriteSheetHolder writeSheetHolder) {
		Workbook workbook = getWorkbook(writeSheetHolder);
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(true);
		Font font = workbook.createFont();
		font.setFontName("跳转链接");
		font.setFontHeightInPoints((short) 12);
		font.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
		font.setUnderline(Font.U_SINGLE);
		//设置边框
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setFont(font);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		return cellStyle;
	}

	@Override
	public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer integer, Integer integer1, Boolean aBoolean) {

	}

	@Override
	public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer integer, Boolean aBoolean) {

	}

	@Override
	public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {

	}

	@Override
	public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {

		// 这里可以对cell进行任何操作
		log.info("第{}行，第{}列写入完成。", cell.getRowIndex(), cell.getColumnIndex());
		if (!aBoolean && cell.getColumnIndex() == 3) {
			CreationHelper createHelper = writeSheetHolder.getSheet().getWorkbook().getCreationHelper();
			Hyperlink hyperlink = createHelper.createHyperlink(HyperlinkType.URL);
			String stringCellValue = cell.getStringCellValue();
			int i = stringCellValue.indexOf("链接:");
			String substring = stringCellValue.substring(i, stringCellValue.length());
			hyperlink.setAddress(substring);
			hyperlink.setAddress(substring);

			cell.setHyperlink(hyperlink);


		}
	}
}
