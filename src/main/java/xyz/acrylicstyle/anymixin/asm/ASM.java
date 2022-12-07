package xyz.acrylicstyle.anymixin.asm;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public interface ASM {
    boolean canProcess(@NotNull String classname);

    default byte @Nullable [] accept(@NotNull String classname, byte @NotNull [] bytes) {
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        acceptClass(classname, cr, cw);
        dumpClass(classname, cw);
        return cw.toByteArray();
    }

    default void acceptClass(@NotNull String classname, @NotNull ClassReader cr, @NotNull ClassWriter cw) {
        throw new UnsupportedOperationException(this.getClass().getTypeName() + " must implement acceptClass or accept");
    }

    default void dumpClass(@NotNull String classname, @NotNull ClassWriter cw) {
        // create directory
        File outDir = new File(".anymixin/out", classname).getParentFile();
        if (!outDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            outDir.mkdirs();
        }

        // dump class file
        String[] fqn = classname.split("/");
        try (BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(new File(outDir, fqn[fqn.length - 1] + ".class").toPath()))) {
            out.write(cw.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
