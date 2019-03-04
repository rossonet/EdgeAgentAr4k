package org.ar4k.agent.cortex.conversationBot.messages;

import java.io.Serializable;

/**
 * 
 * rappresenta un fatto importante da punto di vista giuridico conferma codice
 * OTP, autorizzazione privacy, conferma richiesta contatto commerciale la
 * struttura dati prevede la conferma successiva dellevento probante
 * 
 * @author andrea
 *
 */
public interface Probe extends MemoryFact {
	public static enum ProbeType {
		OTP_SMS, CHANNEL_SMS, OTP_MAIL, CHANNEL_MAIL, CHANNEL_FACEBOOK, OTP_REST_AUTH
	}

	public Serializable legalProbe = null;

	public Serializable getLegalProbe();

	public void setLegalProbe(Serializable legalProbe);

	public String getProbeTitle();

	public void setProbeTitle(String probeTitle);

	public String getProbeDescription();

	public void setProbeDescription(String probeDescription);

	public String getProbeFullContract();

	public void setProbeFullContract(String probeFullContract);

	public boolean isChecked();

	public void setChecked(boolean checked);

	public boolean isToCheck();

	public void setToCheck(boolean toCheck);

	public Serializable getConfirmCode();

	public void setConfirmCode(Serializable confirmCode);
}
