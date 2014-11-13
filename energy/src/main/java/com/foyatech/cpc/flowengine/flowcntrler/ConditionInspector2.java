package com.foyatech.cpc.flowengine.flowcntrler;

import java.util.HashMap;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.context.FelContext;

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
public class ConditionInspector2 extends InFix {
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
    	ConditionInspector2 c = new ConditionInspector2();
    	HashMap keys = new HashMap();
    	keys.put("CUST_TYPE", "P");
    	keys.put("WORK_TYPE", "138");
    	c.setKeyValue(keys);
    	
    	
    	String check = "{CUST_TYPE=P AND WORK_TYPE=138}";
    	try {
			boolean b = c.Check(check);
			System.out.println(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	
    	
    	 //String expression = "CUST_TYPE==P AND WORK_TYPE==138"; 
    	 String expression = " CUST_TYPE == 'P' &&  WORK_TYPE == '138' "; 
         // 编译表达式 
         Expression compiledExp = AviatorEvaluator.compile(expression); 
  
  
         // 执行表达式 
         Boolean result = (Boolean) compiledExp.execute(keys); 
         System.out.println(result); 
         
         
         
         
         FelEngine fel = new FelEngineImpl();    
         FelContext ctx = fel.getContext();  
         expression = " CUST_TYPE == 'P' &&  WORK_TYPE == '138' "; 
         ctx.set("CUST_TYPE", "P");
         ctx.set("WORK_TYPE", 138);
         
         
         Object t = fel.eval(expression);    
         System.out.println(t);   
	}
}

