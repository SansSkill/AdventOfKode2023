package days

@Suppress("unused")
object D20 : Day {
    private data class Pulse(val from: String, val to: String, val isHigh: Boolean)
    private abstract class Module {
        abstract val name: String
        abstract val destinations: List<String>
        abstract fun handlePulse(pulse: Pulse): List<Pulse>
        abstract fun reset(): Unit
    }
    private data class BroadcastModule(override val destinations: List<String>): Module() {
        override val name: String = "broadcaster"
        override fun handlePulse(pulse: Pulse): List<Pulse> = destinations.map { s -> Pulse(name, s, pulse.isHigh) }
        override fun reset(): Unit { }
    }
    private data class FlipFlopModule(
        override val name: String,
        override val destinations: List<String>,
    ): Module() {
        private var isOn: Boolean = false
        override fun handlePulse(pulse: Pulse): List<Pulse> =
            if (pulse.isHigh) emptyList()
            else {
                isOn = !isOn
                destinations.map { s -> Pulse(name, s, isOn) }
            }
        override fun reset(): Unit { isOn = false }
    }
    private data class ConjunctionModule(
        override val name: String,
        override val destinations: List<String>,
        private val inputs: List<String>
    ): Module() {
        private val inputStateMap = inputs.associateWith { false }.toMutableMap()
        override fun handlePulse(pulse: Pulse): List<Pulse> {
            inputStateMap[pulse.from] = pulse.isHigh
            val isHigh = !inputStateMap.values.all { b -> b }
            return destinations.map { s -> Pulse(name, s, isHigh) }
        }

        override fun reset(): Unit {
            inputStateMap.keys.forEach { key -> inputStateMap[key] = false }
        }
    }

    private val modules: List<Module>
    private val moduleMap: Map<String, Module>

    init {
        val moduleLines = readFile().map { s ->
            val (l, r) = s.split(" -> ")
            l to r.split(", ")
        }
        modules = moduleLines.map { (l, destinations) ->
            if (l == "broadcaster") return@map BroadcastModule(destinations)
            when (val char = l[0]) {
                'b' -> BroadcastModule(destinations)
                '%' -> FlipFlopModule(l.substring(1), destinations)
                '&' -> {
                    val name = l.substring(1)
                    val inputs = moduleLines.filter { (_, destinations) -> name in destinations }
                        .map { (name, _) -> if (name[0] == 'b') name else name.substring(1) }
                    ConjunctionModule(name, destinations, inputs)
                }
                else -> error("Unexpected first module name char $char")
            }
        }
        moduleMap = modules.associateBy(Module::name)
    }

    override suspend fun part1(): String {
        fun press(): Pair<Long, Long> {
            var low = 0L
            var high = 0L
            val queue = mutableListOf(Pulse("!button", "broadcaster", false))
            while (queue.isNotEmpty()) {
                val pulse = queue.removeAt(0)
                if (pulse.isHigh) high++ else low++
                moduleMap[pulse.to]?.let { module -> queue.addAll(module.handlePulse(pulse)) }
            }
            return low to high
        }
        var low = 0L
        var high = 0L
        repeat(1000) {
            val (l, r) = press()
            low += l
            high += r
        }
        return low.times(high).toString()
    }

    // TODO: FIX, code is correct but too slow to return
    override suspend fun part2(): String {
        fun press(): Boolean {
            val queue = mutableListOf(Pulse("!button", "broadcaster", false))
            while (queue.isNotEmpty()) {
                val pulse = queue.removeAt(0)
                val module =  moduleMap[pulse.to]
                if (module != null) queue.addAll(module.handlePulse(pulse))
                else if (!pulse.isHigh) return true
            }
            return false
        }
        var count = 0L
        do { count++ } while (!press())
        return count.toString()
    }
}