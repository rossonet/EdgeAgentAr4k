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
package org.ar4k.gw.studio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import org.ar4k.agent.cortex.opennlp.ItalianModel;
import org.ar4k.agent.cortex.opennlp.ItalianVerbConjugation;
import org.ar4k.agent.cortex.opennlp.POSUtils;
import org.ar4k.agent.cortex.opennlp.ItalianVerbConjugation.ConjugationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.util.ResourceUtils;

import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorFactory;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;

public class OpenNlp {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		im = new ItalianModel();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void starting(Description description) {
			System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
		}
	};

	@Test
	public void testSentencesSplit() {
		SentenceDetectorME sentenceDetector = new SentenceDetectorME(SentenceModelMaker());
		String sentences[] = sentenceDetector.sentDetect(
				"  È docente di filosofia e direttore del Centro per gli studi cognitivi dell'Università Tufts. Ha frequentato l'Università di Harvard e quella di Oxford, dove è stato allievo di Gilbert Ryle e Willard Van Orman Quine, e ha insegnato all'Università della California, a Pittsburgh, a Oxford e alla École Normale Supérieure di Parigi. Nelle sue ultime ricerche si occupa di coscienza, filosofia della mente e intelligenza artificiale, ed è noto per aver creato il concetto di sistema intenzionale, oltre che per i suoi contributi alle fondamenta concettuali della biologia evolutiva, attraverso i quali ha avallato le tesi di Richard Dawkins . Come quest'ultimo, Dennett è ateo. ");
		int count = 0;
		for (String frase : sentences) {
			System.out.println(count++ + " - " + frase);
		}
	}

	private SentenceModel SentenceModelMaker() {
		InputStream modelIn = null;
		SentenceModel model = null;
		try {
			File file = ResourceUtils.getFile("classpath:opennlp/opennlp-italian-models-master/models/it/it-sent.bin");
			assertTrue(file.exists());
			modelIn = new FileInputStream(file);
			model = new SentenceModel(modelIn);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		}
		return model;
	}

	private static String generateTrainText() {
		final String lineSeparator = System.lineSeparator();
		StringBuilder sb = new StringBuilder();
		for (String space : Arrays.asList(" ", "\t")) {
			for (String end : Arrays.asList(".", "!", "?", "...")) {
				for (String trainSentence : Arrays.asList("Questa è una frase", "Seconda frase",
						"Questa è una frase aggiuntiva.")) {
					sb.append(trainSentence).append(end);
					sb.append(lineSeparator);
					sb.append(space).append(trainSentence).append(end);
					sb.append(lineSeparator);
					sb.append(space).append(trainSentence).append(end).append(space);
					sb.append(lineSeparator);
				}
			}
		}
		return sb.toString();
	}

	private static SentenceModel train(final String trainText) throws IOException {
		try (ObjectStream<String> lineStream = new PlainTextByLineStream(
				() -> new ByteArrayInputStream(trainText.getBytes()), Charset.forName("UTF-8"));
				ObjectStream<SentenceSample> sampleStream = new SentenceSampleStream(lineStream)) {
			SentenceDetectorFactory sdFactory = new SentenceDetectorFactory("en", true, null, null);
			return SentenceDetectorME.train("en", sampleStream, sdFactory, TrainingParameters.defaultParams());
		}
	}

	private static String getSampleText() {
		return "Questa è una frase. " + "Sto scrivendo il codice " + "E se questo fosse un esempio? " + "certo!";
	}

	@Test
	public void testTrainSentenceModel() throws IOException {
		SentenceModel sentenceModel = train(generateTrainText());
		SentenceDetector sentenceDetector = new SentenceDetectorME(sentenceModel);
		String[] sentences = sentenceDetector.sentDetect(getSampleText());
		System.out.println("Detected sentences (" + sentences.length + "):");
		for (String sentence : sentences) {
			System.out.println(sentence);
		}
	}

	private ItalianModel im;

	@Test
	public void testConjugationOfIrregularVerb() throws ConjugationException {
		// andavamo,andare,indicative imperfect,p,1
		Set<ItalianVerbConjugation> vAndavos = im.getVerbs("cuocesti");
		assertTrue("one and only one verb form must correspond to 'cuocesti'", vAndavos.size() == 1);
		// forcing recognition will return the same result since the verb is already in
		// the database
		vAndavos = im.getVerbs("cuocesti", true);
		assertTrue("one and only one verb form must correspond to 'cuocesti'", vAndavos.size() == 1);
		ItalianVerbConjugation cuocesti = vAndavos.iterator().next();
		assertEquals("the infinitive of 'cuocesti' is 'cuocere'", cuocesti.getInfinitive(), "cuocere");
		assertEquals("the form of 'cuocesti' is 'indicative past historic'", cuocesti.getMode(),
				"indicative past historic");
		assertEquals("the person of 'cuocesti' is 's,2'", cuocesti.getPerson(), 2);
		assertEquals("the person of 'cuocesti' is 's,2'", cuocesti.getNumber(), 's');

		cuocesti.setMode("indicative present");
		cuocesti.setNumber('p');
		cuocesti.setPerson(3);
		assertEquals("the 3-person plural indicative present is 'cuociono'", cuocesti.getConjugated(), "cuociono");
	}

	@Test(expected = ConjugationException.class)
	public void testConjugationException() throws ConjugationException {
		ItalianVerbConjugation essere = im.getVerbs("erano").iterator().next();
		assertEquals("'erano' is a form of 'essere'", essere.getInfinitive(), "essere");
		essere.setMode("imperative");
		essere.setPerson(1);
		essere.setNumber('s');
		essere.getConjugated();
	}

	@Test
	public void testTokenizer() {
		String stmt = "il tuo cane mangiò la mela che ti avevo lasciato, e questo mi farebbe arrabbiare molto se non sapessi che non l'ha fatto apposta";
		String[] tokens = im.tokenize(stmt);

		// tokens presence
		for (String t : tokens)
			assertTrue("the tokenizer gave a token not actually in the statement. Token:" + t + " statement:'" + stmt
					+ "'", stmt.contains(t));

		// tokens order
		int lastPos = -1;
		for (String t : tokens) {
			assertTrue("tokens order has changed!", stmt.indexOf(t, lastPos) != -1);
			lastPos = stmt.indexOf(t, lastPos);
		}
	}

	public void printItalianModel()
			throws ClassNotFoundException, SQLException, FileNotFoundException, ConjugationException {
		ItalianModel im = new ItalianModel();
		// showcase of functions
		ItalianVerbConjugation essere = new ItalianVerbConjugation(im);
		essere.setInfinitive("essere");
		essere.setMode("indicative present");
		essere.setNumber('s');
		essere.setPerson(3);

		System.out.println("lui " + essere.getConjugated());
		// String stmt="Lo ha detto il premier Matteo Renzi al termine del vertice Ue, a
		// Bruxelles, un incontro che nonostante tutte le incognite lo lascia
		// soddisfatto: 'Torniamo dall'Europa avendo vinto una battaglia di metodo e di
		// sostanza', dice Renzi."
		// + "Il mio numero di telefono personale è +39 0268680762836 e non +39 5868
		// 6867 2439";
		String stmt = "io vado in calabria al mare";
		System.err.println(stmt);
		String[] tokens = Span.spansToStrings(im.getTokens(stmt), stmt);
		int p = 0;
		for (String t : im.quickPOSTag(stmt)) {
			System.out.println(tokens[p] + " " + t + ":" + POSUtils.getDescription(t));
			p++;
		}
		System.out.println("-----");
		for (String t : tokens)
			System.out.println(t + ":" + Arrays.deepToString(im.getPoSvalues(t)));
		String[] verbi = { "andavamo", "mangerò", "volare", "correre", "puffavo", "googlare" };
		HashMap<String, String> people = new HashMap<>(6);
		people.put("io", "1s");
		people.put("tu", "2s");
		people.put("lui", "3s");

		people.put("noi", "1p");
		people.put("voi", "2p");
		people.put("loro", "3p");

		for (String verbo : verbi) {
			System.out.println("---");
			for (ItalianVerbConjugation v : im.getVerbs(verbo, true)) {
				for (Entry<String, String> pers : people.entrySet()) {
					System.out.println(v);
					v.setPerson(Integer.parseInt(pers.getValue().substring(0, 1)));
					v.setNumber(pers.getValue().charAt(1));
					v.setMode("subjunctive imperfect");
					try {
						System.out.println("conjugation: che " + pers.getKey() + " " + v.getConjugated());
					} catch (ConjugationException e) {
						e.printStackTrace();
					}
				}
			}
		}
		Set<String> forms = im.getAllKnownInfinitiveVerbs();
		System.out.println("There are " + forms.size() + " infinitive verbs in the database");
		System.out.println("Starting to count irregular forms in 10 seconds...");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			// doesn't really matter...
			// logger.error(e1);
		}
		ItalianVerbConjugation fakeVerb = new ItalianVerbConjugation(im);
		int notInDB = 0, corresponding = 0, irregular = 0;
		HashMap<String, Integer> errors = new HashMap<>(200);
		for (String v : forms) {
			if (!v.endsWith("are") && !v.endsWith("ere") && !v.endsWith("ire"))
				continue;
			ItalianVerbConjugation current = new ItalianVerbConjugation(im);
			current.setInfinitive(v);
			fakeVerb.setInfinitive("eee" + v);
			// now there are two verbs, one in the database (for at least one form) and the
			// other not in it

			for (String mode : ItalianVerbConjugation.getImpersonalModes()) {
				current.setMode(mode);
				fakeVerb.setMode(mode);
				String conjugated;
				try {
					conjugated = current.getConjugated(false);
				} catch (ConjugationException e) {
					notInDB++;
					continue;
				}
				try {
					if (fakeVerb.getConjugated(true).substring(3).equals(conjugated))
						corresponding++;
					else {
						System.out.println("irregular form of " + v + " --> " + conjugated + " ["
								+ fakeVerb.getConjugated(true).substring(3) + "] " + mode);
						irregular++;
					}
				} catch (ConjugationException e) {
					// should never happen
					e.printStackTrace();
				}

			}
			for (String mode : ItalianVerbConjugation.getPersonalModes()) {
				for (int person : new Integer[] { 1, 2, 3 }) {
					for (char num : new Character[] { 's', 'p' }) {
						String conjugated;
						if (mode.equals("imperative") && person == 1 && num == 's')
							continue;
						current.setMode(mode);
						current.setNumber(num);
						current.setPerson(person);

						fakeVerb.setMode(mode);
						fakeVerb.setNumber(num);
						fakeVerb.setPerson(person);
						try {
							conjugated = current.getConjugated(false);
						} catch (ConjugationException e) {
							notInDB++;
							continue;
						}
						try {
							if (fakeVerb.getConjugated(true).substring(3).equals(conjugated))
								corresponding++;
							else {
								System.out.println("irregular form of " + v + " --> " + conjugated + " ["
										+ fakeVerb.getConjugated(true).substring(3) + "] " + mode + " " + person + " "
										+ num);
								errors.put(v.substring(v.length() - 3) + " " + mode + " " + person + " " + num,
										1 + errors.getOrDefault(
												v.substring(v.length() - 3) + " " + mode + " " + person + " " + num,
												0));
								irregular++;
							}
						} catch (ConjugationException e) {
							// should never happen
							e.printStackTrace();
						}
					}
				}
			}
			System.out.println(v + " " + notInDB + " unknown, " + corresponding + " corresponding, " + irregular
					+ " irregular verbs so far (" + 100.0 * irregular / (irregular + corresponding) + "%)");
		}
		System.out.println("--------------\nirregular verb cases:\n");
		errors.entrySet().forEach(kv -> System.out.println(kv.getKey() + "\t\t" + kv.getValue()));

	}

}
