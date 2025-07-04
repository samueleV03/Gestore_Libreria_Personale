package filtri;

import builder.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltroPerStato implements Filtro{

    private Libro.Stato stato;

    public FiltroPerStato(Libro.Stato stato) {
        this.stato = stato;
    }

    @Override
    public List<Libro> filtra(List<Libro> libri) {
        List<Libro> filtrati = new ArrayList<>();
        for (Libro libro : libri) {
            if (libro.getStatoLettura().equals(stato)) {
                filtrati.add(libro);
            }
        }
        return filtrati;
    }
}
