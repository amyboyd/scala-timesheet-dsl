package timesheet;

import scala.collection.mutable

final class Timesheet {
  private val tokens = mutable.Queue.empty[Token]

  def +=(token: Token): Unit = tokens += token

  def hasUnknowns = tokens.count(_.isInstanceOf[Unknown]) > 0

  def unknowns: Seq[Token] = tokens.filter(_.isInstanceOf[Unknown])

  def days: Seq[Token] = tokens.filter(_.isInstanceOf[Day])

  def activities: Seq[Activity] = {
    val activities = mutable.Queue.empty[Activity]
    var day: Day = days.head.asInstanceOf[Day]
    var time: Time = Time(0, 0)

    for (
      token <- tokens
      if !token.isInstanceOf[Unknown]
    ) {
      if (token.isInstanceOf[Day]) {
        day = token.asInstanceOf[Day]
        time = Time(0, 0)
      } else if (token.isInstanceOf[Time]) {
        time = time + token.asInstanceOf[Time]
      } else if (token.isInstanceOf[Task]) {
        activities += Activity(day, time, token.asInstanceOf[Task])
        time = Time(0, 0)
      }
    }

    activities
  }
}

object Timesheet {
  def createFromFreeFormText(input: String): Timesheet = {
    val sanitizedInput = input.
      // Ensure all lines end with closing the sentence, to make parsing "on xyz\nwednesday" easier.
      replaceAll("([^\\.])\n", "$1.\n").
      trim
    var inputParts = sanitizedInput.split("\\s+").toList
    val dayRE = """^(on )?(mon|tues|wednes|thurs|satur|sun)day[,:]?$""".r
    val numberOfHoursAndMinutesRE = """^([\d\.]+) ?(h|hours) ?(\d+) ?(m|mins|minutes)$""".r
    val numberOfHoursRE = """^([\d\.]+) ?(h|hours)$""".r
    val numberOfMinutesRE = """^(\d+) ?(m|mins|minutes)$""".r
    val taskInSentenceRE = """^(on )?([^,\.]+)(,|\.| and)$""".r

    val timesheet = new Timesheet

    while (inputParts.nonEmpty) {
      var found = false

      for (i <- 1 to inputParts.length) {
        if (!found) {
          val current = inputParts.take(i).mkString(" ")

          val token: Option[Token] = current.toLowerCase() match {
            case dayRE(_, day) => Some(Day(day.capitalize + "day"))

            case numberOfHoursAndMinutesRE(hours, _, minutes, _) => Some(Time(hours.toInt, minutes.toInt))

            case numberOfHoursRE(hours, _) => Some(new Time(hours.toFloat))

            case numberOfMinutesRE(minutes, _) => Some(new Time(0, minutes.toInt))

            case "all day" =>
              // Treat all day as 9am-5.30pm, with 1 hour taken away for lunch.
              Some(Time(7, 30))

            case "all morning" =>
              // Treat all morning as 9am-noon.
              Some(Time(3, 0))

            case "all afternoon" =>
              // Treat all afternoon as 1pm-5.30pm (assuming lunch is noon-1pm).
              Some(Time(4, 30))

            case taskInSentenceRE(_, task, _) =>
              val name = current.replaceFirst("^on ", "").replaceFirst("(,|\\.| and)$", "")
              Some(Task(name))

            case _ =>
              if (i == inputParts.length) {
                // At the end of the input - assume the last part of the input must be a task.
                val name = current.replaceFirst("^on ", "")
                Some(Task(name))
              } else {
                None
              }
          }

          if (token.isDefined) {
            timesheet += token.get
            inputParts = inputParts.drop(i)
            found = true
          }
        }
      }

      if (!found) {
        timesheet += Unknown(inputParts.head)
        inputParts = inputParts.drop(1)
      }
    }

    timesheet
  }
}
