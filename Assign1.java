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
		
		System.out.println("Declarations of "+type+": "+declarations+"    References to "+type+": "+references);
	}
	
	public static void parse(String str){
		
		ASTParser parser = ASTParser.newParser(AST.JLS9);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
 
		cu.accept(new ASTVisitor() {
 
			//Set names = new HashSet();
					
			public boolean visit(TypeDeclaration node) {
				SimpleName name = node.getName();
				System.out.println("Declaration: "+name.getIdentifier());
				if (type.equals(name.getIdentifier())) {
					declarations = declarations + 1;
				}
				return true;
			}
			
			public boolean visit(VariableDeclarationStatement node) {
				Type t = node.getType();
				System.out.println("Reference: "+t);
				return true;
			}
			
			public boolean visit(FieldDeclaration node) {
				Type t = node.getType();
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
