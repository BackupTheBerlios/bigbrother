import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Vector;
import java.util.Random;

public class BigBrother extends JFrame{
	public final int MAX_PARTICIPANTES = 10;
	public final int MIN_PARTICIPANTES = 4;
	public JTextArea display;
	private JPanel p1;
	private JPanel p2, p3, p4, p5, p6, p7;
	private JPanel p8, p9;
	private JPanel[] panelParticipantes;
	public JLabel lblTarefa, lblParedao, lblVoto, lblSorteio, lblVencedor;
	public JLabel lblNomeTarefa, lblEscolhido, lblSorteado, lblNomeVencedor;
	public Participante participantes[];
	private int numParticipantes;
	private agentTarefa jogador;
	private BlackBoard bb;
	private Vector vAgentes;
	private Container c;
	private JLabel lblParticipantes;
	public JLabel[] lblpart;
	public JMenuItem iniTarefaItem;
	public JMenuItem fimTarefaItem;
	public JMenuItem criarItem;
	public JMenuItem escolheItem;
	public JMenuItem sorteioItem;
	public JMenuItem eliminaItem;
	public JMenuItem reiniciarItem;
	
	public BigBrother(){
		super("BigBrother");
		
		c = getContentPane();
		c.setLayout( new BorderLayout() );
		p1 = new JPanel();
		p1.setLayout(new BorderLayout());
		p8 = new JPanel();
		p8.setLayout( new GridLayout(1,3) );
		p2 = new JPanel();
		p2.setLayout(new GridLayout(1,2) );
		p3 = new JPanel();
		p3.setLayout(new GridLayout(3,1) );
		p4 = new JPanel();
		p4.setLayout(new GridLayout(1,2) );
		p5 = new JPanel();
		p5.setLayout(new FlowLayout() );
		p6 = new JPanel();
		p6.setLayout(new FlowLayout(FlowLayout.CENTER) );
		p7 = new JPanel();
		p7.setLayout(new FlowLayout() );
		p9 = new JPanel();
		p9.setLayout(new GridLayout(11,1) );
		
		
		numParticipantes = 0;

		criaMenu(this);
		criaLabels();
		
		display = new JTextArea();
		display.setEnabled(true);
		p1.add( new JScrollPane( display ) );
		//c.add( p1, BorderLayout.CENTER );
		c.add( p1, BorderLayout.CENTER );

		criaColunaParticipantes();
		
		vAgentes = new Vector();
		
		participantes = new Participante[MAX_PARTICIPANTES];
		System.out.println("Criando BlackBoard...");
		bb = new BlackBoard( vAgentes );
		
		jogador = new agentTarefa(this,bb);
		jogador.start();
		
		System.out.println("Inicializando GUI");
				
		
		setSize( 800, 550 );
		setLocationRelativeTo(null);
		this.setExtendedState(this.MAXIMIZED_BOTH);
		show();
		//janCriar j = new janCriar(this,true);
		
	}
	
	public void criaLabels(){
		lblTarefa		= new JLabel( "Tarefa :" );
		lblParedao		= new JLabel( "Paredão" );
		lblVoto			= new JLabel( "Escolhido :" );
		lblSorteio		= new JLabel( "Sorteado :" );
		lblVencedor		= new JLabel( "Vencedor :" );
		lblNomeTarefa	= new JLabel( "" );
		lblEscolhido	= new JLabel( "" );
		lblSorteado		= new JLabel( "" );
		lblNomeVencedor	= new JLabel( "" );
		
		p2.add( lblTarefa );
		p2.add( lblNomeTarefa );
		p7.add( lblParedao );
		p5.add( lblVoto );
		p5.add( lblEscolhido );
		p6.add( lblSorteio );
		p6.add( lblSorteado );
		p4.add( lblVencedor );
		p4.add( lblNomeVencedor );
		
		p3.add( p7 );
		p3.add( p5 );
		p3.add( p6 );
		p8.add( p2 );
		p8.add( p3);
		p8.add( p4);
		c.add( p8, BorderLayout.NORTH );
	}
		
	private void criaColunaParticipantes(){
		int i;
		
		panelParticipantes = new JPanel[MAX_PARTICIPANTES];
		lblpart = new JLabel[MAX_PARTICIPANTES];
		lblParticipantes = new JLabel(" Participantes ");
		p9.add( lblParticipantes );
		
		for( i = 0; i < MAX_PARTICIPANTES; i++ ){
			panelParticipantes[i] = new JPanel();
			panelParticipantes[i].setLayout(new FlowLayout(FlowLayout.CENTER));
			lblpart[i] = new JLabel("");
			panelParticipantes[i].add( lblpart[i] );
			p9.add( panelParticipantes[i] );
		}
		
		c.add( p9, BorderLayout.EAST);
	}
	
