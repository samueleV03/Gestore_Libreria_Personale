package strategy;

import builder.Libro;

import java.util.Comparator;
import java.util.List;

public class OrdinaPerValutazione implements OrdinaLibreria {
    @Override
    public void ordina(List<Libro> libri) {
        Comparator<Libro> comparator = new Comparator<Libro>() {
            @Override
            public int compare(Libro l1, Libro l2) {
                return Integer.compare(l1.getValutazione(), l2.getValutazione());
            }
        };
        libri.sort(comparator.reversed()); //per ordinarlo da valutazione più alta a più bassa
    }

}
