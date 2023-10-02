import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Detector {
    public static void main(String[] args) throws IOException {
        String filePath1 = "file1.java";
        String filePath2 = "file2.java";
        
        File file1 = new File(filePath1);
        File file2 = new File(filePath2);
        
        CompilationUnit cu1 = JavaParser.parse(file1);
        CompilationUnit cu2 = JavaParser.parse(file2);

        // Function names
        Set<String> functions1 = new HashSet<>();
        Set<String> functions2 = new HashSet<>();

        // Method calls
        Set<String> methodCalls1 = new HashSet<>();
        Set<String> methodCalls2 = new HashSet<>();

        // Visit methods and method calls in file1
        new MethodVisitor().visit(cu1, functions1);
        new MethodCallVisitor().visit(cu1, methodCalls1);

        // Visit methods and method calls in file2
        new MethodVisitor().visit(cu2, functions2);
        new MethodCallVisitor().visit(cu2, methodCalls2);

        // Compare function names
        double functionSimilarity = getSimilarity(functions1, functions2);
        System.out.println("Function similarity: " + functionSimilarity + "%");

        // Compare method calls
        double methodCallSimilarity = getSimilarity(methodCalls1, methodCalls2);
        System.out.println("Method call similarity: " + methodCallSimilarity + "%");
    }

    private static class MethodVisitor extends VoidVisitorAdapter<Set<String>> {
        @Override
        public void visit(MethodDeclaration n, Set<String> arg) {
            arg.add(n.getNameAsString());
            super.visit(n, arg);
        }
    }

    private static class MethodCallVisitor extends VoidVisitorAdapter<Set<String>> {
        @Override
        public void visit(MethodCallExpr n, Set<String> arg) {
            arg.add(n.getNameAsString());
            super.visit(n, arg);
        }
    }

    private static double getSimilarity(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        double totalElements = set1.size() + set2.size();
        if (totalElements == 0) return 100.0;

        return (2.0 * intersection.size() / totalElements) * 100;
    }
}
