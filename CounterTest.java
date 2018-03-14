import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class CounterTest {

	private static String BASEDIR = "C:\\Users\\Hamzah\\eclipse-workspace\\SENG\\Tests\\";
	
	// Test that the counter does not crash when encountering a directory with no java files in it
	@Test
	void TestNoFiles() throws IOException {
		String[] arguments = new String[2];
		arguments[0] = BASEDIR+"TestNoFIles\\";
		arguments[1] = "String";
		Counter myCounter = new Counter();
		myCounter.setDeclarations(0);
		myCounter.setReferences(0);
		myCounter.main(arguments);
		assertEquals(0, myCounter.getReferences());
	}
	
	// Test that the counter does not crash when given an invalid directory
	@Test
	void TestNoSuchDirectory() throws IOException {
		String[] arguments = new String[2];
		arguments[0] = BASEDIR+"GarbagePath\\";
		arguments[1] = "String";
		Counter myCounter = new Counter();
		myCounter.setDeclarations(0);
		myCounter.setReferences(0);
		myCounter.main(arguments);
		assertEquals(0, myCounter.getReferences());
	}

	// Test that the counter does not count references when argument is not fully qualified name
	@Test
	void TestSimpleName() throws IOException {
		String[] arguments = new String[2];
		arguments[0] = BASEDIR+"TestSimpleName\\";
		arguments[1] = "String";
		Counter myCounter = new Counter();
		myCounter.setDeclarations(0);
		myCounter.setReferences(0);
		myCounter.main(arguments);
		assertEquals(0, myCounter.getReferences());
	}
	
	// Test that the counter does count references when argument is a fully qualified name
	@Test
	void TestFullyQualifiedName() throws IOException {
		String[] arguments = new String[2];
		arguments[0] = BASEDIR+"TestFullyQualifiedName\\";
		arguments[1] = "java.lang.String";
		Counter myCounter = new Counter();
		myCounter.setDeclarations(0);
		myCounter.setReferences(0);
		myCounter.main(arguments);
		assertEquals(2, myCounter.getReferences());
	}
	
	// Test that the counter does count declarations when argument is a fully qualified name
	@Test
	void TestFullyQualifiedName2() throws IOException {
		String[] arguments = new String[2];
		arguments[0] = BASEDIR+"TestFullyQualifiedName2\\";
		arguments[1] = "Main.Something";
		Counter myCounter = new Counter();
		myCounter.setDeclarations(0);
		myCounter.setReferences(0);
		myCounter.main(arguments);
		assertEquals(1, myCounter.getDeclarations());
	}
	
	// Test that the counter can count declarations and references if both occur
	@Test
	void TestRefsAndDecs() throws IOException {
		String[] arguments = new String[2];
		arguments[0] = BASEDIR+"TestFullyQualifiedName2\\";
		arguments[1] = "DoesItWork.MaybeWorks";
		Counter myCounter = new Counter();
		myCounter.setDeclarations(0);
		myCounter.setReferences(0);
		myCounter.main(arguments);
		assertEquals(1, myCounter.getDeclarations());
		assertEquals(2, myCounter.getReferences());
	}
	

}
