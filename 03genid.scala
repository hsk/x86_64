package ccc 
object genid {
  var counter = 0
  def apply(s:String):String = {
    counter += 1
    s + counter
  }
  def main(argv:Array[String]) {
    println(genid("a"))
    println(genid("a"))
    println(genid("a"))
  }
}
