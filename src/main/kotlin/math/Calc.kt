package math

import java.util.*
import kotlin.math.*

private val numStack = Stack<Double>()
private val opStack = Stack<String>()

private enum class NormalOperators(val sign: String, val precedence: Int)
{
    PLUS("+", 2),
    MINUS("-", 2),
    MULTIPLY("*", 3),
    DIVISION("/", 4),
    POWER("^", 5),
    EXPONENTIAL("E", 5),
    UNARY("u", 6);
}

private enum class FunctionalOperators(val func: String)
{
    SIN("sin("),
    COS("cos("),
    TAN("tan("),
    ASIN("asin("),
    ACOS("acos("),
    ATAN("atan("),
    SINH("sinh("),
    COSH("cosh("),
    TANH("tanh("),
    LOG2("log2("),
    LOG10("log10("),
    LN("ln("),
    LOGX("log"),
    SQRT("sqrt("),
    EXP("exp(")
}

private fun getBinaryOperatorPrecedence(currOp: String): Int
{
    return when (currOp)
    {
        NormalOperators.PLUS.sign -> NormalOperators.PLUS.precedence
        NormalOperators.MINUS.sign -> NormalOperators.MINUS.precedence
        NormalOperators.MULTIPLY.sign -> NormalOperators.MULTIPLY.precedence
        NormalOperators.DIVISION.sign -> NormalOperators.DIVISION.precedence
        NormalOperators.POWER.sign -> NormalOperators.POWER.precedence
        NormalOperators.EXPONENTIAL.sign -> NormalOperators.EXPONENTIAL.precedence
        NormalOperators.UNARY.sign -> NormalOperators.UNARY.precedence
        else -> -1
    }
}

private fun clearStacks()
{
    numStack.clear()
    opStack.clear()
}

private fun computeNormalOperation(op: String)
{
    try
    {
        when (op)
        {
            NormalOperators.PLUS.sign ->
            {
                val num0 = numStack.pop()
                val num1 = numStack.pop()
                numStack.push(num1 + num0)
            }

            NormalOperators.MINUS.sign ->
            {
                val num0 = numStack.pop()
                val num1 = numStack.pop()
                numStack.push(num1 - num0)
            }

            NormalOperators.MULTIPLY.sign ->
            {
                val num0 = numStack.pop()
                val num1 = numStack.pop()
                numStack.push(num1 * num0)
            }

            NormalOperators.DIVISION.sign ->
            {
                val num0 = numStack.pop()
                val num1 = numStack.pop()
                numStack.push(num1 / num0)
            }

            NormalOperators.POWER.sign ->
            {
                val num0 = numStack.pop()
                val num1 = numStack.pop()
                numStack.push(num1.pow(num0))
            }

            NormalOperators.EXPONENTIAL.sign ->
            {
                val num0 = numStack.pop()
                val num1 = numStack.pop()
                numStack.push(num1 * (10.0.pow(num0)))
            }

            NormalOperators.UNARY.sign ->
            {
                val num0 = numStack.pop()
                numStack.push(-1.0 * num0)
            }
        }
    }

    catch (es: IndexOutOfBoundsException)
    {
        clearStacks()
        throw Exception("Bad Syntax")
    }

    catch (ae: ArithmeticException)
    {
        clearStacks()
        throw Exception("Division by zero not possible")
    }
}

private fun performSafePushToStack(numString: StringBuilder, currOp: String)
{
    if (numString.isNotEmpty())
    {
        val number = numString.toString().toDouble()
        numStack.push(number)
        numString.clear()

        if (opStack.isEmpty())
        {
            opStack.push(currOp)
        }

        else
        {
            var prevOpPrecedence = getBinaryOperatorPrecedence(opStack.peek())
            val currOpPrecedence = getBinaryOperatorPrecedence(currOp)
            if (currOpPrecedence > prevOpPrecedence)
            {
                opStack.push(currOp)
            }

            else
            {
                while (currOpPrecedence <= prevOpPrecedence)
                {
                    val op = opStack.pop()
                    computeNormalOperation(op)
                    if (!opStack.isEmpty())
                        prevOpPrecedence = getBinaryOperatorPrecedence(opStack.peek())
                    else
                        break
                }
                opStack.push(currOp)
            }
        }
    }

    else if (!numStack.isEmpty() || currOp == NormalOperators.UNARY.sign)
    {
        opStack.push(currOp)
    }

}

