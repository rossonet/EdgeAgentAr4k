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
package org.ar4k.qa.tests;

import java.io.IOException;
import java.util.Map;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.HomunculusSession;
import org.ar4k.agent.core.HomunculusStateMachineConfig;
import org.ar4k.agent.mattermost.model.Channel;
import org.ar4k.agent.mattermost.model.Post;
import org.ar4k.agent.mattermost.model.Team;
import org.ar4k.agent.mattermost.model.User;
import org.ar4k.agent.spring.EdgeAuthenticationManager;
import org.ar4k.agent.spring.EdgeUserDetailsService;
import org.jline.builtins.Commands;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.shell.SpringShellAutoConfiguration;
import org.springframework.shell.jcommander.JCommanderParameterResolverAutoConfiguration;
import org.springframework.shell.jline.JLineShellAutoConfiguration;
import org.springframework.shell.legacy.LegacyAdapterAutoConfiguration;
import org.springframework.shell.standard.FileValueProvider;
import org.springframework.shell.standard.StandardAPIAutoConfiguration;
import org.springframework.shell.standard.commands.StandardCommandsAutoConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({ SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class, Homunculus.class,
		JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
		StandardAPIAutoConfiguration.class, StandardCommandsAutoConfiguration.class, Commands.class,
		FileValueProvider.class, HomunculusStateMachineConfig.class, HomunculusSession.class,
		EdgeUserDetailsService.class, EdgeAuthenticationManager.class, BCryptPasswordEncoder.class })
@TestPropertySource(locations = "classpath:application-mattermost.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class MattermostConnectionAndInteraction {

	@Autowired
	Homunculus homunculus;

	@Before
	public void setUp() throws Exception {
		Thread.sleep(3000L);
		System.out.println(homunculus.getState());
	}

	@After
	public void tearDownAfterClass() throws Exception {

	}

	@Rule
	public TestWatcher watcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
		}
	};
	
	@Test
	@Ignore
	public void checkTestConnection() throws InterruptedException, IOException {
		Thread.sleep(5000);
		System.out.println("***************************** " + homunculus.getMattermostClient().getMe());
		int counter = 0;
		boolean activeReport = false;
		while (counter++ < 100) {
			if (activeReport) {
				System.out.println("\n--- REPORT STATUS ---");
				System.out.println("			teams");
				final Map<String, Team> teams = homunculus.getMattermostClient().getTeams();
				for (final Team t : teams.values()) {
					System.out.println(t.getDisplayName());
				}
				System.out.println("			channels");
				final Map<String, User> users = homunculus.getMattermostClient().getUsers();
				final Map<String, Channel> channels = homunculus.getMattermostClient().getChannels();
				for (final Channel c : channels.values()) {
					System.out.println(
							c.getCreateAt() + " -> " + (c.getDisplayName() != null ? c.getDisplayName() : c.getName())
									+ " [" + c.getType().name() + "]");
				}
				System.out.println("			users");
				for (final User u : users.values()) {
					System.out.println(u.getEmail() + " -> " + u.getFirstName() + " " + u.getLastName() + " ["
							+ u.getNickname() + "]");
				}
				System.out.println("			posts");
				final Map<String, Post> posts = homunculus.getMattermostClient().getPosts();
				for (final Post m : posts.values()) {
					System.out.println(users.get(m.getUserId()).getUsername() + " -> "
							+ channels.get(m.getChannelId()).getDisplayName() + " : " + m.getMessage());
				}
			}
			Thread.sleep(20000);
		}

	}

}
