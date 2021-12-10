package br.com.alura.forum.controller.form;

import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AtualizaTopicoForm {

    @NotEmpty
    @NotNull
    @Size(min = 1)
    private String titulo;
    @NotEmpty @NotNull
    private String mensagem;

    public AtualizaTopicoForm(@NotEmpty @NotNull @Size(min = 1)String titulo, @NotEmpty @NotNull String mensagem) {
        this.titulo = titulo;
        this.mensagem = mensagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public Topico atualiza(Long id, TopicoRepository repository) {
        Topico topico = repository.findById(id).get();

        topico.setTitulo(this.titulo);
        topico.setMensagem(this.mensagem);

        return topico;
    }

}