private infix fun <T> String.isIn(operators: Array<T>): Boolean
{
    for (operator in operators)
    {
        if (operator is NormalOperators)
        {
            if (this == operator.sign)
            {
                return true
            }
        }
        else if (operator is FunctionalOperators)
        {
            if (this.contains(operator.func))
            {
                return true
            }
            else if (this.contains(FunctionalOperators.LOGX.func))
            {
                return true
            }
        }
    }
    return false
}

private infix fun <T> String.notIn(operators: Array<T>): Boolean
{
    return !(this isIn operators)
}

private fun Double.isInt() = this == floor(this)

private fun computeFunction(func: String)
{
    val num = numStack.pop()

    when (func)
    {
        FunctionalOperators.SIN.func ->
        {
            numStack.push(sin(num))
        }

        FunctionalOperators.COS.func ->
        {
            numStack.push(cos(num))
        }

        FunctionalOperators.TAN.func ->
        {
            numStack.push(tan(num))
        }

        FunctionalOperators.ASIN.func ->
        {
            numStack.push(asin(num))
        }

        FunctionalOperators.ACOS.func ->
        {
            numStack.push(acos(num))
        }

        FunctionalOperators.ATAN.func ->
        {
            numStack.push(atan(num))
        }

        FunctionalOperators.SINH.func ->
        {
            numStack.push(sinh(num))
        }

        FunctionalOperators.COSH.func ->
        {
            numStack.push(cosh(num))
        }

        FunctionalOperators.TANH.func ->
        {
            numStack.push(tanh(num))
        }

        FunctionalOperators.SQRT.func ->
        {
            numStack.push(sqrt(num))
        }

        FunctionalOperators.EXP.func -> numStack.push(exp(num))
        FunctionalOperators.LN.func -> numStack.push(ln(num))
        FunctionalOperators.LOG2.func -> numStack.push(log2(num))
        FunctionalOperators.LOG10.func -> numStack.push(log10(num))

        else ->
        {
            if (func.contains(FunctionalOperators.LOGX.func))
            {
                val base = func.substring(3, func.lastIndex).toDouble()
                numStack.push(log(num, base))
            }
        }
    }
}

private fun computeBracket(numString: StringBuilder)
{
    if (numString.isNotEmpty())
    {
        val number = numString.toString().toDouble()
        numStack.push(number)
        numString.clear()
    }

    var operator = opStack.pop()
    while (operator != "(" && operator notIn FunctionalOperators.values())
    {
        computeNormalOperation(operator)
        operator = opStack.pop()
    }

    if (operator isIn FunctionalOperators.values())
    {
        computeFunction(operator)
    }
}

private fun factorial(num: Double, output: Double = 1.0): Double
{
    return if (num == 0.0) output
    else factorial(num - 1, output * num)
}

private fun performFactorial(numString: StringBuilder)
{
    if (numString.isNotEmpty())
    {
        val number = numString.toString().toDouble()
        numString.clear()

        if (number.isInt())
        {
            val result = factorial(number)
            numStack.push(result)
            return
        }
        else
        {
            clearStacks()
            throw Exception("Domain Error")
        }
    }

    else if (!numStack.isEmpty())
    {
        val number = numStack.pop()

        if (number.isInt())
        {
            var result = factorial(number.absoluteValue)

            if (number < 0)
            {
                result = 0 - result
            }

            numStack.push(result)
            return
        }
        else
        {
            clearStacks()
            throw Exception("Domain Error")
        }
    }

    clearStacks()
    throw Exception("Domain Error")
}

private fun performPercentage(numString: StringBuilder)
{
    if (numString.isNotEmpty())
    {
        val number = numString.toString().toDouble()
        numString.clear()
        val result = number / 100
        numStack.push(result)
        return
    }

    else if (!numStack.isEmpty())
    {
        val number = numStack.pop()
        val result = number / 100.0
        numStack.push(result)
        return
    }

    clearStacks()
    throw Exception("Bad Syntax")
}

