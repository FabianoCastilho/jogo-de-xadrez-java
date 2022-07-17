package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PartidaDeXadrez;
import xadrez.PecaDeXadrez;

public class Peao extends PecaDeXadrez {

	private PartidaDeXadrez partida;

	public Peao(Tabuleiro tabuleiro, Cor cor, PartidaDeXadrez partida) {
		super(tabuleiro, cor);
		this.partida = partida;

	}

	@Override
	public String toString() {
		return "P";
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		Posicao p = new Posicao(0, 0);
		if (getCor() == Cor.BRANCO) {
			p.setValues(posicao.getLinha() - 1, posicao.getColuna());
			if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePecaNaPosicao(p))
				mat[p.getLinha()][p.getColuna()] = true;

			p.setValues(posicao.getLinha() - 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
			if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePecaNaPosicao(p)
					&& getTabuleiro().posicaoExiste(p2) && !getTabuleiro().existePecaNaPosicao(p2)
					&& getContadorDeMovimentos() == 0)
				mat[p.getLinha()][p.getColuna()] = true;

			p.setValues(posicao.getLinha() - 1, posicao.getColuna() - 1);
			if (getTabuleiro().posicaoExiste(p) && existePecaAdversaria(p))
				mat[p.getLinha()][p.getColuna()] = true;

			p.setValues(posicao.getLinha() - 1, posicao.getColuna() + 1);
			if (getTabuleiro().posicaoExiste(p) && existePecaAdversaria(p))
				mat[p.getLinha()][p.getColuna()] = true;

			// Movimento especial En Passant
			if (posicao.getLinha() == 3) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().posicaoExiste(esquerda) && existePecaAdversaria(esquerda)
						&& getTabuleiro().peca(esquerda) == partida.getVulneravelEnPassant())
					mat[esquerda.getLinha() - 1][esquerda.getColuna()] = true;

				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				if (getTabuleiro().posicaoExiste(direita) && existePecaAdversaria(direita)
						&& getTabuleiro().peca(direita) == partida.getVulneravelEnPassant())
					mat[direita.getLinha() - 1][direita.getColuna()] = true;

			}

		} else {
			p.setValues(posicao.getLinha() + 1, posicao.getColuna());
			if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePecaNaPosicao(p))
				mat[p.getLinha()][p.getColuna()] = true;

			p.setValues(posicao.getLinha() + 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
			if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePecaNaPosicao(p)
					&& getTabuleiro().posicaoExiste(p2) && !getTabuleiro().existePecaNaPosicao(p2)
					&& getContadorDeMovimentos() == 0)
				mat[p.getLinha()][p.getColuna()] = true;

			p.setValues(posicao.getLinha() + 1, posicao.getColuna() - 1);
			if (getTabuleiro().posicaoExiste(p) && existePecaAdversaria(p))
				mat[p.getLinha()][p.getColuna()] = true;

			p.setValues(posicao.getLinha() + 1, posicao.getColuna() + 1);
			if (getTabuleiro().posicaoExiste(p) && existePecaAdversaria(p))
				mat[p.getLinha()][p.getColuna()] = true;

			// Movimento especial En Passant
			if (posicao.getLinha() == 4) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().posicaoExiste(esquerda) && existePecaAdversaria(esquerda)
						&& getTabuleiro().peca(esquerda) == partida.getVulneravelEnPassant())
					mat[esquerda.getLinha() + 1][esquerda.getColuna()] = true;

				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				if (getTabuleiro().posicaoExiste(direita) && existePecaAdversaria(direita)
						&& getTabuleiro().peca(direita) == partida.getVulneravelEnPassant())
					mat[direita.getLinha() + 1][direita.getColuna()] = true;

			}

		}
		return mat;
	}

}
