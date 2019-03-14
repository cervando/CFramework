package cFramework.log;

import cFramework.communications.fiels.Address;
import cFramework.util.IDHelper;

public class ActivityRegister implements LogRegisterAble{

	Class<?> namer;
	int smnn;
	
	public ActivityRegister(Class<?> namer){
		this.namer = namer;
	}
	
	@Override
	public String header(long name, Address address) {
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
	public String send(long name, String more) {
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
		return 
			"<SEND>"
				+ "<to>"+ addr.toString() + "</to>"
				+ (more.equals("")?  
						"":
						"<data>"+more+"</data>")
			+ "</SEND>";
	}

	@Override
	public String send_debug(long name, String more) {
		return "<send> "
				+ "<to>" + IDHelper.getNameAsString(namer, name) + "</to>"
				+ (more.equals("")?  
						"":
						"<data>"+more+"</data>")
			 +"</send>";
	}
	
	
	@Override
	public String receive(long name, String more) {
		return "<receive> "
					+ "<from>" + IDHelper.getNameAsString(namer, name)+ "</from>" 
					+ (more.equals("")?  
							"":
							"<data>"+more+"</data>")
			+ "</receive>";
	}

	@Override
	public String receive(Address addr, String more) {
		return "<receive><from>"+addr+"</from>"+more+"</receive>";
	}
	
	@Override
	public String receive_debug(long name, String more) {
		return "<receive> "
				+ "<sender> "
					+ "<name>"  + IDHelper.getNameAsString(namer, name)+"</name>"
				+ "</sender> "
				+ (more.equals("")?  
						"":
						"<data>"+more+"</data>")
			  +"</receive>";
	}
	

	@Override
	public String error(String more) {
		return "<ERROR>"+more+"</ERROR>";
	}

	@Override
	public String info(String more) {
		return "<INFO>"+more+"</INFO>";
	}
	
	public String info(String more, long node) {
		// TODO Auto-generated method stub
		return "<INFO>" + more + IDHelper.getNameAsString(namer, node) + "</INFO>";
	}
	
	

	@Override
	public String developer(String more) {
		// TODO Auto-generated method stub
		return "<developer>" + more + "</developer>";
	}
	
	public String developer(String more, long node) {
		// TODO Auto-generated method stub
		return "<developer>" + more + IDHelper.getNameAsString(namer, node) + "</developer>";
	}
	

	@Override
	public String saveRequest(long name, String more) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
