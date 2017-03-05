package ms.annotation

import asteroid.A
import asteroid.Phase
import asteroid.AbstractLocalTransformation

import groovy.transform.CompileStatic

import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.syntax.Token
import org.codehaus.groovy.syntax.Types
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.ast.stmt.IfStatement
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.BinaryExpression
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.ast.expr.BooleanExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import org.codehaus.groovy.ast.expr.DeclarationExpression

import org.codehaus.groovy.ast.VariableScope
import org.codehaus.groovy.classgen.VariableScopeVisitor

/**
 * Monitors time taken by the execution of methods of the class
 * annotated with {@link Auditable}
 *
 *<pre><code>
 * def threshold = 1500
 * def init = new Date().getTime()
 *
 * def result = auditable_oldMethodName(...)
 *
 * def end = new Date().getTime()
 * def took = end - init
 *
 * if (took > threshold) {
 *     log.error "method [$methodName] took more than $threshold ms"
 * }
 *
 * return result
 *</code></pre>
 *
 * @since 0.1.0
 */
@CompileStatic
@Phase(Phase.LOCAL.INSTRUCTION_SELECTION)
class AuditableTransformation extends AbstractLocalTransformation<Auditable, MethodNode>{

    @Override
    void doVisit(AnnotationNode annotation, MethodNode methodNode) {
        Integer thresholdVal = A.UTIL.ANNOTATION.get(annotation, 'threshold', Integer)
        BlockStatement stmt = getEndCount(methodNode, thresholdVal)

        MethodNode newMethod = A.NODES
            .method(getAuditableMethodNodeName(methodNode))
            .parameters(methodNode.parameters)
            .code(methodNode.code)
            .build()

        methodNode.declaringClass.addMethod(newMethod)
        methodNode.code = stmt
    }

    private String getAuditableMethodNodeName(MethodNode methodNode) {
        return 'auditing_' + methodNode.name
    }

    private BlockStatement getEndCount(MethodNode methodNode, Integer threshold) {
        Expression thresholdExpr = varDeclarationX('threshold', Integer, A.EXPR.constX(threshold))

        Expression iniExpr = varDeclarationX('init', Long, getTimeDateX())
        Expression resExpr = wrapInVarDeclX('result', methodNode)
        Expression endExpr = varDeclarationX('end', Long, getTimeDateX())
        Expression calExpr = varDeclarationX('took', Integer, calculationX())
        Expression logExpr = logWarningX(methodNode, threshold)

        Statement ifStmt = ifS(boolX('took', Types.COMPARE_GREATER_THAN, 'threshold'),
                               A.STMT.blockS(A.STMT.stmt(logExpr)))

        Expression resRefExpr = A.EXPR.varX('result')

        return A.STMT.blockS(A.STMT.stmt(thresholdExpr),
                             A.STMT.stmt(iniExpr),
                             A.STMT.stmt(resExpr),
                             A.STMT.stmt(endExpr),
                             A.STMT.stmt(calExpr),
                             ifStmt,
                             A.STMT.returnS(resRefExpr))
    }

    Expression getTimeDateX() {
        return A.EXPR.callX(ctorCallExpr(Date), 'getTime')
    }

    Expression calculationX() {
        //end - init
        return binX('end', Types.MINUS, 'init')
    }

    DeclarationExpression wrapInVarDeclX(String varXName, MethodNode methodNode) {
        List<VariableExpression> args = methodNode
           .parameters
           .collect { Parameter p ->
               A.EXPR.varX(p.name)
           }

        String newMethodName = getAuditableMethodNodeName(methodNode)
        MethodCallExpression codeCallExpr = A.EXPR.callThisX(newMethodName, args as Expression[])
        DeclarationExpression declExpr = varDeclarationX(varXName, methodNode.returnType, codeCallExpr)

        return declExpr
    }

    MethodCallExpression logWarningX(MethodNode methodNode, Integer threshold) {
        return A.EXPR.callX(A.EXPR.varX('log'),
                            'error',
                            A.EXPR.constX("method [" + methodNode.name + "] took more than " + threshold + "ms"))
    }

    private BooleanExpression boolX(Expression expression) {
        return new BooleanExpression(expression)
    }

    private BooleanExpression boolX(String leftVarName, int tokenType, String rightVarName) {
        return boolX(binX(leftVarName, tokenType, rightVarName))
    }

    private BinaryExpression binX(String leftVarName, int tokenType, String rightVarName) {
        return new BinaryExpression(A.EXPR.varX(leftVarName),
                                    Token.newSymbol(tokenType, 0, 0),
                                    A.EXPR.varX(rightVarName))
    }

    private BinaryExpression binX(Expression leftExpr, int tokenType, Expression rightExpr) {
        return new BinaryExpression(leftExpr,
                                    Token.newSymbol(tokenType, 0, 0),
                                    rightExpr)
    }

    private DeclarationExpression varDeclarationX(String varName, ClassNode type, Expression defaultValue) {
        return new DeclarationExpression(
            A.EXPR.varX(varName, type),
            Token.newSymbol(Types.EQUAL, 0, 0),
            defaultValue)
    }

    private DeclarationExpression varDeclarationX(String varName, Class type, Expression defaultValue) {
        return new DeclarationExpression(
            A.EXPR.varX(varName, A.NODES.clazz(type).build()),
            Token.newSymbol(Types.EQUAL, 0, 0),
            defaultValue)
    }

    private ConstructorCallExpression ctorCallExpr(Class type) {
        return new ConstructorCallExpression(A.NODES.clazz(type).build(), new ArgumentListExpression())
    }

    private Statement ifS(BooleanExpression booleanExpr, Statement stmt) {
        return new IfStatement(booleanExpr, stmt, emptyStatement())
    }

    private Statement ifElseS(BooleanExpression booleanExpr, Statement ifStmt, Statement elseStmt) {
        return new IfStatement(booleanExpr, ifStmt, elseStmt)
    }

    private Statement emptyStatement() {
        return A.STMT.stmt(A.EXPR.constX(""))
    }
}
