package gabriel.dac.livraria.domain.services;

import gabriel.dac.livraria.domain.dto.CreateLivroDTO;
import gabriel.dac.livraria.domain.dto.UpdateLivroDTO;
import gabriel.dac.livraria.domain.model.Livro;
import gabriel.dac.livraria.infrastructure.repository.LivroRepository;
import gabriel.dac.livraria.domain.services.exception.EntityNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LivroService {


    @Autowired
    private LivroRepository livroRepository;

    public Livro create(CreateLivroDTO createLivroDTO){

        Livro livro = new Livro(null,
                createLivroDTO.titulo(),
                createLivroDTO.autor(),
                createLivroDTO.ano(),
                createLivroDTO.isbn(),
                createLivroDTO.quantidade());

        return livroRepository.save(livro);
    }

    @Transactional
    public void delete(String id){

        boolean livroExists = livroRepository.existsById(UUID.fromString(id));

        if(!livroExists){
            throw new EntityNotFound("O livro que está tentando ser deletado não existe!");
        }

        livroRepository.deleteById(UUID.fromString(id));
    }

    public Optional<Livro> getLivroById(String id){
        Boolean livroObj = livroRepository.existsById(UUID.fromString(id));
        if(!livroObj){
            throw new EntityNotFound("O livro de ID: " + id + " não foi encontrado!");
        }
        return livroRepository.findById(UUID.fromString(id));
    }

    public List<Livro> getAll(){
        return livroRepository.findAll();
    }

    @Transactional
    public void updateLivroByID(String livroID, UpdateLivroDTO updateLivroDTO){
            Optional<Livro> livroExists = livroRepository.findById(UUID.fromString(livroID));

            if(livroExists.isPresent()){

                Livro livro = livroExists.get();

                if(updateLivroDTO.autor() != null){
                    livro.setAutor(updateLivroDTO.autor());
                }

                if(updateLivroDTO.titulo() != null){
                    livro.setTitulo(updateLivroDTO.titulo());
                }

                if(updateLivroDTO.quantidade() >0 ){
                    livro.setQuantidade(updateLivroDTO.quantidade());
                }

                if(updateLivroDTO.ano() > 0){
                    livro.setAno(updateLivroDTO.ano());
                }

                livroRepository.save(livro);
            }else{
                throw new EntityNotFound("Livro de ID: " + livroID + " não foi encontrado!");
            }
    }



    public Optional<Livro> buscarPorISBN(int isbn){
        Optional<Livro> livroObj = livroRepository.findByIsbn(isbn);
        if(livroObj.isEmpty()){
            throw new EntityNotFound("O livro de ISBN: " + isbn + " não foi encontrado!");
        }
        return livroObj;
    }
}
