package com.qantas.webcrawler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
	
	private static final Logger log = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private WebCrawlService webCrawlService;
	
	@RequestMapping("/crawler")
	public CompletableFuture<List<Node>> crawler(@RequestParam(value="url") String url, 
			@RequestParam(value="depth", defaultValue=WebCrawlService.DEFAULT_DEPTH_AS_STRING) int depth){
		
		log.info("Depth is:" + depth);
		CompletableFuture<List<Node>> nodes = webCrawlService.crawl(url, depth);
		log.info("crawl is returned!!!!");
		return nodes;
	}
}
