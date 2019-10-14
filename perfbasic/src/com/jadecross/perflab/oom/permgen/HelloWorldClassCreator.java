package com.jadecross.perflab.oom.permgen;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * <pre>
 * ASM을 이용하여 동적 클래스를 생성한다.
 * ####### 동적 클래스 샘플 START 
 * public class HelloWorld {
 * 	public void hello() {
 * 		System.out.println("12345");
 * 	}
 * }
 * ####### 동적 클래스 샘플 START
 * </pre>
 */
public class HelloWorldClassCreator implements Opcodes {
	public static byte[] dump(String className) throws Exception {
		ClassWriter cw = new ClassWriter(0);
		MethodVisitor mv;

		cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object", null);

		cw.visitSource(className + ".java", null);

		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(1, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
			mv.visitInsn(RETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "L" + className + ";", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "hello", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(6, l0);
			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			// mv.visitLdcInsn("bbbbb");
			mv.visitLdcInsn("Class : " + className + " Loaded...");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(7, l1);
			mv.visitInsn(RETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this", "L" + className + ";", null, l0, l2, 0);
			mv.visitMaxs(2, 1);
			mv.visitEnd();
		}
		cw.visitEnd();

		return cw.toByteArray();
	}
}