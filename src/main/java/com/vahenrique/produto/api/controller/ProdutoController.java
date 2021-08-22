package com.vahenrique.produto.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vahenrique.produto.domain.model.Produto;
import com.vahenrique.produto.domain.repository.ProdutoRepository;
import com.vahenrique.produto.domain.service.CadastroProdutoService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	private ProdutoRepository produtoRepository;
	private CadastroProdutoService cadastroProdutoService;

	@GetMapping
	private List<Produto> listar() {
		return produtoRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> localizar(@PathVariable Long id) {
		return produtoRepository.findById(id).map(produto -> ResponseEntity.ok(produto))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Produto incluir(@Valid @RequestBody Produto produto) {
		return cadastroProdutoService.salvar(produto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Produto> atualizar(@PathVariable Long id, @Valid @RequestBody Produto produto) {
		if (!produtoRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

		produto.setId(id);
		produto = cadastroProdutoService.salvar(produto);

		return ResponseEntity.ok(produto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		if (!produtoRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

		cadastroProdutoService.deletar(id);

		return ResponseEntity.noContent().build();
	}
}
