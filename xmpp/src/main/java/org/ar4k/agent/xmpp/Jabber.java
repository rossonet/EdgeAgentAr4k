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
package org.ar4k.agent.xmpp;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
//import org.ar4k.agent.watson.Watson;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 * 
 *         Helper connessione XMPP Jabber
 *
 */
//TODO: Appena eliminato watson. capire come integrare
public class Jabber implements Runnable, ChatManagerListener, ChatMessageListener {

  private static final Logger logger = LoggerFactory.getLogger(Jabber.class);

  private boolean running = true;

  private Thread processo = null;

  private String server = "ip-jabber.olark.com";
  private String username = "ar4k";
  private String password = "65B76c!29b";
  private int port = 5222;
  private XMPPTCPConnection connection;

  // private Watson watsonMaster;

  private List<Object> attive = new ArrayList<Object>();

  /*
   * public Jabber(Watson watsonMaster) throws Exception { this.watsonMaster =
   * watsonMaster; connection = login(); }
   */
  @SuppressWarnings("unused")
  private XMPPTCPConnection login() throws Exception {
    XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
        .setUsernameAndPassword(username, password).setHost(server).setPort(port).setSecurityMode(SecurityMode.required)
        .setDebuggerEnabled(false).setServiceName("olark.com").setSendPresence(true).build();
    XMPPTCPConnection.setUseStreamManagementDefault(true);
    XMPPTCPConnection connectionLogin;
    connectionLogin = new XMPPTCPConnection(config);
    connectionLogin.setPacketReplyTimeout(10000);
    connectionLogin.setUseStreamManagement(true);
    ReconnectionManager.getInstanceFor(connectionLogin).enableAutomaticReconnection();
    connectionLogin.connect();
    while (!connectionLogin.isConnected()) {
      Thread.sleep(1000L);
      System.out.print(".");
    }
    connectionLogin.login();
    while (!connectionLogin.isAuthenticated()) {
      Thread.sleep(1000L);
      System.out.print(".");
    }
    ChatManager.getInstanceFor(connectionLogin).addChatListener(this);
    System.out.println(AnsiOutput.toString(AnsiColor.RED, "XMPP Connected", AnsiColor.DEFAULT));
    return connectionLogin;
  }

  public void sendMessage(String webuser, String message)
      throws XmppStringprepException, NotConnectedException, InterruptedException {
    ChatManager chatManager = ChatManager.getInstanceFor(connection);
    Chat chat = chatManager.createChat(webuser);
    chat.sendMessage(message);
  }

  private Collection<RosterEntry> displayUsers() {
    Roster roster = Roster.getInstanceFor(connection);
    roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
    Collection<RosterEntry> entries = roster.getEntries();
    return entries;
  }

  private Collection<Object> registeredUsers() {
    Collection<Object> entries = attive;
    return entries;
  }

  public List<String> listUsers() {
    List<String> risposta = new ArrayList<String>();
    Collection<RosterEntry> utenti = displayUsers();
    // Roster roster = Roster.getInstanceFor(connection);
    for (RosterEntry s : utenti) {
      String user = s.getUser();
      risposta.add(user);
    }
    return risposta;
  }

  private synchronized void disconnect() {
    running = false;
    connection.disconnect();
    connection = null;
  }

  @Override
  protected void finalize() {
    disconnect();
  }

  public synchronized void start() {
    processo = new Thread(this);
    processo.setName("jabber");
    processo.start();
  }

  @Override
  public void run() {
    while (running) {
      // System.out.print(" ");
      if (connection != null) {
        // OfflineMessageManager mOfflineMessageManager = new
        // OfflineMessageManager(connection);
        // Get the message size
        try {
          /*
           * int size = mOfflineMessageManager.getMessageCount(); // System.out.print(size
           * + " "); if (size > 0) { List<org.jivesoftware.smack.packet.Message> messages
           * = mOfflineMessageManager.getMessages(); for
           * (org.jivesoftware.smack.packet.Message messaggio : messages) {
           * newIncomingMessage((EntityBareJid) messaggio.getFrom(), messaggio,
           * ChatManager .getInstanceFor(connection).chatWith((EntityBareJid)
           * messaggio.getFrom())); } }
           * 
           * for (RosterEntry l : displayUsers()) { // System.out.println("try user " +
           * l.getJid().getLocalpartOrNull().toString()); boolean trovato = false; for
           * (ChatEcss t : attive) { if (t.getWebuser() == l.getName()) { trovato = true;
           * } } if (trovato == false) { System.out.println("not found " + l.getName());
           * ChatManager chatmanager = ChatManager.getInstanceFor(connection); Chat
           * newChat = chatmanager.createChat(l.getName()); ChatEcss f = new
           * ChatEcss(watsonMaster, newChat); newChat.addMessageListener(this);
           * attive.add(f); System.out.println("created chat with " + f.getWebuser()); } }
           */
          Thread.sleep(1000L);
        } catch (InterruptedException e) {
          logger.warn(e.getMessage());
        }
      }
    }
  }

