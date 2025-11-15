package gabriel.dac.livraria.domain.services.exception;

public class BookIsbnExists extends RuntimeException{
    public BookIsbnExists(String message){
        super(message);
    }
}
