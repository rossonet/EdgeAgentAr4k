package org.ar4k.agent.tunnels.http2.beacon.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.ar4k.agent.core.interfaces.ConfigSeed;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.tunnels.http2.beacon.BeaconAgent;
import org.ar4k.agent.tunnels.http2.beacon.RegistrationRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http2.grpc.beacon.AgentRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ApproveAgentRequestRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Command;
import org.ar4k.agent.tunnels.http2.grpc.beacon.CommandReplyRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.CommandType;
import org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReport;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Empty;
import org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessage;
import org.ar4k.agent.tunnels.http2.grpc.beacon.HealthRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsRequestReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RequestToAgent;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RpcServiceV1Grpc;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Status;
import org.ar4k.agent.tunnels.http2.grpc.beacon.StatusValue;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Timestamp;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.json.JSONObject;

import io.grpc.stub.StreamObserver;

class BeaconServerRpcService extends RpcServiceV1Grpc.RpcServiceV1ImplBase {

	/**
	 * 
	 */
	private final BeaconServer beaconServer;

	/**
	 * @param beaconServer
	 */
	BeaconServerRpcService(BeaconServer beaconServer) {
		this.beaconServer = beaconServer;
	}

	@Override
	public void approveAgentRequest(ApproveAgentRequestRequest request, StreamObserver<Status> responseObserver) {
		try {
			org.ar4k.agent.tunnels.http2.grpc.beacon.Status.Builder status = Status.newBuilder()
					.setStatus(StatusValue.BAD);
			for (final RegistrationRequest r : this.beaconServer.listAgentRequest) {
				if (r.idRequest.equals(request.getIdRequest())) {
					r.approved = true;
					r.approvedDate = Timestamp.newBuilder().setSeconds(new Date().getTime()).build();
					r.pemApproved = request.getCert();
					r.note = request.getNote();
					status = Status.newBuilder().setStatus(StatusValue.GOOD);
				}
			}
			responseObserver.onNext(status.build());
			responseObserver.onCompleted();
		} catch (final Exception a) {
			BeaconServer.logger.logException(a);
		}
	}

	@Override
	public void completeCommand(CompleteCommandRequest request, StreamObserver<CompleteCommandReply> responseObserver) {
		try {
			final String idRequest = UUID.randomUUID().toString();
			for (final BeaconAgent at : this.beaconServer.agents) {
				if (at.getAgentUniqueName().equals(request.getAgentTarget().getAgentUniqueName())) {
					final RequestToAgent rta = RequestToAgent.newBuilder().setCaller(request.getAgentSender())
							.setUniqueIdRequest(idRequest).setType(CommandType.COMPLETE_COMMAND)
							.addAllWords(request.getWordsList()).setWordIndex(request.getWordIndex())
							.setPosition(request.getPosition()).build();
					at.addRequestForAgent(rta);
					break;
				}
			}
			CommandReplyRequest agentReply = null;
			agentReply = this.beaconServer.waitReply(idRequest, BeaconServer.DEFAULT_TIMEOUT);
			if (agentReply != null) {
				final List<String> sb = new ArrayList<>();
				for (final String cr : agentReply.getRepliesList()) {
					sb.add(cr);
				}
				final CompleteCommandReply finalReply = CompleteCommandReply.newBuilder().addAllReplies(sb).build();
				responseObserver.onNext(finalReply);
			}
			responseObserver.onCompleted();
		} catch (final Exception e) {
			BeaconServer.logger.logException(e);
		}
	}

