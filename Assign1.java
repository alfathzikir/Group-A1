import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
 
public class Assign1 {
	
	static String type;
	static int declarations = 0;
	static int references = 0;
			
	public static void main(String[] args) throws IOException {
		String directory = args[0];
		type = args[1];
		
		System.out.println("Directory: "+directory);
		System.out.println("Type: "+type);
		parseFilesInDir(directory);
		
		System.out.println(type+". Declarations found: "+declarations+"; References found: "+references+".");
	}
	
	public static void parse(String str){
		
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setBindingsRecovery(true);
		parser.setResolveBindings(true);
		
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
 
		cu.accept(new ASTVisitor() {
 
			//Set names = new HashSet();
					
			public boolean visit(TypeDeclaration node) {
				//ITypeBinding myBinding = node.resolveBinding();
				//String qualifiedName = myBinding.getQualifiedName();
				SimpleName name = node.getName();
				String qualifiedName = name.getFullyQualifiedName();
				System.out.println("Declaration: "+qualifiedName);
				if (type.equals(qualifiedName)) {
					declarations = declarations + 1;
				}
				return true;
			}
			
			public boolean visit(VariableDeclarationStatement node) {
				Type t = node.getType();
//				System.out.println(t);
				String name = t.toString();
				if (type.equals(name)) {
					references = references + 1;
				}
//				PrimitiveType t = (PrimitiveType) node.getType();
//				ITypeBinding myBinding = t.resolveBinding();
//				String qualifiedName = myBinding.getQualifiedName();
				System.out.println("Reference: "+t);
				return true;
			}
			
			public boolean visit(FieldDeclaration node) {
				Type t = node.getType();
//				System.out.println(t);
				String name = t.toString();
				if (type.equals(name)) {
					references = references + 1;
				}
//				PrimitiveType t = (PrimitiveType) node.getType();
//				ITypeBinding myBinding = t.resolveBinding();
//				String qualifiedName = myBinding.getQualifiedName();
				System.out.println("Reference: "+t);
				return true;
			}
				
//			public boolean visit(ClassInstanceCreation node) {
//				Type t = node.getType();
//				System.out.println(t);
//				return false;
//			}
			
//			public boolean visit(SimpleName node) {
//				int t = node.getNodeType();
//				System.out.println(t);
//				return false;	
//			}
			
//			public boolean visit(VariableDeclarationFragment node) {
//				SimpleName name = node.getName();
//				this.names.add(name.getIdentifier());
//				System.out.println(name);
//				return false; // do not continue to avoid usage info
//			}
			
//			public boolean visit(SimpleName node) {
//				if (this.names.contains(node.getIdentifier())) {
//				System.out.println("Usage of '" + node + "' at line " +	cu.getLineNumber(node.getStartPosition()));
//				}
//				return true;
//			}
		});
		
	}
	
	public static String readFileToString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
 
		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
 
		reader.close();
 
		return  fileData.toString();	
	}
	
	public static void parseFilesInDir(String directory) throws IOException{
		File root = new File(directory);
		//System.out.println(root.listFiles());
		File[] files = root.listFiles();
		String filePath = null;
 
		 for (File f : files ) {
			 filePath = f.getAbsolutePath();
			 if(f.isFile()){
				 System.out.println("File: "+filePath);
				 parse(readFileToString(filePath));
			 }
		 }
	}
 
}
