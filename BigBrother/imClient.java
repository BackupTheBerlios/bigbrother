import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class imClient extends JFrame {
	private JTextField enter;
	private JTextArea display;
   	private JComboBox combo;
   	private JButton sair;
   	private JPanel painel1, painel2, painel3;
   	Socket client;
   	ObjectOutputStream output;
   	ObjectInputStream input;
   	String aux = "";
   	IMSClientProtocol prot = new IMSClientProtocol();
   	private static final String version = "IMS/1.0";
   	String message = "";
   	private String user;
   	private String endServer;
   	private String destino;
   	private String selecione[] = {"selecione"};

   	public imClient(String usr, String address)
   	{
      	super( "imClient - " + usr);

	  	user = usr;
	  	endServer = address;
	  	int item;
      	Container c = getContentPane();
      	painel1 = new JPanel();
      	painel2 = new JPanel();
      	painel3 = new JPanel();
		
	  	painel1.setLayout(new FlowLayout());
	  	painel2.setLayout( new BorderLayout());
	  	painel3.setLayout( new FlowLayout() );
	  
      	enter = new JTextField(20);
      	enter.setEnabled( false );
      	enter.addActionListener(
        new ActionListener() {
        	public void actionPerformed( ActionEvent e )
        	{
        		sendData( e.getActionCommand() );
            	enter.setText("");
        	}
        } );
        
      	painel1.add( enter );

	  	combo = new JComboBox( selecione );
      	combo.setMaximumRowCount(4);
      	painel1.add( combo );
	
		combo.addItemListener(
			new ItemListener(){
				public void itemStateChanged( ItemEvent e ){
					destino = (String) combo.getSelectedItem();
					System.out.println("destino = " + destino );
				}
			}
		);

      	display = new JTextArea();
      	painel2.add( new JScrollPane( display ));
      	
      	sair = new JButton( "Sair" );
      	painel3.add( sair );
	
		sair.addActionListener(
			new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					doLogout();
					closeAll();
					System.exit(0);
				}
			}
		);
      
      	c.add(painel1, BorderLayout.NORTH );
      	c.add(painel2, BorderLayout.CENTER );
      	c.add(painel3, BorderLayout.SOUTH );

      	setSize( 400, 200 );
 	  
      	show();
	}	

   	public void runClient() 
   	{
      	try {
        		/* Cria um socket e conecta ao servidor */
         		display.setText( "Attempting connection\n" );
         		client = new Socket( 
            	InetAddress.getByName( endServer ), 5000 );

         	display.append( "Connected to: " +
            client.getInetAddress().getHostName() );

         	// Get the input and output streams.
         	output = new ObjectOutputStream(
            	         client.getOutputStream() );
         	output.flush();
         	input = new ObjectInputStream(
                     	client.getInputStream() );

			if( !doLogin() ){
				closeAll();
				System.exit(0);
			}
			
         	enter.setEnabled( true );

			waitMessage();
			
         	

         	// Close connection.
         	display.append( "Closing connection.\n" );
         	output.close();
         	input.close();
         	client.close();
      	}
      	catch ( EOFException eof ) {
        	System.out.println( "Server terminated connection" );
      	}
      	catch ( IOException e ) {
        	e.printStackTrace();
        	System.exit(0);
      	}
   	}

	public void closeAll(){
		try{
			client.shutdownOutput();
			client.shutdownInput();
			output.close();
        	input.close();
        	client.close();
        }catch( IOException e ){
        	e.printStackTrace();
        	System.exit(0);
        }
    }
    
   	private void sendData( String s )
   	{
   		System.out.println("Destino = " + destino);
   		if( destino == null || destino.equals("selecione") || 
   			destino.equals("") ){
        	JOptionPane.showMessageDialog(null,"Selecione um usuario",
						"Aviso",JOptionPane.WARNING_MESSAGE );
			return;
		}
    	try {
        	message = s;
        	System.out.println("Destino = " + destino );
        	s = prot.formatMessage(s,user,destino);
        	System.out.println("enviando:" + s );
         	output.writeObject( s );
         	output.flush();
         	display.append( "\n" + user + ">>" + message );
      	}
      	catch ( IOException cnfex ) {
        	display.append(
            "\nError writing object" );
      	}
   	}

	private void sendDados( String s )
   	{
    	try {
        	message = s;
         	output.writeObject( s );
         	output.flush();
      	}
      	catch ( IOException cnfex ) {
        	display.append(
            "\nError writing object" );
      	}
   	}
   	
	public boolean doLogin(){
		String msg = version + " LOGIN \nFrom:" + user + " \n";
		sendDados( msg );
		if( waitResponse("LOGIN") ){
			System.out.println("Recebeu OK" );
			putUsers( aux );
			return true;
		}else{
			JOptionPane.showMessageDialog(null,"Recebeu com erro" + aux,
						"Aviso",JOptionPane.ERROR_MESSAGE );
			//System.out.println("Recebeu com erro" );
			return false;
		}
	}
	
	public void doLogout(){
		String msg = version + " LOGOUT \nFrom:" + user + " \n";
		sendDados( msg );
		if( waitResponse("LOGOUT") ){
			System.out.println("Recebeu OK" );
			//putUsers( aux );
			return;
		}else{
			JOptionPane.showMessageDialog(null,"Recebeu com erro" + aux,
						"Aviso",JOptionPane.ERROR_MESSAGE );
			//System.out.println("Recebeu com erro" );
			return;
		}
	}
	
	public boolean waitResponse(String metodo){
		String recv;
		boolean rt = false;
		try{
			recv = (String) input.readObject();
			System.out.println("Recebido do servidor:" + recv );
			rt = prot.TrataResponse( recv, metodo );
			aux = prot.getMessage();
		} catch( ClassNotFoundException cnfex ){
			cnfex.printStackTrace();
			System.exit(0);
		}
		catch( IOException e ){
			e.printStackTrace();
			System.exit(0);
		}
		
		return rt;
	}
	
	public void waitMessage(){
		String recv;
		String resp;
		boolean ret;
		int retorno = -1;
		do {
			try {
				recv = (String) input.readObject();
				System.out.println("Recebido do servidor:" + recv );
				ret = prot.TrataServerRequest( recv );
				message = prot.getBody();
				retorno = prot.getStatus();
				switch( retorno ){
					case 0:
						addUser( message );
						break;
					case 1:
						delUser( message );
						break;
					case 3:
						continue;
					default:
						display.append( "\n" + prot.getCaller() +
							">>" + message );
						display.setCaretPosition(
							display.getText().length() );
				}
			} catch ( ClassNotFoundException cnfex ) {
				display.append( "\nUnknown object type received" );
			} catch( IOException e ){
				//System.out.println( "\nUnknown object type received" );
				//e.printStackTrace();
				break;
			}
		} while ( true );
	}
    
    public void addUser( String user ){
    	System.out.println("inserindo... " + user);
    	combo.addItem( user );
    }
    
    public void delUser( String user ){
    	System.out.println("deletando... " + user);
    	combo.removeItem(user);
    }
    
	public void putUsers( String users ){
		String temp;
		int tot;
		System.out.println("putUsers = " + users );
		StringTokenizer tokens = new StringTokenizer( users, " \n" );
		tot = tokens.countTokens();
		for( int i = 1; i <= tot; i++ ){
			temp = tokens.nextToken();
			System.out.println("[" + tot +"]inserindo... " + temp);
			combo.insertItemAt(temp,i);
		}
		
	}
	
	public static void main( String args[] )
   	{
   		String address = JOptionPane.showInputDialog("Digite o IP ou nome do servidor");
   		if( address == null || address.equals("") ){
   	  		System.out.println("Login cancelado");
   	  		System.exit(0);
   	  	}
   	  	
   		String usr = JOptionPane.showInputDialog("Digite seu nick");
   	  	if( usr == null || usr.equals("") ){
   	  		System.out.println("Login cancelado");
   	  		System.exit(0);
   	  	}
   	  
      	imClient app = new imClient(usr, address);

      	app.addWindowListener(
        	new WindowAdapter() {
            	public void windowClosing( WindowEvent e )
            	{
               		System.exit( 0 );
            	}
         	}
      	);

      	app.runClient();
   	}
}

