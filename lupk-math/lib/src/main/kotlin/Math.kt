
/**
 * LUPK MATH
 *
 * @param num is vararg of Int | Flout | Double
 * @return result of summation in Double
 */
fun sum(vararg num: Any): Double
{
    var final = 0.0
    num.forEach {
        if(it is Int ) final += it
        if(it is Double) final += it
        if(it is Float) final += it
    }

    return final
}