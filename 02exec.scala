package ccc 
import java.io._
object exec {
  def apply(cmd:String):Int = {
    val p = Runtime.getRuntime().exec(cmd)
    print(readAll(p.getInputStream()))
    print(readAll(p.getErrorStream()))
    p.waitFor()
  }
  def readAll(p:InputStream):String = {
    def f(s:String, i:BufferedReader):String = {
      i.readLine() match {
        case null => s
        case a =>  f(s+a+"\n", i)
      }
    }
    f("",new BufferedReader(new InputStreamReader(p)))
  }
  def main(argv:Array[String]) {
    exec("ls")
  } 
}
