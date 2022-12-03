package advent.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileManager {

  public void processLines(String fileName, LineProcessor<?> lp) {

    InputStream ioStream = this.getClass().getClassLoader().getResourceAsStream(fileName);

    if (ioStream == null) {
      throw new IllegalArgumentException(fileName + " is not found");
    }

    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(ioStream));
      String line;
      while ((line = br.readLine()) != null) {
        lp.processLine(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
