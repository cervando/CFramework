package kmiddle2.log;

import kmiddle2.communications.fiels.Address;
import kmiddle2.util.IDHelper;

public class AreaRegistrer implements LogRegisterAble{
	
	Class<?> namer = null;
	
	
	public AreaRegistrer(){
		
	}
	
	public AreaRegistrer(Class<?> namer){
		this.namer = namer;
	}

	@Override
	public String header(int name, Address address) {
		String msg = 
			  "<start>"
				+ "<name>" + IDHelper.getNameAsString(namer, name) +"</name>\n"
				+ "<ID>" + name +"</ID>\n"
				+ "<address>\n"
					+ "\t<host>" + address.getIp() +"</host>\n"
					+ "\t<port>" + address.getPort()+"</port>\n"
				+ "</address>\n"
			+ "</start>";
		
		return msg;
	}

	@Override
	public String send(int name, String more) {
		return 
			"<SEND>"
				+ "<to>"+ IDHelper.getNameAsString(namer, name)+ "</to>"
				+ (more.equals("")?  
						"":
						"<data>"+more+"</data>")
			+ "</SEND>";
	}

	@Override
	public String send(Address addr, String more) {
		return "<SEND>"
					+ "<to>" + addr+"</to>"
					+ "<data>" + more+"</data>"
			+ "</SEND>";
	}
		
	@Override
	public String receive(int name, String more) {
		return "<RECEIVE> "
					+ "<from>" + IDHelper.getNameAsString(namer, name)+ "</from>" 
					+ (more.equals("")?  
							"":
							"<data>"+more+"</data>")
			+ "</RECEIVE>";
	}

	@Override
	public String receive(Address addr, String more) {
		return "<RECEIVE><from>"+addr+"</from>"+more+"</RECEIVE>";
	}
	
	@Override
	public String receive_debug(int name, String more) {
		return "<RECEIVE> "
				+ "<from> " + IDHelper.getNameAsString(namer, name) + "</from> "
				+ (more.equals("")?  
						"":
						"<data>"+more+"</data>")
			  +"</RECEIVE>";
	}
	
	

	@Override
	public String saveRequest(int name, String dataType) {
		return "<SAVE_REQUEST>"+""/*NodeNameHelper.getAreaName(name)*/+""+dataType+ "</SAVE_REQUEST>";
	}

	@Override
	public String error(String more) {
		return "<ERROR>"+more+"</ERROR>";
	}

	@Override
	public String info(String more) {
		return "<INFO>"+more+"</INFO>";
	}
	
	public String info(String more, int node) {
		// TODO Auto-generated method stub
		return "<INFO>" + more + IDHelper.getNameAsString(namer, node) + "</INFO>";
	}

	@Override
	public String developer(String more) {
		// TODO Auto-generated method stub
		return "<developer>" + more + "</developer>";
	}
	
	
	public String developer(String more, int node) {
		// TODO Auto-generated method stub
		return "<developer>" + more + IDHelper.getNameAsString(namer, node) + "</developer>";
	}
	

	@Override
	public String send_debug(int name, String more) {
		// TODO Auto-generated method stub
		return send(name, more);
	}

	
	
}
