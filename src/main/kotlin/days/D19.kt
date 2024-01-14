package days

import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

@Suppress("unused")
object D19 : Day {
    private data class Workflow(val name: String, val rules: List<Rule>, val default: String)
    private data class Rule(val property: Char, val op: Char, val n: Int, val to: String)

    private val numberRegex = Regex("\\d+")
    private val workflows: List<Workflow> = readFile().takeWhile(String::isNotEmpty).map { workflowString ->
        val name = workflowString.takeWhile { c -> c != '{' }
        val ruleStrings = workflowString.substring(name.length.inc(), workflowString.length.dec()).split(",")
        val default = ruleStrings.last()
        val rules = ruleStrings.dropLast(1).map { ruleString ->
            val (cond, to) = ruleString.split(":")
            Rule(cond[0], cond[1], cond.substring(2).toInt(), to)
        }
        Workflow(name, rules, default)
    }

    override suspend fun part1(): String {
        data class Part(val x: Int, val m: Int, val a: Int, val s: Int)
        val parts: List<Part> = readFile().drop(workflows.size.inc()).map { partString ->
            val (x, m, a, s) = numberRegex.findAll(partString).map { mr -> mr.value.toInt() }.toList()
            Part(x, m, a, s)
        }

        val workflowMap = workflows.associate { (name, rules, default) ->
            val ruleFns = rules.map { (propertyChar, op, comp, to) ->
                val property = when (propertyChar) {
                    'x' -> Part::x
                    'm' -> Part::m
                    'a' -> Part::a
                    's' -> Part::s
                    else -> error("Unsupported property $propertyChar")
                }
                val fn = if (op == '<') { n: Int -> n < comp } else { n: Int -> n > comp}
                { p: Part -> to.takeIf { fn(property(p)) } }
            }
            name to { p: Part -> ruleFns.firstNotNullOfOrNull { ruleFn -> ruleFn(p) } ?: default }
        }
        fun acceptPart(part: Part): Boolean {
            var workflowName = "in"
            while (workflowName != "A" && workflowName != "R") workflowName = workflowMap.getValue(workflowName)(part)
            return workflowName == "A"
        }
        return parts.filter(::acceptPart).sumOf { (x, m, a, s) -> x + m + a + s }.toString()
    }

    override suspend fun part2(): String {
        fun IntRange.size(): Long = (last - first + 1).toLong()
        data class PartRange(val x: IntRange, val m: IntRange, val a: IntRange, val s: IntRange) {
            val combinations: Long = x.size() * m.size() * a.size() * s.size()
        }
        fun PartRange.splitOn(keepUpTo: Int, param: KProperty1<PartRange, IntRange>): Pair<PartRange?, PartRange?> {
            val first = param(this).first
            val last = param(this).last
            if (first > keepUpTo) return null to this
            else if (last <= keepUpTo) return this to null
            val left = first..keepUpTo
            val right = keepUpTo.inc()..last
            return when (param.name) {
                "x" -> copy(x = left) to copy(x = right)
                "m" -> copy(m = left) to copy(m = right)
                "a" -> copy(a = left) to copy(a = right)
                "s" -> copy(s = left) to copy(s = right)
                else -> error("Unexpected param name ${param.name}")
            }
        }

        fun getValidCombinations(workflowName: String, initialPartRange: PartRange): Long {
            if (workflowName == "A") return initialPartRange.combinations
            else if (workflowName == "R") return 0
            val workflow = workflows.first { (name) -> name == workflowName }
            val queue = mutableListOf(initialPartRange to 0)
            var sum = 0L
            while (queue.isNotEmpty()) {
                val (partRange, ruleIndex) = queue.removeFirst()
                if (ruleIndex == workflow.rules.size) sum += getValidCombinations(workflow.default, partRange)
                else {
                    val (propertyChar, op, comp, to) = workflow.rules[ruleIndex]
                    val param = PartRange::class.memberProperties.first { member -> member.name == propertyChar.toString() } as KProperty1<PartRange, IntRange>
                    val (left, right) = partRange.splitOn(if (op == '>') comp else comp.dec(), param)
                    val match = if (op == '>') right else left
                    val matchless = if (op == '>') left else right
                    if (matchless != null) queue.add(matchless to ruleIndex.inc())
                    if (match != null) sum += getValidCombinations(to, match)
                }
            }
            return sum
        }

        val partRange = PartRange(1..4000, 1..4000, 1..4000, 1..4000)
        return getValidCombinations("in", partRange).toString()
    }
}
