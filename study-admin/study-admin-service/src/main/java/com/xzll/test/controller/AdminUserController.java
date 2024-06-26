package com.xzll.test.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.xzll.admin.api.dto.AdminUserDTO;
import com.xzll.common.base.XzllResponse;
import com.xzll.test.config.Excel2ExcelDataListener;
import com.xzll.test.entity.ExcelResult;
import com.xzll.test.entity.ExcelSource;
import com.xzll.test.service.AdminUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/22 18:10
 * @Description:
 */
@Slf4j
@Api(tags = "用户管理-后台")
@RestController
@RequestMapping("/adminUser")
public class AdminUserController {

	@Autowired
	private AdminUserService adminUserService;

	@GetMapping("/findByUserName")
	@ApiOperation(value = "根据username查找用户", notes = "根据username查找用户")
	public List<AdminUserDTO> findByUserName(@RequestParam(value = "username", required = true) String username) {
		log.info("根据姓名模糊匹配人员列表,入参:{}",username);
		return adminUserService.findByUserNameForTestTrace(username);
	}


	//--------------------------------------------------下边为测试agent所开发的接口----------------------------------------------------




	@PostMapping("/addUser")
	@ApiOperation(value = "添加人员信息", notes = "添加人员信息")
	public XzllResponse<Integer> addUser(@RequestBody AdminUserDTO user) {
		int i = adminUserService.addUser(user);
		return XzllResponse.createOK(i);
	}

	@PostMapping("/batchAddUser")
	@ApiOperation(value = "添加人员信息", notes = "添加人员信息")
	public XzllResponse<Object> batchAddUser(@RequestBody List<AdminUserDTO> users) {
		adminUserService.batchAddUser(users);
		return XzllResponse.createOK();
	}

	@GetMapping("/findUserByUid")
	@ApiOperation(value = "查询人员信息根据uid", notes = "查询人员信息根据uid")
	public XzllResponse<AdminUserDTO> findUserByUid(@RequestParam(value = "uid", required = true) Long uid) {
		AdminUserDTO userByUid = adminUserService.findUserByUid(uid);
		return XzllResponse.createOK(userByUid);
	}

	@GetMapping("/findListByUserName")
	@ApiOperation(value = "根据username查找用户集合（模糊）", notes = "根据username查找用户集合（模糊）")
	public XzllResponse<List<AdminUserDTO>> findListByUserName(@RequestParam(value = "username", required = true) String username) {
		log.info("根据姓名模糊匹配人员列表,入参:{}",username);
		List<AdminUserDTO> result = adminUserService.findByUserNameForTestAgent(username);
		return XzllResponse.createOK(result);
	}

	@PostMapping("/updateUserByUid")
	@ApiOperation(value = "更新人员信息根据uid", notes = "更新人员信息根据uid")
	public XzllResponse<Integer> updateUserByUid(@RequestBody AdminUserDTO userDTO) {
		int i = adminUserService.updateUserByUid(userDTO);
		return XzllResponse.createOK(i);
	}

	@PostMapping("/batchUpdateUser")
	@ApiOperation(value = "批量更新人员信息根据uid", notes = "批量更新人员信息根据uid")
	public XzllResponse<Integer> batchUpdateUser(@RequestBody List<AdminUserDTO> userDTOs) {
		adminUserService.batchUpdateUser(userDTOs);
		return XzllResponse.createOK();
	}

	/**
	 * 为大人提供的接口
	 *
	 * @param file
	 * @param response
	 * @throws IOException
	 */
	@PostMapping(value = "/transfer")
	@ApiOperation("excel转换")
	public void init8MonthData(MultipartFile file, HttpServletResponse response) throws IOException {
		Excel2ExcelDataListener excelDataListener = new Excel2ExcelDataListener();
		InputStream inputStream = new BufferedInputStream(file.getInputStream());
		EasyExcelFactory.read(inputStream, ExcelSource.class, excelDataListener).sheet().doRead();
		List<ExcelSource> list = excelDataListener.getList();

		AtomicInteger num = new AtomicInteger(1);
		AtomicInteger i = new AtomicInteger(1);

		List<ExcelResult> collect = list.stream().map(x -> {
			ExcelResult r = new ExcelResult();
//			if (i.getAndIncrement() % 2 == 0) {
//				r.setZhaiyaolianjie(x.getLianjie());
//			} else {
				r.setNum(num.getAndIncrement());
				LocalDateTime parse = LocalDateTimeUtil.parse(x.getFabushijian(), "yyyy/MM/dd HH:mm:ss");
				String pushTime = LocalDateTimeUtil.format(parse, "yyyy年MM月dd日 HH:mm");
				r.setZhaiyaolianjie("\n" +
						"标题: " + "\n" +
						"发布时间: " + pushTime + "\n" +
						"摘要: " + x.getZhaiyao() + "\n" +
						"投诉要求: 退款" + "\n"
				);

				String format = LocalDateTimeUtil.format(parse, "MM月dd日");
				r.setFabushijian(format);
				r.setTousupingtai("黑猫");
				r.setChulibumen("客服中心");
				r.setDanlingyihang(x.getLianjie());
				r.setXueyuanxinxi(
						"\n" +
								"学员姓名: " + "" + "\n" +
								"电话: " + "\n" +
								"购买课程: " + "\n" +
								"销售人员: " + "\n" +
								"部门: "
				);
				String mmdd = LocalDateTimeUtil.format(LocalDateTime.now(), "MM月dd日");
				r.setChuliguochengmiaosu(
						"\n" +
								"1. " + mmdd + "，xxx." + "\n" +
								"2. " + mmdd + "，“xxx。" + "\n" +
								"3. " + mmdd + "，xxx" + "\n" +
								"xxx"
				);
//			}
			return r;
		}).collect(Collectors.toList());

		// 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setCharacterEncoding("utf-8");
		// 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
		String fileName = URLEncoder.encode("黑猫001", "UTF-8").replaceAll("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

		//内容策略
		WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
		contentWriteCellStyle.setWrapped(Boolean.TRUE);
		//水平居中
		contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
		//垂直居中
		contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		//设置背景为绿色
		contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());

		// 头的策略
		WriteCellStyle headWriteCellStyle = new WriteCellStyle();
		headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
		//注册策略
		HorizontalCellStyleStrategy horizontalCellStyleStrategy =
				new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

		ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(response.getOutputStream(), ExcelResult.class);


		//excelWriterBuilder.registerWriteHandler(new LoopMergeStrategy(2, 3));
		//excelWriterBuilder.registerWriteHandler(new MergeStrategy());

		excelWriterBuilder
				//取消头合并策略
				.automaticMergeHead(false)
				.registerWriteHandler(horizontalCellStyleStrategy)
				.sheet("sheet").doWrite(collect);
		log.info("转换完成!");
	}

}
