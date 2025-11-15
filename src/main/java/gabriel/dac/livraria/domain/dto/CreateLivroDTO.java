package gabriel.dac.livraria.domain.dto;

public record CreateLivroDTO(String titulo, String autor, int ano, int isbn, int quantidade) {
}
