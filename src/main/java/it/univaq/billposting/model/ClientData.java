package it.univaq.billposting.model;

import java.util.List;

public class ClientData {
	
	
	private String username;
	private List<String> cities;
	private String format;
	private List<Integer> budgets;
	private int strat;

	public List<String> getCities() {
		return cities;
	}

	public void setCities(List<String> cities) {
		this.cities = cities;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public List<Integer> getBudgets() {
		return budgets;
	}

	public void setBudgets(List<Integer> budgets) {
		this.budgets = budgets;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getStrat() {
		return strat;
	}

	public void setStrat(int strat) {
		this.strat = strat;
	}
}
