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
package org.ar4k.agent.helper;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/*
 * 
 * @author Andrea Ambrosini
 *
 *         Classe helper per la gestione delle password OTP. Lo stesso algoritmo
 *         è implementato in Javascript nella pagina in
 *         ui/assets/base/html/otp.html
 */
class OtpHelper {

  private OtpHelper() {
    System.out.println("just for static methods");
  }

  public static boolean verificaOTP(String seedOTP, String otpCode, int finestraOTP) {
    boolean ritorno = false;

    List<String> otpValidi = new ArrayList<String>();
    long millisecondi = new java.util.Date().getTime();
    long fine = millisecondi + (Long.valueOf(finestraOTP * 500));
    long contatore = millisecondi - (Long.valueOf(finestraOTP * 500));
    while (contatore <= fine) {
      otpValidi.add(generateTOTP512(seedOTP, (String.valueOf(contatore)), "8"));
      contatore++;
    }
    if (otpValidi.contains(otpCode)) {

      ritorno = true;
    } else {

      ritorno = false;
    }
    return ritorno;
  }

  private final static int[] DIGITS_POWER = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000 };

  private static byte[] hmac_sha(String crypto, byte[] keyBytes, byte[] text) {
    try {
      Mac hmac;
      hmac = Mac.getInstance(crypto);
      SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
      hmac.init(macKey);
      return hmac.doFinal(text);
    } catch (GeneralSecurityException gse) {
      throw new UndeclaredThrowableException(gse);
    }
  }

  private static byte[] hexStr2Bytes(String hex) {
    byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();
    byte[] ret = new byte[bArray.length - 1];
    for (int i = 0; i < ret.length; i++)
      ret[i] = bArray[i + 1];
    return ret;
  }

  public static String generateTOTP(String key, String time, String returnDigits) {
    return generateTOTP(key, time, returnDigits, "HmacSHA1");
  }

  public static String generateTOTP256(String key, String time, String returnDigits) {
    return generateTOTP(key, time, returnDigits, "HmacSHA256");
  }

  public static String generateTOTP512(String key, String time, String returnDigits) {
    return generateTOTP(key, time, returnDigits, "HmacSHA512");
  }

  public static String generateTOTP(String key, String time, String returnDigits, String crypto) {
    int codeDigits = Integer.decode(returnDigits).intValue();
    String result = null;

    while (time.length() < 16) {
      time = "0" + time;
    }

    byte[] msg = hexStr2Bytes(time);
    byte[] k = hexStr2Bytes(key);

    byte[] hash = hmac_sha(crypto, k, msg);

    int offset = hash[hash.length - 1] & 0xf;

    int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8)
        | (hash[offset + 3] & 0xff);

    int otp = binary % DIGITS_POWER[codeDigits];

    result = Integer.toString(otp);
    while (result.length() < codeDigits) {
      result = "0" + result;
    }
    // logger.trace("Generazione password OTP: "+result+" (k: "+key+" t: "+time+"
    // bk: "+k.toString()+" bt: "+msg.toString()+" bh: "+hash.toString())
    return result;
  }

  public static String getRandomHexString(int numchars) {
    Random r = new Random();
    StringBuffer sb = new StringBuffer();
    while (sb.length() < numchars) {
      sb.append(Integer.toHexString(r.nextInt()));
    }
    return sb.toString().substring(0, numchars);
  }
}
