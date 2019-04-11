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

import java.util.Date;
import java.util.List;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.spring.Ar4kUserDetails;
import org.ar4k.gw.anima.TestApplicationRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ComponentScan("org.ar4k.agent")
@Import(TestApplicationRunner.class)
@TestPropertySource(locations = "classpath:application.properties")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthenticationTests {

  @Autowired
  Anima anima;

  @Autowired
  ApplicationContext applicationContext;

  @Autowired
  AuthenticationManager authenticationManager;

  @Before
  public void setUp() throws Exception {
    anima.startingAgent();
  }

  private void printBeans() {
    for (String s : applicationContext.getBeanDefinitionNames()) {
      System.out.println(s);
    }
  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
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
    anima.waitFirstState();
    printBeans();
    checkAuthentication();
    anima.login("admin", "a4c8ff551a");
    System.out.println("CONTEXT: " + SecurityContextHolder.getContext());
    checkAuthentication();
    assertEquals("admin", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
  }

  @Test
  public void baseSessionManagerTest() {
    String sessionId = anima.login("admin", "a4c8ff551a");
    checkAuthentication();
    assertEquals("admin", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    RpcConversation rpc = (RpcConversation) anima.getRpc(sessionId);
    assertEquals("prova echo", rpc.elaborateMessage("prova echo"));
  }

  @Test
  public void destroySessionTest() {
    anima.waitFirstState();
    printBeans();
    checkAuthentication();
    String sessionId = anima.login("admin", "a4c8ff551a");
    System.out.println("CONTEXT: " + SecurityContextHolder.getContext());
    checkAuthentication();
    assertEquals("admin", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    anima.getAnimaHomunculus().onApplicationEvent(new SessionDestroyedEvent("") {
      private static final long serialVersionUID = 7749600721378250259L;

      @Override
      public String getId() {
        return sessionId;
      }

      @Override
      public List<SecurityContext> getSecurityContexts() {
        return null;
      }
    });
    assertThat(anima.getAnimaHomunculus().getSessionInformation(sessionId)).isNull();
  }

  @Test
  public void testMultiplePrincipals() throws Exception {
    Object principal1 = "principal_1";
    Object principal2 = "principal_2";
    String sessionId1 = "1234567890";
    String sessionId2 = "9876543210";
    String sessionId3 = "5432109876";

    anima.getAnimaHomunculus().registerNewSession(sessionId1, principal1);
    anima.getAnimaHomunculus().registerNewSession(sessionId2, principal1);
    anima.getAnimaHomunculus().registerNewSession(sessionId3, principal2);

    assertThat(anima.getAnimaHomunculus().getAllPrincipals()).hasSize(2);
    assertThat(anima.getAnimaHomunculus().getAllPrincipals().contains(principal1)).isTrue();
    assertThat(anima.getAnimaHomunculus().getAllPrincipals().contains(principal2)).isTrue();
  }

  @Test
  public void testSessionInformationLifecycle() throws Exception {
    Object principal = "Some principal object";
    String sessionId = "1234567890";
    // Register new Session
    anima.getAnimaHomunculus().registerNewSession(sessionId, principal);

    // Retrieve existing session by session ID
    Date currentDateTime = anima.getAnimaHomunculus().getSessionInformation(sessionId).getLastRequest();
    assertThat(anima.getAnimaHomunculus().getSessionInformation(sessionId).getPrincipal()).isEqualTo(principal);
    assertThat(anima.getAnimaHomunculus().getSessionInformation(sessionId).getSessionId()).isEqualTo(sessionId);
    assertThat(anima.getAnimaHomunculus().getSessionInformation(sessionId).getLastRequest()).isNotNull();

    // Retrieve existing session by principal
    assertThat(anima.getAnimaHomunculus().getAllSessions(principal, false)).hasSize(1);

    // Sleep to ensure SessionRegistryImpl will update time
    Thread.sleep(1000);

    // Update request date/time
    anima.getAnimaHomunculus().refreshLastRequest(sessionId);

    Date retrieved = anima.getAnimaHomunculus().getSessionInformation(sessionId).getLastRequest();
    assertThat(retrieved.after(currentDateTime)).isTrue();

    // Check it retrieves correctly when looked up via principal
    assertThat(anima.getAnimaHomunculus().getAllSessions(principal, false).get(0).getLastRequest()).isCloseTo(retrieved,
        2000L);

    // Clear session information
    anima.getAnimaHomunculus().removeSessionInformation(sessionId);

    // Check attempts to retrieve cleared session return null
    assertThat(anima.getAnimaHomunculus().getSessionInformation(sessionId)).isNull();
    assertThat(anima.getAnimaHomunculus().getAllSessions(principal, false)).isEmpty();
  }

  @Test
  public void testTwoSessionsOnePrincipalExpiring() throws Exception {
    Object principal = "Some principal object";
    String sessionId1 = "1234567890";
    String sessionId2 = "9876543210";

    anima.getAnimaHomunculus().registerNewSession(sessionId1, principal);
    List<SessionInformation> sessions = anima.getAnimaHomunculus().getAllSessions(principal, false);
    assertThat(sessions).hasSize(1);
    assertThat(contains(sessionId1, principal)).isTrue();

    anima.getAnimaHomunculus().registerNewSession(sessionId2, principal);
    sessions = anima.getAnimaHomunculus().getAllSessions(principal, false);
    assertThat(sessions).hasSize(2);
    assertThat(contains(sessionId2, principal)).isTrue();

    // Expire one session
    SessionInformation session = anima.getAnimaHomunculus().getSessionInformation(sessionId2);
    session.expireNow();

    // Check retrieval still correct
    assertThat(anima.getAnimaHomunculus().getSessionInformation(sessionId2).isExpired()).isTrue();
    assertThat(anima.getAnimaHomunculus().getSessionInformation(sessionId1).isExpired()).isFalse();
  }

  @Test
  public void testTwoSessionsOnePrincipalHandling() throws Exception {
    Object principal = "Some principal object";
    String sessionId1 = "1234567890";
    String sessionId2 = "9876543210";

    anima.getAnimaHomunculus().registerNewSession(sessionId1, principal);
    List<SessionInformation> sessions = anima.getAnimaHomunculus().getAllSessions(principal, false);
    assertThat(sessions).hasSize(1);
    assertThat(contains(sessionId1, principal)).isTrue();

    anima.getAnimaHomunculus().registerNewSession(sessionId2, principal);
    sessions = anima.getAnimaHomunculus().getAllSessions(principal, false);
    assertThat(sessions).hasSize(2);
    assertThat(contains(sessionId2, principal)).isTrue();

    anima.getAnimaHomunculus().removeSessionInformation(sessionId1);
    sessions = anima.getAnimaHomunculus().getAllSessions(principal, false);
    assertThat(sessions).hasSize(1);
    assertThat(contains(sessionId2, principal)).isTrue();

    anima.getAnimaHomunculus().removeSessionInformation(sessionId2);
    assertThat(anima.getAnimaHomunculus().getSessionInformation(sessionId2)).isNull();
    assertThat(anima.getAnimaHomunculus().getAllSessions(principal, false)).isEmpty();
  }

  private boolean contains(String sessionId, Object principal) {
    List<SessionInformation> info = anima.getAnimaHomunculus().getAllSessions(principal, false);

    for (int i = 0; i < info.size(); i++) {
      if (sessionId.equals(info.get(i).getSessionId())) {
        return true;
      }
    }

    return false;
  }

}
