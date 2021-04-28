// Generated by delombok at Sun Apr 18 22:20:18 CEST 2021
/*
 * Copyright (c) 2019 Takayuki Maruyama
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

/**
 * Search emoji request.
 */
public final class SearchEmojiRequest {
  private final String term;
  private final boolean prefixOnly;

  @java.lang.SuppressWarnings("all")
  SearchEmojiRequest(final String term, final boolean prefixOnly) {
    this.term = term;
    this.prefixOnly = prefixOnly;
  }


  @java.lang.SuppressWarnings("all")
  public static class SearchEmojiRequestBuilder {
    @java.lang.SuppressWarnings("all")
    private String term;
    @java.lang.SuppressWarnings("all")
    private boolean prefixOnly;

    @java.lang.SuppressWarnings("all")
    SearchEmojiRequestBuilder() {
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    public SearchEmojiRequest.SearchEmojiRequestBuilder term(final String term) {
      this.term = term;
      return this;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    public SearchEmojiRequest.SearchEmojiRequestBuilder prefixOnly(final boolean prefixOnly) {
      this.prefixOnly = prefixOnly;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public SearchEmojiRequest build() {
      return new SearchEmojiRequest(this.term, this.prefixOnly);
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
      return "SearchEmojiRequest.SearchEmojiRequestBuilder(term=" + this.term + ", prefixOnly=" + this.prefixOnly + ")";
    }
  }

  @java.lang.SuppressWarnings("all")
  public static SearchEmojiRequest.SearchEmojiRequestBuilder builder() {
    return new SearchEmojiRequest.SearchEmojiRequestBuilder();
  }

  @java.lang.SuppressWarnings("all")
  public String getTerm() {
    return this.term;
  }

  @java.lang.SuppressWarnings("all")
  public boolean isPrefixOnly() {
    return this.prefixOnly;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public boolean equals(final java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof SearchEmojiRequest)) return false;
    final SearchEmojiRequest other = (SearchEmojiRequest) o;
    if (this.isPrefixOnly() != other.isPrefixOnly()) return false;
    final java.lang.Object this$term = this.getTerm();
    final java.lang.Object other$term = other.getTerm();
    if (this$term == null ? other$term != null : !this$term.equals(other$term)) return false;
    return true;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + (this.isPrefixOnly() ? 79 : 97);
    final java.lang.Object $term = this.getTerm();
    result = result * PRIME + ($term == null ? 43 : $term.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "SearchEmojiRequest(term=" + this.getTerm() + ", prefixOnly=" + this.isPrefixOnly() + ")";
  }
}
