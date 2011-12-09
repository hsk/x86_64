package ccc

object st2ast {
  def main(argv:Array[String]) {
    val st =
    (("main","=",("fun","(","void",")",
      ("{",("printInt","(",("add","(",(1,",",(2,",",3)),")"),")"),"}")
    )),
    "@",
    ("add","=",("fun","(",("a",",",("b",",","c")),")",
      ("a","+",("b","+","c")))
    ))
    val ast = st2ast(st)
    println("ast="+ast)
    val s = setmem(ast)
    val e = expand(s)
    val m = memAlloc(e)
    emit("e.s", m)
    exec("gcc -m64 -o e e.s ccc/lib.c")
  }

  def apply(st:Any):List[Any] = st match {
    case (a,"@",b) => f(a)::List(f(b))
  }

  def f(fn:Any):Any = fn match {
    case (n,"=",("fun","(",a,")",b)) => ("_"+n, params(a), bodys(b))
  }

  def params(e:Any):List[Any] = e match {
    case (a,",",b) => params(a):::params(b)
    case "void"=>List()
    case a => List(a)
  }
  def fargs(e:Any):List[Any] = e match {
    case (a,",",b) => fargs(a):::fargs(b)
    case a => List(exp(a))
  }

  def exp(e:Any):Any = e match {
    case ("{",b,"}") => bodys(b)
    case ("(",b,")") => exp(b)
    case (a,"(",b,")") => ("call","_"+a,fargs(b))
    case (a,"=",b) => ("mov", exp(b), exp(a))
    case (a,"+",b) => ("add",exp(a), exp(b))
    case ("return", a) => ("ret", exp(a))
    case (a,";") => exp(a)
    case a:Int => a
    case a:String => a
  }
  def bodys(e:Any):List[Any] = e match {
    case (a,"@",b) => bodys(a):::bodys(a)
    case a =>
      exp(a) match {
        case e:List[Any] => e
        case a => List(a)
      }
  }
}

