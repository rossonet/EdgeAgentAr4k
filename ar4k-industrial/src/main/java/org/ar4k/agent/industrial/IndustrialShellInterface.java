/**
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
package org.ar4k.agent.industrial;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.toList;

import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.opcua.client.OpcUaTreeDataNode;
import org.ar4k.agent.opcua.client.OpcUaClientConfig;
import org.ar4k.agent.opcua.client.OpcUaClientNode;
import org.ar4k.agent.opcua.client.OpcUaClientService;
import org.ar4k.agent.opcua.server.OpcUaServerConfig;
import org.ar4k.agent.opcua.server.OpcUaServerService;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.X509IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.json.JSONObject;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Interfaccia da linea di comando per configurazione della connessione
 *         OPC UA e MQTT
 *
 */

//TODO valutare implementazione 4Diac Forte

//TODO integrazione con UNIPI AXON S105

//TODO implementare comandi di navigazione OPCUA client (discovery, list nodeid)

//TODO implementare gestione eventi OPCUA con presa visione e conferma

@ShellCommandGroup("Industrial Commands")
@ShellComponent
@RestController
@RequestMapping("/industrialInterface")
public class IndustrialShellInterface extends AbstractShellHelper {

	protected Availability sessionClientOpcOk() {
		return (sessionOk().equals(Availability.available()) && getWorkingService() instanceof OpcUaClientConfig)
				? Availability.available()
				: Availability.unavailable("you must select a OPCUA client configuration service");
	}

	OpcUaServerService opcUaServer = null;

	protected Availability testActiveOpcNull() {
		return opcUaServer == null ? Availability.available()
				: Availability.unavailable("a ActiveMQ broker exists with name "
						+ opcUaServer.getServer().getServer().getDiagnosticsSummary());
	}

	protected Availability testActiveOpcRunning() {
		return opcUaServer != null ? Availability.available()
				: Availability.unavailable("activeMQ broker is not running");
	}

	@ShellMethod(value = "Add a OPCUA service client to the selected configuration", group = "OPC UA Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addOpcUaClientService(@ShellOption(optOut = true) @Valid OpcUaClientConfig service) {
		getWorkingConfig().pots.add(service);
	}

	@ShellMethod(value = "Add a OPCUA service server to the selected configuration", group = "OPC UA Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addOpcUaServerService(@ShellOption(optOut = true) @Valid OpcUaServerConfig service) {
		getWorkingConfig().pots.add(service);
	}

	@ShellMethod(value = "Start OPC UA Server", group = "OPC UA Commands")
	@ManagedOperation
	@ShellMethodAvailability("testActiveOpcNull")
	public void serverOpcUaStart(@ShellOption(optOut = true) @Valid OpcUaServerConfig opcUaConfig) {
		final OpcUaServerService opcUaInstance = (OpcUaServerService) opcUaConfig.instantiate();
		opcUaInstance.init();
		opcUaServer = opcUaInstance;
	}

	@ShellMethod(value = "Stop OPC UA Server", group = "OPC UA Commands")
	@ManagedOperation
	@ShellMethodAvailability("testActiveOpcRunning")
	public void serverOpcUaStop() throws Exception {
		opcUaServer.kill();
		opcUaServer = null;
	}

	@ShellMethod(value = "List nodes in opcua client config", group = "OPC UA Commands")
	@ManagedOperation
	@ShellMethodAvailability("sessionClientOpcOk")
	public Collection<String> opcClientListNodes() {
		Collection<String> result = new HashSet<>();
		for (final OpcUaClientNode singleSubscription : ((OpcUaClientConfig) getWorkingService()).subscriptions) {
			result.add(singleSubscription.toString());
		}
		return result;
	}

	@ShellMethod(value = "Remove node in opcua client config", group = "OPC UA Commands")
	@ManagedOperation
	@ShellMethodAvailability("sessionClientOpcOk")
	public void opcUaClientRemoveNode(@ShellOption(help = "trigger cnc uuid") String uuid) {
		OpcUaClientNode target = null;
		final List<OpcUaClientNode> nodes = ((OpcUaClientConfig) getWorkingService()).subscriptions;
		for (final OpcUaClientNode n : nodes) {
			if (n.uuid.equals(uuid)) {
				target = n;
			}
		}
		if (target != null) {
			nodes.remove(target);
		}
	}

