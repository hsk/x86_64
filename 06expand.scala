package ccc
object expand {

  def main(argv:Array[String]) {
    val prg = List(
      ("_main", List(), List(
        ("mov", 10, "a"),
        ("mov", 1, "b"),
        ("mov", 2, "c"),
        ("mov", ("call", "_printInt", List(("add", "a", ("add", "b", "c")))),"d"),
        ("ret","d")
      )),
      ("_add", List("a","b"), List(
        ("ret", ("add","a","b"))
      ))
    )
    val p = expand(prg)
    println("p="+p)
    val m = memAlloc(p)
    println("m="+m)
    emit("e.s", m)
    exec("gcc -m64 -o e e.s ccc/lib.c")
  }

  def apply(p:List[Any]):List[Any] = {
    p.map {
      case (n,a:List[String],b:List[Any]) =>
        val ll = b.foldLeft(argv(a,regs)){
          case (l,b)=>
            val (l2, id) = f(l,b)
            l2
        } 
        (n,ll.reverse)
    }
  }

  def argv(as:List[String], rs:List[Any]):List[Any] = {
    (as, rs) match {
      case (List(), rs) => List()
      case (a::as, r::rs) => ("movl", r, a)::argv(as, rs)
    }
  }

  val regs = List("%edi", "%esi", "%edx", "%ecx", "%r8d", "%r9d")
  def f(l:List[Any],e:Any):(List[Any],String) = e match {
    case ("add", a, b) =>
      val id = genid("ex_")
      val (la, a1) = f(l, a)
      val (lb, b1) = f(la, b)
      (("addl", a1, b1, id)::lb,id)
    case ("mov", a:String, id:String) => (("movl", a, id)::l, id)
    case ("mov", a:Int, id:String) => (("movl", ("$"+a),id)::l, id)
    case ("mov", a, id:String) =>
      val (l2, id1) = f(l, a)
      (("movl", id1, id)::l2, id)
    case ("call", a, b:List[Any]) =>
      var (la,ids) = b.foldLeft((l,List[String]())){
        case ((l,ids),b)=>
          val (l2,id) = f(l, b)
          (l2,id::ids)
      }
      (("call", a, ids)::la, "%eax")
    case ("if", a, b:List[Any], c:List[Any]) =>
      val (lb,bid) = b.foldLeft(List[Any](), null:String) {
        case ((l,a),b) => f(l, b)
      }
      val (lc,cid) = c.foldLeft(List[Any](), null:String) {
        case ((l,a),b) => f(l, b)
      }
      val (la,aid) = f(l,a)
      (("ifeq", aid, "$0",lb,lc)::la, null)
    case ("ret", e) =>
      val (l2, id) = f(l, e)
      (("ret", id)::l2, id)
    case id:String => (l, id)
    case e => (e::l, null)
  }

}
