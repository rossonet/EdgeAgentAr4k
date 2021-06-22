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

package org.ar4k.agent.mattermost.client4.api;

import java.io.IOException;
import java.nio.file.Path;

import org.ar4k.agent.mattermost.client4.ApiResponse;
import org.ar4k.agent.mattermost.client4.Pager;
import org.ar4k.agent.mattermost.client4.model.SearchEmojiRequest;
import org.ar4k.agent.mattermost.model.Emoji;
import org.ar4k.agent.mattermost.model.EmojiList;

public interface EmojiApi {

	ApiResponse<Emoji> createEmoji(Emoji emoji, Path imageFile);

	default ApiResponse<EmojiList> getEmojiList() {
		return getEmojiList(Pager.defaultPager());
	}

	ApiResponse<EmojiList> getEmojiList(Pager pager);

	default ApiResponse<EmojiList> getEmojiListSorted() {
		return getEmojiListSorted(Pager.defaultPager());
	}

	ApiResponse<EmojiList> getEmojiListSorted(Pager pager);

	ApiResponse<Boolean> deleteEmoji(String emojiId);

	ApiResponse<Emoji> getEmoji(String emojiId);

	ApiResponse<Path> getEmojiImage(String emojiId) throws IOException;

	ApiResponse<Emoji> getEmojiByName(String emojiName);

	ApiResponse<EmojiList> searchEmoji(SearchEmojiRequest searchRequest);

	ApiResponse<EmojiList> autocompleteEmoji(String name);

}
