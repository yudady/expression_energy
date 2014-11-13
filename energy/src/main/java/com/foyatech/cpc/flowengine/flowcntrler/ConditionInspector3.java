package com.foyatech.cpc.flowengine.flowcntrler;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.compile.SourceBuilder;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.function.Function;
import com.greenpineyu.fel.parser.FelNode;

//import com.foyatech.cpc.flowengine.flowextender.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Foyatech</p>
 *
 * @author Mack Lo
 * @version 1.0
 */
public class ConditionInspector3 extends InFix {
    private String Result = null;


    public void setKeyValue(HashMap<?, ?> keys) {
        this.KeyValue = keys; //= keys;
    }


    
    
    public boolean Check(String infixString ) throws Exception {
        String RegularString;
        String PostfixString;
        String[] item;

        Result = null;
        if( !infixString.matches("^\\{.+\\}$")== true ) {
            return (false);
        }

        RegularString = infixString.replaceAll("[\\{\\}]", "");
        RegularString = RegularString.replaceAll("\\s*=", "=");
        RegularString = RegularString.replaceAll("\\s*<>", "<>");
/*
        RegularString = RegularString.replaceAll("\\s*<\\s*", "<");
        RegularString = RegularString.replaceAll("\\s*>\\s*", ">");
 */
        RegularString = RegularString.replaceAll("\\(", " ( ");
        RegularString = RegularString.replaceAll("\\)", " ) ");
        RegularString = RegularString.replaceAll("\\s{2}", " ");
        RegularString = RegularString.trim();
        item = RegularString.split("\\s+");
        for(int i=0; i<item.length; i++) {
            item[i] = item[i].replaceAll("^OR$", "|");
            item[i] = item[i].replaceAll("^AND$", "&");
        } // end for loop
        PostfixString = new String( toPosfix(item) );
        Result = eval( PostfixString.split(",") );
        if( Result != null )
            return( Result.equals("1") );
        return( false );
    }
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
    	ConditionInspector3 c = new ConditionInspector3();
    	HashMap keys = new HashMap();
    	keys.put("CUST_TYPE", "P");
    	keys.put("WORK_TYPE", 138);
    	c.setKeyValue(keys);
    	
    	
    	//String check = "{WORK_TYPE=22 OR WORK_TYPE=27 OR WORK_TYPE=39 OR WORK_TYPE=138 OR WORK_TYPE=822 OR WORK_TYPE=26}";
    	//String check = "{CUST_TYPE=P AND WORK_TYPE=138}";
    	String check = "{CUST_TYPE=P AND WORK_TYPE>13}";
    	//String check = "{CUST_TYPE=P AND (WORK_TYPE=21 OR WORK_TYPE=24 OR WORK_TYPE=45 OR WORK_TYPE=138)}";
    	//String check = "  { CUST_TYPE=P AND ( WORK_TYPE=21 OR WORK_TYPE=24 OR WORK_TYPE=45 OR WORK_TYPE=138 ) } ";
    	//String check = "  { CUST_TYPE=P AND  (( WORK_TYPE=21 OR WORK_TYPE=24 ) OR ( WORK_TYPE=45 OR WORK_TYPE=138 )) } ";
    	//String check = "{CUST_TYPE <> F}";
    	//String check = "{CUST_TYPE<>F}";
    	//String check = "{WORK_TYPE > 22}";
    	//String check = "{WORK_TYPE>22}";
    	try {
			boolean b = c.Check(check);
			System.out.println(b);
		} catch (Exception e) {
			e.printStackTrace();
		}

    	
//    	(1)   OR判別：
//    	   {WORK_TYPE=22 OR WORK_TYPE=27 OR WORK_TYPE=39 OR WORK_TYPE=138 OR WORK_TYPE=822 OR WORK_TYPE=26}
//    	(2)   AND判別：
//    	   {CUST_TYPE=P AND WORK_TYPE=138}
//    	(3)   OR及 AND合併判別：
//    	   {CUST_TYPE=P AND (WORK_TYPE=21 OR WORK_TYPE=24 OR WORK_TYPE=45 OR WORK_TYPE=138)}
//    	(4)   <>判別：
//    	   {CUST_TYPE <> F}

    	
    	
    	String newCheck = "";
    	String[] words = null;
    	String[] newWords = null;
    	check = check.trim();
    	if(check.startsWith("{") && check.endsWith("}")){
    		//" CUST_TYPE == 'P' &&  WORK_TYPE == '138' "; 
    		check = check.substring(1,check.length() -1);
    		System.out.println("check=>"+check);
    		words = check.split(" ");
    		newWords = new String[words.length];
    		
    		for(int i = 0 ; i < words.length ; i++){
    			String word = words[i];
    			String temp = "";
    			if("AND".equalsIgnoreCase(word)){
    				temp = "&&";
    			}else if("OR".equalsIgnoreCase(word)){
    				temp =  "||";
    			}else if("<>".equalsIgnoreCase(word)){
    				temp =  "!=";
    			}else{
    				if(word.contains("=")){
    					String[] entry = word.split("=");
    					if(entry[1].endsWith(")")){
    						temp = entry[0] + " == '" + entry[1].replace(")", "") + "')";
    					}else{
    						temp = entry[0] + " == '" + entry[1] + "'";
    					}
    				}else if(word.contains("<>")){
    					temp = word.replace("<>", "!=");
    				}else{
    					temp = word;
    				}
    			}
    			newWords[i] = temp;
    		}
    	}
    	newCheck = StringUtils.join(newWords, " ");
    	
    	
    	
    	System.out.println("newCheck=>"+newCheck);
        FelEngine fel = new FelEngineImpl();  
        
//        fel.addFun(new EqualFoya() );
        
        FelContext ctx = fel.getContext(); 
        
        
        Iterator<String> it = keys.keySet().iterator();
        while(it.hasNext()){
        	String key = it.next();
        	Object value = keys.get(key);
        	ctx.set(key, value);
        }
        
        Object t = fel.eval(newCheck);    
        System.out.println(t);     

	}
}

