.globl _main
_main:
	pushq	%rbp
	movq	%rsp, %rbp
movl $1, %edi
call _printInt
	leave
	ret
