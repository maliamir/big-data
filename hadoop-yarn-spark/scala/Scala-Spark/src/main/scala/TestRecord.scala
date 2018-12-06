class TestRecord {

  private var logicalName = ""
  private var duration = 0L
  private var occurrences = 0

  def this(logicalName: String) {
    this()
    this.logicalName = logicalName
  }

  def getLogicalName: String = logicalName

  def addDuration(duration: Long): Unit = {
    this.duration += duration
    this.occurrences += 1
  }

  def getDuration: Long = duration

  def getOccurrences: Integer = occurrences

}