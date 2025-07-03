package strategy;

import builder.Libro;

import java.util.Comparator;
import java.util.List;

public class OrdinaPerAutore implements OrdinaLibreria{
    @Override
    public void ordina(List<Libro> libri) {
        Comparator<Libro> comparator = new Comparator<Libro>() {
            @Override
            public int compare(Libro l1, Libro l2) {
                return l1.getAutore().compareTo(l2.getAutore());
            }
        };
        libri.sort(comparator);
    }
}
