package gabriel.dac.livraria.domain.services;

import gabriel.dac.livraria.domain.dto.CreateLivroDTO;
import gabriel.dac.livraria.domain.dto.UpdateLivroDTO;
import gabriel.dac.livraria.domain.model.Livro;
import gabriel.dac.livraria.domain.services.exception.BookIsbnExists;
import gabriel.dac.livraria.domain.services.exception.EntityNotFound;
import gabriel.dac.livraria.infrastructure.repository.LivroRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private LivroService livroService;

    @Captor
    private ArgumentCaptor<Livro> livroArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    private ArgumentCaptor<Integer> isbnArgumentCaptor;

    @Nested
    class createLivro{

        @Test
        @DisplayName("Deve criar um livro com sucesso")
        void deveCriarLivro(){

            Livro livro = new Livro(UUID.randomUUID(),"Livro 1","Desconhecido",2025,12345,20);

            //Arrange
            doReturn(livro).when(livroRepository).save(livroArgumentCaptor.capture());
            CreateLivroDTO livroInput = new CreateLivroDTO("Livro 1","Desconhecido",2025,12345,20);

            //Action
            Livro livroOutput = livroService.create(livroInput);

            //Assert
            Livro livroCapturado = livroArgumentCaptor.getValue();

            assertNotNull(livroOutput);
            assertEquals(livroInput.titulo(),livroCapturado.getTitulo());
            assertEquals(livroInput.autor(),livroCapturado.getAutor());
            assertEquals(livroInput.ano(),livroCapturado.getAno());
            assertEquals(livroInput.isbn(),livroCapturado.getISBN());
            assertEquals(livroInput.quantidade(),livroCapturado.getQuantidade());

        }

        @Test
        @DisplayName("Deve lancar uma exceção caso o livro já exista")
        void deveLancarExceptionCasoLivroJaExista(){



            //Arrange
            doThrow(new BookIsbnExists("O livro com esse ISBN já existe")).when(livroRepository).save(any());
            CreateLivroDTO createLivroDTO = new CreateLivroDTO("Livro 1","Desconhecido",2025,12345,20);


            //Action

            //Assert

            assertThrows(BookIsbnExists.class,()->livroService.create(createLivroDTO));

        }
    }

    @Nested
    class getLivroByID{

        @Test
        @DisplayName("Deve retornar o livro por ID com sucesso quando o optional está presente")
        void deveRetornarLivroPorIDComSucessoComOptionalPresente(){
            Livro livro = new Livro(UUID.randomUUID(),"Livro 1","Desconhecido",2025,12345,20);

            //Arrange
            doReturn(true).when(livroRepository).existsById(uuidArgumentCaptor.capture());
            doReturn(Optional.of(livro)).when(livroRepository).findById(uuidArgumentCaptor.capture());

            //Act
            Optional<Livro> livroOutput = livroService.getLivroById(livro.getId().toString());

            assertTrue(livroOutput.isPresent());
            assertEquals(livro.getId(),uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Deve retornar exceção para buscar livro não encontrado por Id")
        void deveRetornarExceçãoParaBuscarLivroPorIDNaoEncontrado(){

            UUID livroId = UUID.randomUUID();

            //Arrange
            doReturn(false).when(livroRepository).existsById(uuidArgumentCaptor.capture());


            //Assert
            assertThrows(EntityNotFound.class,()->livroService.getLivroById(livroId.toString()));
            verify(livroRepository,never()).findById(any());
        }
    }

    @Nested
    class getAllLivros{

        @Test
        @DisplayName("Deve retornar todos os livros com sucesso")
        void deveRetornarTodosLivrosComSucesso(){

            //Arrange
            Livro livro = new Livro(UUID.randomUUID(),"Livro 1","Desconhecido",2025,12345,20);

            List<Livro> livrosList = List.of(livro);
            doReturn(List.of(livro)).when(livroRepository).findAll();


            //Act

            List<Livro> livrosOutput = livroService.getAll();

            //Assert

            assertNotNull(livrosOutput);
            assertEquals(livrosList.size(),livrosOutput.size());
        }
    }

    @Nested
    class deleteLivroByID{

        @Test
        @DisplayName("Deve deletar livro por ID com sucesso")
        void deveDeletarLivroPorID(){

            //Arrange
            doReturn(true).when(livroRepository).existsById(uuidArgumentCaptor.capture());
            doNothing().when(livroRepository).deleteById(uuidArgumentCaptor.capture());

            UUID livroUUID = UUID.randomUUID();

            //Act
           livroService.delete(livroUUID.toString());


            List<UUID> idList= uuidArgumentCaptor.getAllValues();
            assertEquals(livroUUID,idList.get(0));
            assertEquals(livroUUID,idList.get(1));

            verify(livroRepository,times(1)).existsById(idList.get(0));
            verify(livroRepository,times(1)).deleteById(idList.get(1));
        }

        @Test
        @DisplayName("Deve lançar exceção quando tentar deletar um livro que não existe")
        void deveLancarExcecaoQuandoTentarDeletarLivroQueNaoExiste(){
            UUID livroUUID = UUID.randomUUID();

            doReturn(false).when(livroRepository).existsById(uuidArgumentCaptor.capture());

            assertThrows(EntityNotFound.class,()->livroService.delete(livroUUID.toString()));

            verify(livroRepository,never()).deleteById(any());
        }
    }

    @Nested
    class updateLivroByID{

        @Test
        @DisplayName("Deve atualizar livro por ID quando existir com dados preenchidos")
        void deveAtualizarLivroPorIDQuandoExistirComDadosPreenchidos(){

            UpdateLivroDTO updateLivroDTO = new UpdateLivroDTO(
                    "newAutor",
                    "newTitulo",
                    2024,
                    20
            );
            Livro livro = new Livro(UUID.randomUUID(),"Livro 1","Desconhecido",2025,12345,20);

            //Arrange
            doReturn(Optional.of(livro)).when(livroRepository).findById(uuidArgumentCaptor.capture());
            doReturn(livro).when(livroRepository).save(livroArgumentCaptor.capture());

            //Act
            livroService.updateLivroByID(livro.getId().toString(),updateLivroDTO);

            assertEquals(livro.getId(),uuidArgumentCaptor.getValue());

            Livro livroCapturado = livroArgumentCaptor.getValue();

            assertEquals(updateLivroDTO.titulo(),livroCapturado.getTitulo());
            assertEquals(updateLivroDTO.autor(),livroCapturado.getAutor());
            assertEquals(updateLivroDTO.ano(),livroCapturado.getAno());
            assertEquals(updateLivroDTO.quantidade(),livroCapturado.getQuantidade());

            verify(livroRepository,times(1)).findById(uuidArgumentCaptor.getValue());
            verify(livroRepository,times(1)).save(livro);

        }

        @Test
        @DisplayName("Não Deve atualizar livro por ID quando ele não existir")
        void naoDeveAtualizarLivroQuandoEleNaoExistir(){

            UpdateLivroDTO updateLivroDTO = new UpdateLivroDTO(
                    "newAutor",
                    "newTitulo",
                    2024,
                    20
            );

            UUID livroID = UUID.randomUUID();

            //Arrange
            doReturn(Optional.empty()).when(livroRepository).findById(uuidArgumentCaptor.capture());

            //Act
            livroService.updateLivroByID(livroID.toString(),updateLivroDTO);

            assertEquals(livroID,uuidArgumentCaptor.getValue());



            verify(livroRepository,times(1)).findById(uuidArgumentCaptor.getValue());
            verify(livroRepository,times(0)).save(any());

        }
    }

    @Nested
    class getByISBN{

        @Test
        @DisplayName( "Deve buscar por ISBN quando o livro existir" )
        void deveBuscarPorISBN(){

            //Arrange
            Livro livro = new Livro(UUID.randomUUID(),"Livro 1","Desconhecido",2025,12345,20);

            //Act
            doReturn(Optional.of(livro)).when(livroRepository).findByIsbn(isbnArgumentCaptor.capture());

            Optional<Livro> livroOutput = livroService.buscarPorISBN(livro.getISBN());
            //Assert

            assertTrue(livroOutput.isPresent());
            assertEquals(livro.getISBN(),isbnArgumentCaptor.getValue());
        }


    }
}