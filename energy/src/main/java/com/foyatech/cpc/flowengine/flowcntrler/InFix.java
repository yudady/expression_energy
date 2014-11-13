package com.foyatech.cpc.flowengine.flowcntrler;

import java.util.HashMap;
/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class InFix {
    //protected Attributes KeyValue = new Attributes();
    protected HashMap<?, ?> KeyValue = null;

    private int priority(char op) {
        switch(op) {
           case '|':
                return 1;
            case '&':
                return 2;
            default:
                return 0;
        }
    }

    public char[] toPosfix(String[] infix) {
        char[] stack = new char[infix.length];
        char op;

        StringBuffer buffer = new StringBuffer();

        int top = 0;
        for(int i = 0; i < infix.length; i++) {
            op = infix[i].charAt(0);
            switch(op) {
                // 運算子堆疊
                case '(':
                    if(top < stack.length) {
                        top++;
                        stack[top] = op;
                    }
                    break;
                case '|': case '&':
                    while(priority(stack[top]) >=
                          priority(op)) {
                        if( buffer.length() > 0 )
                            buffer.append(",");
                        buffer.append(stack[top]);
                        top--;
                    }
                    // 存入堆疊
                    if(top < stack.length) {
                        top++;
                        stack[top] = op;
                    }
                    break;
                // 遇 ) 輸出至 (
                case ')':
                    while(stack[top] != '(') {
                        if( buffer.length() > 0 )
                            buffer.append(",");
                        buffer.append(stack[top]);
                        top--;
                    }
                    top--;  // 不輸出(
                    break;
                // 運算元直接輸出
                default:
                    if( buffer.length() > 0 )
                            buffer.append(",");
                    buffer.append(infix[i]);
                    break;
            }
        } // end for loop

        while(top > 0) {
            if( buffer.length() > 0 )
                buffer.append(",");
            buffer.append(stack[top]);
            top--;
        }

        return buffer.toString().toCharArray();
    }

    private boolean cal(String c1, char op, String c2) throws Exception {
        switch(op) {
            case '&':
                return(compaire(c1) && compaire(c2));
            case '|':
                return(compaire(c1) || compaire(c2));
        }
        return false;
    }

    private boolean compaire(String compString) throws Exception {
        String[] compaireArrary = new String[0];
        String value;

        char op = 0x0;
        compString = compString.replaceAll("\\[*\\]*","");
        if( compString.matches("^\\w+=\\w*$") ) {
            op = '=';
            compaireArrary = compString.split("=");
        } else if( compString.matches("^\\w+<>\\w*$") ) {
            op = '!';
            compaireArrary = compString.split("<>");
/*
        } else if( compString.matches("^\\w+>\\w+$") ) {
            op = '>';
            compaireArrary = compString.split(">");
        } else if( compString.matches("^\\w+<\\w+$") ) {
            op = '<';
            compaireArrary = compString.split("<");
*/
        } else {
            op = ( compString.equals("0") ? '0':'1' );
        }


        switch(op) {
            case '=':
                if( KeyValue.containsKey(compaireArrary[0]) == false ) {
                    throw new Exception("Condition:" +
                                        compaireArrary[0] + " Not Found Exception");
                }
                if( compaireArrary!=null && compaireArrary.length >= 2) {
                    value = (String) KeyValue.get(compaireArrary[0]);
                    if( value == null )
                        return(false);
                    return(compaireArrary[1].equals(value));
                } else if(compaireArrary!=null && compaireArrary.length==1) {
                    value = (String) KeyValue.get(compaireArrary[0]);
                    if( value == null || (value != null && value.length()==0) ) {
                        return(true);
                    }
                }
                break;
            case '!':
                if( KeyValue.containsKey(compaireArrary[0]) == false ) {
                    throw new Exception("Condition:" +
                                        compaireArrary[0] + " Not Found Exception");
                }
                if( compaireArrary!= null && compaireArrary.length >= 2) {
                    value = (String) KeyValue.get(compaireArrary[0]);
                    return( !compaireArrary[1].equals(value));
                } else if(compaireArrary!=null && compaireArrary.length==1) {
                    value = (String) KeyValue.get(compaireArrary[0]);
                    if( value == null || (value != null && value.length()==0) ) {
                        return(false);
                    } else {
                        return(true);
                    }
                }
                break;
/*
            case '>':
                if( compaireArrary!=null && compaireArrary.length >= 2) {
                    int v1, v2;

                    value = (String) KeyValue.get(compaireArrary[0]);
                    if( value != null ) {
                        v1 = Integer.parseInt(value);
                        v2 = Integer.parseInt(compaireArrary[1]);
                        return ( v1 > v2 ? true:false);
                    } else {
                        return(false);
                    }
                }
                return(false);
            case '<':
                if( compaireArrary!=null && compaireArrary.length >= 2) {
                    value = (String) KeyValue.get(compaireArrary[0]);
                    return( !value.equals(compaireArrary[1]) );
                }
                return(false);
*/
            case '0':
                return(false);
            case '1':
                return(true);
        }
        return( false);
    }

    public String eval(String[] postfix) throws Exception {
        String[] stack = new String[postfix.length+1];
        char token;
        int top = 0;

        for(int i = 0; i < postfix.length; i++) {
            token = postfix[i].charAt(0);
            switch(token) {
                case '|': case '&':
                    if( cal(stack[top-1], token, stack[top]) ) {
                        stack[top - 1] = "1";
                    } else {
                        stack[top - 1] = "0";
                    }
                    top--;
                    break;
                default:
                    if(top < stack.length) {
                        top ++;
                        if( postfix.length > 1 ) {
                            stack[top] = postfix[i];
                        } else {
                            stack[top] = (compaire(postfix[i]) == true ? "1" :"0");
                        }
                    }
                    break;
            }
        } // end for loop
        return ( stack[top] );
    }

    public static void main(String[] args) {
        String infix = "(1+2)*(3+4)";

    }
}

