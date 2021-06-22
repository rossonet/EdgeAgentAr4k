// Generated by delombok at Sun Apr 18 22:20:18 CEST 2021
/*
 * Copyright (c) 2017 Takayuki Maruyama
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.ar4k.agent.mattermost.client4;

public final class Pager {
	private final int page;
	private final int perPage;
	private static final Pager DEFAULT = of(0, 60);

	public static Pager defaultPager() {
		return DEFAULT;
	}

	public Pager nextPage() {
		return Pager.of(page + 1, perPage);
	}

	public String toQuery() {
		return toQuery(true);
	}

	public String toQuery(boolean isHead) {
		return (isHead ? "?" : "&") + String.format("page=%d&per_page=%d", page, perPage);
	}

	@java.lang.SuppressWarnings("all")
	private Pager(final int page, final int perPage) {
		this.page = page;
		this.perPage = perPage;
	}

	@java.lang.SuppressWarnings("all")
	public static Pager of(final int page, final int perPage) {
		return new Pager(page, perPage);
	}

	@java.lang.SuppressWarnings("all")
	public int getPage() {
		return this.page;
	}

	@java.lang.SuppressWarnings("all")
	public int getPerPage() {
		return this.perPage;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Pager))
			return false;
		final Pager other = (Pager) o;
		if (this.getPage() != other.getPage())
			return false;
		if (this.getPerPage() != other.getPerPage())
			return false;
		return true;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		result = result * PRIME + this.getPage();
		result = result * PRIME + this.getPerPage();
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "Pager(page=" + this.getPage() + ", perPage=" + this.getPerPage() + ")";
	}
}
