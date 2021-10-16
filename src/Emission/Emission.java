package Emission;

import java.io.FileNotFoundException;

public interface Emission {
    Boolean tryEmit() throws FileNotFoundException;
    Integer total() throws FileNotFoundException;
    Double chance() throws FileNotFoundException;
}
