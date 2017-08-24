package com.qantas.webcrawler;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private final String url;
	private String title;
	private final List<Node> nodes;
	
	public Node(String url) {
		this.url = url;
		this.nodes = new ArrayList<Node>();
	}

	public Node(String url, String title) {
		this.url = url;
		this.title = title;
		this.nodes = new ArrayList<Node>();
	}

	public Node(String url, String title, List<Node> nodes) {
		super();
		this.url = url;
		this.title = title;
		this.nodes = nodes;
	}

	public String getUrl() {
		return url;
	}

	public String getTitle() {
		return title;
	}

	public List<Node> getNodes() {
		return nodes;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void addChildNode(Node node){
		this.nodes.add(node);
	}
	
}
