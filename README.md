Timesheet DSL
=============

This is a project to parse a human-readable string into a structured timesheet, written in Scala.

Example
-------

For an up-to-date reference, view the test files in `src/test/scala`.

An example timesheet may be this string:

```
Monday: 4.5h Project Alpha, 5 hours 20 minutes on Project Beta, 1h10m on training and 1.33h on other things.
On Tuesday, all morning on Project Beta planning and all afternoon on training
wednesday: all day on project beta and 10 minutes on training
saturday 10m project Beta and 20 minutes training
```

Calling `Timesheet.createFromFreeFormText` will parse this string into this:


```
Queue(
    Activity(Day(Monday),Time(4,30),Task(Project Alpha)),
    Activity(Day(Monday),Time(5,20),Task(Project Beta)),
    Activity(Day(Monday),Time(1,10),Task(training)),
    Activity(Day(Monday),Time(1,20),Task(other things)),
    Activity(Day(Tuesday),Time(3,0),Task(Project Beta planning)),
    Activity(Day(Tuesday),Time(4,30),Task(training)),
    Activity(Day(Wednesday),Time(7,30),Task(project beta)),
    Activity(Day(Wednesday),Time(0,10),Task(training)),
    Activity(Day(Saturday),Time(0,10),Task(project Beta)),
    Activity(Day(Saturday),Time(0,20),Task(training))
)
```