	@Override
	public void elaborateMessage(ElaborateMessageRequest request,
			StreamObserver<ElaborateMessageReply> responseObserver) {
		try {
			final String idRequest = UUID.randomUUID().toString();
			for (final BeaconAgent at : this.beaconServer.agents) {
				if (at.getAgentUniqueName().equals(request.getAgentTarget().getAgentUniqueName())) {
					final RequestToAgent rta = RequestToAgent.newBuilder().setCaller(request.getAgentSender())
							.setUniqueIdRequest(idRequest).setType(CommandType.ELABORATE_MESSAGE_COMMAND)
							.setRequestCommand(request.getCommandMessage()).build();
					at.addRequestForAgent(rta);
					break;
				}
			}
			CommandReplyRequest agentReply = null;
			agentReply = this.beaconServer.waitReply(idRequest, BeaconServer.DEFAULT_TIMEOUT);
			if (agentReply != null) {
				final StringBuilder sb = new StringBuilder();
				for (final String cr : agentReply.getRepliesList()) {
					sb.append(cr + "\n");
				}
				final ElaborateMessageReply finalReply = ElaborateMessageReply.newBuilder().setReply(sb.toString())
						.build();
				responseObserver.onNext(finalReply);
			}
			responseObserver.onCompleted();
		} catch (final Exception e) {
			BeaconServer.logger.logException(e);
		}
	}

	@Override
	public void getConfigRuntime(Agent agent, StreamObserver<ConfigReply> responseObserver) {
		try {
			final String idRequest = UUID.randomUUID().toString();
			for (final BeaconAgent at : this.beaconServer.agents) {
				if (at.getAgentUniqueName().equals(agent.getAgentUniqueName())) {
					final RequestToAgent rta = RequestToAgent.newBuilder().setCaller(agent)
							.setUniqueIdRequest(idRequest).setType(CommandType.GET_CONFIGURATION).build();
					at.addRequestForAgent(rta);
					break;
				}
			}
			CommandReplyRequest agentReply = null;
			agentReply = this.beaconServer.waitReply(idRequest, BeaconServer.DEFAULT_TIMEOUT);
			elaborateConfigReply(responseObserver, agentReply);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			BeaconServer.logger.logException(e);
		}
	}

	@Override
	public void getRuntimeProvides(Agent agent, StreamObserver<ListStringReply> responseObserver) {
		try {
			final String idRequest = UUID.randomUUID().toString();
			for (final BeaconAgent at : this.beaconServer.agents) {
				if (at.getAgentUniqueName().equals(agent.getAgentUniqueName())) {
					final RequestToAgent rta = RequestToAgent.newBuilder().setCaller(agent)
							.setUniqueIdRequest(idRequest).setType(CommandType.GET_PROVIDES).build();
					at.addRequestForAgent(rta);
					break;
				}
			}
			CommandReplyRequest agentReply = null;
			agentReply = this.beaconServer.waitReply(idRequest, BeaconServer.DEFAULT_TIMEOUT);
			elaborateProvidesReply(responseObserver, agentReply);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			BeaconServer.logger.logException(e);
		}
	}

	@Override
	public void getRuntimeRequired(Agent agent, StreamObserver<ListStringReply> responseObserver) {
		try {
			final String idRequest = UUID.randomUUID().toString();
			for (final BeaconAgent at : this.beaconServer.agents) {
				if (at.getAgentUniqueName().equals(agent.getAgentUniqueName())) {
					final RequestToAgent rta = RequestToAgent.newBuilder().setCaller(agent)
							.setUniqueIdRequest(idRequest).setType(CommandType.GET_REQUIRED).build();
					at.addRequestForAgent(rta);
					break;
				}
			}
			CommandReplyRequest agentReply = null;
			agentReply = this.beaconServer.waitReply(idRequest, BeaconServer.DEFAULT_TIMEOUT);
			elaborateRequiredReply(responseObserver, agentReply);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			BeaconServer.logger.logException(e);
		}
	}

	@Override
	public void listAgents(Empty request, StreamObserver<ListAgentsReply> responseObserver) {
		try {
			final List<Agent> values = new ArrayList<>();
			for (final BeaconAgent r : this.beaconServer.agents) {
				final Agent a = Agent.newBuilder().setAgentUniqueName(r.getAgentUniqueName())
						.setShortDescription(r.getShortDescription()).setRegisterData(r.getRegisterReply())
						.setJsonHardwareInfo(r.getHardwareInfoAsJson().toString(2))
						.setLastContact(Timestamp.newBuilder().setSeconds(r.getLastCall().getMillis() / 1000)).build();
				values.add(a);
			}
			final ListAgentsReply reply = ListAgentsReply.newBuilder().addAllAgents(values)
					.setResult(Status.newBuilder().setStatus(StatusValue.GOOD)).build();
			responseObserver.onNext(reply);
			responseObserver.onCompleted();
		} catch (final Exception a) {
			BeaconServer.logger.logException(a);
		}
	}

