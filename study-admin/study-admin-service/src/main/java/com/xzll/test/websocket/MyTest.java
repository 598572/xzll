//package com.xzll.test.websocket;
//
//import com.geccocrawler.gecco.GeccoEngine;
//import com.geccocrawler.gecco.annotation.*;
//import com.geccocrawler.gecco.spider.HtmlBean;
//
///**
// * @Author: hzz
// * @Date: 2021/11/19 18:34:48
// * @Description:
// */
//
//@Gecco(matchUrl="https://vpush.vivo.com.cn/#/secondPage/msgCenter", pipelines="consolePipeline")
//public class MyTest implements HtmlBean {
//
//	private static final long serialVersionUID = -7127412585200687225L;
//
//	@RequestParameter("user")
//	private String user;
//
//	@RequestParameter("project")
//	private String project;
//
//	@Text
//	@HtmlField(cssPath="c#app > div.vContent > div.secondPage > div > div.content_right > div > div > div.el-tabs__content")
//	private String star;
//
////	@Text
////	@HtmlField(cssPath=".pagehead-actions li:nth-child(3) .social-count")
////	private String fork;
////
////	@Html
////	@HtmlField(cssPath=".entry-content")
////	private String readme;
//
//	public String getReadme() {
//		return readme;
//	}
//
//	public void setReadme(String readme) {
//		this.readme = readme;
//	}
//
//	public String getUser() {
//		return user;
//	}
//
//	public void setUser(String user) {
//		this.user = user;
//	}
//
//	public String getProject() {
//		return project;
//	}
//
//	public void setProject(String project) {
//		this.project = project;
//	}
//
//	public String getStar() {
//		return star;
//	}
//
//	public void setStar(String star) {
//		this.star = star;
//	}
//
//	public String getFork() {
//		return fork;
//	}
//
//	public void setFork(String fork) {
//		this.fork = fork;
//	}
//
//	public static void main(String[] args) {
//		GeccoEngine.create()
//				.classpath("c#app > div.vContent > div.secondPage > div > div.content_right > div > div > div.el-tabs__content")
//				.start("https://vpush.vivo.com.cn/#/secondPage/msgCenter")
//				.thread(1)
//				.interval(2000)
//				.loop(true)
//				.mobile(false)
//				.start();
//
//
//	}
//}
