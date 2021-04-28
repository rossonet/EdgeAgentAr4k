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

/**
 * Branding API.
 * 
 * @author Takayuki Maruyama
 */
public interface BrandApi {

  /**
   * retrieves the previously uploaded brand image.
   */
  ApiResponse<Path> getBrandImage() throws IOException;

  /**
   * sets the brand image for the system.
   */
  ApiResponse<Boolean> uploadBrandImage(Path dataFIle);

  /**
   * delete the brand image for the system.
   * 
   * @since Mattermost Server 5.6
   */
  ApiResponse<Boolean> deleteBrandImage();

}
