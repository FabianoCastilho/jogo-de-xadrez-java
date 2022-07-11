package application;

import java.util.Scanner;

import xadrez.PartidaDeXadrez;
import xadrez.PecaDeXadrez;
import xadrez.PosicaoDoXadrez;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		PartidaDeXadrez partida = new PartidaDeXadrez();
		
		while(true) {
			UI.printTabuleiro(partida.getPecas());
			System.out.println();
			System.out.print("Origem: ");
			PosicaoDoXadrez origem = UI.lerPosicaoDoXadrez(sc);
			
			System.out.println();
			System.out.print("Destino: ");
			PosicaoDoXadrez destino = UI.lerPosicaoDoXadrez(sc);
			
			PecaDeXadrez pecaCapturada = partida.executaMovimetoDoXadrez(origem, destino);
			
		}

	}

}
