package org.ar4k.agent.industrial;

public class Enumerator {

	public enum SecurityMode {
		signAndEncrypt, sign, none
	}

	public enum AuthMode {
		password, certificate, none
	}

	public enum CryptoMode {
		Basic256Sha256, Basic256, Basic128Rsa15, Aes128_Sha256_RsaOaep, Aes256_Sha256_RsaPss, none
	}

	public enum DeadbandType {
		none, absolute, percent
	}

	public enum DataChangeTrigger {
		status, statusOrValue, statusOrValueOrTimestamp
	}

}
