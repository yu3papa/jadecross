	.file	"02.option-S.c"
	.globl	a
	.data
	.align 4
	.type	a, @object
	.size	a, 4
a:
	.long	10
	.comm	b,4,4
	.globl	a2
	.align 4
	.type	a2, @object
	.size	a2, 4
a2:
	.long	20
	.comm	b2,4,4
	.text
	.globl	f1
	.type	f1, @function
f1:
.LFB2:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	movl	%edi, -4(%rbp)
	addl	$1, -4(%rbp)
	movl	-4(%rbp), %eax
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE2:
	.size	f1, .-f1
	.section	.rodata
.LC0:
	.string	"hello"
	.align 8
.LC1:
	.string	"a:%d, a2:%d, b:%d, b2:%d, c:%d, c2:%d\n"
	.text
	.globl	main
	.type	main, @function
main:
.LFB3:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$48, %rsp
	movl	$100, -4(%rbp)
	movq	$.LC0, -16(%rbp)
	movl	b2(%rip), %edi
	movl	b(%rip), %ecx
	movl	a2(%rip), %edx
	movl	a(%rip), %eax
	movl	-4(%rbp), %r8d
	movl	-20(%rbp), %esi
	movl	%esi, (%rsp)
	movl	%r8d, %r9d
	movl	%edi, %r8d
	movl	%eax, %esi
	movl	$.LC1, %edi
	movl	$0, %eax
	call	printf
	movl	a(%rip), %eax
	movl	%eax, %edi
	call	f1
	movl	%eax, b(%rip)
	movl	a2(%rip), %eax
	movl	%eax, %edi
	call	f2
	movl	%eax, b2(%rip)
	movl	-4(%rbp), %eax
	movl	%eax, %edi
	call	f1
	movl	%eax, -20(%rbp)
	movl	b2(%rip), %edi
	movl	b(%rip), %ecx
	movl	a2(%rip), %edx
	movl	a(%rip), %eax
	movl	-4(%rbp), %r8d
	movl	-20(%rbp), %esi
	movl	%esi, (%rsp)
	movl	%r8d, %r9d
	movl	%edi, %r8d
	movl	%eax, %esi
	movl	$.LC1, %edi
	movl	$0, %eax
	call	printf
	movl	$0, %eax
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE3:
	.size	main, .-main
	.globl	f2
	.type	f2, @function
f2:
.LFB4:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	movl	%edi, -4(%rbp)
	subl	$1, -4(%rbp)
	movl	-4(%rbp), %eax
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE4:
	.size	f2, .-f2
	.ident	"GCC: (GNU) 4.8.5 20150623 (Red Hat 4.8.5-39)"
	.section	.note.GNU-stack,"",@progbits
