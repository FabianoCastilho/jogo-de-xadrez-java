package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadrez.PartidaDeXadrez;
import xadrez.PecaDeXadrez;
import xadrez.PosicaoDoXadrez;
import xadrez.XadrezException;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		PartidaDeXadrez partida = new PartidaDeXadrez();
		List<PecaDeXadrez>	capturadas = new ArrayList<>();
		while(!partida.getCheckMate()) {
			try {
				UI.clearScreen();
				UI.printPartida(partida, capturadas);;
				System.out.println();
				System.out.print("Origem: ");
				PosicaoDoXadrez origem = UI.lerPosicaoDoXadrez(sc);
				
				boolean [][] movimentosPossiveis = partida.movimentosPossiveis(origem);
				UI.clearScreen();
				UI.printTabuleiro(partida.getPecas(), movimentosPossiveis);
				
				System.out.println();
				System.out.print("Destino: ");
				PosicaoDoXadrez destino = UI.lerPosicaoDoXadrez(sc);
				
				PecaDeXadrez pecaCapturada = partida.executaMovimetoDoXadrez(origem, destino);
				
				if (pecaCapturada != null)
					capturadas.add(pecaCapturada);
				
				if (partida.getPecaPromovida() != null) {
					System.out.print("Digite a peca para Promocao ( B / C / T / Q): ");
					String tipo = sc.nextLine().toUpperCase();
					while ( !tipo.equals("B") && !tipo.equals("C") && !tipo.equals("T") && !tipo.equals("Q")) {
						System.out.print("Letra invalida.Digite novamente a peca para Promocao ( B / C / T / Q): ");
						tipo = sc.nextLine().toUpperCase();
					}
					partida.substituirPecaPromovida(tipo);
				}
			}
			catch (XadrezException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		UI.clearScreen();
		UI.printPartida(partida, capturadas);

	}

}
