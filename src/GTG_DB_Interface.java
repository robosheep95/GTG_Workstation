import java.util.ArrayList;

public class GTG_DB_Interface{
	//Result Set Storage
	private Object[][] RSshirtTypes;
	private Object[][] RSsizes;
	private Object[][] RScolors;
	private SQLDatabase GTG_DB;
	private Object[][] RSinventory;
	private Object[][] RSstates;
	//SQL Queue
	private boolean updateNeeded;
	private ArrayList<String> updateList = new ArrayList<String>();
	private String connectionURL;
	
	public GTG_DB_Interface(String connectionURL) {
		this.connectionURL = connectionURL;
		GTG_DB = new SQLDatabase(connectionURL);
		if(GTG_DB.OpenSQLConnection()){
			RSsizes = GTG_DB.executeSELECT("SELECT sizeName FROM size");
			RScolors = GTG_DB.executeSELECT("SELECT colorName FROM color");
			RSinventory = GTG_DB.executeSELECT("SELECT product.productName, size.sizeName ,"
					+ "color.colorName, inventory.quantity FROM inventory LEFT JOIN product ON inventory.productID = product.productID LEFT JOIN size ON inventory.sizeID = size.sizeID"
					+ " LEFT JOIN color ON inventory.colorID = color.colorID");
			//REMOVED 'ORDER BY productName ASC' SO UPDATES WILL WORK WITH ROW NUMBERS THAT 
			//CORRISPOND TO ID'S, BECAUSE ID IS NOT SELECTED
			RSstates = GTG_DB.executeSELECT("select postalCode from state");
			GTG_DB.CloseSQLConnection();
		}
		else{
			GTG_DB.CloseSQLConnection();
		}
	}
	public void runQueue(){
		
		//Open Connection
		GTG_DB.OpenSQLConnection();
		
		//Run all SQL in queue
		for(String stmt : updateList){
			GTG_DB.executeINSERT(stmt);
			//System.out.println(stmt);
		}
		
		//Clear Queue
		updateList = new ArrayList<String>();
		
		//Set updateNeeded = false
		updateNeeded = false;
		
		//Close Connection
		GTG_DB.CloseSQLConnection();
	}
	public Object[][] getRSshirtTypes() {
		return RSshirtTypes;
	}
	public Object[][] getRSsizes() {
		return RSsizes;
	}
	public Object[][] getRScolors() {
		return RScolors;
	}
	public Object[][] getRSstates() {
		return RSstates;
	}
	public Object[][] getInventory() {
		return RSinventory;
	}
	public void setUpdateNeeded(boolean input) {
		updateNeeded = input;
	}
	public boolean getUpdateNeeded(){
		return updateNeeded;
	}
	public void addTOupdateList(String input){
		updateList.add(input);
	}
	public Object[][] executeSELECT(String SQL){
		//Open Connection
			GTG_DB.OpenSQLConnection();
				
			Object [][] results = GTG_DB.executeSELECT(SQL);

			//Close Connection
			GTG_DB.CloseSQLConnection();
			
			return results;
	}
	public boolean isConnected() {
		boolean output = GTG_DB.OpenSQLConnection();
		GTG_DB.CloseSQLConnection();
		return output;
	}
	
}
