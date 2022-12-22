
package com.codebind;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {
	
	public static void main(String[] args) {
		String html = "https://www.cochranelibrary.com/cdsr/reviews/topics";
		
		try {
			Document doc = Jsoup.connect(html).get();
			
			Elements categories = doc.getElementsByClass("browse-by-list-item");
			
			ArrayList<String> links = new ArrayList<String>() ;
			for(Element e: categories.select("a[href]")) {
				crawl(1, e.absUrl("href"), links);
			}
			ArrayList<String> links2 = new ArrayList<String>() ;

			for(String e: links) {
				crawl(2, e, links2);
			}
//			System.out.println(categories);
//			for(Element e: categories) 
//			{
//				Elements catlink = e.getElementsByTag("a");
//				crawl(true, catlink, new ArrayList<String>());
//			}
		} catch(IOException e)
		{
			System.out.println(e);
		}
	}
	
	private static void crawl(int depth, String html, ArrayList<String> visited) {
		if(depth <= 1) {
			Document doc = request(html, visited); 
			System.out.println("A");
			
			if(doc != null) {
				Elements results = doc.getElementsByClass("row-fluid browse-by-wrapper");
				System.out.println("B");
				for(Element link: results.select("href")) {
					String next_link = link.absUrl("href");
					System.out.println("C");
					System.out.println(visited.contains(next_link));
					if(visited.contains(next_link) == false) {
						System.out.println(next_link);
						crawl(depth++, next_link, visited);
						System.out.println(visited);
						break;
					}
				}
				
			}
		}
		else if(depth <= 2) {
			Document doc = request(html, visited);
			if(doc != null) {
				Elements results = doc.getElementsByClass("search-results-section-body");
				for(Element e: results.select("a[href]")) {
					crawl(depth++, e.absUrl("href"), new ArrayList<String>());
				}
			}
		}
	}
	
	private static Document request(String html, ArrayList<String> v) {
		try {
			Connection con = Jsoup.connect(html);
			Document doc = con.get();
			
			if(con.response().statusCode() == 200) {
//				System.out.println(html);
//				System.out.println(doc.getElementsByClass("facet-pill secondary").text());
				v.add(html);
//				System.out.print("a");
//				System.out.println(v);
				return doc;
			}
			return null;
		}
		catch(IOException e) {
			return null;
		}
	}
}