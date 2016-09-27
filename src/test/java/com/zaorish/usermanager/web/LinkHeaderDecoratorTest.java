package com.zaorish.usermanager.web;

import com.zaorish.usermanager.model.PaginationInformation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.util.UriComponentsBuilder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class LinkHeaderDecoratorTest {

	private LinkHeaderDecorator linkHeaderDecorator;

	private UriComponentsBuilder uriBuilder;

	@Before
	public void setUp() {
		linkHeaderDecorator = new LinkHeaderDecorator();
		uriBuilder = UriComponentsBuilder.newInstance();
		uriBuilder.replacePath("");
	}

	@Test
	public void shouldDecorateLinkHeaderWhenMiddlePageOfResourcesIsRetrieved() {
		// given we have more resources than needed to fill in the first page
		PaginationInformation pagination = new PaginationInformation(2, 3).withTotalCount(10);

		// when
		String result = linkHeaderDecorator.decorateLinkHeader(uriBuilder, pagination, "resources");

		// then
		assertThat(result, is("</resources?page=3&size=3>; rel=\"next\", </resources?page=4&size=3>; rel=\"last\", </resources?page=1&size=3>; rel=\"first\", </resources?page=1&size=3>; rel=\"prev\""));
	}
	@Test
	public void shouldDecorateLinkHeaderWhenFirstPageOfResourcesIsRetrieved() {
		// given we have more resources than needed to fill in the first page
		PaginationInformation pagination = new PaginationInformation(1, 4).withTotalCount(10);

		// when
		String result = linkHeaderDecorator.decorateLinkHeader(uriBuilder, pagination, "resources");

		// then
		assertThat(result, is("</resources?page=2&size=4>; rel=\"next\", </resources?page=3&size=4>; rel=\"last\", </resources?page=1&size=4>; rel=\"first\""));
	}
	@Test
	public void shouldDecorateLinkHeaderWhenLastPageOfResourcesIsRetrieved() {
		// given we have more resources than needed to fill in the first page
		PaginationInformation pagination = new PaginationInformation(2, 5).withTotalCount(8);

		// when
		String result = linkHeaderDecorator.decorateLinkHeader(uriBuilder, pagination, "resources");

		// then
		assertThat(result, is("</resources?page=2&size=5>; rel=\"last\", </resources?page=1&size=5>; rel=\"first\", </resources?page=1&size=5>; rel=\"prev\""));
	}

	@Test
	public void shouldDecorateLinkHeaderWhenOnePageOfResourcesExists() {
		// given we have resources for a single page
		PaginationInformation pagination = new PaginationInformation(1, 4).withTotalCount(4);

		// when
		String result = linkHeaderDecorator.decorateLinkHeader(uriBuilder, pagination, "resources");

		// then
		assertThat(result, is("</resources?page=1&size=4>; rel=\"last\", </resources?page=1&size=4>; rel=\"first\""));
	}

}