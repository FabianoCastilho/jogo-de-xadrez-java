package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public abstract class PecaDeXadrez extends Peca {
	
	private Cor cor;
	private int contadorDeMovimentos;

	public PecaDeXadrez(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}
	public int getContadorDeMovimentos() {
		return contadorDeMovimentos;
	}
	public void incrementarContadorDeMovimentos() {
		contadorDeMovimentos ++;
	}
	public void reduzirContadorDeMovimentos() {
		contadorDeMovimentos --;
	}
	public PosicaoDoXadrez getPosicaoDoXadrez() {
		return PosicaoDoXadrez.converteDaPosicao(posicao);
	}
	protected boolean existePecaAdversaria(Posicao posicao) {
		PecaDeXadrez p = (PecaDeXadrez)getTabuleiro().peca(posicao);
		return p != null && p.getCor() != cor;
	}

}
