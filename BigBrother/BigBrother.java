import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class BigBrother extends JFrame{
	private JTextArea display;
	private JPanel p1;
	
	public BigBrother(){
		super("BigBrother");
		
		Container c = getContentPane();
		p1 = new JPanel();
		p1.setLayout(new BorderLayout());
		
		JMenuBar bar = new JMenuBar(); //cria a barra de menus
		setJMenuBar( bar );
		
		JMenu fileMenu = new JMenu( "Arquivo" );
		fileMenu.setMnemonic( 'A' );
		
		JMenuItem abrirItem = new JMenuItem("Abrir");
		fileMenu.add( abrirItem );
		
		JMenuItem exitItem = new JMenuItem( "Sair" );
		exitItem.setMnemonic('r');
		exitItem.addActionListener(
			new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					System.exit( 0 );
				}
			}
		);
		
		fileMenu.add( exitItem );
		bar.add( fileMenu );
		
		JMenu editMenu = new JMenu("Editar");
		editMenu.setMnemonic('E');
		
		JMenu procMenu = new JMenu("Procurar");
		JMenuItem t1 = new JMenuItem("Teste1");
		JMenuItem t2 = new JMenuItem("Teste2");
		procMenu.add(t1);
		procMenu.add(t2);
		editMenu.add(procMenu);
		
		bar.add(editMenu);
		
		JMenu ajudaMenu = new JMenu("Ajuda");
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
		
		display = new JTextArea();
		display.setEnabled(false);
		p1.add( new JScrollPane( display ) );
		c.add( p1, BorderLayout.CENTER );

				
		setSize( 800, 550 );
		show();
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
}