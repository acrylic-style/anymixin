package xyz.acrylicstyle.anymixin;

import net.blueberrymc.nativeutil.NativeUtil;
import net.minecraft.launchwrapper.Launch;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class AnyMixin {
    private static String agentArgs;
    private static Instrumentation inst;
    private final String jarPath;
    private final String[] args;

    public AnyMixin(@NotNull String jarPath, @NotNull String @NotNull [] args) {
        this.jarPath = jarPath;
        this.args = args;
    }

    public void run() throws Exception {
        NativeUtil.appendToSystemClassLoaderSearch(jarPath);
        Manifest manifest;
        try (JarFile file = new JarFile(new File(jarPath))) {
            manifest = new Manifest(file.getInputStream(file.getJarEntry("META-INF/MANIFEST.MF")));
        }
        try {
            String launcherAgentClass = manifest.getMainAttributes().getValue("Launcher-Agent-Class");
            if (launcherAgentClass != null) {
                Class<?> clazz = Class.forName(launcherAgentClass);
                clazz.getMethod("agentmain", String.class, Instrumentation.class).invoke(null, agentArgs, inst);
            }
        } catch (Exception ignore) {}
        String mainClass = manifest.getMainAttributes().getValue("Main-Class");
        if (mainClass == null) {
            throw new IllegalStateException("Main-Class not found");
        }
        AnyMixinTweaker.launchTarget = mainClass;
        launch();
    }

    private void launch() {
        String[] newArgs = new String[args.length + 1];
        newArgs[0] = "--tweakClass=xyz.acrylicstyle.anymixin.AnyMixinTweaker";
        System.arraycopy(args, 0, newArgs, 1, args.length);
        AnyMixinTweaker.args = newArgs;
        Launch.main(newArgs);
    }

    public static void main(String[] args) throws Exception {
        if (inst == null) {
            System.out.println("Instrumentation API is unavailable");
            return;
        }
        if (args.length == 0) {
            System.out.println("Usage: java -jar anymixin.jar <path to jar file>");
            return;
        }
        int srcPos = 1;
        if (args[0].equals("-jar")) {
            // for launcher which doesn't support injecting arguments after -jar but allows injecting arguments before -jar
            srcPos = 2;
        }
        String[] jarArgs = new String[args.length - srcPos];
        System.arraycopy(args, srcPos, jarArgs, 0, jarArgs.length);
        new AnyMixin(args[0], jarArgs).run();
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        AnyMixin.agentArgs = agentArgs;
        AnyMixin.inst = inst;
    }
}