	@Override
	public void listAgentsRequestComplete(Empty request, StreamObserver<ListAgentsRequestReply> responseObserver) {
		try {
			final List<AgentRequest> values = this.beaconServer.listAgentRequests();
			final ListAgentsRequestReply reply = ListAgentsRequestReply.newBuilder().addAllRequests(values)
					.setResult(Status.newBuilder().setStatus(StatusValue.GOOD)).build();
			responseObserver.onNext(reply);
			responseObserver.onCompleted();
		} catch (final Exception a) {
			BeaconServer.logger.logException(a);
		}
	}

	@Override
	public void listAgentsRequestToDo(Empty request, StreamObserver<ListAgentsRequestReply> responseObserver) {
		try {
			final List<AgentRequest> values = this.beaconServer.listAgentRequests();
			final ListAgentsRequestReply reply = ListAgentsRequestReply.newBuilder().addAllRequests(values)
					.setResult(Status.newBuilder().setStatus(StatusValue.GOOD)).build();
			responseObserver.onNext(reply);
			responseObserver.onCompleted();
		} catch (final Exception a) {
			BeaconServer.logger.logException(a);
		}
	}

	@Override
	public void listCommands(ListCommandsRequest request, StreamObserver<ListCommandsReply> responseObserver) {
		try {
			final String idRequest = UUID.randomUUID().toString();
			for (final BeaconAgent at : this.beaconServer.agents) {
				if (at.getAgentUniqueName().equals(request.getAgentTarget().getAgentUniqueName())) {
					final RequestToAgent rta = RequestToAgent.newBuilder().setCaller(request.getAgentSender())
							.setUniqueIdRequest(idRequest).setType(CommandType.LIST_COMMANDS).build();
					at.addRequestForAgent(rta);
					break;
				}
			}
			CommandReplyRequest agentReply = null;
			agentReply = this.beaconServer.waitReply(idRequest, BeaconServer.DEFAULT_TIMEOUT);
			if (agentReply != null) {
				final List<Command> listCommands = new ArrayList<>();
				for (final String cr : agentReply.getRepliesList()) {
					final Command c = Command.newBuilder().setAgentSender(agentReply.getAgentSender()).setCommand(cr)
							.build();
					listCommands.add(c);
				}
				final ListCommandsReply finalReply = ListCommandsReply.newBuilder().addAllCommands(listCommands)
						.build();
				responseObserver.onNext(finalReply);
			}
			responseObserver.onCompleted();
		} catch (final Exception e) {
			BeaconServer.logger.logException(e);
		}
	}

	@Override
	public void pollingCmdQueue(Agent request, StreamObserver<FlowMessage> responseObserver) {
		try {
			final List<RequestToAgent> values = new ArrayList<>();
			for (final BeaconAgent at : this.beaconServer.agents) {
				if (at.getAgentUniqueName().equals(request.getAgentUniqueName())) {
					values.addAll(at.getCommandsToBeExecute());
					break;
				}
			}
			final FlowMessage fm = FlowMessage.newBuilder().addAllToDoList(values).build();
			responseObserver.onNext(fm);
			responseObserver.onCompleted();
		} catch (final Exception a) {
			BeaconServer.logger.logException(a);
		}
	}

