package com.xzll.test;


import com.xzll.common.alarm.EnableAlarmNotice;
import com.xzll.common.rabbitmq.EnableRabbitMq;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@SpringBootTest(classes = StudyTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringJUnit4ClassRunner.class)
@EnableRabbitMq
@EnableAlarmNotice( types= {"ding_ding",
		"email"})
@Slf4j
public class StudyTestApplicationTest {

}
