package com.xzll.common.email.selector;

import com.xzll.common.alarm.EnableAlarmNotice;
import com.xzll.common.alarm.entity.enums.AlarmNoticeTypeEnum;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: hzz
 * @Date: 2021/9/18 17:01:08
 * @Description: 报警方式选择器 根据EnableAlarmNotice注解中的types字段来进行选择性注入bean
 */
public class AlarmServiceSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata annotationMetadata) {

		//获取注解上的type,然后根据types数组选择性的创建bean 放到IOC中
		Map<String, Object> map = annotationMetadata.getAnnotationAttributes(EnableAlarmNotice.class.getName(), true);
		List<String> needInIOCBean = new ArrayList<>();
		if (map != null && !map.isEmpty()) {
			String[] types = (String[]) map.get("types");
			if (Objects.isNull(types) || types.length == 0)
				return new String[0];

			for (int i = 0; i < types.length; i++) {
				needInIOCBean.add(AlarmNoticeTypeEnum.getClassFullName(types[i]));
			}
		}
		return needInIOCBean.toArray(new String[0]);
	}
}