	@ShellMethod(value = "Add node to opcua client config", group = "OPC UA Commands")
	@ManagedOperation
	@ShellMethodAvailability("sessionClientOpcOk")
	public void opcUaClientAddNode(@ShellOption(optOut = true) @Valid OpcUaClientNode node) {
		((OpcUaClientConfig) getWorkingService()).subscriptions.add(node);
	}

	private Map<String, Object> browseOpcUaNodes(String endpointString, String hostname, Integer port,
			String serverPath, Boolean rewriteEndpoint, String authType, String securityMode, String cryptoMode,
			String user, String password, String cryptoCa, String cryptoCrt, String cryptoKey, String crt, String key,
			String clientName, String clientUrn, int baseNodeNamespaceIndex, String baseNodeIdentifier, int maxdeep) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			OpcUaClient client = createClient(endpointString, hostname, port, serverPath, rewriteEndpoint, authType,
					securityMode, cryptoMode, user, password, cryptoCa, cryptoCrt, cryptoKey, crt, key, clientName,
					clientUrn);
			if (client != null) {
				if (baseNodeIdentifier == null) {
					browseNode(maxdeep, resultMap, client, Identifiers.RootFolder);
				} else {
					browseNode(maxdeep, resultMap, client, new NodeId(baseNodeNamespaceIndex, baseNodeIdentifier));
				}
			}
		} catch (Exception ex) {
			logger.logException(ex);
		}
		return resultMap;
	}

	private Map<String, Object> browseNode(int maxDeep, OpcUaClient client, NodeId browseRoot) {
		Map<String, Object> newLevel = new HashMap<>();
		BrowseDescription browse = new BrowseDescription(browseRoot, BrowseDirection.Forward, Identifiers.References,
				true, uint(NodeClass.Object.getValue() | NodeClass.Variable.getValue()),
				uint(BrowseResultMask.All.getValue()));
		try {
			BrowseResult browseResult = client.browse(browse).get();

			List<ReferenceDescription> references = toList(browseResult.getReferences());

			for (ReferenceDescription rd : references) {
				logger.info("{} Node={}", indent, rd.getBrowseName().getName());

				// recursively browse to children
				rd.getNodeId().toNodeId(client.getNamespaceTable())
						.ifPresent(nodeId -> browseNode(maxDeep - 1, client, nodeId));
			}
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Browsing nodeId={} failed: {}", browseRoot, e.getMessage(), e);
		}
		return newLevel;
	}

	private Map<String, Object> discoverOpcUaServers(String endpointString, String hostname, Integer port,
			String serverPath, Boolean rewriteEndpoint, String authType, String securityMode, String cryptoMode,
			String user, String password, String cryptoCa, String cryptoCrt, String cryptoKey, String crt, String key,
			String clientName, String clientUrn, boolean advancedView) {
		Map<String, Object> result = new HashMap<>();
		try {
			OpcUaClient client = null;
			client = createClient(endpointString, hostname, port, serverPath, rewriteEndpoint, authType, securityMode,
					cryptoMode, user, password, cryptoCa, cryptoCrt, cryptoKey, crt, key, clientName, clientUrn);
			String endpointQuery = null;
			if (client != null) {
				if (client.getConfig() != null) {
					if (client.getConfig().getIdentityProvider() != null)
						result.put("identityProvider", client.getConfig().getIdentityProvider().toString());
					if (advancedView && client.getConfig().getMaxPendingPublishRequests() != null)
						result.put("maxPendingPublishRequests",
								client.getConfig().getMaxPendingPublishRequests().toString());
					if (advancedView && client.getConfig().getMaxResponseMessageSize() != null)
						result.put("maxResponseMessageSize", client.getConfig().getMaxResponseMessageSize().toString());
					if (advancedView && client.getConfig().getRequestTimeout() != null)
						result.put("requestTimeout", client.getConfig().getRequestTimeout().toString());
					if (advancedView && client.getConfig().getSessionTimeout() != null)
						result.put("sessionTimeout", client.getConfig().getSessionTimeout().toString());
					if (client.getConfig().getSessionName() != null)
						result.put("sessionName", client.getConfig().getSessionName().get().toString());
				}
				if (client.getStackClient() != null && client.getStackClient().getConfig() != null) {
					if (advancedView && client.getStackClient().getConfig().getEndpoint().getEndpointUrl() != null)
						result.put("endpointUrl", client.getStackClient().getConfig().getEndpoint().getEndpointUrl());
					if (advancedView && client.getStackClient().getConfig().getEndpoint() != null)
						result.put("sndpoint", client.getStackClient().getConfig().getEndpoint().toString());
					if (client.getStackClient().getConfig().getEndpoint() != null)
						endpointQuery = client.getStackClient().getConfig().getEndpoint().getEndpointUrl();
				}
				if (advancedView
						&& client.getStackClient().getConfig().getEndpoint().getServer().getApplicationName() != null)
					result.put("applicationName", client.getStackClient().getConfig().getEndpoint().getServer()
							.getApplicationName().toString());
				if (advancedView
						&& client.getStackClient().getConfig().getEndpoint().getServer().getApplicationUri() != null)
					result.put("applicationUri", client.getStackClient().getConfig().getEndpoint().getServer()
							.getApplicationUri().toString());
				if (advancedView
						&& client.getStackClient().getConfig().getEndpoint().getServer().getProductUri() != null)
					result.put("productUri",
							client.getStackClient().getConfig().getEndpoint().getServer().getProductUri().toString());
				if (advancedView && client.getStackClient().getConfig().getChannelLifetime() != null)
					result.put("channelLifetime", client.getStackClient().getConfig().getChannelLifetime().toString());
			}
			result.put("endpointQuery", endpointQuery);
			List<Map<String, Object>> serverList = new ArrayList<>();
			if (client != null && client.getStackClient() != null)
				for (EndpointDescription ep : DiscoveryClient.getEndpoints(endpointQuery).get()) {
					try {
						Map<String, Object> singEp = new HashMap<>();
						List<String> supportedSecurityModes = new ArrayList<>();
						List<String> supportedCryptoModes = new ArrayList<>();
						List<String> supportedAuthTypes = new ArrayList<>();
						URI uriEp = new URI(ep.getEndpointUrl()).parseServerAuthority();
						singEp.put("schema", uriEp.getScheme());
						singEp.put("hostname", uriEp.getHost());
						singEp.put("port", uriEp.getPort());
						singEp.put("serverPath", uriEp.getPath().isEmpty() ? "/" : uriEp.getPath());
						singEp.put("ca", Base64.getEncoder().encodeToString(ep.getServerCertificate().bytes()));
						if (ep.getSecurityPolicyUri() != null)
							supportedCryptoModes.add(ep.getSecurityPolicyUri());
						for (org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy stObj : ep
								.getUserIdentityTokens()) {
							if (ep.getSecurityMode().toString().equals("None"))
								supportedSecurityModes.add("none");
							if (ep.getSecurityMode().toString().equals("SignAndEncrypt"))
								supportedSecurityModes.add("signAndEncrypt");
							if (ep.getSecurityMode().toString().equals("Sign"))
								supportedSecurityModes.add("sign");
							if (stObj.getTokenType().toString().equals("UserName"))
								supportedAuthTypes.add("password");
							if (stObj.getTokenType().toString().equals("Certificate"))
								supportedAuthTypes.add("certificate");
							if (stObj.getTokenType().toString().equals("Anonymous"))
								supportedAuthTypes.add("none");
						}
						singEp.put("supportedSecurityModes", new ArrayList<>(new HashSet<>(supportedSecurityModes)));
						singEp.put("supportedCryptoModes", new ArrayList<>(new HashSet<>(supportedCryptoModes)));
						singEp.put("supportedAuthTypes", new ArrayList<>(new HashSet<>(supportedAuthTypes)));
						serverList.add(singEp);
					} catch (Exception a) {
						logger.info("error in discovery " + a.getMessage());
					}
				}
			result.put("endpoints", serverList);
		} catch (Exception ex) {
			logger.logException(ex);
		}
		return result;
	}

	private OpcUaClient createClient(String endpointString, String hostname, Integer port, String serverPath,
			Boolean rewriteEndpoint, String authType, String securityMode, String cryptoMode, String user,
			String password, String cryptoCa, String cryptoCrt, String cryptoKey, String crt, String key,
			String clientName, String clientUrn) {
		String securityPolicyTarget = null;
		String securityModeTarget = null;
		IdentityProvider idp = null;
		OpcUaClient client = null;
		boolean useCert = false;
		if ((endpointString == null || endpointString.isEmpty())
				&& (hostname != null && !hostname.isEmpty() && port != 0 && serverPath != null)) {
			if (serverPath.equals("/"))
				serverPath = "";
			endpointString = "opc.tcp://" + hostname + ":" + String.valueOf(port) + serverPath;
		}
		switch (cryptoMode) {
		case "none":
			securityPolicyTarget = SecurityPolicy.None.getUri();
			cryptoCrt = null;
			cryptoKey = null;
			securityMode = "none";
			break;
		case "basic128Rsa15":
			useCert = true;
			securityPolicyTarget = SecurityPolicy.Basic128Rsa15.getUri();
			break;
		case "basic256":
			useCert = true;
			securityPolicyTarget = SecurityPolicy.Basic256.getUri();
			break;
		case "basic256Sha256":
			useCert = true;
			securityPolicyTarget = SecurityPolicy.Basic256Sha256.getUri();
			break;
		default:
			securityPolicyTarget = SecurityPolicy.None.getUri();
			break;
		}
		boolean certAuth = false;
		switch (securityMode) {
		case "none":
			securityModeTarget = "None";
			securityPolicyTarget = SecurityPolicy.None.getUri();
			cryptoCrt = null;
			cryptoKey = null;
			cryptoMode = "none";
			useCert = false;
			break;
		case "sign":
			securityModeTarget = "Sign";
			useCert = true;
			break;
		case "signAndEncrypt":
			securityModeTarget = "SignAndEncrypt";
			useCert = true;
			break;
		default:
			securityModeTarget = "None";
			break;
		}
		if (authType.equals("certificate")) {
			useCert = true;
			certAuth = true;
		}
		if (authType.equals("none")) {
			certAuth = false;
			user = null;
			password = null;
			crt = null;
			key = null;
		}
		List<EndpointDescription> endpoints = null;
		try {
			logger.info("try to list the endpoints at " + endpointString);
			endpoints = DiscoveryClient.getEndpoints(endpointString).get();
		} catch (InterruptedException | ExecutionException e1) {
			logger.logException(e1);
		}
		List<EndpointDescription> targetsEndPoint = new ArrayList<>();
		if (endpoints != null) {
			logger.info("looking for a endpoint with the specific features");
			int count = 0;
			for (EndpointDescription e : endpoints) {
				count++;

				if (e.getSecurityMode().toString().equals(securityModeTarget)
						&& e.getSecurityPolicyUri().equals(securityPolicyTarget)) {
					targetsEndPoint.add(e);
					logger.info("ENDPOINT FOUND:\n" + e.toString());
				} else {
					logger.warn("THE ENDPOINT " + count + " IS NOT GOOD:\n" + e.toString());
				}
			}
		}
		if (targetsEndPoint != null && !targetsEndPoint.isEmpty()) {
			for (EndpointDescription e : targetsEndPoint) {
				try {
					KeyStoreLoader loader = null;
					AuthKeyStoreLoader authLoader = null;
					if (useCert) {
						loader = new KeyStoreLoader();
						if (cryptoKey != null && !cryptoKey.isEmpty() && cryptoCrt != null && !cryptoCrt.isEmpty()) {
							loader.setClientKeyPair(cryptoKey, cryptoCrt);
							logger.info("use key/crt in conf");
						} else {
							loader.create();
							logger.info("generate key/crt");
						}
					}
					if (authType.equals("password") || certAuth) {
						if (authType.equals("password") && !certAuth) {
							idp = new UsernameProvider(user, password);
						}
						if (certAuth) {
							authLoader = new AuthKeyStoreLoader();
							authLoader.setClientKeyPair(key, crt);
							idp = new X509IdentityProvider(authLoader.getClientCertificate(),
									authLoader.getPrivateKey());
						}
					} else {
						idp = new AnonymousProvider();
					}
					EndpointDescription endpoint = e;
					if (rewriteEndpoint == null || rewriteEndpoint)
						endpoint = OpcUaClientService.updateEndpointUrl(endpoint, hostname);
					org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig config = null;
					if (loader != null) {
						config = org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig.builder()
								.setApplicationName(LocalizedText.english(clientName)).setApplicationUri(clientUrn)
								.setCertificate(loader.getClientCertificate()).setKeyPair(loader.getClientKeyPair())
								.setEndpoint(endpoint).setIdentityProvider(idp).setRequestTimeout(uint(15000)).build();
					} else {
						config = org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig.builder()
								.setApplicationName(LocalizedText.english(clientName)).setApplicationUri(clientUrn)
								.setEndpoint(endpoint).setIdentityProvider(idp).setRequestTimeout(uint(10000)).build();
					}
					client = OpcUaClient.create(config);
					client.connect().get();
				} catch (Exception sa) {
					logger.logException(sa);
				}
			}
		} else {
			logger.info("no endpoint found for the client connection");
		}
		return client;
	}

}
