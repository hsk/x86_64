.globl _main
_main:
	pushq	%rbp
	movq	%rsp, %rbp
subq $16, %rsp
movl $3, -4(%rbp)
movl $2, -8(%rbp)
movl $1, -12(%rbp)
movl -4(%rbp), %edi
movl -8(%rbp), %esi
movl -12(%rbp), %edx
call _add
movl %eax, %edi
call _printInt
	leave
	ret
.globl _add
_add:
	pushq	%rbp
	movq	%rsp, %rbp
subq $32, %rsp
movl %edx, -4(%rbp)
movl %esi, -8(%rbp)
movl %edi, -12(%rbp)
movl -12(%rbp), %eax
addl -8(%rbp), %eax
movl %eax, -16(%rbp)
movl -16(%rbp), %eax
addl -4(%rbp), %eax
movl %eax, -20(%rbp)
movl -20(%rbp), %eax
leave
ret
	leave
	ret
