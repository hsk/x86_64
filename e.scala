package ccc
object e {
  def main(argv:Array[String]) {
    asm.open("e.s")
    asm("""
	.text
.globl _add
_add:
	pushq	%rbp
	movq	%rsp, %rbp
	movl	%edi, -4(%rbp)
	movl	%esi, -8(%rbp)
	movl	-8(%rbp), %eax
	addl	-4(%rbp), %eax
	leave
	ret
	.cstring
LC0:
	.ascii "%d\12\0"
	.text
.globl _main
_main:
	pushq	%rbp
	movq	%rsp, %rbp
	movl	$2, %esi
	movl	$1, %edi
	call	_add
	movl	%eax, %esi
	leaq	LC0(%rip), %rdi
	movl	$0, %eax
	call	_printf
	leave
	ret
    """)
    asm.close()
    exec("gcc e.s -o e")
  }
}

