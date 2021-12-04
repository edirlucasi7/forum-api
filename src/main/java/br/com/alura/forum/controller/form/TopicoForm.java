package br.com.alura.forum.controller.form;

import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TopicoForm {

    @NotEmpty @NotNull @Size(min = 1)
    private String titulo;
    @NotEmpty @NotNull
    private String mensagem;
    @NotNull
    private Long idCurso;

    public TopicoForm(@NotEmpty @NotNull @Size String titulo, @NotEmpty @NotNull String mensagem, @NotNull Long idCurso) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.idCurso = idCurso;
    }

    public Topico toModel(EntityManager manager) {
        Curso curso = manager.find(Curso.class, idCurso);
        return new Topico(titulo,mensagem,curso);
    }

    public String getTitulo() {
        return this.titulo;
    }
}
