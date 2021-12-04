package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizaTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.controller.validator.ProibeTitutoDuplicadoValidator;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository repository;
    @PersistenceContext
    private EntityManager manager;
    @Autowired
    private ProibeTitutoDuplicadoValidator proibeTitutoDuplicadoValidator;

    @InitBinder(value = "topicoForm")
    public void init(WebDataBinder binder) {
        binder.addValidators(proibeTitutoDuplicadoValidator);
    }

    @GetMapping
    @Cacheable(value = "listaDeTopicos")
    public Page<TopicoDto> detalhe(@RequestParam(required = false) String nomeCurso, @PageableDefault(sort = "id",
        direction = Sort.Direction.ASC, page = 0, size = 10) Pageable paginacao) {

        if(nomeCurso == null) {
            Page<Topico> topicos = repository.findAll(paginacao);
            return TopicoDto.converte(topicos);
        }
        Page<Topico> topicosFiltrados = repository.findByCursoNome(nomeCurso, paginacao);
        return TopicoDto.converte(topicosFiltrados);
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDto> cadastra(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriComponentsBuilder) {
        Topico topico = topicoForm.toModel(manager);
        repository.save(topico);

        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesDoTopicoDto> detalhe(@PathVariable Long id) {
        Optional<Topico> optionalTopico = repository.findById(id);
        if(optionalTopico.isPresent()) {
            return ResponseEntity.ok(new DetalhesDoTopicoDto(optionalTopico.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDto> atualiza(@PathVariable Long id, @RequestBody @Valid AtualizaTopicoForm topicoForm) {
        Optional<Topico> optionalTopico = repository.findById(id);
        if(optionalTopico.isPresent()) {
            topicoForm.atualiza(id, repository);
            return ResponseEntity.ok(new TopicoDto(optionalTopico.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Topico> optionalTopico = repository.findById(id);
        if(optionalTopico.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
