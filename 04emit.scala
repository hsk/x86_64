package ccc 
object emit {

  def main(argv:Array[String]) {
    // 1を出力するプログラム
    emit("e.s", List(
      ("_main",List(
        ("movl", "$1", "%edi"), // スタックに1をつむ
        ("call", "_printInt",List()) // printInt関数を呼び出す
      ))
    ))
    exec("gcc -m64 -o e e.s ccc/lib.c")
  }

  def apply(filename:String, ls:List[Any]) {
    asm.open(filename)
    ls.foreach {
      case (name:String,body:List[Any]) =>
        asm(".globl "+name)
        asm(name+":")
        asm("\tpushq\t%rbp")
        asm("\tmovq\t%rsp, %rbp")
        def f(e:Any):Any = e match {
          case ("movl",a,b) => asm("movl "+a+", "+b)
          case ("subq",a,b) => asm("subq "+a+", "+b)
          case ("addl",a,b,c) =>
            asm("movl "+a+", %eax")
            asm("addl "+b+", %eax")
            asm("movl %eax, "+c)
          case ("call", n, b:List[Any]) => prms(b, regs); asm("call "+n)
          case ("ret", a) =>
            asm("movl "+a+", %eax")
            asm("leave")
            asm("ret")
          case ("ifeq", a, b, c:List[Any], d:List[Any]) =>
            val id_else = genid("id_else")
            val id_cont = genid("id_cont")
            asm("movl "+a+", %eax")
            asm("cmpl "+b+", %eax")
            asm("jne "+id_else)
            c.foreach(f)
            asm(id_else+":")
            asm("jmp "+id_cont)
            c.foreach(f)
            asm(id_cont+":")
        }
        body.foreach(f)
        asm("\tleave")
        asm("\tret")
    }
    asm.close()
  }
  
  val regs = List("%edi","%esi", "%edx")
  def prms(ps:List[Any],rs:List[Any]) {
    (ps,rs) match {
      case (List(),_) =>
      case (p::ps,r::rs) =>
        asm("movl "+p+", "+r)
        prms(ps, rs)
    }
  }
}
