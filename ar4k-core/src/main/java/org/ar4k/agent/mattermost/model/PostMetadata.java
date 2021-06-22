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
package org.ar4k.agent.mattermost.model;

import java.util.Map;

public class PostMetadata {
	private PostEmbed[] embeds;
	private Emoji[] emojis;
	private FileInfo[] files;
	private Map<String, PostImage> images;
	private Reaction[] reactions;

	@java.lang.SuppressWarnings("all")
	public PostMetadata() {
	}

	@java.lang.SuppressWarnings("all")
	public PostEmbed[] getEmbeds() {
		return this.embeds;
	}

	@java.lang.SuppressWarnings("all")
	public Emoji[] getEmojis() {
		return this.emojis;
	}

	@java.lang.SuppressWarnings("all")
	public FileInfo[] getFiles() {
		return this.files;
	}

	@java.lang.SuppressWarnings("all")
	public Map<String, PostImage> getImages() {
		return this.images;
	}

	@java.lang.SuppressWarnings("all")
	public Reaction[] getReactions() {
		return this.reactions;
	}

	@java.lang.SuppressWarnings("all")
	public void setEmbeds(final PostEmbed[] embeds) {
		this.embeds = embeds;
	}

	@java.lang.SuppressWarnings("all")
	public void setEmojis(final Emoji[] emojis) {
		this.emojis = emojis;
	}

	@java.lang.SuppressWarnings("all")
	public void setFiles(final FileInfo[] files) {
		this.files = files;
	}

	@java.lang.SuppressWarnings("all")
	public void setImages(final Map<String, PostImage> images) {
		this.images = images;
	}

	@java.lang.SuppressWarnings("all")
	public void setReactions(final Reaction[] reactions) {
		this.reactions = reactions;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this)
			return true;
		if (!(o instanceof PostMetadata))
			return false;
		final PostMetadata other = (PostMetadata) o;
		if (!other.canEqual((java.lang.Object) this))
			return false;
		if (!java.util.Arrays.deepEquals(this.getEmbeds(), other.getEmbeds()))
			return false;
		if (!java.util.Arrays.deepEquals(this.getEmojis(), other.getEmojis()))
			return false;
		if (!java.util.Arrays.deepEquals(this.getFiles(), other.getFiles()))
			return false;
		final java.lang.Object this$images = this.getImages();
		final java.lang.Object other$images = other.getImages();
		if (this$images == null ? other$images != null : !this$images.equals(other$images))
			return false;
		if (!java.util.Arrays.deepEquals(this.getReactions(), other.getReactions()))
			return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof PostMetadata;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		result = result * PRIME + java.util.Arrays.deepHashCode(this.getEmbeds());
		result = result * PRIME + java.util.Arrays.deepHashCode(this.getEmojis());
		result = result * PRIME + java.util.Arrays.deepHashCode(this.getFiles());
		final java.lang.Object $images = this.getImages();
		result = result * PRIME + ($images == null ? 43 : $images.hashCode());
		result = result * PRIME + java.util.Arrays.deepHashCode(this.getReactions());
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "PostMetadata(embeds=" + java.util.Arrays.deepToString(this.getEmbeds()) + ", emojis="
				+ java.util.Arrays.deepToString(this.getEmojis()) + ", files="
				+ java.util.Arrays.deepToString(this.getFiles()) + ", images=" + this.getImages() + ", reactions="
				+ java.util.Arrays.deepToString(this.getReactions()) + ")";
	}
}
