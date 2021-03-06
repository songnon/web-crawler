package com.qantas.webcrawler;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@WebMvcTest(MainController.class)
public class MainControllerTests {
	
	@MockBean
    private WebCrawlService webCrawlService;
	
	@Autowired
    private MockMvc mockMvc;
	
	@Test
	public void shouldReturnEmptyList() throws Exception {
		String url = "http://www.google.com";
		when(webCrawlService.crawl(url, WebCrawlService.DEFAULT_DEPTH))
			.thenReturn(CompletableFuture.completedFuture(Collections.emptyList()));
		
		MvcResult result = mockMvc.perform(get("/crawler?url=" + url)).andReturn();
		
		mockMvc.perform(asyncDispatch(result))
	           .andExpect(status().isOk())
	           .andExpect(jsonPath("$", hasSize(0)));
	}
	
	@Test
	public void shouldReturnListWithOneLevel() throws Exception {
		String url = "http://www.test.com/test";
		String title = "test";
		Node node = new Node(url, title);
		List<Node> nodeList = new ArrayList<Node>();
		nodeList.add(node);
		
		when(webCrawlService.crawl(url, WebCrawlService.DEFAULT_DEPTH))
			.thenReturn(CompletableFuture.completedFuture(nodeList));
		
		MvcResult result = mockMvc.perform(get("/crawler?url=" + url)).andReturn();
		
		mockMvc.perform(asyncDispatch(result))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$", hasSize(1)))
			   .andExpect(jsonPath("$[0].url", is(url)))
			   .andExpect(jsonPath("$[0].title", is(title)))
			   .andExpect(jsonPath("$[0].nodes", is(empty())));
	}
	
}
