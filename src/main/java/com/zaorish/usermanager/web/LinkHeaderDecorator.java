package com.zaorish.usermanager.web;

import com.zaorish.usermanager.model.PaginationInformation;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import static com.zaorish.usermanager.model.PaginationInformation.PAGE;
import static com.zaorish.usermanager.model.PaginationInformation.SIZE;

@Component
public class LinkHeaderDecorator {

	private static final String REL_NEXT = "next";
	private static final String REL_PREV = "prev";
	private static final String REL_FIRST = "first";
	private static final String REL_LAST = "last";

	public String decorateLinkHeader(UriComponentsBuilder uriBuilder, PaginationInformation pagination, String path) {
		decorateUrlPath(uriBuilder, path);

		StringBuilder linkHeader = new StringBuilder();
		if (pagination.hasNextPage()) {
			String uriForNextPage = constructNextPageUri(uriBuilder, pagination);
			linkHeader.append(createLinkHeader(uriForNextPage, REL_NEXT));
		}
		if (pagination.hasLastPage()) {
			String uriForLastPage = constructLastPageUri(uriBuilder, pagination);
			appendCommaIfNecessary(linkHeader);
			linkHeader.append(createLinkHeader(uriForLastPage, REL_LAST));
		}
		if (pagination.hasFirstPage()) {
			String uriForFirstPage = constructFirstPageUri(uriBuilder, pagination);
			appendCommaIfNecessary(linkHeader);
			linkHeader.append(createLinkHeader(uriForFirstPage, REL_FIRST));
		}
		if (pagination.hasPreviousPage()) {
			String uriForPrevPage = constructPrevPageUri(uriBuilder, pagination);
			appendCommaIfNecessary(linkHeader);
			linkHeader.append(createLinkHeader(uriForPrevPage, REL_PREV));
		}

		return linkHeader.toString();
	}

	private void decorateUrlPath(UriComponentsBuilder uriBuilder, String urlSuffix) {
		uriBuilder.path("/" + urlSuffix);
	}

	private String constructNextPageUri(UriComponentsBuilder uriBuilder, PaginationInformation pagination) {
		return uriBuilder.replaceQueryParam(PAGE, pagination.getPage() + 1).replaceQueryParam(SIZE, pagination.getSize()).build().encode().toUriString();
	}

	private String constructLastPageUri(UriComponentsBuilder uriBuilder, PaginationInformation pagination) {
		return uriBuilder.replaceQueryParam(PAGE, pagination.getNumberOfPages()).replaceQueryParam(SIZE, pagination.getSize()).build().encode().toUriString();
	}

	private String constructFirstPageUri(UriComponentsBuilder uriBuilder, PaginationInformation pagination) {
		return uriBuilder.replaceQueryParam(PAGE, PaginationInformation.DEFAULT_PAGE).replaceQueryParam(SIZE, pagination.getSize()).build().encode().toUriString();
	}

	private String constructPrevPageUri(UriComponentsBuilder uriBuilder, PaginationInformation pagination) {
		return uriBuilder.replaceQueryParam(PAGE, pagination.getPage() - 1).replaceQueryParam(SIZE, pagination.getSize()).build().encode().toUriString();
	}

	private void appendCommaIfNecessary(StringBuilder linkHeader) {
		if (linkHeader.length() > 0) {
			linkHeader.append(", ");
		}
	}

	private String createLinkHeader(String uri, String rel) {
		return "<" + uri + ">; rel=\"" + rel + "\"";
	}

}
