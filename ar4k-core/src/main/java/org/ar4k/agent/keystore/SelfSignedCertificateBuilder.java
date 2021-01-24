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
package org.ar4k.agent.keystore;

import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 *
 * @author Andrea Ambrosini
 *
 *         Classe builder per la generazione di keystore self signed.
 *
 */
public class SelfSignedCertificateBuilder {

	/**
	 * Signature Algorithm for SHA1 with RSA.
	 * <p>
	 * SHA1 was broken in 2017 and this algorithm should not be used.
	 */
	// public static final String SA_SHA1_RSA = "SHA1withRSA";

	/**
	 * Signature Algorithm for SHA256 with RSA.
	 */
	// public static final String SA_SHA256_RSA = "SHA256withRSA";

	/**
	 * Signature Algorithm for SHA256 with ECDSA.
	 * <p>
	 * May only be uses with EC-based KeyPairs and security profiles.
	 */
//  public static final String SA_SHA256_ECDSA = "SHA256withECDSA";

	private Period validityPeriod = Period.ofYears(1);

	private String commonName = "";
	private String organization = "";
	private String organizationalUnit = "";
	private String localityName = "";
	private String stateName = "";
	private String countryCode = "";

	private String applicationUri = "";
	private List<String> dnsNames = new ArrayList<>();
	private List<String> ipAddresses = new ArrayList<>();
	private String signatureAlgorithm = KeystoreLoader.CIPHER;

	private final KeyPair keyPair;
	private final SelfSignedCertificateGenerator generator;

	private boolean isCa = false;

	public SelfSignedCertificateBuilder(KeyPair keyPair) {
		this(keyPair, new SelfSignedCertificateGenerator());
	}

	public SelfSignedCertificateBuilder(KeyPair keyPair, SelfSignedCertificateGenerator generator) {
		this.keyPair = keyPair;
		this.generator = generator;

		PublicKey publicKey = keyPair.getPublic();

		if (publicKey instanceof RSAPublicKey) {
			signatureAlgorithm = KeystoreLoader.CIPHER;

			int bitLength = ((RSAPublicKey) keyPair.getPublic()).getModulus().bitLength();

			if (bitLength <= 1024) {
				Logger logger = LoggerFactory.getLogger(getClass());
				logger.warn("Using legacy key size: {}", bitLength);
			}
		} else if (keyPair.getPublic() instanceof ECPublicKey) {
			signatureAlgorithm = KeystoreLoader.CIPHER;
		}
	}

	public SelfSignedCertificateBuilder setValidityPeriod(Period validityPeriod) {
		this.validityPeriod = validityPeriod;
		return this;
	}

	public SelfSignedCertificateBuilder isCa(boolean isCaThisCert) {
		this.isCa = isCaThisCert;
		return this;
	}

	public SelfSignedCertificateBuilder setCommonName(String commonName) {
		this.commonName = commonName;
		return this;
	}

	public SelfSignedCertificateBuilder setOrganization(String organization) {
		this.organization = organization;
		return this;
	}

	public SelfSignedCertificateBuilder setOrganizationalUnit(String organizationalUnit) {
		this.organizationalUnit = organizationalUnit;
		return this;
	}

	public SelfSignedCertificateBuilder setLocalityName(String localityName) {
		this.localityName = localityName;
		return this;
	}

	public SelfSignedCertificateBuilder setStateName(String stateName) {
		this.stateName = stateName;
		return this;
	}

	public SelfSignedCertificateBuilder setCountryCode(String countryCode) {
		this.countryCode = countryCode;
		return this;
	}

	public SelfSignedCertificateBuilder setApplicationUri(String applicationUri) {
		this.applicationUri = applicationUri;
		return this;
	}

	public SelfSignedCertificateBuilder addDnsName(String dnsName) {
		dnsNames.add(dnsName);
		return this;
	}

	public SelfSignedCertificateBuilder addIpAddress(String ipAddress) {
		ipAddresses.add(ipAddress);
		return this;
	}

	public SelfSignedCertificateBuilder setSignatureAlgorithm(String signatureAlgorithm) {
		this.signatureAlgorithm = signatureAlgorithm;
		return this;
	}

	public X509Certificate build() throws Exception {
		// Calculate start and end date based on validity period
		LocalDate now = LocalDate.now();
		LocalDate expiration = now.plus(validityPeriod);

		Date notBefore = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date notAfter = Date.from(expiration.atStartOfDay(ZoneId.systemDefault()).toInstant());

		return generator.generateSelfSigned(keyPair, notBefore, notAfter, commonName, organization, organizationalUnit,
				localityName, stateName, countryCode, applicationUri, dnsNames, ipAddresses, signatureAlgorithm, isCa);
	}

}
