package javafxwebbrowser;

import javafx.scene.control.Hyperlink;

public class HistoryItem {

	private String title;
	private Hyperlink url;
	private String date;

	public HistoryItem(String title, Hyperlink url, String date) {
		this.title = title;
		this.url = url;
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Hyperlink getUrl() {
		return url;
	}

	public void setUrl(Hyperlink url) {
		this.url = url;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}