	@Override
	public void register(RegisterRequest request, StreamObserver<RegisterReply> responseObserver) {
		try {
			final String uniqueClientNameForBeacon = request.getName();
			final org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply.Builder replyBuilder = RegisterReply
					.newBuilder();
			RegisterReply reply = null;
			BeaconServer.logger.info("registration request -> " + request);
			BeaconServer.logger.info("registration SSL channel -> "
					+ !Boolean.valueOf(this.beaconServer.getHomunculus().getStarterProperties().getBeaconClearText()));
			if (!Boolean.valueOf(this.beaconServer.getHomunculus().getStarterProperties().getBeaconClearText())
					&& request.getRequestCsr() != null && !request.getRequestCsr().isEmpty()) {
				if (this.beaconServer.acceptAllCerts || isCsrApproved(request.getRequestCsr())) {
					reply = replyBuilder.setStatusRegistration(Status.newBuilder().setStatus(StatusValue.GOOD))
							.setRegisterCode(uniqueClientNameForBeacon)
							.setMonitoringFrequency(this.beaconServer.defaultPollTime)
							.setCert(getFirmedCert(request.getRequestCsr())).setCa(this.beaconServer.caChainPem)
							.build();
					this.beaconServer.agents.add(new BeaconAgent(request, reply));
				} else {
					final RegistrationRequest newRequest = new RegistrationRequest(request);
					addNewCsrToAgentRequest(newRequest);
					reply = replyBuilder.setStatusRegistration(Status.newBuilder().setStatus(StatusValue.WAIT_HUMAN))
							.setRegisterCode(uniqueClientNameForBeacon)
							.setMonitoringFrequency(this.beaconServer.defaultPollTime)
							.setCa(this.beaconServer.caChainPem).build();
				}
			} else {
				reply = replyBuilder.setStatusRegistration(Status.newBuilder().setStatus(StatusValue.GOOD))
						.setRegisterCode(uniqueClientNameForBeacon)
						.setMonitoringFrequency(this.beaconServer.defaultPollTime).build();
				this.beaconServer.agents.add(new BeaconAgent(request, reply));
			}
			responseObserver.onNext(reply);
			responseObserver.onCompleted();
		} catch (final Exception a) {
			BeaconServer.logger.logException(a);
		}
	}

	@Override
	public void sendCommandReply(CommandReplyRequest request, StreamObserver<Status> responseObserver) {
		try {
			this.beaconServer.repliesQueue.put(request.getUniqueIdRequest(), request);
			responseObserver.onNext(Status.newBuilder().setStatus(StatusValue.GOOD).build());
			responseObserver.onCompleted();
		} catch (final Exception a) {
			BeaconServer.logger.logException(a);
		}
	}

	@Override
	public void sendConfigRuntime(ConfigReport request, StreamObserver<ConfigReply> responseObserver) {
		try {
			final String idRequest = UUID.randomUUID().toString();
			for (final BeaconAgent at : this.beaconServer.agents) {
				if (at.getAgentUniqueName().equals(request.getAgent().getAgentUniqueName())) {
					final RequestToAgent rta = RequestToAgent.newBuilder().setCaller(request.getAgent())
							.setUniqueIdRequest(idRequest).setType(CommandType.SET_CONFIGURATION)
							.setRequestCommand(request.getBase64Config()).build();
					at.addRequestForAgent(rta);
					break;
				}
			}
			CommandReplyRequest agentReply = null;
			agentReply = this.beaconServer.waitReply(idRequest, BeaconServer.DEFAULT_TIMEOUT);
			elaborateConfigReply(responseObserver, agentReply);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			BeaconServer.logger.logException(e);
		}
	}

	@Override
	public void sendHealth(HealthRequest request, io.grpc.stub.StreamObserver<Status> responseObserver) {
		try {
			try {
				for (final BeaconAgent at : this.beaconServer.agents) {
					if (at.getAgentUniqueName().equals(request.getAgentSender().getAgentUniqueName())) {
						at.setHardwareInfo(new JSONObject(request.getJsonHardwareInfo()));
						break;
					}
				}
				responseObserver.onNext(Status.newBuilder().setStatusValue(StatusValue.GOOD.getNumber()).build());
				responseObserver.onCompleted();
			} catch (final Exception e) {
				BeaconServer.logger.logException(e);
			}
		} catch (final Exception a) {
			BeaconServer.logger.logException(a);
		}
	}

