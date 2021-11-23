package math

fun add(vararg num: Int): Double
{
    var count = 0.0
    for (i in num) count += i
    return count
}

fun add(vararg num: Double): Double
{
    var count = 0.0
    for (i in num) count += i
    return count
}

fun add(vararg num: Long): Double
{
    var count = 0.0
    for (i in num) count += i
    return count
}