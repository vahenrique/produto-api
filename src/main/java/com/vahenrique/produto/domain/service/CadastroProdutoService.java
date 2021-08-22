package com.vahenrique.produto.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vahenrique.produto.domain.exception.RegraDeNegocioException;
import com.vahenrique.produto.domain.model.Produto;
import com.vahenrique.produto.domain.repository.ProdutoRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CadastroProdutoService {

	private ProdutoRepository produtoRepository;

	@Transactional
	public Produto salvar(Produto produto) {
		if (produtoRepository.findByNome(produto.getNome()).stream()
				.anyMatch(produtoExistente -> !produtoExistente.equals(produto))) {
			throw new RegraDeNegocioException("Já existe um produto com o mesmo nome.");
		}

		return produtoRepository.save(produto);
	}

	@Transactional
	public void deletar(Long id) {
		produtoRepository.deleteById(id);
	}
}
