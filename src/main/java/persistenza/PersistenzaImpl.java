package persistenza;

import builder.Libro;

import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;


public class PersistenzaImpl implements LibreriaPersistente{
    private static final String FILE_PATH = "libreria.json";
    private final Gson gson = new Gson();
    @Override
    public void salvaLibro(Libro libro) throws IOException {

    }

    @Override
    public void rimuoviLibro(Libro libro) throws IOException {

    }

    @Override
    public void salvaLibri(List<Libro> libri) throws IOException {

    }

    @Override
    public List<Libro> ottieniLibri() throws IOException {
        return List.of();
    }
}
