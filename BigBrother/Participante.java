import javax.swing.*;

public class Participante extends Caracteristicas implements Runnable{
   private String strNome;
   private BlackBoard bb;
   private BigBrother out;
   private Thread thread;
   private Atividade idAtividade;
   
   
   /**
   *  Método Construtor
   */
   public Participante(BlackBoard mem, String pstrNome, int pintPeso, 
   		int pintAltura, int pintInteligencia, int pintAgilidade, 
   		int pintDeterminacao, int pintConhecGerais, int pintIdade, 
   		int pintForca, int pintEsperteza, int plogSexo, BigBrother big) {
   	  bb = mem;
   	  out = big;
      this.setNome         (pstrNome);
      this.setPeso         (pintPeso);
      this.setAltura       (pintAltura);
      this.setInteligencia (pintInteligencia);
      this.setAgilidade    (pintAgilidade);
      this.setDeterminacao (pintDeterminacao);
      this.setConhecGerais (pintConhecGerais);
      this.setIdade        (pintIdade);
      this.setForca        (pintForca);
      this.setEsperteza    (pintEsperteza);
      this.setSexo         (plogSexo);
   }
   
   public void setNome(String pstrNome) {
      this.strNome = pstrNome;      
   }
   
   public String getNome() {
      return this.strNome;
   }
   
   public boolean Alterar(String pstrNome, int pintPeso, int pintAltura, int pintInteligencia, int pintAgilidade, int pintDeterminacao, int pintConhecGerais, int pintIdade, int pintForca, int pintEsperteza, int plogSexo) {
      this.setNome         (pstrNome);
      this.setPeso         (pintPeso);
      this.setAltura       (pintAltura);
      this.setInteligencia (pintInteligencia);
      this.setAgilidade    (pintAgilidade);
      this.setDeterminacao (pintDeterminacao);
      this.setConhecGerais (pintConhecGerais);
      this.setIdade        (pintIdade);
      this.setForca        (pintForca);
      this.setEsperteza    (pintEsperteza);
      this.setSexo         (plogSexo);
      return true;
   }
   
   public boolean Excluir() {
      return false;
   }
   
   public void start( String nome ){
   		thread = new Thread(this, nome );
   		thread.start();
   }
   
   public void run(){
   		Thread me = Thread.currentThread();
   		out.display.append("O participante - " + me.getName() + " entra na casa...\n");
   		while( me == thread ){
   			try{
   				Thread.sleep( 700 );
   			}catch( InterruptedException e ){}
   			if( verificaTarefa() ){
   				idAtividade = bb.getTarefa();
   				out.display.append(me.getName() + " aguardando o inicio da tarefa:"
   					+ idAtividade.getNome() + "\n" );
   				aguardaInicioTarefa();
   				ExecutarTarefa();
   			}
   		}
   }
   
   public boolean verificaTarefa(){
   		Thread me = Thread.currentThread();
   		boolean t = bb.hasTarefa();
   		
   		if( t ){
   			out.display.append("Agente :"+ me.getName() + 
   				" encontrou tarefa\n" );
   			bb.assinaTarefa(this);
   		}
   		
   		return t;
   }
   
   public boolean aguardaInicioTarefa(){
   		Thread me = Thread.currentThread();
   		while( me == thread ){
   			if( bb.inicioTarefa() )
   				break;
   			
   			try{
   				Thread.sleep( 700 );
   			}catch( InterruptedException e ){}
   		}
   		return true;
   }   
   
   private void ExecutarTarefa(){
   		Thread me = Thread.currentThread();
   		while( (me == thread) && bb.hasTarefa() ){
   			out.display.append("Agente :"+ me.getName() + 
   				" disputando tarefa " + idAtividade.getNome() + "\n" );
   			
   			try{
   				Thread.sleep( 1500 );
   			}catch( InterruptedException e ){}
   		}
   }   
   
   public void sairDaCasa(){
   		stop();
   }
   
   public void stop(){
   		//Thread curr = Thread.currentThread();
   		out.display.append(thread.getName() + " saindo da casa...\n");
   		thread = null;
   }
}
