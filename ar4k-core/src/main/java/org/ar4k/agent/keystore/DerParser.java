package org.ar4k.agent.keystore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public class DerParser {

	// Classes
	public final static int UNIVERSAL = 0x00;
	public final static int APPLICATION = 0x40;
	public final static int CONTEXT = 0x80;
	public final static int PRIVATE = 0xC0;

	// Constructed Flag
	public final static int CONSTRUCTED = 0x20;

	// Tag and data types
	public final static int ANY = 0x00;
	public final static int BOOLEAN = 0x01;
	public final static int INTEGER = 0x02;
	public final static int BIT_STRING = 0x03;
	public final static int OCTET_STRING = 0x04;
	public final static int NULL = 0x05;
	public final static int OBJECT_IDENTIFIER = 0x06;
	public final static int REAL = 0x09;
	public final static int ENUMERATED = 0x0a;
	public final static int RELATIVE_OID = 0x0d;

	public final static int SEQUENCE = 0x10;
	public final static int SET = 0x11;

	public final static int NUMERIC_STRING = 0x12;
	public final static int PRINTABLE_STRING = 0x13;
	public final static int T61_STRING = 0x14;
	public final static int VIDEOTEX_STRING = 0x15;
	public final static int IA5_STRING = 0x16;
	public final static int GRAPHIC_STRING = 0x19;
	public final static int ISO646_STRING = 0x1A;
	public final static int GENERAL_STRING = 0x1B;

	public final static int UTF8_STRING = 0x0C;
	public final static int UNIVERSAL_STRING = 0x1C;
	public final static int BMP_STRING = 0x1E;

	public final static int UTC_TIME = 0x17;
	public final static int GENERALIZED_TIME = 0x18;

	protected InputStream in;

	public DerParser(InputStream in) throws IOException {
		this.in = in;
	}

	public DerParser(byte[] bytes) throws IOException {
		this(new ByteArrayInputStream(bytes));
	}

	public Asn1Object read() throws IOException {
		int tag = in.read();

		if (tag == -1)
			throw new IOException("Invalid DER: stream too short, missing tag"); //$NON-NLS-1$

		int length = getLength();

		byte[] value = new byte[length];
		int n = in.read(value);
		if (n < length)
			throw new IOException("Invalid DER: stream too short, missing value"); //$NON-NLS-1$

		Asn1Object o = new Asn1Object(tag, length, value);

		return o;
	}

	private int getLength() throws IOException {

		int i = in.read();
		if (i == -1)
			throw new IOException("Invalid DER: length missing"); //$NON-NLS-1$

		// A single byte short length
		if ((i & ~0x7F) == 0)
			return i;

		int num = i & 0x7F;

		// We can't handle length longer than 4 bytes
		if (i >= 0xFF || num > 4)
			throw new IOException("Invalid DER: length field too big (" //$NON-NLS-1$
					+ i + ")"); //$NON-NLS-1$

		byte[] bytes = new byte[num];
		int n = in.read(bytes);
		if (n < num)
			throw new IOException("Invalid DER: length too short"); //$NON-NLS-1$

		return new BigInteger(1, bytes).intValue();
	}

}

class Asn1Object {

	protected final int type;
	protected final int length;
	protected final byte[] value;
	protected final int tag;

	public Asn1Object(int tag, int length, byte[] value) {
		this.tag = tag;
		this.type = tag & 0x1F;
		this.length = length;
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public int getLength() {
		return length;
	}

	public byte[] getValue() {
		return value;
	}

	public boolean isConstructed() {
		return (tag & DerParser.CONSTRUCTED) == DerParser.CONSTRUCTED;
	}

	public DerParser getParser() throws IOException {
		if (!isConstructed())
			throw new IOException("Invalid DER: can't parse primitive entity"); //$NON-NLS-1$

		return new DerParser(value);
	}

	public BigInteger getInteger() throws IOException {
		if (type != DerParser.INTEGER)
			throw new IOException("Invalid DER: object is not integer"); //$NON-NLS-1$

		return new BigInteger(value);
	}

	public String getString() throws IOException {

		String encoding;

		switch (type) {

		// Not all are Latin-1 but it's the closest thing
		case DerParser.NUMERIC_STRING:
		case DerParser.PRINTABLE_STRING:
		case DerParser.VIDEOTEX_STRING:
		case DerParser.IA5_STRING:
		case DerParser.GRAPHIC_STRING:
		case DerParser.ISO646_STRING:
		case DerParser.GENERAL_STRING:
			encoding = "ISO-8859-1"; //$NON-NLS-1$
			break;

		case DerParser.BMP_STRING:
			encoding = "UTF-16BE"; //$NON-NLS-1$
			break;

		case DerParser.UTF8_STRING:
			encoding = "UTF-8"; //$NON-NLS-1$
			break;

		case DerParser.UNIVERSAL_STRING:
			throw new IOException("Invalid DER: can't handle UCS-4 string"); //$NON-NLS-1$

		default:
			throw new IOException("Invalid DER: object is not a string"); //$NON-NLS-1$
		}

		return new String(value, encoding);
	}
}