import java.util.*;
import java.util.Random;

public class BlackBoard {
	public final static int CORRIDA = 1;
	public final static int COMER 	= 2;
	public final static int CALCULO	= 3;
	private final static int MAX_AGENTES = 10;
	private boolean btarefa;
	private boolean biniciarTarefa;
	private Vector vAssinantes;
	private Vector vAgentes;
	private Participante vencedorTarefa;
	private Participante escolhidoParedao;
	private Participante sorteadoParedao;
	private Atividade idAtividade;
	
	public BlackBoard( Vector ag ){
		btarefa = false;
		biniciarTarefa = false;
		vAgentes = ag;
		vAssinantes = new Vector();
		this.clear();
	}
	
	public synchronized void clear(){
		vAssinantes.clear();
		vAgentes.clear();
		vencedorTarefa = null;
		escolhidoParedao = null;
		sorteadoParedao = null;
		idAtividade = null;
		btarefa = false;
		biniciarTarefa = false;
	}
	
	public synchronized void addAgente( Participante p ){
		vAgentes.add(p);
	}
	
	public synchronized void delAgente( Participante p ){
		vAgentes.remove(p);
	}
	
	public synchronized boolean hasTarefa(){
		return btarefa;
	}
	
	public synchronized void publicaTarefa(int nTarefa){
		vAssinantes.clear();
		idAtividade = null;
		if( nTarefa == CORRIDA )
			idAtividade = new Atividade("Corrida",70,180,90,7,8,4,23,8,6,0);
		else if( nTarefa == COMER )
			idAtividade = new Atividade("Comer",85,170,60,4,8,5,20,4,5,0);
		else
			idAtividade = new Atividade("Calculo",70,165,130,4,9,7,28,4,8,1);
			
		btarefa = true;
		vAssinantes.clear();
	}
	
	public synchronized Atividade getTarefa(){
		return idAtividade;
	}
	
	public synchronized void startTarefa(){
		biniciarTarefa = true;
	}
	
	public synchronized void stopTarefa(){
		btarefa = false;
		biniciarTarefa = false;
	}
	
	public synchronized boolean inicioTarefa(){
		return biniciarTarefa;
	}
	
	public synchronized void assinaTarefa( Participante p ){
		vAssinantes.add(p);
		System.out.println("Participante :" + p.getNome() + " assinou a tarefa"); 
	}
	
	public synchronized void removeAssinante( Participante p ){
		vAssinantes.remove(p);
	}
	
	public synchronized int totalAssinantes(){
		return vAssinantes.size();
	}
	
	public synchronized int totalParticipantes(){
		return vAgentes.size();
	}
	
	public synchronized String[] getParticipanteNome(){
		Participante p;
		String[] nomes = new String[vAgentes.size()];
		for( int i = 0; i < vAgentes.size(); i++ ){
			p = (Participante)vAgentes.get(i);
			nomes[i] = p.getNome();
		}
		
		return nomes;
	}
	
	public synchronized void stopParticipantes(){
		Participante p;
		for( int i = 0; i < vAgentes.size(); i++ ){
			p = (Participante)vAgentes.get(i);
			p.stop();
			p = null;
		}
	}
	
	public synchronized Participante getAssinante( int idx ){
		return (Participante)vAssinantes.get( idx );
	}
	
	public synchronized void setVencedorTarefa( Participante winner ){
		vencedorTarefa = winner;
	}
	
	public synchronized String getNomeVencedorTarefa(){
		return vencedorTarefa.getNome();
	}
	
	public synchronized int setEscolhidoParedao( String nome ){
		int i;
		Participante p;
		
		for( i = 0; i < vAgentes.size(); i++ ){
			p = (Participante)vAgentes.get(i);
			if( p.getNome().compareTo(nome) == 0 && p != vencedorTarefa &&
				p != sorteadoParedao ){
				escolhidoParedao = p;
				System.out.println("escolhido para o paredao :" + 
					escolhidoParedao.getNome() + "\n" );
				return 0;
			}else if( p.getNome().compareTo(nome) == 0 && p == vencedorTarefa ){
				return -2;
				//System.out.println("Nao foi possivel escolher...\n");
			}else if( p.getNome().compareTo(nome) == 0 && p == sorteadoParedao ){
				return -3;
			}
		}
		
		return -1;
	}
	
	public synchronized String setSorteadoParedao(){
		Participante p;
		Random r = new Random();
		
		for(;;){
			p = (Participante)vAgentes.get(Math.abs( r.nextInt() ) % vAgentes.size());
			if( p != vencedorTarefa && p != escolhidoParedao ){
				sorteadoParedao = p;
				System.out.println("sorteado para o paredao :" + 
					sorteadoParedao.getNome() + "\n" );
				return sorteadoParedao.getNome();
			}
		}
	}
	
	public synchronized String eliminaParticipante(){
		String nome;
		Random r = new Random();
		if( (Math.abs( r.nextInt() ) % 2) == 0 ){
			delAgente( escolhidoParedao );
			removeAssinante( escolhidoParedao );
			nome = escolhidoParedao.getNome();
			escolhidoParedao.sairDaCasa();
		}else{
			nome = sorteadoParedao.getNome();
			delAgente( sorteadoParedao );
			removeAssinante( sorteadoParedao );
			sorteadoParedao.sairDaCasa();
		}
		
		escolhidoParedao = null;
		sorteadoParedao = null;
		idAtividade = null;
		vencedorTarefa = null;
		vAssinantes.clear();
		return nome;
	}
	
	public synchronized void ultimoParedao(){
		escolhidoParedao = (Participante)vAgentes.get(0);
		sorteadoParedao  = (Participante)vAgentes.get(1);
	}
			
	public synchronized Participante isGameOver(){
		Participante p;
		if( vAgentes.size() == 1 ){
			p = (Participante)vAgentes.firstElement();
			return p;
		}
		
		return null;
	}
}