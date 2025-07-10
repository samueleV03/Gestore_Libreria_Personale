package mediator;

import javax.swing.*;

public class AggiungiLibroMediator implements MediatorIF {

    private JTextField titolo;
    private JTextField autore;
    private JTextField isbn;
    private JButton salva;

    public void setTitolo(JTextField titolo) {
        this.titolo = titolo;
    }

    public void setAutore(JTextField autore) {
        this.autore = autore;
    }

    public void setIsbn(JTextField isbn) {
        this.isbn = isbn;
    }

    public void setSalva(JButton salva) {
        this.salva = salva;
    }

    @Override
    public void widgetCambiato(JComponent widget) {
        if (widget == titolo || widget == autore || widget == isbn) {
            //il pulsante sarà abilitato solo se tutti i campi obbligatori sono stati compilati
            salva.setEnabled(tuttiCompilati());
        }
    }

    //metodo che verifica se tutti i campi sono stati compilati
    private boolean tuttiCompilati() {
        return !titolo.getText().strip().isEmpty()
                && !autore.getText().strip().isEmpty()
                && !isbn.getText().strip().isEmpty();
    }


}
