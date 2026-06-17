package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver>{
    val driversWithTrips = trips.map { it.driver }.toSet()
    return allDrivers.filter { it !in driversWithTrips }.toSet()
}
/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger>
    {
        val passengerTripCounts = trips.flatMap { it.passengers }.groupBy { it }.mapValues { it.value.size }
        return allPassengers.filter { (passengerTripCounts[it] ?: 0) >= minTrips }.toSet()
    }

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger>{
    return trips.filter { it.driver == driver }
        .flatMap { it.passengers }
        .groupBy { it }
        .filter { it.value.size > 1 }
        .keys
}
        

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    return allPassengers.filter { passenger ->
        val passengerTrips = trips.filter { passenger in it.passengers }
        val (withDiscount, withoutDiscount) = passengerTrips.partition { it.discount != null }
        withDiscount.size > withoutDiscount.size
    }.toSet()
}
        

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    if (trips.isEmpty()) return null
    val mostFrequentBucket = trips.groupBy { it.duration / 10 }
        .maxBy { it.value.size }?.key ?: return null
    val start = mostFrequentBucket * 10
    return start..(start + 9)
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty()) return false
    val totalIncome = trips.map { it.cost }.sum()
    val sortedDriverIncomes = trips.groupBy { it.driver }
        .mapValues { (_, driverTrips) -> driverTrips.map { it.cost }.sum() }
        .values
        .sortedDescending()
    val numberOfDriversToCount = (allDrivers.size * 0.2).toInt()
    val topDriversIncome = sortedDriverIncomes.take(numberOfDriversToCount).sum()
    return topDriversIncome >= totalIncome * 0.8
}