	public void criaMenu(BigBrother p){
		final janCriar j;
		final IniTarefa ini;
		final EscolheParticipante escolhe = new EscolheParticipante( this, true );
		
		JMenuBar bar = new JMenuBar(); //cria a barra de menus
		setJMenuBar( bar );
		
		JMenu iniMenu = new JMenu( "Iniciar" );
		iniMenu.setMnemonic( 'I' );
		
		reiniciarItem = new JMenuItem("Reiniciar o Jogo");
		reiniciarItem.addActionListener(
			new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					limpaScreen();
					limpaNomeParticipantes();
					bb.stopParticipantes();
					bb.clear();
					criarItem.setEnabled(true);
					iniTarefaItem.setEnabled(false);
					fimTarefaItem.setEnabled(false);
					escolheItem.setEnabled(false);
					sorteioItem.setEnabled(false);
					eliminaItem.setEnabled(false);	
				}
			}
		);
		iniMenu.add( reiniciarItem );
		
		j = new janCriar(this,true);
		criarItem = new JMenuItem("Criar Participantes");
		criarItem.addActionListener(
			new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					j.show();	
				}
			}
		);
		
		/* Capturando evento de fechamento do dialogo */
		j.addWindowListener(
			new WindowAdapter(){
				public void windowClosing( WindowEvent e ){
					if( numParticipantes >= MIN_PARTICIPANTES ){
						j.dispose();
					}else{
						String s = "Você precisa criar no mínimo " +
							MIN_PARTICIPANTES + " participantes";
						JOptionPane.showMessageDialog(null,s,
							"Erro",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		);
		
		iniMenu.add( criarItem );
		
		JMenuItem exitItem = new JMenuItem( "Sair" );
		exitItem.setMnemonic('r');
		exitItem.addActionListener(
			new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					bb.stopParticipantes();
					jogador = null;
					System.exit(0);
				}
			}
		);
		
		iniMenu.add( exitItem );
		bar.add( iniMenu );
		
		JMenu tarefaMenu = new JMenu("Tarefa");
		tarefaMenu.setMnemonic('T');
		ini = new IniTarefa( this, true );
		
		iniTarefaItem = new JMenuItem("Iniciar Tarefa");
		iniTarefaItem.setEnabled(false);
		iniTarefaItem.addActionListener(
			new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					ini.show();
					jogador.publicaTarefa(ini.tarefaSelecionada());
					//try{
					//	Thread.sleep(5000);
					//}catch( InterruptedException i ){}
				}
			}
		);
		
		fimTarefaItem = new JMenuItem("Finalizar Tarefa");
		fimTarefaItem.setEnabled(false);
		fimTarefaItem.addActionListener(
			new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					jogador.stopTarefa();
					lblNomeVencedor.setForeground(Color.blue);
					lblNomeVencedor.setText(bb.getNomeVencedorTarefa());
					escolheItem.setEnabled(true);
					sorteioItem.setEnabled(true);
					fimTarefaItem.setEnabled(false);
				}
			}
		);
		tarefaMenu.add(iniTarefaItem);
		tarefaMenu.add(fimTarefaItem);
		bar.add(tarefaMenu);
		
		JMenu paredaoMenu = new JMenu("Paredão");
		paredaoMenu.setMnemonic('P');
		
		escolheItem = new JMenuItem("Escolher Participante");
		escolheItem.setEnabled(false);
		sorteioItem = new JMenuItem("Sortear Participante");
		sorteioItem.setEnabled(false);
		eliminaItem = new JMenuItem("Eliminar Participante");
		eliminaItem.setEnabled(false);
		
		/* Escolhe um participante */
		paredaoMenu.add(escolheItem);
		escolheItem.addActionListener(
			new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					//jogador.escolheParticipante( "tu" );
					escolhe.adicionaNomes();
					escolhe.show();
				}
			}
		);
		paredaoMenu.add(sorteioItem);
		
		/* Executa o sorteio de um participante para o paredão */
		sorteioItem.addActionListener(
			new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					lblSorteado.setText(bb.setSorteadoParedao());
					if( !escolheItem.isEnabled() )
						eliminaItem.setEnabled(true);
						sorteioItem.setEnabled(false);
				}
			}
		);
		
		/* Eliminando um participante */
		paredaoMenu.add(eliminaItem);
		eliminaItem.addActionListener(
			new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					String eliminado = bb.eliminaParticipante();
					String msg = "O participante \"" + eliminado +
						"\" foi eliminado da casa !!!";
					JOptionPane.showMessageDialog(null,msg,
						"Participante eliminado",JOptionPane.INFORMATION_MESSAGE);
					limpaScreen();
					limpaNomeEliminado( eliminado );
					if( bb.totalParticipantes() == 2 ){
						iniTarefaItem.setEnabled(false);
						fimTarefaItem.setEnabled(false);
						escolheItem.setEnabled(false);
						sorteioItem.setEnabled(false);
						bb.ultimoParedao();
					}
					
					if( bb.totalParticipantes() > 2 ){
						eliminaItem.setEnabled(false);
						iniTarefaItem.setEnabled(true);
					}
					
					Participante p = jogador.verificaGanhadorJogo();
					if( p != null ){
						msg = "O grande vencedor foi o participante - " +
							p.getNome();
						JOptionPane.showMessageDialog(null,
							msg,"Final de jogo",JOptionPane.INFORMATION_MESSAGE);
						jogador.reinicializaJogo();
						eliminaItem.setEnabled(false);
					}
				}
			}
		);
		bar.add(paredaoMenu);
		
		JMenu ajudaMenu = new JMenu("Ajuda");
		ajudaMenu.setMnemonic('A');
		JMenuItem aboutItem = new JMenuItem( "Sobre..." );
		aboutItem.setMnemonic( 'S' );
		
		aboutItem.addActionListener(
			new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					JOptionPane.showMessageDialog( BigBrother.this,
					"Projeto BigBrother","Sobre", 
					JOptionPane.PLAIN_MESSAGE );
				}
			}
		);
		
		ajudaMenu.add(aboutItem);
		bar.add(ajudaMenu);
	}
	
	private void limpaScreen(){
		lblNomeTarefa.setText("");
		lblEscolhido.setText("");
		lblSorteado.setText("");
		lblNomeVencedor.setText("");
		display.setText("");
	}
	
	private void limpaNomeParticipantes(){
		for( int i = 0; i < MAX_PARTICIPANTES; i++ ){
			lblpart[i].setText("");
		}
	}
	
	private void limpaNomeEliminado( String e ){
		for( int i = 0; i < MAX_PARTICIPANTES; i++ ){
			if( lblpart[i].getText().compareTo(e) == 0 ){
				lblpart[i].setText("");
				return;
			}	
		}
	}
	
	public boolean criaParticipante( String nome, int idade, int sexo){
		if( numParticipantes >= MAX_PARTICIPANTES ){
			JOptionPane.showMessageDialog(null,
				"Numero máximo de participantes atingido",
				"Limite excedido",JOptionPane.INFORMATION_MESSAGE);
			return false;
		}else{
			participantes[numParticipantes] = new Participante( bb, 
					nome, geraValorAleatorio(40,140),	//peso
					geraValorAleatorio(130,220),		//altura
					geraValorAleatorio(30,190),			//inteligencia 
					geraValorAleatorio(2,10),			//Agilidade
					geraValorAleatorio(2,10),			//Determinacao
					geraValorAleatorio(2,10),			//Conhecimentos Gerais
					idade,								//Idade
					geraValorAleatorio(2,10),			//Força
					geraValorAleatorio(2,10),			//Esperteza
					sexo,									//Sexo
					this );
											
			//participantes[1] = new Participante( bb, "Agente2", 65, 165, 135, 8,
			//							7, 7, 23, 8, 8, 1, this );
											
			bb.addAgente( participantes[numParticipantes] );
		
			//System.out.println("Inicializando agentes - etapa 2...");
			participantes[numParticipantes].start(nome);
			lblpart[numParticipantes].setText( nome );
			numParticipantes++;
			return true;
		}
	}
	
	private int geraValorAleatorio( int min, int max ){
		int ret;
		Random r = new Random();
		
		ret = (Math.abs(r.nextInt()) % max) + 1;
		if( ret < min )
			ret = min;
			
		return ret;
	}
	
	public static void main( String args[] ){
		BigBrother app = new BigBrother();
		
		app.addWindowListener(
			new WindowAdapter(){
				public void windowClosing( WindowEvent e ){
					System.exit( 0 );
				}
			}
		);
	}
	
	class janCriar extends JDialog implements ActionListener {
		private JTextField txtNome, txtIdade;
		private JButton btnCriar;
		private JComboBox cmbSexo;
		private JLabel lblNome, lblIdade, lblSexo;
		private String[] optSexo = { "M", "F" };
		private String[] sexo = { "Masculino", "Feminino" };
		private JPanel p1,p2,p3, p4;
		public janCriar( JFrame pai, boolean modal ){
			super( pai, modal );
			
			Container c = getContentPane();
			c.setLayout( new GridLayout(4,1) );
			p1 = new JPanel();
			p1.setLayout(new FlowLayout(FlowLayout.LEFT));
			p2 = new JPanel();
			p2.setLayout(new FlowLayout(FlowLayout.LEFT));
			p3 = new JPanel();
			p3.setLayout(new FlowLayout(FlowLayout.LEFT));
			p4 = new JPanel();
			p4.setLayout(new FlowLayout(FlowLayout.CENTER));
			
			lblNome = new JLabel( "Nome :" );
			p1.add( lblNome );
			
			txtNome = new JTextField(15);
			p1.add( txtNome );
			c.add( p1 );
			
			lblIdade = new JLabel( "Idade :" );
			p2.add( lblIdade );
			txtIdade = new JTextField(10);
			p2.add( txtIdade );
			c.add( p2 );
			
			lblSexo = new JLabel( "Sexo :" );
			p3.add( lblSexo );
			cmbSexo = new JComboBox( sexo );
			p3.add( cmbSexo );
			c.add( p3 );
			
			btnCriar = new JButton( "Criar" );
			p4.add( btnCriar );
			c.add( p4 );
			btnCriar.addActionListener(this);
			
			setSize( 300, 160 );
			setLocationRelativeTo(null);
			setResizable(false);
			setTitle("Cria participante");
			this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
			//show();
		}
		
		public void actionPerformed( ActionEvent e ){
			if( !consisteCampos() ){
				JOptionPane.showMessageDialog(this,
						"Todos os campos devem ser preenchidos!",
						"Aviso",JOptionPane.ERROR_MESSAGE);
				txtNome.grabFocus();
			}else{
				criaParticipante( txtNome.getText(),
					Integer.parseInt(txtIdade.getText()),
					cmbSexo.getSelectedIndex() );
				txtNome.setText("");
				txtIdade.setText("");
				txtNome.grabFocus();
				iniTarefaItem.setEnabled(true);
				criarItem.setEnabled(false);
			}
		}
		
		private boolean consisteCampos(){
			if( txtNome.getText().length() == 0 ||
				txtIdade.getText().length() == 0 )
				return false;
			else
				return true;
		}	
	} /* class janCriar */
	
	class IniTarefa extends JDialog implements ActionListener {
		private JRadioButton radCorrida, radComer, radCalculo;
		private ButtonGroup radGroup;
		private JButton btnIniciar;
		private JPanel p1,p2;
		private int tarefa;
		public IniTarefa( JFrame pai, boolean modal ){
			super( pai, modal );
			
			Container c = getContentPane();
			c.setLayout( new FlowLayout() );
			p1 = new JPanel();
			p1.setLayout(new FlowLayout());
			p2 = new JPanel();
			p2.setLayout(new FlowLayout(FlowLayout.CENTER));
			
			radCorrida = new JRadioButton( "Corrida", true );
			p1.add( radCorrida );
			radComer = new JRadioButton( "Comer", false );
			p1.add( radComer );
			radCalculo = new JRadioButton( "Calculo", false );
			p1.add( radCalculo );
			
			radGroup = new ButtonGroup();
			radGroup.add( radCorrida );
			radGroup.add( radComer );
			radGroup.add( radCalculo );
			
			c.add( p1 );
			
			btnIniciar = new JButton( "Iniciar" );
			p2.add( btnIniciar );
			btnIniciar.addActionListener(this);
			c.add( p2 );
			
			setSize( 250, 140 );
			setLocationRelativeTo(null);
			setResizable(false);
			setTitle("Iniciar tarefa");
		}
		
		public void actionPerformed( ActionEvent e ){
			if( radCorrida.isSelected() ){
				JOptionPane.showMessageDialog(this,"Iniciando tarefa Corrida",
					"Mensagem",JOptionPane.INFORMATION_MESSAGE);
				setTarefa(bb.CORRIDA);
				lblNomeTarefa.setText("Corrida");
			}else if( radComer.isSelected() ){
				JOptionPane.showMessageDialog(this,"Iniciando tarefa da Comida",
					"Mensagem",JOptionPane.INFORMATION_MESSAGE);
				setTarefa(bb.COMER);
				lblNomeTarefa.setText("Comer");
			}else if( radCalculo.isSelected() ){
				JOptionPane.showMessageDialog(this,"Iniciando tarefa de Calculo",
					"Mensagem",JOptionPane.INFORMATION_MESSAGE);
				setTarefa(bb.CALCULO);
				lblNomeTarefa.setText("Calcular");
			}else
				JOptionPane.showMessageDialog(this,"Nenhum foi selecionado",
					"Mensagem",JOptionPane.INFORMATION_MESSAGE);
			
			//fimTarefaItem.setEnabled(true);
			iniTarefaItem.setEnabled(false);
			setVisible(false);
		}
		
		private void setTarefa( int t ){
			tarefa = t;
		}
		
		public int tarefaSelecionada(){
			return tarefa;
		}
	} /* Fim classe IniTarefa */
		
	class EscolheParticipante extends JDialog implements ActionListener {
		private JComboBox cmbParticipantes;
		private JLabel lblParticipantes;
		private JButton btnEscolher;
		private String[] nomes;
		private String[] sel = {"Selecione"};
		private JPanel p1,p2;
		public EscolheParticipante( JFrame pai, boolean modal ){
			super( pai, modal );
			
			Container c = getContentPane();
			c.setLayout( new FlowLayout() );
			p1 = new JPanel();
			p1.setLayout(new FlowLayout());
			p2 = new JPanel();
			p2.setLayout(new FlowLayout(FlowLayout.CENTER));
			
			lblParticipantes = new JLabel("Participantes :");
			p1.add( lblParticipantes );

			cmbParticipantes = new JComboBox( sel );
			p1.add( cmbParticipantes );			
			c.add( p1 );
			
			btnEscolher = new JButton( "Escolher" );
			p2.add( btnEscolher );
			btnEscolher.addActionListener(this);
			c.add( p2 );
			
			setSize( 250, 140 );
			setLocationRelativeTo(null);
			setResizable(false);
			setTitle("Escolher Participante");
			this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		}
		
		public void adicionaNomes(){
			cmbParticipantes.removeAllItems();
			cmbParticipantes.addItem( "Selecione" );
			nomes = bb.getParticipanteNome();
			for( int i = 0; i < nomes.length; i++ ){
				cmbParticipantes.addItem( nomes[i] );
			}
		}
		
		public void actionPerformed( ActionEvent e ){
			if( cmbParticipantes.getSelectedIndex() == 0 ){
				JOptionPane.showMessageDialog(this,"Selecione um participante válido!!!",
					"Mensagem",JOptionPane.ERROR_MESSAGE);
				return;
			}
			if( bb.setEscolhidoParedao( nomes[cmbParticipantes.getSelectedIndex()-1] ) == -2 ){
				JOptionPane.showMessageDialog(this,"Este participante tem imunidade",
					"Mensagem",JOptionPane.INFORMATION_MESSAGE);
				return;
			}else if( bb.setEscolhidoParedao( nomes[cmbParticipantes.getSelectedIndex()-1] ) == -1 ){
				JOptionPane.showMessageDialog(this,"Erro escolhendo participante",
					"Mensagem",JOptionPane.ERROR_MESSAGE);
				return;
			}else if( bb.setEscolhidoParedao( nomes[cmbParticipantes.getSelectedIndex()-1] ) == -3 ){
				JOptionPane.showMessageDialog(this,"O participante já está no paredão",
					"Mensagem",JOptionPane.ERROR_MESSAGE);
				return;
			}else if( bb.setEscolhidoParedao( nomes[cmbParticipantes.getSelectedIndex()-1] ) == 0 ){
				String str = "O participante " + nomes[cmbParticipantes.getSelectedIndex()-1] +
					" está no paredão";
				JOptionPane.showMessageDialog(this,str,
					"Mensagem",JOptionPane.INFORMATION_MESSAGE);
				lblEscolhido.setText(nomes[cmbParticipantes.getSelectedIndex()-1]);
			}
			if( !sorteioItem.isEnabled() )
				eliminaItem.setEnabled(true);
				
			escolheItem.setEnabled(false);
			setVisible(false);
		}
	} /* Fim classe EscolheParticipante */
}