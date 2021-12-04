package br.com.alura.forum.controller.validator;

import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProibeTitutoDuplicadoValidator implements Validator {

    @Autowired
    private TopicoRepository repository;

    @Override
    public boolean supports(Class<?> aClass) {
        return TopicoForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if(errors.hasErrors()) {
            return;
        }

        TopicoForm requestDto = (TopicoForm)target;
        Topico existeTitulo = repository.findByTitulo(requestDto.getTitulo());
        if(existeTitulo != null) {
            errors.rejectValue("titulo",null,"Nao pode existir dois topicos com o mesmo titulo");
        }

    }
}
