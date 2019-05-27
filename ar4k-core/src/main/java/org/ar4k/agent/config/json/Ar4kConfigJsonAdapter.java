package org.ar4k.agent.config.json;

import java.io.IOException;

import org.ar4k.agent.config.Ar4kConfig;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class Ar4kConfigJsonAdapter extends TypeAdapter<Ar4kConfig> {

  @Override
  public void write(JsonWriter out, Ar4kConfig value) throws IOException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Ar4kConfig read(JsonReader in) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

}
