/**
 *  1. Publicar para todos os participantes que eles devem realizar essa Tarefa
 *  2. Aguardar que todos os participantes assinem essa publicação
 *  3. Definir probabilisticamente o vencedor da Tarefa, de acordo com as características da Atividade e dos Participantes
 *  
 *  idAtividade = new Atividade ('Jogar Pin-Bolin', // String pstrNome
 *                               00,                // int pintPeso
 *                               00,                // int pintAltura
 *                               05,                // int pintInteligencia
 *                               00,                // int pintAgilidade   
 *                               05,                // int pintDeterminacao
 *                               01,                // int pintConhecGerais
 *                               00,                // int pintIdade
 *                               02,                // int pintForca
 *                               06,                // int pintEsperteza
 *                               00);               // int pintSexo
 *  idVencedor = agentTarefa.ExecutarTarefa(idAtividade); // Sem Cast
 *
 */
import java.util.*;
//import javax.swing.*;


public class agentTarefa extends Thread {
	private BigBrother out;
	private BlackBoard bb;
	private Atividade idAtv;
	private Participante vencedor;
	private Timer timer;
    /**
	 *  Método Construtor
	 */
	public agentTarefa(BigBrother big, BlackBoard mem) {
		out = big;
		bb = mem;
	}
	
	public void run(){
		Thread me = Thread.currentThread();
		
		while( me == Thread.currentThread() ){
			try{
				Thread.sleep(5000);
			}catch( InterruptedException e ){}
		}
		out.display.append("me != current - jogador\n");
	}
        
    /**
     *  1. Publicar para todos os participantes que eles devem realizar essa Tarefa
     */
	public void publicaTarefa(int tarefa) {
       bb.publicaTarefa(tarefa);
       
       aguardaAssinantes();	
	}
    
    public void stopTarefa(){
    	bb.stopTarefa();
    	idAtv = bb.getTarefa();
    	vencedor = DefinirVencedor( idAtv );
    	out.display.append("Nome do vencedor :" + vencedor.getNome() + "\n");
    	bb.setVencedorTarefa( vencedor );
    }
    	 
    /**
     *  2. Aguardar que todos os participantes assinem essa publicação
     */
	public void aguardaAssinantes() {
       /**
        *  Esperar por um tempo X....
        */
       timer = new Timer();
       timer.schedule( new MeuTimerTask(1), 7000, 7000 );
	}
	

    class MeuTimerTask extends TimerTask {
    	int total;
    	
    	public MeuTimerTask( int n ){
    		total = n;
    	}
    	
        public void run() {
        	if( total > 0 ){
            System.out.println("Inicia a tarefa");
            bb.startTarefa();
            out.fimTarefaItem.setEnabled(true);
            total--;
        	}else
        		timer.cancel();
        }
    }
    
    public void escolheParticipante( String nome ){
    	bb.setEscolhidoParedao( nome );
    }
    
    public Participante verificaGanhadorJogo(){
    	return bb.isGameOver();
    }
    
    public void reinicializaJogo(){
    	bb.stopParticipantes();
    	bb.clear();
    }
    
