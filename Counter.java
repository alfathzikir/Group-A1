import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Map;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

/**
 * The counter program implements an application that:
 * 1) takes a pathname to indicate a directory of interest,
 * 2) takes a string to indicate a fully qualified name of a Java type,
 * 3) counts the number of declarations of that type within that 
 * 	  directory (non-recursively!), and
 * 4) counts the number of references to each occurrence of that type 
 * 	  within that directory (non-recursively!).
 * @author Hamzah Umar, AlFath Zikir, Tariq Rahmani
 * @version 5.0
 */
public class Counter {
	
	static String inputType;
	static String inputDirectory;
	static int declarations = 0;
	static int references = 0;
	
	/**
	 * This is the main method
	 * @param args
	 * @return Nothing
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String inputDirectory = args[0];
		inputType = args[1];

		parseFilesInDir(inputDirectory);
		
		System.out.println(inputType+". Declarations found: "+declarations+"; References found: "+references+".");
	}
	
	/**
	 * Method which reads a file, and converts it to a string
	 * @param filePath This indicates the file path of the file to be read
	 * @return String This returns the string that has been created
	 * @throws IOException
	 */
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
	
	/**
	 * Method which parses all Java files within a directory
	 * @param directory The directory containing the files that need to be parsed
	 * @return Nothing
	 * @throws IOException
	 */
	public static void parseFilesInDir(String directory) throws IOException{
		File root = new File(directory);
		if (root.isDirectory()) {
			File[] files = root.listFiles(new FilenameFilter () {
				public boolean accept(File root, String filename) {
					return filename.endsWith(".java");
				}
			});
			
			String filePath = null;
	 
			for (File f : files ) {
				filePath = f.getAbsolutePath();
				if(f.isFile()){
					parse(readFileToString(filePath));
				}
			}
		}
		
		else {
			System.out.println("Sorry, no such directory found");
		}
	}
	
	/**
	 * Main parsing method which creates an AST out of the input string
	 * Uses an ASTVisitor to visit nodes and count declarations and references
	 * @param str String which will be parsed
	 * @return Nothing
	 */
	public static void parse(String str){
		// create a new parser
		ASTParser parser = ASTParser.newParser(AST.JLS9);
		
		// parser setup
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setBindingsRecovery(true);
		parser.setResolveBindings(true);
		
		Map options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_5);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_5);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5);
		
		parser.setCompilerOptions(options);
		parser.setEnvironment(null, null, null, true);
		
		String unitName = "";
		parser.setUnitName(unitName);
		
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
 
		// make the CompilationUnit accept a new AST visitor and overwrites relevant
		// visit methods
		cu.accept(new ASTVisitor() {
 			
			// visit Type declaration nodes
			public boolean visit(TypeDeclaration node) {
				ITypeBinding myBinding = node.resolveBinding();
				String qualifiedName = myBinding.getQualifiedName();
				if (inputType.equals(qualifiedName)) {
					declarations = declarations + 1;
				}
				return true;
			}
			
			// visit Enum declaration nodes
			public boolean visit(EnumDeclaration node) {
				ITypeBinding myBinding = node.resolveBinding();
				String qualifiedName = myBinding.getQualifiedName();
				if (inputType.equals(qualifiedName)) {
					declarations = declarations + 1;
				}
				return true;
			}
			
			// visit Annotation type nodes
			public boolean visit(AnnotationTypeDeclaration node) {
				ITypeBinding myBinding = node.resolveBinding();
				String qualifiedName = myBinding.getQualifiedName();
				System.out.println("Reference: "+qualifiedName);
				if (inputType.equals(qualifiedName)) {
					references = references + 1;
				}
				return true;
			}
			
			// visit Variable declaration statement nodes
			public boolean visit(VariableDeclarationStatement node) {
				Type t = node.getType();
				ITypeBinding myBinding = t.resolveBinding();
				String qualifiedName = myBinding.getQualifiedName();
				if (inputType.equals(qualifiedName)) {
					references = references + 1;
				}
				return true;
			}
			
			// visit field declaration nodes
			public boolean visit(FieldDeclaration node) {
				Type t = node.getType();
				ITypeBinding myBinding = t.resolveBinding();
				String qualifiedName = myBinding.getQualifiedName();
				if (inputType.equals(qualifiedName)) {
					references = references + 1;
				}
				return true;
			}
			
			// visit class instance creation nodes
			public boolean visit(ClassInstanceCreation node) {
				Type t = node.getType();
				ITypeBinding myBinding = t.resolveBinding();
				String qualifiedName = myBinding.getQualifiedName();
				if (inputType.equals(qualifiedName)) {
					references = references + 1;
				}
				return true;
			}
			
		});
		
	}
	
	public static String getInputType() {
		return inputType;
	}

	public static void setInputType(String inputType) {
		Counter.inputType = inputType;
	}

	public static int getDeclarations() {
		return declarations;
	}

	public static void setDeclarations(int declarations) {
		Counter.declarations = declarations;
	}

	public static int getReferences() {
		return references;
	}

	public static void setReferences(int references) {
		Counter.references = references;
	}

	public static String getInputDirectory() {
		return inputDirectory;
	}
	
	public static void setInputDirectory(String inputDirectory) {
		Counter.inputDirectory = inputDirectory;
	}
	
}
