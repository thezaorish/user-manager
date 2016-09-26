package com.zaorish.usermanager.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class PaginationInformationTest {

	@Test
	public void shouldVerifyFirstPage() {
		assertThat(new PaginationInformation(1, 3).withTotalCount(10).hasFirstPage(), is(true));
		assertThat(new PaginationInformation(2, 1).withTotalCount(3).hasFirstPage(), is(true));
	}

	@Test
	public void shouldVerifyNextPage() {
		assertThat(new PaginationInformation(1, 3).withTotalCount(10).hasNextPage(), is(true));
		assertThat(new PaginationInformation(2, 1).withTotalCount(3).hasNextPage(), is(true));
		assertThat(new PaginationInformation(2, 2).withTotalCount(3).hasNextPage(), is(false));
	}

	@Test
	public void shouldVerifyPreviousPage() {
		assertThat(new PaginationInformation(1, 3).withTotalCount(10).hasPreviousPage(), is(false));
		assertThat(new PaginationInformation(2, 1).withTotalCount(3).hasPreviousPage(), is(true));
		assertThat(new PaginationInformation(2, 2).withTotalCount(3).hasPreviousPage(), is(true));
	}

	@Test
	public void shouldVerifyLastPage() {
		assertThat(new PaginationInformation(1, 3).withTotalCount(10).hasLastPage(), is(true));
		assertThat(new PaginationInformation(2, 1).withTotalCount(3).hasFirstPage(), is(true));
	}

}