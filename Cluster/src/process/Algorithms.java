package process;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Algorithms {
	static ArrayList<node> nodeList =null;
	public static String getAvailableIpAddress(){
		String ip = "";
		if(nodeList==null){
			nodeList = new ArrayList<node>();
		}
		nodeList.clear();
		ResultSet rs = DBManager.readFromTable("node");
		try {
			while(rs.next()){
				node n = new node();
				n.ip = rs.getString("ip");
				n.date = rs.getDate("dates");
				n.load = rs.getInt("loads");
				nodeList.add(n);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!nodeList.isEmpty()){
			for(node n:nodeList){
				if(n.load==0){
					ip = n.ip;
					String querry = "UPDATE `cluster`.`node` SET `loads` = '"+1+"' WHERE `node`.`ip` = '"+n.ip+"';";
					System.out.println(n.ip+"Data base Ip ");
					DBManager.fireQuerry(querry);
					break;
				}
			}
		}
		return ip;
	}
	public static void disconnect(String hostName) {
		// TODO Auto-generated method stub
		String querry = "UPDATE `cluster`.`node` SET `loads` = '0' WHERE `node`.`ip` = '"+hostName+"'";
		DBManager.fireQuerry(querry);
	}
	
}
class node{
	String ip;
	int load;
	Date date;
}