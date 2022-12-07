package xyz.acrylicstyle.anymixin.asm;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMCraftBukkitMain implements ASM {
    @Override
    public boolean canProcess(@NotNull String classname) {
        return classname.equals("org/bukkit/craftbukkit/Main$1");
    }

    @Override
    public void acceptClass(@NotNull String classname, @NotNull ClassReader cr, @NotNull ClassWriter cw) {
        cr.accept(new ClassVisitor(Opcodes.ASM9, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                        if (opcode == Opcodes.INVOKEVIRTUAL &&
                                "org/bukkit/craftbukkit/Main$1".equals(owner) &&
                                "acceptsAll".equals(name) &&
                                "(Ljava/util/List;Ljava/lang/String;)Ljoptsimple/OptionSpecBuilder;".equals(descriptor) &&
                                !isInterface) {
                            super.visitMethodInsn(opcode, owner, name, "(Ljava/util/Collection;Ljava/lang/String;)Ljoptsimple/OptionSpecBuilder;", false);
                        } else {
                            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                        }
                    }
                };
            }
        }, 0);
    }
}
