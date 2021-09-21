package com.xzll.common.email.config;

import com.xzll.common.email.EmailService;
import com.xzll.common.email.selector.MyImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author: hzz
 * @Date: 2021/9/18 16:16:28
 * @Description:
 */
@Import({EmailService.class,
		RedisConfigurationExample.class,
		MyImportBeanDefinitionRegistrar.class
})
//,AlarmServiceSelector.class}//如果在这里添加的话，AlarmServiceSelector的seletor将不会生效，而是按照@Service,如果注掉@Service的话，bean将会no such
@Configuration
public class ImportJavaConfigList {


}
