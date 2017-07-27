import java.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import org.h2.jdbcx.JdbcConnectionPool;

class CommandOptions 
{
	private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:~/SampleLPDB;IFEXISTS=FALSE";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    private static JdbcConnectionPool getConnectionPool() {
        JdbcConnectionPool cp = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        cp = JdbcConnectionPool.create(DB_CONNECTION + ";CACHE_SIZE=6291456" + ";LOG=0" + ";UNDO_LOG=0", DB_USER, DB_PASSWORD);
        return cp;
    }
    
    private static void createSchema(String schema , String user) throws SQLException 
	 {
		    Date timeTaken;
			 
			JdbcConnectionPool jdbcConnectionPool = getConnectionPool();
		    Connection connection = jdbcConnectionPool.getConnection();
			
		    Statement st = connection.createStatement();
			 
			 timeTaken= new Date(System.currentTimeMillis());
			 System.out.println("Before Creation of schema: "  +timeTaken);
			 
			 connection.setAutoCommit(false);
		  
			 int count = st.executeUpdate("CREATE SCHEMA IF NOT EXISTS "+schema+ " AUTHORIZATION "+user);
			 System.out.println("Schema created.");
	 }
    
    private static final String createTable = "C:\\create.txt";
	
	 private static void batchCreateTable(String schema) throws SQLException 
	 {
		 Date timeTaken;
		 timeTaken= new Date(System.currentTimeMillis());
		 System.out.println("Before Creation of tables: "  +timeTaken);
		
		 try (BufferedReader br = new BufferedReader(new FileReader(createTable))) 
		 {
			 timeTaken= new Date(System.currentTimeMillis());
			 System.out.println("Before Creating table: "  +timeTaken);
				
			 String sCurrentLine;

				while ((sCurrentLine = br.readLine()) != null) {
					JdbcConnectionPool jdbcConnectionPool1 = getConnectionPool();
			        Connection connection1 = jdbcConnectionPool1.getConnection();
			        
			        String createSql;
			        Statement createStmt = connection1.createStatement(); 
			         
			        try
			        {
			        	createSql=sCurrentLine;
			        	createSql=sCurrentLine.replace("LPSCHEMA1", schema);
			        	System.out.println(createSql);
			        	createStmt.executeUpdate(createSql);
			        }
			      
			        
			        finally 
			        {
			        	/*connection.commit();
			        	connection.close();
			            jdbcConnectionPool.dispose();*/
			        }
				}
				
				 System.out.println("Tables Created");
		         
		         timeTaken= new Date(System.currentTimeMillis());
		         System.out.println("After creating Table and dropping all index: "  +timeTaken);
			} 
		 catch (IOException e) 
		 {
				e.printStackTrace();
		}

	 }
	 
	 
	 private static final String insertData = "C:\\insert.txt";
	 
    
	 public static void insertRecords() throws FileNotFoundException, IOException, SQLException {
		 
		 Scanner sc = new Scanner(System.in);
		 
		 JdbcConnectionPool jdbcConnectionPool;
		 Connection connection;
		 
		 try
		 {
			 BufferedReader br = new BufferedReader(new FileReader(insertData));
			 
			 String line = br.readLine();
			 String splitby = "#";
			 
			 	do
			 		{
			 		  	 String[] fdetails = line.split(splitby);
			 		  	 String filename_str =  fdetails[0];
			 		  	 String cmd_str =  fdetails[1];
			 		  	 
			 		  	System.out.println(filename_str);
			 		  	
			 		  	jdbcConnectionPool = getConnectionPool();
					     connection = jdbcConnectionPool.getConnection();
					     
					     Statement createStmt;
			 		  	 
			 		  	String command = "cmd.exe /c dir /B \"C:\\LPDebug\\CSV_asian\\*"+filename_str+"\" | findstr /I \"[1-9][_]"+filename_str+"\"";
			 		  	
			 		  	//String createSql;
				        createStmt = connection.createStatement();
				        
				        createStmt.execute(cmd_str);
				    	
				    	connection.commit();
				    	
				    	
			 		  	try {
			 		  		
						    Process process = Runtime.getRuntime().exec(command);
						 
						    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

						    while ((line = reader.readLine()) != null)
						    {
						    	String filename = line;
						    	String cmd = cmd_str.replace(filename_str, filename);
						    	
						    	System.out.println(filename);
						    	
						    	jdbcConnectionPool = getConnectionPool();
							     connection = jdbcConnectionPool.getConnection();
							     
							     createStmt.execute(cmd);
							     
							     connection.commit();
						         connection.close();
						         jdbcConnectionPool.dispose();
						       
						    }
						 
						    reader.close();
						 
						} catch (IOException e) {
						    e.printStackTrace();
						}
						
						
			 		}while((line = br.readLine()) !=null);
			 	
		 }
		 finally{
			 
		 }
 	}
	 
	 
	 private static final String IndexFile = "C:\\createindex.txt";
		
	 public static void createIndex() throws FileNotFoundException, IOException, SQLException 
	 {
		 try (BufferedReader br = new BufferedReader(new FileReader(IndexFile))) {

			 Date timeTaken;
			 
			 timeTaken= new Date(System.currentTimeMillis());
				System.out.println("Before Creating Index: "  +timeTaken);
				
				String sCurrentLine;

				while ((sCurrentLine = br.readLine()) != null) {
					JdbcConnectionPool jdbcConnectionPool = getConnectionPool();
			        Connection connection = jdbcConnectionPool.getConnection();
			        
			        String createSql;
			        Statement createStmt = connection.createStatement(); 
			         
			        try
			        {
			        	createSql=sCurrentLine;
			        	createStmt.executeUpdate(createSql);
			        }
			     
			        finally 
			        {
			        	connection.commit();
			        	connection.close();
			            jdbcConnectionPool.dispose();
			        }
			        
				}
		 }
	
	 }
	 
	 private static void createBufferfolwTable() throws IOException, SQLException
	 {
		 JdbcConnectionPool jdbcConnectionPool;
		 Connection connection;
		 
		 
		    String operation = null;
		    String splitBy = ",";
		    
		    String command = "cmd.exe /c dir /B \"C:\\LPDebug\\CSV_asian\\*Operation.csv\" | findstr /I \"[1-9][_]Operation.csv\"";
		    
		    int i=0, j=0;
		    
	        try
	        {
	        	String line = null;
	        	//ArrayList arrayLines = new ArrayList();
	        	 ArrayList<String> arrayLines = new ArrayList<String>();
	            Process process = Runtime.getRuntime().exec(command);
				 
				    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				 
				    	while((line = reader.readLine()) !=null)
				    	{
				    		{
				    			System.out.println(line);
				    			arrayLines.add(line);
				    			}
				    	}
				    	
				    	arrayLines.add("Operation.csv");
				    	
				    	Iterator<String> itr = arrayLines.iterator();
				    	
				        while(itr.hasNext())
				
				        {
				        	BufferedReader br = new BufferedReader(new FileReader("C:\\LPDebug\\CSV_asian\\"+itr.next()));
				        	i = 0; j=0;
				        	
				        	while((line = br.readLine()) !=null)
				        	{
					        	i++;
					        	j++;
					        	
					        	if(j==1)
					        	continue;
					             String[] b = line.split(splitBy);
					             //System.out.println(b[0]);
					             if(operation == null)
					             {
					            	 operation = b[0].replace("\"", "'");
					             }
					             
					             else
					             {
					            	 operation = operation +","+b[0].replace("\"", "'");
					             }
					             
					             if(i>=970)
					             {
					            	 System.out.println(j);
					            	 System.out.println(operation);
					            	 jdbcConnectionPool = getConnectionPool();
								     connection = jdbcConnectionPool.getConnection();
								     
					            	 try
					            	 {
									     	String createSql;
									        Statement createStmt = connection.createStatement(); 
									        
									        createSql =  "INSERT INTO LPSCHEMA1.OP_BUFFER_FLOW (WITH RECURSIVE OP_HIER(LEAF_BUFFER,PRODUCE_BUFFER,OP_PATH,LEVEL) AS(SELECT CONSUME_FLOW_BUFFER,CONSUME_FLOW_BUFFER,':'||OP_NAME OP_PATH,0 level FROM LPSCHEMA1.TEMP_OP_BUFF_FLOW1 ocb WHERE ocb.OP_NAME IN("+operation+") union all SELECT p2.LEAF_BUFFER,ocb.CONSUME_FLOW_BUFFER, p2.OP_PATH||';'||ocb.CONSUME_FLOW_BUFFER||':'||ocb.OP_NAME,p2.LEVEL+1 FROM LPSCHEMA1.TEMP_OP_BUFF_FLOW2 ocb JOIN OP_HIER p2 ON ocb.BUFFER=p2.PRODUCE_BUFFER AND p2.OP_PATH NOT LIKE '%'||ocb.CONSUME_FLOW_BUFFER||':'||ocb.OP_NAME||'%' ) SELECT * FROM OP_HIER WHERE NOT EXISTS (SELECT 1 FROM LPSCHEMA1.OPPRODUCEBUFFER opb_nxt WHERE OP_HIER.PRODUCE_BUFFER=opb_nxt.BUFFER))";
									        createStmt.execute(createSql);
									        
									        connection.commit();
									        
									        createSql =  "INSERT INTO LPSCHEMA1.OP_BUFFER_FLOW (WITH RECURSIVE OP_HIER(LEAF_BUFFER,PRODUCE_BUFFER,OP_PATH,LEVEL) AS(SELECT CONSUME_FLOW_BUFFER,CONSUME_FLOW_BUFFER,':'||OP_NAME OP_PATH,0 level FROM LPSCHEMA1.TEMP_OP_BUFF_FLOW1 ocb WHERE ocb.OP_NAME IN("+operation+") union all SELECT p2.LEAF_BUFFER,ocb.CONSUME_FLOW_BUFFER, p2.OP_PATH||';'||ocb.CONSUME_FLOW_BUFFER||':'||ocb.OP_NAME,p2.LEVEL+1 FROM LPSCHEMA1.TEMP_OP_BUFF_FLOW2 ocb JOIN OP_HIER p2 ON ocb.BUFFER=p2.PRODUCE_BUFFER AND p2.OP_PATH NOT LIKE '%'||ocb.CONSUME_FLOW_BUFFER||':'||ocb.OP_NAME||'%' ) SELECT * FROM OP_HIER WHERE NOT EXISTS (SELECT 1 FROM LPSCHEMA1.OPPRODUCEBUFFER opb_nxt WHERE OP_HIER.PRODUCE_BUFFER=opb_nxt.BUFFER))";
									        createStmt.execute(createSql);
					            	 }
					            	 
							            	/* catch(Exception e)
							            	 {
							            		 System.out.println(e);
							            	 }*/
							            	 
							            	 finally
							            	 {
										        	connection.commit();
										        	connection.close();
										            jdbcConnectionPool.dispose();
							            	 }
										      
							            	i=0;
							            	
							            	operation = null;
							             }
							       
							        }
							        
							        if(i>0 && i<970)
						            {
							        	System.out.println(j);
						           	 System.out.println(operation);
						           	 jdbcConnectionPool = getConnectionPool();
									     connection = jdbcConnectionPool.getConnection();
									     
						           	 try
						           	 {
						           		 
										     	String createSql;
										        Statement createStmt = connection.createStatement(); 
										     
										        //System.out.println(operation);
										        createSql =  "INSERT INTO LPSCHEMA1.OP_BUFFER_FLOW (WITH RECURSIVE OP_HIER(LEAF_BUFFER,PRODUCE_BUFFER,OP_PATH,LEVEL) AS(SELECT CONSUME_FLOW_BUFFER,CONSUME_FLOW_BUFFER,':'||OP_NAME OP_PATH,0 level FROM LPSCHEMA1.TEMP_OP_BUFF_FLOW1 ocb WHERE ocb.OP_NAME IN("+operation+") union all SELECT p2.LEAF_BUFFER,ocb.CONSUME_FLOW_BUFFER, p2.OP_PATH||';'||ocb.CONSUME_FLOW_BUFFER||':'||ocb.OP_NAME,p2.LEVEL+1 FROM LPSCHEMA1.TEMP_OP_BUFF_FLOW2 ocb JOIN OP_HIER p2 ON ocb.BUFFER=p2.PRODUCE_BUFFER AND p2.OP_PATH NOT LIKE '%'||ocb.CONSUME_FLOW_BUFFER||':'||ocb.OP_NAME||'%' ) SELECT * FROM OP_HIER WHERE NOT EXISTS (SELECT 1 FROM LPSCHEMA1.OPPRODUCEBUFFER opb_nxt WHERE OP_HIER.PRODUCE_BUFFER=opb_nxt.BUFFER))";
										        createStmt.execute(createSql);
										        
										        connection.commit();
									        	
										        createSql =  "INSERT INTO LPSCHEMA1.OP_BUFFER_FLOW (WITH RECURSIVE OP_HIER(LEAF_BUFFER,PRODUCE_BUFFER,OP_PATH,LEVEL) AS(SELECT CONSUME_FLOW_BUFFER,CONSUME_FLOW_BUFFER,':'||OP_NAME OP_PATH,0 level FROM LPSCHEMA1.TEMP_OP_BUFF_FLOW1 ocb WHERE ocb.OP_NAME IN("+operation+") union all SELECT p2.LEAF_BUFFER,ocb.CONSUME_FLOW_BUFFER, p2.OP_PATH||';'||ocb.CONSUME_FLOW_BUFFER||':'||ocb.OP_NAME,p2.LEVEL+1 FROM LPSCHEMA1.TEMP_OP_BUFF_FLOW2 ocb JOIN OP_HIER p2 ON ocb.BUFFER=p2.PRODUCE_BUFFER AND p2.OP_PATH NOT LIKE '%'||ocb.CONSUME_FLOW_BUFFER||':'||ocb.OP_NAME||'%' ) SELECT * FROM OP_HIER WHERE NOT EXISTS (SELECT 1 FROM LPSCHEMA1.OPPRODUCEBUFFER opb_nxt WHERE OP_HIER.PRODUCE_BUFFER=opb_nxt.BUFFER))";
										        createStmt.execute(createSql);
						           	 }
						           	 
						           	/* catch(Exception e)
						           	 {
						           		 System.out.println(e);
						           	 }*/
						           	 
						           	 finally 
									        {
									        	connection.commit();
									        	connection.close();
									            jdbcConnectionPool.dispose();
									        }
						           	i=0;
						           	operation = null;
						            }
						            
							        br.close();
							      
							        reader.close();
							        }
							        }
							       /* catch(Exception e)
							        {
							        	System.out.println(e);
							        }*/
							        finally
							        {
							        	
							        }

	} 
	 
	 private static final String BufferIndexFile = "C:\\bufferflowIndex.txt";
	 
	 private static void cretaIndexonBufferFlow() throws SQLException, FileNotFoundException, IOException
	 {
		 try (BufferedReader br = new BufferedReader(new FileReader(BufferIndexFile))) {

			 Date timeTaken;
			 
			 timeTaken= new Date(System.currentTimeMillis());
				System.out.println("Before Creating Index: "  +timeTaken);
				
				String sCurrentLine;

				while ((sCurrentLine = br.readLine()) != null) {
					JdbcConnectionPool jdbcConnectionPool = getConnectionPool();
			        Connection connection = jdbcConnectionPool.getConnection();
			        
			        String createSql;
			        Statement createStmt = connection.createStatement(); 
			         
			        try
			        {
			        	createSql=sCurrentLine;
			        	createStmt.executeUpdate(createSql);
			        }
			     
			        finally 
			        {
			        	connection.commit();
			        	connection.close();
			            jdbcConnectionPool.dispose();
			        }
			        
				}
		 }
	 }
	 
	 public static void convertToCsv(ResultSet rs) throws SQLException, FileNotFoundException {
	        PrintWriter csvWriter = new PrintWriter(new File("C:/QueryResult.csv")) ;
	        ResultSetMetaData meta = rs.getMetaData() ; 
	        int numberOfColumns = meta.getColumnCount() ; 
	        String dataHeaders = "\"" + meta.getColumnName(1) + "\"" ; 
	        for (int i = 2 ; i < numberOfColumns + 1 ; i ++ ) { 
	                dataHeaders += ",\"" + meta.getColumnName(i).replaceAll("\"","\\\"") + "\"" ;
	        }
	        csvWriter.println(dataHeaders) ;
	        while (rs.next()) {
	            String row = "\"" + rs.getString(1).replaceAll("\"","\\\"") + "\""  ; 
	            for (int i = 2 ; i < numberOfColumns + 1 ; i ++ ) {
	                row += ",\"" + rs.getString(i).replaceAll("\"","\\\"") + "\"" ;
	            }
	        csvWriter.println(row) ;
	        }
	        csvWriter.close();
	    }
	 
	    public static void runQuery(String queryFile) {
	        try {
	        	
	            BufferedReader br = new BufferedReader(new FileReader(queryFile));
	            
	            
	            String sCurrentLine;

				while ((sCurrentLine = br.readLine()) != null) {
					JdbcConnectionPool jdbcConnectionPool1 = getConnectionPool();
			        Connection connection1 = jdbcConnectionPool1.getConnection();
			        
			      
			        PreparedStatement statement;
			         
			        try
			        {
			        	statement =connection1.prepareStatement(sCurrentLine);
			        	
			        	 ResultSet rs = statement.executeQuery();
				            while (rs.next()) {
				                
				                convertToCsv(rs);
				               
				            }
			        }
			        finally 
			        {
			        	
			        }
				}
				
				br.close();
	        } catch (Exception ex) {
	            System.out.println(ex);
	        }
	    }
	 
	 
    public static void main(String args[]) throws IOException, SQLException
    {
        try
        {
        	 Scanner sc = new Scanner(System.in);
             
        	 int index = 1;
        	 int sub = 1;
        	 String dirPath = "";
        	 String desPath = "";
        	 String schemanm = "";
        	 String usernm = "";
        	 int tblOption = 1;
        	
        	 File file = new File("C:/UserInput.txt"); 
        	 if(file.exists())
        	 {
        		 file.delete();
        	 }
        	 
             FileWriter fstream; 
             BufferedWriter out; 
             PrintWriter pw;
             
             fstream = new FileWriter(file, false);
        	 out = new BufferedWriter(fstream);
        	
             do{
     			System.out.println("User Options:");
     			
     			System.out.println("1.Load lpopt_input.dat");
     			System.out.println("2 .Run Query ");
     			/*System.out.println("3.Load Binary DB File ");
     			System.out.println("4. Export from DB");
     			System.out.println("5. Run Analysis");*/
     			
     			System.out.println("Enter your choice:");
     			index=sc.nextInt();
     			
     		    switch(index)
     		    {
	     		   case 1 :
	     			   			System.out.println("1. Directory ");
		     		   			System.out.println("2. Create Schema ");
		     		   			System.out.println("3. Tables to Load");
		     		   			
		     		   			System.out.println("Enter your choice:");
		     		   			sub=sc.nextInt();
		     		   		
		     		   		switch(sub)
		     		   		{
							   
						   	case 1: System.out.println("Enter the directory path of input.dat file: ");
						   			dirPath = sc.next();
						   			
						   			System.out.println("Enter the directory path of destination file: ");
						   			desPath = sc.next();
						   		
						   			out.write("Directory Path->"+dirPath);
						   			out.newLine();
						   			out.write("Destination Path->"+desPath);
						   			out.newLine();
	
									break;  
									
						   	case 2:	System.out.println("Enter the name of the schema to create:");		
						   			schemanm = sc.next();
						   			
						   			System.out.println("Enter the username:");		
						   			usernm = sc.next();
						   			
						   			createSchema(schemanm, usernm);
						   			break;
						   			
						   case 3: System.out.println("Do you want to load all the tables? If yes then type 1 else type 0");
						   		   tblOption = sc.nextInt();
						   		   
						   		   if(tblOption==1)
						   		   
						   			out.write("Table->All");
						   		   	out.newLine();
						   			
						   			switch(tblOption)
						   			{
						   			
						   				case 1: System.out.println("Loading all the tables:");
						   						batchCreateTable(schemanm);
						   						insertRecords();
						   						createIndex();
						   						createBufferfolwTable();
						   						cretaIndexonBufferFlow();
						   						
						   						break;
						   						
						   				case 0: System.out.println("Table names:"); 
						   						String tables= "BUFFER\n" + "BUFFER_PROD_OP \n" + "BUFFER_STAGES\n" + "BUFFERGROUPS\n" + "CAPACITY_BANDS\n" + "DELIVER_OP\n" + "FAMILY\n" + "FAMILYOPERATION\n" + "fixed_res_flows\n" + "LAYER\n" + "FLEXIBLE_BUFFER\n" + "FLEXIBLE_RESOURCE\n" + "HOLIDAY_RESOURCE_LOAD\n" + "HOLIDAY_RESOURCE\n" + "OPERATION\n" + "OPERATION_EFFECTIVE_TIME\n" + "LOAD\n" + "min_outsource\n" + "NONPRODUCE_OPERATIONS\n" + "OPBUFFER_YIELD\n" + "MINBUFFER_BUCKET\n" + "MINTIMEBUFFER_BUCKET\n" + "OBLEVEL\n" + "on_hand_quantity\n" + "OPCONSUMEBUFFER\n" + "OPPRODUCEBUFFE \n" + "PRIMARY_ALTERNAT \n" + "REQUESTLAYER\n" + "RESOURCETIMEBUCKET\n" + "RESOURCE\n" + "PRODUCTMIX \n" + "PROP_PRIMARY_ALTERNATE\n" + "PROP_PRIMARY_EFFECTIVE_TIME\n" + "RESFAM1FAM2\n" + "RESOURCE_TARGET_UTILIZATION\n" + "RESOURCE_YIELD_TARGET\n" + "RESOURCEGROUPS\n" + "wip_quantity\n" + "LOAD_SETUP\n" + "LOAD_YIELD\n" + "max_outsource\n" + "OPGROUPS\n" + "STORAGE_BUCKET\n" + "TIMEBUCKET" ;
						   						System.out.println(tables);
						   						
						   						System.out.println("Enter total number of tables to load:");
						   						int num = sc.nextInt();
						   						
						   						String tablenames[] = new String[num];
						   						
						   						System.out.println("Enter the table names:");
						   						
						   						for (int i = 0; i < tablenames.length; i++) 
						   						{
						   				            tablenames[i] = sc.next();
						   				            
						   						}
						   						
						   						for (int i = 0; i < tablenames.length; i++) 
						   						{
						   						  //if (i != 0) 
						   							 // 
						   						  String name = tablenames[i];
						   						  out.write("Table names->"+name);
						   						if (i != tablenames.length-1) 
						   						  out.write(",");
						   						}
						   						out.close();
						   						
						   						break;

						   				
						   			}
						   		   break;
						   		   
						   default : System.out.println("Not Found!!!");
	     		  					 System.exit(0);
   			
	     		  					break;
		     		   		}
		     		   		
		     		   		break;
		     		   		
	     		   case 2:	System.out.println("Enter the directory path of input.dat file: ");
		   					String queryFile = sc.next();
		   					
		   					runQuery(queryFile);
		   					
	     			   
		   			break;
	     			   
	     		  default : System.out.println("Not Found!!!");
	     		  			System.exit(0);
    			
    			  	break;
    	
     		    }
     		    
             }while(index!=0);
        }
        
        finally
        {
        	
        }
    }
    
  
}



