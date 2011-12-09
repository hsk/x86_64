package ccc
import java.io._
object main {
  def main(argv:Array[String]) {
    val prg = exec.readAll(new FileInputStream(argv(0)))
    val st = parse(prg)
    val ast = st2ast(st)
    val s = setmem(ast)
    val e = expand(s)
    val m = memAlloc(e)
    emit("e.s", m)
    exec("gcc -m64 -o e e.s ccc/lib.c")
  }
}