private fun pushFunctionalOperator(expression: String, index: Int): Int
{
    for (func in FunctionalOperators.values())
    {
        val funLength = func.func.length

        if ((index + funLength < expression.length) && expression.substring(index, index + funLength) == func.func)
        {
            if (func != FunctionalOperators.LOGX)
            {
                opStack.push(func.func)
                return funLength
            }
            else
            {
                val logRegex = Regex("log[0123456789.]+\\(")
                val found = logRegex.find(expression.substring(index, expression.length))

                try
                {
                    val logXString = found!!.value
                    opStack.push(logXString)
                    return logXString.length
                }

                catch (e: NullPointerException)
                {
                    throw Exception("Base Not Found")
                }
            }
        }
    }

    clearStacks()
    throw Exception("Unsupported Operation at ${expression.substring(index, expression.length)}")
}

fun calc(formula: String, precision: Int = 3): Double
{
    val sb = StringBuilder()

    for (i in formula.indices)
    {
        val currChar = formula[i]

        if (currChar.toString() == NormalOperators.MINUS.sign)
        {
            if (i == 0)
            {
                sb.append('u')
            }

            else
            {
                val prevChar = formula[i - 1]

                if (prevChar in "+*/^E(")
                {
                    sb.append('u')
                }

                else
                {
                    sb.append(currChar)
                }
            }
        }
        else
        {
            sb.append(currChar)
        }
    }

    val ex =  sb.toString()

    var i = 0
    val numString = StringBuilder()

    while (i < ex.length)
    {
        val currChar = ex[i]

        if (currChar in "0123456789.")
        {
            if (i != 0 && (ex[i - 1] == ')' || ex[i - 1] == 'e' || (i >= 2 && ex.substring(i - 2, i) == "PI")))
            {
                performSafePushToStack(numString, "*")
            }

            numString.append(currChar)
            i++
        }

        else if (currChar.toString() isIn NormalOperators.values() || currChar == '(')
        {

            if (currChar == '(')
            {
                if (i != 0 && ex[i - 1].toString() notIn NormalOperators.values())
                {
                    performSafePushToStack(numString, "*")
                }
                opStack.push("(")
            }
            else
            {
                performSafePushToStack(numString, currChar.toString())
            }

            i++
        }

        else if (currChar == ')')
        {
            computeBracket(numString)
            i++
        }

        else if (currChar == '!')
        {
            performFactorial(numString)
            i++
        }

        else if (currChar == '%')
        {
            performPercentage(numString)
            i++
        }

        else if (i + 2 <= ex.length && ex.substring(i, i + 2) == "PI")
        {
            if (i != 0 && ex[i - 1].toString() notIn NormalOperators.values() && ex[i - 1] != '(')
            {
                performSafePushToStack(numString, "*")
            }

            numStack.push(PI)
            i += 2
        }

        else if (ex[i] == 'e' && (i + 1 == ex.length || (i + 1) < ex.length && ex[i + 1] != 'x'))
        {
            if (i != 0 && ex[i - 1].toString() notIn NormalOperators.values() && ex[i - 1] != '(')
            {
                performSafePushToStack(numString, "*")
            }

            numStack.push(E)
            i++
        }

        else
        {
            if (i != 0 && ex[i - 1].toString() notIn NormalOperators.values() && ex[i - 1] != '(')
            {
                performSafePushToStack(numString, "*")
            }

            val increment = pushFunctionalOperator(ex, i)
            i += increment
        }
    }

    if (numString.isNotEmpty())
    {
        val number = numString.toString().toDouble()
        numStack.push(number)
        numString.clear()
    }

    while (!opStack.isEmpty())
    {
        val op = opStack.pop()
        if (op isIn FunctionalOperators.values()) {
            clearStacks()
            throw Exception("Bad Syntax")
        }
        computeNormalOperation(op)
    }

    val res = try
    {
        numStack.pop()
    }

    catch (ie: IndexOutOfBoundsException)
    {
        clearStacks()
        throw Exception("Bad Syntax")
    }

    val corrector = 10.0.pow(precision).toInt()
    var result = round(res * corrector) / corrector
    if (result == -0.0) {
        result = 0.0
    }

    return result
}