package com.qantas.webcrawler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class JsoupWebCrawlServiceTest {
	@MockBean
	private JsoupService jsoupService;
	
    private JsoupWebCrawlService webCrawlService;
	
	private final String leafHtmlString = "<html><head><title>Google Leaf</title></head><body><h1>My First Heading</h1><p>My first paragraph.</p></body></html>";
	private final String leafUrl = "http://leaf.com";
	private final String leafUrlTitle = "Google Leaf";
	
	private final String topHtmlString = "<html><head><title>Google Top</title></head>"
			+ "<body><h1>My First Heading</h1><p>My first paragraph.</p>"
			+ "<a class='gb_P' data-pid='23' href='"
			+ leafUrl + "'>Gmail</a></body></html>";
	
	private final String topUrl = "http://www.top.com";
	private final String topUrlTitle = "Google Top";
	
	private Set<String> urlVisited;
		
	@Before
	public void setup() throws Exception{
		// setup for each testcase
		urlVisited = new HashSet<String>();
		this.webCrawlService = new JsoupWebCrawlService(this.jsoupService);
	}
	
	@Test
	public void getLinksShouldReturnNulwithThrow() throws Exception {
		when(jsoupService.jsoupGet(leafUrl)).thenThrow(new IOException("Invalid status code or no HTML content!"));
		
		Node node = webCrawlService.getLinks(leafUrl, urlVisited, 0, WebCrawlService.MAX_DEPTH);
		assertThat(node, is(nullValue()));
		verify(jsoupService, times(1)).jsoupGet(eq(leafUrl));
	}
	
	@Test 
	public void getLinksShouldReturnSingleNode() throws Exception {
		Document leafDoc = Jsoup.parse(leafHtmlString);
		when(jsoupService.jsoupGet(leafUrl)).thenReturn(leafDoc);
		
		Node node = webCrawlService.getLinks(leafUrl, urlVisited, 0, WebCrawlService.MAX_DEPTH);
		assertThat(node, is(notNullValue()));
		assertThat(node.getTitle(), is(equalTo(leafUrlTitle)));
		assertThat(node.getUrl(), is(equalTo(leafUrl)));
		assertThat(node.getNodes(), is(empty()));
		
		verify(jsoupService, times(1)).jsoupGet(eq(leafUrl));
	}
	
	@Test
	public void getLinksShouldReturn2LevelNode() throws Exception {
		// setup the leaf document
		Document leafDoc = Jsoup.parse(leafHtmlString);
		when(jsoupService.jsoupGet(leafUrl)).thenReturn(leafDoc);
		
		// setup the top document
		Document topDoc = Jsoup.parse(topHtmlString);
		when(jsoupService.jsoupGet(topUrl)).thenReturn(topDoc);
		
		Node node = webCrawlService.getLinks(topUrl, urlVisited, 0, WebCrawlService.MAX_DEPTH);
		assertThat(node, is(notNullValue()));
		assertThat(node.getTitle(), is(equalTo(topUrlTitle)));
		assertThat(node.getUrl(), is(equalTo(topUrl)));
		assertThat(node.getNodes().size(), is(1));
		assertThat(node.getNodes().get(0).getNodes(), is(empty()));
		
		verify(jsoupService, times(1)).jsoupGet(eq(topUrl));
		verify(jsoupService, times(1)).jsoupGet(eq(leafUrl));
	}
}
