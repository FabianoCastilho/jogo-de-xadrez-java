package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaDeXadrez;

public class Cavaleiro extends PecaDeXadrez{
	
	public Cavaleiro(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);	
	}
	@Override
	public String toString() {
		return "C";
	}
	private boolean  podeMover(Posicao posicao) {
		PecaDeXadrez p = (PecaDeXadrez)getTabuleiro().peca(posicao);
		return p == null || p.getCor() != getCor();
	}
	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		
		Posicao p = new Posicao(0, 0);
		
		p.setValues(posicao.getLinha() - 1, posicao.getColuna() - 2);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;
		
		p.setValues(posicao.getLinha()  - 2, posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;
		
		p.setValues(posicao.getLinha() - 2 , posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;
		
		p.setValues(posicao.getLinha() -1 , posicao.getColuna() + 2);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;
		
		p.setValues(posicao.getLinha() + 1, posicao.getColuna() + 2);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;
		
		p.setValues(posicao.getLinha() + 2, posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;
		
		p.setValues(posicao.getLinha() + 2, posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;
		
		p.setValues(posicao.getLinha() + 1, posicao.getColuna() - 2);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;
		
		return mat;
	}

}
