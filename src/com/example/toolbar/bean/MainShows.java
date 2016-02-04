package com.example.toolbar.bean;

import java.util.List;

public class MainShows {
	private List<Top> top;
	private List<Hot> hot;
	private List<Click_Ranking> click_ranking;
	
	public MainShows(List<Top> top, List<Hot> hot, List<Click_Ranking> click_ranking) {
		super();
		this.top = top;
		this.hot = hot;
		this.click_ranking = click_ranking;
	}
	public List<Top> getTop() {
		return top;
	}
	public void setTop(List<Top> top) {
		this.top = top;
	}
	public List<Hot> getHot() {
		return hot;
	}
	public void setHot(List<Hot> hot) {
		this.hot = hot;
	}
	public List<Click_Ranking> getRanking() {
		return click_ranking;
	}
	public void setRanking(List<Click_Ranking> ranking) {
		this.click_ranking = ranking;
	}
	

	@Override
	public String toString() {
		return "MainShows [top=" + top + ", hot=" + hot + ", ranking="
				+ click_ranking + "]";
	}
}
