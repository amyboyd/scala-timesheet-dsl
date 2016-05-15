import collection.mutable.Stack
import org.scalatest._
import scala.collection.mutable
import timesheet.Time

class TimeSpec extends FlatSpec {
  "Time" should "store hours and minutes" in {
    val time = new Time(3, 30)
    assert(time.hours === 3)
    assert(time.minutes === 30)
  }

  it should "convert 60 minutes into one hour" in {
    val time = new Time(3, 60)
    assert(time.hours === 4)
    assert(time.minutes === 0)
  }

  it should "convert > 60 minutes into hours" in {
    val time = new Time(3, 186)
    assert(time.hours === 6)
    assert(time.minutes === 6)
  }

  it should "convert a float of hours into hours and minutes" in {
    val time1 = new Time(2.5f)
    assert(time1.hours === 2)
    assert(time1.minutes === 30)

    val time2 = new Time(2.33f)
    assert(time2.hours === 2)
    assert(time2.minutes === 20)
  }

  it should "add two times together" in {
    val time1 = new Time(2.5f)

    val time2 = new Time(2.66f)

    val sum = time1 + time2
    assert(sum.hours === 5)
    assert(sum.minutes === 10)
  }

  it should "be able to chain additions together" in {
    val sum =
      new Time(2.5f) +
      new Time(2.66f) +
      new Time(4, 30) +
      new Time(1, 0)
    assert(sum.hours === 10)
    assert(sum.minutes === 40)
  }
}
