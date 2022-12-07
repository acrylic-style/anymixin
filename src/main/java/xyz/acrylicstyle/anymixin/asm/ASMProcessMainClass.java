package xyz.acrylicstyle.anymixin.asm;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import xyz.acrylicstyle.anymixin.AnyMixinTweaker;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ASMProcessMainClass implements ASM {
    private static final Set<String> CLASS_NAMES = new HashSet<>(Collections.singletonList("io/papermc/paperclip/Paperclip"));

    @Override
    public boolean canProcess(@NotNull String classname) {
        // true if classname is in CLASS_NAMES or is main class
        return CLASS_NAMES.contains(classname) || classname.startsWith(AnyMixinTweaker.launchTarget.replace('.', '/'));
    }

    @Override
    public void acceptClass(@NotNull String classname, @NotNull ClassReader cr, @NotNull ClassWriter cw) {
        cr.accept(new ClassVisitor(Opcodes.ASM9, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                        if (opcode == Opcodes.INVOKESPECIAL &&
                                "java/net/URLClassLoader".equals(owner) &&
                                "<init>".equals(name) &&
                                "([Ljava/net/URL;Ljava/lang/ClassLoader;)V".equals(descriptor) &&
                                !isInterface) {
                            // Invoke getClassLoader() and pushes LaunchClassLoader to the stack
                            super.visitMethodInsn(
                                    Opcodes.INVOKESTATIC,
                                    "net/minecraft/launchwrapper/Launch",
                                    "getClassLoader",
                                    "()Lnet/minecraft/launchwrapper/LaunchClassLoader;",
                                    false
                            );
                            super.visitInsn(Opcodes.DUP); // duplicate classloader
                            super.visitVarInsn(Opcodes.ALOAD, 1); // load and push URL[] to the stack
                            // Add classpath to LaunchClassLoader
                            super.visitMethodInsn(
                                    Opcodes.INVOKEVIRTUAL,
                                    "net/minecraft/launchwrapper/LaunchClassLoader",
                                    "addURLs",
                                    "([Ljava/net/URL;)V",
                                    false
                            );
                            // Initialize mixin environment
                            super.visitMethodInsn(
                                    Opcodes.INVOKESTATIC,
                                    "xyz/acrylicstyle/anymixin/AnyMixinTweaker",
                                    "initMixin",
                                    "()V",
                                    false
                            );
                            // astore_3
                        }
                    }
                };
            }
        }, 0);
    }
}
