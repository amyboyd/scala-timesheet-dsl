package timesheet;

sealed abstract class Token

case class Day(day: String) extends Token

case class Time(var hours: Int, var minutes: Int) extends Token {
  while (minutes >= 60) {
    minutes -= 60
    hours += 1
  }

  def this(hours: Float) = this(
    hours = Math.floor(hours).toInt,
    minutes = Math.round((hours - Math.floor(hours)) * 60).toInt
  )

  def +(other: Time) = Time(this.hours + other.hours, this.minutes + other.minutes)
}

case class Task(name: String) extends Token

case class Unknown(text: String) extends Token

case class Activity(day: Day, time: Time, task: Task)