class IMSClientProtocol{
	private static final int SUCCESS 		= 200,
			ERR_USERALREADY_CONNECTED		= 402,
			ERR_USER_NOT_LOGGED				= 403,
			ERR_USER_FATAL_ERROR			= 404,
			FATAL_SERVER_ERROR				= 500;
	
	private Vector Vheaders;
	private Vector Vvalues;
	private String metodo = "";
	private StringTokenizer tokens;
	private String fita = "";
	private String aux = "";
	private String response = "";
	private String resp = "";
	private String caller = "";
	private int ind;
	private int cod;
	private int status;
	private static final String version = "IMS/1.0";
	private String[] metodos = { "LOGIN", "SNDMSG","LOGOUT",
								 "USER-IN", "USER-OUT" };
	private String[] headers = { "From", "To" };
	
	
	public IMSClientProtocol( )
	{
		//super("IMSprotocol");
		Vheaders = new Vector();
		Vvalues  = new Vector();
	}
	
	public boolean TrataServerRequest( String recv )
	{
		fita = recv;
		ind = 0;
		boolean ret;
		Vheaders.removeAllElements();
		Vvalues.removeAllElements();
		
		if( !getFirstLine() ){
			ret = TrataResponse(recv,"SNDMSG");
			setStatus( 3 );
			return ret;
		}
		
		if( !getHeadersReq() )
			return false;
			
		if( !TrataMetodo() ){
			return false;
		}else{
			return true;
		}
				
	} //Fim de TrataServerRequest()
	
