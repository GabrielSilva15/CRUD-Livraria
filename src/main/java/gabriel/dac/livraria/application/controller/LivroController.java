package gabriel.dac.livraria.application.controller;

import gabriel.dac.livraria.domain.dto.CreateLivroDTO;
import gabriel.dac.livraria.domain.dto.UpdateLivroDTO;
import gabriel.dac.livraria.domain.model.Livro;
import gabriel.dac.livraria.domain.services.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @PostMapping
    public ResponseEntity<Livro> create(@RequestBody CreateLivroDTO createLivroDTO){
        return ResponseEntity.ok().body( livroService.create(createLivroDTO));
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        livroService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Livro> getLivroById(@PathVariable String id){
        Optional<Livro> livro = livroService.getLivroById(id);

        if(livro.isPresent()){
            return ResponseEntity.ok().body(livro.get());
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Livro>> getAll(){
        return ResponseEntity.ok().body(livroService.getAll());
    }

    @GetMapping(value="/isbn/{isbn}")
    public ResponseEntity<Livro> buscarPorISBN(@PathVariable int isbn){
        return ResponseEntity.ok().body(livroService.buscarPorISBN(isbn).get());
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Void> updateLivro(@PathVariable String id, @RequestBody UpdateLivroDTO updateLivroDTO){
            livroService.updateLivroByID(id, updateLivroDTO);
            return ResponseEntity.noContent().build();
    }

}
