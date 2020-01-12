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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.ar4k.agent.config.AnimaStateMachineConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Anima.AnimaStates;
import org.ar4k.agent.core.AnimaHomunculus;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.spring.Ar4kAuthenticationManager;
import org.ar4k.agent.spring.Ar4kUserDetails;
import org.ar4k.agent.spring.Ar4kuserDetailsService;
import org.jline.builtins.Commands;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
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
@Import({ SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class, Anima.class,
    JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
    StandardAPIAutoConfiguration.class, StandardCommandsAutoConfiguration.class, Commands.class,
    FileValueProvider.class, AnimaStateMachineConfig.class, AnimaHomunculus.class, Ar4kuserDetailsService.class,
    Ar4kAuthenticationManager.class, BCryptPasswordEncoder.class })
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthenticationAndRpcTests {

  @Autowired
  Anima anima;

  @Autowired
  ApplicationContext applicationContext;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  protected SessionRegistry sessionRegistry;

  @Before
  public void setUp() throws Exception {
  }

  private void printBeans() {
    for (String s : applicationContext.getBeanDefinitionNames()) {
      System.out.println(s);
    }
  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    @Override
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

  private void checkAuthentication() {
    String username = "NaN";
    if (SecurityContextHolder.getContext() != null) {
      if (SecurityContextHolder.getContext().getAuthentication() != null) {
        Object principalAnon = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principalAnon instanceof Ar4kUserDetails) {
          username = ((Ar4kUserDetails) principalAnon).getUsername();
        } else {
          username = principalAnon.toString();
        }
        System.out.println("AUTHENTICATE AS " + username);
      } else {
        System.out.println("No SecurityContextHolder.getContext().getAuthentication()");
      }
    } else {
      System.out.println("No SecurityContextHolder.getContext()");
    }
  }

  @Test
  public void authenticationTest() {
    while (anima.getState() != null && anima.getState().equals(AnimaStates.INIT)) {
      try {
        Thread.sleep(200L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    printBeans();
    checkAuthentication();
    anima.loginAgent("admin", "a4c8ff551a", null);
    System.out.println("CONTEXT: " + SecurityContextHolder.getContext());
    checkAuthentication();
    assertEquals("admin", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
  }

  @Test
  public void baseSessionManagerTest() {
    String sessionId = anima.loginAgent("admin", "a4c8ff551a", null);
    checkAuthentication();
    assertEquals("admin", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    RpcConversation rpc = (RpcConversation) anima.getRpc(sessionId);
    System.out.println(rpc.listCommands().toString());
    assertTrue(rpc.elaborateMessage("help").contains("Display or save the history of previously run commands"));
  }

  @Test
  public void destroySessionTest() {
    while (anima.getState() != null && anima.getState().equals(AnimaStates.INIT)) {
      try {
        Thread.sleep(200L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    printBeans();
    checkAuthentication();
    String sessionId = anima.loginAgent("admin", "a4c8ff551a", null);
    System.out.println("CONTEXT: " + SecurityContextHolder.getContext());
    checkAuthentication();
    assertEquals("admin", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    anima.terminateSession(sessionId);
    assertThat(sessionRegistry.getSessionInformation(sessionId)).isNull();
  }

  @Test
  public void testMultiplePrincipals() throws Exception {
    Object principal1 = "principal_1";
    Object principal2 = "principal_2";
    String sessionId1 = "1234567890";
    String sessionId2 = "9876543210";
    String sessionId3 = "5432109876";

    sessionRegistry.registerNewSession(sessionId1, principal1);
    sessionRegistry.registerNewSession(sessionId2, principal1);
    sessionRegistry.registerNewSession(sessionId3, principal2);

    assertThat(sessionRegistry.getAllPrincipals()).hasSize(3);
    assertThat(sessionRegistry.getAllPrincipals().contains(principal1)).isTrue();
    assertThat(sessionRegistry.getAllPrincipals().contains(principal2)).isTrue();
  }

  @Test
  public void testSessionInformationLifecycle() throws Exception {
    Object principal = "Some principal object";
    String sessionId = "1234567890";
    // Register new Session
    sessionRegistry.registerNewSession(sessionId, principal);

    // Retrieve existing session by session ID
    Date currentDateTime = sessionRegistry.getSessionInformation(sessionId).getLastRequest();
    assertThat(sessionRegistry.getSessionInformation(sessionId).getPrincipal()).isEqualTo(principal);
    assertThat(sessionRegistry.getSessionInformation(sessionId).getSessionId()).isEqualTo(sessionId);
    assertThat(sessionRegistry.getSessionInformation(sessionId).getLastRequest()).isNotNull();

    // Retrieve existing session by principal
    assertThat(sessionRegistry.getAllSessions(principal, false)).hasSize(1);

    // Sleep to ensure SessionRegistryImpl will update time
    Thread.sleep(1000);

    // Update request date/time
    sessionRegistry.refreshLastRequest(sessionId);

    Date retrieved = sessionRegistry.getSessionInformation(sessionId).getLastRequest();
    assertThat(retrieved.after(currentDateTime)).isTrue();

    // Check it retrieves correctly when looked up via principal
    assertThat(sessionRegistry.getAllSessions(principal, false).get(0).getLastRequest()).isCloseTo(retrieved, 2000L);

    // Clear session information
    sessionRegistry.removeSessionInformation(sessionId);

    // Check attempts to retrieve cleared session return null
    assertThat(sessionRegistry.getSessionInformation(sessionId)).isNull();
    assertThat(sessionRegistry.getAllSessions(principal, false)).isEmpty();
  }

  @Test
  public void testTwoSessionsOnePrincipalExpiring() throws Exception {
    Object principal = "Some principal object";
    String sessionId1 = "1234567890";
    String sessionId2 = "9876543210";

    sessionRegistry.registerNewSession(sessionId1, principal);
    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
    assertThat(sessions).hasSize(1);
    assertThat(contains(sessionId1, principal)).isTrue();

    sessionRegistry.registerNewSession(sessionId2, principal);
    sessions = sessionRegistry.getAllSessions(principal, false);
    assertThat(sessions).hasSize(2);
    assertThat(contains(sessionId2, principal)).isTrue();

    // Expire one session
    SessionInformation session = sessionRegistry.getSessionInformation(sessionId2);
    session.expireNow();

    // Check retrieval still correct
    assertThat(sessionRegistry.getSessionInformation(sessionId2).isExpired()).isTrue();
    assertThat(sessionRegistry.getSessionInformation(sessionId1).isExpired()).isFalse();
  }

  @Test
  public void testTwoSessionsOnePrincipalHandling() throws Exception {
    Object principal = "Some principal object";
    String sessionId1 = "1234567890";
    String sessionId2 = "9876543210";

    sessionRegistry.registerNewSession(sessionId1, principal);
    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
    assertThat(sessions).hasSize(1);
    assertThat(contains(sessionId1, principal)).isTrue();

    sessionRegistry.registerNewSession(sessionId2, principal);
    sessions = sessionRegistry.getAllSessions(principal, false);
    assertThat(sessions).hasSize(2);
    assertThat(contains(sessionId2, principal)).isTrue();

    sessionRegistry.removeSessionInformation(sessionId1);
    sessions = sessionRegistry.getAllSessions(principal, false);
    assertThat(sessions).hasSize(1);
    assertThat(contains(sessionId2, principal)).isTrue();

    sessionRegistry.removeSessionInformation(sessionId2);
    assertThat(sessionRegistry.getSessionInformation(sessionId2)).isNull();
    assertThat(sessionRegistry.getAllSessions(principal, false)).isEmpty();
  }

  private boolean contains(String sessionId, Object principal) {
    List<SessionInformation> info = sessionRegistry.getAllSessions(principal, false);

    for (int i = 0; i < info.size(); i++) {
      if (sessionId.equals(info.get(i).getSessionId())) {
        return true;
      }
    }

    return false;
  }

}
