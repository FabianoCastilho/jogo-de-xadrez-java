package xadrez;

import tabuleiro.Posicao;
import tabuleiro.TabuleiroException;

public class PosicaoDoXadrez {
	private char coluna;
	private int linha;
	public PosicaoDoXadrez(char coluna, int linha) {
		if (coluna < 'a' || coluna > 'h' || linha < 1 || linha > 8 )
			throw new TabuleiroException("Erro ao instanciar a Posição de Xadrez. Os valores válidos são de a1 a h8.");
		this.coluna = coluna;
		this.linha = linha;
	}
	public char getColuna() {
		return coluna;
	}
	public int getLimha() {
		return linha;
	}
	protected Posicao converteParaPosicao() {
		return new Posicao(8 - linha, coluna - 'a');	
	}
	protected static PosicaoDoXadrez converteDaPosicao(Posicao posicao) {
		return new PosicaoDoXadrez((char)('a' + posicao.getColuna()), 8 - posicao.getLinha());
	}
	
	@Override
	public String toString() {
		return "" +coluna + linha;
	}
}