	public boolean TrataResponse(String recv, String metodo ){
		fita = recv;
		ind = 0;
		Vheaders.removeAllElements();
		Vvalues.removeAllElements();
		
		System.out.println("Entrou em TrataResponse()" );
		
		if( !getFirstLineResponse() ){
			System.out.println("Erro em getFirstLineResponse()" );
			return false;
		}
		
		if( metodo.equals("LOGIN") ){
			if( !getHeaders() ){
				System.out.println("Erro em getHeaders()" );
				return false;
			}
			
			if( !getUsers() ){
				System.out.println("Erro em getUsers()" );
				return false;
			}
		}
		
		if( metodo.equals("LOGOUT") ){
			return true;
		}
				
		return true;
	}
    
    public void setMessage(String resposta ){
    	resp = resposta;
    }
    
    public void setStatus( int i ){
    	status = i;
    }
    
    public String getCaller(){
    	return caller;
    }
    
    public int getStatus(){
    	return status;
    }
    
    public String getMessage(){
    	return resp;
    }
    
    public boolean TrataMetodo(){
    	if( metodo.equals("USER-IN") ) {
    		setStatus( 0 );
    		return true;
    	}else if( metodo.equals("SNDMSG") ){
    		setStatus( 2 );
    		return TrataMsg();
    	}else if( metodo.equals("USER-OUT") ){
    		setStatus( 1 );
    		return true;
    	}
    	setResponse( ERR_USER_FATAL_ERROR, "" );
    	return false;
    }
    
    public String getHeaderValue( String hd ) {
    	int i = Vheaders.indexOf(hd);
    	if( i >= 0 ){
    		System.out.println("Vvalues = " + (String) Vvalues.elementAt(i));
    		return (String) Vvalues.elementAt(i);
    	}
    	else
    		return null;
    }
    
    public String setResponse( final int cod, String msg ){
    	switch( cod )
    	{
    		case SUCCESS:
    			response = version+" "+cod+" OK\n\n" + msg + "\n";
    			break;
    		case ERR_USERALREADY_CONNECTED:
    			response = version+" "+cod+" Ja existe usuario\n";
    			break;
    		case ERR_USER_NOT_LOGGED:
    			response = "403 Usuario nao esta on-line";
    		case ERR_USER_FATAL_ERROR:
    			response = "404 Internal User Error";
    			break;
    		default:
    			response = "404 Internal User Error";
    	}
    	
    	return response;
    }
    
    public String formatMessage( String s, String user, String dest ){
    	return version + " SNDMSG \nFrom:"+user+"\nTo:"+dest+" \n\n"+s;
    }
    
    public boolean TrataMsg(){
    	String origem = getHeaderValue("From");
    	System.out.println("getHeaderValue From:user = " + origem );
    	caller = origem;
    	
    	String dest = getHeaderValue("To");
    	System.out.println("getHeaderValue To:user = " + dest );
    	//setResponse( SUCCESS, "" );
    	return true;
    }
    
	public boolean isIMMessage( String tok )
	{
		if( tok.equals( version ) ){
			System.out.println( "Protocol version OK ["+tok+"]");
			return true;
		}
		else{
			System.out.println( "Protocol version error["+tok+"]");
			setMessage( setResponse( FATAL_SERVER_ERROR, "" ) );
			return false;
		}
	}
	
	public boolean getHeaders()
	{
		while( ind < fita.length() ){
			int pos = fita.indexOf("\n",ind);
			if( pos < 0 ){
				System.out.println( "Nao encontrado CRLF");
				System.out.println( "Len fita = " + fita.length() +
									" ind = " + ind );
				setMessage( setResponse( FATAL_SERVER_ERROR, "" ) );
				return false;
			}
		
			aux = fita.substring(ind,pos).trim();
			System.out.println( "aux[" + aux + "]");
		
			tokens = new StringTokenizer( aux, ":\n" );
			if( tokens.countTokens() == 0 ){
				System.out.println( "Linha em branco encontrada !!!");
				incIndice( pos+1 );
				return true;
			}
			
			if( tokens.countTokens() >= 3 ){
				String temp = tokens.nextToken();
				if( findItem( headers, temp ) ){
					Vheaders.addElement( temp );
					temp = tokens.nextToken();
					System.out.println( "value = " + temp );
					Vvalues.addElement( temp );
					incIndice( pos+1 );
				}
				else
					return false;
			} else
				return false;
		} // Fim do laço
		
		return true;
	}
	
