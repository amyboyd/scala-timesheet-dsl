import collection.mutable.Stack
import org.scalatest._
import scala.collection.mutable
import timesheet._

class TimesheetSpec extends FlatSpec {
  val timesheet = Timesheet.createFromFreeFormText(
    """
    Monday: 4.5h Project Alpha, 5 hours 20 minutes on Project Beta, 1h10m on training and 1.33h on other things.
    On Tuesday, all morning on Project Beta planning and all afternoon on training
    wednesday: all day on project beta and 10 minutes on training
    saturday 10m project Beta and 20 minutes training
    """
  )

  "Timesheet" should "parse a human-readable string into a Timesheet instance" in {
    assert(timesheet.activities.length === 10)
    assert(timesheet.activities(0) === Activity(Day("Monday"), Time(4, 30), Task("Project Alpha")))
    assert(timesheet.hasUnknowns === false)
  }

  it should "parse the days and capitalize them" in {
    assert(timesheet.days === mutable.Queue(Day("Monday"), Day("Tuesday"), Day("Wednesday"), Day("Saturday")))
  }
}
