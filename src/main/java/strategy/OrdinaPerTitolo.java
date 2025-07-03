package strategy;

import builder.Libro;

import java.util.Comparator;
import java.util.List;

public class OrdinaPerTitolo implements OrdinaLibreria{
    @Override
    public void ordina(List<Libro> libri) {
        Comparator<Libro> comparator = new Comparator<Libro>() {
            @Override
            public int compare(Libro l1, Libro l2) {
                return l1.getTitolo().compareTo(l2.getTitolo());
            }
        };
        libri.sort(comparator);
    }
}
