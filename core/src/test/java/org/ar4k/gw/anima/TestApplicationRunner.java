/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */
package org.ar4k.gw.anima;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.util.StringUtils;

@SpringBootConfiguration
public class TestApplicationRunner implements ApplicationRunner {

  private static Logger log = LoggerFactory.getLogger(TestApplicationRunner.class);

  String[] args = new String[0];
  String[] disabledCommands = { "--spring.shell.command.quit.enabled=false" };
  String[] fullArgs = StringUtils.concatenateStringArrays(args, disabledCommands);

  public TestApplicationRunner() {
    log.info("Test Application Runner started!");
  }
  
  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("About to do nothing!");
    // Do nothing...
  }
}
