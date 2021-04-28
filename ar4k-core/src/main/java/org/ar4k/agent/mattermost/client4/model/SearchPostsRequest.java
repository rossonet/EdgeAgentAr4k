// Generated by delombok at Sun Apr 18 22:20:18 CEST 2021
/*
 * Copyright (c) 2017-present, Takayuki Maruyama
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
package org.ar4k.agent.mattermost.client4.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Search posts request payload.
 * 
 * @see org.ar4k.agent.mattermost.client4.api.PostApi#searchPosts(String, String, boolean)
 * @author Takayuki Maruyama
 */
public class SearchPostsRequest {
  @JsonProperty("terms")
  private String terms;
  @JsonProperty("is_or_search")
  private boolean isOrSearch;

  @java.lang.SuppressWarnings("all")
  SearchPostsRequest(final String terms, final boolean isOrSearch) {
    this.terms = terms;
    this.isOrSearch = isOrSearch;
  }


  @java.lang.SuppressWarnings("all")
  public static class SearchPostsRequestBuilder {
    @java.lang.SuppressWarnings("all")
    private String terms;
    @java.lang.SuppressWarnings("all")
    private boolean isOrSearch;

    @java.lang.SuppressWarnings("all")
    SearchPostsRequestBuilder() {
    }

    /**
     * @return {@code this}.
     */
    @JsonProperty("terms")
    @java.lang.SuppressWarnings("all")
    public SearchPostsRequest.SearchPostsRequestBuilder terms(final String terms) {
      this.terms = terms;
      return this;
    }

    /**
     * @return {@code this}.
     */
    @JsonProperty("is_or_search")
    @java.lang.SuppressWarnings("all")
    public SearchPostsRequest.SearchPostsRequestBuilder isOrSearch(final boolean isOrSearch) {
      this.isOrSearch = isOrSearch;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public SearchPostsRequest build() {
      return new SearchPostsRequest(this.terms, this.isOrSearch);
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
      return "SearchPostsRequest.SearchPostsRequestBuilder(terms=" + this.terms + ", isOrSearch=" + this.isOrSearch + ")";
    }
  }

  @java.lang.SuppressWarnings("all")
  public static SearchPostsRequest.SearchPostsRequestBuilder builder() {
    return new SearchPostsRequest.SearchPostsRequestBuilder();
  }
}
