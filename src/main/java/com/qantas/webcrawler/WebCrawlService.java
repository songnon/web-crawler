package com.qantas.webcrawler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface WebCrawlService {
	public static final String DEFAULT_DEPTH_AS_STRING = "1";
	public static final int DEFAULT_DEPTH = 1;
	public static final int MAX_DEPTH = 5;
	
	/**
	 * Deep-crawling all links on a URL page.
	 * 
	 * @param url The URL of HTML page
	 * @param depth The maximum depth to crawl.
	 * @return if successful, the node in hierarchy is returned.
	 */
	public CompletableFuture<List<Node>> crawl(String url, int depth);
}
