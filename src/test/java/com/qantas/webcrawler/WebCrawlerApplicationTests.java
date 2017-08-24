package com.qantas.webcrawler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.CoreMatchers.containsString;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WebCrawlerApplicationTests {
	
	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private JsoupService jsoupService;
	
	private final String leafHtmlString = "<html><head><title>Google Leaf</title></head><body><h1>My First Heading</h1><p>My first paragraph.</p></body></html>";
	private final String leafUrl = "http://leaf.com";
	private final String leafUrlTitle = "Google Leaf";

	@Before
	public void setup() throws Exception {
		Document doc = Jsoup.parse(leafHtmlString);
		when(jsoupService.jsoupGet(leafUrl)).thenReturn(doc);
	}

	@Test
	public void integrationTest() {
		String nodeString = this.restTemplate.getForObject("/crawler?url={leafUrl}", String.class, leafUrl);
		assertThat(nodeString, is(notNullValue()));
		assertThat(nodeString, containsString(leafUrl));
		assertThat(nodeString, containsString(leafUrlTitle));
	}
}