    /**
     *  3. Definir probabilisticamente o vencedor da Tarefa, de acordo com as características da Atividade e dos Participantes
     */
    private Participante DefinirVencedor(Atividade pobjAtividade) {
       float vfltProbabilidade,vfltMaiorProb;
       int vqtdCaracteristicas, vintIndice, vintVlrAtiv, vintVlrPartic;
       Participante vobjVencedor,vobjParticipante;
       Float vfltAuxiliar;
       Vector vetProbabilidade = new Vector (10,05);
       
       
       /**
        *  Assume que ainda não há vencedor para a Tarefa
        */
       vobjVencedor  = null;
       vfltMaiorProb = 00;
       
       
       /**
        *  Define a probabilidade de cada participante vencer a Tarefa
        */
       for (vintIndice = 00; vintIndice < bb.totalAssinantes(); vintIndice++) {
          vobjParticipante    = (Participante)  bb.getAssinante(vintIndice); // Cast
          vfltProbabilidade   = 00;
          vqtdCaracteristicas = 00;
          
          
          /**
          *  Característica "Peso"
          */
          vintVlrAtiv   = pobjAtividade.getPeso();
          vintVlrPartic = vobjParticipante.getPeso();
          if (vintVlrAtiv > 00)
          {
             vqtdCaracteristicas++;
             vfltProbabilidade = vfltProbabilidade + (vintVlrAtiv / vintVlrPartic) - 01;
          } // if (vintVlrAtiv > 00)
          
          
          
          /**
          *  Característica "Altura"
          */
          vintVlrAtiv   = pobjAtividade.getAltura();
          vintVlrPartic = vobjParticipante.getAltura();
          if (vintVlrAtiv > 00)
          {
             vqtdCaracteristicas++;
             vfltProbabilidade = vfltProbabilidade + (vintVlrAtiv / vintVlrPartic) - 01;
          } // if (vintVlrAtiv > 00)
          
          
          /**
          *  Característica "Inteligencia"
          */
          vintVlrAtiv   = pobjAtividade.getInteligencia();
          vintVlrPartic = vobjParticipante.getInteligencia();
          if (vintVlrAtiv > 00)
          {
             vqtdCaracteristicas++;
             vfltProbabilidade = vfltProbabilidade + (vintVlrAtiv / vintVlrPartic) - 01;
          } // if (vintVlrAtiv > 00)
          
          
          /**
          *  Característica "Agilidade"
          */
          vintVlrAtiv   = pobjAtividade.getAgilidade();
          vintVlrPartic = vobjParticipante.getAgilidade();
          if (vintVlrAtiv > 00)
          {
             vqtdCaracteristicas++;
             vfltProbabilidade = vfltProbabilidade + (vintVlrAtiv / vintVlrPartic) - 01;
          } // if (vintVlrAtiv > 00)
          
          
          /**
          *  Característica "Determinacao"
          */
          vintVlrAtiv   = pobjAtividade.getDeterminacao();
          vintVlrPartic = vobjParticipante.getDeterminacao();
          if (vintVlrAtiv > 00)
          {
             vqtdCaracteristicas++;
             vfltProbabilidade = vfltProbabilidade + (vintVlrAtiv / vintVlrPartic) - 01;
          } // if (vintVlrAtiv > 00)
          
          
          /**
          *  Característica "ConhecGerais"
          */
          vintVlrAtiv   = pobjAtividade.getConhecGerais();
          vintVlrPartic = vobjParticipante.getConhecGerais();
          if (vintVlrAtiv > 00)
          {
             vqtdCaracteristicas++;
             vfltProbabilidade = vfltProbabilidade + (vintVlrAtiv / vintVlrPartic) - 01;
          } // if (vintVlrAtiv > 00)
          
          
          /**
          *  Característica "Idade"
          */
          vintVlrAtiv   = pobjAtividade.getIdade();
          vintVlrPartic = vobjParticipante.getIdade();
          if (vintVlrAtiv > 00)
          {
             vqtdCaracteristicas++;
             vfltProbabilidade = vfltProbabilidade + (vintVlrAtiv / vintVlrPartic) - 01;
          } // if (vintVlrAtiv > 00)
          
          
          /**
          *  Característica "Forca"
          */
          vintVlrAtiv   = pobjAtividade.getForca();
          vintVlrPartic = vobjParticipante.getForca();
          if (vintVlrAtiv > 00)
          {
             vqtdCaracteristicas++;
             vfltProbabilidade = vfltProbabilidade + (vintVlrAtiv / vintVlrPartic) - 01;
          } // if (vintVlrAtiv > 00)
          
          
          /**
          *  Característica "Esperteza"
          */
          vintVlrAtiv   = pobjAtividade.getEsperteza();
          vintVlrPartic = vobjParticipante.getEsperteza();
          if (vintVlrAtiv > 00)
          {
             vqtdCaracteristicas++;
             vfltProbabilidade = vfltProbabilidade + (vintVlrAtiv / vintVlrPartic) - 01;
          } // if (vintVlrAtiv > 00)
          
          
          /**
          *  Característica "Sexo"
          */
          vintVlrAtiv   = pobjAtividade.getSexo();
          vintVlrPartic = vobjParticipante.getSexo();
          if (vintVlrAtiv > 00)
          {
             vqtdCaracteristicas++;
             
             if (vintVlrAtiv == vintVlrPartic)
                vfltProbabilidade = vfltProbabilidade + (0.5f); /* 50% de Diferencial Competitivo */
          } // if (vintVlrAtiv > 00)
          
          
          /**
          *  "Diferencial Competitivo" deste Participante - Probabilidade de Vitória
          */
          vfltProbabilidade = vfltProbabilidade / vqtdCaracteristicas;
          
          
          /**
          *  Armazena num vetor relacionado a probabilidade de cada participante
          */
          vfltAuxiliar = new Float (vfltProbabilidade);
          vetProbabilidade.addElement (vfltAuxiliar);
          
          
          /**
          *  Define o vencedor de acordo com a Maior probabilidade
          *  Em caso de empate, o que assinou primeiro a tarefa vencerá!!!!!
          */
          if (vfltMaiorProb == 00 || vfltProbabilidade > vfltMaiorProb)
             vobjVencedor = vobjParticipante;
          
       } // for (vintIndice = 00; vintIndice < vetAssinantes.size(); vintIndice++) {
       
       return vobjVencedor;
	} // private Object DefinirVencedor(Object pobjAtividade) {
}

