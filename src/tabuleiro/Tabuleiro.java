package tabuleiro;

public class Tabuleiro {
	
	private int linhas;
	private int colunas;
	private Peca[][] pecas;
	
	public Tabuleiro(int linhas, int colunas) {
		
		if(linhas < 1 || colunas < 1)
			throw new TabuleiroException("Erro ao criar Tabuleiro: deve haver pelo menos 1 linha e 1 coluna.");
		
		this.linhas = linhas;
		this.colunas = colunas;
		pecas = new Peca[linhas][colunas];
	}
	public int getLinhas() {
		return linhas;
	}
	public int getColunas() {
		return colunas;
	}
	public Peca peca(int linha, int coluna) {
		if (!posicaoExiste(linha, coluna))
			throw new TabuleiroException("Posição não existe no Tabuleiro");
		
		return pecas[linha][coluna];
	}
	public Peca peca(Posicao posicao) {
		if (!posicaoExiste(posicao))
			throw new TabuleiroException("Posição não existe no Tabuleiro");
		
		return pecas[posicao.getLinha()][posicao.getColuna()];
	}
	public void colocarPeca(Peca peca, Posicao posicao) {
		if (existePecaNaPosicao(posicao))
			throw new TabuleiroException("Já existe uma peça na posição" + posicao);
		
		pecas[posicao.getLinha()][posicao.getColuna()] = peca;
		peca.posicao = posicao;
	}
	public Peca removePeca (Posicao posicao) {
		if (!posicaoExiste(posicao))
			throw new TabuleiroException("Posição não existe no Tabuleiro");
		
		if (peca(posicao) == null)
			return null;
		
		Peca aux = peca(posicao);
		aux.posicao = null;
		pecas[posicao.getLinha()][posicao.getColuna()] = null;
		return aux;
		
	}
	public boolean posicaoExiste(int linha, int coluna) {
		return linha >= 0 && coluna < linhas && coluna>= 0 && coluna < colunas;
		
	}
	public boolean posicaoExiste(Posicao posicao) {
		return posicaoExiste(posicao.getLinha(), posicao.getColuna());
		
	}
	public boolean existePecaNaPosicao(Posicao posicao) {
		if (!posicaoExiste(posicao))
			throw new TabuleiroException("Posição não existe no Tabuleiro");
		
		return peca(posicao) != null;
	}

}
