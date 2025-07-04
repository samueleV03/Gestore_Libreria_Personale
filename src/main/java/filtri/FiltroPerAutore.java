package filtri;

import builder.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltroPerAutore implements Filtro{

    private String autore;

    public FiltroPerAutore(String autore) {
        this.autore = autore;
    }

    @Override
    public List<Libro> filtra(List<Libro> libri) {
        List<Libro> filtrati = new ArrayList<>();
        for (Libro libro : libri) {
            if (libro.getAutore().equalsIgnoreCase(autore)) {
                filtrati.add(libro);
            }
        }
        return filtrati;
    }
}
