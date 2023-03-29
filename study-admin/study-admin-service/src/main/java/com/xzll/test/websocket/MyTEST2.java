package com.xzll.test.websocket;

//import com.geccocrawler.gecco.pipeline.ConsolePipeline;
//import com.geccocrawler.gecco.spider.Spider;

import com.xzll.test.websocket.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
import java.util.List;

/**
 * @Author: hzz
 * @Date: 2021/11/22 10:04:46
 * @Description:
 */
@Slf4j
public class MyTEST2 implements PageProcessor {


	private Site site = Site.me().setDomain("my.oschina.net");

	@Override
	public void process(Page page) {
		List<String> links = page.getHtml().links().regex("http://my\\.oschina\\.net/flashsword/blog/\\d+").all();
		page.addTargetRequests(links);
		page.putField("title", page.getHtml().xpath("//div[@class='BlogEntity']/div[@class='BlogTitle']/h1").toString());
		page.putField("content", page.getHtml().$("div.content").toString());
		page.putField("tags",page.getHtml().xpath("//div[@class='BlogTags']/a/text()").all());
	}

	@Override
	public Site getSite() {
		return site;

	}

	public static void main(String[] args) throws IOException {
//		Spider.create(new MyTEST2()).addUrl("http://my.oschina.net/flashsword/blog")
//				.addPipeline(new ConsolePipeline()).run();

//		MessageFormat.format(RECEIVE_URL, serverInfoDTO.getAddr(), serverInfoDTO.getPort())


		HttpClientUtil httpClientUtil = new HttpClientUtil();

		String post="{\"appId\":100098090,\"keyword\":\"\",\"startTime\":\"2021-11-01\",\"endTime\":\"2021-11-22\",\"dataFrom\":\"ALL\",\"messageStatus\":\"ALL\",\"page\":1,\"rows\":20}";


		String netRequest = httpClientUtil.postRequest("https://vpush.vivo.com.cn/platform/message/notifications/list", post);
		System.out.println(netRequest);


		String url="http://search.jd.com/Search?keyword=Python&enc=utf-8&book=y&wq=Python&pvid=33xo9lni.p4a1qb";
//		List<JdModel> bookdatas=URLFecter.URLParser(client, url);



		HttpGet request = new HttpGet("https://vpush.vivo.com.cn/#/secondPage/msgCenter");


//		request.setHeader("authorization","oauth " + );
//request.
//		System.out.println(request);









		Document doc = Jsoup.connect("https://vpush.vivo.com.cn/#/secondPage/msgCenter").get();
		log.info("title",doc.title());
		Elements newsHeadlines = doc.select("#app > div.vContent > div.secondPage > div > div.content_right > div > div > div.el-tabs__content");
		for (Element headline : newsHeadlines) {
			log.info("{},{}", headline.attr("title"), headline.absUrl("href"));
		}
	}


}
