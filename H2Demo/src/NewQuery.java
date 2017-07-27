import java.*;
import java.awt.List;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import org.h2.jdbcx.JdbcConnectionPool;

public class NewQuery 
{
	private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:~/SampleLPDB2;IFEXISTS=FALSE";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    //TS :: create another class such as 
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
	    
	//private static final String FILENAME = "C:\\LPDebug\\operation\\OPERATION .csv";
	
	 private static void batchCreatetWithSchema() throws IOException, SQLException
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
									        
									        createSql =  "INSERT INTO LPSCHEMA1.OP_BUFF_FULL_DETAILS( select op.OPERATION, op.OPERATION_COST, op.TYPE OPERATION_TYPE, ocb.CONSUME_FLOW_BUFFER CONSUME_BUFFER, opb.BUFFER PRODUCE_BUFFER, ocb.ROUNDEDLAG CB_ROUNDEDLAG, ocb.DEFAULTRATE CB_DEFAULTRATE, ocb.HANDLING_COST CB_HANDLING_COST, ocb.ALTFLOW CB_ALT_FLOW, ocb.ALTSET CB_ALTSET, ocb.ALTPERCENT CB_ALTPERCENT, ocb.ALTRANK CB_ALTRANK, ocb.SPECIAL_RANK CB_SPECIAL_RANK, opb.PRODUCE_LAG PB_PRODUCE_LAG, opb.PRODUCE_RATE PB_PRODUCE_RATE, opb.HANDLING_COST_IN PB_HANDLING_COST_IN, opb.ALTFLOW PB_ALTFLOW, opb.ALTSET PB_ALTSET, opb.ALTRANK PB_ALTRANK, opb.SPECIAL_RANK PB_SPECIAL_RANK, cb.CARRYING_COST CB_CARRYING_COST,cb.INV_COST CB_INV_COST,cb.TYPE CB_TYPE,cb.STAGE CB_STAGE, cb.level CB_LEVEL, cb.TRANSSHIP CB_TRANSSHIP, cb.PRIORITY CB_PRIORITY,cb.INDEP_DEMAND CB_INDEP_DEMAND, cb.NDOC CB_NDOC, pb.CARRYING_COST PB_CARRYING_COST,pb.INV_COST PB_INV_COST,pb.TYPE PB_TYPE,pb.STAGE PB_STAGE, pb.level PB_LEVEL, pb.TRANSSHIP PB_TRANSSHIP, pb.PRIORITY PB_PRIORITY,pb.INDEP_DEMAND PB_INDEP_DEMAND, pb.NDOC PB_NDOC, oet.EFF_START OPE_EFF_START, oet.EFF_END OPE_EFF_END, oet.OP_TYPE OPE_OP_TYPE, tb.START TB_START,tb.START+tb.DAYS_COUNT TB_END, tb.DISCOUNT_FACTOR TB_DISCOUNT_FACTOR, mbb1.START CB_MINBB_START, mbb1.END CB_MINBB_END, mbb1.MIN CB_MINBB_SAFETY_STOCK, mtbb1.START CB_MINTBB_START, mtbb1.END CB_MINTBB_END, mtbb1.MIN CB_MINTBB_SAFETY_STOCK, mbb2.START PB_MINBB_START, mbb2.END PB_MINBB_END, mbb2.MIN PB_MINBB_SAFETY_STOCK, mtbb2.START PB_MINTBB_START, mtbb2.END PB_MINTBB_END, mtbb2.MIN PB_MINTBB_SAFETY_STOCK, mabb1.START CB_MAXBB_START, mabb1.END CB_MAXBB_END, mabb1.MAX CB_MAXBB_SAFETY_STOCK, matbb1.START CB_MAXTBB_START, matbb1.END CB_MAXTBB_END, matbb1.MAX CB_MAXTBB_SAFETY_STOCK, mabb2.START PB_MAXBB_START, mabb2.END PB_MAXBB_END, mabb2.MAX PB_MAXBB_SAFETY_STOCK, matbb2.START PB_MAXTBB_START, matbb2.END PB_MAXTBB_END, matbb2.MAX PB_MAXTBB_SAFETY_STOCK from LPSCHEMA1.OPERATION op LEFT JOIN LPSCHEMA1.OPCONSUMEBUFFER ocb ON op.OPERATION=ocb.OP_NAME LEFT JOIN LPSCHEMA1.OPPRODUCEBUFFER opb ON op.OPERATION=opb.OPERATION LEFT JOIN LPSCHEMA1.BUFFER cb ON ocb.CONSUME_FLOW_BUFFER =cb.BUFFER LEFT JOIN LPSCHEMA1.BUFFER pb ON pb.BUFFER=opb.BUFFER LEFT JOIN LPSCHEMA1.OPERATION_EFFECTIVE_TIME  oet ON oet.OPERATION=op.OPERATION LEFT JOIN LPSCHEMA1.TIMEBUCKET tb ON (tb.start <= oet.EFF_END and tb.start+tb.DAYS_COUNT >= oet.EFF_START ) LEFT JOIN LPSCHEMA1.MINBUFFER_BUCKET mbb1 ON mbb1.BUFFER=ocb.CONSUME_FLOW_BUFFER LEFT JOIN LPSCHEMA1.MINBUFFER_BUCKET mbb2 ON mbb2.BUFFER=opb.BUFFER LEFT JOIN LPSCHEMA1.MINTIMEBUFFER_BUCKET mtbb1 ON mtbb1.BUFFER =ocb.CONSUME_FLOW_BUFFER LEFT JOIN LPSCHEMA1.MINTIMEBUFFER_BUCKET mtbb2 ON mtbb2.BUFFER =opb.BUFFER LEFT JOIN LPSCHEMA1.MAXBUFFER_BUCKET mabb1 ON mabb1.BUFFER=ocb.CONSUME_FLOW_BUFFER LEFT JOIN LPSCHEMA1.MAXBUFFER_BUCKET mabb2 ON mabb2.BUFFER=opb.BUFFER LEFT JOIN LPSCHEMA1.MAXTIMEBUFFER_BUCKET matbb1 ON matbb1.BUFFER =ocb.CONSUME_FLOW_BUFFER LEFT JOIN LPSCHEMA1.MAXTIMEBUFFER_BUCKET matbb2 ON matbb2.BUFFER =opb.BUFFER WHERE op.OPERATION IN ("+operation+"))"; 
									        createStmt.execute(createSql);
									        