  public boolean isConnected() {
    return connection.isConnected();
  }

  @SuppressWarnings("unused")
  public synchronized void startDemo()
      throws IOException, NotConnectedException, InterruptedException, XmppStringprepException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    // watsonMaster.iniziaConversazione();
    String connected = null;
    Console console = System.console();
    System.out.println(AnsiOutput.toString("\nType ", AnsiColor.RED, "!end", AnsiColor.DEFAULT,
        " to stop the demo and save all the data.\n", AnsiColor.RED, "!list", AnsiColor.DEFAULT,
        " to show all the active chats\n", AnsiColor.RED, "!connect", AnsiColor.DEFAULT,
        " to enter in a specific chat\n", AnsiColor.RED, "!disconnect", AnsiColor.DEFAULT, " for disconnect user\n",
        AnsiColor.RED, "!push", AnsiColor.DEFAULT, " to redirect the user to another page (when connected)\n",
        AnsiColor.DEFAULT));
    boolean attivo = true;
    while (attivo) {
      String prompt = AnsiOutput.toString(AnsiColor.RED, "MANAGER -> ", AnsiColor.DEFAULT);
      if (connected != null) {
        prompt = AnsiOutput.toString(AnsiColor.RED, "MANAGER (", AnsiColor.GREEN, connected, AnsiColor.RED, ")-> ",
            AnsiColor.DEFAULT);
      }
      String messaggio = console.readLine(prompt);
      boolean trattato = false;
      if (messaggio.equals("!end")) {
        for (Object t : attive) {
          // t.end();
        }
        attivo = false;
        trattato = true;
        break;
      }
      if (messaggio.equals("!list")) {
        System.out.println(gson.toJson(listUsers()));
        for (Object a : registeredUsers()) {
          // System.out.println(gson.toJson(a.getChat().getParticipant()));
        }
        trattato = true;
      }
      if (messaggio.equals("!disconnect")) {
        connected = null;
        trattato = true;
      }
      if (messaggio.equals("!connect")) {
        System.out.println(gson.toJson(listUsers()));
        String connectedProva = console.readLine(AnsiOutput.toString(AnsiColor.GREEN, "QUALE? ", AnsiColor.DEFAULT));
        boolean trovato = false;
        for (String ta : listUsers()) {
          if (ta.equals(connectedProva)) {
            connected = connectedProva;
            trovato = true;
          }
        }
        if (trovato == false) {
          System.out.println(connectedProva + " non è una sessione valida");
        }
        trattato = true;
      }
      if (connected != null && trattato == false) {
        sendMessage(connected, messaggio);
      }
    }
    running = false;
    // watsonMaster.fermaConversazione();
  }

  @Override
  public void chatCreated(Chat chat, boolean createdLocally) {
    chat.addMessageListener(this);
  }

  @SuppressWarnings("unused")
  @Override
  public void processMessage(Chat chat, Message message) {
    boolean trovata = false;
    System.out.println(
        AnsiOutput.toString(AnsiColor.RED, chat.getParticipant(), AnsiColor.DEFAULT, " ->" + message.getBody()));
    for (Object t : attive) {
      if (true) { // TODO: da rivedere
        // if (t.getChat() == chat) {
        try {
          // sendMessage(chat.getParticipant(), t.entrata(message));
          sendMessage(chat.getParticipant(), "TODO");
          // System.out
          // .println("Start chat with " + t.getWebuser() + " have " +
          // String.valueOf(t.getContatore()));
        } catch (XmppStringprepException | NotConnectedException | InterruptedException e) {
          logger.warn(e.getMessage());
        }
        trovata = true;
        if (message.getBody().equals("Grazie per il tuo tempo. A presto.")) {
          // t.end();
          try {
            sendMessage(chat.getParticipant(), "!end");
          } catch (NotConnectedException | XmppStringprepException | InterruptedException e) {
          }
        }
      }
    }
    if (trovata == false) {
      Object f;// = new Object(watsonMaster, chat);
      // System.out.println("created chat with " + f.getWebuser());
      try {
        // sendMessage(chat.getParticipant(), f.entrata(message));
        sendMessage(chat.getParticipant(), "TODO");
      } catch (XmppStringprepException | NotConnectedException | InterruptedException e) {
        logger.warn(e.getMessage());
      }
    }

  }

}
