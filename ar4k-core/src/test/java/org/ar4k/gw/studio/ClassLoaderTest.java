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

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.ar4k.gw.studio.jarLoader.JarClassLoader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

/*
 * Studio classloader e annotation lib
 */
@Ignore
public class ClassLoaderTest {

  // private Logger logger = Logger.getLogger(ClassLoaderTest.class);

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }

  };

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    try {
      ClassLoader cl = addFile("/home/andrea/git/opc-ua/build/libs/app.jar");
      for (ClassInfo c : ClassPath.from(cl).getAllClasses()) {
        // System.out.println(c.getName());
        Annotation annotazioni[] = new Annotation[0];
        try {
          annotazioni = c.load().getAnnotations();
        } catch (NoClassDefFoundError | IncompatibleClassChangeError | SecurityException | VerifyError e) {
          // System.out.println(e.getMessage());
        }
        for (Annotation a : annotazioni) {
          System.out.println(c.getName() + " -> " + a.toString());
        }
      }
      ((JarClassLoader) cl).invokeClass(((JarClassLoader) cl).getMainClassName(), new String[0]);
      // System.out.print(instance.toString());
      Thread.sleep(60000L);
    } catch (InterruptedException | IOException | ClassNotFoundException | NoSuchMethodException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  private JarClassLoader addFile(String s) throws IOException {
    File f = new File(s);
    return addFile(f);
  }

  private JarClassLoader addFile(File f) throws IOException {
    return addURL(f.toURI().toURL());
  }

  private JarClassLoader addURL(URL u) throws IOException {
    try {
      JarClassLoader sysloader = new JarClassLoader(u);
      System.out.print("MAIN: " + sysloader.getMainClassName());
      return sysloader;
    } catch (Throwable t) {
      t.printStackTrace();
      throw new IOException("Error, could not add URL to system classloader");
    }
  }

  /**
   * Scans all classes accessible from the context class loader which belong to
   * the given package and subpackages.
   *
   * @param packageName The base package
   * @return The classes
   * @throws ClassNotFoundException
   * @throws IOException
   */
  @SuppressWarnings("unused")
  private Class<?>[] getClasses(ClassLoader classLoader, String packageName)
      throws ClassNotFoundException, IOException {
    // ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    assert classLoader != null;
    String path = packageName.replace('.', '/');
    Enumeration<URL> resources = classLoader.getResources(path);
    List<File> dirs = new ArrayList<File>();
    while (resources.hasMoreElements()) {
      URL resource = (URL) resources.nextElement();
      dirs.add(new File(resource.getFile()));
    }
    ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
    for (File directory : dirs) {
      classes.addAll(findClasses(directory, packageName));
    }
    return (Class[]) classes.toArray(new Class[classes.size()]);
  }

  /**
   * Recursive method used to find all classes in a given directory and subdirs.
   *
   * @param directory   The base directory
   * @param packageName The package name for classes found inside the base
   *                    directory
   * @return The classes
   * @throws ClassNotFoundException
   */
  private List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
    List<Class<?>> classes = new ArrayList<Class<?>>();
    if (!directory.exists()) {
      return classes;
    }
    File[] files = directory.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        assert !file.getName().contains(".");
        classes.addAll(findClasses(file, packageName + "." + file.getName()));
      } else if (file.getName().endsWith(".class")) {
        classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
      }
    }
    return classes;
  }

}
