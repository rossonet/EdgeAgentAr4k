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
package org.ar4k.gw.studio.weka;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import org.ar4k.agent.helper.IOUtils;
import org.junit.Test;

import com.google.common.io.CharSource;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Debug;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

public class WekaTest {

	private static final String TEST_MODEL_BIN = "test-model.bin";

	@Test
	public void testWekaLibrary() throws Exception {
		String data = IOUtils.readResourceFileToString("iris.2D.arff");
		Instances dataset = loadDataset(data);
		Filter filter = new Normalize();
		int trainSize = (int) Math.round(dataset.numInstances() * 0.8);
		int testSize = dataset.numInstances() - trainSize;
		dataset.randomize(new Debug.Random(1));
		filter.setInputFormat(dataset);
		Instances datasetnor = Filter.useFilter(dataset, filter);
		Instances traindataset = new Instances(datasetnor, 0, trainSize);
		Instances testdataset = new Instances(datasetnor, trainSize, testSize);
		MultilayerPerceptron ann = (MultilayerPerceptron) buildClassifier(traindataset);
		String evalsummary = evaluateModel(ann, traindataset, testdataset);
		System.out.println("Evaluation: " + evalsummary);
		saveModel(ann, TEST_MODEL_BIN);
		WekaModelClassifier cls = new WekaModelClassifier();
		String classname = cls.classifiy(Filter.useFilter(cls.createInstance(1.6, 0.2, 0), filter), TEST_MODEL_BIN);
		System.out.println(
				"\n The class name for the instance with petallength =1.6 and petalwidth =0.2 is  " + classname);
		assertEquals("Iris-setosa", classname);

	}

	private Instances loadDataset(String data) {
		Instances dataset = null;
		try {
			dataset = DataSource.read(CharSource.wrap(data).asByteSource(StandardCharsets.UTF_8).openStream());
			if (dataset.classIndex() == -1) {
				dataset.setClassIndex(dataset.numAttributes() - 1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return dataset;
	}

	private Classifier buildClassifier(Instances traindataset) {
		MultilayerPerceptron m = new MultilayerPerceptron();

		try {
			m.buildClassifier(traindataset);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return m;
	}

	private String evaluateModel(Classifier model, Instances traindataset, Instances testdataset) {
		Evaluation eval = null;
		try {
			// Evaluate classifier with test dataset
			eval = new Evaluation(traindataset);
			eval.evaluateModel(model, testdataset);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return eval.toSummaryString("", true);
	}

	private void saveModel(Classifier model, String modelpath) {

		try {
			SerializationHelper.write(modelpath, model);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
