package xadrez;

import java.util.ArrayList;
import java.util.List;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaDeXadrez {

	private int jogada;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	
	private List<Peca> pecasNoTabuleiro = new ArrayList<>();
	private List<Peca> pecasCapturadas = new ArrayList<>();

	public PartidaDeXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		jogada = 1;
		jogadorAtual = Cor.BRANCO;
		configuracaoInicial();
	}

	public int getJogada() {
		return jogada;
	}
	public Cor getJogadorAtual() {
		return jogadorAtual;
	}
	public PecaDeXadrez[][] getPecas() {
		PecaDeXadrez[][] matriz = new PecaDeXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for (int j = 0; j < tabuleiro.getColunas(); j++) {
				matriz[i][j] = (PecaDeXadrez) tabuleiro.peca(i, j);
			}
		}
		return matriz;
	}
	public boolean[][] movimentosPossiveis (PosicaoDoXadrez posicaoOrigem){
		Posicao posicao = posicaoOrigem.converteParaPosicao();
		validarPosicaoOrigem(posicao);
		return tabuleiro.peca(posicao).movimentosPossiveis();
	}
	public PecaDeXadrez executaMovimetoDoXadrez(PosicaoDoXadrez posicaoOrigem, PosicaoDoXadrez posicaoDestino) {
		Posicao origem = posicaoOrigem.converteParaPosicao();
		Posicao destino = posicaoDestino.converteParaPosicao();
		validarPosicaoOrigem(origem);
		validarPosicaoDestino(origem, destino);
		Peca pecaCapturada = fazerMovimento(origem, destino);
		proximaJogada();
		return(PecaDeXadrez) pecaCapturada;
	}
	private Peca fazerMovimento (Posicao origem, Posicao destino) {
		Peca p = tabuleiro.removePeca(origem);
		Peca pecaCapturada = tabuleiro.removePeca(destino);
		tabuleiro.colocarPeca(p, destino);
		if (pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);	
		}
		return pecaCapturada;
	}
	private void validarPosicaoOrigem(Posicao posicao) {
		if (!tabuleiro.existePecaNaPosicao(posicao)) 
			throw new XadrezException("Não existe peça na posição de origem.");
		
		if (jogadorAtual != ((PecaDeXadrez)tabuleiro.peca(posicao)).getCor())
				throw new XadrezException("A peça escolhida não é sua." );
		
		if (!tabuleiro.peca(posicao).existeMovimentoPossivel())
			throw new XadrezException("Não existe movimentos possiveis para a peça escolhida.");
	}
	private void validarPosicaoDestino(Posicao origem, Posicao destino) {
		if (!tabuleiro.peca(origem).movimentoPossivel(destino))
			throw new XadrezException("A peça escolhida não pode se mover para a posição de destino");
	}
	private void proximaJogada() {
		jogada ++;
		jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}

	private void colocarNovaPeca(char coluna, int linha, PecaDeXadrez peca) {
		tabuleiro.colocarPeca(peca, new PosicaoDoXadrez(coluna, linha).converteParaPosicao());
		pecasNoTabuleiro.add(peca);
	}

	private void configuracaoInicial() {

		colocarNovaPeca('c', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('c', 2, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('d', 2, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('e', 2, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('e', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('d', 1, new Rei(tabuleiro, Cor.BRANCO));

		colocarNovaPeca('c', 7, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('c', 8, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('d', 7, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('e', 7, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('e', 8, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('d', 8, new Rei(tabuleiro, Cor.PRETO));
	}

}