									        connection.commit();
									      
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
										        createSql =  "INSERT INTO LPSCHEMA1.OP_BUFF_FULL_DETAILS( select op.OPERATION, op.OPERATION_COST, op.TYPE OPERATION_TYPE, ocb.CONSUME_FLOW_BUFFER CONSUME_BUFFER, opb.BUFFER PRODUCE_BUFFER, ocb.ROUNDEDLAG CB_ROUNDEDLAG, ocb.DEFAULTRATE CB_DEFAULTRATE, ocb.HANDLING_COST CB_HANDLING_COST, ocb.ALTFLOW CB_ALT_FLOW, ocb.ALTSET CB_ALTSET, ocb.ALTPERCENT CB_ALTPERCENT, ocb.ALTRANK CB_ALTRANK, ocb.SPECIAL_RANK CB_SPECIAL_RANK, opb.PRODUCE_LAG PB_PRODUCE_LAG, opb.PRODUCE_RATE PB_PRODUCE_RATE, opb.HANDLING_COST_IN PB_HANDLING_COST_IN, opb.ALTFLOW PB_ALTFLOW, opb.ALTSET PB_ALTSET, opb.ALTRANK PB_ALTRANK, opb.SPECIAL_RANK PB_SPECIAL_RANK, cb.CARRYING_COST CB_CARRYING_COST,cb.INV_COST CB_INV_COST,cb.TYPE CB_TYPE,cb.STAGE CB_STAGE, cb.level CB_LEVEL, cb.TRANSSHIP CB_TRANSSHIP, cb.PRIORITY CB_PRIORITY,cb.INDEP_DEMAND CB_INDEP_DEMAND, cb.NDOC CB_NDOC, pb.CARRYING_COST PB_CARRYING_COST,pb.INV_COST PB_INV_COST,pb.TYPE PB_TYPE,pb.STAGE PB_STAGE, pb.level PB_LEVEL, pb.TRANSSHIP PB_TRANSSHIP, pb.PRIORITY PB_PRIORITY,pb.INDEP_DEMAND PB_INDEP_DEMAND, pb.NDOC PB_NDOC, oet.EFF_START OPE_EFF_START, oet.EFF_END OPE_EFF_END, oet.OP_TYPE OPE_OP_TYPE, tb.START TB_START,tb.START+tb.DAYS_COUNT TB_END, tb.DISCOUNT_FACTOR TB_DISCOUNT_FACTOR, mbb1.START CB_MINBB_START, mbb1.END CB_MINBB_END, mbb1.MIN CB_MINBB_SAFETY_STOCK, mtbb1.START CB_MINTBB_START, mtbb1.END CB_MINTBB_END, mtbb1.MIN CB_MINTBB_SAFETY_STOCK, mbb2.START PB_MINBB_START, mbb2.END PB_MINBB_END, mbb2.MIN PB_MINBB_SAFETY_STOCK, mtbb2.START PB_MINTBB_START, mtbb2.END PB_MINTBB_END, mtbb2.MIN PB_MINTBB_SAFETY_STOCK, mabb1.START CB_MAXBB_START, mabb1.END CB_MAXBB_END, mabb1.MAX CB_MAXBB_SAFETY_STOCK, matbb1.START CB_MAXTBB_START, matbb1.END CB_MAXTBB_END, matbb1.MAX CB_MAXTBB_SAFETY_STOCK, mabb2.START PB_MAXBB_START, mabb2.END PB_MAXBB_END, mabb2.MAX PB_MAXBB_SAFETY_STOCK, matbb2.START PB_MAXTBB_START, matbb2.END PB_MAXTBB_END, matbb2.MAX PB_MAXTBB_SAFETY_STOCK from LPSCHEMA1.OPERATION op LEFT JOIN LPSCHEMA1.OPCONSUMEBUFFER ocb ON op.OPERATION=ocb.OP_NAME LEFT JOIN LPSCHEMA1.OPPRODUCEBUFFER opb ON op.OPERATION=opb.OPERATION LEFT JOIN LPSCHEMA1.BUFFER cb ON ocb.CONSUME_FLOW_BUFFER =cb.BUFFER LEFT JOIN LPSCHEMA1.BUFFER pb ON pb.BUFFER=opb.BUFFER LEFT JOIN LPSCHEMA1.OPERATION_EFFECTIVE_TIME  oet ON oet.OPERATION=op.OPERATION LEFT JOIN LPSCHEMA1.TIMEBUCKET tb ON (tb.start <= oet.EFF_END and tb.start+tb.DAYS_COUNT >= oet.EFF_START ) LEFT JOIN LPSCHEMA1.MINBUFFER_BUCKET mbb1 ON mbb1.BUFFER=ocb.CONSUME_FLOW_BUFFER LEFT JOIN LPSCHEMA1.MINBUFFER_BUCKET mbb2 ON mbb2.BUFFER=opb.BUFFER LEFT JOIN LPSCHEMA1.MINTIMEBUFFER_BUCKET mtbb1 ON mtbb1.BUFFER =ocb.CONSUME_FLOW_BUFFER LEFT JOIN LPSCHEMA1.MINTIMEBUFFER_BUCKET mtbb2 ON mtbb2.BUFFER =opb.BUFFER LEFT JOIN LPSCHEMA1.MAXBUFFER_BUCKET mabb1 ON mabb1.BUFFER=ocb.CONSUME_FLOW_BUFFER LEFT JOIN LPSCHEMA1.MAXBUFFER_BUCKET mabb2 ON mabb2.BUFFER=opb.BUFFER LEFT JOIN LPSCHEMA1.MAXTIMEBUFFER_BUCKET matbb1 ON matbb1.BUFFER =ocb.CONSUME_FLOW_BUFFER LEFT JOIN LPSCHEMA1.MAXTIMEBUFFER_BUCKET matbb2 ON matbb2.BUFFER =opb.BUFFER WHERE op.OPERATION IN ("+operation+"))"; 
										        createStmt.execute(createSql);
										        
										        connection.commit();
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

	public static void main(String[] args) throws Exception 
	{
		try
		{
			batchCreatetWithSchema();
					
		}
		catch (Exception e)
		{
            e.printStackTrace();
        }
		
	}
	

}



