package strategy;

import builder.Libro;

import java.util.List;
//l'interfaccia andrà implementata in ogni classe responsabile del suo specifico ordinamento
public interface OrdinaLibreria {
    public void ordina(List<Libro> libri);
}
