package org.ar4k.agent.core.kettle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.RepositoryPluginType;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.RepositoriesMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryElementMetaInterface;
import org.pentaho.di.repository.RepositoryMeta;
import org.pentaho.di.repository.filerep.KettleFileRepositoryMeta;

// TODO servizio che mette a disposizione della console job da repository pre configurati (da zip file). Utilizzare l'interfaccia ManagedArchive per implementare i file systems

public class Kettle {

  // ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine()

  private Repository repository = null;
  private RepositoryDirectoryInterface directory = null;
  private Set<Job> inLavorazione = new HashSet<Job>();
  private String repoConfFile;

  private Object repoFs;
  private String repositoryTxt;
  private String baseDirectory;
  private String username;
  private String password;
  private String directoryFind;

  public String getRepoConfFile() {
    return repoConfFile;
  }

  public void setRepoConfFile(String repoConfFile) {
    this.repoConfFile = repoConfFile;
  }

  public Object getRepoFs() {
    return repoFs;
  }

  public void setRepoFs(Object repoFs) {
    this.repoFs = repoFs;
  }

  public String getRepositoryTxt() {
    return repositoryTxt;
  }

  public void setRepositoryTxt(String repositoryTxt) {
    this.repositoryTxt = repositoryTxt;
  }

  public String getBaseDirectory() {
    return baseDirectory;
  }

