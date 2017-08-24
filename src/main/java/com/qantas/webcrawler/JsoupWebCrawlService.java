package com.qantas.webcrawler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JsoupWebCrawlService implements WebCrawlService {

	private static final Logger log = LoggerFactory.getLogger(JsoupWebCrawlService.class);

	private final JsoupService jsoupService;
	
	@Autowired
	public JsoupWebCrawlService(JsoupService jsoupService) {
		this.jsoupService = jsoupService;
	}

	@Override
	public List<Node> crawl(String url, int depth) {
		if (depth > MAX_DEPTH){
			depth = MAX_DEPTH;
		}

		Set<String> urlVisited = new HashSet<String>();

		// Return as an List as the response example in the Developer Challenge
		// shows that the final response is in an array.
		Node node = getLinks(url, urlVisited, 0, depth);
		List<Node> nodes = (node != null) ? Arrays.asList(node) : Collections.emptyList();
		return nodes;
	}

	/**
	 * Recursively retrieve all links on a URL page
	 * 
	 * @param url The URL of current node to be crawl.
	 * @param urlVisited The set of URLs have been visited.
	 * @param currentDepth The depth has been visited.
	 * @param maxDepth The maximum depth is allowed.
	 * @return if successful, the node in hierarchy is returned.
	 */
	public Node getLinks(String url, Set<String> urlVisited, int currentDepth, int maxDepth) {
		// Check if URL has been visited to avoid duplicate and the depth
		if (!url.isEmpty() && !urlVisited.contains(url)
				&& (currentDepth <= maxDepth)) {
			log.info("Depth [current:{}, max:{}]", currentDepth, maxDepth);
			try {
				urlVisited.add(url);
				log.info("New url to visit: " + url);

				// Fetch the page using jsoup and then extract links with "a[href]"
				Document document = jsoupService.jsoupGet(url);

				log.info("Title is: " + document.title());
				Node currentNode = new Node(url, document.title());

				Elements linksOnPage = document.select("a[href]");

				// Increase the depth before diving in to process the child nodes
				currentDepth++;

				for (Element link : linksOnPage) {
					String childUrl = link.absUrl("href");
					Node childNode = getLinks(childUrl, urlVisited,
							currentDepth, maxDepth);

					if (childNode != null) {
						currentNode.addChildNode(childNode);
					}
				}

				return currentNode;
			} catch (IOException e) {
				log.error("Failed to retrieve '" + url + "': " + e.getMessage());
			}

		}

		return null;
	}
}
