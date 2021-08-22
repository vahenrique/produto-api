package com.vahenrique.produto.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vahenrique.produto.domain.exception.RegraDeNegocioException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<Campo> campos = new ArrayList<>();

		ex.getBindingResult().getAllErrors().forEach(error -> {
			String nome = ((FieldError) error).getField();
			String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
			campos.add(new Campo(nome, mensagem));
		});

		Erro erro = new Erro();
		erro.setStatus(status.value());
		erro.setDataHora(OffsetDateTime.now());
		erro.setTitulo("Um ou mais campos com valor inválido.");
		erro.setCampos(campos);

		return handleExceptionInternal(ex, erro, headers, status, request);
	}

	@ExceptionHandler(RegraDeNegocioException.class)
	public ResponseEntity<Object> handleNegocio(RegraDeNegocioException ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;

		Erro erro = new Erro();
		erro.setStatus(status.value());
		erro.setDataHora(OffsetDateTime.now());
		erro.setTitulo(ex.getMessage());

		return handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);
	}
}