	private void addNewCsrToAgentRequest(RegistrationRequest newRequest) {
		boolean isPresent = false;
		for (RegistrationRequest registeredRequest : this.beaconServer.listAgentRequest) {
			if (registeredRequest.requestCsr.equals(newRequest.getRegisterRequest().getRequestCsr())) {
				isPresent = true;
				break;
			}
		}
		if (!isPresent) {
			this.beaconServer.listAgentRequest.add(newRequest);
		}
	}

	private boolean checkRegexOnX509(X500Name subject) {
		if (this.beaconServer.filterBlackListCertRegister != null
				&& !this.beaconServer.filterBlackListCertRegister.isEmpty()) {
			if (this.beaconServer.filterBlackListCertRegisterPattern == null) {
				this.beaconServer.filterBlackListCertRegisterPattern = Pattern
						.compile(this.beaconServer.filterBlackListCertRegister);
			}
			final RDN cn = subject.getRDNs(BCStyle.CN)[0];
			return this.beaconServer.filterBlackListCertRegisterPattern
					.matcher(IETFUtils.valueToString(cn.getFirst().getValue())).matches();
		} else {
			return false;
		}
	}

	private void elaborateConfigReply(StreamObserver<ConfigReply> responseObserver, CommandReplyRequest agentReply)
			throws IOException, ClassNotFoundException {
		if (agentReply != null) {
			final String base64Config = agentReply.getBase64Config();
			final ConfigSeed configSeed = ConfigHelper.fromBase64(base64Config);
			final ConfigReply finalReply = ConfigReply.newBuilder().setBase64Config(base64Config)
					.setJsonConfig(ConfigHelper.toJson(configSeed)).setYmlConfig(ConfigHelper.toYaml(configSeed))
					.build();
			responseObserver.onNext(finalReply);
		}
	}

	private void elaborateProvidesReply(StreamObserver<ListStringReply> responseObserver,
			CommandReplyRequest agentReply) {
		if (agentReply != null) {
			final List<String> providesList = agentReply.getRepliesList();
			final ListStringReply finalReply = ListStringReply.newBuilder().setAgentSender(agentReply.getAgentSender())
					.setLinesNumber(providesList.size()).addAllListDatas(providesList).build();
			responseObserver.onNext(finalReply);
		}

	}

	private void elaborateRequiredReply(StreamObserver<ListStringReply> responseObserver,
			CommandReplyRequest agentReply) {
		if (agentReply != null) {
			final List<String> providesList = agentReply.getRepliesList();
			final ListStringReply finalReply = ListStringReply.newBuilder().setAgentSender(agentReply.getAgentSender())
					.setLinesNumber(providesList.size()).addAllListDatas(providesList).build();
			responseObserver.onNext(finalReply);
		}
	}

	private String getFirmedCert(String requestCsr) throws IOException {
		BeaconServer.logger.debug("SIGN CSR BASE64 " + requestCsr);
		final String requestAlias = "beacon-" + UUID.randomUUID().toString().replace("-", "");
		final byte[] data = Base64.getDecoder().decode(requestCsr);
		final PKCS10CertificationRequest csrDecoded = new PKCS10CertificationRequest(data);
		if (!checkRegexOnX509(csrDecoded.getSubject())) {
			BeaconServer.logger.warn("SIGN CSR " + csrDecoded.getSubject());
			return this.beaconServer.getHomunculus().getMyIdentityKeystore().signCertificateBase64(csrDecoded,
					requestAlias, BeaconServer.SIGN_TIME, this.beaconServer.getAliasBeaconServerSignMaster());
		} else
			BeaconServer.logger.warn("\nNOT SIGN CERT\n" + csrDecoded.getSubject()
					+ "\nbeacause it matches the blacklist [" + this.beaconServer.filterBlackListCertRegister + "]");
		return null;
	}

	private boolean isCsrApproved(String newRequestCsr) {
		for (RegistrationRequest registeredRequest : this.beaconServer.listAgentRequest) {
			if (newRequestCsr.equals(registeredRequest.requestCsr)) {
				if (registeredRequest.approved) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

}