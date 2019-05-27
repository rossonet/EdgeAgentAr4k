package org.ar4k.agent.config.json;

import java.io.IOException;

import org.ar4k.agent.config.ServiceConfig;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class SshLocalConfigJsonAdapter extends TypeAdapter<ServiceConfig> {

  @Override
  public void write(JsonWriter out, ServiceConfig value) throws IOException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public ServiceConfig read(JsonReader in) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

}
