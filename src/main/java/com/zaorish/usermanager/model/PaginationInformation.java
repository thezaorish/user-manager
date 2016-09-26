package com.zaorish.usermanager.model;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class PaginationInformation {

	public static final String PAGE = "page";
	public static final String SIZE = "size";

	public static final int DEFAULT_PAGE = 1;
	public static final int DEFAULT_PAGE_SIZE = 20;
	private static final int MAX_SIZE = 100;

	private int page = DEFAULT_PAGE;
	protected int size = DEFAULT_PAGE_SIZE;

	private int totalCount;

	public PaginationInformation(int page, int size) {
		setPage(page);
		setSize(size);
	}

	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = max(DEFAULT_PAGE, page);
	}

	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		if (size == 0) {
			this.size = MAX_VALUE;
		} else {
			this.size = min(max(size, 1), MAX_SIZE);
		}
	}

	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public PaginationInformation withTotalCount(int totalCount) {
		setTotalCount(totalCount);
		return this;
	}

	public int getNumberOfPages() {
		int numberOfPages = getTotalCount() / getSize();
		return (getTotalCount() % getSize() != 0) ? numberOfPages + 1 : numberOfPages;
	}

	public int getStartIndex() {
		return (getPage() - 1) * getSize();
	}
	public int getEndIndex() {
		int endIndex = getPage() * getSize();
		if (endIndex > getTotalCount()) {
			endIndex = getTotalCount();
		}
		return endIndex;
	}

	public boolean hasNextPage() {
		return getPage() < getNumberOfPages();
	}

	public boolean hasPreviousPage() {
		return getPage() > 1;
	}

	public boolean hasFirstPage() {
		return getPage() >= 1;
	}

	public boolean hasLastPage() {
		return getNumberOfPages() >= 1;
	}

	@Override
	public String toString() {
		return "PaginationInformation{" +
				"size=" + getSize() +
				", page=" + getPage() +
				'}';
	}

}
