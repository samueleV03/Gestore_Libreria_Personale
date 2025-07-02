package builder;

public class Libro {

    private String titolo;
    private String autore;
    private String isbn;
    private Stato statoLettura; //per ora con un enum, forse dopo con state
    private String genere;
    private int valutazione;

    //metodi getter
    public String getTitolo(){return this.titolo;}
    public String getAutore(){return this.autore;}
    public String getIsbn(){return this.isbn;}
    public Stato getStatoLettura(){return this.statoLettura;}
    public String getGenere(){return this.genere;}
    public int getValutazione(){return this.valutazione;}

    public void setAutore(String autore) {this.autore = autore;}
    public void setIsbn(String isbn) {this.isbn = isbn;}
    public void setTitolo(String titolo) {this.titolo = titolo;}
    public void setGenere(String genere) {this.genere = genere;}

    //la valutazione deve essere compresa tra 0 e 5
    public void setValutazione(int valutazione) {
        if(valutazione >= 0 && valutazione <= 5)
            this.valutazione = valutazione;
        else
            throw new IllegalArgumentException("Valutazione non valida");
    }

    //Gestisco il costruttore con Builder
    public static class Builder {
        //Parametri richiesti, obbligatori
        private final String titolo;
        private final String autore;
        private final String isbn;
        //Parametri opzionali
        private Stato statoLettura=Stato.DaLeggere;
        private int valutazione=0;
        private String genere="Default genere";

        public Builder(String titolo, String autore, String isbn) {
            this.titolo = titolo;
            this.autore = autore;
            this.isbn = isbn;
        }
        //public Builder Titolo(String titolo) {this.titolo=titolo;return this;}
        //public Builder Autore(String autore) {this.autore=autore;return this;}
        //public Builder ISBN(String ISBN) {this.ISBN=ISBN;return this;}
        public Builder Stato(Stato stato){this.statoLettura=stato;return this;}
        public Builder Valutazione(int valutazione) {this.valutazione=valutazione;return this;}
        public Builder Genere(String genere) {this.genere=genere;return this;}

        public Libro build() {return new Libro(this);}
    }//Builder

    private Libro(Builder builder) {
        this.titolo = builder.titolo;
        this.autore = builder.autore;
        this.isbn = builder.isbn;
        this.statoLettura = builder.statoLettura;
        this.valutazione = builder.valutazione;
        this.genere = builder.genere;
    }

    public static enum Stato{
        Letto, DaLeggere, InLettura
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Libro)) return false;
        Libro libro = (Libro) o;
        return isbn != null && isbn.equals(libro.isbn);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(isbn);
    }

}