  public void setBaseDirectory(String baseDirectory) {
    this.baseDirectory = baseDirectory;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDirectoryFind() {
    return directoryFind;
  }

  public void setDirectoryFind(String directoryFind) {
    this.directoryFind = directoryFind;
  }

  // Inizializzazione Kettle
  public void initKettle() throws KettleException, FileNotFoundException {
    // LogChannelInterface log = new LogChannel(STRING_LOG);
    KettleEnvironment.init();
    repoInit();
  }

  private void repoInit() throws FileNotFoundException, KettleException {
    RepositoriesMeta repositoriesMeta = new RepositoriesMeta();
    if (repoConfFile != "") {
      InputStream xmlFile;
      xmlFile = new FileInputStream(new File(repoConfFile));
      repositoriesMeta.readDataFromInputStream(xmlFile);
    } else {
      repositoriesMeta.readData();
    }
    RepositoryMeta repositoryMeta = null;
    if (repoFs.equals(true)) {
      repositoryMeta = (KettleFileRepositoryMeta) repositoriesMeta.findRepository(repositoryTxt);
      ((KettleFileRepositoryMeta) repositoryMeta).setBaseDirectory(baseDirectory);
    } else {
      repositoryMeta = (RepositoryMeta) repositoriesMeta.findRepository(repositoryTxt);
    }
    PluginRegistry registry = PluginRegistry.getInstance();
    repository = registry.loadClass(RepositoryPluginType.class, repositoryMeta, Repository.class);
    repository.init(repositoryMeta);
    repository.connect(username, password);
    directory = repository.loadRepositoryDirectoryTree();
    directory = directory.findDirectory(directoryFind);
  }

  private void verificaRipristina() throws FileNotFoundException, KettleException {
    try {
      repository.getJobAndTransformationObjects(directory.getObjectId(), false);
    } catch (Exception e) {
      System.out.println("Repository sconnesso, provo la riconnesione");
      repoInit();
      System.out.println("Repository connesso");
    }
  }

  // Metodo per ottenere la lista dei job eseguibili
  public Collection<RepositoryElementMetaInterface> listJobs() throws KettleException, FileNotFoundException {
    verificaRipristina();
    List<RepositoryElementMetaInterface> lavorazioni = repository
        .getJobAndTransformationObjects(directory.getObjectId(), false);
    List<RepositoryElementMetaInterface> risultati = new ArrayList<RepositoryElementMetaInterface>();
    for (RepositoryElementMetaInterface lavoro : lavorazioni) {
      if (lavoro.getObjectType().getTypeDescription().equals("job")) {
        // println "Trovato: "+lavoro.getName()+" , tipo:
        // "+lavoro.getObjectType().getTypeDescription()
        risultati.add(lavoro);
      }
    }
    /*
     * for ( RepositoryElementMetaInterface lavoro in lavorazioni ) { if (
     * lavoro.getObjectType().getTypeDescription() == 'job' ) { JobMeta jobMeta =
     * new JobMeta(); jobMeta = repository.loadJob(lavoro.getName(), directory,
     * null, null); Job job = new Job(repository, jobMeta); risultati.add(lavoro) }
     * }
     */
    return risultati;
  }

  // Lista job in memoria del service per nome Job
  public Collection<Job> listJobsActive(String jobName) throws FileNotFoundException, KettleException {
    verificaRipristina();
    Set<Job> risultati = new HashSet<Job>();
    for (Job lavoro : inLavorazione) {
      if (lavoro.getJobname() == jobName) {
        risultati.add(lavoro);
        // Più avanti verificare se distruggere l'oggetto lavoro
      }
    }
    return risultati;
  }

  // Mette in pausa un Job
  public Job pausa(long jobID) throws FileNotFoundException, KettleException {
    verificaRipristina();
    Job risultato = null;
    for (Job lavoro : inLavorazione) {
      // println lavoro.getId().toString()+" == "+jobID
      if (lavoro.getId() == jobID) {
        risultato = lavoro;
        lavoro.stopAll();
        // Più avanti verificare se distruggere l'oggetto lavoro
      }
    }
    return risultato;
  }

  // ripristina un Job
  public Job ripristina(long jobID) throws FileNotFoundException, KettleException {
    verificaRipristina();
    Job risultato = null;
    for (Job lavoro : inLavorazione) {
      // println lavoro.getId().toString()+" == "+jobID
      if (lavoro.getId() == jobID) {
        risultato = lavoro;
        lavoro.run();
        // Più avanti verificare se distruggere l'oggetto lavoro
      }
    }
    return risultato;
  }

  // elimina un job
  public Job deleteJob(long jobID) throws FileNotFoundException, KettleException {
    verificaRipristina();
    Job risultato = null;
    for (Job lavoro : inLavorazione) {
      // println lavoro.getId().toString()+" == "+jobID
      if (lavoro.getId() == jobID) {
        risultato = lavoro;
        lavoro.stopAll();
        // Più avanti verificare se distruggere l'oggetto lavoro
      }
      Set<Job> temporanei = new HashSet<Job>();
      for (Job job : inLavorazione) {
        if (job.getId() != jobID) {
          temporanei.add(job);
        }
      }
      inLavorazione = temporanei;
    }
    return risultato;
  }

  // Metodo per eseguire i jobs. In Config.groovy ci sono i parametri da
  // configurare
  // Da verificare la configurazione esterna
  public Result runJob(String jobName) throws KettleException, FileNotFoundException {
    verificaRipristina();
    JobMeta jobMeta = new JobMeta();
    jobMeta = repository.loadJob(jobName, directory, null, null);
    // println
    // "-----------------------------------------------------------------------"
    // println "JobMeta Descrizione: " + jobMeta.getDescription();
    // println "JobMeta Versione: " + jobMeta.getJobversion();
    // println "JobMeta Data di modifica: " + jobMeta.getModifiedDate();
    // println "JobMeta Id: " + jobMeta.getObjectId().getId();
    Job job = new Job(repository, jobMeta);
    System.out.println("Job Nome: " + job.getJobname());
    inLavorazione.add(job);
    job.start();
    job.waitUntilFinished();
    // println
    // "-----------------------------------------------------------------------"
    if (job.getErrors() != 0) {
      return null;
    } else {
      return job.getResult();
    }
  }

  public Job getJob(String jobName) throws KettleException, FileNotFoundException {
    verificaRipristina();
    JobMeta jobMeta = new JobMeta();
    jobMeta = repository.loadJob(jobName, directory, null, null);
    // println
    // "-----------------------------------------------------------------------"
    // println "JobMeta Descrizione: " + jobMeta.getDescription();
    // println "JobMeta Versione: " + jobMeta.getJobversion();
    // println "JobMeta Data di modifica: " + jobMeta.getModifiedDate();
    // println "JobMeta Id: " + jobMeta.getObjectId().getId();
    Job job = new Job(repository, jobMeta);
    System.out.println("Job Nome: " + job.getJobname());
    return job;
  }

  public Job runJobFull(String jobName, Map<String, String> parametri) throws KettleException, FileNotFoundException {
    verificaRipristina();
    JobMeta jobMeta = new JobMeta();
    jobMeta = repository.loadJob(jobName, directory, null, null);
    for (String parametro : jobMeta.listParameters()) {
      jobMeta.setParameterValue(parametro, parametri.get(parametro));
      System.out.println(parametro + " = " + parametri.get(parametro));
    }
    // println
    // "-----------------------------------------------------------------------"
    // println "JobMeta Descrizione: " + jobMeta.getDescription();
    // println "JobMeta Versione: " + jobMeta.getJobversion();
    // println "JobMeta Data di modifica: " + jobMeta.getModifiedDate();
    // println "JobMeta Id: " + jobMeta.getObjectId().getId();
    Job job = new Job(repository, jobMeta);
    System.out.println("Job Nome: " + job.getJobname());
    inLavorazione.add(job);
    job.start();
    // println
    // "-----------------------------------------------------------------------"
    return job;
  }

}
