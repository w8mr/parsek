package jafun

import kotlinx.coroutines.delay

data class Employee(val name: String, val age: Int, val salary: Int)
data class Manager(val name: String, val age: Int, val employees: List<Employee>)

fun test() {
    val managers = listOf(
        Manager("Jan", 22, listOf(
            Employee("Piet", 50, 30000),
            Employee("Bert", 39, 40000))),
        Manager("Klaas", 45, listOf(
            Employee("Ellen", 47, 45000),
            Employee("Wim", 37, 35000)
        )))

    println(managers.asSequence().filter { it.age>40 }.flatMap { it.employees }.filter { it.age>40 }.map { it.salary }.sum())

    val iterator = managers.iterator()
    val i = object : Iterator<Int> {
        val unknown = -1
        val empty = 0
        val stay = 1
        val next = 2

        var state = unknown
        var item : Int? = null

        var employeeIter : Iterator<Employee>? = null
        var employeeState = unknown

        private fun calc() {
            while (iterator.hasNext()) {
                val manager = iterator.next()
                if (!(manager.age<40)) continue
                if (employeeState == unknown) employeeIter = manager.employees.iterator()


            }
            state = empty
        }
        override fun hasNext(): Boolean {
            if (state == unknown) calc()
            return state == next
        }

        override fun next(): Int {
            while(true) {
                iterator.next()
            }
        }

    }
    fun next(iterator: Iterator<Manager>) {
    }
}

public fun main(args: Array<String>): Unit {
test() }

suspend fun sus() {
    println("test")
    delay(100L)
    println("test2")

}
