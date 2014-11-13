package com.foyatech.cpc.flowengine.dynamic;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 7.0.3
 * Date: 13-6-28
 * Time: 16:52
 */

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

public class DynamicCompile {

	public static void main(String[] args) {

		StringBuilder classStr = new StringBuilder(
				"package com.fr.svn.compiler;\n" + "\n"
						+ "public class Foo implements DynamicCompile.Test {\n"
						+ "\tpublic void test() {\n"
						+ "\t\tSystem.out.println(\"Hello\");\n" + "\t}\n"
						+ "}");

		JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = jc.getStandardFileManager(null,
				null, null);
		Location location = StandardLocation.CLASS_OUTPUT;
		String path = DynamicCompile.class.getClassLoader().getResource(".")
				.getPath();
		File file = new File(path);
		File[] outputs = new File[] { file };
		try {
			fileManager.setLocation(location, Arrays.asList(outputs));
		} catch (IOException e) {
			e.printStackTrace();
		}

		JavaFileObject jfo = new JavaSourceFromString(
				"com.fr.svn.compiler.Foo", classStr.toString());
		JavaFileObject[] jfos = new JavaFileObject[] { jfo };
		Iterable<JavaFileObject> compilationUnits = Arrays.asList(jfos);
		boolean b = jc.getTask(null, fileManager, null, null, null,
				compilationUnits).call();
		if (b) {// 如果编译成功
			try {
				Test t = (Test) Class.forName("com.fr.svn.compiler.Foo")
						.newInstance();
				t.test();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public static class JavaSourceFromString extends SimpleJavaFileObject {

		/**
		 * 源码
		 */
		final String code;

		/**
		 * 构造方法：从字符串中构造一个FileObject
		 * 
		 * @param name
		 *            the name of the compilation unit represented by this file
		 *            object
		 * @param code
		 *            the source code for the compilation unit represented by
		 *            this file object
		 */
		JavaSourceFromString(String name, String code) {
			super(URI.create("string:///" + name.replace('.', '/')
					+ Kind.SOURCE.extension), Kind.SOURCE);
			this.code = code;
		}

		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			return code;
		}
	}

	public static interface Test {
		// 业务方法签名
		void test();
	}
}
