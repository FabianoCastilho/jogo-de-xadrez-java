package xadrez;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Bispo;
import xadrez.pecas.Cavaleiro;
import xadrez.pecas.Peao;
import xadrez.pecas.Rainha;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaDeXadrez {

	private int jogada;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean check;
	private boolean checkMate;
	private PecaDeXadrez vulneravelEnPassant;
	private PecaDeXadrez pecaPromovida;

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

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	public PecaDeXadrez getVulneravelEnPassant() {
		return vulneravelEnPassant;
	}
	public PecaDeXadrez getPecaPromovida() {
		return pecaPromovida;
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

	public boolean[][] movimentosPossiveis(PosicaoDoXadrez posicaoOrigem) {
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

		if (testCheck(jogadorAtual)) {
			desfazerMovimento(origem, destino, pecaCapturada);
			throw new XadrezException("Voce nao pode se colocar em Check");
		}
		PecaDeXadrez pecaMovida = (PecaDeXadrez) tabuleiro.peca(destino);
		
		//Movimeto especial Promoção
		pecaPromovida = null;
		if( pecaMovida instanceof Peao) {
			if(pecaMovida.getCor() == Cor.BRANCO && destino.getLinha() == 0 || pecaMovida.getCor() == Cor.PRETO && destino.getLinha() == 7) {
				pecaPromovida = (PecaDeXadrez)tabuleiro.peca(destino);
				pecaPromovida = substituirPecaPromovida("Q");
			}
		}

		check = (testCheck(oponente(jogadorAtual))) ? true : false;

		if (testCheckMate(oponente(jogadorAtual)))
			checkMate = true;
		else
			proximaJogada();

		// Movimento especial En Passant.
		if (pecaMovida instanceof Peao
				&& (destino.getLinha() == origem.getLinha() - 2 || destino.getLinha() == origem.getLinha() + 2))
			vulneravelEnPassant = pecaMovida;
		else
			vulneravelEnPassant = null;

		return (PecaDeXadrez) pecaCapturada;
	}
	public PecaDeXadrez substituirPecaPromovida(String tipo) {
		if(pecaPromovida == null) 
			throw new IllegalStateException("Não há peca para promover ");
		if ( !tipo.equals("B") && !tipo.equals("C") && !tipo.equals("T") && !tipo.equals("Q"))
			throw new InvalidParameterException("Tipo de peca escolhida invalida para promoção");
		
		Posicao pos = pecaPromovida.getPosicaoDoXadrez().converteParaPosicao();
		Peca p = tabuleiro.removePeca(pos);
		pecasNoTabuleiro.remove(p);
		
		PecaDeXadrez novaPeca = novaPeca(tipo, pecaPromovida.getCor());
		tabuleiro.colocarPeca(novaPeca, pos);
		pecasNoTabuleiro.add(novaPeca);
		
		return novaPeca;	
	}
	private PecaDeXadrez novaPeca( String tipo, Cor cor) {
		if(tipo.equals("B"))
			return new Bispo(tabuleiro, cor);
		if(tipo.equals("C"))
			return new Cavaleiro(tabuleiro, cor);
		if(tipo.equals("Q"))
			return new Rainha(tabuleiro, cor);
		return new Torre(tabuleiro, cor);
	}

	private Peca fazerMovimento(Posicao origem, Posicao destino) {
		PecaDeXadrez p = (PecaDeXadrez) tabuleiro.removePeca(origem);
		p.incrementarContadorDeMovimentos();
		Peca pecaCapturada = tabuleiro.removePeca(destino);
		tabuleiro.colocarPeca(p, destino);
		if (pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}
		// Movimento Especial torre
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destimoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.removePeca(origemT);
			tabuleiro.colocarPeca(torre, destimoT);
			torre.incrementarContadorDeMovimentos();
		}
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destimoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.removePeca(origemT);
			tabuleiro.colocarPeca(torre, destimoT);
			torre.incrementarContadorDeMovimentos();
		}
		// Movimento Especial En Passant
		if (p instanceof Peao) {
			if (origem.getColuna() != destino.getColuna() && pecaCapturada == null) {
				Posicao peaoPosicao;

				if (p.getCor() == Cor.BRANCO)
					peaoPosicao = new Posicao(destino.getLinha() + 1, destino.getColuna());

				else
					peaoPosicao = new Posicao(destino.getLinha() - 1, destino.getColuna());
				pecaCapturada = tabuleiro.removePeca(peaoPosicao);
				pecasCapturadas.add(pecaCapturada);
				pecasNoTabuleiro.remove(pecaCapturada);
			}
		}
		return pecaCapturada;
	}

	private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
		PecaDeXadrez p = (PecaDeXadrez) tabuleiro.removePeca(destino);
		p.reduzirContadorDeMovimentos();
		tabuleiro.colocarPeca(p, origem);

		if (pecaCapturada != null) {
			tabuleiro.colocarPeca(pecaCapturada, destino);
			pecasCapturadas.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);
		}
		// Movimento Especial torre
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destimoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.removePeca(destimoT);
			tabuleiro.colocarPeca(torre, origemT);
			torre.reduzirContadorDeMovimentos();
		}
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destimoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.removePeca(destimoT);
			tabuleiro.colocarPeca(torre, origemT);
			torre.reduzirContadorDeMovimentos();
		}
		// Movimento Especial En Passant
		if (p instanceof Peao) {
			if (origem.getColuna() != destino.getColuna() && pecaCapturada == vulneravelEnPassant) {
				PecaDeXadrez peao = (PecaDeXadrez)tabuleiro.removePeca(destino);
				Posicao peaoPosicao;

				if (p.getCor() == Cor.BRANCO)
					peaoPosicao = new Posicao(3, destino.getColuna());

				else
					peaoPosicao = new Posicao(4, destino.getColuna());
				tabuleiro.colocarPeca(peao, peaoPosicao);
			}
		}
	}

	private void validarPosicaoOrigem(Posicao posicao) {
		if (!tabuleiro.existePecaNaPosicao(posicao))
			throw new XadrezException("Não existe peça na posição de origem.");

		if (jogadorAtual != ((PecaDeXadrez) tabuleiro.peca(posicao)).getCor())
			throw new XadrezException("A peça escolhida não é sua.");

		if (!tabuleiro.peca(posicao).existeMovimentoPossivel())
			throw new XadrezException("Não existe movimentos possiveis para a peça escolhida.");
	}

	private void validarPosicaoDestino(Posicao origem, Posicao destino) {
		if (!tabuleiro.peca(origem).movimentoPossivel(destino))
			throw new XadrezException("A peça escolhida não pode se mover para a posição de destino");
	}

	private void proximaJogada() {
		jogada++;
		jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}

	private Cor oponente(Cor cor) {
		return (cor == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}

	private PecaDeXadrez rei(Cor cor) {
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaDeXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : list) {
			if (p instanceof Rei)
				return (PecaDeXadrez) p;
		}
		throw new IllegalStateException("Nao ha Rei" + cor + "no tabuleiro");
	}

	private boolean testCheck(Cor cor) {
		Posicao posicaoDoRei = rei(cor).getPosicaoDoXadrez().converteParaPosicao();
		List<Peca> pecasDoOponente = pecasNoTabuleiro.stream().filter(x -> ((PecaDeXadrez) x).getCor() == oponente(cor))
				.collect(Collectors.toList());
		for (Peca p : pecasDoOponente) {
			boolean[][] mat = p.movimentosPossiveis();
			if (mat[posicaoDoRei.getLinha()][posicaoDoRei.getColuna()])
				return true;
		}
		return false;
	}

	private boolean testCheckMate(Cor cor) {
		if (!testCheck(cor))
			return false;
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaDeXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : list) {
			boolean[][] mat = p.movimentosPossiveis();
			for (int i = 0; i < tabuleiro.getLinhas(); i++) {
				for (int j = 0; j < tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao origem = ((PecaDeXadrez) p).getPosicaoDoXadrez().converteParaPosicao();
						Posicao destino = new Posicao(i, j);
						Peca pecaCapturada = fazerMovimento(origem, destino);
						boolean testCheck = testCheck(cor);
						desfazerMovimento(origem, destino, pecaCapturada);
						if (!testCheck)
							return false;
					}
				}
			}
		}
		return true;
	}

	private void colocarNovaPeca(char coluna, int linha, PecaDeXadrez peca) {
		tabuleiro.colocarPeca(peca, new PosicaoDoXadrez(coluna, linha).converteParaPosicao());
		pecasNoTabuleiro.add(peca);
	}

	private void configuracaoInicial() {

		colocarNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('b', 1, new Cavaleiro(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('g', 1, new Cavaleiro(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO, this));

		colocarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('b', 8, new Cavaleiro(tabuleiro, Cor.PRETO));
		colocarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
		colocarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('g', 8, new Cavaleiro(tabuleiro, Cor.PRETO));
		colocarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO, this));

	}

}
