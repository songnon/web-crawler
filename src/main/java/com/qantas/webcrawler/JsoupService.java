package com.qantas.webcrawler;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class JsoupService {
	private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS"
			+ " X 10.11; rv:49.0) Gecko/20100101 Firefox/49.0";
	
	/**
	 * Use Jsoup to retrieve a HTML page
	 * 
	 * @param url The URL of page to be visited.
	 * @return if successful, a Document is returned.
	 * @throws IOException if there is any error.
	 */
	public Document jsoupGet(String url) throws IOException {
		Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
		Document doc = connection.get();
		if (connection.response().statusCode() != 200
				|| !connection.response().contentType().contains("text/html")) {
			throw new IOException("Invalid status code or no HTML content!");
		}

		return doc;
	}
}