	public boolean getHeadersReq()
	{
		while( ind < fita.length() ){
			int pos = fita.indexOf("\n",ind);
			if( pos < 0 ){
				System.out.println( "Nao encontrado CRLF");
				System.out.println( "Len fita = " + fita.length() +
									" ind = " + ind );
				setMessage( setResponse( FATAL_SERVER_ERROR, "" ) );
				return false;
			}
		
			aux = fita.substring(ind,pos).trim();
			System.out.println( "aux[" + aux + "]");
		
			tokens = new StringTokenizer( aux, ":\n" );
			if( tokens.countTokens() == 0 ){
				System.out.println( "Linha em branco encontrada !!!");
				incIndice( pos+1 );
				return true;
			}
			
			if( tokens.countTokens() == 2 ){
				String temp = tokens.nextToken();
				if( findItem( headers, temp ) ){
					Vheaders.addElement( temp );
					temp = tokens.nextToken();
					System.out.println( "value = " + temp );
					Vvalues.addElement( temp );
					incIndice( pos+1 );
				}
				else
					return false;
			} else
				return false;
		} // Fim do laço
		
		return true;
	}
	
	public boolean getUsers(){
		while( ind < fita.length() ){
			int pos = fita.indexOf("\n",ind);
			if( pos < 0 ){
				System.out.println( "Nao encontrado CRLF");
				System.out.println( "Len fita = " + fita.length() +
									" ind = " + ind );
				//setMessage( setResponse( FATAL_SERVER_ERROR ) );
				return false;
			}
		
			aux = fita.substring(ind,pos);
			System.out.println( "aux[" + aux + "]");
			
			setMessage( aux );
			incIndice( pos+1 );
			break;
		} // Fim do laço
		return true;  
	}
	
	public boolean findItem( String vet[], String key )
	{
		for( int i = 0; i < vet.length; i++ ){
			if( key.equals( vet[i] ) ){
				System.out.println( "Chave encontrada[" + key + "]");
				return true;
			}
		}
		
		System.out.println( "Chave inexistente[" + key + "]");
		return false;
	}
	
	public boolean getFirstLine()
	{
		int pos = fita.indexOf("\n");
		if( pos < 0 ){
			System.out.println( "Nao encontrado CRLF");
			return false;
		}
		
		aux = fita.substring(ind,pos);
		System.out.println( "aux[" + aux + "]");
		
		tokens = new StringTokenizer( aux, " \n" );
		if( tokens.countTokens() == 2 ){
			if( isIMMessage( tokens.nextToken() ) ){
				String temp = tokens.nextToken();
				if( findItem( metodos, temp ) ){
					metodo = temp;
					incIndice( pos+1 );
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			System.out.println( "Tokens["+tokens.countTokens()+"]");
			return false;
		}
	}
	
	public boolean getFirstLineResponse()
	{
		int pos = fita.indexOf("\n");
		if( pos < 0 ){
			System.out.println( "Nao encontrado CRLF");
			setMessage( setResponse( FATAL_SERVER_ERROR, "" ) );
			return false;
		}
		
		aux = fita.substring(ind,pos);
		System.out.println( "aux[" + aux + "]");
		
		tokens = new StringTokenizer( aux, " \n" );
		if( tokens.countTokens() >= 3 ){
			if( isIMMessage( tokens.nextToken() ) ){
				String temp = tokens.nextToken();
				if( findResponse( Integer.parseInt(temp) ) ){
					cod = Integer.parseInt(temp);
					System.out.println( "codigo recebido:" + cod);
					incIndice( pos+1 );
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			System.out.println( "Tokens["+tokens.countTokens()+"]");
			return false;
		}
	}
	
	public boolean findResponse( int codigo ){
    	switch( codigo )
    	{
    		case SUCCESS:
    			//response = "200 OK";
    			return true;
    		case ERR_USERALREADY_CONNECTED:
    			//response = "402 Ja existe usuario";
    			return true;
    		case ERR_USER_NOT_LOGGED:
    			//response = "403 Usuario nao esta on-line";
    			return true;
    		case FATAL_SERVER_ERROR:
    			//response = "500 Internal Server Error";
    			return true;
    		default:
    			//response = "500 Internal Server Error";
    			setMessage( setResponse( FATAL_SERVER_ERROR, "" ) );
    			return false;
    	}
    }
    
	public String getBody()
	{
		if( ind < fita.length() ){
			aux = fita.substring(ind);
			System.out.println( "Body[" + aux + "]");
			return aux;
		}
		
		return null;
	}
		
	public void incIndice( int pos )
	{
		ind = pos;
	}
} //Fim da classe IMSProtocol
