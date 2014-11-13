package com.foyatech.cpc.flowengine.dynamic;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.MethodUtils;

public class DynaCompTest {
	public static void main(String[] args) throws Exception {
		HashMap<String, Object> keys = new HashMap<String, Object>();
		keys.put("CUST_TYPE", "P");
		keys.put("WORK_TYPE", 138);
		String exp = " CUST_TYPE==\"P\" && WORK_TYPE==138 ";
		//String exp = " WORK_TYPE>13 ";
		Long start = new Date().getTime();
		for(int i = 0 ; i < 100 ; i ++){
			System.out.println(call(exp, keys));
		}
		Long end = new Date().getTime();
		System.out.println(end - start);
	}

	public static boolean call(String exp, Map<String, Object> keys)
			throws Exception {
		String fullName = "DynaClass";
		StringBuilder src = new StringBuilder();
		src.append("public class DynaClass {\n");
		src.append("    public boolean callBoolean() {								\n");
		Iterator<Entry<String, Object>> it = keys.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> pair = it.next();
			String key = pair.getKey();
			Object val = pair.getValue();
			if (val instanceof String) {
				src.append("        String " + key + " = \"" + val
						+ "\" ;										\n");
			} else if (val instanceof Number) {
				src.append("        Integer " + key + " = " + val
						+ " ;										\n");
			}
		}
		src.append("        return   " + exp + " ;						\n");
		src.append("    }\n");
		src.append("}\n");

		//System.out.println(src);
		DynamicEngine de = DynamicEngine.getInstance();
		Object instance = de.javaCodeToObject(fullName, src.toString());

		return (Boolean) MethodUtils.invokeExactMethod(instance, "callBoolean",
				new Object[] {});

	}
}
