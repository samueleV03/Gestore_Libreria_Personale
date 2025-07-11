package decorator;

import builder.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltroStatoDecorator extends Decorator {

    private Libro.Stato stato;
    public FiltroStatoDecorator(FiltroDecorator filtro, Libro.Stato stato) {
        super(filtro);
        this.stato = stato;
    }

    @Override
    public List<Libro> filtra(List<Libro> libri)
    {
        List<Libro> precedenti = super.filtra(libri); //dobbiamo applicare i filtri precedenti
        List<Libro> filtro = filtraPerStato(precedenti);
        return filtro;
    }

    private List<Libro> filtraPerStato(List<Libro> libri)
    {
        List<Libro> filtrati = new ArrayList<>();
        for (Libro libro : libri) {
            if (libro.getStatoLettura().equals(stato)) {
                filtrati.add(libro);
            }
        }
        return filtrati;
    }
}
