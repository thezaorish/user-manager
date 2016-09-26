package com.zaorish.usermanager.web.validation;

import java.util.List;

public class PagedResponse<T> {

	private final int total;
	private List<T> results;
	private final int page;
	private final int size;

	public PagedResponse(int total, List<T> results, int page, int size) {
		this.total = total;
		this.results = results;
		this.page = page;
		this.size = size;
	}

	public int getTotal() {
		return total;
	}

	public List<T> getResults() {
		return results;
	}
	public void setResults(List<T> results) {
		this.results = results;
	}

	public int getPage() {
		return page;
	}

	public int getSize() {
		return size;
	}

	public int getTotalPages() {
		int totalPages =  total / size;
		if (total % size != 0) {
			totalPages++;
		}
		return totalPages;
	}

}
